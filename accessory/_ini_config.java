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
		
		populate_generic_numbers();
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
		vals.put(credentials.CONFIG_WHERE, credentials.DEFAULT_WHERE);
		vals.put(credentials.CONFIG_FILE_EXTENSION, credentials.DEFAULT_FILE_EXTENSION);
		vals.put(credentials.CONFIG_FILE_USERNAME, credentials.DEFAULT_FILE_USERNAME);
		vals.put(credentials.CONFIG_FILE_PASSWORD, credentials.DEFAULT_FILE_PASSWORD);
		vals.put(credentials.CONFIG_FILE_ENCRYPTED, credentials.DEFAULT_FILE_ENCRYPTED);

		return populate(type, null, vals);
	}

	private boolean populate_generic_crypto()
	{
		String type = types.CONFIG_CRYPTO;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(crypto.CONFIG_STORAGE, crypto.DEFAULT_STORAGE);
		vals.put(crypto.CONFIG_ALGO_CIPHER, crypto.DEFAULT_ALGO_CIPHER);
		vals.put(crypto.CONFIG_ALGO_KEY, crypto.DEFAULT_ALGO_KEY);
		vals.put(crypto.CONFIG_FILES_EXTENSION, crypto.DEFAULT_FILES_EXTENSION);
		vals.put(crypto.CONFIG_LOG_INFO, crypto.DEFAULT_LOG_INFO);
		
		return populate(type, null, vals);
	}

	private boolean populate_generic_logs()
	{
		String type = types.CONFIG_LOGS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(logs.CONFIG_OUT_SCREEN, logs.DEFAULT_OUT_SCREEN);
		vals.put(logs.CONFIG_OUT_FILE, logs.DEFAULT_OUT_FILE);
		vals.put(logs.CONFIG_ERRORS_TIMESTAMP, logs.DEFAULT_ERRORS_TIMESTAMP);
		
		return populate(type, null, vals);
	}	

	private boolean populate_generic_numbers()
	{
		String type = types.CONFIG_NUMBERS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(numbers.CONFIG_PERC_REF_LARGER, numbers.DEFAULT_PERC_REF_LARGER);

		return populate(type, null, vals);
	}
}