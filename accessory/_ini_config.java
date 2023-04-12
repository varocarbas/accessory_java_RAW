package accessory;

import java.util.HashMap;

class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();

	public _ini_config() { }

	public static void populate() { _instance.populate_all(); }

	protected void populate_all_internal()
	{
		populate_basic();
		
		populate_credentials();
		
		populate_crypto();
		
		populate_logs();
		
		populate_numbers();
		
		populate_strings();
		
		populate_os();
	}

	private static void populate_basic()
	{
		String type = _types.CONFIG_BASIC;

		config.update_ini(type, _types.CONFIG_BASIC_NAME, _defaults.APP_NAME);

		for (String subtype: _types.get_subtypes(_types.CONFIG_BASIC_DIR)) { config.update_ini(type, subtype, paths.get_default_dir(subtype)); }
	}

	private boolean populate_credentials()
	{
		String type = _types.CONFIG_CREDENTIALS;

		HashMap<String, Object> vals = new HashMap<String, Object>();		
		vals.put(credentials.CONFIG_WHERE, credentials.DEFAULT_WHERE);
		vals.put(credentials.CONFIG_FILE_EXTENSION, credentials.DEFAULT_FILE_EXTENSION);
		vals.put(credentials.CONFIG_FILE_USERNAME, credentials.DEFAULT_FILE_USERNAME);
		vals.put(credentials.CONFIG_FILE_PASSWORD, credentials.DEFAULT_FILE_PASSWORD);
		vals.put(credentials.CONFIG_FILE_ENCRYPTED, credentials.DEFAULT_FILE_ENCRYPTED);

		return populate(type, null, vals);
	}

	private boolean populate_crypto()
	{
		String type = _types.CONFIG_CRYPTO;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(crypto.CONFIG_STORAGE, crypto.DEFAULT_STORAGE);
		vals.put(crypto.CONFIG_ALGO_CIPHER, crypto.DEFAULT_ALGO_CIPHER);
		vals.put(crypto.CONFIG_ALGO_KEY, crypto.DEFAULT_ALGO_KEY);
		vals.put(crypto.CONFIG_FILES_EXTENSION, crypto.DEFAULT_FILES_EXTENSION);
		vals.put(crypto.CONFIG_LOG_INFO, crypto.DEFAULT_LOG_INFO);
		
		return populate(type, null, vals);
	}

	private boolean populate_logs()
	{
		String type = _types.CONFIG_LOGS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(logs.CONFIG_OUT_SCREEN, logs.DEFAULT_OUT_SCREEN);
		vals.put(logs.CONFIG_OUT_FILE, logs.DEFAULT_OUT_FILE);
		vals.put(logs.CONFIG_ERRORS_TIMESTAMP, logs.DEFAULT_ERRORS_TIMESTAMP);
		
		return populate(type, null, vals);
	}	

	private boolean populate_numbers()
	{
		String type = _types.CONFIG_NUMBERS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(numbers.CONFIG_PERC_REF_LARGER, numbers.DEFAULT_PERC_REF_LARGER);

		return populate(type, null, vals);
	}

	private boolean populate_strings()
	{
		String type = _types.CONFIG_STRINGS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(strings.CONFIG_ENCODING, strings.DEFAULT_ENCODING);

		return populate(type, null, vals);
	}

	private boolean populate_os()
	{
		String type = _types.CONFIG_OS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(os.CONFIG_IS_VIRTUAL_MACHINE, os.DEFAULT_IS_VIRTUAL_MACHINE);

		return populate(type, null, vals);
	}
}