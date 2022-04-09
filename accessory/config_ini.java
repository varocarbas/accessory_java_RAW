package accessory;

public abstract class config_ini 
{
	//Method expected to be called from ini.load().
	public static void populate() 
	{
		populate_generic();
	}

	private static void populate_generic()
	{
		populate_generic_basic();
		populate_generic_credentials();
		populate_generic_crypto();
		populate_generic_logs();
	}
	
	private static void populate_generic_basic()
	{
		String main = types.CONFIG_BASIC;
		
		config.update_ini(main, types.CONFIG_BASIC_NAME, _defaults.APP_NAME);
		
		for (String subtype: types.get_subtypes(types.CONFIG_BASIC_DIR))
		{
			config.update_ini(main, subtype, paths.get_default_dir(subtype));
		}
	}

	private static void populate_generic_credentials()
	{
		String type = types.CONFIG_CREDENTIALS;

		config.update_ini(type, credentials.WHERE, _defaults.CREDENTIALS_WHERE);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_EXTENSION, _defaults.CREDENTIALS_FILE_EXTENSION);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_USERNAME, _defaults.CREDENTIALS_FILE_USERNAME);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_PASSWORD, _defaults.CREDENTIALS_FILE_PASSWORD);
		config.update_ini(type, types.CONFIG_CREDENTIALS_FILE_ENCRYPTED, _defaults.CREDENTIALS_FILE_ENCRYPTED);
	}

	private static void populate_generic_crypto()
	{
		String type = types.CONFIG_CRYPTO;

		config.update_ini(type, types.CONFIG_CRYPTO_FILE_CIPHER, _defaults.CRYPTO_FILE_CIPHER);
		config.update_ini(type, types.CONFIG_CRYPTO_FILE_KEY, _defaults.CRYPTO_FILE_KEY);
		config.update_ini(type, types.CONFIG_CRYPTO_FILE_EXTENSION, _defaults.CRYPTO_FILE_EXTENSION);
	}

	private static void populate_generic_logs()
	{
		String type = types.CONFIG_LOGS;

		config.update_ini(type, logs.SCREEN, _defaults.LOGS_SCREEN);
		config.update_ini(type, logs.FILE, _defaults.LOGS_FILE);
	}	
}