package accessory;

public class xy extends parent
{
	public static final String SEPARATOR = ", ";
	
	public static final int DEFAULT_MIN = 0;
	public static final int DEFAULT_MAX = numbers.MAX_INT;
	public static final int DEFAULT_WRONG = DEFAULT_MIN - 1;
	
	private int _x = DEFAULT_WRONG;
	private int _y = DEFAULT_WRONG;
	private int _min = DEFAULT_MIN;
	private int _max = DEFAULT_MAX;
	
	public xy(xy input_) { instantiate(input_); }

	public xy(int x_, int y_) { instantiate(x_, y_, DEFAULT_MIN, DEFAULT_MAX); }

	public xy(int x_, int y_, int min_, int max_) { instantiate(x_, y_, min_, max_); }

	public int get_x() { return _x; }

	public int get_y() { return _y; }
	
	public int get_min() { return _min; }

	public int get_max() { return _max; }
	
	public String serialise() { return toString(); }
	
	public String toString() 
	{ 
		String output = strings.DEFAULT;
		
		if (is_ok()) output = _x + SEPARATOR + _y;
		
		return output; 
	}

	public boolean equals(xy xy2_) { return (is_ok(xy2_) ? (_x == xy2_._x && _y == xy2_._y && _min == xy2_._min && _max == xy2_._max) : false); }
	
	public boolean is_ok() { return is_ok(_x, _y, _min, _max); }

	private void instantiate(xy input_)
	{
		instantiate_common();
		
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._x, input_._y, input_._min, input_._max);
	}
	
	private void instantiate(int x_, int y_, int min_, int max_)
	{
		instantiate_common();
		
		if (!is_ok(x_, y_, min_, max_)) return;

		populate(x_, y_, min_, max_);
	}

	private boolean is_ok(int x_, int y_, int min_, int max_) { return (numbers.is_ok(x_, min_, max_) && numbers.is_ok(y_, min_, max_)); }	

	private void populate(int x_, int y_, int min_, int max_)
	{
		_x = x_;
		_y = y_;
		_min = min_;
		_max = max_;
	}
}