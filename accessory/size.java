package accessory;

public class size 
{
	public double _min = Double.MIN_VALUE;
	public double _max = Double.MAX_VALUE;

	public String toString()
	{
		return toString(true);
	}
	
	public String toString(boolean is_main_)
	{
		String[] brackets = 
		(
			is_main_ ? new String[] { misc.BRACKET_MAIN_OPEN, misc.BRACKET_MAIN_CLOSE } :
			new String[] { misc.BRACKET_SEC_OPEN, misc.BRACKET_SEC_CLOSE }
		);
		
		return (brackets[0] + strings.to_string(_min) + misc.SEPARATOR_ITEM + strings.to_string(_max) + brackets[1]);
	}

	public boolean equals(size size2_)
	{
		return (!is_ok(size2_) ? false : (_min == size2_._min && _max == size2_._max));		
	}
	
	public static String to_string(size size_, boolean is_main_)
	{
		return (is_ok(size_) ? size_.toString(is_main_) : strings.DEFAULT);	
	}
	
	public static boolean are_equal(size size1_, size size2_)
	{
		return generic.are_equal(size1_, size2_);
	}
	
	public static boolean is_ok(size input_)
	{
		return (input_ != null && input_._min <= input_._max);
	}

	public static boolean complies(size input_, size boundaries_)
	{
		if (!is_ok(input_) || !is_ok(boundaries_)) return false;

		return ((input_._min >= boundaries_._min) && (input_._max <= boundaries_._max));
	}
	
	public static size get_default(Class<?> class_)
	{
		size output = new size(numbers.MIN_DEC, numbers.MAX_DEC);
		if (!generic.is_ok(class_)) return output;
		
		if (generic.are_equal(class_, Integer.class)) output = new size(numbers.MIN_INT, numbers.MAX_INT);
		else if (generic.are_equal(class_, Long.class)) output = new size(numbers.MIN_LONG, numbers.MAX_LONG);
		else if (generic.are_equal(class_, String.class)) output = new size(0, strings.SIZE_DEFAULT);
		
		return output;
	}
	
	public size(size input_)
	{
		if (!is_ok(input_)) return;

		_min = input_._min;
		_max = input_._max;
	}

	public size(double min_, double max_)
	{
		_min = min_;
		_max = max_;
	}
}