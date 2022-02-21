package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class errors 
{	
	public static boolean _triggered = false; 
	
	static { _ini.load(); }

	public static void manage(HashMap<String, String> info_, boolean exit_)
	{			
		String message = get_message(info_);

		logs.update(message, arrays.get_value(info_, keys.ID));

		_triggered = true;
		
		if (exit_) System.exit(1);
	}

	public static void manage(String type_, Exception e_, String[] further_, boolean exit_)
	{		
		String message = (strings.is_ok(type_) ? type_ : keys.ERROR) + misc.SEPARATOR_CONTENT;

		if (generic.is_ok(e_)) message += keys.EXCEPTION + ": " + e_.getMessage() + misc.SEPARATOR_CONTENT; 

		if (arrays.is_ok(further_))
		{
			for (String val: further_)
			{
				message += val + misc.SEPARATOR_CONTENT;
			}
		}

		logs.update(message, null);

		_triggered = true;
		
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

	public static void manage_db(String type_, String query_, Exception e_, String message_)
	{
		String type = types.check_aliases(type_);

		manage
		(
			get_info_db(type, query_, e_, message_), strings.to_boolean
			(
				_config.get_db(types._CONFIG_DB_ERROR_EXIT)
			)
		);
	}

	private static HashMap<String, String> get_info_db(String type_, String query_, Exception e_, String message_)
	{
		if (!strings.is_ok(type_)) return null;

		String type = types.check_aliases(type_);

		HashMap<String, String> info = new HashMap<String, String>();
		info.put(keys.TYPE, type);

		String message = message_;
		if (generic.is_ok(e_)) message = e_.getMessage();
		else if (!strings.is_ok(message)) 
		{
			message = ("Wrong " + types.remove_type(type, types.ERROR_DB));
		}

		info.put(keys.MESSAGE, message);

		if (strings.is_ok(query_)) info.put(keys.QUERY, query_);

		HashMap<String, String> items = new HashMap<String, String>();
		items.put(types._CONFIG_DB_HOST, keys.HOST);
		items.put(types._CONFIG_DB_NAME, keys.DB);
		items.put(types._CONFIG_DB_USER, keys.USER);

		for (Entry<String, String> item: items.entrySet())
		{
			String val = _config.get_db(item.getKey());
			if (strings.is_ok(val)) info.put(item.getValue(), val);
		}

		return info;
	}

	private static HashMap<String, String> get_info_io(String type_, String path_, Exception e_)
	{
		if (!strings.is_ok(type_)) return null;

		String type = types.check_aliases(type_);

		HashMap<String, String> info = new HashMap<String, String>();

		info.put(keys.TYPE, type);
		if (strings.is_ok(path_)) info.put(keys.PATH, path_);
		if (e_ != null && e_ instanceof Exception) info.put(keys.MESSAGE, e_.getMessage());

		return info;
	}

	private static String get_message(HashMap<String, String> info_)
	{
		String separator = misc.SEPARATOR_CONTENT;

		String message = (info_.containsKey(keys.TYPE) ? info_.get(keys.TYPE) : keys.ERROR).toUpperCase();
		message += separator;

		if (!arrays.is_ok(info_)) return message;

		message += arrays.to_string(info_, separator, ": ", new String[] { keys.LOG });

		return message;
	}
}