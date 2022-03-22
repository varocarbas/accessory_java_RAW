package accessory;

public abstract class config_ini 
{
	//Method expected to be called from _ini.load().
	public static void load() 
	{
		load_types();
	}

	//Method including all the generic _config types, not easily included in any other ini class.
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
		String type = types.CONFIG_BASIC;

		config.update_ini(type, types.CONFIG_BASIC_NAME, _defaults.APP_NAME);
		config.update_ini(type, types.CONFIG_BASIC_DIR_APP, paths.DEFAULT_DIR_APP);
		config.update_ini(type, types.CONFIG_BASIC_DIR_INI, paths.DEFAULT_DIR_INI);
		config.update_ini(type, types.CONFIG_LOGS_DIR, paths.DEFAULT_DIR_LOGS);
	}

	private static void load_config_credentials()
	{
		String type = types.CONFIG_CREDENTIALS;

		config.update_ini(type, types.CONFIG_CREDENTIALS_ENCRYPTED, _defaults.CREDENTIALS_ENCRYPTED);
		config.update_ini(type, types.CONFIG_CREDENTIALS_WHERE, _defaults.CREDENTIALS_WHERE);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_DIR, paths.DEFAULT_DIR_CREDENTIALS);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_EXTENSION, _defaults.CREDENTIALS_FILE_EXTENSION);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_SEPARATOR, _defaults.CREDENTIALS_FILE_SEPARATOR);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_USERNAME, _defaults.CREDENTIALS_FILE_USERNAME);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_PASSWORD, _defaults.CREDENTIALS_FILE_PASSWORD);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_ENCRYPTED, _defaults.CREDENTIALS_FILE_ENCRYPTED);
	}

	private static void load_config_logs()
	{
		String type = types.CONFIG_LOGS;

		config.update_ini(type, types.CONFIG_LOGS_DIR, paths.DEFAULT_DIR_LOGS);
		config.update_ini(type, logs.SCREEN, strings.from_boolean(logs.DEFAULT_SCREEN));
		config.update_ini(type, logs.FILE, strings.from_boolean(logs.DEFAULT_FILE));
		config.update_ini(type, logs.DB, strings.from_boolean(logs.DEFAULT_DB));
	}	
	
	private static void load_config_subtypes()
	{	
		String type = types.CONFIG_DB;
		String[] subtypes = { types.CONFIG_DB_CREDENTIALS };
		config.update_subtypes(type, subtypes);

		type = types.CONFIG_LOGS_DB;
		config.update_subtypes(type, subtypes);

		type = types.CONFIG_CREDENTIALS;
		subtypes = new String[] { types.CONFIG_CREDENTIALS_FILE };
		config.update_subtypes(type, subtypes);

		type = types.CONFIG_LOGS;
		subtypes = new String[] { types.CONFIG_LOGS_OUT };
		config.update_subtypes(type, subtypes);
	}
}