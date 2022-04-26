package accessory;

import java.util.HashMap;

class _ini_db extends parent_ini_db 
{
	private static _ini_db _instance = new _ini_db();
	
	public _ini_db() { }
	
	public static void populate(String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { _instance.populate_all(dbs_user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_); }
	
	@SuppressWarnings("unchecked")
	protected boolean populate_all_dbs(HashMap<String, Object> dbs_setup_)
	{	
		String db = types.CONFIG_TESTS_DB;
		HashMap<String, Object> setup_vals = (HashMap<String, Object>)arrays.get_new(dbs_setup_);

		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_tests(db, sources);
		
		boolean is_ok = populate_db(db, sources, setup_vals);
		
		return is_ok;
	}

	private HashMap<String, Object[]> add_source_tests(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_TESTS_DB_SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_TESTS_DB_FIELD_INT, new db_field(data.INT));
		info.put(types.CONFIG_TESTS_DB_FIELD_STRING, new db_field(data.STRING, strings.DEFAULT_SIZE, 0));
		info.put(types.CONFIG_TESTS_DB_FIELD_DECIMAL, new db_field(data.DECIMAL, 15, 3));

		return add_source(source, db_, info, default_fields, sources_);		
	}
}