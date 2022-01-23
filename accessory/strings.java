package accessory;

import java.util.ArrayList;

public class strings 
{
	public static boolean is_ok(String string_)
	{
		return (get_length(string_, true) > 0);
	}
	
	public static boolean are_equivalent(String string1_, String string2_)
	{
		return (normalise(string1_).equals(normalise(string2_)));
	}
	
	public static boolean are_equal(String string1_, String string2_)
	{
		return ((!is_ok(string1_) || !is_ok(string2_)) ? false : string1_.equals(string2_));
	}
	
	public static boolean contains(String needle_, String haystack_, boolean normalise_)
	{
		return (index_of(needle_, haystack_, normalise_) >= 0);
	}
	
	public static boolean contains_end(String needle_, String haystack_, boolean normalise_)
	{
		return contains_start_end(needle_, haystack_, normalise_,false);
	}
	
	public static boolean contains_start(String needle_, String haystack_, boolean normalise_)
	{
		return contains_start_end(needle_, haystack_, normalise_, true);
	}
	
	public static String starts(String string_, int length_)
	{
		return substring(string_, 0, length_);
	}
	
	public static String ends(String string_, int start_)
	{
		return substring(string_, start_, 0);
	}
	
	public static String substring(String string_, int start_, int length_)
	{
		int length0 = get_length(string_, false);
		if (length0 < 1 || start_ < 0 || start_ + length_ > length0) return get_default();
		
		return (length_ > 0 ? string_.substring(start_, start_ + length_) : string_.substring(start_));
	}
	
	public static String[] split
	(
		String haystack_, String regex_, boolean normalise_, 
		int max_size_, boolean trim_, boolean remove_wrong_
	)
	{
		String haystack = haystack_;
		String regex = regex_;
		if (!strings.is_ok(haystack) || !strings.is_ok(regex)) return arrays.get_default();
		
		if (normalise_)
		{
			haystack = normalise(haystack);
			regex = normalise(regex);
		}
		
		String[] output = haystack.split(regex);
		int size = arrays.get_size(output);
		if (size < 1) return arrays.get_default();
		
		if (trim_ || remove_wrong_) output = arrays.clean(output, trim_, remove_wrong_);
		
		if (max_size_ > 1 && size > max_size_)
		{
			String[] temp = arrays.get_range(output, max_size_ - 1, 0);
			String[] temp2 = arrays.get_range(output, 0, max_size_ - 1);

			ArrayList<String> temp3 = arrays.to_arraylist(temp2);
			temp3.add(String.join(regex, temp));
			
			output = arrays.to_array(temp3);
		}
		
		return output;
	}	

	public static int get_length(String string_, Boolean trim_)
	{
		String string = string_;
		if (string == null) return 0;
		
		if (trim_) string = string.trim();
		
		return string.length();
	}
	
	public static int index_of(String needle_, String haystack_, boolean normalise_)
	{
		if (!is_ok(needle_) || !is_ok(haystack_)) return -1;
		
		String haystack = haystack_; 
		String needle = needle_;
		if (normalise_)
		{
			haystack = normalise(haystack);
			needle = normalise(needle);
		}
		
		return haystack.indexOf(needle);
	}
	
	public static String normalise(String string_)
	{
		String string = string_;
		
		if (!is_ok(string_)) string = get_default();
		else string = string.trim().toLowerCase();
		
		return string;
	}
	
	public static String get_default()
	{
		return (String)defaults.get_type(String.class, false);
	}
	
	private static boolean contains_start_end(String needle_, String haystack_, boolean normalise_, boolean first_)
	{
		boolean contains = false;
		
		int length = get_length(haystack_, normalise_);
		int length2 = get_length(needle_, normalise_);
		if (length < 0 || length2 < 0) return contains;
		
		int i = index_of(needle_, haystack_, normalise_);		
		if (first_) contains = (i == 0);
		else contains = (i == (length - length2));
		
		return contains;
	}

}