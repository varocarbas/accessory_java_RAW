package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class credentials 
{
	public static final String SOURCE = types.CONFIG_CREDENTIALS_DB_SOURCE;
	public static final String FIELD_ID = types.CONFIG_CREDENTIALS_DB_FIELD_ID;
	public static final String FIELD_ID_ENC = types.CONFIG_CREDENTIALS_DB_FIELD_ID_ENC;
	public static final String FIELD_USER = types.CONFIG_CREDENTIALS_DB_FIELD_USER;
	public static final String FIELD_USERNAME = types.CONFIG_CREDENTIALS_DB_FIELD_USERNAME;
	public static final String FIELD_PASSWORD = types.CONFIG_CREDENTIALS_DB_FIELD_PASSWORD;
	public static final String FIELD_IS_ENC = types.CONFIG_CREDENTIALS_DB_FIELD_IS_ENC;

	public static final String WHERE = types.CONFIG_CREDENTIALS_WHERE;
	public static final String WHERE_FILE = types.CONFIG_CREDENTIALS_WHERE_FILE;
	public static final String WHERE_DB = types.CONFIG_CREDENTIALS_WHERE_DB;
	public static final String ID = generic.ID;
	public static final String USER = generic.USER;
	public static final String USERNAME = generic.USERNAME;
	public static final String PASSWORD = generic.PASSWORD;
	
	public static final String DEFAULT_ID = _defaults.CREDENTIALS_ID;
	public static final String DEFAULT_USER = _defaults.USER;
	public static final String DEFAULT_WHERE = _defaults.CREDENTIALS_WHERE;

	private static final String SEPARATOR = misc.SEPARATOR_NAME;
	
	static { _ini.start(); }
	public static final String _ID = types.get_id(types.ID_CREDENTIALS);
	
	public static HashMap<String, String> get_username_password(String id_, String user_, boolean encrypted_, String where_)
	{
		HashMap<String, String> id_user = get_id_user(id_, user_);
		String id = id_user.get(ID);
		String user = id_user.get(USER);

		String where = check_where(where_);
		
		String username = get_username_password(id, user, encrypted_, where, true);
		String password = get_username_password(id, user, encrypted_, where, false);

		if (encrypted_)
		{
			String id2 = get_encryption_id(id, user);
			
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
		
	public static boolean encrypt_username_password_file(String id_, String user_)
	{
		HashMap<String, String> id_user = get_id_user(id_, user_);
		String id = id_user.get(ID);
		String user = id_user.get(USER);
		
		HashMap<String, String> temp = encrypt_username_password_file_get(id, user);

		return (arrays.is_ok(temp) ? encrypt_username_password_file(id, user, temp.get(USERNAME), temp.get(PASSWORD)) : false);
	}

	public static boolean encrypt_username_password_file(String id_, String user_, String username_, String password_) { return encrypt_username_password_internal(id_, user_, username_, password_, WHERE_FILE); }

	private static boolean encrypt_username_password_internal(String id_, String user_, String username_, String password_, String where_)
	{
		if (!strings.is_ok(username_) || password_ == null) return false;

		HashMap<String, String> id_user = get_id_user(id_, user_);
		String id = id_user.get(ID);
		String user = id_user.get(USER);

		String encryption_id = get_encryption_id(id, user);
		String[] temp = crypto.encrypt(new String[] { username_, password_ }, encryption_id);
		
		boolean output = false;
		if (!arrays.is_ok(temp)) return output;
		
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(USERNAME, temp[0]);
		vals.put(PASSWORD, temp[1]);
		
		if (where_.equals(WHERE_FILE)) output = encrypt_username_password_file_store(id, user, vals);
		else if (where_.equals(WHERE_DB)) output = encrypt_username_password_db_store(id, user, vals);
		
		return output;
	}

	public static String get_extension() { return config.get_credentials(types.CONFIG_CREDENTIALS_FILE_EXTENSION); }
	
	public static String get_file_full(String id_) { return paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID), get_extension()); }
	
	public static String get_encryption_id(String id_, String user_) { return (strings.are_ok(new String[] { id_, user_ }) ? (id_ + SEPARATOR + user_) : strings.DEFAULT); }
	
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
			if (!io._is_ok) return false;
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
		
	private static boolean encrypt_username_password_db_store(String id_, String user_, HashMap<String, String> outputs_)
	{
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(FIELD_USERNAME, outputs_.get(USERNAME));
		vals.put(FIELD_PASSWORD, outputs_.get(PASSWORD));
		
		db.insert_update(SOURCE, vals, get_db_where(id_, user_));

		return db.is_ok(SOURCE);
	}
	
	private static HashMap<String, String> encrypt_username_password_file_get(String id_, String user_)
	{
		HashMap<String, String> outputs = new HashMap<String, String>();

		for (Entry<String, String> item: encrypt_username_password_file_paths(id_, user_).entrySet())
		{
			String key = item.getKey();
			String path = item.getValue();
			
			outputs.put(key, io.file_to_string(path, true));
			if (!io._is_ok) return null;
		}
		
		return outputs;
	}
	
	private static String get_username_password(String id_, String user_, boolean encrypted_, String where_, boolean is_username_)
	{
		String output = strings.DEFAULT;

		if (where_.equals(WHERE_DB)) output = get_username_password_db(id_, user_, is_username_);
		else if (where_.equals(WHERE_FILE)) output = get_username_password_file(id_, user_, encrypted_, is_username_);

		return output;
	}
	
	private static String get_username_password_db(String id_, String user_, boolean is_username_) { return db.select_one_string(SOURCE, (is_username_ ? FIELD_USERNAME : FIELD_PASSWORD), get_db_where(id_, user_), null); }
	
	private static db_where[] get_db_where(String id_, String user_) { return new db_where[] { new db_where(SOURCE, FIELD_ID, id_), new db_where(SOURCE, FIELD_USER, user_) }; }
	
	private static String get_username_password_file(String id_, String user_, boolean encrypted_, boolean is_username_)
	{   
		String path = get_path_username_password(id_, user_, encrypted_, is_username_);

		String val = io.file_to_string(path, true);

		return (io._is_ok ? val : strings.DEFAULT);
	}

	private static String get_path_username_password(String id_, String user_, boolean encrypted_, boolean is_username_)
	{   
		String file = id_ + SEPARATOR + user_ + SEPARATOR;

		file += (is_username_ ? config.get_credentials(types.CONFIG_CREDENTIALS_FILE_USERNAME) : config.get_credentials(types.CONFIG_CREDENTIALS_FILE_PASSWORD));
		if (encrypted_) file += SEPARATOR + config.get_credentials(types.CONFIG_CREDENTIALS_FILE_ENCRYPTED); 

		return paths.build(new String[] { paths.get_dir(paths.DIR_CREDENTIALS), get_file_full(file) }, true);
	}
	
	private static String check_where(String where_)
	{
		String where = types.check_type(where_, types.get_subtypes(WHERE));

		return (strings.is_ok(where) ? where : DEFAULT_WHERE);
	}
}