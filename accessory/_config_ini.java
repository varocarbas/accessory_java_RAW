package accessory;

public abstract class _config_ini 
{
	//Method expected to be called from _ini.load().
	public static void load() 
	{
		load_aliases_types();
	}

	//Method including all the generic aliases and _config types, not easily included in any other ini class.
	private static void load_aliases_types()
	{
		load_aliases();
		load_types();
	}
	
	private static void load_aliases()
	{
		types.update_aliases(keys.APP, types._CONFIG_BASIC_NAME);
		types.update_aliases(keys.DIR, types._CONFIG_BASIC_DIR_APP);

		types.update_aliases(keys.DB, types._CONFIG_DB_NAME);
		types.update_aliases(keys.HOST, types._CONFIG_DB_HOST);
		types.update_aliases(keys.SERVER, types._CONFIG_DB_HOST);
		types.update_aliases(keys.USER, types._CONFIG_DB_USER);
		types.update_aliases(keys.USERNAME, types._CONFIG_DB_CREDENTIALS_USERNAME);
		types.update_aliases(keys.PASSWORD, types._CONFIG_DB_CREDENTIALS_PASSWORD);

		types.update_aliases(keys.SCREEN, types._CONFIG_LOGS_OUT_SCREEN);
		types.update_aliases(keys.FILE, types._CONFIG_LOGS_OUT_FILE);
	}

	private static void load_types()
	{
		load_types_config();
	}

	private static void load_types_config()
	{
		load_config_basic();
		load_config_credentials();
		load_config_logs();
		load_config_subtypes();
		
		//load_config_linked();
	}
	
	private static void load_config_basic()
	{
		String type = types._CONFIG_BASIC;

		_config.update_ini(type, types._CONFIG_BASIC_NAME, defaults.APP_NAME);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_APP, defaults.DIR_APP);
		_config.update_ini(type, types._CONFIG_BASIC_DIR_INI, defaults.DIR_INI);
		_config.update_ini(type, types._CONFIG_LOGS_DIR, defaults.DIR_LOGS);
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
		_config.update_ini(type, types._CONFIG_LOGS_OUT_SCREEN, strings.from_boolean(defaults.LOGS_SCREEN));
		_config.update_ini(type, types._CONFIG_LOGS_OUT_FILE, strings.from_boolean(defaults.LOGS_FILE));
		_config.update_ini(type, types._CONFIG_LOGS_OUT_DB, strings.from_boolean(defaults.LOGS_DB));
	}	
	
	private static void load_config_subtypes()
	{	
		String type = types._CONFIG_DB;
		String[] subtypes = { types._CONFIG_DB_CREDENTIALS };
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
}