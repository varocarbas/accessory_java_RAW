package accessory;

//This class includes all the default values, mainly the ones directly generated by a call to a static 
//method, except for the ones listed in any other "_"[class_name] like _basic or _alls. 
//All these constants are final and local copies in other classes are encouraged.

public abstract class _defaults 
{
	public static final String APP_NAME = "app";
	public static final String USER = "user1";
	
	public static final String PATHS_DIR_APP = paths.get_default_dir(types.CONFIG_BASIC_DIR_APP);
	public static final String PATHS_DIR_INI = paths.get_default_dir(types.CONFIG_BASIC_DIR_INI);
	public static final String PATHS_DIR_LOGS = paths.get_default_dir(types.CONFIG_BASIC_DIR_LOGS);
	public static final String PATHS_DIR_LOGS_ERRORS = paths.get_default_dir(types.CONFIG_BASIC_DIR_LOGS_ERRORS);
	public static final String PATHS_DIR_LOGS_ACTIVITY = paths.get_default_dir(types.CONFIG_BASIC_DIR_LOGS_ACTIVITY);
	public static final String PATHS_DIR_CREDENTIALS = paths.get_default_dir(types.CONFIG_BASIC_DIR_CREDENTIALS);
	public static final String PATHS_DIR_CRYPTO = paths.get_default_dir(types.CONFIG_BASIC_DIR_CRYPTO);
	
	public static final String STRINGS = "";
	public static final int STRINGS_SIZE = 100;
	
	public static final double NUMBERS_DECIMAL = 0.0;
	public static final long NUMBERS_LONG = 0l;
	public static final int NUMBERS_INT = 0;
	public static final int NUMBERS_SIZE_DECIMALS = 3;
	
	public static final int ARRAYS_SIZE = 5;

	public static final String DATES_FORMAT = dates.DATE;

	public static final boolean CREDENTIALS_ENCRYPTED = false;
	public static final String CREDENTIALS_WHERE = types.CONFIG_CREDENTIALS_WHERE_FILE;
	public static final String CREDENTIALS_FILE_DIR = PATHS_DIR_CREDENTIALS;
	public static final String CREDENTIALS_FILE_EXTENSION = STRINGS;
	public static final String CREDENTIALS_FILE_SEPARATOR = misc.SEPARATOR_NAME;
	public static final String CREDENTIALS_FILE_USERNAME = "username";
	public static final String CREDENTIALS_FILE_PASSWORD = "password";
	public static final String CREDENTIALS_FILE_ENCRYPTED = "enc";
	public static final String CREDENTIALS_ID = "credentials";
	public static final String CREDENTIALS_USER = USER;
	
	public static final String CRYPTO_ID = "crypto";
	public static final String CRYPTO_FILE_CIPHER = "cipher";
	public static final String CRYPTO_FILE_KEY = "key";
	public static final String CRYPTO_FILE_EXTENSION = STRINGS;
	public static final String CRYPTO_ALGORITHM_CIPHER = "AES/CTR/NoPadding";
	public static final String CRYPTO_ALGORITHM_KEY = "AES";
	
	public static final String ERRORS_TYPE = "ERROR";
	public static final String ERRORS_MESSAGE = ERRORS_TYPE;
	public static final String ERRORS_SEPARATOR = misc.SEPARATOR_CONTENT;

	public static final boolean LOGS_SCREEN = true;
	public static final boolean LOGS_FILE = true;
	public static final boolean LOGS_DB = false;
	
	public static final double DB_SIZE_DECIMAL = db.get_default_size(types.DATA_DECIMAL);
	public static final double DB_SIZE_LONG = db.get_default_size(types.DATA_LONG);
	public static final double DB_SIZE_INT = db.get_default_size(types.DATA_INT);
	public static final int DB_SIZE_STRING = db.get_default_size(types.DATA_STRING);
	public static final int DB_SIZE_STRING_BIG = db.get_default_size(types.DATA_STRING_BIG);
	public static final int DB_SIZE_BOOLEAN = db.get_default_size(types.DATA_BOOLEAN);
	public static final int DB_SIZE_TIMESTAMP = db.get_default_size(types.DATA_TIMESTAMP);	

	public static final int DB_SIZE_MYSQL_NUMBER = 8;
	public static final int DB_SIZE_MYSQL_DECIMALS = NUMBERS_SIZE_DECIMALS;
	public static final int DB_SIZE_MYSQL_VARCHAR = STRINGS_SIZE;
	public static final int DB_SIZE_MYSQL_TEXT = strings.SIZE_BIG;
	
	public static final String DB_TYPE = types.CONFIG_DB_TYPE_MYSQL;
	public static final String DB_SETUP = types.CONFIG_DB_SETUP_DEFAULT;
	public static final String DB_MAX_POOL = "500";
	public static final String DB_NAME = STRINGS;
	public static final String DB_HOST = "localhost";
	public static final String DB_USER = STRINGS;
	public static final String DB_CREDENTIALS_TYPE = types.remove_type(DB_TYPE, types.CONFIG_DB_TYPE);
	public static final String DB_CREDENTIALS_USERNAME = STRINGS;
	public static final String DB_CREDENTIALS_PASSWORD = STRINGS;
	public static final boolean DB_CREDENTIALS_ENCRYPTED = CREDENTIALS_ENCRYPTED;
	public static final String DB_ORDER = types.DB_ORDER_ASC;
	public static final boolean DB_ORDER_FIELD = true;
	public static final String DB_WHERE_LINK = types.DB_WHERE_LINK_AND;
	public static final String DB_WHERE_OPERAND = types.DB_WHERE_OPERAND_EQUAL;
	public static final boolean DB_WHERE_LITERAL = true;
	
	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).
}