package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

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
		else if (is_data(input_)) is_ok = data.is_ok((data)input_);
		else if (is_size(input_)) is_ok = size.is_ok((size)input_);
		else if (is_field(input_)) is_ok = field.is_ok((field)input_);
		else is_ok = true;

		return is_ok;
	}

	public static <x> boolean is_ok(Class<?> input_)
	{
		return (input_ != null);
	}

	public static <x, y> boolean is_ok(HashMap<x, y> input_)
	{
		return arrays.is_ok(input_);
	}

	public static boolean is_class(Class<?> input_)
	{
		return are_equal(input_, Class.class);
	}
	
	public static <x> boolean is_class(x input_)
	{
		return (input_ instanceof Class);
	}

	public static boolean is_size(Class<?> input_)
	{
		return are_equal(input_, size.class);
	}
	
	public static <x> boolean is_size(x input_)
	{
		return (input_ instanceof size);
	}

	public static boolean is_data(Class<?> input_)
	{
		return are_equal(input_, data.class);
	}
	
	public static <x> boolean is_data(x input_)
	{
		return (input_ instanceof data);
	}

	public static boolean is_field(Class<?> input_)
	{
		return are_equal(input_, field.class);
	}
	
	public static <x> boolean is_field(x input_)
	{
		return (input_ instanceof field);
	}
	
	public static boolean is_string(Class<?> input_)
	{
		return are_equal(input_, String.class);
	}
	
	public static <x> boolean is_string(x input_)
	{
		return (input_ instanceof String);
	}

	public static boolean is_boolean(Class<?> input_)
	{
		return are_equal(input_, Boolean.class);
	}
	
	public static <x> boolean is_boolean(x input_)
	{
		return (input_ instanceof Boolean);
	}

	public static boolean is_number(Class<?> input_)
	{
		for (Class<?> type: numbers.get_all_classes())
		{
			if (are_equal(input_, type)) return true;
		}

		return false;
	}
	
	public static <x> boolean is_number(x input_)
	{
		for (Class<?> type: numbers.get_all_classes())
		{
			if (is_instance(input_, type)) return true;
		}

		return false;
	}

	public static boolean is_array(Class<?> input_)
	{
		for (Class<?> type: arrays.get_all_classes())
		{
			if (are_equal(input_, type)) return true;
		}

		return false;
	}
	
	public static <x> boolean is_array(x input_)
	{
		for (Class<?> type: arrays.get_all_classes())
		{
			if (is_instance(input_, type)) return true;
		}

		return false;
	}

	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (class_ == null) return output;
		
		if (is_class(class_)) output = get_random_class();
		else if (is_string(class_)) output = strings.get_random(strings.SIZE_SMALL);
		else if (is_boolean(class_)) output = get_random_boolean();
		else if (is_number(class_)) output = numbers.get_random(class_);
		else if (is_array(class_)) output = arrays.get_random(class_);
		
		return output;
	}
	
	public static Class<?> get_random_class()
	{
		Class<?>[] haystack = get_all_classes();
		
		return haystack[numbers.get_random_index(haystack.length)];
	}
	
	public static boolean get_random_boolean()
	{
		return (new Random()).nextBoolean();
	}
	
	public static boolean are_equal(Class<?> input1_, Class<?> input2_)
	{
		return classes_are_equal(input1_, input2_);
	}

	public static <x> boolean are_equal(x input1_, x input2_)
	{
		boolean is_ok1 = is_ok(input1_);
		boolean is_ok2 = is_ok(input2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		boolean output = false;

		Class<?> type1 = get_class(input1_);
		Class<?> type2 = get_class(input2_);
		if (type1 == null || type2 == null || !type1.equals(type2)) return output;
		
		if (is_array(type1)) output = arrays.are_equal(input1_, input2_);
		else output = input1_.equals(input2_);
		
		return output;
	}	
	
	//It has to be synced with get_all_classes().
	public static <x> Class<?> get_class(x input_)
	{
		Class<?> type = null;

		if (input_ instanceof String) type = String.class;
		else if (input_ instanceof Boolean) type = Boolean.class;
		else if (input_ instanceof Integer) type = Integer.class;
		else if (input_ instanceof Long) type = Long.class;
		else if (input_ instanceof Double) type = Double.class;
		else if (input_ instanceof Class<?>) type = Class.class;
		else if (input_ instanceof Method) type = Method.class;
		else if (input_ instanceof Exception) type = Exception.class;
		else if (input_ instanceof size) type = size.class;
		else if (input_ instanceof data) type = data.class;
		else if (input_ instanceof field) type = field.class;
		else if (input_ instanceof HashMap<?, ?>) type = HashMap.class;
		else if (input_ instanceof ArrayList<?>) type = ArrayList.class;
		else if (input_ instanceof Object[]) type = Array.class;
		
		return type;
	}

	//It has to be synced with get_all_classes().
	public static <x> boolean is_instance(x input_, Class<?> class_)
	{
		return (class_ != null && are_equal(class_, get_class(input_)));
	}

	//It has to be synced with get_class().
	public static Class<?>[] get_all_classes()
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		classes.add(Class.class);
		classes.add(Method.class);
		classes.add(Exception.class);

		classes.add(size.class);
		classes.add(data.class);
		classes.add(field.class);
		
		classes.add(String.class);
		classes.add(Boolean.class);

		for (Class<?> type: numbers.get_all_classes())
		{
			classes.add(type);
		}

		for (Class<?> type: arrays.get_all_classes())
		{
			classes.add(type);
		}

		return classes.toArray(new Class<?>[classes.size()]);
	}

	public static Method[] get_all_methods(Class<?> class_, String[] skip_)
	{
		if (!is_ok(class_)) return null;

		ArrayList<Method> methods = new ArrayList<Method>();

		ArrayList<String> skip = arrays.to_arraylist(get_default_method_names());
		skip.addAll(arrays.to_arraylist(skip_));

		for (Method method: class_.getMethods())
		{
			if (skip.contains(method.getName())) continue;

			methods.add(method);
		}

		return arrays.to_array(methods);
	}

	public static Method get_method(Class<?> class_, String name_, Class<?>[] params_, boolean error_exit_)
	{
		Method output = null;
		if (!is_ok(class_) || !strings.is_ok(name_)) return output;

		try 
		{
			output = class_.getMethod(name_, params_);
		} 
		catch (Exception e) 
		{ 
			errors.manage(types.ERROR_GENERIC_METHOD_GET, e, new String[] { name_ }, error_exit_); 
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	public static <x> x call_static_method(Method method_, Object[] args_, boolean error_exit_)
	{
		x output = null;
		if (!is_ok(method_)) return output;

		try 
		{
			output = (x)method_.invoke(null, args_);
		} 
		catch (Exception e) 
		{ 
			errors.manage
			(
				types.ERROR_GENERIC_METHOD_CALL, e, new String[] 
				{ 
					method_.getName(), arrays.to_string(strings.to_strings(args_), null) 
				},
				error_exit_
			); 
		}

		return output;
	}

	private static boolean classes_are_equal(Class<?> class1_, Class<?> class2_)
	{
		boolean is_ok1 = is_ok(class1_);
		boolean is_ok2 = is_ok(class2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		if (class1_.equals(class2_)) return true;

		for (Entry<Class<?>, Class<?>> equivalent: classes_are_equal_equivalents().entrySet())
		{
			Class<?> key = equivalent.getKey();
			Class<?> val = equivalent.getValue();
			
			if 
			(
				(class1_.equals(key) && class2_.equals(val)) || 
				(class2_.equals(key) && class1_.equals(val))
			)
			{ return true; }
		}
		
		return false;
	}

	private static String[] get_default_method_names()
	{
		return new String[]
		{
			"wait", "equals", "toString", "hashCode", 
			"getClass", "notify", "notifyAll"
		};
	}

	private static HashMap<Class<?>, Class<?>> classes_are_equal_equivalents()
	{
		HashMap<Class<?>, Class<?>> equivalents = new HashMap<Class<?>, Class<?>>();
		
		equivalents.put(Double.class, double.class);
		equivalents.put(Long.class, long.class);
		equivalents.put(Integer.class, int.class);
		equivalents.put(Boolean.class, boolean.class);
		
		return equivalents;
	}
}