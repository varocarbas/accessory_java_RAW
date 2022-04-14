package accessory;

import java.util.HashMap;

class _ini_db extends parent_ini_db 
{
	private static _ini_db _instance = new _ini_db();
	
	public _ini_db() { }
	
	public static void populate() { populate_internal(_instance); }
	
	protected boolean populate_all_dbs()
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

	private HashMap<String, db_field> get_fields_tests()
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_TESTS_DB_FIELD_INT, new db_field(data.INT));
		info.put(types.CONFIG_TESTS_DB_FIELD_STRING, new db_field(data.STRING, strings.DEFAULT_SIZE, 0));
		info.put(types.CONFIG_TESTS_DB_FIELD_DECIMAL, new db_field(data.DECIMAL, 15, 3));

		return info;
	}
}