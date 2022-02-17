package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

class _ini 
{
	//This method is expected to be called every time a class is loaded.
	public static void load() 
	{
		load_types();
		load_config();
	}

	private static void load_types()
	{
		types.update_aliases(keys.APP, types._CONFIG_BASIC_NAME);
		types.update_aliases(keys.DIR, types._CONFIG_BASIC_DIR_APP);
		types.update_aliases(keys.INI, types._CONFIG_BASIC_DIR_INI);

		types.update_aliases(keys.DB, types._CONFIG_DB_NAME);
		types.update_aliases(keys.HOST, types._CONFIG_DB_HOST);
		types.update_aliases(keys.SERVER, types._CONFIG_DB_HOST);
		types.update_aliases(keys.USER, types._CONFIG_DB_USER);
		types.update_aliases(keys.USERNAME, types._CONFIG_DB_CREDENTIALS_USERNAME);
		types.update_aliases(keys.PASSWORD, types._CONFIG_DB_CREDENTIALS_PASSWORD);

		types.update_aliases(keys.CONSOLE, types._CONFIG_LOGS_OUT_CONSOLE);
		types.update_aliases(keys.FILE, types._CONFIG_LOGS_OUT_FILE);
	}

	private static void load_config()
	{
		load_config_basic();
		load_config_db();
		load_config_credentials();
		load_config_logs();

		load_config_subtypes();
		load_config_linked();
	}

	private static void load_config_basic()
	{
		String type = types._CONFIG_BASIC;

		_config.update_ini(type, types._CONFIG_BASIC_NAME, defaults.APP_NAME);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_APP, defaults.DIR_APP);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_INI, defaults.DIR_INI);
		_config.update_ini(type, types._CONFIG_LOGS_DIR, defaults.DIR_LOGS);
	}

	private static void load_config_db()
	{
		//Loaded via load_config_linked_common_db().
	}

	private static void load_config_credentials()
	{
		String type = types._CONFIG_CREDENTIALS;

		_config.update_ini(type, types._CONFIG_CREDENTIALS_ENCRYPTED, defaults.CREDENTIALS_ENCRYPTED);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_WHERE, defaults.CREDENTIALS_WHERE);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_FILE_DIR, defaults.CREDENTIALS_FILE_DIR);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_FILE_EXTENSION, defaults.CREDENTIALS_FILE_EXTENSION);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_FILE_SEPARATOR, defaults.CREDENTIALS_FILE_SEPARATOR);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_FILE_USERNAME, defaults.CREDENTIALS_FILE_USERNAME);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_FILE_PASSWORD, defaults.CREDENTIALS_FILE_PASSWORD);
		_config.update_ini(type, types._CONFIG_CREDENTIALS_FILE_ENCRYPTED, defaults.CREDENTIALS_FILE_ENCRYPTED);
	}

	private static void load_config_logs()
	{
		String type = types._CONFIG_LOGS;

		_config.update_ini(type, types._CONFIG_LOGS_DIR, defaults.DIR_LOGS);
		_config.update_ini(type, types._CONFIG_LOGS_OUT_CONSOLE, strings.from_boolean(defaults.LOGS_CONSOLE));
		_config.update_ini(type, types._CONFIG_LOGS_OUT_FILE, strings.from_boolean(defaults.LOGS_FILE));
		_config.update_ini(type, types._CONFIG_LOGS_OUT_DB, strings.from_boolean(defaults.LOGS_DB));

		load_config_logs_db(type);
	}

	private static void load_config_logs_db(String type_)
	{
		_config.update_ini(type_, types._CONFIG_LOGS_DB_TABLE, defaults._CONFIG_LOGS_DB_TABLE);
		_config.update_ini(type_, types._CONFIG_LOGS_DB_COL_ID, defaults._CONFIG_LOGS_DB_COL_ID);
		_config.update_ini(type_, types._CONFIG_LOGS_DB_COL_MESSAGE, defaults._CONFIG_LOGS_DB_COL_MESSAGE);
	}

	private static void load_config_subtypes()
	{	
		String type = types._CONFIG_DB;
		String[] subtypes = new String[] { types._CONFIG_DB_CREDENTIALS };
		_config.update_subtypes(type, subtypes);

		type = types._CONFIG_LOGS_DB;
		_config.update_subtypes(type, subtypes);

		type = types._CONFIG_CREDENTIALS;
		subtypes = new String[] { types._CONFIG_CREDENTIALS_FILE };
		_config.update_subtypes(type, subtypes);

		type = types._CONFIG_LOGS;
		subtypes = new String[] { types._CONFIG_LOGS_OUT };
		_config.update_subtypes(type, subtypes);
	}

	private static void load_config_linked()
	{
		load_config_linked_db();
	}

	private static void load_config_linked_db()
	{
		String main = types._CONFIG_DB;
		String[] secs = new String[] { types._CONFIG_LOGS_DB };

		HashMap<String, String> vals = load_config_linked_common_db_vals();

		load_config_linked_common_update(main, secs, vals);
	}

	private static HashMap<String, String> load_config_linked_common_db_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		vals.put(types._CONFIG_DB_TYPE, defaults.DB_TYPE);
		vals.put(types._CONFIG_DB_MAX_POOL, defaults.DB_MAX_POOL);
		vals.put(types._CONFIG_DB_NAME, defaults.DB_DB);
		vals.put(types._CONFIG_DB_HOST, defaults.DB_HOST);
		vals.put(types._CONFIG_DB_USER, defaults.DB_USER);
		vals.put(types._CONFIG_DB_ERROR_EXIT, strings.from_boolean(defaults.DB_ERROR_EXIT));
		vals.put(types._CONFIG_DB_CREDENTIALS_TYPE, defaults.DB_CREDENTIALS_TYPE);
		vals.put(types._CONFIG_DB_CREDENTIALS_WHERE, defaults.DB_CREDENTIALS_WHERE);
		vals.put(types._CONFIG_DB_CREDENTIALS_ENCRYPTED, strings.from_boolean(defaults.DB_CREDENTIALS_ENCRYPTED));
		vals.put(types._CONFIG_DB_CREDENTIALS_USERNAME, defaults.DB_CREDENTIALS_USERNAME);
		vals.put(types._CONFIG_DB_CREDENTIALS_PASSWORD, defaults.DB_CREDENTIALS_PASSWORD);

		return vals;
	}

	private static void load_config_linked_common_update(String main_, String[] secs_, HashMap<String, String> vals_)
	{	
		_config.update_linked(main_, secs_);

		load_config_linked_common_update_internal(main_, vals_, true);

		for (String sec: secs_)
		{
			load_config_linked_common_update_internal(sec, vals_, false);
		}
	}

	private static void load_config_linked_common_update_internal(String type_, HashMap<String, String> vals_, boolean is_main_)
	{	
		for (Entry<String, String> val: vals_.entrySet())
		{
			_config.update_ini(type_, val.getKey(), (is_main_ ? val.getValue() : strings.DEFAULT));
		}
	}
}