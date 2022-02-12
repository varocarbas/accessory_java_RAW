package accessory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class arrays 
{
	public static final Object DEFAULT = defaults.get_generic();
	
	static { _ini.load(); }
	
	public static boolean is_ok(Object input_)
	{
		return (get_size(input_) > 0);
	}
	
	public static <x, y> boolean is_ok(HashMap<x, y> input_)
	{
		return (get_size(input_) > 0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <x> int get_size(Object input_)
	{
		int size = 0;
		if (input_ == null || !generic.is_array(input_)) return size;
		
		Class type = generic.get_class(input_);
		
		if (type.equals(Array.class)) size = get_size_array((x[])input_);
		else if (type.equals(ArrayList.class)) size = get_size_arraylist((ArrayList<x>)input_);
		else if (type.equals(HashMap.class)) size = get_size_hashmap_xx((HashMap<x, x>)input_); 
		
		return size;
	}
	
	public static <x, y> int get_size(HashMap<x, y> input_)
	{
		return (input_ == null ? 0 : get_size_hashmap_xy(input_));		
	}
			
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <x> x[] to_array(ArrayList<x> input_)
	{
		x[] output = (x[])DEFAULT;
		if (!is_ok(input_)) return output;
		
		Class type = generic.get_class(input_.get(0));
		if (type.equals(generic.DEFAULT)) return output;
		
		int size = input_.size();
		if (type.equals(String.class)) output = (x[])input_.toArray(new String[size]);
		else if (type.equals(Integer.class)) output = (x[])input_.toArray(new Integer[size]);
		else if (type.equals(Double.class)) output = (x[])input_.toArray(new Double[size]);
		else if (type.equals(Boolean.class)) output = (x[])input_.toArray(new Boolean[size]);
		
		return output;
	}
	
	public static <x> ArrayList<x> to_arraylist(x[] input_)
	{
		return (is_ok(input_) ? new ArrayList<x>(Arrays.asList(input_)) : new ArrayList<x>());
	}

	public static <x> String[] new_instance(String[] input_)
	{
		return to_array(to_arraylist(input_));
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> clean(ArrayList<String> input_, boolean trim_, boolean remove_wrong_)
	{
		if (!is_ok(input_)) return (ArrayList<String>)DEFAULT;
		
		ArrayList<String> output = new ArrayList<String>();
		
		for (String item: input_)
		{
			output = clean_internal(output, item, trim_, remove_wrong_);
		}
		
		return output;
	}
	
	public static String[] clean(String[] input_, boolean trim_, boolean remove_wrong_)
	{
		if (!is_ok(input_)) return (String[])DEFAULT;
		
		ArrayList<String> output = new ArrayList<String>();
		
		for (String item: input_)
		{
			output = clean_internal(output, item, trim_, remove_wrong_);
		}
		
		return to_array(output);
	}
	
	@SuppressWarnings("unchecked")
	public static <x> x[] get_range(x[] input_, int start_i, int size_)
	{
		int size0 = get_size(input_);
		int size = (size_ <= 0 ? size0 : start_i + size_);
		
		return 
		(
			(start_i < 0 || size > size0) ? (x[])DEFAULT : 
			Arrays.copyOfRange(input_, start_i, size)
		);
	}

	@SuppressWarnings("unchecked")
	public static <x, y> y get_value(HashMap<x, y> array_, x key_)
	{
		return (y)
		(
			!arrays.is_ok(array_) ? generic.DEFAULT : 
			key_value_get_exists(array_, key_, null, true, true)
		);
	}

	@SuppressWarnings("unchecked")
	public static <x> x get_value(Object array_, x key_)
	{
		return (x)
		(
			!arrays.is_ok(array_) ? generic.DEFAULT : 
			key_value_get_exists(array_, key_, false, true)
		);
	}
	
	public static <x, y> boolean keys_exist(HashMap<x, y> array_, x[] keys_)
	{
		if (!arrays.is_ok(array_) || !arrays.is_ok(keys_)) return false;
		
		for (x key: keys_)
		{
			if (key_exists(array_, key)) return true;
		}
		
		return false;
	}

	public static <x, y> boolean key_exists(HashMap<x, y> array_, x key_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(key_)) ? false : 
			(boolean)key_value_get_exists(array_, key_, null, true, false)
		);	
	}
	
	public static <x> boolean keys_exist(Object array_, x[] keys_)
	{
		if (!arrays.is_ok(array_) || !arrays.is_ok(keys_)) return false;
		
		for (x key: keys_)
		{
			if (key_exists(array_, key)) return true;
		}
		
		return false;
	}
	
	public static <x> boolean key_exists(Object array_, x key_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(key_)) ? false : 
			(boolean)key_value_get_exists(array_, key_, true, false)
		);		
	}

	public static <x, y> boolean value_exists(HashMap<x, y> array_, y value_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(value_)) ? false : 
			(boolean)key_value_get_exists(array_, null, value_, false, false)
		);
	}

	public static <x> boolean value_exists(Object array_, x value_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(value_)) ? false : 
			(boolean)key_value_get_exists(array_, value_, false, false)
		);
	}

	public static String to_string(String[] input_, String separator_)
	{
		if (!arrays.is_ok(input_)) return strings.DEFAULT;
	
		String output = "";
		String separator = (strings.is_ok(separator_) ? separator_ : ", ");

		for (String item: input_)
		{
			if (!strings.is_ok(item)) continue;
			if (!output.equals("")) output += separator;
		
			output += item;
		}
		
		return output;
	}	

	public static String to_string
	(
		HashMap<String, String> input_, String separator1_, 
		String separator2_, String[] keys_ignore_
	)
	{
		if (!arrays.is_ok(input_)) return strings.DEFAULT;
		
		String output = "";
		String separator1 = (strings.is_ok(separator1_) ? separator1_ : ", ");
		String separator2 = (strings.is_ok(separator2_) ? separator2_ : ": ");
		
		for (Entry<String, String> item: input_.entrySet())
		{
			String key = item.getKey();
			String value = item.getValue();
			if 
			(
				!strings.is_ok(key) || !strings.is_ok(value) || 
				value_exists(keys_ignore_, key)
			) 
			{ continue; }
			
			if (!output.equals("")) output += separator1;
			output += (key + separator2 + value);
		}
		
		return output;
	}
	
	@SuppressWarnings("rawtypes")
	public static final Class[] get_all_classes()
	{
		return new Class[] { HashMap.class, ArrayList.class, Array.class };
	}

	private static <x> int get_size_array(x[] input_)
	{
		return input_.length;
	}
	
	private static <x> int get_size_arraylist(ArrayList<x> input_)
	{
		return input_.size();
	}
	
	private static <x> int get_size_hashmap_xx(HashMap<x, x> input_)
	{
		return input_.size();
	}
	
	private static <x, y> int get_size_hashmap_xy(HashMap<x, y> input_)
	{
		return input_.size();
	}
	
	private static ArrayList<String> clean_internal(ArrayList<String> output_, String item_, boolean trim_, boolean remove_wrong_)
	{
		boolean add = true;
		
		String item2 = item_;
		if (strings.is_ok(item2))
		{
			if (trim_) item2 = item2.trim();
		}
		else if (remove_wrong_) add = false;
		
		if (add) output_.add(item2);
	
		return output_;
	}

	private static <x, y> Object key_value_get_exists(HashMap<x, y> array_, x key_, y value_, boolean is_key_, boolean get_)
	{
		for (Entry<x, y> item: array_.entrySet())
		{
			x key = item.getKey();
			y value = item.getValue();

			if ((is_key_ && generic.are_equal(key_, key)) || (!is_key_ && generic.are_equal(value_, value))) 
			{
				return (get_ ? (is_key_ ? value : key) : true);
			}
		}

		return (get_ ? generic.DEFAULT : false);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <x> Object key_value_get_exists(Object array_, x target_, boolean is_key_, boolean get_)
	{
		Class type = generic.get_class(array_);
		if (generic.is_ok(type)) return false;
		
		if (type.equals(HashMap.class))
		{
			for (Entry<x, x> item: ((HashMap<x, x>)array_).entrySet())
			{
				x key = item.getKey();
				x val = item.getValue();
				x target2 = (is_key_ ? key : val);
				
				if (generic.are_equal(target_, target2)) 
				{
					return (get_ ? (is_key_ ? val : key) : true);
				}
			}
		}
		else
		{
			for (x item: (x[])array_)
			{
				if (generic.are_equal(target_, item)) return (get_ ? item : true);
			}		
		}

		return false;
	}
}