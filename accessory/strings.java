package accessory;

import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

public class strings 
{
	public static final String DEFAULT = defaults.STRING;
	
	public static final int SIZE_DEFAULT = defaults.SIZE_STRING;
	public static final int SIZE_SMALL = 10;
	public static final int SIZE_BIG = 500;

	static { ini.load(); }

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

	public static int get_length(String string_)
	{
		return get_length(string_, false);
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

		if (!is_ok(string_)) string = DEFAULT;
		else string = string.trim().toLowerCase();

		return string;
	}

	public static boolean matches_all(String string_, String[] targets_, boolean normalise_)
	{
		return matches(string_, targets_, normalise_, true);
	}

	public static boolean matches_any(String string_, String[] targets_, boolean normalise_)
	{
		return matches(string_, targets_, normalise_, false);
	}

	public static boolean are_equal(String string1_, String string2_)
	{
		boolean is_ok1 = is_ok(string1_);
		boolean is_ok2 = is_ok(string2_);
		
		return ((!is_ok1 || !is_ok2) ? (is_ok1 == is_ok2) : string1_.equals(string2_));
	}

	public static boolean are_equivalent(String string1_, String string2_)
	{
		return are_equal(normalise(string1_), normalise(string2_));
	}

	public static boolean contains_outside(String needle_, String haystack_, boolean normalise_, String start_, String end_)
	{
		return (index_of_outside(needle_, haystack_, normalise_, start_, end_) > -1);
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
		String output = DEFAULT;

		int length0 = get_length(string_, false);
		if 
		(
			length0 < 1 || (((long)start_ + (long)length_) > (long)numbers.MAX_INT) || 
			start_ < 0 || start_ + length_ > length0
		) 
		{ return output; }

		return (length_ > 0 ? string_.substring(start_, start_ + length_) : string_.substring(start_));
	}

	public static String[] split(String haystack_, String regex_)
	{
		return split(haystack_, regex_, false, 0, false, false);
	}
	
	public static String[] split(String haystack_, String regex_, boolean trim_)
	{
		return split(haystack_, regex_, false, 0, trim_, false);
	}
	
	public static String[] split(String haystack_, String regex_, boolean trim_, boolean remove_wrong_)
	{
		return split(haystack_, regex_, false, 0, trim_, remove_wrong_);
	}
	
	public static String[] split(String haystack_, String regex_, boolean normalise_, int max_size_, boolean trim_, boolean remove_wrong_)
	{
		String[] output = null; 

		try
		{
			if 
			(
				haystack_ == null || regex_ == null ||
				(
					is_ok(haystack_) && is_ok(regex_) && 
					!strings.contains(regex_, haystack_, normalise_)
				)
			) 
			{ return output; }
			
			String haystack = haystack_;
			String regex = regex_;
			
			if (normalise_)
			{
				haystack = normalise(haystack);
				regex = normalise(regex);
			}			
			
			output = haystack.split(regex);
			int size = arrays.get_size(output);
			if (size < 2) return output;

			if (trim_ || remove_wrong_) output = arrays.clean(output, trim_, remove_wrong_);

			if (max_size_ > 1 && size > max_size_)
			{
				String[] temp = arrays.get_range(output, max_size_ - 1, 0);
				String[] temp2 = arrays.get_range(output, 0, max_size_ - 1);

				ArrayList<String> temp3 = arrays.to_arraylist(temp2);
				temp3.add(String.join(regex, temp));

				output = arrays.to_array(temp3);
			}			
		}
		catch (Exception e)
		{
			errors.manage(types.ERROR_STRING_SPLIT, e, new String[] { haystack_, regex_ }, false);

			output = null;
		}

		return output;
	}	

	public static String substring_before(String string_, String target_, int count_, boolean normalise_)
	{
		return substring_before_after(string_, target_, count_, normalise_, true);
	}

	public static String substring_after(String string_, String target_, int count_, boolean normalise_)
	{
		return substring_before_after(string_, target_, count_, normalise_, false);
	}	

	public static String get_random(int length_)
	{
		return get_random(length_, true, true, true);
	}

	public static String get_random(int length_, boolean upper_, boolean numbers_, boolean symbols_)
	{
		if (length_ < 1) return DEFAULT;

		String haystack = "abcdefghijklmnopqrstuvwxyz";
		if (upper_) haystack += haystack.toUpperCase();
		if (numbers_) haystack += "0123456789";
		if (symbols_) haystack += "~!@#$%^&*()-_=+[]{}|;:,.<>?";

		String output = "";
		int max = haystack.length() - 1;
		int count = 0;

		while (count < length_)
		{
			count++;
			output += substring(haystack, numbers.get_random_index(max), 1);
		}

		return output;
	}

	public static int index_of_outside(String needle_, String haystack_, boolean normalise_, String start_, String end_)
	{
		int output = index_of(needle_, haystack_, normalise_);
		if (output < 0 || !strings.is_ok(start_) || !strings.is_ok(end_)) return output;
		
		output = 0;
		
		while (true)
		{
			output = index_of(needle_, haystack_, output, normalise_);
			if (output < 0) return output;
			
			int start = index_of(start_, haystack_, normalise_);
			if (start >= 0 && start < output)
			{
				int end = index_of(end_, haystack_, output, normalise_);
				if (end >= 0) continue;
			}
			
			return output;
		}
	}
	
	public static int index_of(String needle_, String haystack_, boolean normalise_)
	{
		return index_of(needle_, haystack_, 0, normalise_);
	}
	
	public static int index_of(String needle_, String haystack_, int start_, boolean normalise_)
	{
		if (!is_ok(needle_) || !is_ok(haystack_)) return -1;

		String haystack = haystack_; 
		String needle = needle_;
		if (normalise_)
		{
			haystack = normalise(haystack);
			needle = normalise(needle);
		}

		return (start_ > 0 ? haystack.indexOf(needle, start_) : haystack.indexOf(needle));
	}

	public static boolean is_int(String string_)
	{
		return is_number_internal(string_, true, false);
	}

	public static boolean is_decimal(String string_)
	{
		return is_number_internal(string_, false, false);		
	}

	public static boolean is_long(String string_)
	{
		return is_number_internal(string_, true, true);		
	}

	public static boolean is_number(String string_)
	{
		return is_number_internal(string_, false, false);	
	}

	public static boolean is_boolean(String string_)
	{
		String[] targets = types.get_subtypes(types.DATA_BOOLEAN, null);

		String string = types.check_subtype(string_, targets, types.ACTIONS_ADD, types.DATA_BOOLEAN);
		
		for (String target: targets)
		{
			if (are_equal(target, string)) return true;
		}
		
		return false;
	}

	public static String from_number_decimal(double input_, boolean to_int_)
	{
		return (to_int_ ? numbers.to_integer_string(input_) : Double.toString(input_));
	}

	public static double to_number_decimal(String string_)
	{
		return (is_decimal(string_) ? Double.parseDouble(string_) : numbers.DEFAULT_DEC);
	}

	public static String from_number_long(long input_)
	{
		return Long.toString(input_);
	}

	public static long to_number_long(String string_)
	{
		return (is_long(string_) ? Long.parseLong(string_) : numbers.DEFAULT_LONG);
	}

	public static String from_number_int(int input_)
	{
		return Integer.toString(input_);
	}

	public static int to_number_int(String string_)
	{
		return (is_int(string_) ? Integer.parseInt(string_) : numbers.DEFAULT_INT);
	}

	public static String from_boolean(boolean input_)
	{
		return ((Boolean)input_).toString();
	}

	public static boolean to_boolean(String string_)
	{
		boolean output = false;

		String string = types.check_subtype(string_, types.get_subtypes(types.DATA_BOOLEAN, null), types.ACTIONS_ADD, types.DATA_BOOLEAN);
		
		if (are_equal(string, types.DATA_BOOLEAN_TRUE)) output = true;
		else if (are_equivalent(string_, types.DATA_BOOLEAN_FALSE)) output = false;

		return output;
	}

	public static String[] to_strings(double[] inputs_)
	{
		return to_strings((Double[])arrays.to_big(inputs_));
	}
	
	public static String[] to_strings(long[] inputs_)
	{
		return to_strings((Long[])arrays.to_big(inputs_));
	}
	
	public static String[] to_strings(int[] inputs_)
	{
		return to_strings((Integer[])arrays.to_big(inputs_));
	}
	
	public static String[] to_strings(boolean[] inputs_)
	{
		return to_strings((Boolean[])arrays.to_big(inputs_));
	}
	
	public static <x> String[] to_strings(x[] inputs_)
	{
		if (!arrays.is_ok(inputs_)) return null;

		ArrayList<String> output = new ArrayList<String>();

		for (x input: inputs_)
		{
			String temp = to_string(input);
			if (is_ok(temp)) output.add(temp);
		}

		return arrays.to_array(output);
	}

	public static <x, y> String to_string(HashMap<x, y> input_)
	{
		return arrays.to_string(input_, null, null, null);
	}
	
	public static String to_string(double[] input_)
	{
		return to_string(arrays.to_big(input_));
	}
	
	public static String to_string(long[] input_)
	{
		return to_string(arrays.to_big(input_));
	}
	
	public static String to_string(int[] input_)
	{
		return to_string(arrays.to_big(input_));
	}
	
	public static String to_string(boolean[] input_)
	{
		return to_string(arrays.to_big(input_));
	}
	
	public static String to_string(Object input_)
	{
		if (generic.is_string(input_)) 
		{
			String temp = (String)input_;
			
			return (is_ok(temp) ? temp : strings.DEFAULT);
		}

		String output = DEFAULT;
		if (!generic.is_ok(input_)) return output;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_ok(type)) return output;

		if (generic.is_array(type)) 
		{
			if (generic.are_equal(type, HashMap.class)) output = arrays.to_string(input_, null, null, null);
			else output = arrays.to_string(input_, null);
		}
		else if (generic.are_equal(type, Class.class)) output = ((Class<?>)input_).getSimpleName();
		else if (generic.are_equal(type, Method.class)) output = ((Method)input_).getName();
		else if (generic.are_equal(type, Exception.class)) output = ((Exception)input_).getMessage();
		else if (generic.are_equal(type, Double.class)) output = from_number_decimal((Double)input_, false);
		else if (generic.are_equal(type, Integer.class)) output = from_number_int((Integer)input_);
		else if (generic.are_equal(type, Long.class)) output = from_number_long((Long)input_);
		else if (generic.are_equal(type, Boolean.class)) output = from_boolean((Boolean)input_);
		else output = input_.toString();
		
		return output;
	}
	
	public static Object from_string(String string_, Class<?> type_)
	{
		Object output = null;
		if (!is_ok(string_)) return output;

		Class<?> type = type_;
		if (!generic.is_ok(type))
		{
			if (is_decimal(string_)) type = Double.class;
			else if (is_long(string_)) type = Long.class;
			else if (is_int(string_)) type = Integer.class;
			else if (is_boolean(string_)) type = Boolean.class;
			else return output;
		}

		if (generic.are_equal(type, Double.class)) output = to_number_decimal(string_);
		else if (generic.are_equal(type, Long.class)) output = to_number_long(string_);
		else if (generic.are_equal(type, Integer.class)) output = to_number_int(string_);
		else if (generic.are_equal(type, Boolean.class)) output = to_boolean(string_);

		return output;
	}

	public static String remove_escape_many(String[] needles_, String haystack_, boolean remove_)
	{
		if (!arrays.is_ok(needles_) || !is_ok(haystack_)) return DEFAULT;

		String output = haystack_;

		for (String needle: needles_)
		{
			output = remove_escape(needle, output, remove_);
		}

		return output;
	}

	public static String remove_escape(String needle_, String haystack_, boolean remove_)
	{
		if (!is_ok(needle_) || !is_ok(haystack_)) return DEFAULT;

		String output = haystack_;
		String replacement = (remove_ ? "" : "\\" + needle_);

		output = output.replace(needle_, replacement);

		return output;
	}

	private static boolean matches(String string_, String[] targets_, boolean normalise_, boolean all_)
	{
		if (!strings.is_ok(string_) || !arrays.is_ok(targets_)) return false;

		boolean is_ok = true;

		for (String target: targets_)
		{
			is_ok = (normalise_ ? are_equivalent(string_, target) : are_equal(string_, target));
			if ((all_ && !is_ok) || (!all_ && is_ok)) break;
		}

		return is_ok;
	}

	private static boolean is_number_internal(String string_, boolean is_integer_, boolean is_long_)
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

		int digit_count = 0;

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
					(is_decimal && (is_integer_ || i == 0 || i == last_i)) || decimal_found || 
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

			if (!decimal_found && Character.isDigit(chars[i])) digit_count++;
		}
		if (group_count != group_no && group_count != group_max) return false;

		int digit_limit = numbers.MAX_DIGITS_DEC;
		if (is_long_) digit_limit = numbers.MAX_DIGITS_LONG;
		else if (is_integer_) digit_limit = numbers.MAX_DIGITS_INT;

		if (digit_count > digit_limit) return false;

		boolean is_ok = true;

		if (digit_count == digit_limit) 
		{
			try
			{
				if (is_long_) Long.parseLong(string_);
				else if (is_integer_) Integer.parseInt(string_);
				else Double.parseDouble(string_);
			}
			catch (Exception e) { is_ok = false; }
		}

		return is_ok;
	}

	private static boolean contains_start_end(String needle_, String haystack_, boolean normalise_, boolean first_)
	{
		boolean contains = false;

		int length = get_length(haystack_, normalise_);
		int length2 = get_length(needle_, normalise_);
		if (length <= 0 || length2 <= 0) return contains;

		int i = index_of(needle_, haystack_, normalise_);
		if (first_) contains = (i == 0);
		else contains = (i == (length - length2));

		return contains;
	}

	private static String substring_before_after(String string_, String target_, int count_, boolean normalise_, boolean is_before_)
	{
		String output = DEFAULT;

		String[] temp = split(string_, target_, normalise_, 0, false, false);
		int size0 = arrays.get_size(temp);
		if (size0 <= count_ || count_ < 1) return output;

		int start_i = 0;
		int size = count_;

		if (!is_before_)
		{
			start_i = count_;
			size = 0;		
		}

		return String.join(target_, arrays.get_range(temp, start_i, size));
	}
}