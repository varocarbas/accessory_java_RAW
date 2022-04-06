package accessory;

public abstract class config_ini 
{
	//Method expected to be called from ini.load().
	public static void load() 
	{
		load_types();
	}

	//Method including all the generic config types, not easily included in any other ini class.
	private static void load_types()
	{
		load_types_config();
	}

	private static void load_types_config()
	{
		load_config_basic();
		load_config_credentials();
		load_config_crypto();
		load_config_logs();
		load_config_subtypes();
		
		//load_config_linked();
	}
	
	private static void load_config_basic()
	{
		String type = types.CONFIG_BASIC;

		config.update_ini(type, types.CONFIG_BASIC_NAME, _defaults.APP_NAME);
		config.update_ini(type, types.CONFIG_BASIC_DIR_APP, _defaults.DIR_APP);
		config.update_ini(type, types.CONFIG_BASIC_DIR_INI, _defaults.DIR_INI);
		config.update_ini(type, types.CONFIG_BASIC_DIR_LOGS, _defaults.DIR_LOGS);
		config.update_ini(type, types.CONFIG_BASIC_DIR_CREDENTIALS, _defaults.DIR_CREDENTIALS);
		config.update_ini(type, types.CONFIG_BASIC_DIR_CRYPTO, _defaults.DIR_CRYPTO);
	}

	private static void load_config_credentials()
	{
		String type = types.CONFIG_CREDENTIALS;

		config.update_ini(type, credentials.ENCRYPTED, _defaults.CREDENTIALS_ENCRYPTED);
		config.update_ini(type, credentials.WHERE, _defaults.CREDENTIALS_WHERE);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_EXTENSION, _defaults.CREDENTIALS_FILE_EXTENSION);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_SEPARATOR, _defaults.CREDENTIALS_FILE_SEPARATOR);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_USERNAME, _defaults.CREDENTIALS_FILE_USERNAME);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_PASSWORD, _defaults.CREDENTIALS_FILE_PASSWORD);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_ENCRYPTED, _defaults.CREDENTIALS_FILE_ENCRYPTED);
	}

	private static void load_config_crypto()
	{
		String type = types.CONFIG_CRYPTO;

		config.update_ini(type, types.CONFIG_CRYPTO_FILE_CIPHER, _defaults.CRYPTO_FILE_CIPHER);
		config.update_ini(type, types.CONFIG_CRYPTO_FILE_KEY, _defaults.CRYPTO_FILE_KEY);
		config.update_ini(type, types.CONFIG_CRYPTO_FILE_EXTENSION, _defaults.CRYPTO_FILE_EXTENSION);
	}

	private static void load_config_logs()
	{
		String type = types.CONFIG_LOGS;

		config.update_ini(type, logs.SCREEN, strings.from_boolean(_defaults.LOGS_SCREEN));
		config.update_ini(type, logs.FILE, strings.from_boolean(_defaults.LOGS_FILE));
	}	
	
	private static void load_config_subtypes()
	{	
		String type = types.CONFIG_DB;
		String[] subtypes = { types.CONFIG_DB_CREDENTIALS };
		config.update_subtypes(type, subtypes);

		type = types.CONFIG_CREDENTIALS;
		subtypes = new String[] { types.CONFIG_CREDENTIALS_FILE };
		config.update_subtypes(type, subtypes);

		type = types.CONFIG_LOGS;
		subtypes = new String[] { types.CONFIG_LOGS_OUT };
		config.update_subtypes(type, subtypes);
	}
}