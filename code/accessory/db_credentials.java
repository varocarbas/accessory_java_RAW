package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_credentials
{
	public static final String SOURCE = _types.CONFIG_CREDENTIALS_DB_SOURCE;

	public static final String ID = _types.CONFIG_CREDENTIALS_DB_FIELD_ID;
	public static final String ID_ENC = _types.CONFIG_CREDENTIALS_DB_FIELD_ID_ENC;
	public static final String USER = _types.CONFIG_CREDENTIALS_DB_FIELD_USER;
	public static final String USERNAME = _types.CONFIG_CREDENTIALS_DB_FIELD_USERNAME;
	public static final String PASSWORD = _types.CONFIG_CREDENTIALS_DB_FIELD_PASSWORD;
	public static final String IS_ENC = _types.CONFIG_CREDENTIALS_DB_FIELD_IS_ENC;
	
	public static boolean is_quick() { return db_common.is_quick(SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(SOURCE, is_quick_); }
	
	static boolean update_info(String id_, String user_, HashMap<String, String> vals_)
	{
		boolean output = false;
		
		String where = get_db_where(id_, user_, true);

		HashMap<String, Object> vals = new HashMap<String, Object>();

		vals.put(USERNAME, vals_.get(credentials.USERNAME));
		vals.put(PASSWORD, vals_.get(credentials.PASSWORD));
		vals.put(ID, id_);
		vals.put(ID_ENC, credentials.get_encryption_id(id_, user_));
		vals.put(USER, user_);
		vals.put(IS_ENC, true);
		
		if (is_quick())
		{
			HashMap<String, String> vals2 = new HashMap<String, String>();
			
			for (Entry<String, Object> val: vals.entrySet()) { vals2.put(db_quick.get_col(SOURCE, val.getKey()), db.adapt_input(val.getValue())); }

			db_quick.insert_update(SOURCE, db_quick.get_col(SOURCE, ID), vals2, where);
			
			output = db_quick.is_ok(SOURCE);
		}
		else 
		{
			db.insert_update(SOURCE, vals, where);

			output = db.is_ok(SOURCE);
		}
		
		return output;
	}
	
	static String get_username_password(String id_, String user_, boolean is_encrypted_, boolean is_username_) 
	{ 
		String field_col = db_common.get_field_quick_col(SOURCE, (is_username_ ? USERNAME : PASSWORD));
		String where = get_db_where(id_, user_, is_encrypted_);
		
		return (is_quick() ? db_quick.select_one_string(SOURCE, field_col, where, db.DEFAULT_ORDER) : db.select_one_string(SOURCE, field_col, where, db.DEFAULT_ORDER)); 
	}
	
	private static String get_db_where(String id_, String user_, boolean is_encrypted_) { return db_where.to_string(new db_where[] { new db_where(SOURCE, ID, id_), new db_where(SOURCE, USER, user_), new db_where(SOURCE, IS_ENC, is_encrypted_) }); }
}