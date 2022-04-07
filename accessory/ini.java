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
	
	public static void load_config_linked_update(String main_, String[] secs_, HashMap<String, Object> vals_)
	{	
		boolean secs_ok = arrays.is_ok(secs_);
		
		if (secs_ok) config.update_linked(main_, secs_);

		load_config_linked_update_internal(main_, vals_, true);
		if (!secs_ok) return;
		
		for (String sec: secs_)
		{
			load_config_linked_update_internal(sec, vals_, false);
		}
	}

	private static void load_config_linked_update_internal(String type_, HashMap<String, Object> vals_, boolean is_main_)
	{	
		for (Entry<String, Object> item: vals_.entrySet())
		{
			String key = item.getKey();
			Object val = item.getValue();
			
			if (generic.is_string(val)) config.update_ini(type_, key, (is_main_ ? (String)val : strings.DEFAULT));
			else if (generic.is_boolean(val)) config.update_ini(type_, key, (is_main_ ? (boolean)val : false));
		}
	}
	
	//Loading all the first classes, the ones whose names start with "_".
	private static void load_first()
	{
		_basic.populate();
		_alls.populate();
		_defaults.populate();
	}
}