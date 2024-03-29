package accessory;

import java.util.HashMap;

public abstract class parent_static 
{	
	static boolean _is_ok = true;

	protected static HashMap<String, Object> _temp = new HashMap<String, Object>();

	private static final long MAX_LOCK_ELAPSED = 1;

	private static boolean _ignore_errors = false;
	private static boolean _ignore_errors_persistent = false;
	private static boolean _error_triggered = false;

	private static volatile boolean _locked = false;
	private static volatile boolean _locked2 = false;

	public static void __lock()
	{			
		long elapsed = dates.start_elapsed();
		
		boolean locked2 = false;
		
		while (true)
		{
			if (!locked2)
			{
				if (!_locked2 || (dates.get_elapsed(elapsed) >= MAX_LOCK_ELAPSED))
				{
					_locked2 = true;
					locked2 = true;
				}
			}
			else
			{
				if (!_locked || (dates.get_elapsed(elapsed) >= MAX_LOCK_ELAPSED))
				{
					_locked = true;
					_locked2 = false;
					
					return;
				}
			}
		}
	}
	
	public static void __unlock() { _locked = false; }
	
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
	
	public static boolean update_temp(String key_, Object val_)
	{
		if (!strings.is_ok(key_) || val_ == null) return false;
		
		if (!arrays.is_ok(_temp)) _temp = new HashMap<String, Object>();
		_temp.put(key_, val_);
		
		return true;
	}
	
	protected static boolean ignore_errors_internal() 
	{ 
		boolean output = _ignore_errors;

		if (!_ignore_errors_persistent) _ignore_errors = false;

		return output;
	}

	protected static void manage_error(String type_, Exception e_, HashMap<String, Object> info_)
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