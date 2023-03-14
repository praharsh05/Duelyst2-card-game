
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

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

public class Player2CardIDs {
	
	@Test
	public void Player2CardIDs() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initalized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		OrderedCardLoader.getPlayer2Cards();

		// Confirms that there are 20 cards in Player2's deck
		assertTrue(OrderedCardLoader.getPlayer2Cards().size() == 20);

		// Confirms that the ID in OrderedCardLoader matches the correct Unit ID for Player2
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(0).getId() == 20);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(1).getId() == 21);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(2).getId() == 22);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(3).getId() == 23);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(4).getId() == 24);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(5).getId() == 25);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(6).getId() == 26);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(7).getId() == 27);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(8).getId() == 28);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(9).getId() == 29);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(10).getId() == 30);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(11).getId() == 31);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(12).getId() == 32);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(13).getId() == 33);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(14).getId() == 34);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(15).getId() == 35);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(16).getId() == 36);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(17).getId() == 37);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(18).getId() == 38);
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(19).getId() == 39);


//			assertTrue(OrderedCardLoader.getPlayer1Cards().get(0) == (StaticConfFiles.c_comodo_charger, 0, Card.class)));
//			if (OrderedCardLoader.getPlayer1Cards().get(0).getId() == 0) {
		

	}
}
