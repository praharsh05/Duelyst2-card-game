package structures.basic;

import actions.PerformAction;
import akka.actor.Actor;
import akka.actor.ActorRef;
import commands.BasicCommands;
import events.TileClicked;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

public class SpecialAbilities {


    // changed attackDirectly method to accommodate ranged attack
    public static boolean rangedAttack(ActorRef out, GameState gameState, Unit unit, Unit enemyUnit, Tile enemyTile, Tile startTile) {

        // Not a friendly unit --> attack
        EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
        BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        BasicCommands.playProjectileAnimation(out, projectile, 0, startTile, enemyTile);
        AppConstants.callSleep(AppConstants.attackSleepTime);

        int attackVal;

        if (enemyUnit.getSummonedID() == 1) // Should update player 1 avatar health
        {
            attackVal = gameState.player1.getAvatar().getHealth() - unit.getAttack();

            gameState.player1.getAvatar().setHealth(attackVal);  // update enemy's health
            enemyUnit.setHealth(attackVal); // update enemy's health front end

            // To avoid negative values as health
            if (gameState.player1.getAvatar().getHealth() < 0)
                gameState.player1.getAvatar().setHealth(0);

        } else if (enemyUnit.getSummonedID() == 2) // Should update player2 avatar health
        {

            attackVal = gameState.player2.getAvatar().getHealth() - unit.getAttack();
            gameState.player2.getAvatar().setHealth(attackVal);  // update enemy's health
            enemyUnit.setHealth(attackVal); // update enemy's health front end

            // To avoid negative values as health
            if (gameState.player2.getAvatar().getHealth() < 0)
                gameState.player2.getAvatar().setHealth(0);

        } else {
            attackVal = enemyUnit.getHealth() - unit.getAttack();
            enemyUnit.setHealth(attackVal);  // update enemy's health

            // To avoid negative values as health
            if (enemyUnit.getHealth() < 0)
                enemyUnit.setHealth(0);
        }

        // update front end
        BasicCommands.setUnitHealth(out, enemyUnit, enemyUnit.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, enemyUnit, enemyUnit.getAttack());
        AppConstants.callSleep(100);

        EffectAnimation ef = BasicObjectBuilders.loadEffect(AppConstants.effects[2]);
        BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.idle); // resets to idle after attack

        if (attackVal <= 0) // enemy unit dead, clear tile and update front end
        {
            gameState.summonedUnits.remove(enemyUnit);

            BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.death);
            AppConstants.callSleep(AppConstants.deathSleepTime);

            BasicCommands.playEffectAnimation(out, ef, enemyTile);
            AppConstants.callSleep(AppConstants.effectSleepTime);
            enemyTile.setUnitToTile(null);
            BasicCommands.deleteUnit(out, enemyUnit);
            AppConstants.callSleep(3000);
            return true;

        } else if (gameState.board.summonableTiles(out,startTile).contains(enemyTile)){ //enemy survived and is in range, counter attack

            attackVal = -1;

            BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.attack); // enemy attacks avatar
            AppConstants.callSleep(AppConstants.attackSleepTime);

            if (unit.getSummonedID() == 1) // Should update avatar health of player1
            {

                attackVal = gameState.player1.getAvatar().getHealth() - enemyUnit.getAttack();
                gameState.player1.getAvatar().setHealth(attackVal);
//		    	gameState.player1.setHealth(attackVal);
                unit.setHealth(attackVal); // update enemy's health front end


                // To avoid negative values as health
                if (gameState.player1.getAvatar().getHealth() < 0) {
                    gameState.player1.getAvatar().setHealth(0);
//			    	gameState.player1.setHealth(0);

                }

            } else if (unit.getSummonedID() == 2) // Should update avatar health of player2
            {

                attackVal = gameState.player2.getAvatar().getHealth() - enemyUnit.getAttack();
                gameState.player2.getAvatar().setHealth(attackVal);
//		    	gameState.player2.setHealth(attackVal);
                unit.setHealth(attackVal); // update enemy's health front end


                // To avoid negative values as health
                if (gameState.player2.getAvatar().getHealth() < 0) {
                    gameState.player2.getAvatar().setHealth(0);
//			    	gameState.player2.setHealth(0);

                }

            } else {
                attackVal = unit.getHealth() - enemyUnit.getAttack();
                unit.setHealth(attackVal); // update unit health

                // To avoid negative values as health
                if (unit.getHealth() < 0)
                    unit.setHealth(0);
            }

            // update front end
            BasicCommands.setUnitHealth(out, unit, unit.getHealth());
            AppConstants.callSleep(100);

            BasicCommands.setUnitAttack(out, unit, unit.getAttack());
            AppConstants.callSleep(100);

            BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.idle); // resets to idle after attack


            if (attackVal <= 0) //unit dead
            {
                gameState.summonedUnits.remove(unit);

                BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
                AppConstants.callSleep(AppConstants.deathSleepTime);

                BasicCommands.playEffectAnimation(out, ef, startTile);
                AppConstants.callSleep(AppConstants.effectSleepTime);
                startTile.setUnitToTile(null);
                BasicCommands.deleteUnit(out, unit);
                AppConstants.callSleep(100);
                return true;

            }

        }

        return true;
    }

    // checks the board for any enemy units to attack
    public static boolean attackUnitRanged(int mode, Player player, ActorRef out, Unit unit, Tile startTile, Tile enemyTile , GameState gameState) {
        // TODO Auto-generated method stub

        // Retrieve the unit from the corresponding tile position
        Unit enemyUnit=enemyTile.getUnitFromTile();
        gameState.startTile=startTile;

        if(enemyUnit!=null)
        {
            // Check whether the unit is a friendly unit or not
            if(enemyUnit.getIsPlayer()!=player.getID())
            {

                ArrayList<Tile> tilesList=gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), TileClicked.opposingPlayer(gameState,player));
                ArrayList<Tile> tilesProvoke = TileClicked.getProvokerTiles(out, gameState, player);

                if (unit.isProvoked() == true){
                    if (tilesProvoke.contains(enemyTile)){
                        rangedAttack(out, gameState,unit,enemyUnit,enemyTile,startTile);
                        return true;}
                }

                // If the enemyTile is in range of the startTile, attack
                if(tilesList.contains(enemyTile))
                {
                    return rangedAttack(out, gameState,unit,enemyUnit,enemyTile,startTile);


                }

            }else {

                if(mode==1)
                {
                    BasicCommands.addPlayer1Notification(out, "Please select an enemy unit to attack! ", 2);
                    AppConstants.callSleep(100);
                }

            }

        }
        return false;

    }

    // this method checks if one of two instances of the silverguard knight is on the board, and if so increase attack by 2. this will be called after the avatar takes damage
    public static void SilverguardKnightAbility(ActorRef out, GameState gameState){

        for (Unit unit: gameState.summonedUnits) {

            if (unit.getId()==3){
                Tile unitTile = unit.getTileFromUnit(unit.getId(), gameState, out);
                BasicCommands.addPlayer1Notification(out, "Silverguard Knight's attack +2", 2);
                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), unitTile);
                unit.setAttack(unit.getAttack() + 2);
                AppConstants.callSleep(50);
                BasicCommands.setUnitAttack(out, unit, unit.getAttack());
            }
            if (unit.getId()==10){
                Tile unitTile = unit.getTileFromUnit(unit.getId(), gameState, out);
                BasicCommands.addPlayer1Notification(out, "Silverguard Knight's attack +2", 2);
                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), unitTile);
                unit.setAttack(unit.getAttack() + 2);
                AppConstants.callSleep(50);
                BasicCommands.setUnitAttack(out, unit, unit.getAttack());
            }

        }
    }

    public static void purebladeEnforcerAbility(ActorRef out, GameState gameState){

        for (Unit unit: gameState.summonedUnits) {

            if (unit.getName().equals("Pureblade Enforcer")){
                unit.setAttack(unit.getAttack()+1);
                unit.setHealth(unit.getHealth()+1);
                AppConstants.callSleep(50);
                BasicCommands.setUnitHealth(out,unit,unit.getHealth());
                BasicCommands.setUnitAttack(out, unit,unit.getAttack());
                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), unit.getTileFromUnit(unit.getId(), gameState,out));
                BasicCommands.addPlayer1Notification(out, "Pureblade Enforcer attack and health +1", 2);
            }
        }
    }

    public static ArrayList<Unit> provoke(ActorRef out, GameState gameState, Player player,Unit unit){
        if (player.getID() == 1) {
            ArrayList<Tile> provokedTiles = gameState.board.summonableTiles(out, unit.getTileFromUnitP2(unit.getId(), gameState, out));
            ArrayList<Unit> provokedUnits = new ArrayList<Unit>();

            for (Tile tile : provokedTiles){
                if (tile.getUnitFromTile()!=null && tile.getUnitFromTile().getIsPlayer() == 1){
                    Unit unitP = tile.getUnitFromTile();
                    unitP.setMoved(true);
                    unitP.setProvoked(true);
                    provokedUnits.add(unitP);
//                    System.out.println(unitP.getName());
                }
            }return provokedUnits;
        }
        else {
            ArrayList<Tile> provokedTiles = gameState.board.summonableTiles(out, unit.getTileFromUnit(unit.getId(), gameState, out));
            ArrayList<Unit> provokedUnits = new ArrayList<Unit>();

            for (Tile tile : provokedTiles){
                if (tile.getUnitFromTile()!=null && tile.getUnitFromTile().getIsPlayer() ==2){
                    Unit unitP = tile.getUnitFromTile();
                    unitP.setMoved(true);
                    unitP.setProvoked(true);
                    provokedUnits.add(unitP);
//                    System.out.println(unitP.getName());

                }
            }return provokedUnits;

        }
        }



    public static ArrayList<Unit> getProvokingUnits(ActorRef out, GameState gameState, Player player){
        ArrayList<Unit> units = new ArrayList<>();
        ArrayList<Tile> tiles = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), player);

        for (Tile tile: tiles) {
            if (tile.getUnitFromTile().getName().equals("Rock Pulveriser") || tile.getUnitFromTile().getName().equals("Ironcliff Guardian")
                    || tile.getUnitFromTile().getName().equals("Silverguard Knight")){
                Unit unit = tile.getUnitFromTile();
                units.add(unit);
//                System.out.println(unit.getName());
            }
        }
        return units;
    }

    public static void blazeHound(ActorRef out, GameState gameState){
        gameState.player1.drawAnotherCard(out,1);
        gameState.player2.drawAnotherCard(out,2);
        BasicCommands.addPlayer1Notification(out, "Drawing another card",2);
    }

    public static void windshrikeMove(int mode, ActorRef out, Tile startTile, Tile endTile,GameState gameState) {

        Unit unitToMove = startTile.getUnitFromTile();

        // Check if there is a unit on the start tile
        if(unitToMove == null) {
            if(mode==1)
            {
                BasicCommands.addPlayer1Notification(out, "No unit on the starting tile", 2);
                AppConstants.callSleep(200);
            }
            return;
        }
        // Check if the end tile is empty
        if(endTile.getUnitFromTile() != null) {
            if(mode==1)
            {
                BasicCommands.addPlayer1Notification(out, "The end tile is already occupied", 2);
                AppConstants.callSleep(200);
            }
            return;
        }
        startTile.setUnitToTile(null); //Update starttile unit to null
        unitToMove.setMoved(true);
        // Move the unit to the end tile
        gameState.board.addUnitToBoard(endTile.getTilex(), endTile.getTiley(), unitToMove);
        AppConstants.callSleep(50);
        BasicCommands.moveUnitToTile(out, unitToMove, endTile);
        unitToMove.setPositionByTile(endTile);
        AppConstants.callSleep(50);
    }

    public static void windshrikeDeathCheck(ActorRef out, GameState gameState, Unit unit){

        if (unit.getName().equals("WindShrike")){
            gameState.player2.drawAnotherCard(out, gameState.player2.getID());
            BasicCommands.addPlayer1Notification(out, "WindShrike dead, computer draws a card!", 2);

        }
    }


}


