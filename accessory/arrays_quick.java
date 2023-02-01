package accessory;

import java.util.Arrays;

//Use the methods in this class very carefully! No input validity checks are performed.
public abstract class arrays_quick 
{	
	public static boolean value_exists(int[] array_, int value_) { return (get_i(array_, value_) != arrays.WRONG_I); }

	public static boolean value_exists(String[] array_, String value_) { return (get_i(array_, value_) != arrays.WRONG_I); }

	public static int get_i(int[] array_, int value_)
	{
		for (int i = 0; i < array_.length; i++)
		{
			if (array_[i] == value_) return i;
		}
		
		return arrays.WRONG_I;
	}

	public static int get_i(String[] array_, String value_)
	{
		for (int i = 0; i < array_.length; i++)
		{
			if (array_[i].equals(value_)) return i;
		}
		
		return arrays.WRONG_I;
	}

	public static int get_i(String[][] array_, int i_, String value_) { return get_i(array_[i_], value_); }

	public static int[] get_new(int[] input_) { return Arrays.copyOfRange(input_, 0, input_.length); }

	public static String[] add(String[] array_, int i_, String value_)
	{
		if (i_ >= array_.length) array_ = arrays.redim(array_, i_ + 1);
		
		array_[i_] = value_;
		
		return array_;
	}
	
	public static double[] get_1d(double[][] array_, int i_) { return Arrays.copyOfRange(array_[i_], 0, array_[i_].length); }
}