package accessory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class arrays 
{
	public static <Any> boolean is_ok(Any[] input_)
	{
		return (get_size(input_) > 0);
	}
	
	public static <Any> boolean is_ok(ArrayList<Any> input_)
	{
		return (get_size(input_) > 0);
	}
	
	public static <Any> boolean is_ok(HashMap<Any, Any> input_)
	{
		return (get_size(input_) > 0);
	}
	
	public static <Any> int get_size(Any[] input_)
	{
		return (input_ == null ? 0 : input_.length);
	}
	
	public static <Any> int get_size(ArrayList<Any> input_)
	{
		return (input_ == null ? 0 : input_.size());
	}
	
	public static <Any> int get_size(HashMap<Any, Any> input_)
	{
		return (input_ == null ? 0 : input_.size());
	}
	
	@SuppressWarnings("unchecked")
	public static <Any> Any[] to_array(ArrayList<Any> input_)
	{
		return (is_ok(input_) ? (Any[])input_.toArray(new String[input_.size()]) : get_default());
	}
	
	public static <Any> ArrayList<Any> to_arraylist(Any[] input_)
	{
		return (is_ok(input_) ? new ArrayList<Any>(Arrays.asList(input_)) : get_default());
	}
	
	public static ArrayList<String> clean(ArrayList<String> input_, boolean trim_, boolean remove_wrong_)
	{
		if (!is_ok(input_)) return get_default();
		
		ArrayList<String> output = new ArrayList<String>();
		
		for (String item: input_)
		{
			output = clean_internal(output, item, trim_, remove_wrong_);
		}
		
		return output;
	}
	
	public static String[] clean(String[] input_, boolean trim_, boolean remove_wrong_)
	{
		if (!is_ok(input_)) return get_default();
		
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
	
	public static <Any> Any[] get_range(Any[] input_, int start_i, int size_)
	{
		int size0 = get_size(input_);
		int size = (size_ <= 0 ? size0 : start_i + size_);
		
		return 
		(
			(start_i < 0 || size > size0) ? get_default() : 
			Arrays.copyOfRange(input_, start_i, size)
		);
	}
	
	@SuppressWarnings("unchecked")
	public static <Any> Any get_value(HashMap<Any, Any> array_, Any key_)
	{
		return 
		(
			key_exists(array_, key_) ? array_.get(key_) : 
			(Any)defaults.get_type(null, true)
		);
	}
	
	public static <Any> boolean keys_exist(HashMap<Any, Any> array_, Any[] keys_)
	{
		if (!arrays.is_ok(array_) || !arrays.is_ok(keys_)) return false;
		
		for (Any key: keys_)
		{
			if (!key_exists(array_, key)) return false;
		}
		
		return true;	
	}
	
	public static <Any> boolean key_exists(HashMap<Any, Any> array_, Any key_)
	{
		return key_value_exists(array_, key_, true);	
	}
	
	public static <Any> boolean value_exists(HashMap<Any, Any> array_, Any value_)
	{
		return key_value_exists(array_, value_, false);
	}
	
	public static <Any> boolean key_value_exists(HashMap<Any, Any> array_, Any target_, boolean is_key_)
	{
		if (target_ == null || !is_ok(array_)) return false;
		
		boolean is_string = (target_ instanceof String);
		
		for (Entry<Any, Any> item: array_.entrySet())
		{
			Any target2 = (is_key_ ? item.getKey() : item.getValue());
			if (target2 == null) continue;
			
			if (is_string && target2.equals(target_)) return true;
		}
		
		return false;
	}
	
	public static <Any> Any get_default()
	{
		return defaults.get_generic();
	}
		
	public static String to_string(String[] input_, String separator_)
	{
		if (!arrays.is_ok(input_)) return strings.get_default();
	
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
	
	public static String to_string(HashMap<String, String> input_, String separator1_, String separator2_)
	{
		if (!arrays.is_ok(input_)) return strings.get_default();
		
		String output = "";
		String separator1 = (strings.is_ok(separator1_) ? separator1_ : ", ");
		String separator2 = (strings.is_ok(separator2_) ? separator2_ : ": ");
		
		for (Entry<String, String> item: input_.entrySet())
		{
			String key = item.getKey();
			String value = item.getValue();
			if (!strings.is_ok(key) || !strings.is_ok(value)) continue;
			
			if (output != "") output += separator1;
			output += (key + separator2 + value);
		}
		
		return output;
	}
}