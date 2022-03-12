package accessory;

import java.util.Random;

public class numbers 
{
	public static final double DEFAULT_DEC = 0.0;
	public static final double MIN_DEC = -1 * Double.MAX_VALUE; //!!!
	public static final double MAX_DEC = Double.MAX_VALUE;
	public static final int MAX_DIGITS_DEC = 308;

	public static final long DEFAULT_LONG = 0;
	public static final long MIN_LONG = Long.MIN_VALUE;
	public static final long MAX_LONG = Long.MAX_VALUE;
	public static final int MAX_DIGITS_LONG = 19;

	public static final int DEFAULT_INT = 0;
	public static final int MIN_INT = Integer.MIN_VALUE;
	public static final int MAX_INT = Integer.MAX_VALUE;
	public static final int MAX_DIGITS_INT = 10;
	
	static { ini.load(); }

	public static final Class<?>[] get_all_classes()
	{
		return new Class<?>[] { Integer.class, int.class, Long.class, long.class, Double.class, double.class };
	}

	public static boolean is_ok(double input_, double min_, double max_)
	{
		return ((input_ >= min_ || min_ == MIN_DEC) && (input_ <= max_ || max_ == MAX_DEC));
	}

	public static boolean is_ok(long input_, long min_, long max_)
	{
		return ((input_ >= min_ || min_ == MIN_LONG) && (input_ <= max_ || max_ == MAX_LONG));
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
	
	public static <x> double to_number(x input_)
	{
		double output = 0.0;
		
		Class<?> type = generic.get_class(input_);
		
		if (generic.are_equal(type, Double.class)) output = (double)input_;
		else if (generic.are_equal(type, Integer.class)) output = (double)((int)input_); //!!!
		else if (generic.are_equal(type, Long.class)) output = (double)((long)input_); //!!!
		
		return output;
	}
	
	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (!generic.is_number(class_)) return output;
		
		if (generic.are_equal(class_, Double.class)) output = get_random_decimal(MIN_DEC, MAX_DEC);
		else if (generic.are_equal(class_, Long.class)) output = get_random_long(MIN_LONG, MAX_LONG);
		else if (generic.are_equal(class_, Integer.class)) output = get_random_int(MIN_INT, MAX_INT);

		return output;
	}
	
	public static int get_random_index(int max_i_)
	{
		return get_random_int(0, max_i_);
	}
	
	public static int get_random_int(int min_, int max_)
	{
		if (min_ > max_) return 0;
		if (min_ == max_) return min_;
		if (min_ != 0) return (int)get_random_decimal((double)min_, (double)max_);
		
		Random random = new Random();
		int output = random.nextInt(max_);
		
		if (output < min_) output = min_;
		if (output > max_) output = max_;
		
		return output;
	}
	
	public static long get_random_long(long min_, long max_)
	{		
		if (min_ > max_) return 0;
		if (min_ == max_) return min_;
		
		return (long)get_random_decimal((double)min_, (double)max_);
	}
	
	public static double get_random_decimal(double min_, double max_)
	{
		return get_random_decimal(new size(min_, max_, defaults.SIZE_DECIMALS));
	}
	
	public static double get_random_decimal(size size_)
	{
		double output = 0.0;
		if (!size.is_ok(size_)) return output;
		
		Random random = new Random();
		output = random.nextDouble();
		
		output = (size_._min + output * (size_._max - size_._min));
		
		if (output < size_._min) output = size_._min;
		if (output > size_._max) output = size_._max;
		
		return output;
	}
	
	public static int get_length(int input_)
	{
		return get_length((double)input_);
	}
	
	public static int get_length(long input_)
	{
		return get_length((double)input_);
	}
	
	public static int get_length(double input_)
	{
		int length = 1;
		
		double input = Math.abs(input_);
		if (input < 10.0) return length;
		
		while (input >= 10.0)
		{
			input /= 10.0;
			length++;
		}
		
		return length;
	}
	
	public static String to_integer_string(double input_)
	{
		String output = "";

		double input = input_;
		int length = get_length(input_);
		boolean is_negative = (input < 0);
		if (length < MAX_DIGITS_LONG) return Long.toString((long)input);

		input = Math.abs(input);
		
		while (input >= 1)
		{
			output = (int)(input % 10) + output;
			input /= 10;
		}
		
		if (is_negative) output = "-" + output;	
		
		return output;
	}
}