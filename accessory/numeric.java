package accessory;

public class numeric 
{
	static { _ini.load(); }
	
	public static boolean is_ok(double input_, double min_, double max_)
	{
		return
		(
			(input_ >= min_ || min_ == get_def_double(false)) &&
			(input_ <= max_ || max_ == get_def_double(true))				
		);
	}
	
	public static boolean is_ok(int input_, int min_, int max_)
	{
		return
		(
			(input_ >= min_ || min_ == get_def_int(false)) && 
			(input_ <= max_ || max_ == get_def_int(true))				
		);
	}
	
	public static int get_def_int(boolean max_)
	{
		return (int)defaults.get_type(Integer.class, max_);
	}
	
	public static double get_def_double(boolean max_)
	{
		return (double)defaults.get_type(Double.class, max_);
	}
}
