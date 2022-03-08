package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class errors 
{	
	public static boolean _triggered = false; 
	
	static { _ini.load(); }

	public static void manage(HashMap<String, String> info_, boolean exit_)
	{			
		String message = get_all(info_);

		logs.update(message, arrays.get_value(info_, keys.ID));

		_triggered = true;
		
		if (!tests._running && exit_) System.exit(1);
	}

	public static void manage(String type_, Exception e_, String[] further_, boolean exit_)
	{		
		String all = (strings.is_ok(type_) ? type_ : keys.ERROR);
		all += misc.SEPARATOR_CONTENT + get_message(e_, type_);

		if (arrays.is_ok(further_))
		{
			for (String val: further_)
			{
				all += misc.SEPARATOR_CONTENT + val;
			}
		}

		logs.update(all, null);

		_triggered = true;
		
		if (!tests._running && exit_) System.exit(1);
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
		String message2 = get_message(e_, type_);
		if (strings.is_ok(message2) && (generic.is_ok(e_) || strings.is_ok(message))) message = message2;
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
		info.put(keys.MESSAGE, get_message(e_, type_));
		
		return info;
	}

	private static String get_all(HashMap<String, String> info_)
	{
		String separator = misc.SEPARATOR_CONTENT;

		String all = (info_.containsKey(keys.TYPE) ? info_.get(keys.TYPE) : keys.ERROR).toUpperCase();
		all += separator;

		if (!arrays.is_ok(info_)) return all;

		all += arrays.to_string(info_, separator, misc.SEPARATOR_KEYVAL, new String[] { keys.LOG });

		return all;
	}
	
	private static String get_message(Exception e_, String type_)
	{
		String message = "";
		
		if (generic.is_ok(e_)) message = e_.getMessage();
		else if (strings.is_ok(type_)) 
		{
			String type = types.remove_type(type_, types.ERROR_DB);
			if (strings.is_ok(type)) message = "Wrong " + type;
		}
		
		return message;
	}
}