package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_ini 
{
	public static void load() 
	{
		load_aliases_types();
		load_sources();
	}
	
	//Method expected to be called together with each load_config_sources_[source]().
	public static void load_config_sources_default_fields(String main_)
	{
		config.update_ini(main_, _types.CONFIG_DB_FIELDS_DEFAULT_ID, _defaults.DB_FIELDS_DEFAULT_ID);
		config.update_ini(main_, _types.CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, _defaults.DB_FIELDS_DEFAULT_TIMESTAMP);	
	}

	private static void load_sources()
	{
		HashMap<String, String> source_mains = new HashMap<String, String>();
		source_mains.put(_types.CONFIG_LOGS_DB_SOURCE, _types.CONFIG_LOGS);
		source_mains.put(_types.CONFIG_TESTS_DB_SOURCE, _types.CONFIG_TESTS);
		
		load_sources_all(source_mains);
	}
	
	//Method including the aliases and types more closely related to the DB setup.
	private static void load_aliases_types()
	{
		load_types();
	}
	
	private static void load_types()
	{
		load_types_config();
	}
	
	private static void load_types_config()
	{
		load_config_db();
		load_config_sources();

		load_config_linked_db();
	}

	private static void load_config_db()
	{
		//Loaded via load_config_linked_db().
	}
	
	private static void load_config_linked_db()
	{
		String main = _types.CONFIG_DB;
		String[] secs = { _types.CONFIG_LOGS_DB };

		HashMap<String, String> vals = load_config_linked_db_vals();

		ini.load_config_linked_update(main, secs, vals);
	}
	
	private static HashMap<String, String> load_config_linked_db_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		vals.put(_types.CONFIG_DB_TYPE, _defaults.DB_TYPE);
		vals.put(_types.CONFIG_DB_SETUP, _defaults.DB_SETUP);
		vals.put(_types.CONFIG_DB_MAX_POOL, _defaults.DB_MAX_POOL);
		vals.put(_types.CONFIG_DB_NAME, _defaults.DB_NAME);
		vals.put(_types.CONFIG_DB_HOST, _defaults.DB_HOST);
		vals.put(_types.CONFIG_DB_USER, _defaults.DB_USER);
		vals.put(_types.CONFIG_DB_ERROR_EXIT, strings.from_boolean(_defaults.DB_ERROR_EXIT));
		vals.put(_types.CONFIG_DB_CREDENTIALS_TYPE, _defaults.DB_CREDENTIALS_TYPE);
		vals.put(_types.CONFIG_DB_CREDENTIALS_WHERE, _defaults.DB_CREDENTIALS_WHERE);
		vals.put(_types.CONFIG_DB_CREDENTIALS_ENCRYPTED, strings.from_boolean(_defaults.DB_CREDENTIALS_ENCRYPTED));
		vals.put(_types.CONFIG_DB_CREDENTIALS_USERNAME, _defaults.DB_CREDENTIALS_USERNAME);
		vals.put(_types.CONFIG_DB_CREDENTIALS_PASSWORD, _defaults.DB_CREDENTIALS_PASSWORD);

		return vals;
	}
	
	private static void load_config_sources()
	{
		String[] mains = new String[] { _types.CONFIG_LOGS, _types.CONFIG_TESTS  };
		
		for (String main: mains)
		{
			load_config_sources_default_fields(main);	
			
			if (main.equals(_types.CONFIG_LOGS)) load_config_sources_logs(main);
			else if (main.equals(_types.CONFIG_TESTS)) load_config_sources_tests(main);
		}
	}

	private static void load_config_sources_logs(String main_)
	{
		config.update_ini(main_, _types.CONFIG_LOGS_DB_SOURCE, _defaults.LOGS_DB_SOURCE);
		config.update_ini(main_, _types.CONFIG_LOGS_DB_FIELD_ID, _defaults.LOGS_DB_FIELD_ID);
		config.update_ini(main_, _types.CONFIG_LOGS_DB_FIELD_MESSAGE, _defaults.LOGS_DB_FIELD_MESSAGE);
	}

	private static void load_config_sources_tests(String main_)
	{
		config.update_ini(main_, _types.CONFIG_TESTS_DB, _defaults.DB_NAME);
		config.update_ini(main_, _types.CONFIG_TESTS_DB_SOURCE, _defaults.TESTS_DB_SOURCE);
		config.update_ini(main_, _types.CONFIG_TESTS_DB_FIELD_INT, _defaults.TESTS_DB_FIELD_INT);
		config.update_ini(main_, _types.CONFIG_TESTS_DB_FIELD_STRING, _defaults.TESTS_DB_FIELD_STRING);
		config.update_ini(main_, _types.CONFIG_TESTS_DB_FIELD_DECIMAL, _defaults.TESTS_DB_FIELD_DECIMAL);
	}
	
	private static void load_sources_all(HashMap<String, String> source_mains_)
	{		
		for (Entry<String, String> item: source_mains_.entrySet())
		{
			String source = item.getKey();
			String main = item.getValue();
		
			load_sources_source(source);			
			db.add_source_main(source, main);
		}
	}
	
	private static void load_sources_source(String source_)
	{
		if (source_.equals(_types.CONFIG_LOGS_DB_SOURCE)) load_sources_source_logs(source_);
		else if (source_.equals(_types.CONFIG_TESTS_DB_SOURCE)) load_sources_source_tests(source_);
	}
	
	private static void load_sources_source_logs(String source_)
	{
		if (db.source_is_ok(source_)) return;
		
		HashMap<String, db_field> fields = db.get_default_fields();
		fields.put(_types.CONFIG_LOGS_DB_FIELD_ID, new db_field(_types.DATA_INT));
		fields.put(_types.CONFIG_LOGS_DB_FIELD_MESSAGE, new db_field(_types.DATA_STRING));
		
		db.add_source(source_, fields);
	}

	private static void load_sources_source_tests(String source_)
	{
		if (db.source_is_ok(source_)) return;
		
		HashMap<String, db_field> fields = db.get_default_fields();
		fields.put(_types.CONFIG_TESTS_DB_FIELD_INT, new db_field(_types.DATA_INT));
		fields.put(_types.CONFIG_TESTS_DB_FIELD_STRING, new db_field(_types.DATA_STRING, strings.SIZE_DEFAULT, 0));
		fields.put(_types.CONFIG_TESTS_DB_FIELD_DECIMAL, new db_field(_types.DATA_DECIMAL, 15, 3));

		db.add_source(source_, fields);
	}
}