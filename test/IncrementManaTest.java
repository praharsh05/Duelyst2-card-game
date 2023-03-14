import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.EndTurnClicked;
import events.EventProcessor;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Player;
import utils.AppConstants;

public class IncrementManaTest {

	@Test
	public void IncrementManaTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initalized

		// This sets up the GameState and initializes the players. See Initialize.java to confirm what is instantiated
		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// Creating a new EndTurnClicked object to get to processEvent, which has the logic for switching the player turns
		EndTurnClicked e = new EndTurnClicked();

		// Ends player1's turn
		e.processEvent(null, gameState, eventmessage);

		// Checks that human player's getMana equals the turn number + 1 as the turn changes
		assertTrue(gameState.player1.getMana() == gameState.playerTurnNumber + 1);

		// Ends player2's turn
		e.processEvent(null, gameState, eventmessage);

		// Checks that AI player's getMana equals the turn number + 1 as the turn changes
		assertTrue(gameState.player2.getMana() == gameState.compTurnNumber + 1);
		
	}

}