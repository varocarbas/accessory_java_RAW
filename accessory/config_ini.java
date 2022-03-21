package accessory;

public abstract class config_ini 
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
		_types.update_aliases(_keys.APP, _types.CONFIG_BASIC_NAME);
		_types.update_aliases(_keys.DIR, _types.CONFIG_BASIC_DIR_APP);

		_types.update_aliases(_keys.DB, _types.CONFIG_DB_NAME);
		_types.update_aliases(_keys.HOST, _types.CONFIG_DB_HOST);
		_types.update_aliases(_keys.SERVER, _types.CONFIG_DB_HOST);
		_types.update_aliases(_keys.USER, _types.CONFIG_DB_USER);
		_types.update_aliases(_keys.USERNAME, _types.CONFIG_DB_CREDENTIALS_USERNAME);
		_types.update_aliases(_keys.PASSWORD, _types.CONFIG_DB_CREDENTIALS_PASSWORD);

		_types.update_aliases(_keys.SCREEN, _types.CONFIG_LOGS_OUT_SCREEN);
		_types.update_aliases(_keys.FILE, _types.CONFIG_LOGS_OUT_FILE);
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
		String type = _types.CONFIG_BASIC;

		config.update_ini(type, _types.CONFIG_BASIC_NAME, _defaults.APP_NAME);
		config.update_ini(type, _types.CONFIG_BASIC_DIR_APP, _defaults.DIR_APP);
		config.update_ini(type, _types.CONFIG_BASIC_DIR_INI, _defaults.DIR_INI);
		config.update_ini(type, _types.CONFIG_LOGS_DIR, _defaults.DIR_LOGS);
	}

	private static void load_config_credentials()
	{
		String type = _types.CONFIG_CREDENTIALS;

		config.update_ini(type, _types.CONFIG_CREDENTIALS_ENCRYPTED, _defaults.CREDENTIALS_ENCRYPTED);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_WHERE, _defaults.CREDENTIALS_WHERE);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_FILE_DIR, _defaults.CREDENTIALS_FILE_DIR);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_FILE_EXTENSION, _defaults.CREDENTIALS_FILE_EXTENSION);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_FILE_SEPARATOR, _defaults.CREDENTIALS_FILE_SEPARATOR);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_FILE_USERNAME, _defaults.CREDENTIALS_FILE_USERNAME);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_FILE_PASSWORD, _defaults.CREDENTIALS_FILE_PASSWORD);
		config.update_ini(type, _types.CONFIG_CREDENTIALS_FILE_ENCRYPTED, _defaults.CREDENTIALS_FILE_ENCRYPTED);
	}

	private static void load_config_logs()
	{
		String type = _types.CONFIG_LOGS;

		config.update_ini(type, _types.CONFIG_LOGS_DIR, _defaults.DIR_LOGS);
		config.update_ini(type, _types.CONFIG_LOGS_OUT_SCREEN, strings.from_boolean(_defaults.LOGS_SCREEN));
		config.update_ini(type, _types.CONFIG_LOGS_OUT_FILE, strings.from_boolean(_defaults.LOGS_FILE));
		config.update_ini(type, _types.CONFIG_LOGS_OUT_DB, strings.from_boolean(_defaults.LOGS_DB));
	}	
	
	private static void load_config_subtypes()
	{	
		String type = _types.CONFIG_DB;
		String[] subtypes = { _types.CONFIG_DB_CREDENTIALS };
		config.update_subtypes(type, subtypes);

		type = _types.CONFIG_LOGS_DB;
		config.update_subtypes(type, subtypes);

		type = _types.CONFIG_CREDENTIALS;
		subtypes = new String[] { _types.CONFIG_CREDENTIALS_FILE };
		config.update_subtypes(type, subtypes);

		type = _types.CONFIG_LOGS;
		subtypes = new String[] { _types.CONFIG_LOGS_OUT };
		config.update_subtypes(type, subtypes);
	}
}