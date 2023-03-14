package structures.basic;

import akka.actor.ActorRef;
import events.CardClicked;
import events.TileClicked;
import structures.GameState;

import java.util.ArrayList;

public class ComputerTiles {


    // as only one ai unit has a different summoning type, this is just for that card. (Planar scout)
    public static ArrayList<Tile> planarScoutSummonableTiles(GameState gameState, ActorRef out) {
        ArrayList<Tile> planarScoutTiles = new ArrayList<Tile>();
        planarScoutTiles = gameState.board.getTilesWithoutUnits(out, gameState.board.getTiles(), gameState.player2);
        return planarScoutTiles;
    }

    // only attack tile list needed that is different
    public static ArrayList<Tile> pyromancerAttackTiles(GameState gameState, ActorRef out) {
        ArrayList<Tile> pyromancerTiles = new ArrayList<Tile>();
        pyromancerTiles = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), TileClicked.opposingPlayer(gameState, gameState.player2));
        return pyromancerTiles;
    }

    // only movement difference
    public static ArrayList<Tile> windshrikeMovementTiles(GameState gameSate, ActorRef out) {
        ArrayList<Tile> windshrikeTiles = new ArrayList<Tile>();
        windshrikeTiles = gameSate.board.getTilesWithoutUnits(out, gameSate.board.getTiles(), gameSate.player2);
        return windshrikeTiles;
    }
}
