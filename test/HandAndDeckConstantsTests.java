import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import utils.AppConstants;

public class HandAndDeckConstantsTests {

	@Test
	public void maxHandSize() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Tests that the maximum number of cards in the AI player's hand is 6
		assertEquals(AppConstants.maxCardsInHand, 6);
	}

	@Test
	public void maxDeckSize() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Tests that the maximum number of cards in the AI player's hand is 6
		assertEquals(AppConstants.maxCardsInDeck, 20);

	}

}