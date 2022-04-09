package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class config 
{	
	//--- Initialiased via config_ini.load().
	private static HashMap<String, HashMap<String, String>> _info = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, String[]> _subtypes = new HashMap<String, String[]>();
	private static HashMap<String, String[]> _linked = new HashMap<String, String[]>();
	//---

	static { ini.load(); }

	public static String get_basic(String key_)
	{
		return get(types.CONFIG_BASIC, key_);
	}
	
	public static boolean update_basic(String key_, boolean val_)
	{
		return update_basic(key_, strings.to_string(val_));
	}

	public static boolean update_basic(String key_, String val_)
	{
		return update(types.CONFIG_BASIC, key_, val_);
	}
	
	public static boolean matches_basic(String key_, boolean val_)
	{
		return matches_basic(key_, strings.to_string(val_));
	}

	public static boolean matches_basic(String key_, String val_)
	{
		return matches(types.CONFIG_BASIC, key_, val_);
	}

	public static String get_db(String key_)
	{
		return get(types.CONFIG_DB, key_);
	}
	
	public static boolean update_db(String key_, boolean val_)
	{
		return update_db(key_, strings.to_string(val_));
	}
	
	public static boolean update_db(String key_, String val_)
	{
		return update(types.CONFIG_DB, key_, val_);
	}
	
	public static boolean matches_db(String key_, boolean val_)
	{
		return matches_db(key_, strings.to_string(val_));
	}
	
	public static boolean matches_db(String key_, String val_)
	{
		return matches(types.CONFIG_DB, key_, val_);
	}

	public static String get_credentials(String key_)
	{
		return get(types.CONFIG_CREDENTIALS, key_);
	}

	public static boolean update_credentials(String key_, boolean val_)
	{
		return update_credentials(key_, strings.to_string(val_));
	}
	
	public static boolean update_credentials(String key_, String val_)
	{
		return update(types.CONFIG_CREDENTIALS, key_, val_);
	}

	public static boolean matches_credentials(String key_, boolean val_)
	{
		return matches_credentials(key_, strings.to_string(val_));
	}
	
	public static boolean matches_credentials(String key_, String val_)
	{
		return matches(types.CONFIG_CREDENTIALS, key_, val_);
	}

	public static String get_crypto(String key_)
	{
		return get(types.CONFIG_CRYPTO, key_);
	}

	public static boolean update_crypto(String key_, boolean val_)
	{
		return update_crypto(key_, strings.to_string(val_));
	}
	
	public static boolean update_crypto(String key_, String val_)
	{
		return update(types.CONFIG_CRYPTO, key_, val_);
	}

	public static boolean matches_crypto(String key_, boolean val_)
	{
		return matches_crypto(key_, strings.to_string(val_));
	}
	
	public static boolean matches_crypto(String key_, String val_)
	{
		return matches(types.CONFIG_CRYPTO, key_, val_);
	}

	public static String get_logs(String key_)
	{
		return get(types.CONFIG_LOGS, key_);
	}

	public static boolean update_logs(String key_, boolean val_)
	{
		return update_logs(key_, strings.to_string(val_));
	}
	
	public static boolean update_logs(String key_, String val_)
	{
		return update(types.CONFIG_LOGS, key_, val_);
	}

	public static boolean matches_logs(String key_, boolean val_)
	{
		return matches_logs(key_, strings.to_string(val_));
	}
	
	public static boolean matches_logs(String key_, String val_)
	{
		return matches(types.CONFIG_LOGS, key_, val_);
	}

	public static String get(String type_, String key_)
	{		
		String output = null;
		
		String type = check_type(type_);
		if (!strings.is_ok(type) || !strings.is_ok(key_)) return output;
		
		if (_info.containsKey(type) && _info.get(type).containsKey(key_)) output = _info.get(type).get(key_);

		return output;
	}

	public static boolean update(String type_, String key_, boolean val_)
	{
		return update(type_, key_, strings.to_string(val_));
	}
	
	public static boolean update(String type_, String key_, String val_)
	{	
		boolean is_ok = false;
		
		String type = check_type(type_);
		if (!strings.is_ok(type) || !strings.is_ok(key_)) return is_ok;

		is_ok = update_matches(type, key_, val_, true, false);
		if (!is_ok) return is_ok;

		String[] secs = get_linked(type);
		if (!arrays.is_ok(secs)) return is_ok;

		for (String sec: secs)
		{
			String val = get(sec, key_);
			if (generic.is_ok(val)) continue;

			update(sec, key_, val_);
		}

		return is_ok;
	}

	public static boolean matches(String type_, String key_, boolean val_)
	{
		return matches(type_, key_, strings.to_string(val_));
	}
	
	public static boolean matches(String type_, String key_, String val_)
	{
		return update_matches(type_, key_, val_, false, false);
	}	

	public static String[] get_linked(String main_)
	{
		String main = check_type(main_);
		
		return ((strings.is_ok(main) && _linked.containsKey(main)) ? _linked.get(main) : null);
	}

	public static String get_linked_main(String type_)
	{
		String output = strings.DEFAULT;
		
		String type = check_type(type_);
		if (!strings.is_ok(type)) return output;
		
		if (type.equals(types.CONFIG_DB_SETUP_DEFAULT)) return types.CONFIG_DB;
		if (!arrays.is_ok(_linked)) return output;
		
		for (Entry<String, String[]> item: _linked.entrySet())
		{
			String key = item.getKey();

			if (strings.are_equal(type, key)) return key;
			else if (arrays.value_exists(item.getValue(), type)) return type;
		}

		return output;
	}

	public static boolean update_linked(String main_, String[] secs_)
	{
		String main = check_type(main_);
		if (!strings.is_ok(main) || !arrays.is_ok(secs_)) return false;

		_linked.put(main, secs_);

		return true;
	}

	public static boolean update_subtypes(String main_, String[] subtypes_)
	{
		String main = check_type(main_);
		if (!strings.is_ok(main) || !arrays.is_ok(subtypes_)) return false;

		_subtypes.put(main, (String[])arrays.get_new(subtypes_));

		return true;
	}

	public static boolean update_ini(String type_, HashMap<String, Object> vals_)
	{	
		String type = check_type(type_);
		if (!strings.is_ok(type) || !arrays.is_ok(vals_)) return false;
		
		for (Entry<String, Object> item: vals_.entrySet())
		{
			String key = item.getKey();
			Object val = item.getValue();
			
			String val2 = null;
			if (generic.is_string(val)) val2 = (String)val;
			else if (generic.is_boolean(val)) val2 = strings.to_string((boolean)val);
			else return false;
			
			if (!update_matches(type, key, val2, true, true)) return false;
		}
		
		return true;
	}
	
	public static boolean update_ini(String type_, String key_, boolean val_)
	{
		return update_ini(type_, key_, strings.to_string(val_));
	}
	
	public static boolean update_ini(String type_, String key_, String val_)
	{
		return update_matches(check_type(type_), key_, val_, true, true);
	}

	public static String check_type(String type_)
	{
		return types.check_type(type_, types.get_subtypes(types.CONFIG));
	}
	
	static HashMap<String, Boolean> update_db_conn_info(String setup_, HashMap<String, String> params_)
	{
		HashMap<String, Boolean> output = null;
		
		String type = check_type(setup_);
		if (!arrays.is_ok(params_) || !strings.is_ok(type) || !strings.are_equal(get_linked_main(type), types.CONFIG_DB)) return output;

		output = new HashMap<String, Boolean>();

		for (Entry<String, String> param: params_.entrySet())
		{
			String key = param.getKey();
			String val = param.getValue();
			if (val == null) continue;

			output.put(key, config.update(type, key, val));
		}

		return output;
	}
	
	private static boolean update_matches(String type_, String key_, String val_, boolean update_, boolean ini_)
	{
		boolean is_ok = false;

		String type = type_;
		String key = key_;

		if (!strings.is_ok(type) || !strings.is_ok(key)) return is_ok;
		if (!generic.is_string(val_)) return is_ok;

		HashMap<String, String> info = new HashMap<String, String>();
		if (_info.containsKey(type)) info = new HashMap<String, String>(_info.get(type));
		else if (!ini_) return is_ok; 

		if (!ini_ && !info.containsKey(key_)) return is_ok;

		is_ok = true;
		String val = strings.to_string(val_);

		info = update_matches_internal(info, update_matches_get_keys_all(key, ini_, type), val, update_, ini_);
		
		if (!arrays.is_ok(info)) is_ok = false;
		else if (update_) _info.put(type, new HashMap<String, String>(info));

		return is_ok;
	}

	private static String[] update_matches_get_keys_all(String key_, boolean ini_, String type_)
	{
		String[] subtypes = (_subtypes.containsKey(type_) ? _subtypes.get(key_) : null);

		return update_matches_get_keys(key_, ini_, type_, subtypes);
	}

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

		return (update_ ? info : null);	
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
			for (String type2: types.get_subtypes(subtype))
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