
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Unit;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

public class Player1CardIDs {

	@Test
	public void Player1CardIDs() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initalized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		OrderedCardLoader.getPlayer1Cards();

		// Confirms that there are 20 cards in Player1's deck
		assertTrue(OrderedCardLoader.getPlayer1Cards().size() == 20);

		// Confirms that the ID in OrderedCardLoader matches the correct Unit ID for Player1
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(0).getId() == 0);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(1).getId() == 1);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(2).getId() == 2);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(3).getId() == 3);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(4).getId() == 4);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(5).getId() == 5);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(6).getId() == 6);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(7).getId() == 7);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(8).getId() == 8);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(9).getId() == 9);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(10).getId() == 10);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(11).getId() == 11);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(12).getId() == 12);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(13).getId() == 13);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(14).getId() == 14);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(15).getId() == 15);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(16).getId() == 16);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(17).getId() == 17);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(18).getId() == 18);
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(19).getId() == 19);

	}
}