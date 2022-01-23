package accessory;

public class credentials 
{
	private static String dir = get_defaultault_dir();
	private static String file_heading = strings.get_default();
	private static String file_heading_passw = file_heading;
	
    public static void update_dir(String dir_)
    {
    	if (!strings.is_ok(dir_)) return;
    	
    	dir = dir_;
    }

    public static void update_file_heading(String file_heading_, String what_)
    {
    	if (!strings.is_ok(file_heading_)) return;
    
    	if (strings.are_equivalent(what_, keys.PASSWORD)) file_heading_passw = file_heading_;
    	else file_heading = file_heading_;
    }
    
    public static String get_password(String user_, boolean encrypted_, String what_)
    {
    	String password = strings.get_default();
    	if (!strings.is_ok(user_)) return password;
    	
    	String what = (strings.is_ok(what_) ? what_ : keys.FILE);
    	
    	if (strings.are_equivalent(what, keys.FILE)) 
    	{
    		password = get_password_file(user_, encrypted_);
    	}
    	
    	return password;
    }
    
    public static String get_password_file(String user_, boolean encrypted_)
    {   
    	if (!strings.is_ok(user_)) return strings.get_default();

    	String path = get_path(user_ + paths.EXT_TEXT, keys.PASSWORD);
    	
    	String[] lines = io.file_to_array(path, true);
    	if (!arrays.is_ok(lines) || !strings.is_ok(lines[0])) return strings.get_default();
    
    	String password = lines[0];     	 
    	if (encrypted_) password = encryption.decrypt(password);
    	
    	return password;
    }
    
    private static String get_defaultault_dir()
    {
    	return paths.get_default_dir(keys.CREDENTIALS);
    }
   
    private static String get_path(String file_, String what_)
    {
    	String file = file_;
    	if (!strings.is_ok(file)) return strings.get_default();
    	
    	String heading = file_heading;
    	if (strings.are_equivalent(what_, keys.PASSWORD)) heading = file_heading_passw;
    	if (strings.is_ok(heading)) file = heading + file;
    	
    	return paths.build(new String[] { dir, file }, true);
    }
}