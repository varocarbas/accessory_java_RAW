package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_credentials 
{
	public static final String SOURCE = types.CONFIG_CREDENTIALS_DB_SOURCE;

	public static final String ID = types.CONFIG_CREDENTIALS_DB_FIELD_ID;
	public static final String ID_ENC = types.CONFIG_CREDENTIALS_DB_FIELD_ID_ENC;
	public static final String USER = types.CONFIG_CREDENTIALS_DB_FIELD_USER;
	public static final String USERNAME = types.CONFIG_CREDENTIALS_DB_FIELD_USERNAME;
	public static final String PASSWORD = types.CONFIG_CREDENTIALS_DB_FIELD_PASSWORD;
	public static final String IS_ENC = types.CONFIG_CREDENTIALS_DB_FIELD_IS_ENC;

	private static HashMap<String, String> _cols = null;
	
	private static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	public static boolean is_quick() { return _is_quick; }
	
	public static void is_quick(boolean is_quick_) { _is_quick = is_quick_; }
	
	public static String get_field_col(String field_) { return (_is_quick ? get_col(field_) : field_); }

	public static String get_col(String field_) { return (_cols.containsKey(field_) ? _cols.get(field_) : strings.DEFAULT); }

	public static String[] get_fields() { return new String[] { ID, ID_ENC, USER, USERNAME, PASSWORD, IS_ENC }; }

	static void start() { _cols = db.get_cols(SOURCE, get_fields()); }
	
	static boolean update_info(String id_, String user_, HashMap<String, String> vals_)
	{
		String where = get_db_where(id_, user_, true);

		HashMap<String, Object> vals = new HashMap<String, Object>();

		vals.put(USERNAME, vals_.get(credentials.USERNAME));
		vals.put(PASSWORD, vals_.get(credentials.PASSWORD));
		vals.put(ID, id_);
		vals.put(ID_ENC, credentials.get_encryption_id(id_, user_));
		vals.put(USER, user_);
		vals.put(IS_ENC, true);
		
		if (_is_quick)
		{
			HashMap<String, String> vals2 = new HashMap<String, String>();
			
			for (Entry<String, Object> val: vals.entrySet()) { vals2.put(get_col(val.getKey()), db.adapt_input(val.getValue())); }

			db_quick.insert_update(SOURCE, ID, vals2, where);
		}
		else db.insert_update(SOURCE, vals, where);

		return db.is_ok(SOURCE);
	}
	
	static String get_username_password(String id_, String user_, boolean is_encrypted_, boolean is_username_) 
	{ 
		String field_col = get_field_col(is_username_ ? USERNAME : PASSWORD);
		String where = get_db_where(id_, user_, is_encrypted_);
		
		return (_is_quick ? db_quick.select_one_string(SOURCE, field_col, where, db.DEFAULT_ORDER) : db.select_one_string(SOURCE, field_col, where, db.DEFAULT_ORDER)); 
	}
	
	private static String get_db_where(String id_, String user_, boolean is_encrypted_) { return db_where.to_string(new db_where[] { new db_where(SOURCE, ID, id_), new db_where(SOURCE, USER, user_), new db_where(SOURCE, IS_ENC, is_encrypted_) }); }
}