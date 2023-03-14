import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import utils.AppConstants;

public class InitialHealthValuesTest {

	@Test
	public void InitializedAIHealthTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// This test is to confirm that the AI's initial health ==
		// AppConstants.playerMaxHealth (20)
		assertEquals(AppConstants.playerMaxHealth, gameState.player2.getHealth());
	}

	@Test
	public void InitializedPlayerHealthTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// This test is to confirm that Player1 initial health ==
		// AppConstants.playerMaxHealth (20)
		assertEquals(AppConstants.playerMaxHealth, gameState.player1.getHealth());
	}

}