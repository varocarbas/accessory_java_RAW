package accessory;

public class parent_static 
{
	protected static boolean _is_ok = true;
	protected static boolean _ignore_errors = false;
	protected static boolean _ignore_errors_persistent = false;
	
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
	
	public static void ignore_errors_persistent_end() { _ignore_errors_persistent = false; }
	
	protected static boolean ignore_errors_internal() 
	{ 
		boolean output = _ignore_errors;
	
		if (!_ignore_errors_persistent) _ignore_errors = false;
		
		return output;
	}
}