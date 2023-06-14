package accessory;

//This class is loaded last, after all the other ini/first classes. Consequently, the corresponding
//start() methods called from here are assumed to deal with information requiring any of those other 
//classes to have already completed their actions. A good example is a method getting the cols of a DB 
//table (_ini_db has to be loaded first).

class _starts extends parent_first 
{
	private static _starts _instance = new _starts(); 

	public _starts() { }
	
	public static void populate() { _instance.populate_internal_common(); }

	protected void populate_internal() { }
}