package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class generic 
{
	static { ini.load(); }

	public static <x> boolean is_ok(x input_)
	{
		boolean is_ok = false;
		if (input_ == null) return is_ok;

		if (is_string(input_)) is_ok = strings.is_ok((String)input_);
		else if (is_array(input_)) is_ok = arrays.is_ok(input_);
		else if (is_boolean(input_) || is_number(input_)) is_ok = true;
		else if (is_data(input_)) is_ok = data.is_ok((data)input_);
		else if (is_size(input_)) is_ok = size.is_ok((size)input_);
		else if (is_db_field(input_)) is_ok = db_field.is_ok((db_field)input_);
		else if (is_db_where(input_)) is_ok = db_where.is_ok((db_where)input_);
		else if (is_db_order(input_)) is_ok = db_order.is_ok((db_order)input_);
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
		return is_common(input_, new Class<?>[] { Class.class }, true);
	}
	
	public static <x> boolean is_class(x input_)
	{
		return is_common(input_, new Class<?>[] { Class.class }, false);
	}

	public static boolean is_size(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { size.class }, true);
	}
	
	public static <x> boolean is_size(x input_)
	{
		return is_common(input_, new Class<?>[] { size.class }, false);
	}

	public static boolean is_data(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { data.class }, true);
	}
	
	public static <x> boolean is_data(x input_)
	{
		return is_common(input_, new Class<?>[] { data.class }, false);
	}

	public static boolean is_db_field(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { db_field.class }, true);
	}
	
	public static <x> boolean is_db_field(x input_)
	{
		return is_common(input_, new Class<?>[] { db_field.class }, false);
	}

	public static boolean is_db_where(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { db_where.class }, true);
	}
	
	public static <x> boolean is_db_where(x input_)
	{
		return is_common(input_, new Class<?>[] { db_where.class }, false);
	}

	public static boolean is_db_order(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { db_order.class }, true);
	}
	
	public static <x> boolean is_db_order(x input_)
	{
		return is_common(input_, new Class<?>[] { db_order.class }, false);
	}
	
	public static boolean is_string(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { String.class }, true);
	}
	
	public static <x> boolean is_string(x input_)
	{
		return is_common(input_, new Class<?>[] { String.class }, false);
	}

	public static boolean is_boolean(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { Boolean.class, boolean.class }, true);
	}
	
	public static boolean is_boolean(boolean input_)
	{
		return true;
	}
	
	public static <x> boolean is_boolean(x input_)
	{
		return is_common(input_, new Class<?>[] { Boolean.class, boolean.class }, false);
	}

	public static boolean is_number(Class<?> input_)
	{
		return is_common(input_, numbers.get_all_classes(true), true);
	}
	
	public static <x> boolean is_number(x input_)
	{
		return is_common(input_, numbers.get_all_classes(true), false);
	}

	public static boolean is_array(Class<?> input_)
	{
		return is_common(input_, arrays.get_all_classes(true, true), true);
	}
	
	public static <x> boolean is_array(x input_)
	{
		return is_common(input_, arrays.get_all_classes(true, true), false);
	}
	
	public static <x, y> HashMap<x, y> get_new(HashMap<x, y> input_)
	{
		return arrays.get_new(input_);
	}
	
	public static <x> ArrayList<ArrayList<x>> get_new(ArrayList<ArrayList<x>> input_)
	{
		return arrays.get_new(input_);
	}

	public static double[] get_new(double[] input_)
	{
		return arrays.get_new(input_);
	}
	
	public static long[] get_new(long[] input_)
	{
		return arrays.get_new(input_);
	}
	
	public static int[] get_new(int[] input_)
	{
		return arrays.get_new(input_);
	}
	
	public static boolean[] get_new(boolean[] input_)
	{
		return arrays.get_new(input_);
	}
	
	public static <x> Object get_new(Object input_)
	{
		Object output = null;
		if (!is_ok(input_)) return output;
		
		Class<?> type = get_class(input_);
		if (is_array(type)) output = arrays.get_new(input_);
		else if (are_equal(type, size.class)) output = new size((size)input_);
		else if (are_equal(type, data.class)) output = new data((data)input_);
		else if (are_equal(type, db_field.class)) output = new db_field((db_field)input_);
		else if (are_equal(type, db_where.class)) output = new db_where((db_where)input_);
		else if (are_equal(type, db_order.class)) output = new db_order((db_order)input_);
		else output = input_;
		
		return output;
	}
	
	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (class_ == null) return output;
		
		if (is_class(class_)) output = get_random_class(true, true);
		else if (is_string(class_) || are_equal(class_, Object.class)) output = strings.get_random(strings.SIZE_SMALL);
		else if (is_boolean(class_)) output = get_random_boolean();
		else if (is_number(class_)) output = numbers.get_random(class_);
		else if (is_array(class_)) output = arrays.get_random(class_); 
		
		return output;
	}
	
	public static Class<?> get_random_class(boolean small_too_, boolean array_specific_too_)
	{
		Class<?>[] haystack = get_all_classes(small_too_, array_specific_too_);
		
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
		if (type1 == null || type2 == null || !classes_are_equal(type1, type2)) return output;
		
		if (is_array(type1)) output = arrays.are_equal(input1_, input2_);
		else output = input1_.equals(input2_);
		
		return output;
	}	
	
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
		else if (input_ instanceof db_field) type = db_field.class;
		else if (input_ instanceof db_where) type = db_where.class;
		else if (input_ instanceof db_order) type = db_order.class;
		else if (input_ instanceof HashMap<?, ?>) type = HashMap.class;
		else if (input_ instanceof ArrayList<?>) type = ArrayList.class;
		else if (input_ instanceof Object[]) type = Array.class;
		else if (input_ instanceof double[]) type = double[].class;
		else if (input_ instanceof long[]) type = long[].class;
		else if (input_ instanceof int[]) type = int[].class;
		else if (input_ instanceof boolean[]) type = boolean[].class;
		else if (input_ instanceof Object) type = Object.class;
		
		return type;
	}

	public static <x> Class<?> get_class_specific(x input_)
	{
		Class<?> type = get_class(input_);
		if (type == null || !are_equal(type, Array.class)) return type;

		if (input_ instanceof String[]) type = String[].class;
		else if (input_ instanceof Boolean[]) type = Boolean[].class;
		else if (input_ instanceof Integer[]) type = Integer[].class;
		else if (input_ instanceof Long[]) type = Long[].class;
		else if (input_ instanceof Double[]) type = Double[].class;
		else if (input_ instanceof Class[]) type = Class[].class;
		else if (input_ instanceof Method[]) type = Method[].class;
		else if (input_ instanceof Exception[]) type = Exception[].class;
		else if (input_ instanceof size[]) type = size[].class;
		else if (input_ instanceof data[]) type = data[].class;
		else if (input_ instanceof db_field[]) type = db_field[].class;
		else if (input_ instanceof db_where[]) type = db_where[].class;
		else if (input_ instanceof db_order[]) type = db_order[].class;
		
		return type;
	}

	public static boolean is_array_class(Class<?> class_)
	{
		if (class_ == null) return false;
		
		for (Class<?> class2: arrays.get_all_classes_array())
		{
			if (class_.equals(class2)) return true;
		}
		
		return false;
	}
	
	public static <x> boolean is_instance(x input_, Class<?> class_)
	{
		return (class_ != null && are_equal(class_, get_class(input_)));
	}

	public static Class<?>[] get_all_classes(boolean small_too_, boolean array_specific_too_)
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		classes.add(Class.class);
		classes.add(Method.class);
		classes.add(Exception.class);

		classes.add(size.class);
		classes.add(data.class);
		classes.add(db_field.class);
		classes.add(db_where.class);
		classes.add(db_order.class);
		
		classes.add(String.class);
		classes.add(Boolean.class);
		classes.add(Object.class);
		
		if (small_too_) classes.add(boolean.class);
		
		for (Class<?> type: numbers.get_all_classes(small_too_))
		{
			classes.add(type);
		}

		for (Class<?> type: arrays.get_all_classes(small_too_, array_specific_too_))
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
			errors.manage
			(
				types.ERROR_GENERIC_METHOD_GET, e, new String[] 
				{ 
					name_, strings.to_string(params_) 
				},
				error_exit_
			);
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
					method_.getName(), strings.to_string(args_)
				},
				error_exit_
			); 
		}

		return output;
	}
	
	private static <x> boolean is_common(x input_, Class<?>[] classes_, boolean is_class_)
	{
		for (Class<?> type: classes_)
		{
			if ((is_class_ && classes_are_equal((Class<?>)input_, type)) || (!is_class_ && is_instance(input_, type))) return true;
		}

		return false;
	}

	private static boolean classes_are_equal(Class<?> class1_, Class<?> class2_)
	{
		boolean is_ok1 = is_ok(class1_);
		boolean is_ok2 = is_ok(class2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		if (class1_.equals(class2_)) return true;
		
		for (Entry<Class<?>, Class<?>> equivalent: get_class_equivalents().entrySet())
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
	
	private static HashMap<Class<?>, Class<?>> get_class_equivalents()
	{
		HashMap<Class<?>, Class<?>> equivalents = new HashMap<Class<?>, Class<?>>();
		
		equivalents.put(Double.class, double.class);
		equivalents.put(Long.class, long.class);
		equivalents.put(Integer.class, int.class);
		equivalents.put(Boolean.class, boolean.class);
		equivalents.put(Double[].class, double[].class);
		equivalents.put(Long[].class, long[].class);
		equivalents.put(Integer[].class, int[].class);
		equivalents.put(Boolean[].class, boolean[].class);
		
		return equivalents;
	}
}