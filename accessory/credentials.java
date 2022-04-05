package accessory;

import java.util.HashMap;

public abstract class credentials 
{   
	public static final String WHERE = types.CONFIG_CREDENTIALS_WHERE;
	public static final String ENCRYPTED = types.CONFIG_CREDENTIALS_ENCRYPTED;	

	public static final String DEFAULT_ID = _defaults.CREDENTIALS_ID;
	public static final String DEFAULT_USER = _defaults.CREDENTIALS_USER;
	public static final String DEFAULT_WHERE = _defaults.CREDENTIALS_WHERE;
	
	static { ini.load(); }

	public static HashMap<String, String> get(String id_, String user_, boolean encrypted_, String where_)
	{
		String id = (strings.is_ok(id_) ? id_ : DEFAULT_ID);
		String user = (strings.is_ok(user_) ? user_ : DEFAULT_USER);

		String where = check_where(where_);
		
		String username = get_username_password(id, user, encrypted_, where, true);
		String password = get_username_password(id, user, encrypted_, where, false);

		if (encrypted_)
		{
			//username = crypto.decrypt(username, id_);
			//password = crypto.decrypt(password, id_);
		}

		return get(username, password);
	}

	public static HashMap<String, String> get(String username_, String password_)
	{
		HashMap<String, String> credentials = new HashMap<String, String>();
		if (!strings.is_ok(username_) || password_ == null) return credentials;
		
		credentials.put(generic.USERNAME, username_);
		credentials.put(generic.PASSWORD, password_);

		return credentials;
	}
	
	public static boolean encrypt_files(String id_, String user_)
	{
		String id = (strings.is_ok(id_) ? id_ : DEFAULT_ID);
		String user = (strings.is_ok(user_) ? user_ : DEFAULT_USER);

		String[] vals = encrypt_files_get_vals(id, user);
		if (!arrays.is_ok(vals)) return false;
		
		String[] outputs = new String[2];
		
		for (int i = 0; i < vals.length; i++)
		{
			outputs[i] = crypto.encrypt(vals[i], id);
			if (!strings.is_ok(outputs[i])) return false;
		}
				
		//return encrypt_files_store_vals(id, user, outputs);
		
		return false;
	}
	
	public static String get_extension()
	{
		return config.get_credentials(types.CONFIG_CREDENTIALS_FILE_EXTENSION);
	}
	
	public static String get_file_full(String id_)
	{
		return paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID), get_extension());
	}
	
	private static boolean encrypt_files_store_vals(String id_, String user_, String[] outputs_)
	{
		String[] paths = new String[] 
		{ 
			get_path_username_password(id_, user_, true, true),
			get_path_username_password(id_, user_, true, false)
		};
		
		for (int i = 0; i < paths.length; i++)
		{
			io.line_to_file(paths[i], outputs_[i], false, true);
			if (!io._is_ok) return false;
		}
		
		return true;
	}
	
	private static String[] encrypt_files_get_vals(String id_, String user_)
	{
		String[] vals = new String[2];
		
		String[] paths = new String[] 
		{ 
			get_path_username_password(id_, user_, false, true),
			get_path_username_password(id_, user_, false, false)
		};
		
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

		String where = (strings.is_ok(where_) ? where_ : DEFAULT_WHERE);

		if (strings.are_equivalent(where, types.CONFIG_CREDENTIALS_WHERE_FILE)) 
		{
			output = get_username_password_file(id_, user_, encrypted_, is_username_);
		}

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
		String separator = config.get_credentials(types.CONFIG_CREDENTIALS_FILE_SEPARATOR);

		String file = id_ + separator + user_;

		file += separator + 
		(
			is_username_ ? config.get_credentials(types.CONFIG_CREDENTIALS_FILE_USERNAME) : 
			config.get_credentials(types.CONFIG_CREDENTIALS_FILE_PASSWORD)
		);
		if (encrypted_) file += separator + config.get_credentials(types.CONFIG_CREDENTIALS_FILE_ENCRYPTED); 

		return paths.build(new String[] { paths.get_dir(paths.DIR_CREDENTIALS), get_file_full(file) }, true);
	}
	
	private static String check_where(String where_)
	{
		return types.check_subtype(where_, types.get_subtypes(WHERE, null), null, null);
	}
}