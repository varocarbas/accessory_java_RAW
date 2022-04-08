package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_ini 
{
	public static final String ERROR_SETUPS = types.ERROR_INI_DB_SETUPS;
	public static final String ERROR_SOURCES = types.ERROR_INI_DB_SOURCES;
	
	private static boolean _populated = false;
	
	public static void populate() 
	{
		if (_populated) return;
		
		String error = strings.DEFAULT;
		if (!populate_all_setups()) error = ERROR_SETUPS;
		if (error.equals(strings.DEFAULT) && !populate_all_sources()) error = ERROR_SOURCES;

		_populated = true;
		if (error.equals(strings.DEFAULT)) return;

		errors._exit = true;
		errors.manage(error);
	}

	public static boolean populate_setup(String setup_, String db_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(arrays.is_ok(vals_) ? vals_ : get_setup_default());
		
		String setup = (strings.is_ok(setup_) ? setup_ : _defaults.DB_SETUP);
		
		vals.put(db.SETUP, setup);
		vals.put(db.NAME, (strings.is_ok(db_) ? db_ : _defaults.DB_NAME));		
		
		return config.update_ini(setup, vals);
	}

	public static boolean populate_source(String main_, String source_, String table_, HashMap<String, Object[]> fields_)
	{
		String main = config.check_type(main_);
		String source = config.check_type(source_);
		if (!strings.is_ok(main) || !strings.is_ok(source) || !strings.is_ok(table_) || !arrays.is_ok(fields_)) return false;

		HashMap<String, Object[]> defaults = get_fields_default();
		if (!arrays.is_ok(defaults)) return false;
		
		HashMap<String, db_field> fields = new HashMap<String, db_field>();		

		int count = 0;
		while (count < 2)
		{
			count++;
			
			for (Entry<String, Object[]> item: (count == 1 ? defaults : fields_).entrySet())
			{
				String id = item.getKey();
				Object[] val = item.getValue();
				
				String col = (String)val[0];
				config.update_ini(main, id, col);
				
				db_field field = (db_field)val[1];
				fields.put(id, field);
			}
		}
	
		return (!db.add_source(source, fields) ? false : (config.update_ini(main, source, table_) && db.add_source_main(source, main)));
	}
	
	public static HashMap<String, Object[]> get_fields(HashMap<String, db_field> info_, String type_field_)
	{		
		HashMap<String, Object[]> fields = new HashMap<String, Object[]>();
		if (!arrays.is_ok(info_)) return fields;
	
		for (Entry<String, db_field> item: info_.entrySet())
		{
			String id = item.getKey();
			fields = add_field(id, get_col_default(id, type_field_), item.getValue(), fields);	
		}
		
		return fields;
	}
	
	public static HashMap<String, Object[]> add_field(String id_, String col_, db_field field_, HashMap<String, Object[]> all_fields_)
	{
		all_fields_.put(id_, new Object[] { col_, field_ });
		
		return all_fields_;
	}

	public static String get_col_default(String field_, String type_)
	{
		return types.remove_type(field_, type_);
	}
	
	private static boolean populate_all_setups()
	{
		boolean is_ok = true;
		
		is_ok = populate_setup(null, null, null); //Default setup & default DB.
	
		return is_ok;
	}
	
	private static boolean populate_all_sources()
	{	
		boolean is_ok = true;
	
		String main = types.CONFIG_TESTS; 
		String source = types.CONFIG_TESTS_DB_SOURCE;
		String table = "tests";
		HashMap<String, Object[]> fields = get_fields(get_fields_tests(), types.CONFIG_TESTS_DB_FIELD);
		
		is_ok = populate_source(main, source, table, fields);

		return is_ok;
	}
	
	private static HashMap<String, db_field> get_fields_tests()
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_TESTS_DB_FIELD_INT, new db_field(data.INT));
		info.put(types.CONFIG_TESTS_DB_FIELD_STRING, new db_field(data.STRING, strings.DEFAULT_SIZE, 0));
		info.put(types.CONFIG_TESTS_DB_FIELD_DECIMAL, new db_field(data.DECIMAL, 15, 3));

		return info;
	}

	private static HashMap<String, Object[]> get_fields_default()
	{
		HashMap<String, Object[]> fields = new HashMap<String, Object[]>();

		fields = add_field(types.CONFIG_DB_DEFAULT_FIELD_ID, "_id", new db_field(data.INT, new String[] { db_field.KEY_PRIMARY, db_field.AUTO_INCREMENT }), fields);
		fields = add_field(types.CONFIG_DB_DEFAULT_FIELD_TIMESTAMP, "_timestamp", new db_field(data.TIMESTAMP, new String[] { db_field.TIMESTAMP }), fields);

		return fields;
	}
	
	private static HashMap<String, Object> get_setup_default()
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		vals.put(types.CONFIG_DB_TYPE, _defaults.DB_TYPE);
		vals.put(types.CONFIG_DB_MAX_POOL, _defaults.DB_MAX_POOL);
		vals.put(types.CONFIG_DB_HOST, _defaults.DB_HOST);
		vals.put(types.CONFIG_DB_CREDENTIALS_USER, _defaults.DB_USER);
		vals.put(types.CONFIG_DB_CREDENTIALS_ENCRYPTED, _defaults.DB_CREDENTIALS_ENCRYPTED);
		vals.put(types.CONFIG_DB_CREDENTIALS_USERNAME, _defaults.DB_CREDENTIALS_USERNAME);
		vals.put(types.CONFIG_DB_CREDENTIALS_PASSWORD, _defaults.DB_CREDENTIALS_PASSWORD);

		return vals;
	}
}