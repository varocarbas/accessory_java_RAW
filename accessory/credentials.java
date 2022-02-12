package accessory;

import java.util.HashMap;

public class credentials 
{   	
	static { _ini.load(); }
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> get(String type_, String user_, boolean encrypted_, String where_)
    {		
		HashMap<String, String> credentials = (HashMap<String, String>)arrays.DEFAULT;
		if (!strings.are_ok(new String[] { type_, user_})) return credentials;
		
    	String type = types.check_aliases(type_);
    	String where = types.check_aliases(where_);
    	
		String username = get_username_password(type, user_, encrypted_, where, true);
		String password = get_username_password(type, user_, encrypted_, where, false);
		if (!strings.are_ok(new String[] { username, password })) return credentials;
	
		credentials = new HashMap<String, String>();
		credentials.put(keys.USERNAME, username);
		credentials.put(keys.PASSWORD, password);
		
		return credentials;
    }
	
    public static String get_username(String type_, String user_, boolean encrypted_, String where_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;
		
    	String type = types.check_aliases(type_);
    	String where = types.check_aliases(where_);
    	
    	return get_username_password(type, user_, encrypted_, where, true);
    }
	
    public static String get_username_file(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;
	
    	String type = types.check_aliases(type_);
    	
    	return get_username_password(type, user_, encrypted_, types._CONFIG_CREDENTIALS_WHERE_FILE, true);
    }
    
    public static String get_path_username(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;
		
    	String type = types.check_aliases(type_);
		
    	return get_path_username_password(type, user_, encrypted_, true);
    }
	
    public static String get_password(String type_, String user_, boolean encrypted_, String where_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;
		
    	String type = types.check_aliases(type_);
    	String where = types.check_aliases(where_);
    	
    	return get_username_password(type, user_, encrypted_, where, false);
    }
	
    public static String get_password_file(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;
		
    	String type = types.check_aliases(type_);
    	
    	return get_username_password(type, user_, encrypted_, types._CONFIG_CREDENTIALS_WHERE_FILE, false);
    }
        
    public static String get_path_password(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return strings.DEFAULT;
		
    	String type = types.check_aliases(type_);
    	
    	return get_path_username_password(type, user_, encrypted_, false);    	
    }
    
	private static String get_username_password
    (
    	String type_, String user_, boolean encrypted_, String where_, boolean is_username_
    )
    {
    	String output = strings.DEFAULT;

		String where = (strings.is_ok(where_) ? where_ : defaults.CREDENTIALS_WHERE);
		
    	if (strings.are_equivalent(where, types._CONFIG_CREDENTIALS_WHERE_FILE)) 
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
     	String separator = _config.get_credentials(types._CONFIG_CREDENTIALS_FILE_SEPARATOR);
    	String extension = _config.get_credentials(types._CONFIG_CREDENTIALS_FILE_EXTENSION);
    	
    	String file = type_ + separator + user_;

    	file += separator + 
		(
			is_username_ ? _config.get_credentials(types._CONFIG_CREDENTIALS_FILE_USERNAME) : 
			_config.get_credentials(types._CONFIG_CREDENTIALS_FILE_PASSWORD)
    	);
    	
    	if (encrypted_) file += separator + _config.get_credentials(types._CONFIG_CREDENTIALS_FILE_ENCRYPTED);  	
    	if (strings.is_ok(extension)) file += extension;

    	return paths.build
    	(
    		new String[] { _config.get_credentials(types._CONFIG_CREDENTIALS_FILE_DIR), file }, true
    	);
    }
}