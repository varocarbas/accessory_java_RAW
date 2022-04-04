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
		HashMap<String, String> credentials = null;

		String id = (strings.is_ok(id_) ? id_ : DEFAULT_ID);
		String user = (strings.is_ok(user_) ? user_ : DEFAULT_USER);

		String where = check_where(where_);

		String username = get_username_password(id, user, encrypted_, where, true);
		String password = get_username_password(id, user, encrypted_, where, false);
		if (!strings.are_ok(new String[] { username, password })) return credentials;

		credentials = new HashMap<String, String>();
		credentials.put(generic.USERNAME, username);
		credentials.put(generic.PASSWORD, password);

		return credentials;
	}

	public static String get_extension()
	{
		return config.get_credentials(types.CONFIG_CREDENTIALS_FILE_EXTENSION);
	}
	
	public static String get_file_full(String id_)
	{
		return paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID), get_extension());
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

		String[] lines = io.file_to_array(path, true);
		if (!arrays.is_ok(lines) || !strings.is_ok(lines[0])) return strings.DEFAULT;

		String output = lines[0];     	 
		//if (encrypted_) output = crypto.decrypt(output, id_);

		return output;
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
		if (encrypted_) file += separator + config.get_credentials(ENCRYPTED); 

		return paths.build(new String[] { paths.get_dir(paths.DIR_CREDENTIALS), get_file_full(file) }, true);
	}
	
	private static String check_where(String where_)
	{
		return types.check_subtype(where_, types.get_subtypes(WHERE, null), null, null);
	}
}