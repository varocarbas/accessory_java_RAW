package accessory;

import java.util.HashMap;

class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();

	public _ini_config() { }

	public static void populate() { _instance.populate_all(); }

	protected void populate_all_internal()
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

		for (String subtype: types.get_subtypes(types.CONFIG_BASIC_DIR)) { config.update_ini(type, subtype, paths.get_default_dir(subtype)); }
	}

	private boolean populate_generic_credentials()
	{
		String type = types.CONFIG_CREDENTIALS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_CREDENTIALS_WHERE, credentials.DEFAULT_WHERE);
		vals.put(types.CONFIG_CREDENTIALS_FILE_EXTENSION, credentials.DEFAULT_FILE_EXTENSION);
		vals.put(types.CONFIG_CREDENTIALS_FILE_USERNAME, credentials.DEFAULT_FILE_USERNAME);
		vals.put(types.CONFIG_CREDENTIALS_FILE_PASSWORD, credentials.DEFAULT_FILE_PASSWORD);
		vals.put(types.CONFIG_CREDENTIALS_FILE_ENCRYPTED, credentials.DEFAULT_FILE_ENCRYPTED);

		return populate(type, null, vals);
	}

	private boolean populate_generic_crypto()
	{
		String type = types.CONFIG_CRYPTO;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_CRYPTO_FILE_CIPHER, crypto.DEFAULT_FILE_CIPHER);
		vals.put(types.CONFIG_CRYPTO_FILE_KEY, crypto.DEFAULT_FILE_KEY);
		vals.put(types.CONFIG_CRYPTO_FILE_EXTENSION, crypto.DEFAULT_FILE_EXTENSION);

		return populate(type, null, vals);
	}

	private boolean populate_generic_logs()
	{
		String type = types.CONFIG_LOGS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_LOGS_OUT_SCREEN, logs.DEFAULT_OUT_SCREEN);
		vals.put(types.CONFIG_LOGS_OUT_FILE, logs.DEFAULT_OUT_FILE);

		return populate(type, null, vals);
	}	
}