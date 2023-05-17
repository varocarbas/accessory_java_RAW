package accessory;

import java.util.HashMap;

//One of the start() overloads in this class has to be called before using any of the resources of this library.
public class _ini extends parent_ini
{	
	public static final String INFO_NAME = _types.INI_INFO_APP_NAME;
	public static final String INFO_INCLUDES_LEGACY = _types.INI_INFO_INCLUDES_LEGACY;

	public static final String DBS_SETUP = _types.CONFIG_DB_SETUP;
	public static final String DBS_SETUP_NAME = _types.CONFIG_DB_NAME;
	public static final String DBS_SETUP_MAX_POOL = _types.CONFIG_DB_SETUP_MAX_POOL;
	public static final String DBS_SETUP_HOST = _types.CONFIG_DB_SETUP_HOST;
	public static final String DBS_SETUP_TYPE = _types.CONFIG_DB_SETUP_TYPE;
	public static final String DBS_SETUP_CREDENTIALS_USERNAME = _types.CONFIG_DB_SETUP_CREDENTIALS_USERNAME;
	public static final String DBS_SETUP_CREDENTIALS_PASSWORD = _types.CONFIG_DB_SETUP_CREDENTIALS_PASSWORD;
	public static final String DBS_SETUP_CREDENTIALS_USER = _types.CONFIG_DB_SETUP_CREDENTIALS_USER;
	public static final String DBS_SETUP_CREDENTIALS_ENCRYPTED = _types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED;
	public static final String DBS_SETUP_CREDENTIALS_MEMORY = _types.CONFIG_DB_SETUP_CREDENTIALS_MEMORY;
	
	public static final boolean DEFAULT_INCLUDES_LEGACY = false;
	public static final String DEFAULT_NAME = _defaults.APP_NAME;
	public static final String DEFAULT_USER = _defaults.USER;
	public static final boolean DEFAULT_CREDENTIALS_ENCRYPTED = true;
	public static final boolean DEFAULT_CREDENTIALS_MEMORY = true;
	
	private static _ini _instance = new _ini();

	public _ini() { }

	public static boolean is_populated() { return _instance._populated; }
	
	public static boolean includes_legacy() { return _instance.INCLUDES_LEGACY; }
	
	public static String get_name_start() { return _instance.NAME; }

	public static HashMap<String, Object> get_info(String name_, boolean includes_legacy_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		if (strings.is_ok(name_)) info.put(INFO_NAME, name_);
		
		info.put(INFO_INCLUDES_LEGACY, includes_legacy_);
		
		return info;
	}
	
	public static Object get_info_val(HashMap<String, Object> info_, String key_)
	{
		Object output = null;
		if (info_ == null || key_ == null) return output;
		
		if (info_.containsKey(key_)) output = info_.get(key_);
		
		if (key_.equals(INFO_NAME))
		{
			if (!generic.is_string(output)) output = strings.DEFAULT;
		}
		else if (key_.equals(INFO_INCLUDES_LEGACY)) 
		{
			if (!generic.is_boolean(output)) output = DEFAULT_INCLUDES_LEGACY;
		}

		return output;
	}
	
	public static HashMap<String, Object> get_dbs_setup(String db_name_, String setup_, String user_, String host_, boolean encrypted_) { return db.get_setup_vals(db_name_, setup_, user_, host_, encrypted_); }
	
	public static HashMap<String, Object> get_dbs_setup(String setup_, String user_, String host_, boolean encrypted_) { return db.get_setup_vals(setup_, user_, host_, encrypted_); }

	public static HashMap<String, Object> get_dbs_setup(String setup_, String username_, String password_, String host_) { return db.get_setup_vals(setup_, username_, password_, host_); }
	
	public static Object get_dbs_setup_val(HashMap<String, Object> dbs_setup_, String key_)
	{
		Object output = null;
		if (dbs_setup_ == null || key_ == null) return output;
		
		if (dbs_setup_.containsKey(key_)) output = dbs_setup_.get(key_);
		
		if 
		(
			key_.equals(DBS_SETUP_NAME) || key_.equals(DBS_SETUP_MAX_POOL) || key_.equals(DBS_SETUP_HOST) ||
			key_.equals(DBS_SETUP_TYPE) || key_.equals(DBS_SETUP_CREDENTIALS_USERNAME) || 
			key_.equals(DBS_SETUP_CREDENTIALS_PASSWORD) || key_.equals(DBS_SETUP_CREDENTIALS_USER)
		)
		{
			if (!generic.is_string(output)) output = strings.DEFAULT;
		}
		else if (key_.equals(DBS_SETUP_CREDENTIALS_ENCRYPTED) || key_.equals(DBS_SETUP_CREDENTIALS_MEMORY)) 
		{
			if (!generic.is_boolean(output)) output = (key_.equals(DBS_SETUP_CREDENTIALS_ENCRYPTED) ? DEFAULT_CREDENTIALS_ENCRYPTED : DEFAULT_CREDENTIALS_MEMORY);
		}
		
		return output;
	}
	
	public static void start() { if (!_instance._populated) start(null, DEFAULT_INCLUDES_LEGACY); }

	public static void start(HashMap<String, Object> info_) { if (!_instance._populated) _instance.populate_all((String)get_info_val(info_, INFO_NAME), (boolean)get_info_val(info_, INFO_INCLUDES_LEGACY)); }

	public static void start(String name_, boolean includes_legacy_) { if (!_instance._populated) _instance.populate_all(name_, includes_legacy_); }

	public static void start(HashMap<String, Object> info_, HashMap<String, Object> dbs_setup_) { start((String)get_info_val(info_, INFO_NAME), (boolean)get_info_val(info_, INFO_INCLUDES_LEGACY), dbs_setup_); }

	public static void start(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_) { start(name_, includes_legacy_, dbs_setup_, null); }

	public static void start(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_, String[] types_to_ignore_) { if (!_instance._populated) _instance.populate_all(name_, includes_legacy_, dbs_setup_, types_to_ignore_); }

	public static void start(String name_, String user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) { if (!_instance._populated) _instance.populate_all(name_, user_, null, null, dbs_host_, dbs_encrypted_, includes_legacy_); }

	public static void start(String name_, String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_) { if (!_instance._populated) _instance.populate_all(name_, null, dbs_username_, dbs_password_, dbs_host_, false, includes_legacy_); }
}