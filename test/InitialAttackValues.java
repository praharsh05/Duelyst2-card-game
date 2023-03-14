
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.BetterUnit;
import structures.basic.Board;
import structures.basic.Player;
import structures.basic.Tile;
import utils.AppConstants;
import utils.BasicObjectBuilders;

public class InitialAttackValues {

	@Test
	public void AIInitialIAttackValue() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);
		
		//Confirms that the Avatar's initial attack is equal to 2

        assertEquals(gameState.aiAvatar.getAttack(),2); // Set player current tile

	}
	
	@Test
	public void PlayerInitialAttackValue() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);
		
		//Confirms that the Player's initial attack is equal to 2

        assertEquals(gameState.avatar.getAttack(),2); // Set player current tile

	}
}