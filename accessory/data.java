package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public class data 
{
	public Class<?> _class = null;
	public String _type = null;
	public size _size = null;

	private static HashMap<String, Class<?>> _all_classes = new HashMap<String, Class<?>>();

	public static boolean is_ok(data input_)
	{
		return ((input_ == null) ? false : is_ok(input_._type, input_._class, input_._size, true));
	}

	public static <x> boolean complies(x val_, data data_)
	{
		boolean is_ok = false;
		if (!generic.is_ok(val_) || !is_ok(data_) || !class_complies(val_.getClass(), data_._class)) return is_ok;

		if (data_._class.equals(Boolean.class)) is_ok = true;
		else 
		{
			double size = 0;
			
			if (data_._class.equals(String.class)) size = ((String)val_).length();
			else if (is_numeric(data_._class)) 
			{
				if (val_.getClass().equals(Integer.class)) size = (double)((int)val_); //!!!
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
			if (item.equals(class_)) return true;
		}

		return false;
	}

	private static size get_boundaries(Class<?> class_)
	{
		size boundaries = new size(0.0, 0.0);

		if (class_.equals(String.class)) boundaries._max = (double)Integer.MAX_VALUE;
		else if (class_.equals(Integer.class))
		{
			boundaries._min = (double)numbers.MIN_INT;
			boundaries._max = (double)numbers.MAX_INT;
		}
		else if (class_.equals(Double.class))
		{
			boundaries._min = (double)numbers.MIN_DEC;
			boundaries._max = (double)numbers.MAX_DEC;
		}
		else if (class_.equals(Boolean.class)) 
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
		_all_classes.put(types.DATA_DECIMAL, Double.class);
		_all_classes.put(types.DATA_BOOLEAN, Boolean.class);
	}

	private static String check_type(String type_)
	{
		return types.check_subtype(type_, types.get_subtypes(types.DATA, null), null, null);
	}
}