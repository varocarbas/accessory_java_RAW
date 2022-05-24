package accessory;

import java.util.HashMap;

public abstract class errors extends parent_static 
{
	public static boolean _exit = false;

	public static final String DEFAULT_TYPE = _defaults.ERRORS_TYPE;
	public static final String DEFAULT_MESSAGE = _defaults.ERRORS_MESSAGE;
	public static final String DEFAULT_SEPARATOR = _defaults.ERRORS_SEPARATOR;

	private static boolean _triggered = false; 

	public static String get_id() { return types.get_id(types.ID_ERRORS); }

	public static boolean triggered() 
	{
		boolean output = _triggered;

		_triggered = false; 

		return output;
	}

	public static void manage(HashMap<String, String> info_)
	{
		String message = get_all(info_);
		logs.update(message, (String)arrays.get_value(info_, get_key(types.WHAT_ID)), true);

		_triggered = true;

		if (!parent_tests.is_running() && _exit) System.exit(1); 
	}

	public static void manage(String type_, Exception e_, HashMap<String, String> info_) { manage(type_, e_, to_string(info_)); }

	public static void manage(String type_, Exception e_, String[] further_) { manage(type_, e_, to_string(further_)); }

	private static void manage(String type_, Exception e_, String further_)
	{		
		String all = (strings.is_ok(type_) ? type_ : DEFAULT_TYPE);

		all += DEFAULT_SEPARATOR + get_message(e_, type_);
		if (strings.is_ok(further_)) all += DEFAULT_SEPARATOR + further_;

		logs.update(all, null, true);

		_triggered = true;

		if (!parent_tests.is_running() && _exit) System.exit(1);
	}

	private static String to_string(HashMap<String, String> info_) { return (arrays.is_ok(info_) ? arrays.to_string(info_, DEFAULT_SEPARATOR, misc.SEPARATOR_KEYVAL, null) : DEFAULT_MESSAGE); }

	private static String to_string(String[] further_)
	{
		String output = DEFAULT_MESSAGE;

		if (arrays.is_ok(further_))
		{
			for (String val: further_) { output += DEFAULT_SEPARATOR + val; }
		}

		return output;
	}

	public static void manage_db(String type_, String query_, Exception e_, String message_, HashMap<String, String> info_) { manage(get_info_db(type_, query_, e_, message_, info_)); }

	@SuppressWarnings("unchecked")
	private static HashMap<String, String> get_info_db(String type_, String query_, Exception e_, String message_, HashMap<String, String> info_)
	{
		String type = types.check_type(type_, types.ERROR_DB);

		HashMap<String, String> info = (HashMap<String, String>)arrays.get_new(info_);
		info.put(get_key(types.WHAT_TYPE), (strings.is_ok(type) ? type : DEFAULT_TYPE));

		String message = message_;
		String message2 = get_message(e_, type);

		if ((strings.is_ok(message2) && (generic.is_ok(e_)) || !strings.is_ok(message))) message = message2;
		info.put(get_key(types.WHAT_MESSAGE), message);

		if (strings.is_ok(query_)) info.put(get_key(types.WHAT_QUERY), query_);

		return info;
	}

	private static String get_all(HashMap<String, String> info_)
	{
		String all = to_string(info_);

		String type = (String)arrays.get_value(info_, get_key(types.WHAT_TYPE));
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;

		all = type + DEFAULT_SEPARATOR + all;

		return (strings.is_ok(all) ? all : DEFAULT_MESSAGE);
	}

	private static String get_key(String what_) { return _keys.get_key(what_); }

	private static String get_message(Exception e_, String type_)
	{
		String message = DEFAULT_TYPE;

		if (e_ != null) message = e_.getMessage();
		else if (strings.is_ok(type_)) 
		{
			String type = types.remove_type(type_, types.ERROR);
			if (strings.is_ok(type) && !strings.contains("wrong", type, true) && !strings.are_equivalent(type, "error")) message = "Wrong " + type;
		}

		return message;
	}
}