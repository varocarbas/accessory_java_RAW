package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class serialisation 
{
	public static final String NULL = "NULL";
	
	public static final String LABEL = "label";
	public static final String VALUE = "value";
	public static final String CLASS = "class";
	
	public static final String SEPARATOR = misc.SEPARATOR_ITEM2;
	public static final String SEPARATOR2 = misc.SEPARATOR_CONTENT;
	
	public static final String LABEL_START = misc.LABEL_START;
	public static final String LABEL_END = misc.LABEL_END;
	public static final String VALUE_START = misc.REPLACEABLE_START;
	public static final String VALUE_END = misc.REPLACEABLE_END;
	public static final String CLASS_START = misc.LABEL2_START;
	public static final String CLASS_END = misc.LABEL2_END;

	public static boolean string_is_ok(String string_) { return (strings.is_ok(string_) && !string_.equals(NULL)); }

	public static String serialise(String output_, HashMap<String, Object> items_)
	{
		if (!arrays.is_ok(items_)) return output_;
		
		String output = (strings.is_ok(output_) ? output_ : "");
		
		for (Entry<String, Object> item: items_.entrySet()) { output = serialise(output, item.getKey(), item.getValue()); }
	
		return output;
	}

	public static String serialise(String output_, String label_, Object value_)
	{
		String item = serialise(label_, value_);
		if (!strings.is_ok(item)) return output_;
		
		return ((strings.is_ok(output_) ? output_ + SEPARATOR : "") + item);
	}
	
	public static String serialise(String label_, Object value_) { return serialise(label_, value_, null); }
	
	public static String serialise(String label_, Object value_, Class<?> class_)
	{
		if (!strings.is_ok(label_)) return strings.DEFAULT;
	
		String output = LABEL_START + label_ + LABEL_END;
		output += SEPARATOR2;	
		
		String value = (VALUE_START + (value_ != null ? strings.to_string(value_) : NULL) + VALUE_END);
		output += value;
		output += SEPARATOR2;
		
		Class<?> class2 = (class_ != null ? class_ : generic.get_class(value_));
		output += (CLASS_START + (class2 != null ? strings.to_string(class2) : NULL) + CLASS_END);
		
		return output;
	}

	public static HashMap<String, String>[] unserialise(String input_)
	{
		if 
		(
			!strings.contains_all
			(
				new String[] 
				{ 
					SEPARATOR2, LABEL_START, LABEL_END, VALUE_START, 
					VALUE_END, CLASS_START, CLASS_END
				}, 
				input_, false
			)
		) 
		{ return null; }
		
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
				
		String start = "'";
		String end = "'";
		
		int i = 0;
		int max_i = strings.WRONG_I;
		
		HashMap<String, String[]> targets = new HashMap<String, String[]>();
		
		targets.put(LABEL, new String[] { LABEL_START, LABEL_END });
		targets.put(VALUE, new String[] { VALUE_START, VALUE_END });
		targets.put(CLASS, new String[] { CLASS_START, CLASS_END });
		
		while (true)
		{
			if (max_i > strings.WRONG_I) i = max_i + 1;
			max_i = strings.get_i(SEPARATOR, input_, false, start, end, i);

			HashMap<String, String> temp = new HashMap<String, String>();
			
			for (Entry<String, String[]> target: targets.entrySet())
			{
				String[] needles = target.getValue();
				
				int[] is = strings.get_is(needles, input_, false, start, end, i, true);
				if (!strings.is_are_ok(is, max_i)) 
				{
					temp = null;
					
					break;
				}

				temp.put(target.getKey(), strings.get_substring(input_, is, needles[0], needles[1], false));
			
				int temp3 = strings.get_i(SEPARATOR2, input_, false, start, end, i);
				if (temp3 > strings.WRONG_I && temp3 <= max_i) i = temp3;
			}

			if (temp != null) output.add(temp);
			
			if (max_i <= strings.WRONG_I) break;
		}
	
		return (output.size() > 0 ? arrays.to_array(output) : null);
	}

	public static String serialise_array(String label_, Object array_) { return serialise(label_, (Object)get_string_array(array_), generic.get_class(array_)); }

	public static String get_string_array(Object array_) { return arrays.to_string(array_, arrays.SEPARATOR_ARRAY, false); }
	
	public static Object parse_value(String value_) 
	{ 
		Object output = null;
		if (value_ == null || value_.equals(NULL)) return output;
		
		if (value_.contains(arrays.SEPARATOR_ARRAY)) output = value_.split(arrays.SEPARATOR_ARRAY);
		else if (strings.is_boolean(value_)) output = strings.to_boolean(value_);
		else if (strings.is_number(value_)) output = strings.to_number(value_);
		else output = value_;
		
		return output;
	}
	
	public static Object parse_value(String value_, String class_) { return parse_value(value_, parse_class(class_)); }
	
	public static Object parse_value(String value_, Class<?> class_)
	{
		Object output = null;
		if (value_ == null || value_.equals(NULL)) return output;
		
		if (class_ == null) return parse_value(value_);

		if (generic.are_equal(class_, Double.class)) output = strings.to_number_decimal(value_);
		else if (generic.are_equal(class_, Long.class)) output = strings.to_number_long(value_);
		else if (generic.are_equal(class_, Integer.class)) output = strings.to_number_int(value_);
		else if (generic.are_equal(class_, Boolean.class)) output = strings.to_boolean(value_);
		else output = parse_value(value_);

		return output;
	}
	
	public static Object parse_array(String array_) 
	{ 
		Object output = null;
		if (!string_is_ok(array_)) return output;
		
		if (array_.contains(arrays.SEPARATOR_ARRAY)) output = array_.split(arrays.SEPARATOR_ARRAY);
		
		return output;
	}
	
	public static Object parse_array(String array_, Class<?> class_) 
	{ 
		Object output = null;
		if (!string_is_ok(array_)) return output;
		
		if (class_ == null) return parse_array(array_);
		
		if (class_.equals(String[].class)) output = array_.split(arrays.SEPARATOR_ARRAY);
		
		return output;
	}
	
	public static Class<?> parse_class(String input_)
	{
		if (!string_is_ok(input_)) return null;
			
		Class<?>[] targets = new Class<?>[] { String.class, Double.class, Long.class, Integer.class, Boolean.class };
		
		for (Class<?> target: targets)
		{
			if (!arrays.value_exists(generic.get_string_equivalents(target), input_)) return target;
		}
		
		return null;
	}

	public static HashMap<String, String> get_unserialised_item_values(String serialised_, String[] labels_) { return get_unserialised_item_values(unserialise(serialised_), labels_); }
	
	public static HashMap<String, String> get_unserialised_item_values(HashMap<String, String>[] items_, String[] labels_)
	{
		if (!arrays.is_ok(items_) || !arrays.is_ok(labels_)) return null;
		
		HashMap<String, String> output = new HashMap<String, String>();
		
		for (String label: labels_)
		{
			if (!strings.is_ok(label)) continue;
	
			String val = get_unserialised_item_value(items_, label);
			if (!strings.is_ok(val)) return null;
			
			output.put(label, val);
		}
		
		return output;
	}
	
	public static String get_unserialised_item_value(HashMap<String, String>[] items_, String label_)
	{
		String output = strings.DEFAULT;
		
		HashMap<String, String> item = get_unserialised_item(items_, label_);
		
		if (arrays.is_ok(item) && item.containsKey(VALUE)) output = item.get(VALUE);
		
		return output;
	}
	
	public static Object get_unserialised_item_class(HashMap<String, String>[] items_, String label_)
	{
		Object output = null;
		
		HashMap<String, String> item = get_unserialised_item(items_, label_);
		
		if (arrays.is_ok(item) && item.containsKey(CLASS)) output = item.get(VALUE);
		
		return output;
	}
	
	public static HashMap<String, String> get_unserialised_item(HashMap<String, String>[] items_, String label_)
	{
		if (!strings.is_ok(label_) || !arrays.is_ok(items_)) return null;
		
		for (HashMap<String, String> item: items_)
		{
			if (item != null && item.containsKey(LABEL) && item.get(LABEL).equals(label_)) return new HashMap<String, String>(item);
		}
		
		return null;
	}
}