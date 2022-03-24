package accessory;

public class size extends parent 
{				
	public static final long MIN_LONG = Long.MIN_VALUE;
	public static final double MIN_DECIMAL = numbers.MIN_DECIMAL;
	public static final int MIN_INT = Integer.MIN_VALUE;
	
	public static final double MAX_DECIMAL = numbers.MAX_DECIMAL;
	public static final long MAX_LONG = Long.MAX_VALUE;
	public static final int MAX_INT = Integer.MAX_VALUE;

	public static final int DEFAULT_DECIMALS = _defaults.SIZE_DECIMALS;
	public static final int DEFAULT_STRING = strings.DEFAULT_SIZE;
	
	public double _min = MIN_DECIMAL;
	public double _max = MAX_DECIMAL;
	public int _decimals = DEFAULT_DECIMALS;
	
	public static boolean is_ok(size input_)
	{
		return (input_ != null && input_.is_ok());
	}
	
	public static String to_string(size size_, boolean is_main_)
	{
		return (size_ != null ? size_.toString(is_main_) : strings.DEFAULT);	
	}
	
	public static boolean are_equal(size size1_, size size2_)
	{
		return are_equal_common(size1_, size2_);
	}
	
	public static boolean complies(size input_, size boundaries_)
	{
		if (!is_ok(input_) || !is_ok(boundaries_)) return false;

		return ((input_._min >= boundaries_._min) && (input_._max <= boundaries_._max));
	}

	public size(size input_)
	{
		if (input_ == null) return;
		
		instantiate(input_._min, input_._max, input_._decimals);
	}

	public size(double min_, double max_, int decimals_)
	{
		instantiate(min_, max_, decimals_);
	}

	public String toString()
	{
		return toString(true);
	}

	public boolean equals(size size2_)
	{
		return (!is_ok(size2_) ? false : (_min == size2_._min && _max == size2_._max && _decimals == size2_._decimals));		
	}

	public boolean is_ok()
	{
		 return is_ok(_min, _max, _decimals);
	}
	
	public String toString(boolean is_main_)
	{
		String[] brackets = 
		(
			is_main_ ? new String[] { misc.BRACKET_MAIN_OPEN, misc.BRACKET_MAIN_CLOSE } :
			new String[] { misc.BRACKET_SEC_OPEN, misc.BRACKET_SEC_CLOSE }
		);
		
		return (brackets[0] + arrays.to_string(new double[] { _min, _max, (double)_decimals }, misc.SEPARATOR_ITEM) + brackets[1]);
	}
	
	public size get_default(Class<?> class_)
	{
		size output = new size(MIN_DECIMAL, MAX_DECIMAL, DEFAULT_DECIMALS);
		if (!generic.is_ok(class_)) return output;
		
		if (generic.are_equal(class_, Integer.class)) output = new size(MIN_INT, MAX_INT, 0);
		else if (generic.are_equal(class_, Long.class)) output = new size(MIN_LONG, MAX_LONG, 0);
		else if (generic.are_equal(class_, String.class)) output = new size(0, DEFAULT_STRING, 0);
		
		return output;
	}
	
	protected void ini() { }
	
	private void instantiate(double min_, double max_, int decimals_)
	{
		instantiate_common();
		if (!is_ok(min_, max_, decimals_)) return;

		populate(min_, max_, decimals_);
	}
	
	private boolean is_ok(double min_, double max_, int decimals_)
	{
		return (min_ <= max_ && decimals_ >= 0);
	}	

	private void populate(double min_, double max_, int decimals_)
	{
		_is_ok = true;
		
		_min = min_;
		_max = max_;
		_decimals = decimals_;
	}
}