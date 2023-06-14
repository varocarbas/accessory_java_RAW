package accessory;

public abstract class db_tests
{
	public static final String SOURCE = _types.CONFIG_TESTS_DB_SOURCE;
	
	public static final String DECIMAL = _types.CONFIG_TESTS_DB_FIELD_DECIMAL;
	public static final String INT = _types.CONFIG_TESTS_DB_FIELD_INT;
	public static final String STRING = _types.CONFIG_TESTS_DB_FIELD_STRING;
	public static final String BOOLEAN = _types.CONFIG_TESTS_DB_FIELD_BOOLEAN;

	public static boolean is_quick() { return db_common.is_quick(SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(SOURCE, is_quick_); }
}