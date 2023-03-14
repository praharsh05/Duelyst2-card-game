package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;

/**
 * Indicates that a unit instance has started a move. 
 * The event reports the unique id of the unit.
 * 
 * { 
 *   messageType = “unitMoving”
 *   id = <unit id>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class UnitMoving implements EventProcessor{

	public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		if(gameState.isGameActive) // if the frontend connection is active
		{
			gameState.clickMessage=message.get("messagetype");//message to keep track of previous click on front-end
			int unitid = message.get("id").asInt();


		}
		
	}

}
