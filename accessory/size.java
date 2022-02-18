package accessory;

public class size 
{
	public double _min = Double.MIN_VALUE;
	public double _max = Double.MAX_VALUE;

	public static boolean is_ok(size input_)
	{
		return (input_ != null);
	}

	public static boolean complies(size input_, size boundaries_)
	{
		if (!is_ok(input_) || !is_ok(boundaries_)) return false;

		return ((input_._min >= boundaries_._min) && (input_._max <= boundaries_._max));
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