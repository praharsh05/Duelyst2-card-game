import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import utils.AppConstants;

public class InitialHandSizes {

	@Test
	public void PlayerInitialHandSize() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Tests that the initial number of cards in the human player's hand is 3
		assertEquals(AppConstants.minCardsInHand, gameState.player1.hand.size());

	}
	
	@Test
	public void AIInitialHandSize() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Tests that the initial number of cards in the AI player's hand is 3
		assertEquals(AppConstants.minCardsInHand, gameState.player2.hand.size());
	}

}