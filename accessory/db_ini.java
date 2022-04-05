package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_ini 
{
	public static void load() 
	{
		load_types();
		load_sources();
	}
	
	//Method expected to be called together with each load_config_sources_[source]().
	public static void load_config_sources_default_fields(String main_)
	{
		config.update_ini(main_, db.FIELD_ID, _defaults.DB_DEFAULT_COL_ID);
		config.update_ini(main_, db.FIELD_TIMESTAMP, _defaults.DB_DEFAULT_COL_TIMESTAMP);	
	}

	private static void load_sources()
	{
		HashMap<String, String> source_mains = new HashMap<String, String>();
		source_mains.put(types.CONFIG_LOGS_DB_SOURCE, types.CONFIG_LOGS);
		source_mains.put(types.CONFIG_TESTS_DB_SOURCE, types.CONFIG_TESTS);
		
		load_sources_all(source_mains);
	}
	
	//Method including the types more closely related to the DB setup.
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
		String main = types.CONFIG_DB;
		String[] secs = { types.CONFIG_LOGS_DB };

		HashMap<String, String> vals = load_config_linked_db_vals();

		ini.load_config_linked_update(main, secs, vals);
	}
	
	private static HashMap<String, String> load_config_linked_db_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		vals.put(types.CONFIG_DB_TYPE, _defaults.DB_TYPE);
		vals.put(types.CONFIG_DB_SETUP, _defaults.DB_SETUP);
		vals.put(db.MAX_POOL, _defaults.DB_MAX_POOL);
		vals.put(db.NAME, _defaults.DB_NAME);
		vals.put(db.HOST, _defaults.DB_HOST);
		vals.put(db.USER, _defaults.DB_USER);
		vals.put(types.CONFIG_DB_ERROR_EXIT, strings.from_boolean(_defaults.DB_ERROR_EXIT));
		vals.put(types.CONFIG_DB_CREDENTIALS_ENCRYPTED, strings.from_boolean(_defaults.DB_CREDENTIALS_ENCRYPTED));
		vals.put(db.USERNAME, _defaults.DB_CREDENTIALS_USERNAME);
		vals.put(db.PASSWORD, _defaults.DB_CREDENTIALS_PASSWORD);

		return vals;
	}
	
	private static void load_config_sources()
	{
		String[] mains = new String[] { types.CONFIG_LOGS, types.CONFIG_TESTS  };
		
		for (String main: mains)
		{
			load_config_sources_default_fields(main);	
			
			if (main.equals(types.CONFIG_LOGS)) load_config_sources_logs(main);
			else if (main.equals(types.CONFIG_TESTS)) load_config_sources_tests(main);
		}
	}

	private static void load_config_sources_logs(String main_)
	{
		config.update_ini(main_, logs.SOURCE, _defaults.LOGS_DB_TABLE);
		config.update_ini(main_, logs.FIELD_ID, _defaults.LOGS_DB_COL_ID);
		config.update_ini(main_, logs.FIELD_MESSAGE, _defaults.LOGS_DB_COL_MESSAGE);
	}

	private static void load_config_sources_tests(String main_)
	{
		config.update_ini(main_, types.CONFIG_TESTS_DB, _defaults.DB_NAME);
		config.update_ini(main_, tests.SOURCE, _defaults.TESTS_DB_TABLE);
		config.update_ini(main_, tests.FIELD_INT, _defaults.TESTS_DB_COL_INT);
		config.update_ini(main_, tests.FIELD_STRING, _defaults.TESTS_DB_COL_STRING);
		config.update_ini(main_, tests.FIELD_DECIMAL, _defaults.TESTS_DB_COL_DECIMAL);
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
		if (source_.equals(types.CONFIG_LOGS_DB_SOURCE)) load_sources_source_logs(source_);
		else if (source_.equals(types.CONFIG_TESTS_DB_SOURCE)) load_sources_source_tests(source_);
	}
	
	private static void load_sources_source_logs(String source_)
	{
		if (db.source_is_ok(source_)) return;
		
		HashMap<String, db_field> fields = db.get_default_fields();
		fields.put(logs.FIELD_ID, new db_field(data.INT));
		fields.put(logs.FIELD_MESSAGE, new db_field(data.STRING));
		
		db.add_source(source_, fields);
	}

	private static void load_sources_source_tests(String source_)
	{
		if (db.source_is_ok(source_)) return;
		
		HashMap<String, db_field> fields = db.get_default_fields();
		fields.put(tests.FIELD_INT, new db_field(data.INT));
		fields.put(tests.FIELD_STRING, new db_field(data.STRING, strings.DEFAULT_SIZE, 0));
		fields.put(tests.FIELD_DECIMAL, new db_field(data.DECIMAL, 15, 3));

		db.add_source(source_, fields);
	}
}