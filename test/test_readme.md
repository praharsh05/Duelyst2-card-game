# MSCTeamProject Test ReadMe

This file is to store data about the unit tests, which are used to test the implemented backend logic.

#BoardCreationTest:
Contains 2 test -> `checkTileInitialized()` and `checkBoardInitialized()`

# AvatarsNotNullTest:

Contains 2 Tests -> `AIAvatarNotNullTest`, `PlayerAvatarNotNullTest`

# AIAvatarNotNullTest

Tests that `gameState.aiAvatar` is null before initialization and is not null when the game is initialized 

# PlayerAvatarNotNullTest

Tests that `gameState.avatar` is null before initialization and is not null when the game is initialized 

# BoardCreationTest:

Contains 2 Tests -> checkTileInitialized and checkBoardInitialized

# checkTileInitialized

Tests if tile object array is created in Board in the constructor call

# checkBoardInitialized

Tests that the board object is created in GameState after receiving the 'Initialize' message

# CardNamesMatchCardIDs

Contains 2 Tests -> `Player1CardIDNameMatch` and `Player2CardIDNameMatch   `

# Player1CardIDNameMatch

Tests that the card IDs in `OrderedCardLoader.getPlayer1Cards()` match with the correct unit names

# Player2CardIDNameMatch

Tests that the card IDs in `OrderedCardLoader.getPlayer2Cards()` match with the correct unit names

#HandAndDeckConstantsTests:

Contains 2 Tests -> `maxHandSize`, `maxDeckSize`

# maxHandSize:

This test confirms that the max hand size for both players (`AppConstants.maxCardsInHand`) is 6.

# maxDeckSize: 

This test confirms that the max hand size for both players (`AppConstants.maxCardsInDeck`) is 20.

# IncrementManaTest

This test confirms that the mana for both players is equal to turn + 1 with each new turn.

# InitalizationTest

This test was written by the Professor and was included in our source code. It creates a new `GameState` and a new `Initialize` event processor. Then it verifies that `gameState.gameInitalised` returns true (the game state is updated) once an initialize message is received. 

# InitialAttackValues:

Contains 2 Tests -> `AIInitialIAttackValue` and `PlayerInitialIAttackValue`

# AIInitialAttackValue

This test confirms that the initial attack for the computer Avatar (`gameState.aiAvatar.getAttack()`) is set to 2

# PlayerInitialAttackValue

This test confirms that the initial attack for the human Avatar (`gameState.avatar.getAttack()`) is set to 2

# InitialHandSizes:

Contains 2 Tests -> `PlayerInitialHandSize` and `AIInitialHandSize` 

# AIInitialHandSize

This test confirms that the initial number of cards in the AI player's hand (`gameState.player2.hand.size()`) is 3 (`AppConstants.minCardsInHand`).

# PlayerInitialHandSize

This test confirms that the initial number of cards in the human player's hand (`gameState.player1.hand.size()`) is 3 (`AppConstants.minCardsInHand`). 

# InitialHealthValuesTest:

Contains 2 Tests -> `InitializedAIHealthTest`, `InitializedPlayerHealthTest`

# InitializedAIHealthTest

This test is to confirm that the AI's initial health (`gameState.player2.getHealth()`) equals 20, the maximum health constant defined in AppConstants (`AppConstants.playerMaxHealth`)

# InitializedPlayerHealthTest

This test is to confirm that the human player's initial health (`gameState.player1.getHealth()`) equals 20, the maximum health constant defined in AppConstants (`AppConstants.playerMaxHealth`)

# InitialPositionsTest:

Contains 2 Tests -> `AIInitialPositionTest`, `PlayerInitialPositionTest`

# AIInitialPositionTest

Tests that the AI's starting position on the board (`gameState.player2.getCurrentTile()`) is set at (7,2) (`gameState.board.returnTile(7, 2))`).

# PlayerInitialPositionTest

Tests that the human player's starting position on the board (`gameState.player1.getCurrentTile()`) is set at (1,2) (`gameState.board.returnTile(1, 2)`).

# IsGameOverTest

This test confirms that the game ends when the human or computer player's health reaches 0, or either player runs out of cards. 

# Player1CardIDs: 

This test confirms that the Card ID numbers for Player1's cards in OrderedCardLoader (`OrderedCardLoader.getPlayer1Cards().get(0).getId()`) are equal to their positions in the cardsInDeck ArrayList.

# Player2CardIDs: 

This test confirms that the Card ID numbers for Player2's cards in OrderedCardLoader (`OrderedCardLoader.getPlayer2Cards().get(0).getId()`) match correctly with their positions in the cardsInDeck ArrayList.

# PlayerInitialAttackTest

This test was written by the Professor and was included in our source code. It creates a new `GameState` and a new `Initialize` event processor. Then it verifies that `gameState.gameInitalised` returns true (the game state is updated) once an initialize message is received. 

# PlayerTurnTest

This test confirms that it is player1's turn when (`gameState.player1Turn`) is set to true and that it is the AI's turn when (`gameState.player1Turn`) is set to false.


