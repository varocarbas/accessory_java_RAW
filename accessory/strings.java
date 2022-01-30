package accessory;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class strings 
{
	static { _ini.load(); }
	
	public static <x> boolean is_ok(x input_)
	{
		return (input_ instanceof String ? is_ok((String)input_) : false);
	}
	
	public static boolean is_ok(String string_)
	{
		return (get_length(string_, true) > 0);
	}
	
	public static boolean are_ok(String[] strings_)
	{
		if (!arrays.is_ok(strings_)) return false;
		
		for (String string: strings_)
		{
			if (!is_ok(string)) return false;
		}
		
		return true;
	}
	
	public static int get_length(String string_, Boolean trim_)
	{
		String string = string_;
		if (string == null) return 0;
		
		if (trim_) string = string.trim();
		
		return string.length();
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
	
	public static boolean are_equal(String string1_, String string2_)
	{
		return ((!is_ok(string1_) || !is_ok(string2_)) ? false : string1_.equals(string2_));
	}
	
	public static boolean are_equivalent(String string1_, String string2_)
	{
		return (normalise(string1_).equals(normalise(string2_)));
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
	
	public static String get_start(String string_, int length_)
	{
		return substring(string_, 0, length_);
	}
	
	public static String get_end(String string_, int start_)
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
	
	public static boolean is_integer(String string_)
	{
		return is_numeric_internal(string_, true);
	}
	
	public static boolean is_decimal(String string_)
	{
		return is_numeric_internal(string_, false);		
	}
	
	public static boolean is_numeric(String string_)
	{
		return is_numeric_internal(string_, false);	
	}

	public static String from_boolean(boolean input_)
	{
		return (input_ ? keys.TRUE : keys.FALSE);
	}
	
	public static boolean to_boolean(String string_, boolean default_)
	{
		boolean output = default_;
		
		if (are_equivalent(string_, keys.TRUE)) output = true;
		else if (are_equivalent(string_, keys.FALSE)) output = false;

		return output;
	}
	
	private static boolean is_numeric_internal(String string_, boolean integer_)
	{
		if (!is_ok(string_)) return false;
		
		char[] chars = string_.toCharArray();
		int last_i = chars.length - 1;
		
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		char decimal = symbols.getDecimalSeparator();
		char group = symbols.getGroupingSeparator();
		
		int group_no = -1;
		int group_max = 3;
		int group_count = -1;
		boolean decimal_found = false;
		
		for (int i = 0; i <= last_i; i++)
		{
			if (group_count > -1) group_count++;

			if (chars[i] == group || chars[i] == decimal)
			{
				boolean is_decimal = false;
				int group_max2 = group_max;
				if (chars[i] == decimal)
				{
					is_decimal = true;
					group_max2++;
				}
				
				if 
				(
					(is_decimal && (integer_ || i == 0 || i == last_i)) || decimal_found || 
					(group_count != group_no && group_count != group_max2)
				)
				{ 
					return false; 
				}
				else
				{
					group_count = 0;
					if (is_decimal) 
					{
						group_count = group_no;
						decimal_found = true;
					}
				}
			}
			else if (chars[i] == '-' || chars[i] == '+') 
			{
				if (i != 0) return false;
			}
			else if (!Character.isDigit(chars[i])) return false;
		}
		
		if (group_count != group_no && group_count != group_max) return false;
		
		return true;
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