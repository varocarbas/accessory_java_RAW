package accessory;

public class numbers 
{
	public static final double DEFAULT_DEC = (double)defaults.get_class(Double.class);
	public static final double MIN_DEC = Double.MIN_VALUE;
	public static final double MAX_DEC = Double.MAX_VALUE;
	public static final int MAX_DIGITS_DEC = 308;

	public static final int DEFAULT_INT = (int)defaults.get_class(Integer.class);
	public static final int MIN_INT = Integer.MIN_VALUE;
	public static final int MAX_INT = Integer.MAX_VALUE;
	public static final int MAX_DIGITS_INT = 10;

	static { _ini.load(); }

	@SuppressWarnings("rawtypes")
	public static final Class[] get_all_classes()
	{
		return new Class[] { Integer.class, Double.class };
	}

	public static boolean is_ok(double input_, double min_, double max_)
	{
		return ((input_ >= min_ || min_ == MIN_DEC) && (input_ <= max_ || max_ == MAX_DEC));
	}

	public static boolean is_ok(int input_, int min_, int max_)
	{
		return ((input_ >= min_ || min_ == MIN_INT) && (input_ <= max_ || max_ == MAX_INT));
	}

	public static <x> boolean is_ok(x input_)
	{
		return generic.is_number(input_);
	}

	public static Object get_default()
	{
		return 0;
	}
}