# MSCTeamProject

This is a backend developed for a tactical card game application in Java 

# Board creation

A Board class has been created to accomodate different tile objects and its methods ,which constitutes the board

# Drawing tiles

A method called "drawTileWithSleep(ActorRef out,Tile tile,int mode,long sleepTime)" has been added to control the sleep time
after updating DrawTile on the front end.

# Player creation

The constructor method has been modified to differentiate both players' attributes and methods


# Draw Card

This was changed to ArrayList `Deck` and `Hand` in the player class for better handling of deck and hand.
the `Deck` has 2 sets of card for the players and is created through the `createDeck()` method in player class, `Hand` gets 3 cards from `Deck` at initialization using the `setHand(ActorRef out, int playerID)` method. the cards are removed from the deck whenever a card is drawn using the `drawAnotherCard(ActorRef out, int playerID)` method.

The end turn class was modified to include `drawAnotherCard(out, playerID)` to facilitate the drawing of card after endTurn is clicked.

# OverDraw

if the hand is full of cards, i.e from position 1-6 then the 7th position will not be drawn on the board, the 
corresponding card will be deleted from the deck. The method is part of the `drawAnotherCard(out, playerID)` in the player class.


# Player attack ability

An arraylist 'summonedUnits' will be used to keep track of movements and attacks of all the units on board in a turn
A 'PerformAction' class has been introduced to include the methods for the implementation of available
actions (highlight/move/attack) and perfrom appropriate tasks in order
	
Added highlightTiles() to highlight or unhighlight the available tiles to move or attack upon clicking on avatar.

Added attackUnit() to implement the avatar attack logic on an enemy unit and handling counter attack

# Get tiles containing units

`getTilesWithUnits(ActorRef,Tile[][], Player)` is a method in the board class that takes in the 2d array of 
tiles(board) and player and returns an ArrayList of tiles that contain a unit belonging to the input player.

# Card Clicked Highlighting

When a card in the hand is clicked, the miniCard will be highlighted using `highlightMiniCard(ActorRef out, int position, GameState gameState)` method. The hand position is being tracked in gameState using the int variable `handPosClicked`.
I have added a method called `highlightSummonableTiles(ActorRef, GameState)` in the `CardClicked` class. When a card
is clicked in the front end this method is called and it will call `getTilesWithUnits()`, which will be fed into the method
`getTilesToAttack()` method to return a list of all adjacent tiles which will then be passed though the `highlightTilesWhite()`
method to highlight all the available summoning tiles for the given player.

# BetterUnit creation

I have modified the constructor so the board is passed into it. This is so the `addUnitToBoard(tilex,tiley)` can be used in the
creation of the avatar. The avatar needs to be treated as any other unit for the most part so needs to be accessed 
through the tiles in the same way.

# Move units

The `highlightAndMove(ActorRef, GameState, Tile)` method is called in the TileClicked class and is used to first highlight the
available units moves and then on the second click (if a friendly unit), will execute the movement. If it is an enemy unit, 
it will just clear the highlighting and the player will be able to click another unit again.

# Move and attack units

The `highlightMoveAndAttack(mode,player,ActorRef, Tile,GameState)` method is called in the TileClicked class and is used to first highlight the
available units to move along with the attackable units adjacent to it. On the second click of the same unit, will execute the movement or attack or both.
If it is not the same unit, it will just clear the highlighting and the player will be able to click another unit again.

# Linking players to units

A variable `int isPlayer` is created in the Unit class to store either the value 1 or 2 which will the used to check that the 
isPlayer is the same as the current playerID. There may be a better way to do this but for now it gives us all the functionality
we need to execute core game logic.

# Clearing highlighting with otherClicked

The otherClicked compares `gameState.clickMessage` with `cardClick` JsonNode variable for every event class.
The gameState keeps track of the last click and if it not then a number of methods will be called in order 
to prepare the game for appropriate actions. 
Till now we are clearing card highlighting and subsequent tile highlighting by checking if the `gameState.SummonTileList` 
is null or not, if it is then `clearCardClicked( ActorRef out, GameState gameState, Player player)` will be 
called. This method has two helper methods `CardClicked.clearHighlightMiniCard` and `gameState.board.clearTileHighlighting`

I have created a method `clearHighlighting(ActorRef, Board)` which is called in the otherClicked class as well as in the tileClicked class
int places which simply reloads the tiles with the mode '0'. 

# GameEnd

A method to check if the game has ended, when the players reach 0 health or have no cards left to play, was 
created in the PerformAction class `gameEnd(ActorRef out, GameState gameState)`. the method returns a boolean
and also sets `gameState.isGameOver`, it is being called in the Heartbeat.java to periodically check the state.

# Loading and drawing units

`createPlayerUnits(ActorRef)` (player1 and player 2) loads the units into an arrayList and sets their health and attack and isPlayer value.
`drawUnitToBoard(ActorRef,Unit,Card,TilePlayer)` checks which player is being used to call the method, then takes in the cardId and checks it 
against the unit id as it iterates through the arrayList. if a match (there should always be), the unit is drawn onto the board and the front end 
features are added such as heal attack and summoning animation. finally, the unit is set to the tile.

# Spells

This is a new Class for now. This is used to create spell objects and play them once the cards are played. The only current method is
`playSpell(ActorRef, Card, Tile, BetterUnit)` which creates 4 spell objects, then checks the cardId's to the spell objects Id
and triggers some fundamental logic required from the spells. As a side not, both this method and `drawUnitToBoard()` checks the cardID and
also checks the cardId+10 as there are duplicate cards and the second card has an id of the first + 10.

# Summoning on the board

A unit would be summoned if a card is clicked in hand and a subsequent tile is clicked which
 is highlighted. `summonCard(ActorRef out, GameState gameState, Tile clicked, Player player)`
 was added in the TileClicked class. the player class was modified to add certain helper 
 method such as `Unit getUnitbyCard(int i, Player p)` , `deleteCardInHand(ActorRef out, int playerID, GameState gameState)`
 and `createUnits(Player player)` whose tasks are evident of their names. 
 
# Computer Player

The computer player and all the logic regarding the computer player runs on a seperate thread `startAIThread`.
this thread runs the logic in form of the method `startAILogic` and any exception that occurs was handled by 
interrupting the thread and passing on the turn back to player so that the game does not crash and keep 
continuing.
`startAILogic` has a flag `isContinue` which sets how many times the logic should be run. the method starts 
with getting the game state at that moment like, cards in the hand, `checkUnitTiles` gives which tiles belong 
to which player,`listPossibleMove` gives the possible moves that can be made on the board, i.e. possible 
summons, possible movement and possible attack. After this the actions are performed based on the game state, 
these are done using the methods `drawCardAndProcessAction`, `moveAIProcessAction`, `attackAIProcessAction`.
These method have additional logic and decide the optimal move to be made based on the data available to them 
form the `listPossibleMove` and `checkUnitTiles` methods. Once an optinal move is finalised the 
`drawCardAndProcessAction` will use `drawCardAI` to simulate the tileClicked event to summon a unit, 
similarly `moveAIProcessAction`, `attackAIProcessAction` will use `moveAIUnit` to move and `attackAIUnit` to
attack, respectively.

# Ranged attack

`rangedAttack(ActorRef out, GameState gameState, Unit unit, Unit enemyUnit, Tile enemyTile, Tile startTile)` method has been created which is an edited
version of `attackUnit()` method. It will execute the ranged attack logic and will be called inside `attackUnitRanged()`. I have also added additional 
checks inside the `TileClicked` class to implement different subroutines of highlighting if the clicked units is one that has ranged attack.

# Silverguard Knight ability

The method `SilverguardKnightAbility(ActorRef out, GameState gameState)` has been added which checks if a silverguard Knight is on the board, and if so,
increased the attck of the unit by 2. this is called inside the attack method which checks if the avatar has been damaged. this will also need to be 
called inside any spell logic which checks for the avatar being damaged.

# Truestrike

The method` truestrike() ` is called in the tile clicked, the correct highlighting is implemented in the card clicked class

# Sundrop Elixr

the method `SundropElixir()` is called in the tile clicked and the correct highlighting is implemented in the card clicked class

