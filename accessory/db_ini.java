package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_ini 
{
	private static boolean _populated = false;
	
	public static void populate() 
	{
		if (_populated) return;
		
		String error = strings.DEFAULT;
		if (!populate_all_dbs()) error = types.ERROR_INI_DB_DBS;
		
		_populated = true;
		if (error.equals(strings.DEFAULT)) return;

		ini.manage_error(error);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean populate_db(String db_, String name_, HashMap<String, Object[]> sources_, HashMap<String, Object> setup_vals_)
	{
		boolean is_ok = true;

		String db = config.check_type(db_);
		if (!strings.is_ok(db) || !arrays.is_ok(sources_)) return false;

		config.update_ini(db_, types.CONFIG_DB_NAME, name_);		
		HashMap<String, Object> setup_vals = get_setup_vals(db, setup_vals_);
		
		for (Entry<String, Object[]> item: sources_.entrySet())
		{
			String id = item.getKey();
			Object[] temp = item.getValue();
			
			if (!populate_source(id, (String)temp[0], (HashMap<String, Object[]>)temp[1], setup_vals))
			{
				ini.manage_error(types.ERROR_INI_DB_SOURCE);
				
				return false;
			}
		}
		
		return is_ok;
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

	public static HashMap<String, Object[]> add_source(String id_, String table_, HashMap<String, Object[]> fields_, HashMap<String, Object[]> all_sources_)
	{
		all_sources_.put(id_, new Object[] { table_, fields_ });
		
		return all_sources_;
	}
	
	public static HashMap<String, Object[]> add_field(String id_, String col_, db_field field_, HashMap<String, Object[]> all_fields_)
	{
		all_fields_.put(id_, new Object[] { col_, field_ });
		
		return all_fields_;
	}

	public static boolean setup_vals_are_ok(HashMap<String, Object> setup_vals_)
	{
		String instance = ini.get_generic_key(types.WHAT_INSTANCE);
		if (!arrays.keys_exist(setup_vals_, new String[] { types.CONFIG_DB, types.CONFIG_DB_SETUP, instance })) return false;
		if (!strings.are_ok(new String[] { (String)setup_vals_.get(types.CONFIG_DB), (String)setup_vals_.get(types.CONFIG_DB_SETUP) })) return false;
		
		return true;
	}
	
	private static String get_col_default(String field_, String type_) { return types.remove_type(field_, type_); }

	private static boolean populate_source(String source_, String table_, HashMap<String, Object[]> fields_, HashMap<String, Object> setup_vals_)
	{
		if (!setup_vals_are_ok(setup_vals_)) return false;

		String db = config.check_type((String)setup_vals_.get(types.CONFIG_DB));
		String source = config.check_type(source_);
		if (!strings.is_ok(db) || !strings.is_ok(source) || !strings.is_ok(table_) || !arrays.is_ok(fields_)) return false;

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
				config.update_ini(db, id, col);
				
				db_field field = (db_field)val[1];
				fields.put(id, field);
			}
		}
		
		if (!accessory.db.add_source_ini(source, fields, setup_vals_)) return false;
		if (!config.update_ini(db, source, table_)) return false;
		
		return true;
	}
	
	private static HashMap<String, Object> get_setup_vals(String db_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(arrays.is_ok(vals_) ? vals_ : get_setup_default());
		
		if (!vals.containsKey(types.CONFIG_DB)) vals.put(types.CONFIG_DB, db_);
		if (!vals.containsKey(types.CONFIG_DB_SETUP)) vals.put(types.CONFIG_DB_SETUP, db_);
		if (!vals.containsKey(types.CONFIG_DB_SETUP_TYPE)) vals.put(types.CONFIG_DB_SETUP_TYPE, _defaults.DB_TYPE);
		
		vals = ini.get_config_default_generic(types.CONFIG_DB_SETUP_CREDENTIALS, vals);

		return (arrays.value_exists(config.update_ini((String)vals.get(types.CONFIG_DB_SETUP), vals), false) ? null : vals);
	}

	private static boolean populate_all_dbs()
	{	
		boolean is_ok = true;
		
		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		
		String db = types.CONFIG_TESTS_DB;
		String name = _defaults.STRINGS;
		HashMap<String, Object> setup_vals = null;
		
		String source = types.CONFIG_TESTS_DB_SOURCE;
		String table = "tests";
		HashMap<String, Object[]> fields = get_fields(get_fields_tests(), types.CONFIG_TESTS_DB_FIELD);
		sources = add_source(source, table, fields, sources);
		
		is_ok = populate_db(db, name, sources, setup_vals);
		
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

		vals.put(types.CONFIG_DB_SETUP, _defaults.DB_SETUP);
		vals.put(types.CONFIG_DB_SETUP_TYPE, _defaults.DB_TYPE);
		vals.put(types.CONFIG_DB_SETUP_MAX_POOL, _defaults.DB_MAX_POOL);
		vals.put(types.CONFIG_DB_SETUP_HOST, _defaults.DB_HOST);

		return vals;
	}
}