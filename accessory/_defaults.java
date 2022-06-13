package accessory;

public abstract class _defaults 
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
	public static final int NUMBERS_DECIMALS = 3;

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