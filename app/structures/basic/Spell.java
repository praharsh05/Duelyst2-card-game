package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.OtherClicked;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

public class Spell {

    int manaCost;
    int id;

    public Spell(ActorRef out, int manaCost, int id) {
        this.manaCost = manaCost;
        this.id = id;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // methods forming a basic representation of the spells and unfinished logic as to how they will effect the game
    public static void truestike(ActorRef out, Card card, Tile tile, GameState gameState) { // if truestrike is played, deal 2 damage to the unit

        int hp;

            Unit unitToAttack = tile.getUnitFromTile();

           if (unitToAttack.getIsPlayer()==2) {

                gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
                gameState.player1.setMana(gameState.player1.getMana()-card.getManacost());//decrease the mana
                gameState.player1.setPlayer(out);//reflecting the mana on board
                gameState.player1.deleteCardInHand(out, gameState.player1.getID(), gameState);//delete the card in hand
                AppConstants.callSleep(500);
                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), tile);
                AppConstants.callSleep(100);

                if (unitToAttack.getSummonedID()==2) {

                    hp = gameState.player2.getAvatar().getHealth()-2;
                    gameState.player2.getAvatar().setHealth(hp);
                    unitToAttack.setHealth(hp);
                    BasicCommands.setUnitHealth(out, unitToAttack, hp);
                    AppConstants.callSleep(100);

                    if(gameState.player2.getAvatar().getHealth()<0)
                        gameState.player2.getAvatar().setHealth(0);

                }
                else  {
                    hp = unitToAttack.getHealth()-2;
                    if(hp<0) {
                        unitToAttack.setHealth(0);
                        BasicCommands.setUnitHealth(out, unitToAttack, 0);
                    }

                    else {unitToAttack.setHealth(hp);
                        BasicCommands.setUnitHealth(out, unitToAttack, hp);
                    }


                }
                if(hp<=0) // enemy unit dead, clear tile and update front end
                {
                    SpecialAbilities.windshrikeDeathCheck(out, gameState, unitToAttack);
                    gameState.summonedUnits.remove(unitToAttack);

                    BasicCommands.playUnitAnimation(out, unitToAttack, UnitAnimationType.death);
                    AppConstants.callSleep(AppConstants.deathSleepTime);
                    EffectAnimation ef = BasicObjectBuilders.loadEffect(AppConstants.effects[2]);
                    BasicCommands.playEffectAnimation(out, ef, tile);
                    AppConstants.callSleep(AppConstants.effectSleepTime);
                    tile.setUnitToTile(null);
                    BasicCommands.deleteUnit(out, unitToAttack);
                    AppConstants.callSleep(1000);

                }
            }
        else if (unitToAttack.getIsPlayer() == 1){
            OtherClicked.clearCardClicked(out, gameState, gameState.player1);//clear highlighting
            BasicCommands.addPlayer1Notification(out, "Please select an enemy unit", 2);}

    }

    public static void sundropElixir(ActorRef out, Card card, Tile tile, GameState gameState) {  // if sundropElixir is played, heal 5 to a unit (this must not take the unit over its starting health value)

        Spell sundropElixir = new Spell(out, 1, 8);


            Unit unitToHeal = tile.getUnitFromTile();

            if (unitToHeal.getIsPlayer()==1) {
                if (unitToHeal.getHealth() == unitToHeal.getMaxHealth()){
                    OtherClicked.clearCardClicked(out, gameState, gameState.player1);//clear highlighting
                    BasicCommands.addPlayer1Notification(out, "Health already full!", 2);
                    return;
                }
                gameState.player1.setMana(gameState.player1.getMana()-card.getManacost());//decrease the mana
                gameState.player1.setPlayer(out);//reflecting the mana on board

                gameState.player1.deleteCardInHand(out, gameState.player1.getID(), gameState);//delete the card in hand
                AppConstants.callSleep(200);
                gameState.board.clearTileHighlighting(out, gameState.board.allTiles());

                if ((unitToHeal.getHealth() + 5) > unitToHeal.getMaxHealth()){

                    if(unitToHeal.getId()==40){
                        BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                        gameState.player1.getAvatar().setHealth(AppConstants.playerMaxHealth);
                        unitToHeal.setHealth(gameState.player1.getAvatar().getHealth());
                        AppConstants.callSleep(100);
                        BasicCommands.setUnitHealth(out, gameState.summonedUnits.get(0), gameState.player1.getAvatar().getHealth());
                    }
                    else{
                    BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                    unitToHeal.setHealth(unitToHeal.getMaxHealth());
                    AppConstants.callSleep(100);
                    BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getMaxHealth());}
                }

                else {
                    if(unitToHeal.getId()==40){
                        BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                        gameState.player1.getAvatar().setHealth(gameState.player1.getAvatar().getHealth() + 5);
                        unitToHeal.setHealth(gameState.player1.getAvatar().getHealth());
                        AppConstants.callSleep(100);
                        BasicCommands.setUnitHealth(out, gameState.summonedUnits.get(0), gameState.player1.getAvatar().getHealth());
                    }
                    else{
                    BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                    unitToHeal.setHealth(unitToHeal.getHealth() + 5);
                    AppConstants.callSleep(100);
                    BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());}
                }

            } else if (unitToHeal.getIsPlayer() == 2){
                OtherClicked.clearCardClicked(out, gameState, gameState.player1);//clear highlighting
                BasicCommands.addPlayer1Notification(out, "Please select a friendly unit", 2);
            }
    }

    public static void staffOfYKir(ActorRef out, Card card, Tile tile, GameState gameState) { // if staffOfYKir is played, add 2 attack to your avatar

        Spell staffOfYKir = new Spell(out, 2, 22);
        Unit unitFromTile = tile.getUnitFromTile();
		AppConstants.printLog("<-------- AI :: staffOfYKir():: unitFromTile: "+unitFromTile);
		AppConstants.printLog("<-------- AI :: staffOfYKir():: (unitFromTile.getSummonedID(): "+unitFromTile.getSummonedID());

            if (unitFromTile.getSummonedID() == 2) {
//            if (gameState.summonedUnits.get(1).getId()==41) {
            gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
            gameState.player2.setMana(gameState.player2.getMana()-card.getManacost());//decrease the mana
            gameState.player2.setPlayer(out);//reflecting the mana on board
            gameState.player2.deleteCardInHand(out, gameState.player2.getID(), gameState);//delete the card in hand
            AppConstants.callSleep(500);
            BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
            int newAttack = gameState.player2.getAvatar().getAttack() + 2;
            gameState.player2.getAvatar().setAttack(newAttack);
            unitFromTile.setAttack(newAttack);
            AppConstants.callSleep(100);
            BasicCommands.setUnitAttack(out, unitFromTile, newAttack);

            SpecialAbilities.purebladeEnforcerAbility(out,gameState);//spell card played increase stats

            }
    }

    public static void entropicDecay(ActorRef out, Card card, Tile tile, GameState gameState) { // if entropicDecay is played, reduce a non-avatar unit to 0 health (KILL THEM)

        Spell entropicDecay = new Spell(out, 5, 27);


            Unit unitToKill = tile.getUnitFromTile();

            if (unitToKill != null && unitToKill.getSummonedID() != 1 && unitToKill.getIsPlayer() == 1) {

                gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
                gameState.player2.setMana(gameState.player2.getMana()-card.getManacost());//decrease the mana
                gameState.player2.setPlayer(out);//reflecting the mana on board
                gameState.player2.deleteCardInHand(out, gameState.player2.getID(), gameState);//delete the card in hand
                AppConstants.callSleep(500);

                unitToKill.setHealth(0);
                AppConstants.callSleep(100);
                BasicCommands.setUnitHealth(out, unitToKill, 0);
                gameState.summonedUnits.remove(unitToKill);
                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), tile);
                BasicCommands.playUnitAnimation(out, unitToKill, UnitAnimationType.death);
                AppConstants.callSleep(AppConstants.deathSleepTime);
                EffectAnimation ef = BasicObjectBuilders.loadEffect(AppConstants.effects[2]);
//                BasicCommands.playEffectAnimation(out, ef, tile);
                AppConstants.callSleep(AppConstants.effectSleepTime);
                tile.setUnitToTile(null);
                BasicCommands.deleteUnit(out, unitToKill);
                AppConstants.callSleep(1000);

                SpecialAbilities.purebladeEnforcerAbility(out,gameState);//spell card played increase stats

            }
    }

    // this method will highlight the enemy units on the board. the player parameter must take in the enemy player (the player whose turn it is NOT)
    public void highlightEnemyUnits(ActorRef out, GameState gameSate, Player player) {

        ArrayList<Tile> tileList = gameSate.board.getTilesWithUnits(out, gameSate.board.getTiles(), player);

        gameSate.board.highlightTilesRed(out, tileList);

    }

    //this method will highlight all friendly units white. player paramet must take in the current player (the player whose turn it IS)
    public void highlighFriendlyUnits(ActorRef out, GameState gameSate, Player player) {

        ArrayList<Tile> tileList = gameSate.board.getTilesWithUnits(out, gameSate.board.getTiles(), player);

        gameSate.board.highlightTilesWhite(out, tileList);
    }
}


