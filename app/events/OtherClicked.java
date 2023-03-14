package events;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import utils.AppConstants;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		if(gameState.isGameActive) // if the frontend connection is active
		{
			cardClick=message.get("messagetype");//message to keep track of current click on front-end

			if(gameState.clickMessage != cardClick){
				if(gameState.SummonTileList != null){//to check if cardClick happened
					if(gameState.player1Turn) clearCardClicked(out, gameState, gameState.player1);//clear for player1
					
				}
                gameState.clickMessage=cardClick;//update the gameState on this click
            }
			
			gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
			TileClicked.setStartTile(false) ;//to set the move to false
		}
	}

	//method to clear the cardClicked functionality
 
	public static void clearCardClicked( ActorRef out, GameState gameState, Player player){
		CardClicked.clearHighlightMiniCard(out, gameState);//clear highlighting of miniCards

		// Clear all tiles and set SummonTileList to null
		gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
		gameState.SummonTileList=null;
	}

}

