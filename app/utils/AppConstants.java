package utils;


/**
* This class file will be used to store all of the constant values that are 
* responsible for proper game implementation. 
*
* This class will be also used to store any global 
* variables and methods that can be accessed from several Java classes. 
*
**/
public class AppConstants {

	public static boolean isDebugMode=true; // variable to view system logs, set this to false to hide logs from the terminal
	
	public static  long gameTaskInterval= 5000; // 5 seconds
	public static long allowedHeartbeatTimeGap = 3600000; //30 minutes

	// Board constants
	public static int boardWidth=9;
	public static int boardHeight=5;
	
	
	// Card constants
	public static int minCardsInHand=3;
	public static int maxCardsInHand=6;
	public static int maxCardsInDeck=20;
	
	
	// Player constants
	public static int playerMaxHealth=20;

	//Sleep time constants
	public static long attackSleepTime=1500;
	public static long deathSleepTime=2000;
	public static long effectSleepTime=500;
	public static long drawTileSleepTime=1;
	
	// AI action codes
	public static String move="move";
	public static String attack="attack";
	public static String drawCardSummon="drawCardSummon";
	public static String drawCardSpell="drawCardSpell";


	//string array of player 1 units
	public static String[] p1unit = {
		StaticConfFiles.u_comodo_charger,
		StaticConfFiles.u_pureblade_enforcer,
		StaticConfFiles.u_fire_spitter,
		StaticConfFiles.u_silverguard_knight,
		StaticConfFiles.u_azure_herald,
		StaticConfFiles.u_ironcliff_guardian,
		StaticConfFiles.u_azurite_lion,
		StaticConfFiles.u_hailstone_golem,
			StaticConfFiles.u_silverguard_knight,
			StaticConfFiles.u_fire_spitter,
			StaticConfFiles.u_comodo_charger,
			StaticConfFiles.u_pureblade_enforcer,
			StaticConfFiles.u_azure_herald,
			StaticConfFiles.u_ironcliff_guardian,
			StaticConfFiles.u_azurite_lion,
			StaticConfFiles.u_hailstone_golem
	};

	//string array of player 2 units
	public static String[] p2unit = {
		StaticConfFiles.u_rock_pulveriser,
		StaticConfFiles.u_bloodshard_golem,
		StaticConfFiles.u_blaze_hound,
		StaticConfFiles.u_windshrike,
		StaticConfFiles.u_pyromancer,
		StaticConfFiles.u_serpenti,
		StaticConfFiles.u_planar_scout,
		StaticConfFiles.u_hailstone_golemR,
			StaticConfFiles.u_rock_pulveriser,
			StaticConfFiles.u_bloodshard_golem,
			StaticConfFiles.u_blaze_hound,
			StaticConfFiles.u_windshrike,
			StaticConfFiles.u_pyromancer,
			StaticConfFiles.u_serpenti,
			StaticConfFiles.u_planar_scout,
			StaticConfFiles.u_hailstone_golemR
	};
	
	
	public static String[] effects = {
			StaticConfFiles.f1_buff,
			StaticConfFiles.f1_inmolation,
			StaticConfFiles.f1_martyrdom,
			StaticConfFiles.f1_summon
	};
	
	
	
	public static void printLog(String message) {
		if(isDebugMode)
			System.out.println(message);
	}
	
	public static void callSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	
}
