package accessory;

import java.util.HashMap;

public abstract class db_tests
{
	public static final String SOURCE = _types.CONFIG_TESTS_DB_SOURCE;
	
	public static final String DECIMAL = _types.CONFIG_TESTS_DB_FIELD_DECIMAL;
	public static final String INT = _types.CONFIG_TESTS_DB_FIELD_INT;
	public static final String STRING = _types.CONFIG_TESTS_DB_FIELD_STRING;
	public static final String BOOLEAN = _types.CONFIG_TESTS_DB_FIELD_BOOLEAN;

	static String[] _fields = null;
	static String[] _cols = null;
	static HashMap<String, String> _fields_cols = null;
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	static void populate_fields() { _fields = db_common.add_default_fields(SOURCE, new String[] { DECIMAL, INT, STRING, BOOLEAN }); }
}