import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
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

public class PlayerTurnTest {

	@Test
	public void Player1TurnTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		// This sets up the GameState and initializes the players. See Initialize.java to confirm what is instantiated
		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		//Confirms that when player1Turn is set to true, it is player1's turn else if it is false, it is player2's turn
		if (gameState.player1Turn == true) {
			assertTrue(gameState.player1Turn);
			
		} else {
			assertFalse(gameState.player1Turn);
		}
	}

}