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
		String db = _defaults.DB;
		String name = _defaults.DB_NAME;
		HashMap<String, Object> setup_vals = (HashMap<String, Object>)arrays.get_new(dbs_setup_);

		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_tests(db, sources);
		sources = add_source_credentials(db, sources);

		boolean is_ok = populate_db(db, name, sources, setup_vals);

		return is_ok;
	}

	private HashMap<String, Object[]> add_source_tests(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_TESTS_DB_SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(types.CONFIG_TESTS_DB_FIELD_INT, new db_field(types.DATA_INT));
		info.put(types.CONFIG_TESTS_DB_FIELD_STRING, new db_field(types.DATA_STRING_SMALL));
		info.put(types.CONFIG_TESTS_DB_FIELD_DECIMAL, new db_field(types.DATA_DECIMAL, 15, 3));
		info.put(types.CONFIG_TESTS_DB_FIELD_BOOLEAN, new db_field(types.DATA_BOOLEAN));

		return add_source(source, null, db_, info, default_fields, sources_);		
	}

	private HashMap<String, Object[]> add_source_credentials(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_CREDENTIALS_DB_SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		db_field field_string = new db_field(types.DATA_STRING_SMALL);

		info.put(types.CONFIG_CREDENTIALS_DB_FIELD_ID, new db_field(field_string));
		info.put(types.CONFIG_CREDENTIALS_DB_FIELD_ID_ENC, new db_field(field_string));
		info.put(types.CONFIG_CREDENTIALS_DB_FIELD_USER, new db_field(field_string));
		info.put(types.CONFIG_CREDENTIALS_DB_FIELD_USERNAME, new db_field(field_string));
		info.put(types.CONFIG_CREDENTIALS_DB_FIELD_PASSWORD, new db_field(field_string));
		info.put(types.CONFIG_CREDENTIALS_DB_FIELD_IS_ENC, new db_field(types.DATA_BOOLEAN));

		return add_source(source, null, db_, info, default_fields, sources_);		
	}
}