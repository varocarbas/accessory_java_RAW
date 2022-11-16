package accessory;

public abstract class db_tests 
{
	public static final String SOURCE = types.CONFIG_TESTS_DB_SOURCE;
	
	public static final String DECIMAL = types.CONFIG_TESTS_DB_FIELD_DECIMAL;
	public static final String INT = types.CONFIG_TESTS_DB_FIELD_INT;
	public static final String STRING = types.CONFIG_TESTS_DB_FIELD_STRING;
	public static final String BOOLEAN = types.CONFIG_TESTS_DB_FIELD_BOOLEAN;

	private static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	public static boolean is_quick() { return _is_quick; }
	
	public static void is_quick(boolean is_quick_) { _is_quick = is_quick_; }

	public static String[] get_fields() { return new String[] { DECIMAL, INT, STRING, BOOLEAN }; }

	static void start() { }
}