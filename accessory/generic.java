package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public abstract class generic 
{
	public static final String TYPE = types.what_to_key(types.WHAT_TYPE);
	public static final String KEY = types.what_to_key(types.WHAT_KEY);
	public static final String VALUE = types.what_to_key(types.WHAT_VALUE);
	public static final String FURTHER = types.what_to_key(types.WHAT_FURTHER);
	public static final String INFO = types.what_to_key(types.WHAT_INFO);
	public static final String MIN = types.what_to_key(types.WHAT_MIN);
	public static final String MAX = types.what_to_key(types.WHAT_MAX);	
	public static final String USERNAME = types.what_to_key(types.WHAT_USERNAME);
	public static final String PASSWORD = types.what_to_key(types.WHAT_PASSWORD);
	public static final String ID = types.what_to_key(types.WHAT_ID);
	public static final String QUERY = types.what_to_key(types.WHAT_QUERY);
	
	static { ini.load(); }
	
	public static boolean is_ok(double[] input_)
	{
		return is_ok(input_, false);
	}
	
	public static boolean is_ok(long[] input_)
	{
		return is_ok(input_, false);
	}
	
	public static boolean is_ok(int[] input_)
	{
		return is_ok(input_, false);
	}
	
	public static boolean is_ok(boolean[] input_)
	{
		return is_ok(input_, false);
	}

	public static boolean is_ok(Class<?> input_)
	{
		return is_ok(input_, false);
	}

	public static <x, y> boolean is_ok(HashMap<x, y> input_)
	{
		return is_ok(input_, false);
	}
	
	public static boolean is_ok(Object input_)
	{
		return is_ok(input_, false);
	}

	public static boolean is_class(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { Class.class }, true);
	}
	
	public static boolean is_class(Object input_)
	{
		return is_common(input_, new Class<?>[] { Class.class }, false);
	}

	public static boolean is_size(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { size.class }, true);
	}
	
	public static boolean is_size(Object input_)
	{
		return is_common(input_, new Class<?>[] { size.class }, false);
	}

	public static boolean is_data(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { data.class }, true);
	}
	
	public static boolean is_data(Object input_)
	{
		return is_common(input_, new Class<?>[] { data.class }, false);
	}

	public static boolean is_db_field(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { db_field.class }, true);
	}
	
	public static boolean is_db_field(Object input_)
	{
		return is_common(input_, new Class<?>[] { db_field.class }, false);
	}

	public static boolean is_db_where(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { db_where.class }, true);
	}
	
	public static boolean is_db_where(Object input_)
	{
		return is_common(input_, new Class<?>[] { db_where.class }, false);
	}

	public static boolean is_db_order(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { db_order.class }, true);
	}
	
	public static boolean is_db_order(Object input_)
	{
		return is_common(input_, new Class<?>[] { db_order.class }, false);
	}
	
	public static boolean is_string(Class<?> input_)
	{
		return is_common(input_, new Class<?>[] { String.class }, true);
	}
	
	public static boolean is_string(Object input_)
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
	
	public static boolean is_boolean(Object input_)
	{
		return is_common(input_, new Class<?>[] { Boolean.class, boolean.class }, false);
	}

	public static boolean is_number(Class<?> input_)
	{
		return is_common(input_, numbers.get_all_classes(), true);
	}
	
	public static boolean is_number(Object input_)
	{
		return is_common(input_, numbers.get_all_classes(), false);
	}

	public static boolean is_array(Class<?> input_)
	{
		return is_common(input_, arrays.get_all_classes(), true);
	}
	
	public static boolean is_array(double[] input_)
	{
		return true;
	}
	
	public static boolean is_array(long[] input_)
	{
		return true;
	}
	
	public static boolean is_array(int[] input_)
	{
		return true;
	}
	
	public static boolean is_array(boolean[] input_)
	{
		return true;
	}
	
	public static boolean is_array(Object input_)
	{
		return is_common(input_, arrays.get_all_classes(), false);
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
	
	public static Object get_new(Object input_)
	{
		Object output = null;
		if (!is_ok(input_, true)) return output;
		
		Class<?> type = get_class(input_);
		if (type == null) return output;
		
		if (type.equals(double[].class)) output = get_new((double[])input_);
		else if (type.equals(long[].class)) output = get_new((long[])input_);
		else if (type.equals(int[].class)) output = get_new((int[])input_);
		else if (type.equals(boolean[].class)) output = get_new((boolean[])input_);
		else if (is_array(type)) output = arrays.get_new(input_);
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
		
		if (class_.equals(Object.class) || class_.equals(Object[].class)) output = arrays.get_random(String[].class);
		else if (is_class(class_)) output = get_random_class();
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

	public static boolean are_equal(Object input1_, Object input2_)
	{
		boolean is_ok1 = is_ok(input1_, true);
		boolean is_ok2 = is_ok(input2_, true);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		boolean output = false;

		Class<?> type1 = get_class(input1_);
		Class<?> type2 = get_class(input2_);
		if (type1 == null || type2 == null || !classes_are_equal(type1, type2)) return output;
		
		if (is_array(type1)) output = arrays.are_equal(input1_, input2_);
		else output = input1_.equals(input2_);
		
		return output;
	}	
	
	public static Class<?> get_class(double input_)
	{
		return double.class;
	}
	
	public static Class<?> get_class(long input_)
	{
		return long.class;
	}
	
	public static Class<?> get_class(int input_)
	{
		return int.class;
	}
	
	public static Class<?> get_class(boolean input_)
	{
		return boolean.class;
	}
	
	public static Class<?> get_class(Object input_)
	{
		Class<?> type = null;
		if (input_ == null) return type;
		
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
		else if (input_ instanceof double[]) type = double[].class;
		else if (input_ instanceof long[]) type = long[].class;
		else if (input_ instanceof int[]) type = int[].class;
		else if (input_ instanceof boolean[]) type = boolean[].class;
		else if (input_ instanceof String[]) type = String[].class;
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
		else if (input_ instanceof Object[]) type = Array.class; //It has to go after all the specific array classes (small native like double[] not included).
		else if (input_ instanceof Object) type = Object.class;
		
		return type;
	}

	//Targeting all the classes equivalent to Array.class/Object[].class, not the case of native small like double[].
	public static boolean is_array_class(Class<?> class_)
	{
		if (class_ == null) return false;
		
		for (Class<?> class2: arrays.get_all_classes_array())
		{
			if (class_.equals(class2)) return true;
		}
		
		return false;
	}
	
	public static boolean is_instance(Object input_, Class<?> class_)
	{
		return (class_ != null && are_equal(class_, get_class(input_)));
	}

	public static Class<?>[] get_all_classes()
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
		classes.add(boolean.class);
		classes.add(Object.class);
		
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

	public static Object call_static_method(Method method_, Object[] args_, boolean error_exit_)
	{
		Object output = null;
		if (!is_ok(method_)) return output;

		try 
		{
			output = method_.invoke(null, args_);
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
	
	static boolean is_ok(double[] input_, boolean minimal_)
	{
		return arrays.is_ok(input_, minimal_);
	}
	
	static boolean is_ok(long[] input_, boolean minimal_)
	{
		return arrays.is_ok(input_, minimal_);
	}
	
	static boolean is_ok(int[] input_, boolean minimal_)
	{
		return arrays.is_ok(input_, minimal_);
	}
	
	static boolean is_ok(boolean[] input_, boolean minimal_)
	{
		return arrays.is_ok(input_, minimal_);
	}

	static boolean is_ok(Class<?> input_, boolean minimal_)
	{
		return (input_ != null);
	}

	static <x, y> boolean is_ok(HashMap<x, y> input_, boolean minimal_)
	{
		return arrays.is_ok(input_, minimal_);
	}

	static boolean is_ok(Object input_, boolean minimal_)
	{
		boolean is_ok = false;
		if (input_ == null) return is_ok;
		
		if (is_string(input_)) is_ok = strings.is_ok((String)input_, minimal_);
		else if (is_array(input_)) is_ok = arrays.is_ok(input_, minimal_);
		else if (is_boolean(input_) || is_number(input_)) is_ok = true;
		else if (minimal_) is_ok = true;		
		if (minimal_) return is_ok;
		
		if (is_data(input_)) is_ok = data.is_ok((data)input_);
		else if (is_size(input_)) is_ok = size.is_ok((size)input_);
		else if (is_db_field(input_)) is_ok = db_field.is_ok((db_field)input_);
		else if (is_db_where(input_)) is_ok = db_where.is_ok((db_where)input_);
		else if (is_db_order(input_)) is_ok = db_order.is_ok((db_order)input_);
		else is_ok = true;

		return is_ok;
	}

	private static boolean is_common(Object input_, Class<?>[] classes_, boolean is_class_)
	{
		if (input_ == null) return false;
		
		for (Class<?> type: classes_)
		{
			if ((is_class_ && classes_are_equal((Class<?>)input_, type)) || (!is_class_ && is_instance(input_, type))) return true;
		}

		return false;
	}

	//This method returns "true" for classes which are indeed virtually identical, at least for most purposes.
	//But there are still some caveats which have to be fully understood before calling it. For example, the
	//peculiarities when dealing with native big/small types (e.g., Double[]/double[]), assumed to be identical here.
	private static boolean classes_are_equal(Class<?> class1_, Class<?> class2_)
	{
		boolean is_ok1 = is_ok(class1_);
		boolean is_ok2 = is_ok(class2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		if (class1_.equals(class2_)) return true;
		
		for (Entry<Class<?>, Class<?>> item: get_class_equals().entrySet())
		{
			Class<?> key = item.getKey();
			Class<?> val = item.getValue();
			if 
			(
				(class1_.equals(key) && class2_.equals(val)) || 
				(class1_.equals(val) && class2_.equals(key))
			)
			{ return true; }
		}
		
		return false;
	}

	private static String[] get_default_method_names()
	{
		return new String[]
		{
			"wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll"
		};
	}
	
	private static HashMap<Class<?>, Class<?>> get_class_equals()
	{
		HashMap<Class<?>, Class<?>> output = new HashMap<Class<?>, Class<?>>();
		
		output.put(Double.class, double.class);
		output.put(Long.class, long.class);
		output.put(Integer.class, int.class);
		output.put(Boolean.class, boolean.class);
		output.put(Double[].class, double[].class);
		output.put(Long[].class, long[].class);
		output.put(Integer[].class, int[].class);
		output.put(Boolean[].class, boolean[].class);
		output.put(Object[].class, Array.class);
		
		return output;
	}
}