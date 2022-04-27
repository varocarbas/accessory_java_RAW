package accessory;

//This class includes constants of essential importance, to be loaded at the very beginning and whose values are 
//determined by exclusively relying on calls to native or external code.
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below. 
//- All these constants will be accessed directly here, via the corresponding local methods (i.e., no local copies).

class _basic extends parent_ini_first 
{
	private static _basic _instance = new _basic(); 
	
	public _basic() { }
	public static void populate() { _instance.populate_internal(); }
	
	public static String DIR_SEPARATOR = null;

	protected void populate_internal_vals() 
	{ 
		if (_populated) return;
		
		DIR_SEPARATOR = (System.getProperty("os.name").contains("Windows") ? "\\" : "/");
		
		_populated = true;
	} 
}
