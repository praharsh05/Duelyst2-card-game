package structures.basic;

import akka.actor.ActorRef;
import akka.util.Collections;
import commands.BasicCommands;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;


/**
 * The board class will contain tile objects stored in a 2D array data structure and will contain
 * methods to set the constraints of the available moves and total size of the board (9x5).
 * The Board object consists of several tile objects.
 */

public class Board {

    Tile[][] tiles = null;

    public Board(ActorRef out) {
        tiles = new Tile[AppConstants.boardWidth][AppConstants.boardHeight];
        setTiles(out);
    }

    /**
     * This method creates tile objects and assign those tiles to the board object.
     *
     * @param out
     */

    public void setTiles(ActorRef out) {

        // Create a tile object
        Tile tile;

        // Iterate through the tiles array
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                // Create a new tile object
                tile = BasicObjectBuilders.loadTile(i, j);

                // Assign that tile object to the array position
                tiles[i][j] = tile;

                // Draw the tile on the front end
//                BasicCommands.drawTile(out, tile, 0);
                drawTileWithSleep(out, tile, 0, AppConstants.drawTileSleepTime);

            }
        }

    }


    /**
     * Getter method to return tiles objects of a board
     *
     * @return Tile
     */

    public Tile[][] getTiles() {
        return this.tiles;
    }

    /**
     * this method will take in an x and y parameter and return the tile object at that position
     *
     * @param x
     * @param y
     * @return
     */
    public Tile returnTile(int x, int y) {
        return tiles[x][y];
    }


    /**
     * This method will take in a tile and return an ArrayList of the two cardinal and
     * one diagonal tiles available for a standard move in the game
     *
     * @param out
     * @param tile
     * @return
     */
    public ArrayList<Tile> getAdjacentTiles(ActorRef out, Tile tile) {

        // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();

        // for the cardinal tiles I will check if the tile if either 2 or 1 tiles away from the edge of the board so
        // we avoid any nullPointExceptions

        // checking for the top-most tiles
        if (y > 1) {
            adjacentTiles.add(returnTile(x, y - 1));
            adjacentTiles.add(returnTile(x, y - 2));
        } else if (y == 1) {
            adjacentTiles.add(returnTile(x, y - 1));
        }

        // checking for the right-most tiles
        if (x < AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x + 1, y));
            adjacentTiles.add(returnTile(x + 2, y));
        } else if (x == AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x + 1, y));
        }

        // checking for the bottom-most tiles
        if (y < AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y + 1));
            adjacentTiles.add(returnTile(x, y + 2));
        } else if (y == AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y + 1));
        }

        // checking for the left-most tiles
        if (x > 1) {
            adjacentTiles.add(returnTile(x - 1, y));
            adjacentTiles.add(returnTile(x - 2, y));
        } else if (x == 1) {
            adjacentTiles.add(returnTile(x - 1, y));
        }

        // top-right
        if (x < AppConstants.boardWidth - 1 && y > 0) {
            adjacentTiles.add(returnTile(x + 1, y - 1));
        }

        // bottom-right
        if (x < AppConstants.boardWidth - 1 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x + 1, y + 1));
        }

        // bottom-left
        if (x > 0 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x - 1, y + 1));
        }

        // top-left
        if (x > 0 && y > 0) {
            adjacentTiles.add(returnTile(x - 1, y - 1));
        }

        return adjacentTiles;
    }

    public ArrayList<Tile> summonableTiles(ActorRef out, Tile tile) { // reteive list of summonable tiles

        // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();


        // checking for the left-most tiles
        if (x > 0) {
            adjacentTiles.add(returnTile(x - 1, y));
        }
        
        
        // checking for the top-most tiles
        if (y > 0) {
            adjacentTiles.add(returnTile(x, y - 1));
        }
        
        // checking for the bottom-most tiles
        if (y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x, y + 1));
        }

        // checking for the right-most tiles
        if (x < AppConstants.boardWidth - 1) {
            adjacentTiles.add(returnTile(x + 1, y));
        }

       

        // bottom-left
        if (x > 0 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x - 1, y + 1));
        }

        // top-left
        if (x > 0 && y > 0) {
            adjacentTiles.add(returnTile(x - 1, y - 1));
        }
        
        // top-right
        if (x < AppConstants.boardWidth - 1 && y > 0) {
            adjacentTiles.add(returnTile(x + 1, y - 1));
        }

        // bottom-right
        if (x < AppConstants.boardWidth - 1 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x + 1, y + 1));
        }

        return adjacentTiles;
    }


    
    /** This method will take a tile and finds it's adjacent tiles to move and it will find the 
     * attackable unit (if present) to those adjacent tiles and highlight those tiles.
     * 
     * Note: if mode==0, this function only returns the tiles list 
     * 		 if mode==1, this function returns the tiles list and updates front end from here itself.
     * 
     * @param mode --> if mode==1, highlighitng | if mode==0, clearhighlighting
     * 
     * @param player
     * @param out
     * @param tile
     * @param gameState
     * @return
     */
    

	public ArrayList<Tile> highlightTilesMoveAndAttack(int mode, Player player, ActorRef out, Tile tile, GameState gameState) {

		
		 // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

		 // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();
        int newx;
        int newy;
        Tile newTile;

        
        int idx=0;
        for(int i=-2;i<3;i++)
        {
        	
        	for(int j=(-1*idx);j<=idx;j++)
        	{
        		
        		newx=x+i;
        		newy=y+j;

        		if((newx>=0 && newx<AppConstants.boardWidth)&&(newy>=0 && newy<AppConstants.boardHeight))
        		{
        			newTile=returnTile(newx, newy);
            		
        			if(newTile!=tile) // No need to highlight starttile
        			{
        				if(j==(-1*idx) || j==idx) // Check for attackable units
        				{
        					
        					if(newTile.getUnitFromTile()==null)
        					{
        						ArrayList<Tile> attackableTiles=getAdjacentTilesToAttack(player, out, tile,newTile);
        						adjacentTiles.addAll(attackableTiles);
//    							gameState.board.highlightTilesRed(out, attackableTiles);

            					if(mode==1) //highlight , else clear
            						gameState.board.highlightTilesRed(out,attackableTiles); // update front end
        					}
        					

        				}

        				
        				if(newTile.getUnitFromTile()!=null)
        				{
	        				if(newTile.getUnitFromTile().getIsPlayer()!=tile.getUnitFromTile().getIsPlayer())
	        				{
      					
	            				adjacentTiles.add(newTile);

	        					if(mode==1)
	        					{
	        			                drawTileWithSleep(out, newTile, 2, AppConstants.drawTileSleepTime);
	        						
	        					}
	        						
	        				}
        				}else {
            				adjacentTiles.add(newTile);

        					if(mode==1)
        					{
        						drawTileWithSleep(out, newTile, 1, AppConstants.drawTileSleepTime);
        					}

        				}
    					
        			}

        		}

        		
        	}
        	if(i<0)
        		idx++;
        	else
        		idx--;
        }
        
        return adjacentTiles;
	}

    /**
     * This method will take in a tile and return an ArrayList of the two cardinal and
     * one diagonal tiles available for a standard attack in the game
     *
     * @param out
     * @param tile
     * @return
     */
    public ArrayList<Tile> getAdjacentTilesToAttack(Player player,ActorRef out, Tile tile) {

        // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();
        int newx;
        int newy;
        Tile newTile;
        
        for(int i=-1;i<2;i++)
        {
        	for(int j=-1;j<2;j++)
        	{
        		
        		newx=x+i;
        		newy=y+j;

        		if((newx>=0 && newx<AppConstants.boardWidth)&&(newy>=0 && newy<AppConstants.boardHeight))
        		{
        			newTile=returnTile(newx, newy);

        			if(newTile!=tile && newTile.getUnitFromTile()!=null) // Check if the attackable tile has any unit present
        			{

        				if(newTile.getUnitFromTile().getIsPlayer()!=player.getID())
            			adjacentTiles.add(newTile);

        			}

        		}

        		
        	}
        }



        return adjacentTiles;
    }

    /**
     * This method is used only for
     * 		- Highlight move and attack tiles
     * 		- attack tiles after moving (not direct attack)
     * 
     * This method will take in a tile (from loop) and an actual startTile(where unit is clicked) and return an ArrayList of the two cardinal and
     * one diagonal tiles available for a standard attack in the game
     *
     * @param player
     * @param out
     * @param startTile
     * @param tile
     * @return
     */
    public ArrayList<Tile> getAdjacentTilesToAttack(Player player,ActorRef out, Tile startTile,Tile tile) {

        // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();
        int newx;
        int newy;
        Tile newTile;
        
        for(int i=-1;i<2;i++)
        {
        	for(int j=-1;j<2;j++)
        	{
        		
        		newx=x+i;
        		newy=y+j;

        		if((newx>=0 && newx<AppConstants.boardWidth)&&(newy>=0 && newy<AppConstants.boardHeight))
        		{

        			newTile=returnTile(newx, newy);

        			if(newTile!=startTile && newTile!=tile && newTile.getUnitFromTile()!=null) // Check if the attackable tile has any unit present
        			{

        				if(newTile.getUnitFromTile().getIsPlayer()!=startTile.getUnitFromTile().getIsPlayer())
        					adjacentTiles.add(newTile);

        			}

        		}

        		
        	}
        }



        return adjacentTiles;
    }

    /**
     * This method is used only to
     * 		- retrieve nearby tiles in attack pattern
     * 
     * 
     * This method will take in a tile (from loop) and return an ArrayList of the tiles
     * in the attackable pattern.
     * Even if the tile contains unit on it, it will return that tile
     *
     * @param player
     * @param out
     * @param startTile
     * @param tile
     * @return
     */
    
	public ArrayList<Tile> retrieveAdjacentTilesToAttackPosition(ActorRef out, Tile tile) {

		// arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();
        int newx;
        int newy;
        Tile newTile;
        
        for(int i=-1;i<2;i++)
        {
        	for(int j=-1;j<2;j++)
        	{
        		
        		newx=x+i;
        		newy=y+j;
//        		AppConstants.printLog("New xy: ["+newx+","+newy+"]");

        		if((newx>=0 && newx<AppConstants.boardWidth)&&(newy>=0 && newy<AppConstants.boardHeight))
        		{
        			newTile=returnTile(newx, newy);

        			if(newTile!=tile) // Check if the attackable tile has any unit present
        			{
              			adjacentTiles.add(newTile);

        			}

        		}

        		
        	}
        }



        return adjacentTiles;
	}

	
    /**
     * method to iterate through the arrayList of adjacent tiles and drawTile() with white highlighting
     *
     * @param out
     * @param tiles
     */
    public void highlightTilesWhite(ActorRef out, ArrayList<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile.getUnitFromTile() == null) {  // i added this condition as tiles with units should never be highlighted in white only red
//                BasicCommands.drawTile(out, tile, 1);
                drawTileWithSleep(out, tile, 1, AppConstants.drawTileSleepTime);

            }
            	
        }
    }

    /**
     * method to iterate through the arrayList of adjacent tiles and drawTile() with red highlighting
     *
     * @param out
     * @param tiles
     */
    public void highlightTilesRed(ActorRef out, ArrayList<Tile> tiles) {

        for (Tile tile : tiles) {
        	
//            BasicCommands.drawTile(out, tile, 2);
            drawTileWithSleep(out, tile, 2, AppConstants.drawTileSleepTime);

        }
    }

    public void highlightTilesWhiteSpell(ActorRef out, ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            if (tile.getUnitFromTile() != null){
                drawTileWithSleep(out, tile, 1, AppConstants.drawTileSleepTime);
            }
        }
    }

 
    public void clearTileHighlighting(ActorRef out, ArrayList<Tile> tiles) {  // method to clear the highlighted tiles changed to git rid of BufferOverflow Exception
        for (Tile tile : tiles) {
//            BasicCommands.drawTile(out, tile, 0);
            drawTileWithSleep(out, tile, 0, AppConstants.drawTileSleepTime);

        }
       // AppConstants.callSleep(50);//added this in order to stop bufferoverflow
    }

    public void addUnitToBoard(int x, int y, Unit unit) {
        tiles[x][y].setUnitToTile(unit);

    }

    public void addDummyUnitsonBoard(ActorRef out, GameState gameState) {
        // TODO Auto-generated method stub


        // Place a unit with attack:3 and health:2 at [2,2]
        int x = 3, y = 2;
        Unit unit1 = gameState.player1.getPlayerUnits().get(0);
        unit1.setSummonedID(gameState.summonedUnits.size()+1); 
        unit1.setIsPlayer(1); // set to player 1
        addUnitToBoard(x, y, unit1);
        gameState.summonedUnits.add(unit1);


        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        
     // Place enemy unit with attack:2 and health:1 at [2,1]
        x = 0;
        y = 0;
        unit1 = gameState.player2.getPlayerUnits().get(0);
        unit1.setSummonedID(gameState.summonedUnits.size()+1);
        unit1.setIsPlayer(2); // set to player 2
        addUnitToBoard(x, y, unit1);       
        gameState.summonedUnits.add(unit1); //add unit to arraylist

        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        
        
        // Place enemy unit with attack:21 and health:2 at [2,4]
        x = 4;
        y = 2;
        unit1 = gameState.player2.getPlayerUnits().get(1);
        unit1.setSummonedID(gameState.summonedUnits.size()+1);
        unit1.setIsPlayer(2); // set to player 2
        addUnitToBoard(x, y, unit1);       
        gameState.summonedUnits.add(unit1); //add unit to arraylist

        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        
       
        
        x = 7;
        y = 1;
        unit1 = gameState.player1.getPlayerUnits().get(4);
        unit1.setSummonedID(gameState.summonedUnits.size()+1);
        unit1.setIsPlayer(1); // set to player 2
        addUnitToBoard(x, y, unit1);       
        gameState.summonedUnits.add(unit1); //add unit to arraylist

        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        
        x = 5;
        y = 4;
        unit1 = gameState.player1.getPlayerUnits().get(6);
        unit1.setSummonedID(gameState.summonedUnits.size()+1);
        unit1.setIsPlayer(1); // set to player 2
        addUnitToBoard(x, y, unit1);       
        gameState.summonedUnits.add(unit1); //add unit to arraylist

        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        

    }

    // check whether a tile has a unit on it and returns a list of tiles occupied by units
    // i have added the condition of unit id and player id being the same as for now at least i cannot access only the player1's units without a different method
    // so i have set player1 units and player 1 id to 1 and same for player2 to 2.
    public ArrayList<Tile> getTilesWithUnits(ActorRef out, Tile[][] tiles, Player player) {

        ArrayList<Tile> tilesWithUnits = new ArrayList<>();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                if (tile.getUnitFromTile() != null ) {
                    if(tile.getUnitFromTile().getIsPlayer() == player.getID()) {
                        tilesWithUnits.add(tile);
                        AppConstants.callSleep(2);
                    }
                }
            }
        }

        return tilesWithUnits;
    }
    
    // Method similar to getTilesWithUnits but returns all tiles on the board without units
    public ArrayList<Tile> getTilesWithoutUnits(ActorRef out, Tile[][] tiles, Player player) {
    	

        ArrayList<Tile> tilesWithoutUnits = new ArrayList<>();
       
        for (int i = 0; i < AppConstants.boardWidth; i++) {
            for (int j = 0; j < AppConstants.boardHeight; j++) {
//            	AppConstants.callSleep(50);
                Tile tile = tiles[i][j];
                if (tile.getUnitFromTile() == null) {
                    tilesWithoutUnits.add(tile);
                }
            }
        }
        return tilesWithoutUnits;
    }
    public ArrayList<Tile> allTiles() {
        ArrayList<Tile> allTiles = new ArrayList<>();
        for (int i = 0; i < AppConstants.boardWidth; i++) {
            for (int j = 0; j < AppConstants.boardHeight; j++) {
                allTiles.add(tiles[i][j]);
            }
        }
        return allTiles;
    }

    public void drawTileWithSleep(ActorRef out,Tile tile,int mode,long time) {
    	BasicCommands.drawTile(out, tile, mode);
    	AppConstants.callSleep(time);
    }
}
