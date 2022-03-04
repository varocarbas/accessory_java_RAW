package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class data 
{
	public String _type = null;
	public Class<?> _class = null;
	public size _size = null;

	private static HashMap<String, Class<?>> _all_classes = new HashMap<String, Class<?>>();

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
		boolean is_ok1 = is_ok(data1_);
		boolean is_ok2 = is_ok(data2_);
		
		return ((!is_ok1 || !is_ok2) ? (is_ok1 == is_ok2) : data1_.equals(data2_));
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

	public data(data input_)
	{
		if (!is_ok(input_)) return;

		_type = input_._type;
		_class = input_._class;
		_size = update_size(_class, input_._size);
	}

	public data(String type_, size size_)
	{
		String type = check_type(type_);
		Class<?> temp = get_class(type);
		if (!is_ok(type_, temp, size_, false)) return;

		_type = type;
		_class = temp;
		_size = update_size(_class, size_);
	}

	private static boolean is_ok(String type_, Class<?> class_, size size_, boolean check_size_)
	{
		return 
		(
			strings.is_ok(type_) && class_is_ok(class_) &&
			(!check_size_ || (check_size_ && size_is_ok(class_, size_)))
		);
	}

	private static size update_size(Class<?> class_, size size_)
	{
		size boundaries = get_boundaries(class_);

		return new size(size.complies(size_, boundaries) ? size_ : boundaries);
	}

	private static boolean size_is_ok(Class<?> class_, size size_)
	{
		return size.complies(size_, get_boundaries(class_));
	}

	private static boolean is_numeric(Class<?> class_)
	{
		for (Class<?> item: numbers.get_all_classes())
		{
			if (generic.are_equal(class_, item)) return true;
		}

		return false;
	}

	private static size get_boundaries(Class<?> class_)
	{
		size boundaries = new size(0.0, 0.0);

		if (generic.are_equal(class_, String.class)) boundaries._max = (double)Integer.MAX_VALUE;
		else if (generic.are_equal(class_, Integer.class))
		{
			boundaries._min = (double)numbers.MIN_INT;
			boundaries._max = (double)numbers.MAX_INT;
		}
		else if (generic.are_equal(class_, Long.class))
		{
			boundaries._min = (double)numbers.MIN_LONG;
			boundaries._max = (double)numbers.MAX_LONG;
		}
		else if (generic.are_equal(class_, Double.class))
		{
			boundaries._min = (double)numbers.MIN_DEC;
			boundaries._max = (double)numbers.MAX_DEC;
		}
		else if (generic.are_equal(class_, Boolean.class)) 
		{
			boundaries._min = 2.0;
			boundaries._max = 2.0;	
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
		_all_classes.put(types.DATA_STRING, String.class);
		_all_classes.put(types.DATA_INTEGER, Integer.class);
		_all_classes.put(types.DATA_LONG, Long.class);
		_all_classes.put(types.DATA_DECIMAL, Double.class);
		_all_classes.put(types.DATA_BOOLEAN, Boolean.class);
	}

	private static String check_type(String type_)
	{
		return types.check_subtype(type_, types.get_subtypes(types.DATA, null), null, null);
	}
}