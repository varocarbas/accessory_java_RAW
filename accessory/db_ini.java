package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class db_ini 
{
	public static void load() 
	{
		load_sources();
		load_aliases_types();
	}

	//Method expected to be called together with each load_config_sources_[source]().
	public static void load_config_sources_default_fields(String main_)
	{
		_config.update_ini(main_, types._CONFIG_DB_FIELDS_DEFAULT_ID, defaults.DB_FIELDS_DEFAULT_ID);
		_config.update_ini(main_, types._CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, defaults.DB_FIELDS_DEFAULT_TIMESTAMP);	
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
		String main = types._CONFIG_DB;
		String[] secs = { types._CONFIG_LOGS_DB };

		HashMap<String, String> vals = load_config_linked_db_vals();

		_ini.load_config_linked_update(main, secs, vals);
	}
	
	private static HashMap<String, String> load_config_linked_db_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		vals.put(types._CONFIG_DB_TYPE, defaults.DB_TYPE);
		vals.put(types._CONFIG_DB_SETUP, defaults.DB_SETUP);
		vals.put(types._CONFIG_DB_MAX_POOL, defaults.DB_MAX_POOL);
		vals.put(types._CONFIG_DB_NAME, defaults.DB_NAME);
		vals.put(types._CONFIG_DB_HOST, defaults.DB_HOST);
		vals.put(types._CONFIG_DB_USER, defaults.DB_USER);
		vals.put(types._CONFIG_DB_ERROR_EXIT, strings.from_boolean(defaults.DB_ERROR_EXIT));
		vals.put(types._CONFIG_DB_CREDENTIALS_TYPE, defaults.DB_CREDENTIALS_TYPE);
		vals.put(types._CONFIG_DB_CREDENTIALS_WHERE, defaults.DB_CREDENTIALS_WHERE);
		vals.put(types._CONFIG_DB_CREDENTIALS_ENCRYPTED, strings.from_boolean(defaults.DB_CREDENTIALS_ENCRYPTED));
		vals.put(types._CONFIG_DB_CREDENTIALS_USERNAME, defaults.DB_CREDENTIALS_USERNAME);
		vals.put(types._CONFIG_DB_CREDENTIALS_PASSWORD, defaults.DB_CREDENTIALS_PASSWORD);

		return vals;
	}
	
	private static void load_config_sources()
	{
		String[] mains = new String[] { types._CONFIG_LOGS, types._CONFIG_TESTS  };
		
		for (String main: mains)
		{
			load_config_sources_default_fields(main);	
			
			if (main.equals(types._CONFIG_LOGS)) load_config_sources_logs(main);
			else if (main.equals(types._CONFIG_TESTS)) load_config_sources_tests(main);
		}
	}

	private static void load_config_sources_logs(String main_)
	{
		_config.update_ini(main_, types._CONFIG_LOGS_DB_SOURCE, defaults.LOGS_DB_SOURCE);
		_config.update_ini(main_, types._CONFIG_LOGS_DB_FIELD_ID, defaults.LOGS_DB_FIELD_ID);
		_config.update_ini(main_, types._CONFIG_LOGS_DB_FIELD_MESSAGE, defaults.LOGS_DB_FIELD_MESSAGE);
	}

	private static void load_config_sources_tests(String main_)
	{
		_config.update_ini(main_, types._CONFIG_TESTS_DB, defaults.DB_NAME);
		_config.update_ini(main_, types._CONFIG_TESTS_DB_SOURCE, defaults.TESTS_DB_SOURCE);
		_config.update_ini(main_, types._CONFIG_TESTS_DB_FIELD_INT, defaults.TESTS_DB_FIELD_INT);
		_config.update_ini(main_, types._CONFIG_TESTS_DB_FIELD_STRING, defaults.TESTS_DB_FIELD_STRING);
		_config.update_ini(main_, types._CONFIG_TESTS_DB_FIELD_DECIMAL, defaults.TESTS_DB_FIELD_DECIMAL);
	}
	
	private static void load_sources()
	{
		HashMap<String, String> source_mains = new HashMap<String, String>();
		source_mains.put(types._CONFIG_LOGS_DB_SOURCE, types._CONFIG_LOGS);
		source_mains.put(types._CONFIG_TESTS_DB_SOURCE, types._CONFIG_TESTS);
		
		for (Entry<String, String> item: source_mains.entrySet())
		{
			String source = item.getKey();
			String main = item.getValue();
		
			if (source.equals(types._CONFIG_LOGS_DB_SOURCE)) load_sources_logs(source);
			else if (source.equals(types._CONFIG_TESTS_DB_SOURCE)) load_sources_tests(source);
			
			db.add_source_main(source, main);
		}
	}
	
	private static void load_sources_logs(String source_)
	{
		if (db.source_is_ok(source_)) return;
		
		HashMap<String, db_field> fields = db.get_default_fields();
		fields.put(types._CONFIG_LOGS_DB_FIELD_ID, new db_field(new data(types.DATA_INTEGER, null), null));
		fields.put(types._CONFIG_LOGS_DB_FIELD_MESSAGE, new db_field(new data(types.DATA_STRING, null), null));
		
		db.add_source(source_, fields);
	}

	private static void load_sources_tests(String source_)
	{
		if (db.source_is_ok(source_)) return;
		
		HashMap<String, db_field> fields = db.get_default_fields();
		fields.put(types._CONFIG_TESTS_DB_FIELD_INT, new db_field(new data(types.DATA_INTEGER, null), null));
		fields.put(types._CONFIG_TESTS_DB_FIELD_STRING, new db_field(new data(types.DATA_STRING, null), null));
		fields.put(types._CONFIG_TESTS_DB_FIELD_DECIMAL, new db_field(new data(types.DATA_DECIMAL, null), null));

		db.add_source(source_, fields);
	}
}