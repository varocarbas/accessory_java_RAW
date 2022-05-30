package accessory;

abstract class _defaults 
{
	public static final String APP_NAME = "app";
	public static final String USER = "user1";

	public static final String STRINGS = "";
	public static final int STRINGS_SIZE = strings.SIZE_REGULAR;

	public static final boolean BOOLEAN = false;
	public static final int BOOLEAN_INT = generic.boolean_to_int(BOOLEAN);
	public static final String BOOLEAN_STRING = generic.boolean_to_string(BOOLEAN);
	public static final byte BYTE = Byte.MIN_VALUE;
	public static final char CHAR = Character.MIN_VALUE;

	public static final double NUMBERS_DECIMAL = 0.0;
	public static final long NUMBERS_LONG = 0l;
	public static final int NUMBERS_INT = 0;
	public static final int NUMBERS_SIZE_DECIMALS = 3;

	public static final int ARRAYS_SIZE = 5;

	public static final String DATES_FORMAT = dates.FORMAT_DATE_TIME;
	public static final String DATES_FORMAT_DATE_TIME = dates.FORMAT_DATE_TIME;
	public static final String DATES_FORMAT_DATE = dates.FORMAT_DATE;
	public static final String DATES_FORMAT_TIME = dates.FORMAT_TIME;
	public static final String DATES_FORMAT_TIMESTAMP = dates.FORMAT_TIMESTAMP;

	public static final String DATES_UNIT = types.DATES_UNIT_SECONDS;
	public static final String DATES_UNIT_DATE_TIME = types.DATES_UNIT_DAYS;
	public static final String DATES_UNIT_DATE = types.DATES_UNIT_DAYS;
	public static final String DATES_UNIT_TIME = types.DATES_UNIT_SECONDS;
	public static final String DATES_UNIT_TIMESTAMP = types.DATES_UNIT_MINUTES;

	public static final int DATES_SIZE_DAYS = 50;
	public static final int DATES_SIZE_HOURS = 10;

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

	public static final String ERRORS_TYPE = types.ERROR_DEFAULT;
	public static final String ERRORS_MESSAGE = ERRORS_TYPE;
	public static final String ERRORS_SEPARATOR = misc.SEPARATOR_CONTENT;

	public static final boolean LOGS_SCREEN = true;
	public static final boolean LOGS_FILE = true;
	public static final boolean LOGS_DB = false;

	public static final double SIZE_MIN = numbers.MIN_DECIMAL;
	public static final double SIZE_MAX = numbers.MAX_DECIMAL;
	public static final int SIZE_DECIMALS = NUMBERS_SIZE_DECIMALS;

	public static final String DB = types.CONFIG_DB_DEFAULT;
	public static final String DB_NAME = "accessory";
	public static final String DB_SOURCE = types.CONFIG_TESTS_DB_SOURCE;
	public static final String DB_SETUP = types.CONFIG_DB;
	public static final String DB_TYPE = types.CONFIG_DB_SETUP_TYPE_MYSQL;
	public static final String DB_MAX_POOL = "500";
	public static final String DB_HOST = "localhost";
	public static final String DB_CREDENTIALS_TYPE = types.remove_type(DB_TYPE, types.CONFIG_DB_SETUP_TYPE);
	public static final boolean DB_CREDENTIALS_ENCRYPTED = false;
	public static final String DB_ORDER = types.DB_ORDER_ASC;
	public static final boolean DB_ORDER_IS_FIELD = true;
	public static final String DB_WHERE_LINK = types.DB_WHERE_LINK_AND;
	public static final String DB_WHERE_OPERAND = types.DB_WHERE_OPERAND_EQUAL;
	public static final boolean DB_WHERE_LITERAL = true;

	public static final int DB_SIZE_MYSQL_NUMBER = 8;
	public static final int DB_SIZE_MYSQL_DECIMALS = NUMBERS_SIZE_DECIMALS;
	public static final int DB_SIZE_MYSQL_VARCHAR = STRINGS_SIZE;
	public static final int DB_SIZE_MYSQL_TEXT = strings.SIZE_BIG;
	public static final int DB_SIZE_MYSQL_TIMESTAMP = dates.get_length(dates.FORMAT_TIMESTAMP);

	public static final String DB_FIELD_TYPE = types.DATA_STRING_SMALL;
	public static final int DB_FIELD_DECIMALS = SIZE_DECIMALS;

	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).

	public static Double get(Double input_) { return (Double)NUMBERS_DECIMAL; }

	public static double get(double input_) { return NUMBERS_DECIMAL; }

	public static Long get(Long input_) { return (Long)NUMBERS_LONG; }

	public static long get(long input_) { return NUMBERS_LONG; }

	public static Integer get(Integer input_) { return(Integer)NUMBERS_INT; }

	public static int get(int input_) { return NUMBERS_INT; }

	public static Boolean get(Boolean input_) { return (Boolean)BOOLEAN; }

	public static boolean get(boolean input_) { return BOOLEAN; }

	public static Byte get(Byte input_) { return (Byte)BYTE; }

	public static byte get(byte input_) { return BYTE; }

	public static Character get(Character input_) { return (Character)CHAR; }

	public static char get(char input_) { return CHAR; }

	public static double[] get(double[] input_) { return null; }

	public static long[] get(long[] input_) { return null; }

	public static int[] get(int[] input_) { return null; }

	public static boolean[] get(boolean[] input_) { return null; }

	public static byte[] get(byte[] input_) { return null; }

	public static char[] get(char[] input_) { return null; }

	public static <x> Object get(Object input_) { return null; }

	public static Object get(Class<?> type_)
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