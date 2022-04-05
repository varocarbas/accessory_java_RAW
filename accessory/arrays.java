package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class arrays 
{
	public static final int DEFAULT_SIZE = _defaults.SIZE_ARRAY;
	
	static { ini.load(); }

	public static Class<?>[] get_all_classes()
	{
		return _alls._arrays_classes;
	}

	//All these classes require a special treatment. In some cases, they are assumed to be equivalent to their big counterparts.
	//Their instances can only be used as Object/x[] after having been converted into their corresponding big counterparts.
	//Methods with Object/x[] parameters have to include specific overloads for all these classes. Additionally, they have 
	//to internally (i.e., in the overload including the Object parameter) account for these scenarios too, because some calls 
	//might not be reaching the specific overloads (e.g., Object val = new int[] { 1, 2, 3 } going to method(Object input_)).
	//!!!
	public static Class<?>[] get_all_classes_small()
	{
		return _alls._arrays_classes_small;
	}
	
	//All these classes are equivalent to Array.class and their instances can be used as Object/x[].
	public static Class<?>[] get_all_classes_array()
	{
		return _alls._arrays_classes_array;
	}
	
	public static Class<?>[] get_all_classes_numeric()
	{
		return _alls._arrays_classes_numeric;
	}
	
	public static <x> boolean is_ok(ArrayList<ArrayList<x>> input_)
	{
		return is_ok(input_, false);
	}
		
	public static <x, y> boolean is_ok(HashMap<x, y> input_)
	{
		return is_ok(input_, false);
	}
	
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
	
	public static boolean is_ok(byte[] input_)
	{
		return is_ok(input_, false);
	}
	
	public static boolean is_ok(Object input_)
	{
		return is_ok(input_, false);
	}
	
	public static <x> int get_size(ArrayList<ArrayList<x>> input_)
	{
		return (input_ == null ? 0 : get_size_arraylist(input_));		
	}
	
	public static <x, y> int get_size(HashMap<x, y> input_)
	{
		return (input_ == null ? 0 : get_size_hashmap_xy(input_));		
	}
	
	public static int get_size(double[] input_)
	{
		return (input_ == null ? 0 : input_.length);
	}
	
	public static int get_size(long[] input_)
	{
		return (input_ == null ? 0 : input_.length);
	}
	
	public static int get_size(int[] input_)
	{
		return (input_ == null ? 0 : input_.length);
	}
	
	public static int get_size(boolean[] input_)
	{
		return (input_ == null ? 0 : input_.length);
	}
	
	public static int get_size(byte[] input_)
	{
		return (input_ == null ? 0 : input_.length);
	}
	
	@SuppressWarnings("unchecked")
	public static <x> int get_size(Object input_)
	{
		int size = 0;
		
		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) return size;
		
		if (type.equals(double[].class)) size = get_size((double[])input_);
		else if (type.equals(long[].class)) size = get_size((long[])input_);
		else if (type.equals(int[].class)) size = get_size((int[])input_);
		else if (type.equals(boolean[].class)) size = get_size((boolean[])input_);
		else if (type.equals(byte[].class)) size = get_size((byte[])input_);
		else if (generic.are_equal(type, ArrayList.class)) size = get_size_arraylist((ArrayList<x>)input_);
		else if (generic.are_equal(type, HashMap.class)) size = get_size_hashmap_xx((HashMap<x, x>)input_); 
		else size = ((Object[])input_).length;
		
		return size;
	}
	
	public static <x> boolean are_equal(ArrayList<ArrayList<x>> input1_, ArrayList<ArrayList<x>> input2_)
	{
		if (!is_arraylist_xx(input1_) || !is_arraylist_xx(input2_)) return false;
		
		boolean is_ok1 = is_ok(input1_, true);
		boolean is_ok2 = is_ok(input2_, true);	
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		return (((get_size(input1_) != get_size(input2_)) || !generic.are_equal(get_class_items(input1_), get_class_items(input2_))) ? false : input1_.equals(input2_));
	}
	
	public static <x, y> boolean are_equal(HashMap<x, y> input1_, HashMap<x, y> input2_)
	{
		boolean is_ok1 = is_ok(input1_, true);
		boolean is_ok2 = is_ok(input2_, true);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		int size = get_size(input1_);
		Class<?> type_key = get_class_key_xy(input1_);
		Class<?> type_val = get_class_val_xy(input1_);
		
		return (((size == get_size(input2_)) && generic.are_equal(type_key, get_class_key_xy(input2_)) && generic.are_equal(type_val, get_class_val_xy(input2_))) ? input1_.equals(input2_) : false);
	}
	
	public static boolean are_equal(double[] input1_, double[] input2_)
	{
		return are_equal(to_big(input1_), to_big(input2_));
	}
	
	public static boolean are_equal(long[] input1_, long[] input2_)
	{
		return are_equal(to_big(input1_), to_big(input2_));
	}
	
	public static boolean are_equal(int[] input1_, int[] input2_)
	{
		return are_equal(to_big(input1_), to_big(input2_));
	}
	
	public static boolean are_equal(boolean[] input1_, boolean[] input2_)
	{
		return are_equal(to_big(input1_), to_big(input2_));
	}
	
	public static boolean are_equal(byte[] input1_, byte[] input2_)
	{
		return are_equal(to_big(input1_), to_big(input2_));
	}
	
	@SuppressWarnings("unchecked")
	public static <x> boolean are_equal(Object input1_, Object input2_)
	{
		boolean is_ok1 = is_ok(input1_, true);
		boolean is_ok2 = is_ok(input2_, true);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);
	
		int size = get_size(input1_);
		Class<?> type = generic.get_class(input1_);
		if (!generic.is_array(type) || !generic.are_equal(type, generic.get_class(input2_)) || size != get_size(input2_)) return false;

		if (type.equals(double[].class)) return are_equal((double[])input1_, (double[])input2_);
		else if (type.equals(long[].class)) return are_equal((long[])input1_, (long[])input2_);
		else if (type.equals(int[].class)) return are_equal((int[])input1_, (int[])input2_);
		else if (type.equals(boolean[].class)) return are_equal((boolean[])input1_, (boolean[])input2_);
		else if (type.equals(byte[].class)) return are_equal((byte[])input1_, (byte[])input2_);
		else if (generic.are_equal(type, HashMap.class))
		{
			return 
			(
				(
					generic.are_equal(get_class_key_xx((HashMap<x, x>)input1_), get_class_key_xx((HashMap<x, x>)input2_)) &&
					generic.are_equal(get_class_val_xx((HashMap<x, x>)input1_), get_class_val_xx((HashMap<x, x>)input2_))
				)
				? input1_.equals(input2_) : false
			);
		}
		else
		{
			if (generic.are_equal(type, ArrayList.class))
			{
				return (generic.are_equal(get_class_items((ArrayList<x>)input1_), get_class_items((ArrayList<x>)input2_)) ? input1_.equals(input2_) : false);
			}
			else if (!generic.are_equal(get_class_items((x[])input1_), get_class_items((x[])input2_))) return false;
		}
		
		x[] input1 = (x[])input1_;
		x[] input2 = (x[])input2_;
		
		for (int i = 0; i < size; i++)
		{
			if (!generic.is_ok(input1[i], true)) 
			{
				if (generic.is_ok(input2[i], true)) return false;
			}
			else if (!generic.are_equal(input1[i], input2[i])) return false;
		}
				
		return true;
	}
	
	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (!generic.is_array(class_)) return output;

		if 
		(
			generic.are_equal(class_, Array.class) || generic.are_equal(class_, String[].class) || 
			generic.are_equal(class_, Boolean[].class) || generic.are_equal(class_, Class[].class) ||
			value_exists(get_all_classes_numeric(), class_)
		)
		{
			output = get_random_array(class_);
		}
		else if (generic.are_equal(class_, ArrayList.class)) output = get_random_arraylist();
		else if (generic.are_equal(class_, HashMap.class)) output = get_random_hashmap(); 

		return output;
	}

	public static Object get_random_array(Class<?> class_)
	{
		Object output = null;
		if (class_ == null) return output;
		
		if (generic.are_equal(class_, Array.class) || generic.are_equal(class_, String[].class))
		{
			String[] temp = new String[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = strings.get_random(strings.SIZE_SMALL);
			}
			
			output = temp;
		}
		else if (class_.equals(double[].class))
		{
			double[] temp = new double[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (double)numbers.get_random(Double.class);
			}
			
			output = temp;
		}
		else if (class_.equals(Double[].class))
		{
			Double[] temp = new Double[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (Double)numbers.get_random(Double.class);
			}
			
			output = temp;
		}
		else if (class_.equals(long[].class))
		{
			long[] temp = new long[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (long)numbers.get_random(Long.class);
			}
			
			output = temp;
		}
		else if (class_.equals(Long[].class))
		{
			Long[] temp = new Long[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (Long)numbers.get_random(Long.class);
			}
			
			output = temp;
		}
		else if (class_.equals(int[].class))
		{
			int[] temp = new int[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (int)numbers.get_random(Integer.class);
			}
			
			output = temp;
		}
		else if (class_.equals(Integer[].class))
		{
			Integer[] temp = new Integer[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (Integer)numbers.get_random(Integer.class);
			}
			
			output = temp;
		}
		else if (class_.equals(boolean[].class))
		{
			boolean[] temp = new boolean[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = generic.get_random_boolean();
			}
			
			output = temp;
		}
		else if (class_.equals(Boolean[].class))
		{
			Boolean[] temp = new Boolean[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = (Boolean)generic.get_random_boolean();
			}
			
			output = temp;
		}
		else if (generic.are_equal(class_, Class[].class))
		{
			Class<?>[] temp = new Class<?>[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++)
			{
				temp[i] = generic.get_random_class();
			}
			
			output = temp;
		}
		
		return output;
	}

	public static ArrayList<Object> get_random_arraylist()
	{
		ArrayList<Object> output = new ArrayList<Object>();
		
		for (int i = 0; i < DEFAULT_SIZE; i++)
		{
			output.add(strings.get_random(strings.SIZE_SMALL));
		}
		
		return output;		
	}

	public static HashMap<Object, Object> get_random_hashmap()
	{
		HashMap<Object, Object> output = new HashMap<Object, Object>();
		
		for (int i = 0; i < DEFAULT_SIZE; i++)
		{
			output.put(Integer.toString(i), strings.get_random(strings.SIZE_SMALL));
		}
		
		return output;	
	}

	@SuppressWarnings("unchecked")
	public static <x> x[] to_array(ArrayList<ArrayList<x>> input_)
	{
		return (is_arraylist_xx(input_) ? null : to_array_internal((ArrayList<x>)input_));
	}
	
	@SuppressWarnings("unchecked")
	public static <x> x[] to_array(Object input_)
	{
		return (generic.are_equal(generic.get_class(input_), ArrayList.class) ? to_array_internal((ArrayList<x>)input_) : null);
	}
	
	public static <x, y> HashMap<x, y> get_new(HashMap<x, y> input_)
	{
		return (!is_ok(input_) ? new HashMap<x, y>() : new HashMap<x, y>(input_));
	}
	
	public static <x> ArrayList<ArrayList<x>> get_new(ArrayList<ArrayList<x>> input_)
	{
		return (!is_ok(input_) ? new ArrayList<ArrayList<x>>() : new ArrayList<ArrayList<x>>(input_));
	}

	public static double[] get_new(double[] input_)
	{
		return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length));
	}
	
	public static long[] get_new(long[] input_)
	{
		return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length));
	}
	
	public static int[] get_new(int[] input_)
	{
		return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length));
	}
	
	public static boolean[] get_new(boolean[] input_)
	{
		return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length));
	}
	
	public static byte[] get_new(byte[] input_)
	{
		return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length));
	}
	
	@SuppressWarnings("unchecked")
	public static <x> Object get_new(Object input_)
	{
		Object output = null;
		if (!is_ok(input_)) return output;

		Class<?> type = generic.get_class(input_);
		if (type == null) return output;
		
		if (type.equals(double[].class)) output = get_new((double[])input_);
		else if (type.equals(long[].class)) output = get_new((long[])input_);
		else if (type.equals(int[].class)) output = get_new((int[])input_);
		else if (type.equals(boolean[].class)) output = get_new((boolean[])input_);
		else if (type.equals(byte[].class)) output = get_new((byte[])input_);
		else if (generic.are_equal(type, ArrayList.class)) output = get_new_arraylist((ArrayList<x>)input_);
		else if (generic.are_equal(type, HashMap.class)) output = get_new_hashmap((HashMap<x, x>)input_); 
		else output = get_new_array((x[])input_);
		
		return output;
	}
	
	public static ArrayList<Double> to_arraylist(double[] input_)
	{
		return (is_ok(input_) ? new ArrayList<Double>(Arrays.asList((Double[])to_big(input_))) : new ArrayList<Double>());
	}
	
	public static ArrayList<Long> to_arraylist(long[] input_)
	{
		return (is_ok(input_) ? new ArrayList<Long>(Arrays.asList((Long[])to_big(input_))) : new ArrayList<Long>());
	}
	
	public static ArrayList<Integer> to_arraylist(int[] input_)
	{
		return (is_ok(input_) ? new ArrayList<Integer>(Arrays.asList((Integer[])to_big(input_))) : new ArrayList<Integer>());
	}
	
	public static ArrayList<Boolean> to_arraylist(boolean[] input_)
	{
		return (is_ok(input_) ? new ArrayList<Boolean>(Arrays.asList((Boolean[])to_big(input_))) : new ArrayList<Boolean>());
	}
	
	public static ArrayList<Byte> to_arraylist(byte[] input_)
	{
		return (is_ok(input_) ? new ArrayList<Byte>(Arrays.asList((Byte[])to_big(input_))) : new ArrayList<Byte>());
	}
	
	public static <x> ArrayList<x> to_arraylist(x[] input_)
	{
		return (is_ok(input_) ? new ArrayList<x>(Arrays.asList(input_)) : new ArrayList<x>());
	}	

	public static double[] get_range(double[] input_, int start_i, int size_)
	{
		return (double[])to_small(get_range((Double[])to_big(input_), start_i, size_));
	}

	public static long[] get_range(long[] input_, int start_i, int size_)
	{
		return (long[])to_small(get_range((Long[])to_big(input_), start_i, size_));
	}

	public static int[] get_range(int[] input_, int start_i, int size_)
	{
		return (int[])to_small(get_range((Integer[])to_big(input_), start_i, size_));
	}

	public static boolean[] get_range(boolean[] input_, int start_i, int size_)
	{
		return (boolean[])to_small(get_range((Boolean[])to_big(input_), start_i, size_));
	}

	public static byte[] get_range(byte[] input_, int start_i, int size_)
	{
		return (byte[])to_small(get_range((Byte[])to_big(input_), start_i, size_));
	}
	
	public static <x> x[] get_range(x[] input_, int start_i, int size_)
	{
		if (!is_ok(input_) || start_i < 0 || size_ < 0) return null;
		
		int size0 = get_size(input_);
		int size = (size_ < 1 ? size0 : start_i + size_);

		return (size > size0 ? null : Arrays.copyOfRange(input_, start_i, size));
	}

	public static <x, y> HashMap<x, y> remove_key(HashMap<x, y> array_, x key_, boolean normalise_)
	{
		return remove_key_value(array_, key_, normalise_, true);
	}

	public static <x, y> HashMap<x, y> remove_value(HashMap<x, y> array_, y value_, boolean normalise_)
	{
		return remove_key_value(array_, value_, normalise_, false);
	}

	public static <x> Object remove_key(Object array_, x key_, boolean normalise_)
	{
		return remove_key_value(array_, key_, normalise_, generic.are_equal(generic.get_class(array_), HashMap.class));
	}

	public static double[] remove_value(double[] array_, double value_, boolean normalise_)
	{
		return arrays.to_small((Double[])remove_key_value(arrays.to_big(array_), value_, normalise_, false));
	}

	public static long[] remove_value(long[] array_, long value_, boolean normalise_)
	{
		return arrays.to_small((Long[])remove_key_value(arrays.to_big(array_), value_, normalise_, false));
	}

	public static int[] remove_value(int[] array_, int value_, boolean normalise_)
	{
		return arrays.to_small((Integer[])remove_key_value(arrays.to_big(array_), value_, normalise_, false));
	}

	public static boolean[] remove_value(boolean[] array_, boolean value_, boolean normalise_)
	{
		return arrays.to_small((Boolean[])remove_key_value(arrays.to_big(array_), value_, normalise_, false));
	}

	public static byte[] remove_value(byte[] array_, byte value_, boolean normalise_)
	{
		return arrays.to_small((Byte[])remove_key_value(arrays.to_big(array_), value_, normalise_, false));
	}
	
	public static <x> Object remove_value(Object array_, x value_, boolean normalise_)
	{
		return remove_key_value(array_, value_, normalise_, false);
	}
	
	@SuppressWarnings("unchecked")
	public static <x, y> y get_value(HashMap<x, y> array_, x key_)
	{
		return (y)(!is_ok(array_) ? null : key_value_get_exists(array_, key_, null, true, true));
	}

	@SuppressWarnings("unchecked")
	public static <x> x get_value(Object array_, x key_)
	{
		return (x)(!is_ok(array_) ? null : key_value_get_exists(array_, key_, false, true));
	}

	public static <x, y> boolean keys_exist(HashMap<x, y> array_, double[] keys_)
	{
		return keys_exist(array_, (Double[])to_big(keys_));
	}

	public static <x, y> boolean keys_exist(HashMap<x, y> array_, long[] keys_)
	{
		return keys_exist(array_, (Long[])to_big(keys_));
	}

	public static <x, y> boolean keys_exist(HashMap<x, y> array_, int[] keys_)
	{
		return keys_exist(array_, (Integer[])to_big(keys_));
	}

	public static <x, y> boolean keys_exist(HashMap<x, y> array_, boolean[] keys_)
	{
		return keys_exist(array_, (Boolean[])to_big(keys_));
	}
	
	public static <x, y> boolean keys_exist(HashMap<x, y> array_, x[] keys_)
	{
		if (!is_ok(array_) || !is_ok(keys_)) return false;

		for (x key: keys_)
		{
			if (key_exists(array_, key)) return true;
		}

		return false;
	}

	public static <x, y> boolean key_exists(HashMap<x, y> array_, x key_)
	{
		return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, null, true, false));	
	}
	
	public static <x> boolean keys_exist(Object array_, double[] keys_)
	{
		return keys_exist(array_, (Double[])to_big(keys_));
	}
	
	public static <x> boolean keys_exist(Object array_, long[] keys_)
	{
		return keys_exist(array_, (Long[])to_big(keys_));
	}
	
	public static <x> boolean keys_exist(Object array_, int[] keys_)
	{
		return keys_exist(array_, (Integer[])to_big(keys_));
	}
	
	public static <x> boolean keys_exist(Object array_, boolean[] keys_)
	{
		return keys_exist(array_, (Boolean[])to_big(keys_));
	}
	
	public static <x> boolean keys_exist(Object array_, x[] keys_)
	{
		if (!is_ok(array_) || !is_ok(keys_) || !generic.are_equal(generic.get_class(array_), HashMap.class)) return false;

		for (x key: keys_)
		{
			if (key_exists(array_, key)) return true;
		}

		return false;
	}

	public static <x> boolean key_exists(Object array_, x key_)
	{
		return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false));		
	}

	public static <x, y> boolean value_exists(HashMap<x, y> array_, y value_)
	{
		return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, null, value_, false, false));
	}

	public static boolean value_exists(double[] array_, double value_)
	{
		return value_exists(to_big(array_), (Double)value_);
	}
	
	public static boolean value_exists(long[] array_, long value_)
	{
		return value_exists(to_big(array_), (Long)value_);
	}
	
	public static boolean value_exists(int[] array_, int value_)
	{
		return value_exists(to_big(array_), (Integer)value_);
	}
	
	public static boolean value_exists(boolean[] array_, boolean value_)
	{
		return value_exists(to_big(array_), (Boolean)value_);
	}
	
	public static boolean value_exists(byte[] array_, byte value_)
	{
		return value_exists(to_big(array_), (Byte)value_);
	}
	
	public static <x> boolean value_exists(Object array_, x value_)
	{
		return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false));
	}
		
	public static <x, y> String to_string(HashMap<x, y> input_, String separator1_, String separator2_, String[] keys_ignore_)
	{
		return to_string_internal(input_, separator1_, separator2_, keys_ignore_, false);
	}
	
	public static <x> String to_string(Object input_, String separator1_, String separator2_, String[] keys_ignore_)
	{
		return (generic.are_equal(generic.get_class(input_), HashMap.class) ? to_string_internal(input_, separator1_, separator2_, keys_ignore_, false) : strings.DEFAULT);
	}

	public static String to_string(double[] input_, String separator_)
	{
		return to_string(to_big(input_), separator_);
	}
	
	public static String to_string(long[] input_, String separator_)
	{
		return to_string(to_big(input_), separator_);
	}
	
	public static String to_string(int[] input_, String separator_)
	{
		return to_string(to_big(input_), separator_);
	}
	
	public static String to_string(boolean[] input_, String separator_)
	{
		return to_string(to_big(input_), separator_);
	}
	
	@SuppressWarnings("unchecked")
	public static <x> String to_string(Object input_, String separator_)
	{
		String output = strings.DEFAULT;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) return output;
		
		if (type.equals(double[].class)) return to_string((double[])input_, separator_);
		else if (type.equals(long[].class)) return to_string((long[])input_, separator_);
		else if (type.equals(int[].class)) return to_string((int[])input_, separator_);
		else if (type.equals(boolean[].class)) return to_string((boolean[])input_, separator_);
		
		boolean is_array = generic.is_array_class(type);
		boolean is_arraylist = generic.are_equal(type, ArrayList.class);
		if (!is_array && !is_arraylist) return output;

		output = "";
		String separator = (strings.is_ok(separator_) ? separator_ : misc.SEPARATOR_ITEM);
		boolean first_time = true;
		
		if (is_array)
		{
			for (x item: (x[])input_)
			{
				if (first_time) first_time = false;
				else output += separator;

				output += to_string_val(item);
			}	
		}
		else if (is_arraylist)
		{
			for (x item: (ArrayList<x>)input_)
			{
				if (first_time) first_time = false;
				else output += separator;

				output += to_string_val(item);
			}
		}

		if (!first_time) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;

		return output;
	}
	
	public static String lines_to_string(String[] lines_)
	{
		if (!is_ok(lines_)) return strings.DEFAULT;
		
		String output = "";
		
		for (String line: lines_)
		{
			if (!strings.is_ok(line)) continue;
			if (!output.equals("")) output += misc.NEW_LINE;
			
			output += line;
		}
		
		return output;
	}

	public static Double[] to_big(double[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		Double[] output = new Double[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Double)input_[i];
		}
		
		return output;
	}
	
	public static Long[] to_big(long[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		Long[] output = new Long[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Long)input_[i];
		}
		
		return output;
	}
	
	public static Integer[] to_big(int[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		Integer[] output = new Integer[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Integer)input_[i];
		}
		
		return output;
	}
	
	public static Boolean[] to_big(boolean[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		Boolean[] output = new Boolean[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Boolean)input_[i];
		}
		
		return output;
	}
	
	public static Byte[] to_big(byte[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		Byte[] output = new Byte[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Byte)input_[i];
		}
		
		return output;
	}
	
	public static double[] to_small(Double[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		double[] output = new double[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Double)input_[i];
		}
		
		return output;
	}
	
	public static long[] to_small(Long[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		long[] output = new long[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Long)input_[i];
		}
		
		return output;
	}
	
	public static int[] to_small(Integer[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		int[] output = new int[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (Integer)input_[i];
		}
		
		return output;
	}
	
	public static boolean[] to_small(Boolean[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		boolean[] output = new boolean[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (boolean)input_[i];
		}
		
		return output;
	}
	
	public static byte[] to_small(Byte[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		byte[] output = new byte[tot];
		
		for (int i = 0; i < tot; i++)
		{
			output[i] = (byte)input_[i];
		}
		
		return output;
	}

	static <x> boolean is_ok(ArrayList<ArrayList<x>> input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
		
	static <x, y> boolean is_ok(HashMap<x, y> input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
	
	static boolean is_ok(double[] input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
	
	static boolean is_ok(long[] input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
	
	static boolean is_ok(int[] input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
	
	static boolean is_ok(boolean[] input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
	
	static boolean is_ok(byte[] input_, boolean minimal_)
	{
		return (minimal_ ? (input_ != null) : (get_size(input_) > 0));
	}
	
	static boolean is_ok(Object input_, boolean minimal_)
	{
		if (minimal_) return (input_ != null);
		
		boolean is_ok = false;
		
		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) return is_ok;
		
		if (type.equals(double[].class)) is_ok = is_ok((double[])input_);
		else if (type.equals(long[].class)) is_ok = is_ok((long[])input_);
		else if (type.equals(int[].class)) is_ok = is_ok((int[])input_);
		else if (type.equals(boolean[].class)) is_ok = is_ok((boolean[])input_);
		else if (type.equals(byte[].class)) is_ok = is_ok((byte[])input_);
		else is_ok = (get_size(input_) > 0);
		
		return is_ok;
	}

	static final Class<?>[] populate_all_classes()
	{
		ArrayList<Class<?>> output = new ArrayList<Class<?>>();
		
		output.add(HashMap.class);
		output.add(ArrayList.class);
		
		output.addAll(new ArrayList<Class<?>>(Arrays.asList(get_all_classes_array())));
		output.addAll(new ArrayList<Class<?>>(Arrays.asList(get_all_classes_small())));
		
		return output.toArray(new Class<?>[output.size()]);		
	}
	
	static Class<?>[] populate_all_classes_small()
	{
		return new Class<?>[] { double[].class, long[].class, int[].class, boolean[].class, byte[].class };
	}
	
	static Class<?>[] populate_all_classes_array()
	{
		return new Class<?>[]
		{
			Array.class, String[].class, Boolean[].class, Integer[].class, Long[].class, Double[].class, Byte[].class, 
			Class[].class, Method[].class, Exception[].class, LocalTime[].class, LocalDateTime[].class, size[].class, 
			data[].class, db_field[].class, db_where[].class, db_order[].class		
		};
	}
	
	static Class<?>[] populate_all_classes_numeric()
	{
		return new Class<?>[] { Double[].class, double[].class, Long[].class, long[].class, Integer[].class, int[].class };		
	}

	//Method to filter out ArrayList<x> instances being used as ArrayList<ArrayList<x>> args.
	//!!!
	private static <x> boolean is_arraylist_xx(ArrayList<ArrayList<x>> input_)
	{
		return (is_ok(input_) || input_ == null || input_.get(0) == null || generic.are_equal(input_.get(0), ArrayList.class));
	}
	
	private static <x> x[] get_new_array(x[] input_)
	{
		return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length));
	}
	
	private static <x> ArrayList<x> get_new_arraylist(ArrayList<x> input_)
	{
		return (!is_ok(input_) ? new ArrayList<x>() : new ArrayList<x>(input_));
	}
	
	private static <x> HashMap<x, x> get_new_hashmap(HashMap<x, x> input_)
	{
		return (!is_ok(input_) ? new HashMap<x, x>() : new HashMap<x, x>(input_));
	}
	
	private static <x> Class<?> get_class_key_xx(HashMap<x, x> input_)
	{
		return get_class_key_val_xx(input_, true);
	}
	
	private static <x> Class<?> get_class_val_xx(HashMap<x, x> input_)
	{
		return get_class_key_val_xx(input_, false);
	}
	
	private static <x, y> Class<?> get_class_key_xy(HashMap<x, y> input_)
	{
		return get_class_key_val_xy(input_, true);
	}
	
	private static <x, y> Class<?> get_class_val_xy(HashMap<x, y> input_)
	{
		return get_class_key_val_xy(input_, false);
	}

	private static <x> Class<?> get_class_items(ArrayList<ArrayList<x>> input_)
	{
		return (!is_ok(input_) ? null : get_class_items(input_.get(0)));
	}

	@SuppressWarnings("unused")
	private static Class<?> get_class_items(double[] input_)
	{
		return double.class;
	}
	
	@SuppressWarnings("unused")
	private static Class<?> get_class_items(long[] input_)
	{
		return long.class;
	}
	
	@SuppressWarnings("unused")
	private static Class<?> get_class_items(int[] input_)
	{
		return int.class;
	}
	
	@SuppressWarnings("unused")
	private static Class<?> get_class_items(boolean[] input_)
	{
		return boolean.class;
	}
	
	@SuppressWarnings("unused")
	private static Class<?> get_class_items(byte[] input_)
	{
		return byte.class;
	}
	
	private static <x> Class<?> get_class_items(x[] input_)
	{
		Class<?> output = null;
		if (!is_ok(input_)) return output;
		
		for (x item: input_)
		{
			output = get_class_item(item, output);
			if (generic.are_equal(output, Object.class)) break;
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x> Class<?> get_class_items(Object input_)
	{
		Class<?> output = null;
		if (!generic.are_equal(generic.get_class(input_), ArrayList.class)) return output;
		
		for (x item: (ArrayList<x>)input_)
		{
			output = get_class_item(item, output);
			if (generic.are_equal(output, Object.class)) break;
		}

		return output;
	}
	
	private static Class<?> get_class_item(Object item_, Class<?> class_)
	{
		Class<?> output = class_;
		
		Class<?> temp = generic.get_class(item_);
		if (output == null) output = temp;
		else if (!generic.are_equal(temp, output)) output = Object.class;
		
		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x> String to_string_val(Object val_)
	{
		String output = strings.DEFAULT;
		
		Class<?> type = generic.get_class(val_);
		if (type == null) return output;

		if (type.equals(double[].class)) output = Arrays.toString((double[])val_);
		else if (type.equals(long[].class)) output = Arrays.toString((long[])val_);
		else if (type.equals(int[].class)) output = Arrays.toString((int[])val_);
		else if (type.equals(boolean[].class)) output = Arrays.toString((boolean[])val_);
		else if (generic.are_equal(type, db_where[].class)) output = db_where.to_string((db_where[])val_);
		else if (generic.are_equal(type, db_order[].class)) output = db_order.to_string((db_order[])val_);			
		else if (generic.is_array_class(type)) output = Arrays.toString((x[])val_);
		else output = strings.to_string(val_);
		
		return output;
	}

	private static <x> Class<?> get_class_key_val_xx(HashMap<x, x> input_, boolean is_key_)
	{
		Class<?> output = null;
		if (!is_ok(input_)) return output;
		
		for (Entry<x, x> item: input_.entrySet())
		{
			output = get_class_item((is_key_ ? item.getKey() : item.getValue()), output);
			if (generic.are_equal(output, Object.class)) break;
		}
		
		return output;
	}
	
	private static <x, y> Class<?> get_class_key_val_xy(HashMap<x, y> input_, boolean is_key_)
	{
		Class<?> output = null;
		if (!is_ok(input_)) return output;
		
		for (Entry<x, y> item: input_.entrySet())
		{
			output = get_class_item((is_key_ ? item.getKey() : item.getValue()), output);
			if (generic.are_equal(output, Object.class)) break;
		}
		
		return output;
	}
	
	private static <x> int get_size_arraylist(ArrayList<x> input_)
	{
		return input_.size();
	}

	private static <x> int get_size_hashmap_xx(HashMap<x, x> input_)
	{
		return input_.size();
	}

	private static <x, y> int get_size_hashmap_xy(HashMap<x, y> input_)
	{
		return input_.size();
	}

	private static <x, y> Object key_value_get_exists(HashMap<x, y> array_, x key_, y value_, boolean is_key_, boolean get_)
	{
		for (Entry<x, y> item: array_.entrySet())
		{
			x key = item.getKey();
			y value = item.getValue();

			if ((is_key_ && generic.are_equal(key_, key)) || (!is_key_ && generic.are_equal(value_, value))) 
			{
				return (get_ ? (is_key_ ? value : key) : true);
			}
		}

		return (get_ ? null : false);
	}

	@SuppressWarnings("unchecked")
	private static <x> Object key_value_get_exists(Object array_, x target_, boolean is_key_, boolean get_)
	{
		Class<?> type = generic.get_class(array_);
		if (!generic.is_ok(type)) return (get_ ? null : false);

		if (generic.are_equal(type, HashMap.class))
		{
			for (Entry<x, x> item: ((HashMap<x, x>)array_).entrySet())
			{
				x key = item.getKey();
				x val = item.getValue();
				x target2 = (is_key_ ? key : val);

				if (generic.are_equal(target_, target2)) 
				{
					return (get_ ? (is_key_ ? val : key) : true);
				}
			}
		}
		else
		{
			for (x item: (x[])array_)
			{
				if (generic.are_equal(target_, item)) return (get_ ? item : true);
			}		
		}

		return (get_ ? null : false);
	}

	@SuppressWarnings("unchecked")
	private static <x> x[] to_array_internal(ArrayList<x> input_)
	{
		x[] output = null;

		Class<?> type = get_class_items((ArrayList<x>)input_);
		if (!generic.is_ok(type)) return output;

		int size = ((ArrayList<x>)input_).size();
		if (generic.are_equal(type, String.class)) output = (x[])((ArrayList<x>)input_).toArray(new String[size]);
		else if (generic.are_equal(type, Integer.class)) output = (x[])((ArrayList<x>)input_).toArray(new Integer[size]);
		else if (generic.are_equal(type, Long.class)) output = (x[])((ArrayList<x>)input_).toArray(new Long[size]);
		else if (generic.are_equal(type, Double.class)) output = (x[])((ArrayList<x>)input_).toArray(new Double[size]);
		else if (generic.are_equal(type, Boolean.class)) output = (x[])((ArrayList<x>)input_).toArray(new Boolean[size]);
		else if (generic.are_equal(type, Byte.class)) output = (x[])((ArrayList<x>)input_).toArray(new Byte[size]);
		else if (generic.are_equal(type, Class.class)) output = (x[])((ArrayList<x>)input_).toArray(new Class<?>[size]);
		else if (generic.are_equal(type, Method.class)) output = (x[])((ArrayList<x>)input_).toArray(new Method[size]);
		else if (generic.are_equal(type, Exception.class)) output = (x[])((ArrayList<x>)input_).toArray(new Exception[size]);
		else if (generic.are_equal(type, size.class)) output = (x[])((ArrayList<x>)input_).toArray(new size[size]);
		else if (generic.are_equal(type, data.class)) output = (x[])((ArrayList<x>)input_).toArray(new data[size]);
		else if (generic.are_equal(type, db_field.class)) output = (x[])((ArrayList<x>)input_).toArray(new db_field[size]);
		else if (generic.are_equal(type, db_where.class)) output = (x[])((ArrayList<x>)input_).toArray(new db_where[size]);
		else if (generic.are_equal(type, db_order.class)) output = (x[])((ArrayList<x>)input_).toArray(new db_order[size]);
		else if (generic.are_equal(type, Object.class)) output = (x[])((ArrayList<x>)input_).toArray(new Object[size]);

		return output;
	}
	
	@SuppressWarnings("unchecked")
	private static <x, y> String to_string_internal(Object input_, String separator1_, String separator2_, String[] keys_ignore_, boolean is_xx_)
	{
		if (!is_ok(input_)) return strings.DEFAULT;

		String output = "";
		String separator1 = (strings.is_ok(separator1_) ? separator1_ : misc.SEPARATOR_ITEM);
		String separator2 = (strings.is_ok(separator2_) ? separator2_ : misc.SEPARATOR_KEYVAL);

		HashMap<x, x> input_xx = null;
		HashMap<x, y> input_xy = null;
		
		if (is_xx_) input_xx = (HashMap<x, x>)input_;
		else input_xy = (HashMap<x, y>)input_;
		
		boolean first_time = true;
		
		for (Object item: (is_xx_ ? input_xx : input_xy).entrySet())
		{
			Object key = null;
			Object val = null;
			
			if (is_xx_)
			{
				key = ((Entry<x, x>) item).getKey();
				val = ((Entry<x, x>) item).getValue();	
			}
			else
			{
				key = ((Entry<x, y>) item).getKey();
				val = ((Entry<x, y>) item).getValue();	
			}			
			if (!generic.is_ok(key) || value_exists(keys_ignore_, key)) continue; 

			if (first_time) first_time = false;
			else output += separator1;
			
			String key2 = (generic.is_array(key) ? key.toString() : strings.to_string(key));
			String val2 = to_string_val(val);
			
			output += (key2 + separator2 + val2);
		}

		if (!first_time) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;
		
		return output;
	}
	
	private static <x, y> HashMap<x, y> remove_key_value(HashMap<x, y> array_, Object key_val_, boolean normalise_, boolean is_key_)
	{
		HashMap<x, y> output = new HashMap<x, y>();

		Object val01 = key_val_;
		
		for (Entry<x, y> item: array_.entrySet())
		{
			x key2 = item.getKey();
			y val2 = item.getValue();

			boolean found = remove_key_value_loop(val01, (is_key_ ? key2 : val2), normalise_);
			if (found) continue;
			
			output.put(key2, val2);
		}
		
		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x> Object remove_key_value(Object array_, Object key_val_, boolean normalise_, boolean is_key_)
	{
		Object output = null;
		
		Class<?> type = generic.get_class(array_);
		if (!generic.is_array(type)) return output;

		if (type.equals(double[].class)) output = remove_value((double[])array_, (double)key_val_, normalise_);
		else if (type.equals(long[].class)) output = remove_value((long[])array_, (long)key_val_, normalise_);
		else if (type.equals(int[].class)) output = remove_value((int[])array_, (int)key_val_, normalise_);
		else if (type.equals(boolean[].class)) output = remove_value((boolean[])array_, (boolean)key_val_, normalise_);
		else if (type.equals(byte[].class)) output = remove_value((byte[])array_, (byte)key_val_, normalise_);
		
		Object val01 = key_val_;
		
		if (generic.are_equal(type, HashMap.class))
		{
			HashMap<x, x> output2 = new HashMap<x, x>();
			
			for (Entry<x, x> item: ((HashMap<x, x>)array_).entrySet())
			{
				x key2 = item.getKey();
				x val2 = item.getValue();

				boolean found = remove_key_value_loop(val01, (is_key_ ? key2 : val2), normalise_);
				if (found) continue;
				
				output2.put(key2, val2);
			}
			
			output = output2;
		}
		else if (generic.are_equal(type, ArrayList.class))
		{
			ArrayList<x> output2 = new ArrayList<x>();
			
			for (x item: (ArrayList<x>)array_)
			{
				boolean found = remove_key_value_loop(val01, item, normalise_);
				if (found) continue;

				output2.add(item);
			}
			
			output = output2;
		}
		else if (generic.are_equal(type, Array.class))
		{
			ArrayList<x> output2 = new ArrayList<x>();
			
			for (x item: (x[])array_)
			{
				boolean found = remove_key_value_loop(val01, item, normalise_);
				if (found) continue;
				
				output2.add(item);
			}
			
			output = to_array(output2);
		}
		
		return output;
	}
	
	private static boolean remove_key_value_loop(Object val01_, Object val02_, boolean normalise_)
	{
		boolean output = true;

		Class<?> type1 = generic.get_class(val01_);
		Class<?> type2 = generic.get_class(val02_);
		
		if (!generic.are_equal(val01_, val02_))
		{
			if (!normalise_ || !generic.are_equal(type1, String.class) || !generic.are_equal(type2, String.class)) output = false;
			else
			{
				String val21 = (String)val01_;
				String val22 = (String)val02_;
				
				if (!strings.are_equivalent(val21, val22)) output = false;	
			}
		}
		
		return output;
	}
}