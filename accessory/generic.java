package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public abstract class generic extends parent_static 
{	
	public static final String ERROR_METHOD_GET = types.ERROR_GENERIC_METHOD_GET;
	public static final String ERROR_METHOD_CALL = types.ERROR_GENERIC_METHOD_CALL;

	public static String get_class_id() { return types.get_id(types.ID_GENERIC); }

	public static boolean is_ok(double[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(long[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(int[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(boolean[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(byte[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(char[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(Class<?> input_) { return is_ok(input_, false); }

	public static <x, y> boolean is_ok(HashMap<x, y> input_) { return is_ok(input_, false); }

	public static boolean is_ok(Object input_) { return is_ok(input_, false); }

	public static boolean is_class(Class<?> input_) { return is_common(input_, new Class<?>[] { Class.class }, true); }

	public static boolean is_class(Object input_) { return is_common(input_, new Class<?>[] { Class.class }, false); }

	public static boolean is_size(Class<?> input_) { return is_common(input_, new Class<?>[] { size.class }, true); }

	public static boolean is_size(Object input_) { return is_common(input_, new Class<?>[] { size.class }, false); }

	public static boolean is_data(Class<?> input_) { return is_common(input_, new Class<?>[] { data.class }, true); }

	public static boolean is_data(Object input_) { return is_common(input_, new Class<?>[] { data.class }, false); }

	public static boolean is_db_field(Class<?> input_) { return is_common(input_, new Class<?>[] { db_field.class }, true); }

	public static boolean is_db_field(Object input_) { return is_common(input_, new Class<?>[] { db_field.class }, false); }

	public static boolean is_db_where(Class<?> input_) { return is_common(input_, new Class<?>[] { db_where.class }, true); }

	public static boolean is_db_where(Object input_) { return is_common(input_, new Class<?>[] { db_where.class }, false); }

	public static boolean is_db_order(Class<?> input_) { return is_common(input_, new Class<?>[] { db_order.class }, true); }

	public static boolean is_db_order(Object input_) { return is_common(input_, new Class<?>[] { db_order.class }, false); }

	public static boolean is_string(Class<?> input_) { return is_common(input_, new Class<?>[] { String.class }, true); }

	public static boolean is_string(Object input_) { return is_common(input_, new Class<?>[] { String.class }, false); }

	public static boolean is_boolean(Class<?> input_) { return is_common(input_, new Class<?>[] { Boolean.class, boolean.class }, true); }

	public static boolean is_boolean(boolean input_) { return true; }

	public static boolean is_boolean(Boolean input_) { return true; }

	public static boolean is_boolean(Object input_) { return is_common(input_, new Class<?>[] { Boolean.class, boolean.class }, false); }

	public static boolean is_byte(Class<?> input_) { return is_common(input_, new Class<?>[] { Byte.class, byte.class }, true); }

	public static boolean is_byte(byte input_) { return true; }

	public static boolean is_byte(Byte input_) { return true; }

	public static boolean is_byte(Object input_) { return is_common(input_, new Class<?>[] { Byte.class, byte.class }, false); }

	public static boolean is_char(Class<?> input_) { return is_common(input_, new Class<?>[] { Character.class, char.class }, true); }

	public static boolean is_char(char input_) { return true; }

	public static boolean is_char(Character input_) { return true; }

	public static boolean is_char(Object input_) { return is_common(input_, new Class<?>[] { Character.class, char.class }, false); }

	public static boolean is_number(Class<?> input_) { return is_common(input_, numbers.get_all_classes(), true); }

	public static boolean is_number(Object input_) { return is_common(input_, numbers.get_all_classes(), false); }

	public static boolean is_array(Class<?> input_) { return is_common(input_, arrays.get_all_classes(), true); }

	public static boolean is_array(double[] input_) { return true; }

	public static boolean is_array(long[] input_) { return true; }

	public static boolean is_array(int[] input_) { return true; }

	public static boolean is_array(boolean[] input_) { return true; }

	public static boolean is_array(byte[] input_) { return true; }

	public static boolean is_array(char[] input_) { return true; }

	public static boolean is_array(Object input_) { return is_common(input_, arrays.get_all_classes(), false); }

	public static boolean get_random_boolean() { return (new Random()).nextBoolean(); }

	public static byte get_random_byte() { return get_random_bytes(1)[0]; }

	public static byte[] get_random_bytes(int tot) 
	{ 
		if (tot < 1) return null;

		byte[] output = new byte[tot];

		(new Random()).nextBytes(output);

		return output;
	}

	public static char get_random_char(boolean upper_, boolean numbers_, boolean symbols_) { return get_random_chars(1, upper_, numbers_, symbols_)[0]; }

	public static char[] get_random_chars(int tot_, boolean upper_, boolean numbers_, boolean symbols_) { return (tot_ < 1 ? null : strings.get_random(tot_, upper_, numbers_, symbols_).toCharArray()); }

	public static boolean are_equal(Class<?> input1_, Class<?> input2_) { return classes_are_equal(input1_, input2_); }

	public static Class<?> get_class(double input_) { return double.class; }

	public static Class<?> get_class(long input_) { return long.class; }

	public static Class<?> get_class(int input_) { return int.class; }

	public static Class<?> get_class(boolean input_) { return boolean.class; }

	public static Class<?> get_class(byte input_) { return byte.class; }

	public static Class<?> get_class(char input_) { return char.class; }

	public static boolean is_instance(Object input_, Class<?> class_) { return (class_ != null && are_equal(class_, get_class(input_))); }

	public static Class<?>[] get_all_classes() { return _alls.GENERIC_CLASSES; }

	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (class_ == null) return output;

		if (is_class(class_)) output = get_random_class();
		else if (is_string(class_)) output = strings.get_random(strings.SIZE_SMALL);
		else if (is_boolean(class_)) output = get_random_boolean();
		else if (is_number(class_)) output = numbers.get_random(class_);
		else if (is_byte(class_)) output = get_random_byte();
		else if (is_char(class_)) output = get_random_char(true, true, true);
		else if (is_array(class_)) output = arrays.get_random(class_); 
		else if (are_equal(class_, LocalDateTime.class)) output = dates.get_random_datetime(); 
		else if (are_equal(class_, LocalDate.class)) output = dates.get_random_date(); 
		else if (are_equal(class_, LocalTime.class)) output = dates.get_random_time(); 
		else if (class_.equals(Object.class)) output = arrays.get_random(String[].class);

		return output;
	}

	public static Class<?> get_random_class()
	{
		Class<?>[] haystack = get_all_classes();

		return haystack[numbers.get_random_index(haystack.length)];
	}

	public static boolean are_equal(Object input1_, Object input2_) { return are_equal(input1_, input2_, true); }	

	public static void to_screen(Object input_) { to_screen(input_, false); }

	public static void to_screen(Object input_, boolean add_timestamp_) { logs.update_screen(strings.to_string(input_), add_timestamp_); }

	public static int boolean_to_int(boolean input_) { return numbers.from_boolean(input_); }

	public static String boolean_to_string(boolean input_) { return strings.from_boolean(input_); }

	public static boolean int_to_boolean(int input_) { return numbers.to_boolean(input_); }

	public static boolean string_to_boolean(String input_) { return strings.to_boolean(input_); }

	public static Class<?> get_class(Object input_)
	{
		Class<?> type = null;
		if (input_ == null) return type;

		if (input_ instanceof String) type = String.class;
		else if (input_ instanceof Boolean) type = Boolean.class;
		else if (input_ instanceof Integer) type = Integer.class;
		else if (input_ instanceof Long) type = Long.class;
		else if (input_ instanceof Double) type = Double.class;
		else if (input_ instanceof Byte) type = Byte.class;
		else if (input_ instanceof Character) type = Character.class;
		else if (input_ instanceof Class<?>) type = Class.class;
		else if (input_ instanceof Method) type = Method.class;
		else if (input_ instanceof Exception) type = Exception.class;
		else if (input_ instanceof LocalDateTime) type = LocalDateTime.class;
		else if (input_ instanceof LocalDate) type = LocalDate.class;
		else if (input_ instanceof LocalTime) type = LocalTime.class;
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
		else if (input_ instanceof byte[]) type = byte[].class;
		else if (input_ instanceof char[]) type = char[].class;
		else if (input_ instanceof String[]) type = String[].class;
		else if (input_ instanceof Double[]) type = Double[].class;
		else if (input_ instanceof Long[]) type = Long[].class;
		else if (input_ instanceof Integer[]) type = Integer[].class;
		else if (input_ instanceof Boolean[]) type = Boolean[].class;
		else if (input_ instanceof Byte[]) type = Byte[].class;
		else if (input_ instanceof Character[]) type = Character[].class;
		else if (input_ instanceof Class[]) type = Class[].class;
		else if (input_ instanceof Method[]) type = Method[].class;
		else if (input_ instanceof Exception[]) type = Exception[].class;
		else if (input_ instanceof LocalDateTime[]) type = LocalDateTime[].class;
		else if (input_ instanceof LocalDate[]) type = LocalDate[].class;
		else if (input_ instanceof LocalTime[]) type = LocalTime[].class;
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

	public static Method[] get_all_methods(Class<?> class_, String[] skip_)
	{
		if (!is_ok(class_)) return null;

		ArrayList<Method> methods = new ArrayList<Method>();

		ArrayList<String> skip = arrays.to_arraylist(get_all_default_methods());
		skip.addAll(arrays.to_arraylist(skip_));

		for (Method method: class_.getMethods())
		{
			if (skip.contains(method.getName())) continue;

			methods.add(method);
		}

		return arrays.to_array(methods);
	}

	public static Method get_method(Class<?> class_, String name_, Class<?>[] params_)
	{
		method_start();

		Method output = null;
		if (!is_ok(class_) || !strings.is_ok(name_)) return output;

		try { output = class_.getMethod(name_, params_); } 
		catch (Exception e) 
		{
			HashMap<String, Object> info = new HashMap<String, Object>();
			info.put("class", class_);
			info.put("name", name_);
			info.put("params", params_);

			manage_error(ERROR_METHOD_GET, e, info);
		}

		method_end();

		return output;
	}

	public static Object call_static_method(Method method_, Object[] args_)
	{
		method_start();

		Object output = null;
		if (!is_ok(method_)) return output;

		try { output = method_.invoke(null, args_); } 
		catch (Exception e) 
		{  
			HashMap<String, Object> info = new HashMap<String, Object>();
			info.put("method", method_);
			info.put("args", args_);

			manage_error(ERROR_METHOD_CALL, e, info);
		}

		method_end();

		return output;
	}

	public static boolean class_exists(String name_) { return (get_class_from_name(name_) != null); }

	public static Class<?> get_class_from_name(String name_)
	{
		Class<?> output = null;

		try { output = Class.forName(name_); } 
		catch (Exception e) { output = null; }	

		return output;
	}

	static boolean is_ok(double[] input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static boolean is_ok(long[] input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static boolean is_ok(int[] input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static boolean is_ok(boolean[] input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static boolean is_ok(byte[] input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static boolean is_ok(char[] input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static boolean is_ok(Class<?> input_, boolean minimal_) { return (input_ != null); }

	static <x, y> boolean is_ok(HashMap<x, y> input_, boolean minimal_) { return arrays.is_ok(input_, minimal_); }

	static String[] populate_all_default_methods() { return new String[] { "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll", "ignore_errors", "ignore_errors_persistent_end", "__lock", "__unlock" }; }	

	static Class<?>[] populate_all_classes()
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		classes.add(Class.class);
		classes.add(Method.class);
		classes.add(Exception.class);
		classes.add(LocalDateTime.class);
		classes.add(LocalDate.class);
		classes.add(LocalTime.class);

		classes.add(size.class);
		classes.add(data.class);
		classes.add(db_field.class);
		classes.add(db_where.class);
		classes.add(db_order.class);

		classes.add(String.class);
		classes.add(Boolean.class);
		classes.add(boolean.class);
		classes.add(Byte.class);
		classes.add(byte.class);
		classes.add(Character.class);
		classes.add(char.class);
		classes.add(Object.class);

		for (Class<?> type: numbers.get_all_classes()) { classes.add(type); }
		for (Class<?> type: arrays.get_all_classes()) { classes.add(type); }

		return classes.toArray(new Class<?>[classes.size()]);
	}

	static HashMap<Class<?>, Class<?>[]> populate_all_class_equivalents()
	{
		HashMap<Class<?>, Class<?>[]> output = new HashMap<Class<?>, Class<?>[]>();

		output.put(Double.class, new Class<?>[] { double.class });
		output.put(Long.class, new Class<?>[] { long.class });
		output.put(Integer.class, new Class<?>[] { int.class });
		output.put(Boolean.class, new Class<?>[] { boolean.class });
		output.put(Byte.class, new Class<?>[] { byte.class });
		output.put(Character.class, new Class<?>[] { char.class });
		output.put(Double[].class, new Class<?>[] { double[].class });
		output.put(Long[].class, new Class<?>[] { long[].class });
		output.put(Integer[].class, new Class<?>[] { int[].class });
		output.put(Boolean[].class, new Class<?>[] { boolean[].class });
		output.put(Byte[].class, new Class<?>[] { byte[].class });
		output.put(Character[].class, new Class<?>[] { char[].class });
		output.put(Array.class, new Class<?>[] { Object[].class });

		return output;
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

	static boolean are_equal(Object input1_, Object input2_, boolean check_inputs_)
	{
		if (check_inputs_)
		{
			boolean is_ok1 = is_ok(input1_, true);
			boolean is_ok2 = is_ok(input2_, true);
			if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);			
		}

		boolean output = false;

		Class<?> type1 = get_class(input1_);
		Class<?> type2 = get_class(input2_);
		if (type1 == null || type2 == null || !classes_are_equal(type1, type2)) return output;

		if (is_array(type1)) output = arrays.are_equal(input1_, input2_);
		else output = input1_.equals(input2_);

		return output;
	}

	private static String[] get_all_default_methods() { return _alls.GENERIC_DEFAULT_METHOD_NAMES; }

	private static HashMap<Class<?>, Class<?>[]> get_all_class_equivalents() { return _alls.GENERIC_CLASSES_EQUIVALENTS; }

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

		for (Entry<Class<?>, Class<?>[]> item: get_all_class_equivalents().entrySet())
		{
			Class<?> key = item.getKey();

			for (Class<?> val: item.getValue())
			{
				if ((class1_.equals(key) && class2_.equals(val)) || (class1_.equals(val) && class2_.equals(key))) return true;	
			}
		}

		return false;
	}
}