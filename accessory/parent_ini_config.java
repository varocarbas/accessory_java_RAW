package accessory;

import java.util.HashMap;

public abstract class parent_ini_config 
{
	protected boolean _populated = false;
	
	protected abstract void populate_all();
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> get_config_default_generic(String type_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> output = (HashMap<String, Object>)arrays.get_new(vals_);
	
		String[] all_boolean = types.get_all_config_boolean();
		
		for (String subtype: types.get_subtypes(type_))
		{
			if (output.containsKey(subtype)) continue;
			
			output.put(subtype, (arrays.value_exists(all_boolean, subtype) ? false : strings.DEFAULT));
		}
		
		return output;
	}
	
	protected static void populate_internal(parent_ini_config instance_) 
	{
		if (instance_._populated) return;
		
		instance_.populate_all(); 
	}
	
	protected boolean populate(String type_store_, String type_root_, HashMap<String, Object> vals_)
	{
		String type_store = config.check_type(type_store_);
		if (!strings.is_ok(type_store) || !arrays.is_ok(vals_)) return false;
		
		String type_root = config.check_type(type_root_);
		if (!strings.is_ok(type_root)) type_root = type_store;
		
		return arrays.value_exists(config.update_ini(type_store_, vals_), false);
		//return arrays.value_exists(config.update_ini(type_store_, get_config_default_generic(type_root_, vals_)), false);
	}
}