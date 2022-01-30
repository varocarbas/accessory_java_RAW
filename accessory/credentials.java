package accessory;

import java.util.HashMap;

public class credentials 
{   
	static { _ini.load(); }
	
	private static final String FILE_SEPARATOR = misc.SEPARATOR_NAME;
	private static final String FILE_EXTENSION = strings.get_default();
	private static final String FILE_ADDITION_USERNAME = keys.USERNAME;
	private static final String FILE_ADDITION_PASSWORD = keys.PASSWORD;
	private static final String FILE_ADDITION_ENCRYPTED = keys.ENCRYPT;
	
	public static HashMap<String, String> get(String type_, String user_, boolean encrypted_, String where_)
    {
		HashMap<String, String> credentials = arrays.get_default();
		if (!strings.are_ok(new String[] { type_, user_})) return credentials;
		
		String username = get_username_password(type_, user_, encrypted_, where_, true);
		String password = get_username_password(type_, user_, encrypted_, where_, false);
		if (!strings.are_ok(new String[] { username, password })) return credentials;
		
		credentials = new HashMap<String, String>();
		credentials.put(keys.USERNAME, username);
		credentials.put(keys.PASSWORD, password);
		
		return credentials;
    }
	
    public static String get_username(String type_, String user_, boolean encrypted_, String where_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return arrays.get_default();
		
    	return get_username_password(type_, user_, encrypted_, where_, true);
    }
	
    public static String get_username_file(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return arrays.get_default();
		
    	return get_username_password(type_, user_, encrypted_, keys.FILE, true);
    }
    
    public static String get_path_username(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return arrays.get_default();
		
    	return get_path_username_password(type_, user_, encrypted_, true);
    }
	
    public static String get_password(String type_, String user_, boolean encrypted_, String where_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return arrays.get_default();
		
    	return get_username_password(type_, user_, encrypted_, where_, false);
    }
	
    public static String get_password_file(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return arrays.get_default();
		
    	return get_username_password(type_, user_, encrypted_, keys.FILE, false);
    }
        
    public static String get_path_password(String type_, String user_, boolean encrypted_)
    {
		if (!strings.are_ok(new String[] { type_, user_})) return arrays.get_default();
		
    	return get_path_username_password(type_, user_, encrypted_, false);    	
    }
    
	private static String get_username_password
    (
    	String type_, String user_, boolean encrypted_, String where_, boolean is_username_
    )
    {
    	String output = strings.get_default();

		String where = (strings.is_ok(where_) ? where_ : defaults.CREDENTIALS_WHERE);
    	
    	if (strings.are_equivalent(where, keys.FILE)) 
    	{
    		output = get_username_password_file(type_, user_, encrypted_, is_username_);
    	}
    	
    	return output;
    }
    
    private static String get_username_password_file
    (
    	String type_, String user_, boolean encrypted_, boolean is_username_
    )
    {   
    	String path = get_path_username_password(type_, user_, encrypted_, is_username_);
    	
    	String[] lines = io.file_to_array(path, true);
    	if (!arrays.is_ok(lines) || !strings.is_ok(lines[0])) return strings.get_default();
    
    	String output = lines[0];     	 
    	if (encrypted_) output = encryption.decrypt(output);
    	
    	return output;
    }
    
    private static String get_path_username_password(String type_, String user_, boolean encrypted_, boolean is_username_)
    {    
    	String file = type_ + FILE_SEPARATOR + user_;
    	file += FILE_SEPARATOR + (is_username_ ? FILE_ADDITION_USERNAME : FILE_ADDITION_PASSWORD); 
    	if (encrypted_) file += FILE_SEPARATOR + FILE_ADDITION_ENCRYPTED;
    	if (strings.is_ok(FILE_EXTENSION)) file += FILE_EXTENSION;
    	
    	return paths.build
    	(
    		new String[] { _config.get_basic(types._CONFIG_BASIC_DIR_CREDENTIALS), file }, true
    	);
    }
}