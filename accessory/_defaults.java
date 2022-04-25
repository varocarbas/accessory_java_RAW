package accessory;

//This class includes all the default values, mainly the ones directly generated by a call to a static 
//method, except for the ones listed in any other "_"[class_name] like _basic or _alls. 
//All these constants are final and local copies in other classes are encouraged.

abstract class _defaults 
{
	public static final String APP_NAME = "app";
	public static final String USER = "user1";
		
	public static final String STRINGS = "";
	public static final int STRINGS_SIZE = 100;

	public static final boolean BOOLEAN = false;
	public static final byte BYTE = Byte.MIN_VALUE;
	public static final char CHAR = Character.MIN_VALUE;
	
	public static final double NUMBERS_DECIMAL = 0.0;
	public static final long NUMBERS_LONG = 0l;
	public static final int NUMBERS_INT = 0;
	public static final int NUMBERS_SIZE_DECIMALS = 3;
	
	public static final int ARRAYS_SIZE = 5;

	public static final String DATES_FORMAT = dates.DATE;

	public static final String CREDENTIALS_WHERE = types.CONFIG_CREDENTIALS_WHERE_FILE;
	public static final String CREDENTIALS_FILE_EXTENSION = STRINGS;
	public static final String CREDENTIALS_FILE_USERNAME = "username";
	public static final String CREDENTIALS_FILE_PASSWORD = "password";
	public static final String CREDENTIALS_FILE_ENCRYPTED = "enc";
	public static final String CREDENTIALS_ID = "credentials";
	
	public static final String CRYPTO_ID = "crypto";
	public static final String CRYPTO_FILE_CIPHER = "cipher";
	public static final String CRYPTO_FILE_KEY = "key";
	public static final String CRYPTO_FILE_EXTENSION = STRINGS;
	public static final String CRYPTO_ALGORITHM_CIPHER = "AES/CTR/NoPadding";
	public static final String CRYPTO_ALGORITHM_KEY = "AES";
	public static final boolean CRYPTO_LOG_ENCRYPTION_INFO = true;
	
	public static final String ERRORS_TYPE = "ERROR";
	public static final String ERRORS_MESSAGE = ERRORS_TYPE;
	public static final String ERRORS_SEPARATOR = misc.SEPARATOR_CONTENT;

	public static final boolean LOGS_SCREEN = true;
	public static final boolean LOGS_FILE = true;
	public static final boolean LOGS_DB = false;

	public static final String DB = types.CONFIG_TESTS_DB;
	public static final String DB_SOURCE = types.CONFIG_TESTS_DB_SOURCE;
	public static final String DB_SETUP = types.CONFIG_DB;
	public static final String DB_TYPE = types.CONFIG_DB_SETUP_TYPE_MYSQL;
	public static final String DB_MAX_POOL = "500";
	public static final String DB_HOST = "localhost";
	public static final String DB_CREDENTIALS_TYPE = types.remove_type(DB_TYPE, types.CONFIG_DB_SETUP_TYPE);
	public static final boolean DB_CREDENTIALS_ENCRYPTED = false;
	public static final String DB_ORDER = types.DB_ORDER_ASC;
	public static final boolean DB_ORDER_FIELD = true;
	public static final String DB_WHERE_LINK = types.DB_WHERE_LINK_AND;
	public static final String DB_WHERE_OPERAND = types.DB_WHERE_OPERAND_EQUAL;
	public static final boolean DB_WHERE_LITERAL = true;

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
		
	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).

	public static Double get_default(Double input_) { return (Double)NUMBERS_DECIMAL; }

	public static double get_default(double input_) { return NUMBERS_DECIMAL; }
	
	public static Long get_default(Long input_) { return (Long)NUMBERS_LONG; }
	
	public static long get_default(long input_) { return NUMBERS_LONG; }
	
	public static Integer get_default(Integer input_) { return(Integer)NUMBERS_INT; }
	
	public static int get_default(int input_) { return NUMBERS_INT; }
	
	public static Boolean get_default(Boolean input_) { return (Boolean)BOOLEAN; }
	
	public static boolean get_default(boolean input_) { return BOOLEAN; }
	
	public static Byte get_default(Byte input_) { return (Byte)BYTE; }
	
	public static byte get_default(byte input_) { return BYTE; }

	public static Character get_default(Character input_) { return (Character)CHAR; }
	
	public static char get_default(char input_) { return CHAR; }

	public static double[] get_default(double[] input_) { return null; }

	public static long[] get_default(long[] input_) { return null; }

	public static int[] get_default(int[] input_) { return null; }

	public static boolean[] get_default(boolean[] input_) { return null; }
	
	public static byte[] get_default(byte[] input_) { return null; }

	public static char[] get_default(char[] input_) { return null; }
	
	public static <x> x[] get_default(x[] input_) { return null; }
	
	public static <x> Object get_default(Object input_) { return null; }
	
	public static Object get_default(Class<?> type_)
	{
		Object output = null;
		
		if (generic.are_equal(type_, Double.class)) output = NUMBERS_DECIMAL;
		else if (generic.are_equal(type_, Long.class)) output = NUMBERS_LONG;
		else if (generic.are_equal(type_, Integer.class)) output = NUMBERS_INT;
		else if (generic.is_string(type_)) output = STRINGS;
		else if (generic.is_boolean(type_)) output = BOOLEAN;
		else if (generic.is_byte(type_)) output = BYTE;
		else if (generic.is_char(type_)) output = CHAR;
		
		return output;
	}
}