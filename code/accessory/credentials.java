package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class credentials extends parent_static
{
	public static final String CONFIG_WHERE = _types.CONFIG_CREDENTIALS_WHERE;
	public static final String CONFIG_FILE_EXTENSION = _types.CONFIG_CREDENTIALS_FILE_EXTENSION;
	public static final String CONFIG_FILE_USERNAME = _types.CONFIG_CREDENTIALS_FILE_USERNAME;
	public static final String CONFIG_FILE_PASSWORD = _types.CONFIG_CREDENTIALS_FILE_PASSWORD;
	public static final String CONFIG_FILE_ENCRYPTED = _types.CONFIG_CREDENTIALS_FILE_ENCRYPTED;
	
	public static final String WHERE = CONFIG_WHERE;
	public static final String WHERE_FILE = _types.CONFIG_CREDENTIALS_WHERE_FILE;
	public static final String WHERE_DB = _types.CONFIG_CREDENTIALS_WHERE_DB;
	public static final String ID = _keys.ID;
	public static final String USER = _keys.USER;
	public static final String USERNAME = _keys.USERNAME;
	public static final String PASSWORD = _keys.PASSWORD;
	public static final int USERNAME_I = 0;
	public static final int PASSWORD_I = 1;
	
	public static final String DEFAULT_ID = "credentials";
	public static final String DEFAULT_USER = _defaults.USER;
	public static final String DEFAULT_WHERE = WHERE_FILE;
	public static final String DEFAULT_FILE_EXTENSION = strings.DEFAULT;
	public static final String DEFAULT_FILE_USERNAME = "username";
	public static final String DEFAULT_FILE_PASSWORD = "password";
	public static final String DEFAULT_FILE_ENCRYPTED = "enc";
	
	private static final String SEPARATOR = misc.SEPARATOR_NAME;

	public static String get_path(String id_, String user_, boolean is_encrypted_) { return get_path(id_, user_, is_encrypted_, null); }
	
	public static String get_username(HashMap<String, String> credentials_) { return ((credentials_ != null && credentials_.containsKey(USERNAME)) ? credentials_.get(USERNAME) : strings.DEFAULT); }

	public static String get_username(String[] credentials_) { return ((credentials_ != null && credentials_.length > USERNAME_I) ? credentials_[USERNAME_I] : strings.DEFAULT); }
	
	public static String get_password(HashMap<String, String> credentials_) { return ((credentials_ != null && credentials_.containsKey(PASSWORD)) ? credentials_.get(PASSWORD) : strings.DEFAULT); }

	public static String get_password(String[] credentials_) { return ((credentials_ != null && credentials_.length > PASSWORD_I) ? credentials_[PASSWORD_I] : strings.DEFAULT); }
	
	public static String encrypt_string(String id_, String user_, String plain_) { return encrypt_decrypt_string(id_, user_, plain_, true); }
	
	public static String decrypt_string(String id_, String user_, String encrypted_) { return encrypt_decrypt_string(id_, user_, encrypted_, false); }
	
	public static boolean encrypt_string_to_file(String id_, String user_, String plain_) 
	{ 
		HashMap<String, String> id_user = get_id_user(id_, user_);
		
		String id = id_user.get(ID);
		String user = id_user.get(USER);
		
		String encrypted = encrypt_string(id, user, plain_);
		if (!strings.is_ok(encrypted)) return false;

		io.line_to_file(get_path(id, user, true), encrypted, false);
		
		return io.is_ok();
	}
	
	public static String get_string_from_file(String id_, String user_, boolean is_encrypted_) 
	{ 
		HashMap<String, String> id_user = get_id_user(id_, user_);

		String id = id_user.get(ID);
		String user = id_user.get(USER);
		
		String string = io.file_to_string(get_path(id, user, is_encrypted_), true);
		if (!io.is_ok() || !strings.is_ok(string)) return strings.DEFAULT;

		return (is_encrypted_ ? decrypt_string(id, user, string) : string);
	}
	
	public static HashMap<String, String> get_username_password_file(String id_, String user_, boolean is_encrypted_) { return get_username_password(id_, user_, is_encrypted_, WHERE_FILE); }

	public static HashMap<String, String> get_username_password_db(String id_, String user_, boolean is_encrypted_) { return get_username_password(id_, user_, is_encrypted_, WHERE_DB); }

	public static HashMap<String, String> get_username_password(String id_, String user_, boolean is_encrypted_, String where_)
	{
		String where = check_where(where_);
		if (!strings.is_ok(where)) return null;

		HashMap<String, String> id_user = get_id_user(id_, user_);
		String id = id_user.get(ID);
		String user = id_user.get(USER);

		String username = get_username_password(id, user, is_encrypted_, where, true);
		String password = get_username_password(id, user, is_encrypted_, where, false);

		if (is_encrypted_)
		{
			String id2 = get_encryption_id(id_user);

			username = crypto.decrypt(username, id2);
			password = crypto.decrypt(password, id2);
		}

		return get_username_password(username, password);
	}

	public static HashMap<String, String> get_username_password(String username_, String password_)
	{
		HashMap<String, String> credentials = new HashMap<String, String>();
		if (!strings.is_ok(username_) || password_ == null) return credentials;

		credentials.put(USERNAME, username_);
		credentials.put(PASSWORD, password_);

		return credentials;
	}

	public static boolean encrypt_username_password(String id_, String user_, String username_, String password_, String where_)
	{
		boolean output = false;

		String where = check_where(where_);
		if (!strings.is_ok(where)) return output;

		if (where.equals(WHERE_FILE)) output = encrypt_username_password_file(id_, user_, username_, password_);
		else if (where.equals(WHERE_DB)) encrypt_username_password_db(id_, user_, username_, password_);

		return output;
	}

	public static boolean encrypt_username_password_file(String id_, String user_, String username_, String password_) { return encrypt_username_password_internal(id_, user_, username_, password_, WHERE_FILE); }

	public static boolean encrypt_username_password_db(String id_, String user_, String username_, String password_) { return encrypt_username_password_internal(id_, user_, username_, password_, WHERE_DB); }

	public static String get_extension() { return (String)config.get_credentials(_types.CONFIG_CREDENTIALS_FILE_EXTENSION); }

	public static String get_file_full(String id_) { return paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID), get_extension()); }

	public static String get_encryption_id(String id_, String user_) { return (strings.are_ok(new String[] { id_, user_ }) ? (id_ + SEPARATOR + user_) : strings.DEFAULT); }
	
	private static String encrypt_decrypt_string(String id_, String user_, String input_, boolean is_encrypt_) 
	{
		String id = get_encryption_id(get_id_user(id_, user_));
		if (!strings.is_ok(id) || !strings.is_ok(input_)) return strings.DEFAULT;
		
		return (is_encrypt_ ? crypto.encrypt(input_, id) : crypto.decrypt(input_, id));
	}

	private static String get_encryption_id(HashMap<String, String> id_user_) { return (arrays.keys_exist(id_user_, new String[] { ID, USER}) ? get_encryption_id(id_user_.get(ID), id_user_.get(USER)) : strings.DEFAULT) ; }

	private static boolean encrypt_username_password_internal(String id_, String user_, String username_, String password_, String where_)
	{
		boolean output = false;
		if (!strings.is_ok(username_) || password_ == null || (where_.equals(WHERE_DB) && !db.table_exists(db_credentials.SOURCE))) return output;

		HashMap<String, String> id_user = get_id_user(id_, user_);
		String id = id_user.get(ID);
		String user = id_user.get(USER);

		String encryption_id = get_encryption_id(id, user);
		String[] temp = crypto.encrypt(new String[] { username_, password_ }, encryption_id);		
		if (!arrays.is_ok(temp)) return output;

		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(USERNAME, temp[0]);
		vals.put(PASSWORD, temp[1]);

		if (where_.equals(WHERE_FILE)) output = encrypt_username_password_file_store(id, user, vals);
		else if (where_.equals(WHERE_DB)) output = encrypt_username_password_db_store(id, user, vals);

		return output;
	}

	private static HashMap<String, String> get_id_user(String id_, String user_)
	{
		HashMap<String, String> output = new HashMap<String, String>();

		output.put(ID, (strings.is_ok(id_) ? id_ : DEFAULT_ID));
		output.put(USER, (strings.is_ok(user_) ? user_ : DEFAULT_USER));

		return output;
	}

	private static boolean encrypt_username_password_file_store(String id_, String user_, HashMap<String, String> outputs_)
	{
		for (Entry<String, String> item: encrypt_username_password_file_paths(id_, user_).entrySet())
		{
			String key = item.getKey();
			String path = item.getValue();

			io.line_to_file(path, outputs_.get(key), false);
			if (!io.is_ok()) return false;
		}

		return true;
	}

	private static HashMap<String, String> encrypt_username_password_file_paths(String id_, String user_)
	{
		HashMap<String, String> outputs = new HashMap<String, String>();

		outputs.put(USERNAME, get_path_username_password(id_, user_, true, true));
		outputs.put(PASSWORD, get_path_username_password(id_, user_, true, false));

		return outputs;
	}

	private static boolean encrypt_username_password_db_store(String id_, String user_, HashMap<String, String> vals_) { return db_credentials.update_info(id_, user_, vals_); }

	private static String get_username_password(String id_, String user_, boolean is_encrypted_, String where_, boolean is_username_)
	{
		String output = null;

		if (where_.equals(WHERE_DB)) 
		{
			if (db.table_exists(db_credentials.SOURCE)) output = get_username_password_db_internal(id_, user_, is_encrypted_, is_username_);
		}
		else if (where_.equals(WHERE_FILE)) output = get_username_password_file_internal(id_, user_, is_encrypted_, is_username_);

		return output;
	}

	private static String get_username_password_db_internal(String id_, String user_, boolean is_encrypted_, boolean is_username_) { return db_credentials.get_username_password(id_, user_, is_encrypted_, is_username_); }

	private static String get_username_password_file_internal(String id_, String user_, boolean is_encrypted_, boolean is_username_)
	{   
		String path = get_path_username_password(id_, user_, is_encrypted_, is_username_);

		String val = io.file_to_string(path, true);

		return (io.is_ok() ? val : strings.DEFAULT);
	}
	
	private static String get_path_username_password(String id_, String user_, boolean is_encrypted_, boolean is_username_) { return get_path(id_, user_, is_encrypted_, (String)(is_username_ ? config.get_credentials(_types.CONFIG_CREDENTIALS_FILE_USERNAME) : config.get_credentials(_types.CONFIG_CREDENTIALS_FILE_PASSWORD))); }

	private static String get_path(String id_, String user_, boolean is_encrypted_, String further_)
	{
		String file = id_ + SEPARATOR + user_;

		if (strings.is_ok(further_)) file += SEPARATOR + further_;
		if (is_encrypted_) file += SEPARATOR + config.get_credentials(_types.CONFIG_CREDENTIALS_FILE_ENCRYPTED); 

		return paths.build(new String[] { paths.get_dir(paths.DIR_CREDENTIALS), get_file_full(file) }, true);
	}

	private static String check_where(String where_)
	{
		String where = _types.check_type(where_, WHERE);

		return (strings.is_ok(where) ? where : DEFAULT_WHERE);
	}
}