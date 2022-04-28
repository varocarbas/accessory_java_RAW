package accessory;

import java.util.HashMap;

public abstract class credentials 
{
	public static final String SOURCE = types.CONFIG_CREDENTIALS_DB_SOURCE;
	public static final String FIELD_ID = types.CONFIG_CREDENTIALS_DB_FIELD_ID;
	public static final String FIELD_USER = types.CONFIG_CREDENTIALS_DB_FIELD_USER;
	public static final String FIELD_USERNAME = types.CONFIG_CREDENTIALS_DB_FIELD_USERNAME;
	public static final String FIELD_PASSWORD = types.CONFIG_CREDENTIALS_DB_FIELD_PASSWORD;
	
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
		
		String[] temp = encrypt_username_password_file_get(id, user);

		return (arrays.is_ok(temp) ? encrypt_username_password_file(id, user, temp[0], temp[1]) : false);
	}

	public static boolean encrypt_username_password_file(String id_, String user_, String username_, String password_) { return encrypt_username_password_internal(id_, user_, username_, password_, WHERE_FILE); }

	private static boolean encrypt_username_password_internal(String id_, String user_, String username_, String password_, String where_)
	{
		if (!strings.is_ok(username_) || password_ == null) return false;

		HashMap<String, String> id_user = get_id_user(id_, user_);
		String id = id_user.get(ID);
		String user = id_user.get(USER);

		String encryption_id = get_encryption_id(id, user);
		String[] vals = crypto.encrypt(new String[] { username_, password_ }, encryption_id);
		
		boolean output = false;
		if (!arrays.is_ok(vals)) return output;
		
		if (where_.equals(WHERE_FILE)) output = encrypt_username_password_file_store(id, user, vals);
		
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

	private static boolean encrypt_username_password_file_store(String id_, String user_, String[] outputs_)
	{
		String[] paths = new String[] { get_path_username_password(id_, user_, true, true), get_path_username_password(id_, user_, true, false) };
		
		for (int i = 0; i < paths.length; i++)
		{
			io.line_to_file(paths[i], outputs_[i], false);
			if (!io._is_ok) return false;
		}
		
		return true;
	}
	
	private static String[] encrypt_username_password_file_get(String id_, String user_)
	{
		String[] vals = new String[2];
		
		String[] paths = new String[] { get_path_username_password(id_, user_, false, true), get_path_username_password(id_, user_, false, false) };
		
		for (int i = 0; i < paths.length; i++)
		{
			vals[i] = io.file_to_string(paths[i], true);
			if (!io._is_ok) return null;
		}

		return vals;
	}
	
	private static String get_username_password(String id_, String user_, boolean encrypted_, String where_, boolean is_username_)
	{
		String output = strings.DEFAULT;

		if (where_.equals(WHERE_FILE)) output = get_username_password_file(id_, user_, encrypted_, is_username_);

		return output;
	}

	private static String get_username_password_file(String id_, String user_, boolean encrypted_, boolean is_username_)
	{   
		String path = get_path_username_password(id_, user_, encrypted_, is_username_);

		String val = io.file_to_string(path, true);

		return (io._is_ok ? val : strings.DEFAULT);
	}

	private static String get_path_username_password(String id_, String user_, boolean encrypted_, boolean is_username_)
	{   
		String file = id_ + SEPARATOR + user_;

		file += SEPARATOR + 
		(
			is_username_ ? config.get_credentials(types.CONFIG_CREDENTIALS_FILE_USERNAME) : 
			config.get_credentials(types.CONFIG_CREDENTIALS_FILE_PASSWORD)
		);
		if (encrypted_) file += SEPARATOR + config.get_credentials(types.CONFIG_CREDENTIALS_FILE_ENCRYPTED); 

		return paths.build(new String[] { paths.get_dir(paths.DIR_CREDENTIALS), get_file_full(file) }, true);
	}
	
	private static String check_where(String where_)
	{
		String where = types.check_type(where_, types.get_subtypes(WHERE));

		return (strings.is_ok(where) ? where : DEFAULT_WHERE);
	}
}