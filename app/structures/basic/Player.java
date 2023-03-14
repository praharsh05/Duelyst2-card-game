package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;//importing for cards in deck and hand
import java.util.*;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	BetterUnit avatar;
	int playerID; // 1=player1, 2= computerPlayer
	int health;
	int mana;
	int position = 1;//variable to set card position in hand
	
	boolean highlighted=false;

	List <Unit> playerUnits = new ArrayList<Unit>();

	String[] unitFiles;
	

	public List<Card> deck;// deck of card
	public List<Card> hand;// hand containing card
	
	Tile currentTile; // To keep track of avatar's tile


	/** constructor to create a player with set health and mana which calls setPlayer to place the data on the front end.
	 * 
	 * @param playerID
	 * @param out
	 * @param avatar
	 * @param cardsdeck
	 */
	public Player(int playerID, ActorRef out, BetterUnit avatar, String[] unitFiles) {
		this.avatar = avatar;
		this.playerID=playerID;
		this.health = avatar.getHealth();
		this.mana = 2; // this will be set to player turn +1 once we have player turn available
		this.hand= new ArrayList<Card>();
		this.deck = new ArrayList<Card>();
		this.unitFiles=unitFiles;
		setPlayer(out);
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	
	public BetterUnit getAvatar() {
		return this.avatar;
	}
	public void setAvatar(BetterUnit avatar) {
		this.avatar = avatar;
	}
	
	public int getID() {
		return playerID;
	}
	public void setID(int playerID) {
		this.playerID = playerID;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	
	public void setCurrentTile(Tile currentTile) {
	    	this.currentTile=currentTile;
	}
	    
	 public Tile getCurrentTile() {
	    	return currentTile;
	 }

	public boolean getHighlighted() {
		return highlighted;
	}
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public Card getCardByHandPos(int i) {
		if (i>=0 && i < hand.size()){
		return hand.get(i);}
		else return null;
	}

	public Unit getUnitbyCard(Card card){
		Unit unit=null;
		Card c=card;//changes here for conflict resolution
		if (c != null) {
			for (Unit u : playerUnits) {
				if (u.getId() == c.getId()) unit = u;
				//trying to return the unit for that particular card
			}
			return unit;
		}
		else return null;
	}

	//method to get total cards in the deck
	public int getCardInDeck(){
		return deck.size();
	}

	//method to get total cards in hand
	public int getCardInHand() {
		return hand.size();
	}
	

	// This method syncs up the Player health with the health of their Avatar
	public void syncHealth() {
		this.health = this.getAvatar().getHealth();
	}

	// This method syncs up the Player health with the health of their Avatar
	public void syncPlayerHealth(GameState gameState) {
		if(gameState.summonedUnits.size()>0) // To tackle IndexOutofBoundException
		{
		//this.health = this.avatar.getHealth();
		this.health = gameState.summonedUnits.get(0).getHealth();
		}
	}
	// This method syncs up the AI health with the health of their Avatar
	public void syncAIHealth(GameState gameState) {
		if(gameState.summonedUnits.size()>1) // To tackle IndexOutofBoundException
		{
		this.health = gameState.summonedUnits.get(1).getHealth();
		}
	}
	
	/** Setting the player health on the front end
	 * 
	 * @param out
	 */
	public void setPlayerHealth(ActorRef out) {
		if(playerID==1)
		{
			BasicCommands.setPlayer1Health(out, this);
			AppConstants.callSleep(100);
		
		}else {
			 BasicCommands.setPlayer2Health(out, this);
			 AppConstants.callSleep(100);
		}
	}
	
	/** Setting the player mana on the front end
	 * 
	 * @param out
	 */
	public void setPlayerMana(ActorRef out){
		if(playerID==1)
		{	
			BasicCommands.setPlayer1Mana(out, this);
			AppConstants.callSleep(100);
		}else {
		     BasicCommands.setPlayer2Mana(out, this);
			 AppConstants.callSleep(100);
		}
	}
	
	/** Setting the player health and mana on the front end
	 * 
	 * @param out
	 */
	public void setPlayer(ActorRef out){
		if(playerID==1)
		{
			BasicCommands.setPlayer1Mana(out, this);
			AppConstants.callSleep(100);
			
			BasicCommands.setPlayer1Health(out, this);
			AppConstants.callSleep(100);
		}else {
		     BasicCommands.setPlayer2Mana(out, this);
			 AppConstants.callSleep(100);
			 
			 BasicCommands.setPlayer2Health(out, this);
			 AppConstants.callSleep(100);
		}
	}

	//method to create the deck of card for players
	public void createDeck(int playerID) {
		if(playerID==1){
			deck=OrderedCardLoader.getPlayer1Cards();
		}
		else{
			deck=OrderedCardLoader.getPlayer2Cards();
		}
		// for debugging
		// for(Card c: deck) {
		// 	System.out.println("Card in deck: "+c.getCardname()+" with id: "+ c.getId());
		// }
	}
	
	/** This method sets the hand of the corresponding player object
	 * @param playerID
	 * @param out
	 */
    public void setHand(ActorRef out, int playerID) {
        for(int i=0;i<AppConstants.minCardsInHand;i++){
			//move the top card from deck to hand
			hand.add(i, deck.get(0));
			deck.remove(0);
			if(playerID==1){
				// drawCard [i]
				BasicCommands.drawCard(out, hand.get(i), position, 0);
				AppConstants.callSleep(500);
				// increment the position
				position++;
			}else
				position++; // For player 2 hand tracking
            
        }
    }


    /** This method draws a card from the deck and adds that card to the hand
     * of the corresponding player object
     * 
     * @param out
     */
    
	public void drawAnotherCard(ActorRef out, int playerID) {
		if(position<=AppConstants.maxCardsInHand && deck.size()>0){
			//move the top card from deck to hand
			if((position-1)>0){//added to manage index out of bound exception
				hand.add(position-1, deck.get(0));
				deck.remove(0);
				if(playerID==1){
					//draw the card
					BasicCommands.drawCard(out, hand.get(position-1) , position, 0);
					AppConstants.callSleep(500);
					//increment the position
					position++;
				}else
					position++;
			}
			else{
				hand.add(0, deck.get(0));
				deck.remove(0);
				if(playerID==1){
					//draw the card
					BasicCommands.drawCard(out, hand.get(position-1) , position, 0);
					AppConstants.callSleep(500);
					//increment the position
					position++;
				}else
					position++;
			}
			
		}
		else {
			if(deck.size()>0)
			{
				if(playerID==1){
					BasicCommands.addPlayer1Notification(out, "Hand is full! ", 2);
					deck.remove(0);
					AppConstants.callSleep(500);
				}
				else{
					deck.remove(0);
					AppConstants.callSleep(500);
				}
			}else {
				// deck empty scenario
				if(playerID==1){
					BasicCommands.addPlayer1Notification(out, "Deck is empty! ", 2);
					AppConstants.callSleep(200);
				}
				
			}
				
		}
		
	}



	/** This method deletes the card from the hand position
	 * @param ActorRef 
	 * @param playerID
	 * @param GameState 
	 */
	public void deleteCardInHand(ActorRef out, int playerID, GameState gameState) {
		if(playerID==1){
			BasicCommands.deleteCard(out, gameState.handPosClicked);//delete card
			AppConstants.callSleep(200);
			for(int i=gameState.handPosClicked;i<position-1;i++){//redrawing the card to fill in the hand position
				Card c = getCardByHandPos(i);
				BasicCommands.drawCard(out, c , i, 0);
				AppConstants.callSleep(200);
			}
			BasicCommands.deleteCard(out, position-1);//delete the last card
			AppConstants.callSleep(200);
			hand.remove(gameState.handPosClicked-1);//removing card from hand position
			gameState.handPosClicked=-1;//setting the hand postion in gamestate to initial value
			// // decrement the position
			position--;
		}else {
			hand.remove(gameState.handPosClicked-1);//removing card from hand position
			gameState.handPosClicked=-1;//setting the hand postion in gamestate to initial value
			position--;
		}
		
	}

	//my understanding of creating units for both player and AI
	 /** This method creates a list of units
	 *
     * 
     * @param player
     */

	public void createUnits(Player player){
		// System.out.println("Inside create units");
		int j=0;
			for(int i=0;i<unitFiles.length;i++){
				// System.out.println("i= "+i);
				Card c = deck.get(j);
				if(c.getId()==4 || c.getId()==8 || c.getId()==14 || c.getId()==18 || c.getId()==22 || c.getId()==27 || c.getId()==32 || c.getId()==37) j++;
				c=deck.get(j);
					Unit u = BasicObjectBuilders.loadUnit(unitFiles[i], c.getId(),Unit.class);
					u.setIsPlayer(player.playerID);
					u.setHealth(c.getBigCard().getHealth());
					u.setAttack(c.getBigCard().getAttack());
					u.setName(c.getCardname());
					u.setMaxHealth(c.getBigCard().getHealth());
					playerUnits.add(u);//changes here for conflict resolution
					j++;
			}
	}
	

	
	
	//method to get units of a player
	public List<Unit> getPlayerUnits(){
		return playerUnits;
	}
		

	// method to draw the unit to the board and set the front end attack and health. Updated to take an id and draw the unit with that Id
	public void drawUnitToBoard(ActorRef out, Unit unit,Tile tile, Card card, Player player,GameState gameState) {
			
		if ( player.getID() == 1) {
			for (Unit u : playerUnits) {
				if (u.getId() == card.getId()) { // check the card ids
					//added these in order to summon the unit on board rather than in the top left corner
					tile.setUnitToTile(unit);
					unit.setSummonedID(gameState.summonedUnits.get(gameState.summonedUnits.size()-1).getSummonedID()+1);//unique summonedID
					unit.setIsPlayer(1);
					gameState.board.addUnitToBoard(tile.getTilex(), tile.getTiley(), unit);
					gameState.summonedUnits.add(unit);
					unit.setPositionByTile(tile);
					BasicCommands.drawUnit(out, unit, tile);
					AppConstants.callSleep(100);
					BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), tile);
					AppConstants.callSleep(100);
					BasicCommands.setUnitHealth(out, unit, unit.getHealth());
					AppConstants.callSleep(100);
					BasicCommands.setUnitAttack(out, unit, unit.getAttack());
					AppConstants.callSleep(100);
				}
			}
		}
		else {
			for (Unit u : playerUnits) {
				if (u.getId() == card.getId()) {
					tile.setUnitToTile(unit);
					unit.setSummonedID(gameState.summonedUnits.get(gameState.summonedUnits.size()-1).getSummonedID()+1);//unique summoneddId
					unit.setIsPlayer(2);
					gameState.board.addUnitToBoard(tile.getTilex(), tile.getTiley(), unit);
					gameState.summonedUnits.add(unit);
					unit.setPositionByTile(tile);
					BasicCommands.drawUnit(out, unit, tile);
					AppConstants.callSleep(100);
					BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), tile);
					AppConstants.callSleep(100);
					BasicCommands.setUnitHealth(out, unit, unit.getHealth());
					AppConstants.callSleep(100);
					BasicCommands.setUnitAttack(out, unit, unit.getAttack());
					AppConstants.callSleep(100);
				}
			}
			
		}
		}
	
}
