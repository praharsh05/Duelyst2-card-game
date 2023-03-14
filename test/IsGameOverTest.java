import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.PerformAction;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Tile;
import utils.AppConstants;

public class IsGameOverTest {

	@Test
	public void IsGameOver() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// If -> Tests that isGameOver == true when player1's health is 0 and player1 has no remaining cards
		// Else if -> Tests that isGameOver == true when player1's health is 0 and player1 has no remaining cards
		// Else -> Tests that isGameOver is false if neither of these conditions are met

		if (gameState.player1.getHealth() <= 0
				|| (gameState.player1.getCardInDeck() == 0 && gameState.player1.getCardInHand() == 0)) {
			assertTrue(gameState.isGameOver);
		} else if (gameState.player2.getHealth() <= 0
				|| (gameState.player2.getCardInDeck() == 0 && gameState.player2.getCardInHand() == 0)) {
			assertTrue(gameState.isGameOver);
		} else {
			assertFalse(gameState.isGameOver);
		}

	}

}