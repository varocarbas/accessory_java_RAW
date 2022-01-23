package accessory;

import java.util.HashMap;

public class errors 
{	
	public static void manage(HashMap<String, String> info_, boolean to_file_, boolean exit_)
	{		
		String message = get_message(info_);
		
		System.out.println(message);
	
		if (to_file_)
		{
			String path = arrays.get_value(info_, keys.LOG);
			if (!strings.is_ok(path)) path = get_log_path();
			
			io.line_to_file(path, message, true, null, false);
		}
		
		if (exit_) System.exit(1);
	}

	public static void manage_io(String type_, String path_, Exception e_, boolean to_file_, boolean exit_)
	{
		manage(get_info_io(type_, path_, e_), to_file_, exit_);
	}
	
	public static void manage_db(String type_, String host_, String db_, String user_, String query_, Exception e_, boolean to_file_, boolean exit_)
	{
		manage(get_info_db(type_, host_, db_, user_, query_, e_), to_file_, exit_);
	}
	
	private static String get_message(HashMap<String, String> info_)
	{
		String message = "ERROR --- ";
		if (!arrays.is_ok(info_)) return message;
			
		message += arrays.to_string(info_, " --- ", ": ");
		
		return message;
	}
	
	public static HashMap<String, String> get_info_db(String type_, String host_, String db_, String user_, String query_, Exception e_)
	{
		if (!strings.is_ok(type_)) return arrays.get_default();
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(keys.LOG, get_log_path());
		
		info.put(keys.TYPE, type_);
		info.put
		(
			keys.MESSAGE, 
			(
				(e_ != null && e_ instanceof Exception) ? 
				e_.getMessage() : ("Wrong " + type_)
			)
		);
		
		if (strings.is_ok(host_)) info.put(keys.HOST, host_);
		if (strings.is_ok(db_)) info.put(keys.DB, db_);
		if (strings.is_ok(user_)) info.put(keys.USER, user_);
		if (strings.is_ok(query_)) info.put(keys.QUERY, query_);

		return info;
	}
	
	public static HashMap<String, String> get_info_io(String type_, String path_, Exception e_)
	{
		if (!strings.is_ok(type_)) return arrays.get_default();
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(keys.LOG, get_log_path());
		
		if (strings.is_ok(path_)) info.put(keys.PATH, path_);
		if (e_ != null && e_ instanceof Exception) info.put(keys.MESSAGE, e_.getMessage());

		return info;
	}
	
	public static String get_log_path()
	{
		String path = "error.log";
		
		return path;
	}
}
