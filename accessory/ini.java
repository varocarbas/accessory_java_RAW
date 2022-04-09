package accessory;

public abstract class ini 
{
	//Method expected to be called every time a non-ini, non-first, non-parent abstract class is loaded.
	//It has to include all the load() methods of all the ini classes.
	public static void load() 
	{
		populate_first();
		
		config_ini.populate();
		db_ini.populate();
	}
	
	//Loading all the first classes, the ones whose names start with "_".
	private static void populate_first()
	{
		_basic.populate();
		_alls.populate();
		_defaults.populate();
	}
}