package accessory;

public class _basic 
{
	public static String DIR_SEPARATOR = null;
	
	public static void load() 
	{ 
		DIR_SEPARATOR = get_dir_separator();
	} 
	
	private static String get_dir_separator()
	{
		return (strings.contains("win", System.getProperty("os.name"), true) ? "\\" : "/");
	}
}
