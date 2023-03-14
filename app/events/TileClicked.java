package events;


import com.fasterxml.jackson.databind.JsonNode;


import actions.PerformAction;

import akka.actor.ActorRef;

import commands.BasicCommands;

import structures.GameState;
import structures.basic.*;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import static actions.PerformAction.moveUnit;

import java.util.ArrayList;
import java.util.logging.Handler;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * {
 * messageType = “tileClicked”
 * tilex = <x index of the tile>
 * tiley = <y index of the tile>
 * }
 *
 * @author Dr. Richard McCreadie
 */

public class TileClicked implements EventProcessor {

    public static Tile startTile; // start tile
    public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made

    @Override

    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


    	
        if (gameState.isGameActive) // if the frontend connection is active
        {
            cardClick=message.get("messagetype");//message to keep track of previous click on front-end
            int tilex = message.get("tilex").asInt();
            int tiley = message.get("tiley").asInt();
            Tile clickedTile = gameState.board.returnTile(tilex, tiley); // clicked tile object

            if (gameState.player1Turn == true) // Player 1 clicked the tile
            {
                if(gameState.clickMessage.asText().equals("cardclicked") ){//summoning
                    summonCard(out,gameState,clickedTile,gameState.player1);
                    gameState.clickMessage=cardClick;//updating the new click to tile clicked
                }
                else if(cardClick.asText().equals("tileclicked")){

                    highlightAndMove(out, gameState, clickedTile, gameState.player1); // add turns

                }
                else if(gameState.clickMessage.asText().equals("initalize")){
                    gameState.clickMessage=cardClick;//updating the new click to tile clicked
                    highlightAndMove(out, gameState, clickedTile, gameState.player1);
                }

            }
            else {
            	//Since no highlighting is needed for player 2, no need of performing clearhighlighting
            	if(message.get("action")!=null)
            	{
            		tilex = message.get("start_tilex").asInt();
                    tiley = message.get("start_tiley").asInt();
                    startTile = gameState.board.returnTile(tilex, tiley); // clicked tile object

            		// Get the unit index from the summoned arraylist position
                    int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);

	                // Check for the action code and perform step
	            	if(message.get("action").asText().equalsIgnoreCase(AppConstants.move)) // Movement only
	            	{

	                    // Move unit
	                    PerformAction.moveUnit(0,out, startTile, clickedTile, gameState);

	                    // Update unit movement status
	                    gameState.summonedUnits.get(unitIdx).setMoved(true);

	                    AppConstants.callSleep(200);


	            	}else if(message.get("action").asText().equalsIgnoreCase(AppConstants.attack)) // Movement only
	            	{
	            		 boolean attackStatus=false;

	                     attackStatus=PerformAction.attackUnit(0,gameState.player2,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
	                     if(unitIdx<gameState.summonedUnits.size() && unitIdx>-1)
	                     {
	                    	 if(gameState.summonedUnits.get(unitIdx)!=null && unitIdx<gameState.summonedUnits.size())
	                    		 gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
	                     }
	                     
	            	}else if(message.get("action").asText().equalsIgnoreCase(AppConstants.drawCardSummon)) // Summon unit
	            	{
	            		gameState.handPosClicked=message.get("position").asInt();
	            		
            			AppConstants.printLog("<-------- AI :: summoning :: gameState.handPosClicked: "+gameState.handPosClicked);

            	        Card handCard = gameState.player2.getCardByHandPos(gameState.handPosClicked-1);//getting the card by hand position
            			AppConstants.printLog("<-------- AI :: summoning :: handCard: "+handCard.getCardname());

            		
            			if (handCard.getCardname().equals("Staff of Y'Kir'")) {
                            BasicCommands.addPlayer1Notification(out, "Playing spell: Staff of Y'Kir", 2);
            		            Spell.staffOfYKir(out, handCard, startTile, gameState);

           		        }else if (handCard.getCardname().equals("Entropic Decay")) {
                            BasicCommands.addPlayer1Notification(out, "Playing spell: Entropic Decay", 2);
           		            Spell.entropicDecay(out, handCard, clickedTile, gameState);

            		    }else {
                            // Summon the unit
                            summonCard(out,gameState,clickedTile,gameState.player2);
            		    }
	            		
	            	}
            	}
            	 startTile=null;

            }


        }


    }

    /** Method checks if the tile click is for the first time or second
     * if it is first time click,
     * 			- get the click unit object
     * 			- if the unit is not moved and attacked yet, highlight movable and attackable tiles
     * 			- if the unit has already moved, highlight attackable tiles only
     * 			- if the unit has already attacked, no valid moves left
     * if it is second time click,
     * 			- Move the unit,if the clicked tile is empty
     * 			- if the clicked tile isnot empty, attack the unit given that's not a firendly unit
     * @param out
     * @param gameState
     * @param clickedTile
     * @param player
     */

    private void highlightAndMove(ActorRef out, GameState gameState, Tile clickedTile, Player player) {

        if (startTile == null) { // if the start tile hasn't been set yet
            Unit selectedUnit = clickedTile.getUnitFromTile(); // get the unit from the clicked tile
            
            AppConstants.callSleep(100);
            
            if (selectedUnit != null) { // if the unit is not null
                startTile = clickedTile; // set the start tile to the clicked tile
                
                // Get the unit index from the summoned arraylist position
                int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);

                // first check for provoke to stop movement
                if (SpecialAbilities.getProvokingUnits(out, gameState, TileClicked.opposingPlayer(gameState, player)) != null){
                    for (Unit unit: SpecialAbilities.getProvokingUnits(out, gameState, TileClicked.opposingPlayer(gameState, player))) {
                        SpecialAbilities.provoke(out,gameState, player, unit);
                    }
                }

                // checks for ranged units and highlights all enemy units
                if (startTile.getUnitFromTile().getName().equals("Fire Spitter") || startTile.getUnitFromTile().getName().equals("Pyromancer"))
                {

                    if (startTile.getUnitFromTile().isProvoked()==true){

                        ArrayList<Tile> tiles = getProvokerTiles(out, gameState, player);
                        BasicCommands.addPlayer1Notification(out, "Unit provoked!", 2);
                        gameState.board.highlightTilesRed(out, tiles);
                    }
                    else if (startTile.getUnitFromTile().getMoved() == false && startTile.getUnitFromTile().getAttacked() == false){

                        gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTiles(out, startTile));
                        gameState.board.highlightTilesRed(out, gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), opposingPlayer(gameState,player)));}
                    else if (startTile.getUnitFromTile().getAttacked()==false && startTile.getUnitFromTile().getMoved()==true){

                        gameState.board.highlightTilesRed(out, gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), opposingPlayer(gameState,player)));
                    }
                    else if (startTile.getUnitFromTile().getAttacked()==true && startTile.getUnitFromTile().getMoved()==false){ //Unit has already moved or attacked
                        BasicCommands.addPlayer1Notification(out, "Exhausted!", 2);}


                    else {
                        BasicCommands.addPlayer1Notification(out, "Exhausted!", 2);}
                }



                // If the unit is Azurite Lion or Serpenti, implement Attack Twice logic
                else if(gameState.summonedUnits.get(unitIdx).getId() == 7 || gameState.summonedUnits.get(unitIdx).getId() == 17 || gameState.summonedUnits.get(unitIdx).getId() == 26 || gameState.summonedUnits.get(unitIdx).getId() == 36) 
                {
                	
                	// If the unit has attacked twice already
            		if(gameState.summonedUnits.get(unitIdx).getAttackedTwice() == true) {
                        BasicCommands.addPlayer1Notification(out, "Exhausted!", 2);

            		}
                	// If the unit has not moved or attacked once yet
            		else if(gameState.summonedUnits.get(unitIdx).getMoved() == false && gameState.summonedUnits.get(unitIdx).getAttackedOnce() == false) {
                    	gameState.board.highlightTilesMoveAndAttack(1,player,out, startTile,gameState); // highlight tiles to move and attack
                    // If the unit has attacked once but not moved
                	}else if(gameState.summonedUnits.get(unitIdx).getAttackedOnce() == true && gameState.summonedUnits.get(unitIdx).getMoved() == false) {
                    	gameState.board.highlightTilesMoveAndAttack(1,player,out, startTile,gameState); // highlight tiles to move and attack
                	}
                	// If the unit has moved but has not attacked once yet
                	else if(gameState.summonedUnits.get(unitIdx).getMoved() == true && gameState.summonedUnits.get(unitIdx).getAttackedOnce() == false) {
                    	gameState.board.highlightTilesRed(out, gameState.board.getAdjacentTilesToAttack(player,out, startTile)); // highlight tiles to attack only
                	// If the unit has moved and has attacked once already but not twice yet
                	}else if(gameState.summonedUnits.get(unitIdx).getMoved() == true && gameState.summonedUnits.get(unitIdx).getAttackedOnce() == true && gameState.summonedUnits.get(unitIdx).getAttackedTwice() == false) {
                     	gameState.board.highlightTilesRed(out, gameState.board.getAdjacentTilesToAttack(player,out, startTile)); // highlight tiles to attack only
                	}
                }	
                
                // Else, proceed normally
                // Check whether that unit has moved or attacked in the turn
                else if (gameState.summonedUnits.get(unitIdx).getMoved()==false && gameState.summonedUnits.get(unitIdx).getAttacked()==false) // Unit hasn't moved or attacked yet
                {
                	// Unit not moved or attacked yet
                	gameState.board.highlightTilesMoveAndAttack(1,player,out, startTile,gameState); // highlight tiles to move and attack

                }else if(gameState.summonedUnits.get(unitIdx).getAttacked()==false && gameState.summonedUnits.get(unitIdx).getMoved()==true){
                	// Unit has moved,but not attacked yet

                    // checking if unit is provoked and highlighting only the provoking enemy
                    if(gameState.summonedUnits.get(unitIdx).isProvoked()){
                        ArrayList<Tile> tiles = getProvokerTiles(out, gameState, player);
                        BasicCommands.addPlayer1Notification(out, "Unit provoked!", 2);
                        gameState.board.highlightTilesRed(out, tiles);
                    }
                    else {
                        gameState.board.highlightTilesRed(out, gameState.board.getAdjacentTilesToAttack(player, out, startTile)); // highlight tiles to attack only
                    }
                	
                }else {
                	//Unit has already moved or attacked
                    BasicCommands.addPlayer1Notification(out, "Exhausted!", 2);
                }
            
            } else {
                BasicCommands.addPlayer1Notification(out, "Please select a unit to move or attack! ", 2); // if the unit is null

            }
            
            AppConstants.callSleep(100);

        } else if (startTile.getUnitFromTile()!=null && startTile.getUnitFromTile().getIsPlayer() == player.getID()){ // Second click moves the unit to the clicked tile or attack

        	// Get the unit index from the summoned arraylist position
            int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);

            // clear the highlighting once move is clicked
            gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
            AppConstants.callSleep(200);
            
            // checks if the unit is fire Spitter , if so trigger ranged attack
            if(clickedTile.getUnitFromTile()!=null && gameState.summonedUnits.get(unitIdx).getAttacked()==false &&
                (gameState.summonedUnits.get(unitIdx).getName().equals("Fire Spitter"))||gameState.summonedUnits.get(unitIdx).getName().equals("Pyromancer")){
            // Clicked an occupied tile --> attack
        	boolean attackStatus=false;
        	gameState.board.clearTileHighlighting(out, gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), opposingPlayer(gameState,player)));
            attackStatus = SpecialAbilities.attackUnitRanged(1, player,out, gameState.summonedUnits.get(unitIdx),startTile,clickedTile,gameState);
//                gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);

            startTile.getUnitFromTile().setAttacked(attackStatus);
            if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
            {
                if(gameState.summonedUnits.get(unitIdx)!=null) {
                	gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
                    System.out.println(attackStatus);
                }
            }
            
            
            }
            
            if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
            {

            if(gameState.summonedUnits.get(unitIdx).getName().equals("WindShrike")){
                if (clickedTile.getUnitFromTile()== null && gameState.summonedUnits.get(unitIdx).getMoved() == false && gameState.summonedUnits.get(unitIdx).getAttackedOnce() == false){

                    SpecialAbilities.windshrikeMove(1,out, startTile, clickedTile, gameState);
                }
                else if(clickedTile.getUnitFromTile()!=null && clickedTile.getUnitFromTile().getIsPlayer() != player.getID() && gameState.summonedUnits.get(unitIdx).getAttacked()==false && gameState.summonedUnits.get(unitIdx).getMoved()==false && !gameState.board.getAdjacentTilesToAttack(player,out, startTile).contains(clickedTile)) {

                    boolean attackStatus=false;

                    attackStatus=PerformAction.attackUnit(1,player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
                    startTile.getUnitFromTile().setAttacked(attackStatus);
                    if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
                    {
                        if(gameState.summonedUnits.get(unitIdx)!=null) {
                            gameState.summonedUnits.get(unitIdx).setMoved(attackStatus);
                            gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
                        }
                    }
                }else if(clickedTile.getUnitFromTile()!=null && clickedTile.getUnitFromTile().getIsPlayer() != player.getID() && gameState.summonedUnits.get(unitIdx).getAttacked()==false && gameState.board.getAdjacentTilesToAttack(player,out, startTile).contains(clickedTile)){ // Clicked an occupied tile --> attack

                    boolean attackStatus=false;

                    attackStatus=PerformAction.attackUnit(1,player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
                    startTile.getUnitFromTile().setAttacked(attackStatus);
                    if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
                    {
                        if(gameState.summonedUnits.get(unitIdx)!=null) {
                        	gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
                        }
                    }

                }

            }



            // If the unit is Azurite Lion or Serpenti, implement Attack Twice logic
            else if(gameState.summonedUnits.get(unitIdx).getId() == 7 || gameState.summonedUnits.get(unitIdx).getId() == 17 || gameState.summonedUnits.get(unitIdx).getId() == 26 || gameState.summonedUnits.get(unitIdx).getId() == 36) 
            {
            	
            	 // If it is not the unit with attack twice ability, proceed normally
                 // If an empty tile is clicked, and the player unit has not moved or attacked twice yet, move to the tile, set moved to true
            	 if(clickedTile.getUnitFromTile()==null && gameState.summonedUnits.get(unitIdx).getMoved()==false && gameState.summonedUnits.get(unitIdx).getAttackedTwice()==false){ // Clicked an empty tile --> movement
                     moveUnit(1,out, startTile, clickedTile, gameState); // move the unit to the clicked tile
//                     gameState.summonedUnits.get(unitIdx).setMoved(true);

                 // If a tile with an enemy unit is clicked, the player has not attacked twice or moved yet, and the enemy unit is not adjacent.
                 // It is not a direct attack, it is an attack-and-move, thus moved and attacked should be set to true. This should only be possible if moved is still false.
                 }else if(clickedTile.getUnitFromTile()!=null && clickedTile.getUnitFromTile().getIsPlayer() != player.getID() && gameState.summonedUnits.get(unitIdx).getAttackedTwice()==false && gameState.summonedUnits.get(unitIdx).getMoved()==false && !gameState.board.getAdjacentTilesToAttack(player,out, startTile).contains(clickedTile)) {
                     boolean attackStatus=false;
                     gameState.summonedUnits.get(unitIdx).setMoved(true);
                     
                     attackStatus=PerformAction.attackUnit(1,player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
                     
                     if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
                     {
                         if(gameState.summonedUnits.get(unitIdx)!=null) {
                        	 // If the unit has not already attacked once, set attacked once to true
                    	 if(gameState.summonedUnits.get(unitIdx).getAttackedOnce() == false) {
                    		gameState.summonedUnits.get(unitIdx).setAttackedOnce(attackStatus);
                    	// If the unit has already attacked once, set attacked twice to true
                    	}else if(gameState.summonedUnits.get(unitIdx).getAttackedOnce() == true) {
                    		gameState.summonedUnits.get(unitIdx).setAttackedTwice(attackStatus);
                    	}
                         }
                     }
                 // If a tile with an enemy unit is clicked and it is adjacent, and the player has not attacked twice yet
                 // It is a direct attack, only attack should be set to true
                 }  else if(clickedTile.getUnitFromTile()!=null && clickedTile.getUnitFromTile().getIsPlayer() != player.getID() && gameState.summonedUnits.get(unitIdx).getAttackedTwice()==false && gameState.board.getAdjacentTilesToAttack(player,out, startTile).contains(clickedTile)) {
                     boolean attackStatus=false;
                     
                     attackStatus=PerformAction.attackUnit(1,player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
                     
                     if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
                     {
                         if(gameState.summonedUnits.get(unitIdx)!=null) {
                        	 // If the unit has not already attacked once, set attacked once to true
                    	 if(gameState.summonedUnits.get(unitIdx).getAttackedOnce() == false) {
                    		gameState.summonedUnits.get(unitIdx).setAttackedOnce(attackStatus);
                    	// If the unit has already attacked once, set attacked twice to true
                    	}else if(gameState.summonedUnits.get(unitIdx).getAttackedOnce() == true) {
                    		gameState.summonedUnits.get(unitIdx).setAttackedTwice(attackStatus);
                    	}
                         }
                     }
                 	
                 }else if(clickedTile.getUnitFromTile()!=null && gameState.summonedUnits.get(unitIdx).getAttackedTwice() == true) {
//                	 BasicCommands.addPlayer1Notification(out, "Already attacked twice", 2);
                     BasicCommands.addPlayer1Notification(out, "Exhausted!", 2);

                 }
            }


            
            // Else, proceed normally
            // If an empty tile is clicked, and the player unit has not moved or attacked yet, move to the tile, set moved to true
            else if(clickedTile.getUnitFromTile()==null && gameState.summonedUnits.get(unitIdx).getMoved()==false && gameState.summonedUnits.get(unitIdx).getAttacked()==false) // Clicked an empty tile --> movement
            {

                moveUnit(1,out, startTile, clickedTile, gameState); // move the unit to the clicked tile
//                gameState.summonedUnits.get(unitIdx).setMoved(true);
            
            // If a tile with an enemy unit is clicked, the player has not attacked or moved yet, and the enemy unit is not adjacent.
            // It is not a direct attack, it is an attack-and-move, thus moved and attacked should be set to true. This should only be possible if moved is still false.
            }else if(clickedTile.getUnitFromTile()!=null && clickedTile.getUnitFromTile().getIsPlayer() != player.getID() && gameState.summonedUnits.get(unitIdx).getAttacked()==false && gameState.summonedUnits.get(unitIdx).getMoved()==false && !gameState.board.getAdjacentTilesToAttack(player,out, startTile).contains(clickedTile)) 
            {
               
                boolean attackStatus=false;
                
                attackStatus=PerformAction.attackUnit(1,player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
        
                if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
                {
                	if(gameState.summonedUnits.get(unitIdx)!=null) {
                    gameState.summonedUnits.get(unitIdx).setMoved(attackStatus);
                	gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
                	}
                }
            }
            // If a tile with an enemy unit is clicked and it is adjacent, and the player has not attacked yet
            // It is a direct attack, only attack should be set to true
            else if(clickedTile.getUnitFromTile()!=null && clickedTile.getUnitFromTile().getIsPlayer() != player.getID() && gameState.summonedUnits.get(unitIdx).getAttacked()==false && gameState.board.getAdjacentTilesToAttack(player,out, startTile).contains(clickedTile))
            { // Clicked an occupied tile --> attack

                boolean attackStatus=false;
                
                attackStatus=PerformAction.attackUnit(1,player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
                
                if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
                {
                    if(gameState.summonedUnits.get(unitIdx)!=null) {
                    	gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
                    }
                }
            	
            }
        }
            
            startTile = null; // Reset the start tile to no unit
            if(unitIdx>-1 && unitIdx<gameState.summonedUnits.size())
            {
                if(gameState.summonedUnits.get(unitIdx)!=null) {
                	gameState.summonedUnits.get(unitIdx).setProvoked(false);
//         	 gameState.summonedUnits.get(unitIdx).setMoved(false);

                }
            }
        }
        else {

            gameState.board.clearTileHighlighting(out, gameState.board.highlightTilesMoveAndAttack(1,player,out, startTile,gameState));
            AppConstants.callSleep(200);
            startTile = null; // Reset the start tile to no unit
        }
    }

    // returns tiles of adjacent units with provoke
    public static ArrayList<Tile> getProvokerTiles(ActorRef out, GameState gameState, Player player) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile tile: gameState.board.summonableTiles(out, startTile)){
            if (SpecialAbilities.getProvokingUnits(out, gameState, TileClicked.opposingPlayer(gameState, player)).contains(tile.getUnitFromTile())){
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public static void setStartTile(boolean bool){
        if(bool==false) startTile=null;
    }


    /** This method summons the card to the board
     *
     * @param out
     * @param gameState
     * @param clciked
     * @param player
     *
     */
    public void summonCard(ActorRef out, GameState gameState, Tile clicked, Player player) {
        // System.out.println("inside summon function");
        Card handCard = player.getCardByHandPos(gameState.handPosClicked-1);//getting the card by hand position
        // System.out.println("Card name: "+handCard.getCardname());
        Unit unitSummon = player.getUnitbyCard(handCard);//getting the unit by hand position
        // System.out.println("Id of the unit to summoned: "+unitSummon.getId());
        // System.out.println("player mana: "+ player.getMana());
        // System.out.println("mana cost: "+ handCard.getManacost());

        if (handCard!=null){

        if (player.getMana() >= handCard.getManacost() && handCard.getCardname().equals("Truestrike")) {
            if (clicked.getUnitFromTile() == null){
                OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
                BasicCommands.addPlayer1Notification(out, "Please select an enemy unit", 2);
                return;
            }
            else Spell.truestike(out, handCard, clicked, gameState);
            return;

        }

        if (player.getMana() >= handCard.getManacost() && handCard.getCardname().equals("Sundrop Elixir")) {
            if (clicked.getUnitFromTile() == null){
                OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
                BasicCommands.addPlayer1Notification(out, "Please select a friendly unit", 2);
                return;
            }
            else Spell.sundropElixir(out, handCard, clicked, gameState);
            return;
        }

        // added the conditions of checking if the tile has a unit on it already and that the summonable tile list contains the clicked tile ontop of checking mana cost

        if(player.getMana()>=handCard.getManacost() && clicked.getUnitFromTile() == null &&  PerformAction.getSummonableTiles(out, gameState, player).contains(clicked)){

        	if(unitSummon!=null) { // It's a unit
        		// Azure Herald special ability (When this unit is summoned give your avatar +3 health (maximum 20)
            	if(unitSummon.getId() == 5 || unitSummon.getId() == 15) {
            		// If +3 increase would make avatar health greater than max health, set avatar health to max health
            		if(gameState.player1.getAvatar().getHealth() + 3 > AppConstants.playerMaxHealth) {
                        // variable to track how much healing the avatar recieves
                        int healing =  AppConstants.playerMaxHealth-gameState.summonedUnits.get(0).getHealth();
                        gameState.player1.getAvatar().setHealth(AppConstants.playerMaxHealth);
            			BasicCommands.setUnitHealth(out, gameState.summonedUnits.get(0), gameState.player1.getAvatar().getHealth());
                        // buff effect and notification of healing effect
                        BasicCommands.addPlayer1Notification(out, "Healing avatar +" + healing, 2);
                        AppConstants.callSleep(200);
                        BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), gameState.summonedUnits.get(0).getTileFromUnit(40, gameState,out));
            		}
            		else {
            			// Increase avatar health by 3
            			gameState.player1.getAvatar().setHealth(gameState.player1.getAvatar().getHealth() + 3);
        
                		// Update on front end
            			
            			BasicCommands.setUnitHealth(out, gameState.summonedUnits.get(0), gameState.player1.getAvatar().getHealth());
                        BasicCommands.addPlayer1Notification(out, "Healing avatar +3", 2);
                        AppConstants.callSleep(200);
                        BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), gameState.summonedUnits.get(0).getTileFromUnit(40, gameState,out));
            		}
            	}

                player.setMana(player.getMana()-handCard.getManacost());//decrease the mana
                player.setPlayer(out);//reflecting the mana on board
                player.deleteCardInHand(out, player.getID(), gameState);//delete the card in hand
                AppConstants.callSleep(200);
                gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
                gameState.SummonTileList=null;
                // clearTileHighSummon(out, gameState, player);//clear the tile summoning
                player.drawUnitToBoard(out, unitSummon, clicked, handCard, player, gameState);//draw unit on board
                AppConstants.callSleep(200);
                unitSummon.setMoved(true);//restricting move
                unitSummon.setAttacked(true);//restricting attack
                // Restricting units with the attack twice special ability
                if(unitSummon.getId() == 7 || unitSummon.getId() == 17 || unitSummon.getId() == 26 || unitSummon.getId() == 36) {
                	unitSummon.setAttackedOnce(true);
                	unitSummon.setAttackedTwice(true);
                }
                // gameState.SummonTileList=null;

                // If the summoned unit is Blaze Hound, both players draw a card
                if(unitSummon.getName().equals("Blaze Hound")) {
                    SpecialAbilities.blazeHound(out, gameState);
                }

                if(player.getID()==1)//Notifications active for only player1
                BasicCommands.addPlayer1Notification(out, "Summoning Complete", 2);
        	}


        }
        else {//if conditions are not met
            if(player.getMana()<handCard.getManacost()){//if not enough mana
                if(player.getID()==1)//Notifications active for only player1
                {
                	BasicCommands.addPlayer1Notification(out, "Not enough Mana", 2);
                	OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
                }
            }
            else if(clicked.getUnitFromTile() != null){//if tile already occupied
            	if(player.getID()==1)//Notifications active for only player1
                {
            		BasicCommands.addPlayer1Notification(out, "Tile Already occupied", 2);
            		OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
                }
            }
            else if(!PerformAction.getSummonableTiles(out, gameState, player).contains(clicked)){//if outside the summon tile list
//            	if(player.getID()==1)
//                {
            		// If the unit being summoned is Ironcliff Guardian or Planar Scout, implement airdrop special ability
            		if(unitSummon.getId() == 6 || unitSummon.getId() == 16 || unitSummon.getId() == 28 || unitSummon.getId() == 38) {
            			gameState.SummonTileList = gameState.board.getTilesWithoutUnits(out, gameState.board.getTiles(), player);
            			 player.setMana(player.getMana()-handCard.getManacost());//decrease the mana
                         player.setPlayer(out);//reflecting the mana on board
                         player.deleteCardInHand(out, player.getID(), gameState);//delete the card in hand
                         AppConstants.callSleep(200);
                         gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
                         gameState.SummonTileList=null;
                         // clearTileHighSummon(out, gameState, player);//clear the tile summoning
                         player.drawUnitToBoard(out, unitSummon, clicked, handCard, player, gameState);//draw unit on board
                         AppConstants.callSleep(200);
                         unitSummon.setMoved(true);//restricting move
                         unitSummon.setAttacked(true);//restricting attack
                         if(player.getID()==1) { //Notifications active for only player1 
                             BasicCommands.addPlayer1Notification(out, "Summoning Complete", 2);
                     	}
            		}else {
            			BasicCommands.addPlayer1Notification(out, "Outside Summonable area", 2);
                		OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
            		}
               // }
            }
        }}
    }

    // Method to return the opposing player (useful for highlighting opposing players units etc) (can be moved)
    public static Player opposingPlayer(GameState gameState, Player player){
        Player playerOp = player;
        if (player.getID() == 1){
            playerOp = gameState.player2;
        }
        else if (player.getID() == 2){
            playerOp = gameState.player1;
        }
        return playerOp;
    }
}
