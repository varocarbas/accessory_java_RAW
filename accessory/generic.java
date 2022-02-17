package accessory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class generic 
{
	static { _ini.load(); }

	public static <x> boolean is_ok(x input_)
	{
		boolean is_ok = false;
		if (input_ == null) return is_ok;

		if (is_string(input_)) is_ok = strings.is_ok((String)input_);
		else if (is_array(input_)) is_ok = arrays.is_ok(input_);
		else if (is_boolean(input_) || is_number(input_)) is_ok = true;

		return is_ok;
	}

	@SuppressWarnings("rawtypes")
	public static <x> boolean is_ok(Class input_)
	{
		return (input_ != null);
	}
	
	public static <x, y> boolean is_ok(HashMap<x, y> input_)
	{
		return arrays.is_ok(input_);
	}

	public static <x> boolean is_string(x input_)
	{
		return (input_ instanceof String);
	}

	public static <x> boolean is_boolean(x input_)
	{
		return (input_ instanceof Boolean);
	}

	@SuppressWarnings("rawtypes")
	public static <x> boolean is_number(x input_)
	{
		for (Class type: numbers.get_all_classes())
		{
			if (is_instance(input_, type)) return true;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	public static <x> boolean is_array(x input_)
	{
		for (Class type: arrays.get_all_classes())
		{
			if (is_instance(input_, type)) return true;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	public static <x> boolean are_equal(x input1_, x input2_)
	{
		if (!is_ok(input1_) || !is_ok(input2_)) return false;

		Class type1 = get_class(input1_);
		Class type2 = get_class(input2_);

		return (type1 != null && type1.equals(type2) && input1_.equals(input2_));
	}

	//It has to be synced with get_all_classes().
	@SuppressWarnings("rawtypes")
	public static <x> Class get_class(x input_)
	{
		Class type = null;

		if (input_ instanceof String) type = String.class;
		else if (input_ instanceof Boolean) type = Boolean.class;
		else if (input_ instanceof Integer) type = Integer.class;
		else if (input_ instanceof Double) type = Double.class;
		else if (input_ instanceof HashMap<?, ?>) type = HashMap.class;
		else if (input_ instanceof ArrayList<?>) type = ArrayList.class;
		else if (input_ instanceof Object[]) type = Array.class;

		return type;
	}

	//It has to be synced with get_all_classes().
	@SuppressWarnings("rawtypes")
	public static <x> boolean is_instance(x input_, Class class_)
	{
		return (class_ != null && class_.equals(get_class(input_)));
	}

	//It has to be synced with get_class().
	@SuppressWarnings("rawtypes")
	public static Class[] get_all_classes()
	{
		ArrayList<Class> classes = new ArrayList<Class>();

		classes.add(String.class);
		classes.add(Boolean.class);

		for (Class type: numbers.get_all_classes())
		{
			classes.add(type);
		}

		for (Class type: arrays.get_all_classes())
		{
			classes.add(type);
		}

		return classes.toArray(new Class[classes.size()]);
	}
}