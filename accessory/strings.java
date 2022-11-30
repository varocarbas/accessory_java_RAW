package accessory;

import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class strings extends parent_static
{	
	public static final int MIN_SIZE = 0;
	public static final int MAX_SIZE = 65535;

	public static final int SIZE_SMALL = 10;
	public static final int SIZE_REGULAR = 100;
	public static final int SIZE_BIG = 500;

	public static final String DEFAULT = _defaults.STRINGS;
	public static final int DEFAULT_SIZE = _defaults.STRINGS_SIZE;

	public static boolean is_ok(String string_) { return is_ok(string_, false); }

	public static boolean are_ok(String[] strings_)
	{
		if (!arrays.is_ok(strings_)) return false;

		for (String string: strings_)
		{
			if (!is_ok(string)) return false;
		}

		return true;
	}

	public static int get_length(String string_) { return get_length(string_, false); }

	public static int get_length(String string_, Boolean trim_)
	{
		String string = string_;
		if (string == null) return 0;

		if (trim_) string = string.trim();

		return string.length();
	}

	public static String normalise(String string_) { return normalise(string_, false); }

	public static boolean matches_all(String string_, String[] targets_, boolean normalise_) { return matches(string_, targets_, normalise_, true); }

	public static boolean matches_any(String string_, String[] targets_, boolean normalise_) { return matches(string_, targets_, normalise_, false); }

	public static boolean are_equal(String string1_, String string2_)
	{
		boolean is_ok1 = is_ok(string1_, true);
		boolean is_ok2 = is_ok(string2_, true);

		return ((!is_ok1 || !is_ok2) ? (is_ok1 == is_ok2) : string1_.equals(string2_));
	}

	public static boolean are_equivalent(String string1_, String string2_) { return are_equal(normalise(string1_), normalise(string2_)); }

	public static boolean contains_outside(String needle_, String haystack_, boolean normalise_, String start_, String end_) { return (index_of_outside(needle_, haystack_, normalise_, start_, end_) > -1); }

	public static boolean contains(String needle_, String haystack_, boolean normalise_) { return (index_of(needle_, haystack_, normalise_) >= 0); }

	public static boolean contains_end(String needle_, String haystack_, boolean normalise_) { return contains_start_end(needle_, haystack_, normalise_, false); }

	public static boolean contains_start(String needle_, String haystack_, boolean normalise_) { return contains_start_end(needle_, haystack_, normalise_, true); }

	public static String get_start(String input_) { return get_start(input_, 1); }

	public static String get_start(String input_, int length_) { return substring(input_, 0, length_); }

	public static String get_end(String input_) { return get_end(input_, get_length(input_) - 1); }

	public static String get_end(String input_, int start_) { return substring(input_, start_, 0); }

	public static String truncate(String input_, int max_length_)
	{
		int length = get_length(input_);
				
		return ((max_length_ < 1 || length <= max_length_) ? input_ : substring(input_, 0, max_length_));
	}
	
	public static String substring(String input_, int start_, int length_)
	{
		String output = DEFAULT;

		int length0 = get_length(input_, false);
		if (length_ < 0 || length0 < 1 || start_ < 0 || start_ + length_ > length0 || (((long)start_ + (long)length_) > (long)numbers.MAX_INT)) return output; 

		return (length_ > 0 ? input_.substring(start_, start_ + length_) : input_.substring(start_));
	}

	public static String[] split(String needle_, String haystack_) { return split(needle_, haystack_, false); }

	public static String[] split(String needle_, String haystack_, boolean normalise_in_) { return split(needle_, haystack_, normalise_in_, false); }

	public static String[] split(String needle_, String haystack_, boolean normalise_in_, boolean only_first_) { return split(needle_, haystack_, normalise_in_, false, only_first_); }

	public static String[] split(String needle_, String haystack_, boolean normalise_in_, boolean normalise_out_, boolean only_first_)
	{
		int length = get_length(needle_);
		if (length < 0 || !is_ok(haystack_, true) || !contains(needle_, haystack_, normalise_in_)) return null; 

		ArrayList<String> output = new ArrayList<String>(); 

		int i = 0;

		while (true)
		{
			int temp = index_of(needle_, haystack_, i, normalise_in_);
			if (temp < 0) 
			{
				if (i == 0) return null;
				else
				{
					String out = substring_after(haystack_, i - 1);
					if (normalise_out_) out = normalise(out);					
					output.add(out);

					return arrays.to_array(output);					
				}
			}

			String out = substring_between(haystack_, i, temp, true);
			if (normalise_out_) out = normalise(out);			
			output.add(out);
			
			i = temp + length;

			if (only_first_)
			{
				out = substring_after(haystack_, i - 1);
				if (normalise_out_) out = normalise(out);
				output.add(out);

				return arrays.to_array(output);	
			}						
		}
	}	

	public static String substring_between(String string_, int start_, int end_, boolean end_excluded_)
	{
		int length = end_ - start_;
		if (!end_excluded_) length++;

		return substring(string_, start_, length);
	}

	public static String substring_before(String string_, int i_) { return substring_before_after(string_, i_, true); }

	public static String substring_before(String needle_, String haystack_, boolean normalise_) { return substring_before_after(needle_, haystack_, normalise_, true); }

	public static String substring_before(String needle_, String haystack_, boolean normalise_, int times_) { return substring_before_after(needle_, haystack_, normalise_, times_, true); }

	public static String substring_after(String string_, int i_) { return substring_before_after(string_, i_, false); }

	public static String substring_after(String needle_, String haystack_, boolean normalise_) { return substring_before_after(needle_, haystack_, normalise_, false); }

	public static String substring_after(String needle_, String haystack_, boolean normalise_, int times_) { return substring_before_after(needle_, haystack_, normalise_, times_, false); }	

	public static String get_random(int length_) { return get_random(length_, true, true, true); }

	public static String get_random(int length_, boolean upper_, boolean numbers_, boolean symbols_)
	{
		if (length_ < 1) return DEFAULT;

		String haystack = "abcdefghijklmnopqrstuvwxyz";
		if (upper_) haystack += haystack.toUpperCase();
		if (numbers_) haystack += "0123456789";
		if (symbols_) haystack += "~!@#$%^&*()-_=+[]{}|;:,.<>?'\"`/\\";

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
		if (!is_ok(start_, true) || !is_ok(end_, true)) return index_of(needle_, haystack_, normalise_);

		int output = 0;

		while (true)
		{
			output = index_of(needle_, haystack_, output, normalise_);
			if (output < 0) return output;

			int start = index_of(start_, haystack_, normalise_);
			if (start >= 0 && start < output)
			{
				int end = index_of(end_, haystack_, output, normalise_);
				if (end >= 0 && end > output) 
				{
					output++;

					continue;
				}
			}

			return output;
		}
	}

	public static int index_of(String needle_, String haystack_, boolean normalise_) { return index_of(needle_, haystack_, 0, normalise_); }

	public static int index_of(String needle_, String haystack_, int start_, boolean normalise_)
	{
		if (!is_ok(needle_, true) || !is_ok(haystack_, true)) return -1;

		String haystack = haystack_; 
		String needle = needle_;

		if (normalise_)
		{
			haystack = normalise(haystack, true);
			needle = normalise(needle, true);
		}

		return (start_ > 0 ? haystack.indexOf(needle, start_) : haystack.indexOf(needle));
	}

	public static boolean is_int(String string_) { return is_number(string_, true, false); }

	public static boolean is_decimal(String string_) { return is_number(string_, false, false); }

	public static boolean is_long(String string_) { return is_number(string_, true, true); }

	public static boolean is_number(String string_) { return is_number(string_, false, false); }

	public static boolean is_boolean(String string_)
	{
		String type = data.BOOLEAN;

		String[] targets = types.get_subtypes(type);
		String string = types.check_type(string_, targets, types.ACTION_ADD, type);

		for (String target: targets)
		{
			if (are_equal(target, string)) return true;
		}

		return false;
	}

	public static String from_number(double input_) { return numbers.to_string_decimal(input_, false); }

	public static String from_number_decimal(double input_, boolean to_int_) { return numbers.to_string_decimal(input_, to_int_); }

	public static String from_number_long(long input_) { return numbers.to_string_long(input_); }

	public static String from_number_int(int input_) { return numbers.to_string_int(input_); }

	public static double to_number(String string_) { return to_number_decimal(string_); }

	public static double to_number_decimal(String string_) 
	{ 
		if (!is_decimal(string_)) return numbers.DEFAULT_DECIMAL;

		String string = remove_thousands_sep(normalise(string_), null);
		for (char exp: get_all_exps()) { string = string.replace(exp, 'e'); }

		return Double.parseDouble(string); 
	}

	public static long to_number_long(String string_) 
	{ 
		long output = numbers.DEFAULT_LONG;
		if (!is_number(string_)) return output;
		
		return (is_long(string_) ? Long.parseLong(string_) : numbers.to_long(to_number_decimal(string_))); 
	}

	public static int to_number_int(String string_) 
	{ 
		int output = numbers.DEFAULT_INT;
		if (!is_number(string_)) return output;
		
		return (is_int(string_) ? Integer.parseInt(string_) : numbers.to_int(to_number_decimal(string_))); 
	}

	public static String from_boolean(int input_) { return from_boolean(numbers.to_boolean(input_)); }

	public static String from_boolean(boolean input_) { return ((Boolean)input_).toString(); }

	public static boolean to_boolean(String string_)
	{
		String string = normalise(string_);

		for (Entry<Boolean, String[]> item: get_all_booleans().entrySet())
		{
			for (String target: item.getValue())
			{
				if (are_equal(string, target)) return item.getKey();
			}
		}

		return false;
	}

	public static String to_string(double[] input_) { return to_string(arrays.to_big(input_)); }

	public static String to_string(long[] input_) { return to_string(arrays.to_big(input_)); }

	public static String to_string(int[] input_) { return to_string(arrays.to_big(input_)); }

	public static String to_string(boolean[] input_) { return to_string(arrays.to_big(input_)); }

	public static String to_string(byte[] input_) { return to_string(arrays.to_big(input_)); }

	public static String to_string(char[] input_) { return to_string(arrays.to_big(input_)); }

	public static String to_string(String input_) { return (is_ok(input_) ? input_ : DEFAULT); }

	public static String to_string(Object input_)
	{
		String output = DEFAULT;

		Class<?> type = generic.get_class(input_);
		if (type == null) return output;

		if (type.equals(double[].class)) output = to_string((double[])input_);
		else if (type.equals(long[].class)) output = to_string((long[])input_);
		else if (type.equals(int[].class)) output = to_string((int[])input_);
		else if (type.equals(boolean[].class)) output = to_string((boolean[])input_);
		else if (type.equals(byte[].class)) output = to_string((byte[])input_);
		else if (type.equals(char[].class)) output = to_string((char[])input_);
		else if (generic.is_array(type)) 
		{
			if (generic.are_equal(type, HashMap.class)) output = arrays.to_string(input_, null, null, null);
			else output = arrays.to_string(input_, null);
		}
		else if (generic.are_equal(type, Class.class)) output = ((Class<?>)input_).getSimpleName();
		else if (generic.are_equal(type, Method.class)) output = ((Method)input_).getName();
		else if (generic.are_equal(type, Exception.class)) output = ((Exception)input_).getMessage();
		else if (generic.are_equal(type, LocalDateTime.class)) output = dates.to_string((LocalDateTime)input_);
		else if (generic.are_equal(type, LocalDate.class)) output = dates.to_string((LocalDate)input_);
		else if (generic.are_equal(type, LocalTime.class)) output = dates.to_string((LocalTime)input_);
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
		if (type == null)
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

	public static String remove(String needle_, String haystack_) { return remove_escape_replace(needle_, haystack_, null, types.ACTION_REMOVE); }

	public static String remove(String[] needles_, String haystack_) { return remove_escape_replace_many(needles_, haystack_, null, types.ACTION_REMOVE); }

	public static String escape(String needle_, String haystack_) { return remove_escape_replace(needle_, haystack_, null, types.ACTION_ESCAPE); }

	public static String escape(String[] needles_, String haystack_) { return remove_escape_replace_many(needles_, haystack_, null, types.ACTION_ESCAPE); }

	public static String unescape(String needle_, String haystack_) { return remove_escape_replace(needle_, haystack_, null, types.ACTION_UNESCAPE); }

	public static String unescape(String[] needles_, String haystack_) { return remove_escape_replace_many(needles_, haystack_, null, types.ACTION_UNESCAPE); }

	public static String replace(String needle_, String haystack_, String replacement_) { return remove_escape_replace(needle_, haystack_, replacement_, types.ACTION_REPLACE); }

	public static String replace(String[] needles_, String haystack_, String replacement_) { return remove_escape_replace_many(needles_, haystack_, replacement_, types.ACTION_REPLACE); }

	static boolean is_ok(String string_, boolean minimal_) { return (minimal_ ? (string_ != null) : (get_length(string_, true) > 0)); }

	static HashMap<Boolean, String[]> populate_all_booleans()
	{
		HashMap<Boolean, String[]> booleans = new HashMap<Boolean, String[]>();

		booleans.put(true, new String[] { data.TRUE, from_boolean(true), from_number_int(numbers.to_int(true)) });
		booleans.put(false, new String[] { data.FALSE, from_boolean(false), from_number_int(numbers.to_int(false)) });

		return booleans;
	}

	static char[] populate_all_exps() { return new char[] { 'e', '^' }; }

	private static char[] get_all_exps() { return _alls.STRINGS_EXPS; }

	private static String remove_escape_replace_many(String[] needles_, String haystack_, String replacement_, String action_)
	{
		if (!is_ok(haystack_, true)) return DEFAULT;

		String output = haystack_;
		if (!arrays.is_ok(needles_)) return output;

		for (String needle: needles_) 
		{ 
			if (!is_ok(needle, true)) continue;

			output = remove_escape_replace(needle, output, replacement_, action_);
		}

		return output;
	}

	private static String remove_escape_replace(String needle_, String haystack_, String replacement_, String action_)
	{
		if (!is_ok(haystack_, true)) return DEFAULT;
		if (!is_ok(needle_, true)) return haystack_;
		
		if (action_.equals(types.ACTION_UNESCAPE)) return unescape_internal(needle_, haystack_);
		
		String replacement = get_replacement(needle_, replacement_, action_);

		return ((action_.equals(types.ACTION_ESCAPE) && haystack_.contains(replacement)) ? remove_escape_replace_escaped(needle_, haystack_, replacement) : remove_escape_replace_default(needle_, haystack_, replacement));
	}

	private static String remove_escape_replace_default(String needle_, String haystack_, String replacement_) { return haystack_.replace(needle_, replacement_); }
	
	private static String remove_escape_replace_escaped(String needle_, String haystack_, String replacement_)
	{
		String output = "";

		char[] chars = haystack_.toCharArray();				
		
		int last_i = chars.length - 1;	
		int i = 0;
		
		while (true)
		{
			int i2 = haystack_.indexOf(needle_, i);
			
			if (i2 < 0) 
			{
				output += strings.substring_after(haystack_, i - 1);
				
				break;
			}
			
			String temp = substring_between(haystack_, i, i2, false);
			if (i2 == 0 || chars[i2 - 1] != '\\') temp = remove_escape_replace_default(needle_, temp, replacement_);

			output += temp;
			
			i = i2 + 1;
			if (i > last_i) break;
		}
		
		return output;
	}
	
	private static String unescape_internal(String needle_, String haystack_)
	{
		ArrayList<Character> chars = arrays.to_arraylist(haystack_.toCharArray());				
		int last_i = chars.size() - 1;	

		char[] chars2 = needle_.toCharArray();				
		int length2 = chars2.length;
		
		char escape = '\\';
		int i = 0;

		while (true)
		{
			i = arrays.index_of(chars, escape, i, arrays.WRONG_I);
			if (i == arrays.WRONG_I) break;
			
			boolean found = true;
			
			for (int i2 = 0; i2 < length2; i2++)
			{
				int i3 = i2 + i;
				
				if (i3 > last_i || chars.get(i3) != chars2[i2]) 
				{
					found = false;
					
					break;
				}
			}
			
			if (found && i > 0)
			{
				if ((chars.get(i - 1) == escape) && (i == 1 || chars.get(i - 2) != escape))
				{
					chars.remove(i - 1);
					
					i--;
					last_i--;
				}
			}
					
			i++;
			if (i > last_i) break;
		}
		
		return (new String(arrays.to_small(arrays.to_array(chars))));
	}
	
	private static String get_replacement(String needle_, String replacement_, String action_)
	{
		String replacement = replacement_;	
		
		if (action_.equals(types.ACTION_REMOVE)) replacement = "";
		else if (action_.equals(types.ACTION_ESCAPE)) replacement = "\\" + needle_;
	
		return replacement;
	}
		
	private static HashMap<Boolean, String[]> get_all_booleans() { return _alls.STRINGS_BOOLEANS; }

	private static String normalise(String string_, boolean minimal_)
	{
		String string = string_;

		if (!is_ok(string_, minimal_)) string = DEFAULT;
		else 
		{
			if (!minimal_) string = string.trim();
			string = string.toLowerCase();
		}

		return string;
	}

	private static boolean matches(String string_, String[] targets_, boolean normalise_, boolean all_)
	{
		if (!is_ok(string_, true) || !arrays.is_ok(targets_)) return false;

		boolean is_ok = true;

		for (String target: targets_)
		{
			is_ok = (normalise_ ? are_equivalent(string_, target) : are_equal(string_, target));
			if ((all_ && !is_ok) || (!all_ && is_ok)) break;
		}

		return is_ok;
	}

	private static boolean is_number(String input_, boolean is_integer_, boolean is_long_)
	{
		if (!is_ok(input_)) return false;

		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		char dec_char = symbols.getDecimalSeparator();
		char[] exp_chars2 = get_all_exps();

		String input = remove_thousands_sep(normalise(input_), symbols); 
		if (!is_ok(input)) return false;

		char[] chars = input.toCharArray();
		int last_i = chars.length - 1;

		int digit_tot = 0;
		boolean dec_found = false;
		char exp = ' ';

		for (int i = 0; i <= last_i; i++)
		{
			if (chars[i] == dec_char)
			{
				if (dec_found || is_integer_ || exp != ' ' || i == 0 || i == last_i) return false;

				dec_found = true;
			}
			else if (chars[i] == '-' || chars[i] == '+') 
			{
				if (i != 0) return false;
			}
			else 
			{
				if (!Character.isDigit(chars[i])) 
				{
					if (exp == ' ' && arrays.value_exists(exp_chars2, chars[i])) 
					{
						exp = chars[i];

						continue;
					}
					else return false;
				}

				digit_tot++;
			}
		}		

		int digit_max = numbers.MAX_DIGITS_DECIMAL;
		if (is_long_) digit_max = numbers.MAX_DIGITS_LONG;
		else if (is_integer_) digit_max = numbers.MAX_DIGITS_INT;

		if (exp != ' ') 
		{
			try
			{
				if (exp != 'e') input = input.replace(exp, 'e');

				return (numbers.get_length(Double.parseDouble(input)) <= digit_max);
			}
			catch (Exception e) { return false; }
		}
		else if (digit_tot > digit_max) return false;

		boolean is_ok = true;

		if (digit_tot == digit_max) 
		{
			try
			{
				if (is_long_) Long.parseLong(input);
				else if (is_integer_) Integer.parseInt(input);
				else Double.parseDouble(input);
			}
			catch (Exception e) { is_ok = false; }
		}

		return is_ok;		
	}

	private static String remove_thousands_sep(String input_, DecimalFormatSymbols symbols_) 
	{ 
		String target = String.valueOf((symbols_ == null ? DecimalFormatSymbols.getInstance() : symbols_).getGroupingSeparator()); 

		return input_.replaceAll(target, "");
	}

	private static boolean contains_start_end(String needle_, String haystack_, boolean normalise_, boolean first_)
	{
		boolean contains = false;

		int length = get_length(haystack_);
		int length2 = get_length(needle_);
		if (length <= 0 || length2 <= 0) return contains;

		int i = index_of(needle_, haystack_, normalise_);
		if (first_) contains = (i == 0);
		else contains = (i == (length - length2));

		return contains;
	}

	private static String substring_before_after(String needle_, String haystack_, boolean normalise_, boolean is_before_)
	{
		int i = index_of(needle_, haystack_, normalise_);
		if (i < 0) return DEFAULT;

		if (!is_before_) i = i + needle_.length() - 1;
		
		return substring_before_after(haystack_, i, is_before_);
	}

	private static String substring_before_after(String string_, int i_, boolean is_before_)
	{
		if (i_ < 0) return DEFAULT;

		int start = 0;
		int length = i_;
		if (!is_before_)
		{
			start = i_ + 1;
			length = 0;
		}

		return substring(string_, start, length);
	}

	private static String substring_before_after(String needle_, String haystack_, boolean normalise_, int times_, boolean is_before_)
	{
		String output = DEFAULT;

		int times = times_;
		if (times <= 0) times = 1;

		String[] temp = split(needle_, haystack_, normalise_);
		int size0 = arrays.get_size(temp);
		if (size0 <= times) return output;

		int start_i = 0;
		int size = times;

		if (!is_before_)
		{
			start_i = times;
			size = 0;		
		}

		return String.join(needle_, (String[])arrays.get_range(temp, start_i, size));
	}
}