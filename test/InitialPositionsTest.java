import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.BetterUnit;
import utils.AppConstants;

public class InitialPositionsTest {

	@Test
	public void AIInitialPositionTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Confirms that the AI's initial tile position is (7,2)
		assertEquals(gameState.player2.getCurrentTile(), gameState.board.returnTile(7, 2)); // Set player current tile

	}

	@Test
	public void PlayerInitialPositionTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Confirms that player1's initial tile position is (1,2)
		assertEquals(gameState.player1.getCurrentTile(), gameState.board.returnTile(1, 2)); // Set player current tile

	}

}