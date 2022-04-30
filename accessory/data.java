package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

//All the min/max/size values in this class refer to generic types and, as such, are defined  
//according to intrinsic or pragmatic limitations of the language or the intended approach. 

public class data extends parent
{
	public static final String STRING = types.DATA_STRING;
	public static final String STRING_BIG = types.DATA_STRING_BIG;
	public static final String INT = types.DATA_INT;	
	public static final String LONG = types.DATA_LONG;
	public static final String DECIMAL = types.DATA_DECIMAL;
	public static final String TIMESTAMP = types.DATA_TIMESTAMP;
	public static final String BOOLEAN = types.DATA_BOOLEAN;

	public static final String TRUE = types.DATA_BOOLEAN_TRUE;
	public static final String FALSE = types.DATA_BOOLEAN_FALSE;
	
	//--- For numeric types, min/max values (e.g., max decimal value). For other types, min/max
	//lengths or number of elements (e.g., max string length). 
	//These limits are theoretically independent from the DB/db_field ones, although they are expected
	//to be highly compatible with those for purely practical reasons.
	
	public static final double MIN_DECIMAL = numbers.MIN_DECIMAL;
	public static final double MIN_LONG = numbers.MIN_LONG;
	public static final double MIN_INT = numbers.MIN_INT;
	public static final double MIN_STRING = 0.0;
	public static final double MIN_STRING_BIG = MIN_STRING;
	public static final double MIN_BOOLEAN = 2.0;
	public static final double MIN_TIMESTAMP = 0.0;
	
	public static final double MAX_DECIMAL = numbers.MAX_DECIMAL;
	public static final double MAX_LONG = numbers.MAX_LONG;
	public static final double MAX_INT = numbers.MAX_INT;
	public static final double MAX_STRING = db.get_max_size(STRING);
	public static final double MAX_STRING_BIG = db.get_max_size(STRING_BIG);
	public static final double MAX_BOOLEAN = 2.0;
	public static final double MAX_TIMESTAMP = dates.SIZE_TIMESTAMP;
	
	//---
	
	public static final String DEFAULT_TYPE = STRING;
	
	private String _type = strings.DEFAULT;
	private Class<?> _class = null;
	private size _size = null;	
	
	private String _temp_type = strings.DEFAULT;
	private Class<?> _temp_class = null;
	private size _temp_size = null;
	
	public static boolean are_equal(data data1_, data data2_) { return are_equal_common(data1_, data2_); }	

	public data(data input_) { instantiate(input_); }

	public data(String type_, size size_) { instantiate(type_, null, size_); }

	public String get_type() { return _type; }

	public Class<?> get_class() { return _class; }

	public size get_size() { return _size; }
	
	public String toString()
	{
		String output = "";

		if (strings.is_ok(_type)) output = _type;

		if (generic.is_ok(_class))
		{
			if (strings.is_ok(output)) output += misc.SEPARATOR_ITEM;
			output += strings.to_string(_class);
		}

		if (generic.is_ok(_size)) 
		{
			if (strings.is_ok(output)) output += misc.SEPARATOR_ITEM;
			output += _size.toString(false);
		}

		return (misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE);
	}

	public boolean equals(data data2_) { return (is_ok(data2_) ? (strings.are_equal(_type, data2_._type) && generic.are_equal(_class, data2_._class) && size.are_equal(_size, data2_._size)) : false); }

	public static <x> boolean complies(x val_, data data_)
	{
		boolean is_ok = false;
		
		Class<?> type = generic.get_class(val_);
		Class<?> type2 = data_.get_class();
		if (val_ == null || !is_ok(data_) || !type_complies(type, type2)) return is_ok;

		if (generic.are_equal(type2, Boolean.class)) 
		{
			if (generic.are_equal(type, Boolean.class)) is_ok = true;
			else if (generic.are_equal(type, Integer.class)) 
			{
				int val = (int)val_;
				is_ok = (val == 0 || val == 1);
			}
		}
		else 
		{
			double size = 0;
			
			if (generic.are_equal(type2, String.class)) size = ((String)val_).length();
			else if (generic.is_number(type2)) size = numbers.to_number(val_);
			else return false;

			size size2 = data_.get_size();
			is_ok = numbers.is_ok(size, size2._min, size2._max);
		}

		return is_ok;
	}

	public static boolean type_is_ok(String type_) { return val_is_ok_common(type_, types.DATA, DEFAULT_TYPE); }

	public static String check_type(String type_) { return check_val_common(type_, types.DATA, DEFAULT_TYPE); }

	public static boolean size_is_ok(String type_, size size_) { return size.complies(size_, get_boundaries(type_)); }

	public static size check_size(String type_, size size_)
	{
		size boundaries = get_boundaries(type_);

		return new size(size.complies(size_, boundaries) ? size_ : boundaries);
	}

	public static size get_default_size(String type_) { return get_boundaries(type_); }
	
	public static boolean is_number(String type_) { return is_common(type_, new String[] { DECIMAL, LONG, INT }); }

	public static boolean is_string(String type_) { return is_common(type_, new String[] { STRING, STRING_BIG }); }
	
	public boolean is_ok()
	{
		_is_ok = is_ok(_type, _class, _size);

		return _is_ok;
	}
	
	static HashMap<String, Class<?>> populate_all_classes()
	{
		HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
		
		classes.put(STRING, String.class);
		classes.put(STRING_BIG, String.class);
		classes.put(INT, Integer.class);
		classes.put(LONG, Long.class);
		classes.put(DECIMAL, Double.class);
		classes.put(BOOLEAN, Boolean.class);
		classes.put(TIMESTAMP, String.class);
		
		return classes;
	}
	
	static HashMap<Class<?>, Class<?>> populate_all_compatible()
	{
		HashMap<Class<?>, Class<?>> classes = new HashMap<Class<?>, Class<?>>();
		
		classes.put(Double.class, Integer.class);
		classes.put(Double.class, Long.class);
		classes.put(Long.class, Integer.class);
		classes.put(Boolean.class, Integer.class);
		
		return classes;
	}

	private static boolean is_common(String type_, String[] targets_)
	{
		String type = check_type(type_);
		if (!strings.is_ok(type)) return false;

		for (String target: targets_)
		{
			if (type.equals(target)) return true;
		}

		return false;
	}

	private static size get_boundaries(String type_)
	{
		size boundaries = new size(size.WRONG_MIN, size.WRONG_MAX, size.WRONG_DECIMALS);

		String type = check_type(type_);
		if (!strings.is_ok(type)) return boundaries;

		if (type.equals(STRING)) 
		{
			boundaries._min = MIN_STRING;
			boundaries._max = MAX_STRING;
		}
		else if (type.equals(STRING_BIG)) 
		{
			boundaries._min = MIN_STRING_BIG;
			boundaries._max = MAX_STRING_BIG;
		}
		else if (type.equals(INT))
		{
			boundaries._min = MIN_INT;
			boundaries._max = MAX_INT;
		}
		else if (type.equals(LONG))
		{
			boundaries._min = MIN_LONG;
			boundaries._max = MAX_LONG;
		}
		else if (type.equals(DECIMAL))
		{
			boundaries._min = MIN_DECIMAL;
			boundaries._max = MAX_DECIMAL;
			boundaries._decimals = size.DEFAULT_DECIMALS;
		}
		else if (type.equals(BOOLEAN)) 
		{
			boundaries._min = MIN_BOOLEAN;
			boundaries._max = MAX_BOOLEAN;	
		}
		else if (type.equals(TIMESTAMP)) 
		{
			boundaries._min = MIN_TIMESTAMP;
			boundaries._max = MAX_TIMESTAMP;		
		}

		return boundaries;
	}

	private static boolean class_is_ok(Class<?> class_) { return (class_ != null && get_all_classes().containsValue(class_)); }

	private static boolean type_complies(Class<?> input_, Class<?> target_)
	{
		if (input_ == null || target_ == null) return false;
		if (generic.are_equal(input_, target_)) return true;

		for (Entry<Class<?>, Class<?>> item: get_all_compatible().entrySet())
		{
			if (generic.are_equal(target_, item.getKey()) && generic.are_equal(input_, item.getValue())) return true;
		}

		return false;
	}

	private static Class<?> get_class(String type_) { return (get_all_classes().containsKey(type_) ? get_all_classes().get(type_) : null); }

	private static HashMap<String, Class<?>> get_all_classes() { return _alls.DATA_CLASSES; }

	private static HashMap<Class<?>, Class<?>> get_all_compatible() { return _alls.DATA_COMPATIBLE; }

	private void instantiate(data input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_type, input_._temp_class, input_._temp_size);
	}
	
	private void instantiate(String type_, Class<?> class_, size size_)
	{
		instantiate_common();
		if (!is_ok(type_, class_, size_)) return;

		populate(_temp_type, _temp_class, _temp_size);
	}
	
	private boolean is_ok(String type_, Class<?> class_, size size_)
	{
		_temp_type = check_type(type_);
		_temp_class = (class_is_ok(class_) ? class_ : get_class(_temp_type));
		_temp_size = check_size(_temp_type, size_);
		
		return (strings.is_ok(_temp_type) && class_is_ok(_temp_class) && size_is_ok(_temp_type, _temp_size));
	}

	private void populate(String type_, Class<?> class_, size size_)
	{
		_is_ok = true;

		_type = type_;
		_class = class_;
		_size = new size(size_);
	}
}