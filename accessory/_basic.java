package accessory;

public class _basic 
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
