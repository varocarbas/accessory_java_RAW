package accessory;

import java.util.HashMap;

public class parent_static 
{
	protected static boolean _is_ok = true;
	protected static boolean _ignore_errors = false;
	protected static boolean _ignore_errors_persistent = false;
	protected static boolean _error_triggered = false;

	public static boolean is_ok() 
	{
		boolean output = _is_ok;

		_is_ok = true;

		return output; 
	}

	public static void ignore_errors() { ignore_errors(false); }

	public static void ignore_errors(boolean persistent_) 
	{ 
		_ignore_errors = true; 
		_ignore_errors_persistent = persistent_;
	}

	public static void ignore_errors_persistent_end() 
	{ 
		_ignore_errors_persistent = false; 
		_ignore_errors = false;
	}

	protected static boolean ignore_errors_internal() 
	{ 
		boolean output = _ignore_errors;

		if (!_ignore_errors_persistent) _ignore_errors = false;

		return output;
	}

	protected static void manage_error(String type_, Exception e_, HashMap<String, String> info_)
	{
		if (!ignore_errors_internal()) 
		{
			errors.manage(type_, e_, info_); 

			_error_triggered = true;
		}
	}

	protected static void method_start()
	{
		_is_ok = false;
		_error_triggered = false;
	}

	protected static void method_end()
	{
		if (_error_triggered) 
		{
			_is_ok = false;
			_error_triggered = false;
		}
		else _is_ok = true;
	}
}