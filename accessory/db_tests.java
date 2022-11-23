package accessory;

import java.util.HashMap;

public abstract class db_tests
{
	public static final String SOURCE = types.CONFIG_TESTS_DB_SOURCE;
	
	public static final String DECIMAL = types.CONFIG_TESTS_DB_FIELD_DECIMAL;
	public static final String INT = types.CONFIG_TESTS_DB_FIELD_INT;
	public static final String STRING = types.CONFIG_TESTS_DB_FIELD_STRING;
	public static final String BOOLEAN = types.CONFIG_TESTS_DB_FIELD_BOOLEAN;

	private static String[] _fields = null;
	private static HashMap<String, String> _cols = null;
	
	private static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	public static boolean is_quick() { return _is_quick; }
	
	public static void is_quick(boolean is_quick_) { _is_quick = is_quick_; }
	
	public static String get_field_col(String field_) { return (_is_quick ? get_col(field_) : field_); }

	public static String get_col(String field_) 
	{ 
		get_cols();
		
		return (_cols.containsKey(field_) ? _cols.get(field_) : strings.DEFAULT); 
	}

	public static String[] get_fields() 
	{
		if (_fields == null) _fields = new String[] { DECIMAL, INT, STRING, BOOLEAN };
		
		return _fields; 
	}

	public static HashMap<String, String> get_cols() 
	{ 
		if (_cols == null) _cols = db.get_cols(SOURCE, get_fields());
			
		return _cols; 
	} 

	static void start() 
	{ 
		get_fields();
		
		get_cols();
	}
}