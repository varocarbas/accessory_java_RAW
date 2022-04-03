package accessory;

import java.util.HashMap;

public abstract class credentials 
{   
	public static final String DIR = types.CONFIG_BASIC_DIR_CREDENTIALS;
	public static final String WHERE = types.CONFIG_CREDENTIALS_WHERE;
	public static final String ENCRYPTED = types.CONFIG_CREDENTIALS_ENCRYPTED;	

	public static final String DEFAULT_WHERE = _defaults.CREDENTIALS_WHERE;
	
	static { ini.load(); }

	public static HashMap<String, String> get(String type_, String user_, boolean encrypted_, String where_)
	{
		HashMap<String, String> credentials = null;

		if (!strings.are_ok(new String[] { type_, user_})) return credentials;

		String where = check_where(where_);

		String username = get_username_password(type_, user_, encrypted_, where, true);
		String password = get_username_password(type_, user_, encrypted_, where, false);
		if (!strings.are_ok(new String[] { username, password })) return credentials;

		credentials = new HashMap<String, String>();
		credentials.put(generic.USERNAME, username);
		credentials.put(generic.PASSWORD, password);

		return credentials;
	}

	private static String get_username_password(String type_, String user_, boolean encrypted_, String where_, boolean is_username_)
	{
		String output = strings.DEFAULT;

		String where = (strings.is_ok(where_) ? where_ : DEFAULT_WHERE);

		if (strings.are_equivalent(where, types.CONFIG_CREDENTIALS_WHERE_FILE)) 
		{
			output = get_username_password_file(type_, user_, encrypted_, is_username_);
		}

		return output;
	}

	private static String get_username_password_file(String type_, String user_, boolean encrypted_, boolean is_username_)
	{   
		String path = get_path_username_password(type_, user_, encrypted_, is_username_);
		
		String[] lines = io.file_to_array(path, true);
		if (!arrays.is_ok(lines) || !strings.is_ok(lines[0])) return strings.DEFAULT;

		String output = lines[0];     	 
		if (encrypted_) output = encryption.decrypt(output);

		return output;
	}

	private static String get_path_username_password(String type_, String user_, boolean encrypted_, boolean is_username_)
	{   
		String separator = config.get_credentials(types.CONFIG_CREDENTIALS_FILE_SEPARATOR);
		String extension = config.get_credentials(types.CONFIG_CREDENTIALS_FILE_EXTENSION);

		String file = type_ + separator + user_;

		file += separator + 
		(
			is_username_ ? config.get_credentials(types.CONFIG_CREDENTIALS_FILE_USERNAME) : 
			config.get_credentials(types.CONFIG_CREDENTIALS_FILE_PASSWORD)
		);

		if (encrypted_) file += separator + config.get_credentials(ENCRYPTED);  	
		if (strings.is_ok(extension)) file += extension;

		return paths.build(new String[] { config.get_credentials(DIR), file }, true);
	}
	
	private static String check_where(String where_)
	{
		return types.check_subtype(where_, types.get_subtypes(WHERE, null), null, null);
	}
}