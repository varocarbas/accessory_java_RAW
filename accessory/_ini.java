package accessory;

class _ini 
{
	//This method is expected to be called every time a class is loaded.
	public static void load() 
	{
		load_basic();
		load_sql();
		load_credentials();
	}
	
	private static void load_basic()
	{
		String type = types._CONFIG_BASIC;
		
		_config.update_ini(type, types._CONFIG_BASIC_NAME, defaults.APP_NAME);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_APP, defaults.DIR_APP);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_INI, defaults.DIR_INI);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_ERRORS, defaults.DIR_ERRORS);
	}
	
	private static void load_sql()
	{
		String type = types._CONFIG_SQL;
		
		_config.update_ini(type, types._CONFIG_SQL_TYPE, defaults.SQL_TYPE);
		_config.update_ini(type, types._CONFIG_SQL_MAX_POOL, defaults.SQL_MAX_POOL);
		_config.update_ini(type, types._CONFIG_SQL_DB, defaults.SQL_DB);
		_config.update_ini(type, types._CONFIG_SQL_HOST, defaults.SQL_HOST);
		_config.update_ini(type, types._CONFIG_SQL_USER, defaults.SQL_USER);
		_config.update_ini(type, types._CONFIG_SQL_ERROR_EXIT, strings.from_boolean(defaults.SQL_ERROR_EXIT));
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_TYPE, defaults.SQL_CREDENTIALS_TYPE);
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_WHERE, defaults.SQL_CREDENTIALS_WHERE);
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_ENCRYPTED, defaults.SQL_CREDENTIALS_ENCRYPTED);
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_USERNAME, defaults.SQL_CREDENTIALS_USERNAME);
		_config.update_ini(type, types._CONFIG_SQL_CREDENTIALS_PASSWORD, defaults.SQL_CREDENTIALS_PASSWORD);
		
		_config.update_ini(type, keys.SERVER, defaults.SQL_HOST);
	}
	
	private static void load_credentials()
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
}