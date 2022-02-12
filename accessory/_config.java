package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class _config 
{	
	//--- Initialiased via _ini.load().
	private static HashMap<String, HashMap<String, String>> info = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, String[]> subtypes = new HashMap<String, String[]>();
	private static HashMap<String, String[]> linked = new HashMap<String, String[]>();
	//------

	static { _ini.load(); }

	public static String get_basic(String key_)
	{
		return get(types._CONFIG_BASIC, key_);
	}

	public static <x> boolean update_basic(String key_, x val_)
	{
		return update(types._CONFIG_BASIC, key_, val_);
	}

	public static <x> boolean matches_basic(String key_, x val_)
	{
		return matches(types._CONFIG_BASIC, key_, val_);
	}

	public static String get_sql(String key_)
	{
		return get(types._CONFIG_SQL, key_);
	}

	public static <x> boolean update_sql(String key_, x val_)
	{
		return update(types._CONFIG_SQL, key_, val_);
	}

	public static <x> boolean matches_sql(String key_, x val_)
	{
		return matches(types._CONFIG_SQL, key_, val_);
	}

	public static String get_credentials(String key_)
	{
		return get(types._CONFIG_CREDENTIALS, key_);
	}

	public static <x> boolean update_credentials(String key_, x val_)
	{
		return update(types._CONFIG_CREDENTIALS, key_, val_);
	}

	public static <x> boolean matches_credentials(String key_, x val_)
	{
		return matches(types._CONFIG_CREDENTIALS, key_, val_);
	}

	public static String get_logs(String key_)
	{
		return get(types._CONFIG_LOGS, key_);
	}

	public static <x> boolean update_logs(String key_, x val_)
	{
		return update(types._CONFIG_LOGS, key_, val_);
	}

	public static <x> boolean matches_logs(String key_, x val_)
	{
		return matches(types._CONFIG_LOGS, key_, val_);
	}

	public static String get_logs_sql(String key_)
	{
		return get(types._CONFIG_LOGS_SQL, key_);
	}

	public static <x> boolean update_logs_sql(String key_, x val_)
	{
		return update(types._CONFIG_LOGS_SQL, key_, val_);
	}

	public static <x> boolean matches_logs_sql(String key_, x val_)
	{
		return matches(types._CONFIG_LOGS_SQL, key_, val_);
	}
	
	@SuppressWarnings("unchecked")
	public static <x> x get(String type_, String key_)
	{		
		String type = types.check_aliases(type_);
		String key = types.check_aliases(key_);
		
		x output = (x)generic.DEFAULT;

		if (strings.is_ok(key) && info.containsKey(type) && info.get(type).containsKey(key)) 
		{
			output = (x)info.get(type).get(key);
		}

		return output;
	}

	public static <x> boolean update(String type_, String key_, x val_)
	{
		String type = types.check_aliases(type_);
		String key = types.check_aliases(key_);

		boolean is_ok = update_matches(type, key, val_, true, false);
		if (!is_ok) return is_ok;
		
		String[] secs = get_linked(type);
		if (!arrays.is_ok(secs)) return is_ok;
		
		for (String sec: secs)
		{
			x val2 = get(sec, key);
			if (generic.is_ok(val2)) continue;
			
			update(sec, key, val_);
		}

		return is_ok;
	}

	public static <x> boolean matches(String type_, String key_, x val_)
	{
		return update_matches(type_, key_, val_, false, false);
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Boolean> update_conn_info(HashMap<String, String> params_, String type_)
	{
		HashMap<String, Boolean> output = (HashMap<String, Boolean>)arrays.DEFAULT;
		if (!arrays.is_ok(params_)) return output;

		String type = types.check_aliases(type_);
		if (!strings.are_equal(get_linked_main(type), types._CONFIG_SQL)) return output;

		output = new HashMap<String, Boolean>();

		for (Entry<String, String> param: params_.entrySet())
		{
			String key = param.getKey();
			key = types.check_aliases(key);
			
			String val = param.getValue();

			boolean is_ok = false;
			if (type.equals(types._CONFIG_SQL)) is_ok = _config.update_sql(key, val);
			else if (type.equals(types._CONFIG_LOGS_SQL)) is_ok = _config.update_logs_sql(key, val);

			output.put(key, is_ok);
		}

		return output;
	}

	public static String[] get_linked(String main_)
	{
		String main = types.check_aliases(main_);
		
		return 
		(
			(strings.is_ok(main) && linked.containsKey(main)) ?
			linked.get(main) : (String[])arrays.DEFAULT
		);
	}
	
	public static String get_linked_main(String type_)
	{
		String type = types.check_aliases(type_);
		
		String output = strings.DEFAULT;
		if (!strings.is_ok(type) || !arrays.is_ok(linked)) return output;
		
		for (Entry<String, String[]> item: linked.entrySet())
		{
			String key = item.getKey();
			key = types.check_aliases(key);
			
			if (strings.are_equal(type, key)) return key;
			else if (arrays.value_exists(item.getValue(), type)) return type;
		}
		
		return output;
	}
	
	public static <x> boolean update_linked(String main_, String[] secs_)
	{
		String main = types.check_aliases(main_);
		if (!strings.is_ok(main) || !arrays.is_ok(secs_)) return false;
		
		linked.put(main, secs_);
		
		return true;
	}
	
	public static <x> boolean update_subtypes(String main_, String[] subtypes_)
	{
		String main = types.check_aliases(main_);
		if (!strings.is_ok(main) || !arrays.is_ok(subtypes_)) return false;
		
		subtypes.put(main, arrays.new_instance(subtypes_));
		
		return true;
	}
	
	public static <x> boolean update_ini(String type_, String key_, x val_)
	{
		return update_matches(type_, key_, val_, true, true);
	}
	
	private static <x> boolean update_matches(String type_, String key_, x val_, boolean update_, boolean ini_)
	{
		boolean is_ok = false;
		
		String type = types.check_aliases(type_);
		String key = types.check_aliases(key_);

		if (!strings.is_ok(type) || !strings.is_ok(key)) return is_ok;
		if (!generic.is_string(val_)) return is_ok;

		HashMap<String, String> local = new HashMap<String, String>();
		if (info.containsKey(type)) local = new HashMap<String, String>(info.get(type));
		else if (!ini_) return is_ok; 

		is_ok = true;
		String val = strings.to_string(val_);

		local = update_matches_internal
		(
			local, update_matches_get_keys_all(key, ini_, type), val, update_, ini_
		);

		if (!arrays.is_ok(local)) is_ok = false;
		else if (update_) info.put(type, new HashMap<String, String>(local));

		return is_ok;
	}

	private static String[] update_matches_get_keys_all(String key_, boolean ini_, String type_)
	{
		String[] local = (subtypes.containsKey(type_) ? subtypes.get(key_) : (String[])arrays.DEFAULT);
		
		return update_matches_get_keys(key_, ini_, type_, local);
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, String> update_matches_internal(HashMap<String, String> info_, String[] keys_, String val_, boolean update_, boolean ini_)
	{
		HashMap<String, String> info = new HashMap<String, String>(info_);

		boolean val_is_ok = strings.is_ok(val_);
		
		for (String key: keys_)
		{
			boolean key_is_ok = info.containsKey(key);

			if (update_) 
			{
				if ((!ini_ && key_is_ok && val_is_ok) || (ini_ && !key_is_ok)) 
				{ 
					info.put(key, (val_is_ok ? val_ : strings.DEFAULT)); 
					
					break;
				}
			}
			else 
			{
				if (key_is_ok && strings.are_equal(info.get(key), val_)) return info;
			}
		}

		return (update_ ? info : (HashMap<String, String>)arrays.DEFAULT);	
	}

	private static String[] update_matches_get_keys(String key_, boolean ini_, String type_, String[] subtypes_)
	{
		ArrayList<String> all_keys = new ArrayList<String>();
		all_keys.add(key_);    	
		if (!ini_) all_keys.add(types.add_type(key_, update_matches_get_keys_type(key_, type_, subtypes_)));  

		return arrays.to_array(all_keys);
	}

	private static String update_matches_get_keys_type(String key_, String type_, String[] subtypes_)
	{
		String type = type_;
		if (!arrays.is_ok(subtypes_)) return type;

		for (String subtype: subtypes_)
		{
			for (String type2: types.get_subtypes(subtype, null))
			{
				if (strings.are_equivalent(key_, types.remove_type(type2, subtype)))
				{
					type = subtype;
					break;
				}
			}
		}

		return type;
	}
}