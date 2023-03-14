package events;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.databind.JsonNode;

import actions.PerformAction;
import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;

/**
 * In the user’s browser, the game is running in an infinite loop, where there is around a 1 second delay 
 * between each loop. Its during each loop that the UI acts on the commands that have been sent to it. A 
 * heartbeat event is fired at the end of each loop iteration. As with all events this is received by the Game 
 * Actor, which you can use to trigger game logic.
 * 
 * { 
 *   String messageType = “heartbeat”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Heartbeat implements EventProcessor{

	// Declare timer task variables
	public Timer heartbeatTimer=null;
	public TimerTask heartbeatTimerTask=null;
	
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		if(gameState.player1!=null && gameState.player2!=null) // To avoid NULLpointerexception
    	{
    		gameState.player1.syncHealth();
    		gameState.player1.setPlayerHealth(out);
    		gameState.player2.syncHealth();
    		gameState.player2.setPlayerHealth(out); 
    		
    		//to check if the game ended or not
			PerformAction.gameEnd(out, gameState);
        
    	}
		
		 // Comment below lines if you want to reset backend after 60 minutes of inactivity of the frontend communication
		if(gameState.isGameOver==false)
			gameState.isGameActive=true;
		else
			gameState.isGameActive=false;
		

	}
	
	 
/** This method starts a timer task and will be executed periodically at an interval gap of
 * the time set in 'AppConstants.gameTaskInterval' variable.
 * 
 * Longer timegap between latest time of heartbeat reception and current time indicates inactive
 * connection between the front end and the back end. So stop the back end execution.
 * 
 * If the timegap is more than 60minutes, it will display a message about the inactivity of the user and asks user to 
 * reload the game for effective working
 * @param gameState
 */
	
	 private void startHeartbeatTaskTimer(ActorRef out,GameState gameState) {
		 heartbeatTimer = new Timer();
		 heartbeatTimerTask = new TimerTask() {
	            public void run() {
	            	
        			
	            	// Calculate the time gap between the latest heartbeat receival time and current time
	            	long timeDifference = System.currentTimeMillis()-gameState.lastHeartbeatTime;
	            	// If the time gap is more than the allowed time gap, reset game variables and stop timer.
	            	if(timeDifference>AppConstants.allowedHeartbeatTimeGap || gameState.isGameOver==true)
	            	{

	        			AppConstants.printLog("------> The game was not active for more than 15 minutes. Please reload the browser! ");
	        			gameState.isGameActive=false;
	        			gameState.clearStateVariables();
	            		stopGameTaskTimer();
	            		
	            	}
	               
	            }
	        };
	        heartbeatTimer.schedule(heartbeatTimerTask, new Date(),AppConstants.gameTaskInterval);
	    }
	 
/** This method stops the already started timer task
 * 
 */

	 private void stopGameTaskTimer() {
	        if (heartbeatTimer != null) {
	        	heartbeatTimer.cancel();
	            heartbeatTimer.purge();
	            heartbeatTimer = null;
	        }
	    }

}


