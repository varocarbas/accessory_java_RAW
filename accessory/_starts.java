package accessory;

//This class forces the corresponding classes to load at the very beginning via calling their start() methods.

class _starts extends parent_ini_first 
{
	private static _starts _instance = new _starts(); 

	public _starts() { }
	public static void populate() { _instance.populate_internal_common(); }

	protected void populate_internal() { }
}