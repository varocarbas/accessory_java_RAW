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
		
		if (type == Array.class) size = get_size_array((x[])input_);
		else if (type == ArrayList.class) size = get_size_arraylist((ArrayList<x>)input_);
		else if (type == HashMap.class) size = get_size_hashmap_xx((HashMap<x, x>)input_); 
		
		return size;
	}
	
	public static <x, y> int get_size(HashMap<x, y> input_)
	{
		return (input_ == null ? 0 : get_size_hashmap_xy(input_));		
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
		
	public static String[] to_array(ArrayList<String> input_)
	{
		return (String[])(is_ok(input_) ? input_.toArray(new String[input_.size()]) : DEFAULT);
	}
		
	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> to_arraylist(x[] input_)
	{
		return (is_ok(input_) ? new ArrayList<x>(Arrays.asList(input_)) : (ArrayList<x>)DEFAULT);
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
	
	public static ArrayList<String> clean_internal(ArrayList<String> output_, String item_, boolean trim_, boolean remove_wrong_)
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
	
	public static <x> x get_value(HashMap<x, x> array_, x key_)
	{
		return (key_exists(array_, key_) ? array_.get(key_) : defaults.get_generic());
	}
	
	public static <x> boolean keys_exist(HashMap<x, x> array_, x[] keys_)
	{
		if (!arrays.is_ok(array_) || !arrays.is_ok(keys_)) return false;
		
		for (x key: keys_)
		{
			if (!key_exists(array_, key)) return false;
		}
		
		return true;	
	}
	
	public static <x> boolean key_exists(HashMap<x, x> array_, x key_)
	{
		return key_value_exists(array_, key_, true);	
	}
	
	public static <x> boolean value_exists(HashMap<x, x> array_, x value_)
	{
		return key_value_exists(array_, value_, false);
	}
	
	public static <x> boolean value_exists(x[] array_, x value_)
	{
		if (value_ == null || !is_ok(array_)) return false;
		
		boolean is_string = (value_ instanceof String);
		
		for (x val2: array_)
		{
			if 
			(
				(is_string && val2.equals(value_)) || 
				(!is_string && val2 == value_)
			) 
			{ return true; }
		}
		
		return false;
	}
		
	public static String to_string(String[] input_, String separator_)
	{
		if (!arrays.is_ok(input_)) return strings.DEFAULT;
	
		String output = "";
		String separator = (strings.is_ok(separator_) ? separator_ : ", ");

		for (String item: input_)
		{
			if (!strings.is_ok(item)) continue;
			if (output != "") output += separator;
		
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
			
			if (output != "") output += separator1;
			output += (key + separator2 + value);
		}
		
		return output;
	}
	
	@SuppressWarnings("rawtypes")
	public static final Class[] get_all_classes()
	{
		return new Class[] { HashMap.class, ArrayList.class, Array.class };
	}
	
	private static <x> boolean key_value_exists(HashMap<x, x> array_, x target_, boolean is_key_)
	{
		if (target_ == null || !is_ok(array_)) return false;
		
		boolean is_string = (target_ instanceof String);
		
		for (Entry<x, x> item: array_.entrySet())
		{
			x target2 = (is_key_ ? item.getKey() : item.getValue());
			if (target2 == null) continue;
			
			if 
			(
				(is_string && target2.equals(target_)) || 
				(!is_string && target2 == target_)
			) 
			{ return true; }
		}
		
		return false;
	}
}