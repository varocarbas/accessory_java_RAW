package accessory;

class _ini 
{
	//This method is expected to be called every time a class is loaded.
	public static void load() 
	{
		load_basic();
		load_sql();
	}
	
	private static void load_basic()
	{
		String type = types._CONFIG_BASIC;
		
		_config.update_ini(type, types._CONFIG_BASIC_NAME, keys.APP);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_APP, defaults.DIR_APP);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_INI, defaults.DIR_INI);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_ERRORS, defaults.DIR_ERRORS);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_CREDENTIALS, defaults.DIR_CREDENTIALS);
	}
	
	private static void load_sql()
	{
		String type = types._CONFIG_SQL;
		
		_config.update_ini(type, types._CONFIG_SQL_TYPE, defaults.SQL_TYPE);
		_config.update_ini(type, types._CONFIG_SQL_MAX_POOL, defaults.SQL_MAX_POOL);
		_config.update_ini(type, types._CONFIG_SQL_DB, strings.get_default());
		_config.update_ini(type, types._CONFIG_SQL_HOST, defaults.SQL_HOST);
		_config.update_ini(type, types._CONFIG_SQL_USER, strings.get_default());
		_config.update_ini(type, types._CONFIG_SQL_ERROR_EXIT, strings.from_boolean(defaults.SQL_ERROR_EXIT));
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_TYPE, defaults.SQL_CREDENTIALS_TYPE);
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_WHERE, defaults.SQL_CREDENTIALS_WHERE);
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_ENCRYPTED, strings.from_boolean(defaults.SQL_CREDENTIALS_ENCRYPTED));
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_USERNAME, strings.get_default());
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_PASSWORD, strings.get_default());
	}
}