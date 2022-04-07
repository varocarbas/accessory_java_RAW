package accessory;

import java.util.HashMap;

public abstract class errors 
{	
	public static boolean _triggered = false; 
	public static boolean _exit = false;
	
	static { ini.load(); }

	public static void manage(HashMap<String, String> info_)
	{			
		String message = get_all(info_);

		logs.update(message, arrays.get_value(info_, generic.ID), true);

		_triggered = true;
		
		if (!tests._running && _exit) System.exit(1);
	}

	public static void manage(String type_, Exception e_, String[] further_)
	{		
		String all = (strings.is_ok(type_) ? type_ : "error");
		all += misc.SEPARATOR_CONTENT + get_message(e_, type_);

		if (arrays.is_ok(further_))
		{
			for (String val: further_)
			{
				all += misc.SEPARATOR_CONTENT + val;
			}
		}

		logs.update(all, null, true);

		_triggered = true;
		
		if (!tests._running && _exit) System.exit(1);
	}

	public static void manage_io(String type_, String path_, Exception e_)
	{
		manage(get_info_io(type_, path_, e_));
	}

	public static void manage_db(String type_, String query_, Exception e_, String message_)
	{
		manage(get_info_db(type_, query_, e_, message_));
	}

	private static HashMap<String, String> get_info_db(String type_, String query_, Exception e_, String message_)
	{
		String type = types.check_subtype(type_, types.get_subtypes(types.ERROR_DB, null), null, null);

		HashMap<String, String> info = new HashMap<String, String>();
		info.put(generic.TYPE, type);
		
		String message = message_;
		String message2 = get_message(e_, type);
		
		if ((strings.is_ok(message2) && (generic.is_ok(e_)) || !strings.is_ok(message))) message = message2;
		info.put("message", message);

		if (strings.is_ok(query_)) info.put(generic.QUERY, query_);

		String[] keys = new String[] { db.HOST, db.NAME, db.USER }; 

		for (String key: keys)
		{
			String val = config.get_db(key);
			if (strings.is_ok(val)) info.put(accessory.types.remove_type(key, types.CONFIG_DB), val);
		}

		return info;
	}

	private static HashMap<String, String> get_info_io(String type_, String path_, Exception e_)
	{
		if (!strings.is_ok(type_)) return null;

		HashMap<String, String> info = new HashMap<String, String>();

		info.put(generic.TYPE, type_);
		if (strings.is_ok(path_)) info.put("path", path_);
		info.put("message", get_message(e_, type_));
		
		return info;
	}

	private static String get_all(HashMap<String, String> info_)
	{
		String separator = misc.SEPARATOR_CONTENT;

		String all = (info_.containsKey(generic.TYPE) ? info_.get(generic.TYPE) : "error").toUpperCase();
		all += separator;

		if (!arrays.is_ok(info_)) return all;

		all += arrays.to_string(info_, separator, misc.SEPARATOR_KEYVAL, null);

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