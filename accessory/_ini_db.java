package accessory;

import java.util.HashMap;

class _ini_db extends parent_ini_db 
{
	private static _ini_db _instance = new _ini_db();

	public _ini_db() { }

	public static void populate(String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { _instance.populate_all(dbs_user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_); }

	public static void populate(HashMap<String, Object> dbs_setup_) { _instance.populate_all(dbs_setup_); }

	@SuppressWarnings("unchecked")
	protected boolean populate_all_dbs(HashMap<String, Object> dbs_setup_)
	{	
		HashMap<String, Object> setup_vals = (HashMap<String, Object>)arrays.get_new(dbs_setup_);

		String db = (String)arrays.get_value(setup_vals, types.CONFIG_DB);
		if (!strings.is_ok(db)) db = accessory.db.DEFAULT_DB;
		
		String name = (String)arrays.get_value(setup_vals, types.CONFIG_DB_NAME);		
		if (!strings.is_ok(name)) name = accessory.db.DEFAULT_DB_NAME;
	
		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_tests(db, sources);
		sources = add_source_credentials(db, sources);

		boolean is_ok = populate_db(db, name, sources, setup_vals);

		return is_ok;
	}

	private HashMap<String, Object[]> add_source_tests(String db_, HashMap<String, Object[]> sources_)
	{
		String source = tests.SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(tests.FIELD_INT, new db_field(data.INT));
		info.put(tests.FIELD_STRING, new db_field(data.STRING));
		info.put(tests.FIELD_DECIMAL, new db_field(data.DECIMAL, 15, 3));
		info.put(tests.FIELD_BOOLEAN, new db_field(data.BOOLEAN));

		return add_source(source, null, db_, info, default_fields, sources_);		
	}

	private HashMap<String, Object[]> add_source_credentials(String db_, HashMap<String, Object[]> sources_)
	{
		String source = credentials.SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		db_field field_string = new db_field(data.STRING);

		info.put(credentials.FIELD_ID, new db_field(field_string));
		info.put(credentials.FIELD_ID_ENC, new db_field(field_string));
		info.put(credentials.FIELD_USER, new db_field(field_string));
		info.put(credentials.FIELD_USERNAME, new db_field(field_string));
		info.put(credentials.FIELD_PASSWORD, new db_field(field_string));
		info.put(credentials.FIELD_IS_ENC, new db_field(data.BOOLEAN));

		return add_source(source, null, db_, info, default_fields, sources_);		
	}
}