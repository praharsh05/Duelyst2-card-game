import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Tile;
import utils.AppConstants;
import utils.BasicObjectBuilders;

public class BoardCreationTest {

	GameState gameState;
	CheckMessageIsNotNullOnTell altTell;
	Initalize initalizeProcessor ;
	@Before                                         
    public void setUp() {

		// First override the alt tell variable so we can issue commands without a running front-end
		altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		
		// As we are not starting the front-end, we have no GameActor, so lets manually create
		// the components we want to test
		gameState = new GameState(); // create state storage
		initalizeProcessor =  new Initalize(); // create an initalize event processor

    }
	
/** Test to check if tile object array is created in Board in the constructor call
 * 
 */
	@Test
	public void checkTileInitialized() {
		Board board = null; // Declare a board object
		
		// Call constructor method
		board = new Board(null);
		
		// Check whether the tile object array in the Board is not null
		assertNotNull("** Tiles array should not be NULL! **", board.getTiles());
		
		// Check whether the length of the tiles array in Board is equal to the boardWidth in AppConstants
		assertEquals("** The board width should be "+AppConstants.boardWidth, AppConstants.boardWidth,board.getTiles().length);
		
		// Check whether the length of the tiles[0][0] in Board is equal to the boardHeight in AppConstants
		assertEquals("** The board height should be "+AppConstants.boardHeight, AppConstants.boardHeight,board.getTiles()[0].length);
		
		// Check whether all tile object's width and height is '115'
		for(int i=0;i<board.getTiles().length;i++)
		{
			for(int j=0;j<board.getTiles()[i].length;j++)
			{
				// check whether the width of tile object at [i][j] is '115'
				assertEquals("** The tile width of ["+i+","+j+"] should be 115 ! **", 115, board.getTiles()[i][j].getWidth());
				
				// check whether the height of tile object at [i][j] is '115'
				assertEquals("** The tile height of ["+i+","+j+"] should be 115 ! **", 115, board.getTiles()[i][j].getHeight());
			}
		}
		

	}
	
	
/** Test to check if board object is created in GameState after receiving the 'Initialize' message
 * 
 */

	@Test
	public void checkBoardInitialized() {
		
		// set Board object as null
		assertNull(gameState.board);
		
		// lets simulate recieveing an initalize message
		ObjectNode eventMessage = Json.newObject(); // create a dummy message
		initalizeProcessor.processEvent(null, gameState, eventMessage); // send it to the initalize event processor
		
		// check if the updated board object is not null
		assertNotNull("** Board object should not be NULL! **", gameState.board);
		
		
		
	}
	
}
