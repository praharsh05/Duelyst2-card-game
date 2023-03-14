import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Player;
import utils.AppConstants;

public class AvatarsNotNullTest {

	@Test
	public void AIAvatarNotNullTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initalized

		//Confirms that the AI Avatar is null before the game is initialized
		assertNull("Player should be initialized", gameState.aiAvatar);
		
		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Checks that the AI Avatar is not null when game is initialized)
		assertNotNull("Player should be initialized", gameState.aiAvatar);

	}

	@Test
	public void PlayerAvatarNotNullTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initialized
		
		//Confirms that the Player Avatar is null before the game is initialized
		assertNull("Player should be initialized", gameState.avatar);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Checks that the Player Avatar is not null when game is initialized)
		assertNotNull("Player should be initialized", gameState.avatar);

	}
}