package accessory;

import java.util.HashMap;

public abstract class errors extends parent_static 
{
	public static boolean _exit = false;

	public static final String DEFAULT_TYPE = types.ERROR_DEFAULT;
	public static final String DEFAULT_MESSAGE = DEFAULT_TYPE;
	public static final String DEFAULT_SEPARATOR = misc.SEPARATOR_CONTENT;

	private static boolean _triggered = false; 

	public static String get_class_id() { return types.get_id(types.ID_ERRORS); }

	public static boolean triggered() 
	{
		boolean output = _triggered;

		_triggered = false; 

		return output;
	}

	public static void manage(String type_, Exception e_) { manage(get_message(type_, e_)); }

	public static void manage(String type_, Exception e_, String further_) { manage(get_message(type_, e_, further_)); }

	public static void manage(HashMap<String, Object> info_) { manage(null, null, info_); }

	public static void manage(String type_, Exception e_, HashMap<String, Object> info_) { manage(get_message(type_, e_, get_message(info_))); }

	public static void manage(String[] info_) { manage(null, null, info_); }

	public static void manage(String type_, Exception e_, String[] info_) { manage(get_message(type_, e_, get_message(info_))); }

	private static String get_message(Object info_) 
	{		
		Class<?> type = generic.get_class(info_);
		if (!generic.is_array(type)) return strings.DEFAULT;
	
		String message = "";
		
		if (generic.are_equal(type, HashMap.class)) message = arrays.to_string(arrays.add(info_, _temp), DEFAULT_SEPARATOR, misc.SEPARATOR_KEYVAL, null);
		else 
		{
			message = arrays.to_string(info_, DEFAULT_SEPARATOR);
			
			String temp = arrays.to_string(_temp, DEFAULT_SEPARATOR, misc.SEPARATOR_KEYVAL, null);
			if (strings.is_ok(temp))
			{
				if (strings.is_ok(message)) message += DEFAULT_SEPARATOR;
				message += temp;
			}
		}
		
		if (arrays.is_ok(_temp)) _temp = new HashMap<String, Object>();
		
		return message; 
	}

	private static String get_message(String type_, Exception e_)
	{
		String message = (strings.is_ok(type_) ? type_ : DEFAULT_TYPE);

		String temp = strings.to_string(e_);
		if (strings.is_ok(temp)) 
		{
			if (message != "") message += DEFAULT_SEPARATOR;
			message += temp;
		}

		return message;
	}
	
	private static String get_message(String type_, Exception e_, String further_)
	{
		String message = get_message(type_, e_);
		
		if (strings.is_ok(further_)) 
		{
			if (strings.is_ok(message)) message += DEFAULT_SEPARATOR;
			message += further_;
		}
		
		return message;
	}
	
	private static void manage(String message_)
	{		
		logs.update(check_message(message_), null, true);

		_triggered = true;

		if (!parent_tests.is_running() && _exit) System.exit(1);
	}

	private static String check_message(String message_)
	{
		String message = (strings.is_ok(message_) ? message_ : strings.DEFAULT);
		if (!arrays.is_ok(_temp)) return message;
		
		String temp = get_message(_temp);
		if (strings.is_ok(temp))
		{
			if (strings.is_ok(message)) message += DEFAULT_SEPARATOR;
			message += temp;
			
			_temp = new HashMap<String, Object>();
		}	
		
		return (strings.is_ok(message) ? message : DEFAULT_MESSAGE);
	}
}