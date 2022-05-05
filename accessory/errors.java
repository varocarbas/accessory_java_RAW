package accessory;

import java.util.HashMap;

public abstract class errors 
{		
	public static boolean _exit = false;
	
	public static final String DEFAULT_TYPE = _defaults.ERRORS_TYPE;
	public static final String DEFAULT_MESSAGE = _defaults.ERRORS_MESSAGE;
	public static final String DEFAULT_SEPARATOR = _defaults.ERRORS_SEPARATOR;

	private static boolean _triggered = false; 
	
	static { _ini.start(); }
	public static final String _ID = types.get_id(types.ID_ERRORS);
	
	public static boolean triggered() 
	{
		boolean output = _triggered;
		
		_triggered = false; 
		
		return output;
	}
	
	public static void manage(HashMap<String, String> info_)
	{
		String message = get_all(info_);
		logs.update(message, (String)arrays.get_value(info_, get_generic(types.WHAT_ID)), true);

		_triggered = true;
		
		if (!tests.is_running() && _exit) System.exit(1); 
	}

	public static void manage(String type_, Exception e_, String[] further_)
	{		
		String all = (strings.is_ok(type_) ? type_ : DEFAULT_TYPE);
		all += DEFAULT_SEPARATOR + get_message(e_, type_);

		if (arrays.is_ok(further_))
		{
			for (String val: further_)
			{
				all += DEFAULT_SEPARATOR + val;
			}
		}

		logs.update(all, null, true);

		_triggered = true;
		
		if (!tests.is_running() && _exit) System.exit(1);
	}

	public static void manage_io(String type_, String path_, Exception e_)
	{
		manage(get_info_io(type_, path_, e_));
	}

	public static void manage_db(String type_, String query_, Exception e_, String message_, HashMap<String, String> info_)
	{
		manage(get_info_db(type_, query_, e_, message_, info_));
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, String> get_info_db(String type_, String query_, Exception e_, String message_, HashMap<String, String> info_)
	{
		String type = types.check_type(type_, types.get_subtypes(types.ERROR_DB));

		HashMap<String, String> info = (HashMap<String, String>)arrays.get_new(info_);
		info.put(get_generic(types.WHAT_TYPE), (strings.is_ok(type) ? type : DEFAULT_TYPE));
		
		String message = message_;
		String message2 = get_message(e_, type);
		
		if ((strings.is_ok(message2) && (generic.is_ok(e_)) || !strings.is_ok(message))) message = message2;
		info.put("message", message);

		if (strings.is_ok(query_)) info.put(get_generic(types.WHAT_QUERY), query_);

		return info;
	}

	private static HashMap<String, String> get_info_io(String type_, String path_, Exception e_)
	{
		HashMap<String, String> info = new HashMap<String, String>();

		info.put(get_generic(types.WHAT_TYPE), (strings.is_ok(type_) ? type_ : DEFAULT_TYPE));
		
		if (strings.is_ok(path_)) info.put("path", path_);
		info.put("message", get_message(e_, type_));
		
		return info;
	}

	private static String get_all(HashMap<String, String> info_)
	{
		if (!arrays.is_ok(info_)) return DEFAULT_MESSAGE;

		String all = arrays.to_string(info_, DEFAULT_SEPARATOR, misc.SEPARATOR_KEYVAL, null);
		
		String type = (String)arrays.get_value(info_, get_generic(types.WHAT_TYPE));
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;
		all = type + DEFAULT_SEPARATOR + all;

		return (strings.is_ok(all) ? all : DEFAULT_MESSAGE);
	}
	
	private static String get_generic(String what_) { return parent_ini.get_generic_key(what_); }
	
	private static String get_message(Exception e_, String type_)
	{
		String message = DEFAULT_TYPE;
		
		if (e_ != null) message = e_.getMessage();
		else if (strings.is_ok(type_)) 
		{
			String type = types.remove_type(type_, types.ERROR);
			if (strings.is_ok(type) && !strings.contains("wrong", type, true) && !strings.are_equivalent(type_, "error")) message = "Wrong " + type;
		}
		
		return message;
	}
}