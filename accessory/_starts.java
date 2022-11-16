package accessory;

class _starts extends parent_ini_first 
{
	private static _starts _instance = new _starts(); 

	public _starts() { }
	
	public static void populate() { _instance.populate_internal_common(); }

	protected void populate_internal() 
	{ 
		db_common.start(); 
	
		db_quick.start();
	}
}