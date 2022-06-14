package accessory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public abstract class numbers extends parent_static 
{
	public static final String PERC_REF_LARGER = types.CONFIG_NUMBERS_PERC_REF_LARGER;
	
	public static final double MIN_DECIMAL = -1 * Double.MAX_VALUE; //!!!
	public static final long MIN_LONG = Long.MIN_VALUE;
	public static final int MIN_INT = Integer.MIN_VALUE;
	public static final int MIN_DIGITS_DECIMALS = 0;

	public static final double MAX_DECIMAL = Double.MAX_VALUE;
	public static final long MAX_LONG = Long.MAX_VALUE;
	public static final int MAX_INT = Integer.MAX_VALUE;
	public static final int MAX_DIGITS_DECIMAL = 308;
	public static final int MAX_DIGITS_LONG = 19;
	public static final int MAX_DIGITS_INT = 10;	
	public static final int MAX_DECIMALS = 10;

	public static final double DEFAULT_DECIMAL = _defaults.NUMBERS_DECIMAL;
	public static final long DEFAULT_LONG = _defaults.NUMBERS_LONG;
	public static final int DEFAULT_INT = _defaults.NUMBERS_INT;
	public static final int DEFAULT_DECIMALS = _defaults.NUMBERS_DECIMALS;

	public static final double DEFAULT_PERC_SIMILAR = 0.1;
	public static final boolean DEFAULT_PERC_REF_LARGER = true;
	public static final int DEFAULT_ROUND_DECIMALS = DEFAULT_DECIMALS;
	public static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.HALF_UP;
	
	public static RoundingMode _round_mode = DEFAULT_ROUND_MODE;
	public static int _round_decimals = DEFAULT_ROUND_DECIMALS;
	
	public static String get_class_id() { return types.get_id(types.ID_NUMBERS); }

	public static final Class<?>[] get_all_classes() { return _alls.NUMBERS_CLASSES; }

	public static boolean is_ok(double input_, double min_, double max_, double ignore_) { return ((input_ >= min_ || min_ == ignore_) && (input_ <= max_ || max_ == ignore_)); }

	public static boolean is_ok(double input_, double min_, double max_) { return (input_ >= min_ && input_ <= max_); }

	public static boolean is_ok(long input_, long min_, long max_, long ignore_) { return ((input_ >= min_ || min_ == ignore_) && (input_ <= max_ || max_ == ignore_)); }

	public static boolean is_ok(long input_, long min_, long max_) { return (input_ >= min_ && input_ <= max_); }

	public static boolean is_ok(int input_, int min_, int max_, int ignore_) { return ((input_ >= min_ || min_ == ignore_) && (input_ <= max_ || max_ == ignore_)); }

	public static boolean is_ok(int input_, int min_, int max_) { return (input_ >= min_ && input_ <= max_); }

	public static boolean is_ok(Object input_) { return generic.is_number(input_); }

	public static double to_number(Object input_)
	{
		double output = 0.0;

		Class<?> type = generic.get_class(input_);

		if (generic.are_equal(type, Double.class)) output = (double)input_;
		else if (generic.are_equal(type, Integer.class)) output = (double)((int)input_); //!!!
		else if (generic.are_equal(type, Long.class)) output = (double)((long)input_); //!!!
		else if (generic.are_equal(type, Boolean.class)) output = (double)to_int((boolean)input_);
		else if (generic.are_equal(type, String.class)) output = to_decimal((String)input_);

		return output;
	}

	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (!generic.is_number(class_)) return output;

		if (generic.are_equal(class_, Double.class)) output = get_random_decimal(MIN_DECIMAL, MAX_DECIMAL);
		else if (generic.are_equal(class_, Long.class)) output = get_random_long(MIN_LONG, MAX_LONG);
		else if (generic.are_equal(class_, Integer.class)) output = get_random_int(MIN_INT, MAX_INT);

		return output;
	}

	public static int get_random_index(int max_i_) { return get_random_int(0, max_i_); }

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
		double output = (new Random()).nextDouble();

		output = (min_ + output * (max_ - min_));

		if (output < min_) output = min_;
		if (output > max_) output = max_;

		return output;
	}

	public static int get_length(int input_) { return get_length((double)input_); }

	public static int get_length(long input_) { return get_length((double)input_); }

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

	public static double to_decimal(String input_) { return strings.to_number_decimal(input_); }

	public static long to_long(double input_)
	{
		double output = Math.floor(input_);

		return (long)((output >= (double)MIN_LONG && output <= (double)MAX_LONG) ? output : DEFAULT_LONG);	
	}

	public static long to_long(String input_) { return strings.to_number_long(input_); }

	public static int to_int(double input_)
	{	
		double output = Math.floor(input_);

		return (int)((output >= (double)MIN_INT && output <= (double)MAX_INT) ? output : DEFAULT_INT);
	}

	public static int to_int(boolean input_) { return (input_ ? 1 : 0); }

	public static int to_int(String input_) { return strings.to_number_int(input_); }

	public static boolean to_boolean(int input_) { return (input_ == 1); }

	public static int from_boolean(boolean input_) { return (input_ ? 1 : 0); }

	public static String to_string_decimal(double input_, boolean to_int_) { return (to_int_ ? to_string_decimal_integer(input_) : Double.toString(input_)); }

	public static String to_string_long(long input_) { return Long.toString(input_); }

	public static String to_string_int(int input_) { return Integer.toString(input_); }

	public static boolean are_similar(double val1_, double val2_) { return are_similar(val1_, val2_, DEFAULT_PERC_SIMILAR); }

	public static boolean are_similar(double val1_, double val2_, double perc_) { return (get_perc_generic(val1_, val2_, false, true) <= Math.abs(perc_)); }

	public static double get_perc(double val1_, double val2_) { return get_perc(val1_, val2_, false, false); }

	public static double get_perc(double val1_, double val2_, boolean round_, boolean out_abs_) { return get_perc_common(val1_, val2_, round_, out_abs_, true); }

	public static double get_perc_generic(double val1_, double val2_) { return get_perc_generic(val1_, val2_, false, false); }

	public static double get_perc_generic(double val1_, double val2_, boolean round_, boolean out_abs_) { return get_perc_common(val1_, val2_, round_, out_abs_, true); }
	
	public static double get_perc_hist(double new_, double old_) { return get_perc_hist(new_, old_, false, false); }
	
	public static double get_perc_hist(double new_, double old_, boolean round_, boolean out_abs_) { return get_perc_common(new_, old_, round_, out_abs_, false); }

	public static double apply_perc(double val_, double perc_) { return apply_perc(val_, perc_, false); }

	public static double apply_perc(double val_, double perc_, boolean round_) 
	{ 
		double output = (val_ * (100.0 + perc_) / 100.0); 
		if (round_) output = round(output);
		
		return output;
	}

	public static double round(double val_) { return round(val_, _round_decimals, _round_mode); }
	
	static final Class<?>[] populate_all_classes() { return new Class<?>[] { Integer.class, int.class, Long.class, long.class, Double.class, double.class }; }	

	private static double get_perc_common(double new_, double old_, boolean round_, boolean out_abs_, boolean is_generic_)
	{
		double output = 0;
		if (old_ == 0) return output;
		
		double new2 = new_;
		double old2 = old_;
		
		if (is_generic_)
		{
			if ((boolean)config.get_numbers(PERC_REF_LARGER) && (Math.abs(new2) > Math.abs(old2)))
			{
				new2 = old_;
				old2 = new_;
			}
		}
		
		output = 100 * (new2 - old2) / old2;
		if (out_abs_ || is_generic_) output = Math.abs(output);
		
		if (round_) output = round(output);
		
		return output;
	}
	
	private static double round(double val_, int decimals_, RoundingMode mode_)
	{
		if (decimals_ == 0) return val_;
	
		RoundingMode mode = mode_;
		if (mode == null) mode = DEFAULT_ROUND_MODE;
		
		int decimals = decimals_;
		if (decimals < 0 || decimals > MAX_DECIMALS) decimals = DEFAULT_DECIMALS;
	
		return (new BigDecimal(val_)).setScale(decimals, mode_).doubleValue();
	}
	
	private static String to_string_decimal_integer(double input_)
	{
		String output = "";

		double input = input_;

		int length = get_length(input);
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