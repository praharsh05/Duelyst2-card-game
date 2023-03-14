package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import commands.BasicCommands;
import demo.CheckMoveLogic;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.*;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import structures.basic.Board;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.BetterUnit;


/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * <p>
 * {
 * messageType = “initalize”
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class Initalize implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        gameState.gameInitalised = true;
        gameState.clickMessage=message.get("messagetype");//initializing the clickMessage here


		// Create a board object and assign it to the gameState board object
        gameState.board = new Board(out);


        // creating the avatar object
        Unit avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 40, Unit.class);

        // Change by using the returnTile method instead of creating tile objects here
        // placing avatar on board and setting stats
        gameState.avatar =  new BetterUnit(out,avatar, gameState.board.returnTile(1,2), gameState.board);
        avatar.setIsPlayer(1);
        avatar.setSummonedID(1);
        avatar.setName("Avatar"); // needs name set otherwise it throw null pointer exception when checking unit names
        // creating the player object and passing the avatar object to allow the players health to be set to the avatars.
        gameState.player1 = new Player(1,out,gameState.avatar, AppConstants.p1unit);
        AppConstants.callSleep(200);
        gameState.player1Turn=true;
		
        gameState.player1.setCurrentTile(gameState.board.returnTile(1,2)); // Set player current tile

        //setting the deck as an ArrayList
        gameState.player1.createDeck(gameState.player1.getID());
        AppConstants.callSleep(200);
        
        // loading the units for player 1
        // gameState.player1.createPlayer1Units(out);
        gameState.player1.createUnits(gameState.player1);

        //Setting the hand as an ArrayList
        gameState.player1.setHand(out,1);
        AppConstants.callSleep(200);


        //************************************// COMPUTER PLAYER //******************************************
		// creating ai avatar object
        Unit aiAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 41, Unit.class);

        // Change by using the returnTile method instead of creating tile objects here
        // placing avatar on board and setting stats
        gameState.aiAvatar = new BetterUnit(out, aiAvatar, gameState.board.returnTile(7,2), gameState.board);
        aiAvatar.setIsPlayer(2);
        aiAvatar.setSummonedID(2);
        aiAvatar.setName("AI Avatar"); // needs name set otherwise it throw null pointer exception when checking unit names

        // creating the player object and passing the avatar object to allow the players health to be set to the avatars.
        gameState.player2 = new ComputerPlayer(2,out, gameState.aiAvatar, AppConstants.p2unit);
        AppConstants.callSleep(200);

        gameState.player2.setCurrentTile(gameState.board.returnTile(7,2)); // Set player current tile

        //print message to the terminal notifying the start of the draw card method
        //setting the deck as an ArrayList
        gameState.player2.createDeck(gameState.player2.getID());
        AppConstants.callSleep(200);

        // loading the units for player 2
        // gameState.player2.createPlayer2Units(out);
        gameState.player2.createUnits(gameState.player2);
        //Setting the hand as an ArrayList
        gameState.player2.setHand(out,2);
        AppConstants.callSleep(200);

        
        // Add Player avatars to summoned Units arraylist
        gameState.summonedUnits.add(avatar);
        gameState.summonedUnits.add(aiAvatar);
        
       
        AppConstants.printLog("------> Game Board and player initialized!");

        //give notification to player that initialization is done
        BasicCommands.addPlayer1Notification(out, "Game Started, Your Turn", 2);
       
        // User 1 makes a change
        //CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
        //CheckMoveLogic.executeDemo(out);
    }




}


