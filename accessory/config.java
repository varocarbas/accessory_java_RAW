package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class config 
{	
	//--- Initialiased via config_ini.load().
	private static HashMap<String, HashMap<String, String>> _info = new HashMap<String, HashMap<String, String>>();
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

		return update_matches(type, key_, val_, true, false);
	}

	public static boolean matches(String type_, String key_, boolean val_)
	{
		return matches(type_, key_, strings.to_string(val_));
	}
	
	public static boolean matches(String type_, String key_, String val_)
	{
		return update_matches(type_, key_, val_, false, false);
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
		return update_matches(type_, key_, val_, true, true);
	}

	public static String check_type(String type_)
	{
		return types.check_type(type_, types.get_subtypes(types.CONFIG));
	}
	
	static HashMap<String, Boolean> update_db_conn_info(String setup_, HashMap<String, String> params_)
	{
		HashMap<String, Boolean> output = null;
		
		String type = check_type(setup_);
		if (!arrays.is_ok(params_) || !strings.is_ok(type)) return output;

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

		String type = check_type(type_);
		String key = check_type(key_);

		if (!strings.is_ok(type) || !strings.is_ok(key)) return is_ok;
		if (!generic.is_string(val_)) return is_ok;

		HashMap<String, String> info = new HashMap<String, String>();
		if (_info.containsKey(type)) info = new HashMap<String, String>(_info.get(type));
		else if (!ini_) return is_ok; 

		if (!ini_ && !info.containsKey(key_)) return is_ok;

		is_ok = true;
		String val = strings.to_string(val_);

		info = update_matches_internal(info, key, val, update_, ini_);
		
		if (!arrays.is_ok(info)) is_ok = false;
		else if (update_) _info.put(type, new HashMap<String, String>(info));

		return is_ok;
	}

	private static HashMap<String, String> update_matches_internal(HashMap<String, String> info_, String key_, String val_, boolean update_, boolean ini_)
	{
		HashMap<String, String> info = new HashMap<String, String>(info_);

		boolean val_is_ok = strings.is_ok(val_);
		boolean key_is_ok = info.containsKey(key_);

		if (update_) 
		{
			if ((!ini_ && key_is_ok && val_is_ok) || (ini_ && !key_is_ok)) 
			{ 
				info.put(key_, (val_is_ok ? val_ : strings.DEFAULT));
			}
		}
		else if (key_is_ok && strings.are_equal(info.get(key_), val_)) return info;

		return (update_ ? info : null);	
	}
}