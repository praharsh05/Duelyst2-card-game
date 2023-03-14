
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
import structures.basic.Card;
import structures.basic.Player;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

public class CardNamesMatchCardIDs {

	@Test
	public void Player1CardIDNameMatch() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initialized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		assertTrue(OrderedCardLoader.getPlayer1Cards().get(0).getCardname().equals("Comodo Charger"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(1).getCardname().equals("Pureblade Enforcer"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(2).getCardname().equals("Fire Spitter"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(3).getCardname().equals("Silverguard Knight"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(4).getCardname().equals("Truestrike"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(5).getCardname().equals("Azure Herald"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(6).getCardname().equals("Ironcliff Guardian"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(7).getCardname().equals("Azurite Lion"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(8).getCardname().equals("Sundrop Elixir"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(9).getCardname().equals("Hailstone Golem"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(10).getCardname().equals("Silverguard Knight"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(11).getCardname().equals("Fire Spitter"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(12).getCardname().equals("Comodo Charger"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(13).getCardname().equals("Pureblade Enforcer"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(14).getCardname().equals("Truestrike"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(15).getCardname().equals("Azure Herald"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(16).getCardname().equals("Ironcliff Guardian"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(17).getCardname().equals("Azurite Lion"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(18).getCardname().equals("Sundrop Elixir"));
		assertTrue(OrderedCardLoader.getPlayer1Cards().get(19).getCardname().equals("Hailstone Golem"));

	}
	
	@Test
	public void Player2CardIDNameMatch() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initialized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		assertTrue(OrderedCardLoader.getPlayer2Cards().get(0).getCardname().equals("Rock Pulveriser"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(1).getCardname().equals("Bloodshard Golem"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(2).getCardname().equals("Staff of Y'Kir'"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(3).getCardname().equals("Blaze Hound"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(4).getCardname().equals("WindShrike"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(5).getCardname().equals("Pyromancer"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(6).getCardname().equals("Serpenti"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(7).getCardname().equals("Entropic Decay"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(8).getCardname().equals("Planar Scout"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(9).getCardname().equals("Hailstone Golem"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(10).getCardname().equals("Rock Pulveriser"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(11).getCardname().equals("Bloodshard Golem"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(12).getCardname().equals("Staff of Y'Kir'"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(13).getCardname().equals("Blaze Hound"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(14).getCardname().equals("WindShrike"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(15).getCardname().equals("Pyromancer"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(16).getCardname().equals("Serpenti"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(17).getCardname().equals("Entropic Decay"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(18).getCardname().equals("Planar Scout"));
		assertTrue(OrderedCardLoader.getPlayer2Cards().get(19).getCardname().equals("Hailstone Golem"));

	}

}