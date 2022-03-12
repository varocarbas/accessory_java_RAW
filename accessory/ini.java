package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class ini 
{
	//Method expected to be called every time a non-ini static class is loaded.
	//It has to include all the load() methods of all the ini classes.
	public static void load() 
	{
		_config_ini.load();
		db_ini.load();
	}
	
	public static void load_config_linked_update(String main_, String[] secs_, HashMap<String, String> vals_)
	{	
		_config.update_linked(main_, secs_);

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
			_config.update_ini(type_, val.getKey(), (is_main_ ? val.getValue() : strings.DEFAULT));
		}
	}
}