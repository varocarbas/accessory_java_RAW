package accessory;

//This class includes constants of essential importance, to be loaded at the very beginning and whose values are 
//determined by exclusively relying on calls to native or external code.
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below. 
//- All these constants will be accessed directly here, via the corresponding local methods (i.e., no local copies).

public class _basic extends parent_first 
{
	private static _basic _instance = new _basic(); 

	public _basic() { }
	public static void populate() { _instance.populate_internal_common(); }

	private static boolean _is_windows = false;
	private static boolean _is_linux = false;
	private static String _dir_separator = null;
	
	public static boolean is_windows() { return _is_windows; }

	public static boolean is_linux() { return _is_linux; }
	
	public static String get_dir_separator() { return _dir_separator; }
	
	protected void populate_internal() 
	{ 
		if (_populated) return;

		String os = System.getProperty("os.name");
		
		if (os.contains("Windows")) _is_windows = true;
		else if (os.contains("Linux")) _is_linux = true;
		
		_dir_separator = (_is_windows ? "\\" : "/");

		_populated = true;
	} 
}