package structures.basic;

import akka.actor.ActorRef;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.BasicCommands;
import structures.GameState;

/**
 * This is a representation of a Unit on the game board.
 * A unit has a unique id (this is used by the front-end.
 * Each unit has a current UnitAnimationType, e.g. move,
 * or attack. The position is the physical position on the
 * board. UnitAnimationSet contains the underlying information
 * about the animation frames, while ImageCorrection has
 * information for centering the unit on the tile. 
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Unit {

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	boolean attackedOnce = false;
	boolean attackedTwice = false;
	String name;
	int id;
	UnitAnimationType animation;
	Position position;
	UnitAnimationSet animations;
	ImageCorrection correction;
	// basic variables for health and attack of units
	int unitHealth;
	int unitAttack;

	int isPlayer; // human = 1 ai = 2 this is to just check the unit is player to the playerId when interacting with units
	
	boolean moved=false; // variable to check whether the unit has already moved or not
	boolean attacked=false; // variable to check whether the unit has already attacked other units or not

	int maxHealth;
	int summonedID;
//	int ownerPlayer;

	boolean provoked;
	
	public Unit() {}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;	
		position = new Position(0,0,0,0);
		this.correction = correction;
		this.animations = animations;
		
	}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(currentTile.getXpos(),currentTile.getYpos(),currentTile.getTilex(),currentTile.getTiley());
		this.correction = correction;
		this.animations = animations;
	}
	
	
	
	public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
			ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = animation;
		this.position = position;
		this.animations = animations;
		this.correction = correction;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UnitAnimationType getAnimation() {
		return animation;
	}
	public void setAnimation(UnitAnimationType animation) {
		this.animation = animation;
	}

	public ImageCorrection getCorrection() {
		return correction;
	}

	public void setCorrection(ImageCorrection correction) {
		this.correction = correction;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public UnitAnimationSet getAnimations() {
		return animations;
	}

	public void setAnimations(UnitAnimationSet animations) {
		this.animations = animations;
	}
	
	/** Setter and getter method for unitHealth
	 * 
	 * @param out
	 */
	public int getHealth() {
		return unitHealth;
	}
	
	public void setHealth(int unitHealth) {
		this.unitHealth = unitHealth;
	}
	
	/** Setter and getter method for unitAttack
	 * 
	 * @param out
	 */
	public int getAttack() {
		return unitAttack;
	}
	
	public void setAttack(int unitAttack) {
		this.unitAttack = unitAttack;
	}
	
	public boolean getAttackedOnce() {
		return attackedOnce;
	}

	public void setAttackedOnce(boolean attackedOnce) {
		this.attackedOnce = attackedOnce;
	}

	public boolean getAttackedTwice() {
		return attackedTwice;
	}

	public void setAttackedTwice(boolean attackedTwice) {
		this.attackedTwice = attackedTwice;
	}

	public int getIsPlayer() {
		return isPlayer;
	}

	public void setIsPlayer(int isPlayer) {
		this.isPlayer = isPlayer;
	}

	public boolean getMoved() {
		return moved;
	}
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	
	public boolean getAttacked() {
		return attacked;
	}
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int getUnitHealth) {
		this.maxHealth = getUnitHealth;
	}

	public boolean isProvoked() {
		return provoked;
	}

	public void setProvoked(boolean provoked) {
		this.provoked = provoked;
	}

	/** Method to get the summoned id of a particular unit on the board
	 * 
	 * @return
	 */
	public int getSummonedID() {
		return summonedID;
	}
	
	/** Method to set the id for  a particular unit while summoning on the board
	 * 
	 * @return
	 */
	
	public void setSummonedID(int summonedID) {
		this.summonedID = summonedID;
	}
	
	// this is a method that can be called to remove a unit from the board. this will be used in another method 'isAlive()' to check if the unit is alive during the game.
	// this could be achieved by using the basicCommands.deletUnit() directly, however this will allow us to more easily call that function.
	public void unitRemoval(ActorRef out){
		BasicCommands.deleteUnit(out,this);
	}

	// this will be the main method to check during gameplay. I am aware that I could include the unitRemoval method directly here, however, for purely testing purposes
	// it may be handy to separate these two methods.
	public boolean isALive(ActorRef out){
		if ( unitHealth <= 0){
			this.unitRemoval(out);
			return false;
		}
		else return true;
	}


	/**
	 * This command sets the position of the Unit to a specified
	 * tile.
	 * @param tile
	 */
	@JsonIgnore
	public void setPositionByTile(Tile tile) {
		position = new Position(tile.getXpos(),tile.getYpos(),tile.getTilex(),tile.getTiley());
	}

	// method to retrieve the tile that a particular unit is on (player 1 only)
	public Tile getTileFromUnit(int unitID, GameState gameState, ActorRef out) {
		Tile unitTile = null;
		for (Tile tile : gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player1)) {
			if (tile.getUnitFromTile().getId() == unitID){
				unitTile = tile;
				return unitTile;
			}
		}
		return null;

	}

	public Tile getTileFromUnitP2(int unitID, GameState gameState, ActorRef out) {
		Tile unitTile = null;
		for (Tile tile : gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player2)) {
			if (tile.getUnitFromTile().getId() == unitID){
				unitTile = tile;
				return unitTile;
			}
		}
		return null;

	}
	
}
