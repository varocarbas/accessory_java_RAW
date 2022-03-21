package accessory;

public abstract class _defaults 
{	
	public static final String STRING = "";
	public static final double DECIMAL = 0.0;
	public static final long LONG = 0l;
	public static final int INT = 0;

	public static final int SIZE_STRING = 100;
	public static final int SIZE_ARRAY = 5;
	public static final int SIZE_DECIMALS = 3;
	
	public static final int SIZE_MYSQL_NUMBER = 8;
	public static final int SIZE_MYSQL_DECIMALS = SIZE_DECIMALS;
	public static final int SIZE_MYSQL_VARCHAR = SIZE_STRING;
	public static final int SIZE_MYSQL_TEXT = strings.SIZE_BIG;

	public static final String DATES_FORMAT = _types.DATES_FORMAT_DATE;
	
	public static final String APP_NAME = _keys.APP;
	public static final String DIR_APP = paths.get_default_dir(_types.WHAT_DIR_APP);
	public static final String DIR_INI = paths.get_default_dir(_types.WHAT_DIR_INI);
	public static final String DIR_LOGS = paths.get_default_dir(_types.WHAT_DIR_LOGS);
	
	static final boolean CREDENTIALS_ENCRYPTED = false;
	static final String CREDENTIALS_WHERE = _types.CONFIG_CREDENTIALS_WHERE_FILE;
	static final String CREDENTIALS_FILE_DIR = paths.get_default_dir(_types.WHAT_DIR_CREDENTIALS);
	static final String CREDENTIALS_FILE_EXTENSION = STRING;
	static final String CREDENTIALS_FILE_SEPARATOR = misc.SEPARATOR_NAME;
	static final String CREDENTIALS_FILE_USERNAME = _keys.USERNAME;
	static final String CREDENTIALS_FILE_PASSWORD = _keys.PASSWORD;
	static final String CREDENTIALS_FILE_ENCRYPTED = "enc";

	static final String DB_TYPE = _types.CONFIG_DB_TYPE_MYSQL;
	static final String DB_SETUP = _types.CONFIG_DB_SETUP_MAIN;
	static final String DB_MAX_POOL = "500";
	static final String DB_NAME = STRING;
	static final String DB_HOST = "localhost";
	static final String DB_USER = STRING;
	static final String DB_FIELDS_DEFAULT_ID = misc.SEPARATOR_NAME + _keys.ID;
	static final String DB_FIELDS_DEFAULT_TIMESTAMP = "_timestamp";
	static final String DB_CREDENTIALS_TYPE = _types.remove_type(DB_TYPE, _types.CONFIG_DB_TYPE);
	static final String DB_CREDENTIALS_WHERE = CREDENTIALS_WHERE;
	static final String DB_CREDENTIALS_USERNAME = STRING;
	static final String DB_CREDENTIALS_PASSWORD = STRING;
	static final boolean DB_CREDENTIALS_ENCRYPTED = CREDENTIALS_ENCRYPTED;
	static final boolean DB_ERROR_EXIT = true;
	static final String DB_ORDER = _types.DB_ORDER_ASC;
	static final boolean DB_ORDER_FIELD = true;
	static final String DB_WHERE_LINK = _types.DB_WHERE_LINK_AND;
	static final String DB_WHERE_OPERAND = _types.DB_WHERE_OPERAND_EQUAL;
	static final boolean DB_WHERE_LITERAL = true;
	
	static final String LOGS_DB_SOURCE = "logs";
	static final String LOGS_DB_FIELD_ID = _keys.ID;
	static final String LOGS_DB_FIELD_MESSAGE = "message";
	
	static final String TESTS_DB_SOURCE = "tests";
	static final String TESTS_DB_FIELD_INT = "int";
	static final String TESTS_DB_FIELD_STRING = "string";
	static final String TESTS_DB_FIELD_DECIMAL = "decimal";

	static final boolean LOGS_SCREEN = true;
	static final boolean LOGS_FILE = true;
	static final boolean LOGS_DB = false;

	//Method meant to force this class to be loaded when required (e.g., when ini.load() is called).
	public static void load() { } 
}