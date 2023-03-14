package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import utils.AppConstants;
import structures.basic.Unit;

import java.util.ArrayList;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * <p>
 * {
 * messageType = “cardClicked”
 * position = <hand index position [1-6]>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class CardClicked implements EventProcessor {

    public int handPosition;//variable to hold hand position
    public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (gameState.isGameActive) // if the frontend connection is active
        {
            cardClick=message.get("messagetype");//message to keep track of previous click on front-end
            handPosition = message.get("position").asInt();//get hand position

            if(gameState.clickMessage != cardClick){
                gameState.clickMessage=cardClick;
            }
            if (gameState.player1Turn) { // for the first player

                gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
                //method call to highlight card
                highlightMiniCard(out, handPosition, gameState);
                //method to highlight tiles on which card can be summoned
                highlightSummonableTiles(out, gameState, gameState.player1);


            }
            
        }

    }

    public void highlightSummonableTiles(ActorRef out, GameState gameState, Player player) {
    	
//        if(gameState.SummonTileList==null){
        	// Get the card that has just been clicked
        	Card clickedCard = gameState.player1.getCardByHandPos(gameState.handPosClicked-1);

        if (clickedCard != null) {

            // If the card ID is 6, 16, 28 or 38 (IronCliff Guardian or Planar Scout)
            if (clickedCard.getId() == 6 || clickedCard.getId() == 16 || clickedCard.getId() == 28 || clickedCard.getId() == 38) {
                gameState.SummonTileList = gameState.board.getTilesWithoutUnits(out, gameState.board.getTiles(), player);
                gameState.board.highlightTilesWhite(out, gameState.SummonTileList);
            }

            // highlighting for the two human player spells
            if (clickedCard.getCardname().equals("Truestrike")) {
                gameState.SummonTileList = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player2);
                gameState.board.highlightTilesRed(out, gameState.SummonTileList);

            } else if (clickedCard.getCardname().equals("Sundrop Elixir")) {
                gameState.SummonTileList = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player1);
                gameState.board.highlightTilesWhiteSpell(out, gameState.SummonTileList);

            } else {
                gameState.SummonTileList = new ArrayList<Tile>();
                // list of the tiles with units
                ArrayList<Tile> list = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), player);

                // iteration through the list and highlight adjacent tiles
                for (Tile items : list) {
                    ArrayList<Tile> listItem = gameState.board.summonableTiles(out, items);
                    for (Tile tile : listItem) {
                        gameState.SummonTileList.add(tile);
                    }
                }
                gameState.board.highlightTilesWhite(out, gameState.SummonTileList);
            }

//        }
        }
    }

    

    /** This method highlights MiniCards in hand
     * 
     * @param out
     * @param position
     * @param gameState
     * 
     */
    public void highlightMiniCard(ActorRef out, int position, GameState gameState) {
 
        if(gameState.handPosClicked<0){//check if its the first click
 
            gameState.handPosClicked=position;//set the gamestate variable to new position clicked
            Card card1 = gameState.player1.getCardByHandPos(position-1);//get the card at the hand position
            BasicCommands.drawCard(out, card1, gameState.handPosClicked, 1);//highlight the card
        }
        else if(position != gameState.handPosClicked){//check if another card is clicked
 
            Card card2 = gameState.player1.getCardByHandPos(position-1);//get the card at the new position
            Card card1 = gameState.player1.getCardByHandPos(gameState.handPosClicked-1);//get the card at earlier position
            BasicCommands.drawCard(out, card1, gameState.handPosClicked, 0);//dehighlight the previous position
            BasicCommands.drawCard(out, card2, position, 1);//highlight the new postion
            gameState.handPosClicked=position;//set the new position to gameState
        }
        else{//this is not done yet
            clearHighlightMiniCard(out, gameState);
            gameState.board.clearTileHighlighting(out, gameState.SummonTileList);
//            gameState.SummonTileList=null;
        }
	}

    //method to clear all highlights
    public static void clearHighlightMiniCard(ActorRef out, GameState gameState) {
    	if(gameState.handPosClicked>-1 && gameState.handPosClicked<=gameState.player1.hand.size()) // To tackle IndexOutOfBoundException
    	{
	        Card card1 = gameState.player1.getCardByHandPos(gameState.handPosClicked-1);//get the card at earlier position
	        BasicCommands.drawCard(out, card1, gameState.handPosClicked, 0);//dehighlight the previous position
	        gameState.handPosClicked=-1;
            //set the gameState hand position to -1
    	}
    }
}
