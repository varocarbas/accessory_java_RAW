package accessory;

import java.util.HashMap;

class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();
	
	public _ini_config() { }
	
	public static void populate() { populate_internal(_instance); }

	protected void populate_all()
	{
		populate_generic_basic();
		populate_generic_credentials();
		populate_generic_crypto();
		populate_generic_logs();
	}
	
	private static void populate_generic_basic()
	{
		String type = types.CONFIG_BASIC;
		
		config.update_ini(type, types.CONFIG_BASIC_NAME, _defaults.APP_NAME);
		
		for (String subtype: types.get_subtypes(types.CONFIG_BASIC_DIR))
		{
			config.update_ini(type, subtype, paths.get_default_dir(subtype));
		}
	}

	private boolean populate_generic_credentials()
	{
		String type = types.CONFIG_CREDENTIALS;
		
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_CREDENTIALS_WHERE, _defaults.CREDENTIALS_WHERE);
		vals.put(types.CONFIG_CREDENTIALS_FILE_EXTENSION, _defaults.CREDENTIALS_FILE_EXTENSION);
		vals.put(types.CONFIG_CREDENTIALS_FILE_USERNAME, _defaults.CREDENTIALS_FILE_USERNAME);
		vals.put(types.CONFIG_CREDENTIALS_FILE_PASSWORD, _defaults.CREDENTIALS_FILE_PASSWORD);
		vals.put(types.CONFIG_CREDENTIALS_FILE_ENCRYPTED, _defaults.CREDENTIALS_FILE_ENCRYPTED);

		return populate(type, null, vals);
	}

	private boolean populate_generic_crypto()
	{
		String type = types.CONFIG_CRYPTO;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_CRYPTO_FILE_CIPHER, _defaults.CRYPTO_FILE_CIPHER);
		vals.put(types.CONFIG_CRYPTO_FILE_KEY, _defaults.CRYPTO_FILE_KEY);
		vals.put(types.CONFIG_CRYPTO_FILE_EXTENSION, _defaults.CRYPTO_FILE_EXTENSION);

		return populate(type, null, vals);
	}

	private boolean populate_generic_logs()
	{
		String type = types.CONFIG_LOGS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_LOGS_OUT_SCREEN, _defaults.LOGS_SCREEN);
		vals.put(types.CONFIG_LOGS_OUT_FILE, _defaults.LOGS_FILE);
		
		return populate(type, null, vals);
	}	
}