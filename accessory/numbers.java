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
	
	static { _ini.load(); }

	public static final Class<?>[] get_all_classes()
	{
		return new Class<?>[] { Integer.class, Long.class, Double.class };
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
	
	public static int get_random_index(int max_i_)
	{
		return (int)get_random(new size(0, max_i_), null);
	}
	
	public static double get_random_class(Class<?> class_)
	{
		return get_random(accessory.size.get_default(class_), null);
	}
	
	public static double get_random(size size_)
	{
		return get_random(size_);
	}
	
	public static double get_random(size size_, Random random_)
	{
		double output = 0.0;
		if (!size.is_ok(size_)) return output;
		
		Random random = (!generic.is_ok(random_) ? new Random() : random_);
		output = random.nextDouble();
		
		output = (size_._min + output * (size_._max - size_._min));
		
		if (output < size_._min) output = size_._min;
		if (output > size_._max) output = size_._max;
		
		return output;
	}
}