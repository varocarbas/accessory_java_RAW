package accessory;

import java.util.HashMap;

public abstract class credentials 
{   	
	static { ini.load(); }

	public static HashMap<String, String> get(String type_, String user_, boolean encrypted_, String where_)
	{
		HashMap<String, String> credentials = null;
		if (!strings.are_ok(new String[] { type_, user_})) return credentials;

		String type = type_;
		String where = where_;

		String username = get_username_password(type, user_, encrypted_, where, true);
		String password = get_username_password(type, user_, encrypted_, where, false);
		if (!strings.are_ok(new String[] { username, password })) return credentials;

		credentials = new HashMap<String, String>();
		credentials.put(generic.USERNAME, username);
		credentials.put(generic.PASSWORD, password);

		return credentials;
	}

	public static String get_username(String type_, String user_, boolean encrypted_, String where_)
	{
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;

		return get_username_password(type_, user_, encrypted_, where_, true);
	}

	public static String get_username_file(String type_, String user_, boolean encrypted_)
	{
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;

		return get_username_password(type_, user_, encrypted_, types.CONFIG_CREDENTIALS_WHERE_FILE, true);
	}

	public static String get_path_username(String type_, String user_, boolean encrypted_)
	{
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;

		return get_path_username_password(type_, user_, encrypted_, true);
	}

	public static String get_password(String type_, String user_, boolean encrypted_, String where_)
	{
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;

		return get_username_password(type_, user_, encrypted_, where_, false);
	}

	public static String get_password_file(String type_, String user_, boolean encrypted_)
	{
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;

		return get_username_password(type_, user_, encrypted_, types.CONFIG_CREDENTIALS_WHERE_FILE, false);
	}

	public static String get_path_password(String type_, String user_, boolean encrypted_)
	{
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;

		return get_path_username_password(type_, user_, encrypted_, false);    	
	}

	private static String get_username_password(String type_, String user_, boolean encrypted_, String where_, boolean is_username_)
	{
		String output = strings.DEFAULT;

		String where = (strings.is_ok(where_) ? where_ : _defaults.CREDENTIALS_WHERE);

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

		if (encrypted_) file += separator + config.get_credentials(types.CONFIG_CREDENTIALS_FILE_ENCRYPTED);  	
		if (strings.is_ok(extension)) file += extension;

		return paths.build
		(
			new String[] { config.get_credentials(types.CONFIG_CREDENTIALS_FILE_DIR), file }, true
		);
	}
}