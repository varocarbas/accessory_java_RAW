package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class errors 
{	
	static { _ini.load(); }
	
	public static String check(String type_, String[] types_)
	{
		return types.check_subtype
		(
			type_, (arrays.is_ok(types_) ? types_ : types.get_all_subtypes()), null, null
		);
	}
	
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
	
	public static void manage_sql(String type_, String query_, Exception e_, String message_, boolean to_file_)
	{
		manage
		(
			get_info_sql(type_, query_, e_, message_), to_file_, strings.to_boolean
			(
				_config.get_sql(types._CONFIG_SQL_ERROR_EXIT), defaults.SQL_ERROR_EXIT
			)
		);
	}
	
	public static HashMap<String, String> get_info_sql(String type_, String query_, Exception e_, String message_)
	{
		if (!strings.is_ok(type_)) return arrays.get_default();
		
		HashMap<String, String> info = new HashMap<String, String>();
		
		info.put(keys.LOG, get_log_path());
		info.put(keys.TYPE, type_);
		
		String message = message_;
		if (e_ != null && e_ instanceof Exception) message = e_.getMessage();
		else if (!strings.is_ok(message)) 
		{
			message = ("Wrong " + types.remove_type(type_, types.ERROR_SQL));
		}
			
		info.put(keys.MESSAGE, message);
		
		if (strings.is_ok(query_)) info.put(keys.QUERY, query_);
		
		HashMap<String, String> items = new HashMap<String, String>();
		items.put(types._CONFIG_SQL_HOST, keys.HOST);
		items.put(types._CONFIG_SQL_DB, keys.DB);
		items.put(types._CONFIG_SQL_USER, keys.USER);
		
		for (Entry<String, String> item: items.entrySet())
		{
			String val = _config.get_sql(item.getKey());
			if (strings.is_ok(val)) info.put(item.getValue(), val);
		}

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
	
	private static String get_log_path()
	{
		String file = "errors.log";
		String app_name = _config.get_basic(types._CONFIG_BASIC_NAME);
		if (strings.is_ok(app_name)) file = app_name + misc.SEPARATOR_NAME + file;

		String path = "";
		String dir = _config.get_basic(types._CONFIG_BASIC_DIR_ERRORS);
		if (strings.is_ok(dir)) path = dir;
		
		path += file;
		
		return path;
	}
	
	private static String get_message(HashMap<String, String> info_)
	{
		String message = "ERROR --- ";
		if (!arrays.is_ok(info_)) return message;
			
		message += arrays.to_string
		(
			info_, " --- ", ": ", new String[] { keys.LOG }
		);
		
		return message;
	}
}
