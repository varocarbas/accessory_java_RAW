package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class ini 
{
	//Method expected to be called every time a non-ini, non-first, non-parent abstract class is loaded.
	//It has to include all the load() methods of all the ini classes.
	public static void load() 
	{
		load_first();
		
		config_ini.load();
		db_ini.load();
	}
	
	public static void load_config_linked_update(String main_, String[] secs_, HashMap<String, String> vals_)
	{	
		config.update_linked(main_, secs_);

		load_config_linked_update_internal(main_, vals_, true);

		for (String sec: secs_)
		{
			load_config_linked_update_internal(sec, vals_, false);
		}
	}

	private static void load_config_linked_update_internal(String type_, HashMap<String, String> vals_, boolean is_main_)
	{	
		for (Entry<String, String> val: vals_.entrySet())
		{
			config.update_ini(type_, val.getKey(), (is_main_ ? val.getValue() : strings.DEFAULT));
		}
	}
	
	//Loading all the first classes, the ones whose names start with "_".
	private static void load_first()
	{
		_basic.load();
		_alls.load();
		_defaults.load();
	}
}