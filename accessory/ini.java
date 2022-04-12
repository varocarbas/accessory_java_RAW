package accessory;

import java.util.HashMap;

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

	public static void manage_error(String type_)
	{
		errors._exit = true;
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(get_generic_key(types.WHAT_TYPE), (strings.is_ok(type_) ? type_ : strings.DEFAULT));
		
		errors.manage(info);
	}
	
	public static HashMap<String, Object> get_config_default_generic(String type_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = arrays.get_new(vals_);
		
		for (String subtype: types.get_subtypes(type_))
		{
			if (vals.containsKey(subtype)) continue;
			
			vals.put(subtype, (arrays.value_exists(types.get_all_config_boolean(), subtype) ? false : strings.DEFAULT));
		}
		
		return vals;
	}

	//The keys in the generic class might not have been loaded yet, so better getting them directly from types.
	public static String get_generic_key(String what_)
	{
		String what = types.check_type(what_, types.get_subtypes(types.WHAT));
		
		return (strings.is_ok(what) ? types.what_to_key(what) : strings.DEFAULT);
	}
	
	//Loading all the first classes, the ones whose names start with "_".
	private static void populate_first()
	{
		_basic.populate();
		_alls.populate();
		_defaults.populate();
	}
}