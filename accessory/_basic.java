package accessory;

//This class includes constants of essential importance whose single values might not be delivered in
//a too quick or reliable way, at least, as per the default Java's performance with global static final
//variables, or because of any other reason advising against including them in the _defaults class.
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below, by calling external methods only accessorily. 
//- All these constants will be constants will be these variables are accessed directly here.

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
