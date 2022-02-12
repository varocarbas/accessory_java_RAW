package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class errors 
{	
	static { _ini.load(); }
		
	public static void manage(HashMap<String, String> info_, boolean exit_)
	{		
		String message = get_message(info_);
		logs.update(message, arrays.get_value(info_, keys.ID), true);
		
		if (exit_) System.exit(1);
	}

	public static void manage_io(String type_, String path_, Exception e_, boolean errors_to_file_, boolean exit_)
	{
		String type = types.check_aliases(type_);
		
		boolean changed = false;
		boolean out = strings.to_boolean(_config.get_logs(types._CONFIG_LOGS_OUT_FILE));
		
		if (!errors_to_file_ && out) 
		{
			changed = true;
			_config.update_logs(types._CONFIG_LOGS_OUT_FILE, strings.from_boolean(false));
		}
		
		manage(get_info_io(type, path_, e_), exit_);
		
		if (changed) _config.update_logs(types._CONFIG_LOGS_OUT_FILE, strings.from_boolean(true));
	}
	
	public static void manage_sql(String type_, String query_, Exception e_, String message_)
	{
		String type = types.check_aliases(type_);
		
		manage
		(
			get_info_sql(type, query_, e_, message_), strings.to_boolean
			(
				_config.get_sql(types._CONFIG_SQL_ERROR_EXIT)
			)
		);
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> get_info_sql(String type_, String query_, Exception e_, String message_)
	{
		if (!strings.is_ok(type_)) return (HashMap<String, String>)arrays.DEFAULT;
		
		String type = types.check_aliases(type_);
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(keys.TYPE, type);
		
		String message = message_;
		if (e_ != null && e_ instanceof Exception) message = e_.getMessage();
		else if (!strings.is_ok(message)) 
		{
			message = ("Wrong " + types.remove_type(type, types.ERROR_SQL));
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
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> get_info_io(String type_, String path_, Exception e_)
	{
		if (!strings.is_ok(type_)) return (HashMap<String, String>)arrays.DEFAULT;
		
		String type = types.check_aliases(type_);
		
		HashMap<String, String> info = new HashMap<String, String>();
		
		info.put(keys.TYPE, type);
		if (strings.is_ok(path_)) info.put(keys.PATH, path_);
		if (e_ != null && e_ instanceof Exception) info.put(keys.MESSAGE, e_.getMessage());

		return info;
	}
	
	private static String get_message(HashMap<String, String> info_)
	{
		String message = "ERROR --- ";
		if (!arrays.is_ok(info_)) return message;
			
		message += arrays.to_string(info_, " --- ", ": ", new String[] { keys.LOG });
		
		return message;
	}
}