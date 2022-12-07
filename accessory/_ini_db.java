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
		sources = add_source_info(db, sources);
		sources = add_source_crypto(db, sources);
		
		boolean is_ok = populate_db(db, name, sources, setup_vals);

		return is_ok;
	}

	private HashMap<String, Object[]> add_source_tests(String db_, HashMap<String, Object[]> sources_)
	{
		String source = db_tests.SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(db_tests.INT, new db_field(data.INT));
		info.put(db_tests.STRING, new db_field(data.STRING));
		info.put(db_tests.DECIMAL, db_common.get_field_decimal(15));
		info.put(db_tests.BOOLEAN, db_common.get_field_boolean(true));

		return add_source(source, null, db_, info, default_fields, sources_);		
	}

	private HashMap<String, Object[]> add_source_credentials(String db_, HashMap<String, Object[]> sources_)
	{
		String source = db_credentials.SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(db_credentials.ID, db_common.get_field_string(true));
		info.put(db_credentials.ID_ENC, db_common.get_field_string(true));
		info.put(db_credentials.USER, db_common.get_field_string());
		info.put(db_credentials.USERNAME, db_common.get_field_string());
		info.put(db_credentials.PASSWORD, db_common.get_field_string());
		info.put(db_credentials.IS_ENC, db_common.get_field_is_enc());

		return add_source(source, null, db_, info, default_fields, sources_);		
	}

	private HashMap<String, Object[]> add_source_info(String db_, HashMap<String, Object[]> sources_)
	{
		String source = db_info.SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(db_info.KEY, db_common.get_field_string(db_info.MAX_SIZE_KEY, true));
		info.put(db_info.VALUE, db_common.get_field_string(db_info.MAX_SIZE_VALUE));
		info.put(db_info.IS_ENC, db_common.get_field_is_enc());
		
		return add_source(source, null, db_, info, default_fields, sources_);		
	}

	private HashMap<String, Object[]> add_source_crypto(String db_, HashMap<String, Object[]> sources_)
	{
		String source = db_crypto.SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(db_crypto.ID, db_common.get_field_string(db_crypto.MAX_SIZE_ID, true));
		info.put(db_crypto.ALGO, db_common.get_field_string(db_crypto.MAX_SIZE_ALGO));
		info.put(db_crypto.IV, db_common.get_field_string(db_crypto.MAX_SIZE_IV));
		info.put(db_crypto.KEY, db_common.get_field_string_big());

		return add_source(source, null, db_, info, default_fields, sources_);		
	}
}