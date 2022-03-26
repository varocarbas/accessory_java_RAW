package accessory;

public class size extends parent 
{	
	public static final double MIN = numbers.MIN_DECIMAL;
	public static final int MIN_DECIMALS = numbers.MIN_DIGITS_DECIMALS;
	
	public static final double MAX = numbers.MAX_DECIMAL;
	public static final int MAX_DECIMALS = numbers.MAX_DIGITS_DECIMALS;
	
	public static final double WRONG_MIN = numbers.DEFAULT_DECIMAL;
	public static final double WRONG_MAX = numbers.DEFAULT_DECIMAL;
	public static final int WRONG_DECIMALS = numbers.DEFAULT_INT;
	
	public static final double DEFAULT_MIN = numbers.MIN_DECIMAL;
	public static final double DEFAULT_MAX = numbers.MAX_DECIMAL;
	public static final int DEFAULT_DECIMALS = _defaults.SIZE_DECIMALS;

	public double _min = WRONG_MIN;
	public double _max = WRONG_MAX;
	public int _decimals = WRONG_DECIMALS;

	public static boolean are_equal(size size1_, size size2_)
	{
		return are_equal_common(size1_, size2_);
	}
	
	public static String to_string(size size_, boolean is_main_)
	{
		return (size_ != null ? size_.toString(is_main_) : strings.DEFAULT);	
	}
	
	public static boolean complies(size input_, size boundaries_)
	{
		if (!is_ok(input_) || !is_ok(boundaries_)) return false;

		return 
		(
			(input_._min >= boundaries_._min) && 
			(input_._max <= boundaries_._max) && 
			(input_._decimals == boundaries_._decimals)
		);
	}

	public size(size input_)
	{
		instantiate(input_);
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
		_is_ok = is_ok(_min, _max, _decimals);

		return _is_ok;
	}

	public String toString(boolean is_main_)
	{
		String[] brackets = 
		(
			is_main_ ? new String[] { misc.BRACKET_MAIN_OPEN, misc.BRACKET_MAIN_CLOSE } :
			new String[] { misc.BRACKET_SEC_OPEN, misc.BRACKET_SEC_CLOSE }
		);

		return (brackets[0] + arrays.to_string(new Object[] { _min, _max, _decimals }, misc.SEPARATOR_ITEM) + brackets[1]);
	}

	private void instantiate(size input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._min, input_._max, input_._decimals);
	}

	private void instantiate(double min_, double max_, int decimals_)
	{
		instantiate_common();
		if (!is_ok(min_, max_, decimals_)) return;

		populate(min_, max_, decimals_);
	}

	private boolean is_ok(double min_, double max_, int decimals_)
	{
		return (min_ <= max_ && numbers.is_ok(decimals_, MIN_DECIMALS, MAX_DECIMALS));
	}	

	private void populate(double min_, double max_, int decimals_)
	{
		_is_ok = true;

		_min = min_;
		_max = max_;
		_decimals = decimals_;
	}
}