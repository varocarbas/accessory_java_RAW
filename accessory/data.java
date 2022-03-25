package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class data 
{
	public static final double MIN_DECIMAL = size.MIN_DECIMAL;
	public static final double MIN_LONG = size.MIN_LONG;
	public static final double MIN_INT = size.MIN_INT;
	public static final int MIN_DECIMALS = size.MIN_DECIMALS;
	public static final int MIN_STRING = size.MIN_STRING;
	
	public static final double MAX_DECIMAL = size.MAX_DECIMAL;
	public static final double MAX_LONG = size.MAX_LONG;
	public static final double MAX_INT = size.MAX_INT;
	public static final int MAX_DECIMALS = size.MAX_DECIMALS;
	public static final int MAX_STRING = size.MAX_STRING;
	
	public static final String STRING = types.DATA_STRING;
	public static final String STRING_BIG  = types.DATA_STRING_BIG;
	public static final String INT = types.DATA_INT;	
	public static final String LONG = types.DATA_LONG;
	public static final String DECIMAL = types.DATA_DECIMAL;
	public static final String TIMESTAMP = types.DATA_TIMESTAMP;
	public static final String BOOLEAN = types.DATA_BOOLEAN;
	public static final String TYPE_STRING = STRING;
	public static final String TYPE_STRING_BIG = STRING_BIG;
	public static final String TYPE_INT = INT;	
	public static final String TYPE_LONG = LONG;
	public static final String TYPE_DECIMAL = DECIMAL;
	public static final String TYPE_TIMESTAMP = TIMESTAMP;
	public static final String TYPE_BOOLEAN = BOOLEAN;

	public static final String TRUE = types.DATA_BOOLEAN_TRUE;
	public static final String FALSE = types.DATA_BOOLEAN_FALSE;
	public static final String BOOLEAN_TRUE = TRUE;
	public static final String BOOLEAN_FALSE = FALSE;
	
	public boolean _is_ok = false;

	public String _type = strings.DEFAULT;
	public Class<?> _class = null;
	public size _size = null;

	private static HashMap<String, Class<?>> _all_classes = new HashMap<String, Class<?>>();

	public data(data input_)
	{
		_is_ok = false;
		if (!is_ok(input_._type, input_._class, input_._size, false)) return;

		populate_main(input_._type, input_._class, input_._size);
	}

	public data(String type_, size size_)
	{
		_is_ok = false;

		String type = check_type(type_);
		Class<?> temp = get_class(type);

		if (!is_ok(type_, temp, size_, false)) return;

		populate_main(type, temp, size_);
	}

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

	public boolean equals(data data2_)
	{
		if (!is_ok(data2_)) return false;

		return 
		(
			strings.are_equal(_type, data2_._type) && 
			generic.are_equal(_class, data2_._class) && 
			size.are_equal(_size, data2_._size)
		);		
	}

	public static String to_string(data data_)
	{
		return (is_ok(data_) ? data_.toString() : strings.DEFAULT);	
	}

	public static boolean are_equal(data data1_, data data2_)
	{
		return generic.are_equal(data1_, data2_);
	}

	public static boolean is_ok(data input_)
	{
		return ((input_ == null) ? false : is_ok(input_._type, input_._class, input_._size, true));
	}

	public static <x> boolean complies(x val_, data data_)
	{
		boolean is_ok = false;
		if (!generic.is_ok(val_) || !is_ok(data_) || !class_complies(generic.get_class(val_), data_._class)) return is_ok;

		if (generic.are_equal(data_._class, Boolean.class)) is_ok = true;
		else 
		{
			double size = 0;

			if (generic.are_equal(data_._class, String.class)) size = ((String)val_).length();
			else if (is_numeric(data_._class)) 
			{
				if (generic.are_equal(data_._class, Integer.class)) size = (double)((int)val_); //!!!
				else if (generic.are_equal(data_._class, Long.class)) size = (double)((long)val_); //!!!
				else size = (double)val_;
			}
			else return false;

			is_ok = numbers.is_ok(size, data_._size._min, data_._size._max);
		}

		return is_ok;
	}

	public static boolean type_is_ok(String type_)
	{
		return (strings.is_ok(check_type(type_)));
	}

	public static String check_type(String type_)
	{
		return types.check_subtype(type_, types.get_subtypes(types.DATA, null), null, null);
	}

	public static size get_default_size(String type_)
	{
		return get_boundaries(type_);
	}

	public static boolean is_numeric(String type_)
	{
		return is_common(type_, new String[] { DECIMAL, LONG, INT });
	}

	public static boolean is_string(String type_)
	{
		return is_common(type_, new String[] { STRING, STRING_BIG });		
	}

	private void populate_main(String type_, Class<?> class_, size size_)
	{
		_is_ok = true;

		_type = type_;
		_class = class_;
		_size = update_size(type_, size_);
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

	private static boolean is_ok(String type_, Class<?> class_, size size_, boolean check_size_)
	{
		return 
		(
			strings.is_ok(type_) && class_is_ok(class_) &&
			(!check_size_ || (check_size_ && size_is_ok(type_, size_)))
		);
	}

	private static size update_size(String type_, size size_)
	{
		size boundaries = get_boundaries(type_);

		return new size(size.complies(size_, boundaries) ? size_ : boundaries);
	}

	private static boolean size_is_ok(String type_, size size_)
	{
		return size.complies(size_, get_boundaries(type_));
	}

	private static boolean is_numeric(Class<?> class_)
	{
		for (Class<?> item: numbers.get_all_classes())
		{
			if (generic.are_equal(class_, item)) return true;
		}

		return false;
	}

	private static size get_boundaries(String type_)
	{
		size boundaries = new size(0.0, 0.0, size.DEFAULT_DECIMALS);

		String type = check_type(type_);
		if (!strings.is_ok(type)) return boundaries;

		if (is_string(type)) boundaries._max = (double)strings.MAX_SIZE;
		else if (generic.are_equal(type, INT))
		{
			boundaries._min = (double)size.MIN_INT;
			boundaries._max = (double)size.MAX_INT;
		}
		else if (generic.are_equal(type, LONG))
		{
			boundaries._min = (double)size.MIN_LONG;
			boundaries._max = (double)size.MAX_LONG;
		}
		else if (generic.are_equal(type, DECIMAL))
		{
			boundaries._min = (double)size.MIN_DECIMAL;
			boundaries._max = (double)size.MAX_DECIMAL;
		}
		else if (generic.are_equal(type, BOOLEAN)) 
		{
			boundaries._min = 2.0;
			boundaries._max = 2.0;	
		}
		else if (generic.are_equal(type, TIMESTAMP)) 
		{
			boundaries._min = 0.0;
			boundaries._max = dates.get_time_pattern(dates.DATE_TIME).length();	
		}

		return boundaries;
	}

	private static boolean class_is_ok(Class<?> class_)
	{
		populate_all_classes();

		return (class_ != null && _all_classes.containsValue(class_));
	}

	private static boolean class_complies(Class<?> input_, Class<?> target_)
	{
		if (input_ == null || target_ == null) return false;
		if (input_.equals(target_)) return true;

		for (Entry<Class<?>, Class<?>> item: class_complies_get_compatible().entrySet())
		{
			if (target_.equals(item.getKey()) && input_.equals(item.getValue())) return true;
		}

		return false;
	}

	private static HashMap<Class<?>, Class<?>> class_complies_get_compatible()
	{
		HashMap<Class<?>, Class<?>> output = new HashMap<Class<?>, Class<?>>();

		output.put(Double.class, Integer.class);
		output.put(Double.class, Long.class);
		output.put(Long.class, Integer.class);

		return output;
	}

	private static Class<?> get_class(String type_)
	{
		populate_all_classes();

		return (_all_classes.containsKey(type_) ? _all_classes.get(type_) : null);
	}

	private static void populate_all_classes()
	{
		if (arrays.is_ok(_all_classes)) return;

		_all_classes = new HashMap<String, Class<?>>();
		_all_classes.put(STRING, String.class);
		_all_classes.put(STRING_BIG, String.class);
		_all_classes.put(INT, Integer.class);
		_all_classes.put(LONG, Long.class);
		_all_classes.put(DECIMAL, Double.class);
		_all_classes.put(BOOLEAN, Boolean.class);
		_all_classes.put(TIMESTAMP, String.class);
	}
}