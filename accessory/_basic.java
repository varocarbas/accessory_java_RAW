package accessory;

//This class includes constants of essential importance, to be loaded at the very beginning and whose values are 
//determined by exclusively relying on calls to native or external code.
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below. 
//- All these constants will be accessed directly here, via the corresponding local methods (i.e., no local copies).

class _basic 
{
	public static String DIR_SEPARATOR = null;
	
	private static boolean _populated = false;
	
	public static void populate() 
	{ 
		if (_populated) return;
		
		DIR_SEPARATOR = (strings.contains("win", System.getProperty("os.name"), true) ? "\\" : "/");
		
		_populated = true;
	} 
}
