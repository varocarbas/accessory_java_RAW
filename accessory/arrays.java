package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class arrays extends parent_static 
{
	public static final int WRONG_I = -1;

	public static final int DEFAULT_SIZE = 5;
	
	public static final double DEFAULT_DECIMAL = 0.0;
	public static final long DEFAULT_LONG = 0l;
	public static final int DEFAULT_INT = 0;
	public static final boolean DEFAULT_BOOLEAN = false;
	public static final byte DEFAULT_BYTE = (byte)0;
	public static final char DEFAULT_CHAR = ' ';
	
	public static Class<?>[] get_all_classes() { return _alls.ARRAYS_CLASSES; }

	//All these classes require a special treatment. In some cases, they are assumed to be equivalent to their big counterparts.
	//Their instances can only be used as Object[]/x[] after having been converted into their corresponding big counterparts.
	//Methods with Object[]/x[] parameters have to include specific overloads for all these classes. Additionally, they have 
	//to internally (i.e., in the overload including the Object parameter) account for these scenarios too because some calls 
	//might not be reaching the specific overloads (e.g., Object val = new int[] { 1, 2, 3 } going to method(Object input_)).
	//!!!
	public static Class<?>[] get_all_classes_small() { return _alls.ARRAYS_CLASSES_SMALL; }

	//All these classes are equivalent to Array.class and their instances can be used as Object[]/x[].
	public static Class<?>[] get_all_classes_array() { return _alls.ARRAYS_CLASSES_ARRAY; }

	public static Class<?>[] get_all_classes_numeric() { return _alls.ARRAYS_CLASSES_NUMERIC; }

	public static boolean is_ok(double[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(long[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(int[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(boolean[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(byte[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(char[] input_) { return is_ok(input_, false); }

	public static boolean is_ok(Object input_) { return is_ok(input_, false); }

	public static int get_size(double[] input_) { return (input_ == null ? 0 : input_.length); }

	public static int get_size(long[] input_) { return (input_ == null ? 0 : input_.length); }

	public static int get_size(int[] input_) { return (input_ == null ? 0 : input_.length); }

	public static int get_size(boolean[] input_) { return (input_ == null ? 0 : input_.length); }

	public static int get_size(byte[] input_) { return (input_ == null ? 0 : input_.length); }

	public static int get_size(char[] input_) { return (input_ == null ? 0 : input_.length); }

	@SuppressWarnings("unchecked")
	public static <x, y> int get_size(Object input_)
	{
		int size = 0;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) return size;

		if (type.equals(double[].class)) size = get_size((double[])input_);
		else if (type.equals(long[].class)) size = get_size((long[])input_);
		else if (type.equals(int[].class)) size = get_size((int[])input_);
		else if (type.equals(boolean[].class)) size = get_size((boolean[])input_);
		else if (type.equals(byte[].class)) size = get_size((byte[])input_);
		else if (type.equals(char[].class)) size = get_size((char[])input_);
		else if (generic.are_equal(type, ArrayList.class)) size = ((ArrayList<x>)input_).size();
		else if (generic.are_equal(type, HashMap.class)) size = (hashmap_is_xy(input_) ? (HashMap<x, y>)input_ : (HashMap<x, x>)input_).size();
		else size = ((Object[])input_).length;

		return size;
	}

	public static boolean are_equal(Double[] input1_, double[] input2_) { return are_equal(input1_, to_big(input2_)); }

	public static boolean are_equal(Long[] input1_, long[] input2_) { return are_equal(input1_, to_big(input2_)); }

	public static boolean are_equal(Integer[] input1_, int[] input2_) { return are_equal(input1_, to_big(input2_)); }

	public static boolean are_equal(Boolean[] input1_, boolean[] input2_) { return are_equal(input1_, to_big(input2_)); }

	public static boolean are_equal(Byte[] input1_, byte[] input2_) { return are_equal(input1_, to_big(input2_)); }

	public static boolean are_equal(Character[] input1_, char[] input2_) { return are_equal(input1_, to_big(input2_)); }

	public static boolean are_equal(double[] input1_, Double[] input2_) { return are_equal(to_big(input1_), input2_); }

	public static boolean are_equal(long[] input1_, Long[] input2_) { return are_equal(to_big(input1_), input2_); }

	public static boolean are_equal(int[] input1_, Integer[] input2_) { return are_equal(to_big(input1_), input2_); }

	public static boolean are_equal(boolean[] input1_, Boolean[] input2_) { return are_equal(to_big(input1_), input2_); }

	public static boolean are_equal(byte[] input1_, Byte[] input2_) { return are_equal(to_big(input1_), input2_); }

	public static boolean are_equal(char[] input1_, Character[] input2_) { return are_equal(to_big(input1_), input2_); }

	public static boolean are_equal(double[] input1_, double[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }

	public static boolean are_equal(long[] input1_, long[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }

	public static boolean are_equal(int[] input1_, int[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }

	public static boolean are_equal(boolean[] input1_, boolean[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }

	public static boolean are_equal(byte[] input1_, byte[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }

	public static boolean are_equal(char[] input1_, char[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }

	@SuppressWarnings("unchecked")
	public static <x> boolean are_equal(Object input1_, Object input2_)
	{
		boolean is_ok1 = is_ok(input1_, true);
		boolean is_ok2 = is_ok(input2_, true);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		int size = get_size(input1_);
		Class<?> type = generic.get_class(input1_);
		Class<?> type2 = generic.get_class(input2_);
		if (!generic.is_array(type) || !generic.are_equal(type, type2) || size != get_size(input2_)) return false;

		if (type.equals(double[].class) || type2.equals(double[].class)) 
		{
			return are_equal
			(
				(type.equals(double[].class) ? to_big((double[])input1_) : (Double[])input1_),
				(type2.equals(double[].class) ? to_big((double[])input2_) : (Double[])input2_)
			);
		}
		else if (type.equals(long[].class) || type2.equals(long[].class)) 
		{
			return are_equal
			(
				(type.equals(long[].class) ? to_big((long[])input1_) : (Long[])input1_),
				(type2.equals(long[].class) ? to_big((long[])input2_) : (Long[])input2_)
			);	
		}
		else if (type.equals(int[].class) || type2.equals(int[].class)) 
		{
			return are_equal
			(
				(type.equals(int[].class) ? to_big((int[])input1_) : (Integer[])input1_),
				(type2.equals(int[].class) ? to_big((int[])input2_) : (Integer[])input2_)
			);		
		}
		else if (type.equals(boolean[].class) || type2.equals(boolean[].class)) 
		{
			return are_equal
			(
				(type.equals(boolean[].class) ? to_big((boolean[])input1_) : (Boolean[])input1_),
				(type2.equals(boolean[].class) ? to_big((boolean[])input2_) : (Boolean[])input2_)
			);
		}
		else if (type.equals(byte[].class) || type2.equals(byte[].class)) 
		{
			return are_equal
			(
				(type.equals(byte[].class) ? to_big((byte[])input1_) : (Byte[])input1_),
				(type2.equals(byte[].class) ? to_big((byte[])input2_) : (Byte[])input2_)
			);			
		}
		else if (type.equals(char[].class) || type2.equals(char[].class)) 
		{
			return are_equal
			(
				(type.equals(char[].class) ? to_big((char[])input1_) : (Character[])input1_),
				(type2.equals(char[].class) ? to_big((char[])input2_) : (Character[])input2_)
			);			
		}
		else if (generic.are_equal(type, HashMap.class)) 
		{
			Class<?>[] types21 = get_classes_items(input1_);
			Class<?>[] types22 = get_classes_items(input2_);

			return (are_equal(types21, types22) ? are_equal_hashmap(input1_, input2_, !generic.are_equal(types21[0], types21[1])) : false);
		}
		else
		{
			if (generic.are_equal(type, ArrayList.class))
			{
				if (!generic.are_equal(get_class_items(input1_), get_class_items(input2_))) return false;

				ArrayList<x> input1 = (ArrayList<x>)input1_;
				ArrayList<x> input2 = (ArrayList<x>)input2_;
				if (input1.equals(input2)) return true;

				for (int i = 0; i < size; i++)
				{
					if (!generic.is_ok(input1.get(i), true)) 
					{
						if (generic.is_ok(input2.get(i), true)) return false;
					}
					else if (!generic.are_equal(input1.get(i), input2.get(i))) return false;
				}

				return true;
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
			generic.are_equal(class_, Boolean[].class) || generic.are_equal(class_, Byte[].class) ||
			generic.are_equal(class_, Character[].class) || generic.are_equal(class_, Class[].class) ||
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

		if (class_.equals(double[].class))
		{
			double[] temp = new double[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (double)numbers.get_random(Double.class); }

			output = temp;
		}
		else if (class_.equals(Double[].class))
		{
			Double[] temp = new Double[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (Double)numbers.get_random(Double.class); }

			output = temp;
		}
		else if (class_.equals(long[].class))
		{
			long[] temp = new long[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (long)numbers.get_random(Long.class); }

			output = temp;
		}
		else if (class_.equals(Long[].class))
		{
			Long[] temp = new Long[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (Long)numbers.get_random(Long.class); }

			output = temp;
		}
		else if (class_.equals(int[].class))
		{
			int[] temp = new int[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (int)numbers.get_random(Integer.class); }

			output = temp;
		}
		else if (class_.equals(Integer[].class))
		{
			Integer[] temp = new Integer[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (Integer)numbers.get_random(Integer.class); }

			output = temp;
		}
		else if (class_.equals(boolean[].class))
		{
			boolean[] temp = new boolean[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = generic.get_random_boolean(); }

			output = temp;
		}
		else if (class_.equals(Boolean[].class))
		{
			Boolean[] temp = new Boolean[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = (Boolean)generic.get_random_boolean(); }

			output = temp;
		}
		else if (class_.equals(byte[].class)) output = generic.get_random_bytes(DEFAULT_SIZE);
		else if (class_.equals(Byte[].class)) output = (Byte[])to_big(generic.get_random_bytes(DEFAULT_SIZE));
		else if (class_.equals(char[].class)) output = generic.get_random_chars(DEFAULT_SIZE, true, true, true);
		else if (class_.equals(Character[].class)) output = (Character[])to_big(generic.get_random_chars(DEFAULT_SIZE, true, true, true));
		else if (generic.are_equal(class_, Class[].class))
		{
			Class<?>[] temp = new Class<?>[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = generic.get_random_class(); }

			output = temp;
		}
		else if (generic.are_equal(class_, String[].class) || generic.are_equal(class_, Array.class))
		{
			String[] temp = new String[DEFAULT_SIZE];

			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = strings.get_random(strings.SIZE_SMALL); }

			output = temp;
		}

		return output;
	}

	public static ArrayList<String> get_random_arraylist()
	{
		ArrayList<String> output = new ArrayList<String>();

		for (int i = 0; i < DEFAULT_SIZE; i++) { output.add(strings.get_random(strings.SIZE_SMALL)); }

		return output;		
	}

	public static HashMap<String, String> get_random_hashmap()
	{
		HashMap<String, String> output = new HashMap<String, String>();

		for (int i = 0; i < DEFAULT_SIZE; i++) { output.put(Integer.toString(i), strings.get_random(strings.SIZE_SMALL)); }

		return output;	
	}

	public static <x> boolean __key_exists_sync(double[] input_, x key_) { return __key_exists_sync(to_big(input_), key_); }

	public static <x> boolean __key_exists_sync(long[] input_, x key_) { return __key_exists_sync(to_big(input_), key_); }

	public static <x> boolean __key_exists_sync(int[] input_, x key_) { return __key_exists_sync(to_big(input_), key_); }

	public static <x> boolean __key_exists_sync(boolean[] input_, x key_) { return __key_exists_sync(to_big(input_), key_); }

	public static <x> boolean __key_exists_sync(byte[] input_, x key_) { return __key_exists_sync(to_big(input_), key_); }

	public static <x> boolean __key_exists_sync(char[] input_, x key_) { return __key_exists_sync(to_big(input_), key_); }

	public static boolean __key_exists_sync(Object input_, Object key_) { return (boolean)__key_value_get_exists_sync(input_, key_, true, false); }

	public static <x> boolean __value_exists_sync(double[] input_, x value_) { return __value_exists_sync(to_big(input_), value_); }

	public static <x> boolean __value_exists_sync(long[] input_, x value_) { return __value_exists_sync(to_big(input_), value_); }

	public static <x> boolean __value_exists_sync(int[] input_, x value_) { return __value_exists_sync(to_big(input_), value_); }

	public static <x> boolean __value_exists_sync(boolean[] input_, x value_) { return __value_exists_sync(to_big(input_), value_); }

	public static <x> boolean __value_exists_sync(byte[] input_, x value_) { return __value_exists_sync(to_big(input_), value_); }

	public static <x> boolean __value_exists_sync(char[] input_, x value_) { return __value_exists_sync(to_big(input_), value_); }

	public static boolean __value_exists_sync(Object input_, Object value_) { return (boolean)__key_value_get_exists_sync(input_, value_, false, false); }

	public static <x> Object __get_key_sync(double[] input_, x value_) { return __get_key_sync(to_big(input_), value_); }

	public static <x> Object __get_key_sync(long[] input_, x value_) { return __get_key_sync(to_big(input_), value_); }

	public static <x> Object __get_key_sync(int[] input_, x value_) { return __get_key_sync(to_big(input_), value_); }

	public static <x> Object __get_key_sync(boolean[] input_, x value_) { return __get_key_sync(to_big(input_), value_); }

	public static <x> Object __get_key_sync(byte[] input_, x value_) { return __get_key_sync(to_big(input_), value_); }

	public static <x> Object __get_key_sync(char[] input_, x value_) { return __get_key_sync(to_big(input_), value_); }

	public static Object __get_key_sync(Object input_, Object value_) { return __key_value_get_exists_sync(input_, value_, false, true); }

	public static <x> Object __get_value_sync(double[] input_, x key_) { return __get_value_sync(to_big(input_), key_); }

	public static <x> Object __get_value_sync(long[] input_, x key_) { return __get_value_sync(to_big(input_), key_); }

	public static <x> Object __get_value_sync(int[] input_, x key_) { return __get_value_sync(to_big(input_), key_); }

	public static <x> Object __get_value_sync(boolean[] input_, x key_) { return __get_value_sync(to_big(input_), key_); }

	public static <x> Object __get_value_sync(byte[] input_, x key_) { return __get_value_sync(to_big(input_), key_); }

	public static <x> Object __get_value_sync(char[] input_, x key_) { return __get_value_sync(to_big(input_), key_); }

	public static Object __get_value_sync(Object input_, Object key_) { return __key_value_get_exists_sync(input_, key_, true, true); }

	public static <x> double[] __remove_key_sync(double[] input_, x key_) { return to_small((Double[])__remove_key_sync(to_big(input_), key_)); }

	public static <x> long[] __remove_key_sync(long[] input_, x key_) { return to_small((Long[])__remove_key_sync(to_big(input_), key_)); }

	public static <x> int[] __remove_key_sync(int[] input_, x key_) { return to_small((Integer[])__remove_key_sync(to_big(input_), key_)); }

	public static <x> boolean[] __remove_key_sync(boolean[] input_, x key_) { return to_small((Boolean[])__remove_key_sync(to_big(input_), key_)); }

	public static <x> byte[] __remove_key_sync(byte[] input_, x key_) { return to_small((Byte[])__remove_key_sync(to_big(input_), key_)); }

	public static <x> char[] __remove_key_sync(char[] input_, x key_) { return to_small((Character[])__remove_key_sync(to_big(input_), key_)); }

	public static Object __remove_key_sync(Object input_, Object key_) { return __remove_key_sync(input_, key_, false); }

	public static Object __remove_key_sync(Object input_, Object key_, boolean normalise_) { return __remove_key_value_sync(input_, key_, normalise_, true); }

	public static <x> double[] __remove_value_sync(double[] input_, x value_, boolean normalise_) { return to_small((Double[])__remove_value_sync(to_big(input_), value_)); }

	public static <x> long[] __remove_value_sync(long[] input_, x value_, boolean normalise_) { return to_small((Long[])__remove_value_sync(to_big(input_), value_)); }

	public static <x> int[] __remove_value_sync(int[] input_, x value_, boolean normalise_) { return to_small((Integer[])__remove_value_sync(to_big(input_), value_)); }

	public static <x> boolean[] __remove_value_sync(boolean[] input_, x value_, boolean normalise_) { return to_small((Boolean[])__remove_value_sync(to_big(input_), value_)); }

	public static <x> byte[] __remove_value_sync(byte[] input_, x value_) { return to_small((Byte[])__remove_value_sync(to_big(input_), value_)); }

	public static <x> char[] __remove_value_sync(char[] input_, x value_) { return to_small((Character[])__remove_value_sync(to_big(input_), value_)); }

	public static Object __remove_value_sync(Object input_, Object value_) { return __remove_value_sync(input_, value_, false); }

	public static Object __remove_value_sync(Object input_, Object value_, boolean normalise_) { return __remove_key_value_sync(input_, value_, normalise_, false); }

	public static ArrayList<Double> add(ArrayList<Double> main_, double[] new_) { return (ArrayList<Double>)add(main_, to_big(new_)); }

	public static ArrayList<Long> add(ArrayList<Long> main_, long[] new_) { return (ArrayList<Long>)add(main_, to_big(new_)); }

	public static ArrayList<Integer> add(ArrayList<Integer> main_, int[] new_) { return (ArrayList<Integer>)add(main_, to_big(new_)); }

	public static ArrayList<Boolean> add(ArrayList<Boolean> main_, boolean[] new_) { return (ArrayList<Boolean>)add(main_, to_big(new_)); }

	public static ArrayList<Byte> add(ArrayList<Byte> main_, byte[] new_) { return (ArrayList<Byte>)add(main_, to_big(new_)); }

	public static ArrayList<Character> add(ArrayList<Character> main_, char[] new_) { return (ArrayList<Character>)add(main_, to_big(new_)); }

	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> add(ArrayList<x> main_, x[] new_) { return (ArrayList<x>)add(main_, to_arraylist(new_)); }

	public static ArrayList<Double> add(double[] main_, ArrayList<Double> new_) { return (ArrayList<Double>)add(to_big(main_), new_); }

	public static ArrayList<Long> add(long[] main_, ArrayList<Long> new_) { return (ArrayList<Long>)add(to_big(main_), new_); }

	public static ArrayList<Integer> add(int[] main_, ArrayList<Integer> new_) { return (ArrayList<Integer>)add(to_big(main_), new_); }

	public static ArrayList<Boolean> add(boolean[] main_, ArrayList<Boolean> new_) { return (ArrayList<Boolean>)add(to_big(main_), new_); }

	public static ArrayList<Byte> add(byte[] main_, ArrayList<Byte> new_) { return (ArrayList<Byte>)add(to_big(main_), new_); }

	public static ArrayList<Character> add(char[] main_, ArrayList<Character> new_) { return (ArrayList<Character>)add(to_big(main_), new_); }

	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> add(x[] main_, ArrayList<x> new_) { return (ArrayList<x>)add(to_arraylist(main_), new_); }

	public static double[] add(double[] main_, double[] new_) { return to_small((Double[])add(to_big(main_), to_big(new_))); }

	public static long[] add(long[] main_, long[] new_) { return to_small((Long[])add(to_big(main_), to_big(new_))); }

	public static int[] add(int[] main_, int[] new_) { return to_small((Integer[])add(to_big(main_), to_big(new_))); }

	public static boolean[] add(boolean[] main_, boolean[] new_) { return to_small((Boolean[])add(to_big(main_), to_big(new_))); }

	public static byte[] add(byte[] main_, byte[] new_) { return to_small((Byte[])add(to_big(main_), to_big(new_))); }

	public static char[] add(char[] main_, char[] new_) { return to_small((Character[])add(to_big(main_), to_big(new_))); }

	public static Double[] add(double[] main_, Double[] new_) { return (Double[])add(to_big(main_), new_); }

	public static Long[] add(long[] main_, Long[] new_) { return (Long[])add(to_big(main_), new_); }

	public static Integer[] add(int[] main_, Integer[] new_) { return (Integer[])add(to_big(main_), new_); }

	public static Boolean[] add(boolean[] main_, Boolean[] new_) { return (Boolean[])add(to_big(main_), new_); }

	public static Byte[] add(byte[] main_, Byte[] new_) { return (Byte[])add(to_big(main_), new_); }

	public static Character[] add(char[] main_, Character[] new_) { return (Character[])add(to_big(main_), new_); }

	public static Double[] add(Double[] main_, double[] new_) { return (Double[])add(main_, to_big(new_)); }

	public static Long[] add(Long[] main_, long[] new_) { return (Long[])add(main_, to_big(new_)); }

	public static Integer[] add(Integer[] main_, int[] new_) { return (Integer[])add(main_, to_big(new_)); }

	public static Boolean[] add(Boolean[] main_, boolean[] new_) { return (Boolean[])add(main_, to_big(new_)); }

	public static Byte[] add(Byte[] main_, byte[] new_) { return (Byte[])add(main_, to_big(new_)); }

	public static Character[] add(Character[] main_, char[] new_) { return (Character[])add(main_, to_big(new_)); }

	public static Object add(Object main_, Object new_) { return add(main_, new_, false); }

	@SuppressWarnings("unchecked")
	public static <x, y> Object add(Object main_, Object new_, boolean only_unique_)
	{
		Class<?> type = generic.get_class(main_);
		if (!generic.is_array(type)) return get_new(new_);

		Object output = get_new(main_);
		Class<?> type2 = generic.get_class(new_);
		if (!generic.is_array(type2) || !generic.are_equal(type, type2)) return output;

		if (type.equals(double[].class) && type2.equals(double[].class)) return add(to_big((double[])main_), to_big((double[])new_), only_unique_);
		else if (type.equals(double[].class)) return add(to_big((double[])main_), new_, only_unique_);
		else if (type2.equals(double[].class)) return add(main_, to_big((double[])new_), only_unique_);
		else if (type.equals(long[].class) && type2.equals(long[].class)) return add(to_big((long[])main_), to_big((long[])new_), only_unique_);
		else if (type.equals(long[].class)) return add(to_big((long[])main_), new_, only_unique_);
		else if (type2.equals(long[].class)) return add(main_, to_big((long[])new_), only_unique_);
		else if (type.equals(int[].class) && type2.equals(int[].class)) return add(to_big((int[])main_), to_big((int[])new_), only_unique_);
		else if (type.equals(int[].class)) return add(to_big((int[])main_), new_, only_unique_);
		else if (type2.equals(int[].class)) return add(main_, to_big((int[])new_), only_unique_);
		else if (type.equals(boolean[].class) && type2.equals(boolean[].class)) return add(to_big((boolean[])main_), to_big((boolean[])new_), only_unique_);
		else if (type.equals(boolean[].class)) return add(to_big((boolean[])main_), new_, only_unique_);
		else if (type2.equals(boolean[].class)) return add(main_, to_big((boolean[])new_), only_unique_);
		else if (type.equals(byte[].class) && type2.equals(byte[].class)) return add(to_big((byte[])main_), to_big((byte[])new_), only_unique_);
		else if (type.equals(byte[].class)) return add(to_big((byte[])main_), new_, only_unique_);
		else if (type2.equals(byte[].class)) return add(main_, to_big((byte[])new_), only_unique_);
		else if (type.equals(char[].class) && type2.equals(char[].class)) return add(to_big((char[])main_), to_big((char[])new_), only_unique_);
		else if (type.equals(char[].class)) return add(to_big((char[])main_), new_, only_unique_);
		else if (type2.equals(char[].class)) return add(main_, to_big((char[])new_), only_unique_);

		if (generic.are_equal(type, HashMap.class)) 
		{
			Class<?>[] types2 = get_classes_items(main_);
			if (!are_equal(types2, get_classes_items(new_))) return output;

			if (generic.are_equal(types2[0], types2[1]))
			{
				HashMap<x, x> output2 = (HashMap<x, x>)get_new(main_);
				HashMap<x, x> new2 = (HashMap<x, x>)get_new(new_);

				for (Entry<x, x> item: new2.entrySet())
				{
					x key = item.getKey();
					if (only_unique_ && output2.containsKey(key)) continue;

					output2.put(key, item.getValue());
				}

				return output2;
			}
			else
			{
				HashMap<x, y> output2 = (HashMap<x, y>)get_new(main_);
				HashMap<x, y> new2 = (HashMap<x, y>)get_new(new_);

				for (Entry<x, y> item: new2.entrySet())
				{
					x key = item.getKey();
					if (only_unique_ && output2.containsKey(key)) continue;

					output2.put(key, item.getValue());
				}

				return output2;
			}
		}
		else
		{
			if (!generic.are_equal(get_class_items(main_), get_class_items(new_))) return output;

			ArrayList<x> output2 = null;
			ArrayList<x> new2 = null;
			boolean is_arraylist = false;

			if (generic.are_equal(type, ArrayList.class)) 
			{
				output2 = (ArrayList<x>)get_new(main_);
				new2 = (ArrayList<x>)get_new(new_);
				is_arraylist = true;
			}
			else
			{
				output2 = to_arraylist((x[])main_);
				new2 = to_arraylist((x[])new_);
			}

			for (x val: new2)
			{
				if (only_unique_ && output2.contains(val)) continue;
				output2.add(val);
			}

			return (is_arraylist ? output2 : to_array(output2));
		}
	}

	@SuppressWarnings("unchecked")
	public static <x> x[] to_array(ArrayList<x> input_)
	{
		x[] output = null;

		int size = arrays.get_size(input_);
		if (size == 0) return output;
		
		Class<?> type = get_class_items(input_);
		if (type == null) return output;

		if (generic.are_equal(type, String.class)) output = (x[])input_.toArray(new String[size]);
		else if (generic.are_equal(type, Integer.class)) output = (x[])input_.toArray(new Integer[size]);
		else if (generic.are_equal(type, Long.class)) output = (x[])input_.toArray(new Long[size]);
		else if (generic.are_equal(type, Double.class)) output = (x[])input_.toArray(new Double[size]);
		else if (generic.are_equal(type, Boolean.class)) output = (x[])input_.toArray(new Boolean[size]);
		else if (generic.are_equal(type, Byte.class)) output = (x[])input_.toArray(new Byte[size]);
		else if (generic.are_equal(type, Character.class)) output = (x[])input_.toArray(new Character[size]);
		else if (generic.are_equal(type, Class.class)) output = (x[])input_.toArray(new Class<?>[size]);
		else if (generic.are_equal(type, Method.class)) output = (x[])input_.toArray(new Method[size]);
		else if (generic.are_equal(type, Exception.class)) output = (x[])input_.toArray(new Exception[size]);
		else if (generic.are_equal(type, LocalDateTime.class)) output = (x[])input_.toArray(new LocalDateTime[size]);
		else if (generic.are_equal(type, LocalDate.class)) output = (x[])input_.toArray(new LocalDate[size]);
		else if (generic.are_equal(type, LocalTime.class)) output = (x[])input_.toArray(new LocalTime[size]);
		else if (generic.are_equal(type, size.class)) output = (x[])input_.toArray(new size[size]);
		else if (generic.are_equal(type, data.class)) output = (x[])input_.toArray(new data[size]);
		else if (generic.are_equal(type, db_field.class)) output = (x[])input_.toArray(new db_field[size]);
		else if (generic.are_equal(type, db_where.class)) output = (x[])input_.toArray(new db_where[size]);
		else if (generic.are_equal(type, db_order.class)) output = (x[])input_.toArray(new db_order[size]);
		else if (generic.are_equal(type, Object.class)) output = (x[])input_.toArray(new Object[size]);
		else if (generic.are_equal(type, HashMap.class)) output = (x[])input_.toArray(new HashMap[size]);
		else if (generic.are_equal(type, ArrayList.class)) output = (x[])input_.toArray(new ArrayList[size]);

		return output;
	}

	public static <x> ArrayList<x> get_new(ArrayList<x> input_) { return get_new_arraylist(input_); }

	public static double[] get_new(double[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }

	public static long[] get_new(long[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }

	public static int[] get_new(int[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }

	public static boolean[] get_new(boolean[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }

	public static byte[] get_new(byte[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }

	public static char[] get_new(char[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }

	//Specific _xx and _xy overloads are needed because the default Object overload fails to recognise a null hashmap as such.
	//It would be possible to create an additional get_new() overload, but not the two required to account for both scenarios (xx and xy).
	@SuppressWarnings("unchecked")
	public static <x> HashMap<x, x> get_new_hashmap_xx(HashMap<x, x> input_) { return (HashMap<x, x>)get_new_hashmap(input_, false); }

	@SuppressWarnings("unchecked")
	public static <x, y> HashMap<x, y> get_new_hashmap_xy(HashMap<x, y> input_) { return (HashMap<x, y>)get_new_hashmap(input_, true); }

	@SuppressWarnings("unchecked")
	public static <x> Object get_new(Object input_)
	{
		Object output = null;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) return output;
		
		if (type.equals(double[].class)) output = get_new(to_big((double[])input_));
		else if (type.equals(long[].class)) output = get_new(to_big((long[])input_));
		else if (type.equals(int[].class)) output = get_new(to_big((int[])input_));
		else if (type.equals(boolean[].class)) output = get_new(to_big((boolean[])input_));
		else if (type.equals(byte[].class)) output = get_new(to_big((byte[])input_));
		else if (type.equals(char[].class)) output = get_new(to_big((char[])input_));
		else if (generic.are_equal(type, ArrayList.class)) output = get_new_arraylist((ArrayList<x>)input_);
		else if (generic.are_equal(type, HashMap.class)) output = get_new_hashmap(input_); 
		else output = get_new_array((x[])input_);

		return output;
	}

	public static ArrayList<Double> to_arraylist(double[] input_) { return (is_ok(input_) ? new ArrayList<Double>(Arrays.asList((Double[])to_big(input_))) : new ArrayList<Double>()); }

	public static ArrayList<Long> to_arraylist(long[] input_) { return (is_ok(input_) ? new ArrayList<Long>(Arrays.asList((Long[])to_big(input_))) : new ArrayList<Long>()); }

	public static ArrayList<Integer> to_arraylist(int[] input_) { return (is_ok(input_) ? new ArrayList<Integer>(Arrays.asList((Integer[])to_big(input_))) : new ArrayList<Integer>()); }

	public static ArrayList<Boolean> to_arraylist(boolean[] input_) { return (is_ok(input_) ? new ArrayList<Boolean>(Arrays.asList((Boolean[])to_big(input_))) : new ArrayList<Boolean>()); }

	public static ArrayList<Byte> to_arraylist(byte[] input_) { return (is_ok(input_) ? new ArrayList<Byte>(Arrays.asList((Byte[])to_big(input_))) : new ArrayList<Byte>()); }

	public static ArrayList<Character> to_arraylist(char[] input_) { return (is_ok(input_) ? new ArrayList<Character>(Arrays.asList((Character[])to_big(input_))) : new ArrayList<Character>()); }

	public static <x> ArrayList<x> to_arraylist(x[] input_) { return (is_ok(input_) ? new ArrayList<x>(Arrays.asList(input_)) : new ArrayList<x>()); }	

	public static <x> int index_of(double[] array_, double target_, int start_i_, int last_i_) { return index_of_internal(to_big(array_), target_, start_i_, last_i_); }

	public static <x> int index_of(long[] array_, long target_, int start_i_, int last_i_) { return index_of_internal(to_big(array_), target_, start_i_, last_i_); }

	public static <x> int index_of(int[] array_, int target_, int start_i_, int last_i_) { return index_of_internal(to_big(array_), target_, start_i_, last_i_); }

	public static <x> int index_of(boolean[] array_, boolean target_, int start_i_, int last_i_) { return index_of_internal(to_big(array_), target_, start_i_, last_i_); }

	public static <x> int index_of(byte[] array_, byte target_, int start_i_, int last_i_) { return index_of_internal(to_big(array_), target_, start_i_, last_i_); }

	public static <x> int index_of(char[] array_, char target_, int start_i_, int last_i_) { return index_of_internal(to_big(array_), target_, start_i_, last_i_); }

	public static <x> int index_of(x[] array_, x target_, int start_i_, int last_i_) { return index_of_internal(array_, target_, start_i_, last_i_); }
	
	public static <x> int index_of(ArrayList<x> array_, x target_, int start_i_, int last_i_) { return index_of_internal(array_, target_, start_i_, last_i_); }
	
	public static double[] get_range(double[] input_, int start_i, int size_) { return (double[])to_small((Double[])get_range((Double[])to_big(input_), start_i, size_)); }

	public static long[] get_range(long[] input_, int start_i, int size_) { return (long[])to_small((Long[])get_range((Long[])to_big(input_), start_i, size_)); }

	public static int[] get_range(int[] input_, int start_i, int size_) { return (int[])to_small((Integer[])get_range((Integer[])to_big(input_), start_i, size_)); }

	public static boolean[] get_range(boolean[] input_, int start_i, int size_) { return (boolean[])to_small((Boolean[])get_range((Boolean[])to_big(input_), start_i, size_)); }

	public static byte[] get_range(byte[] input_, int start_i, int size_) { return (byte[])to_small((Byte[])get_range((Byte[])to_big(input_), start_i, size_)); }

	public static char[] get_range(char[] input_, int start_i, int size_) { return (char[])to_small((Character[])get_range((Character[])to_big(input_), start_i, size_)); }

	@SuppressWarnings("unchecked")
	public static <x> Object get_range(Object input_, int start_i_, int size_)
	{
		Object output = null;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type) || generic.are_equal(type, HashMap.class) || start_i_ < 0) return output;

		int size0 = get_size(input_);
		int size = (size_ < 1 ? size0 : start_i_ + size_);
		if (size > size0 || start_i_ >= size0) return output;

		if (generic.are_equal(type, ArrayList.class)) output = new ArrayList<x>(((ArrayList<x>)input_).subList(start_i_, size));
		else 
		{
			if (type.equals(double[].class)) output = get_range(to_big((double[])input_), start_i_, size);
			else if (type.equals(long[].class)) output = get_range(to_big((long[])input_), start_i_, size);
			else if (type.equals(int[].class)) output = get_range(to_big((int[])input_), start_i_, size);
			else if (type.equals(boolean[].class)) output = get_range(to_big((boolean[])input_), start_i_, size);
			else if (type.equals(byte[].class)) output = get_range(to_big((byte[])input_), start_i_, size);
			else if (type.equals(char[].class)) output = get_range(to_big((char[])input_), start_i_, size);
			else output = Arrays.copyOfRange((x[])input_, start_i_, size);
		}

		return output;
	}

	public static <x> double[] remove_key(double[] array_, x key_) { return remove_key_value(array_, key_, false, true); }

	public static <x> long[] remove_key(long[] array_, x key_) { return remove_key_value(array_, key_, false, true); }

	public static <x> int[] remove_key(int[] array_, x key_) { return remove_key_value(array_, key_, false, true); }

	public static <x> boolean[] remove_key(boolean[] array_, x key_) { return remove_key_value(array_, key_, false, true); }

	public static <x> byte[] remove_key(byte[] array_, x key_) { return remove_key_value(array_, key_, false, true); }

	public static <x> char[] remove_key(char[] array_, x key_) { return remove_key_value(array_, key_, false, true); }

	public static <x> Object remove_key(Object array_, x key_) { return remove_key(array_, key_, false); }

	public static <x> Object remove_key(Object array_, x key_, boolean normalise_) { return remove_key_value(array_, key_, normalise_, true); }

	public static <x> double[] remove_value(double[] array_, x value_) { return remove_key_value(array_, value_, false, false); }

	public static <x> long[] remove_value(long[] array_, x value_) { return remove_key_value(array_, value_, false, false); }

	public static <x> int[] remove_value(int[] array_, x value_) { return remove_key_value(array_, value_, false, false); }

	public static <x> boolean[] remove_value(boolean[] array_, x value_) { return remove_key_value(array_, value_, false, false); }

	public static <x> byte[] remove_value(byte[] array_, x value_) { return remove_key_value(array_, value_, false, false); }

	public static <x> char[] remove_value(char[] array_, x value_) { return remove_key_value(array_, value_, false, false); }

	public static <x> Object remove_value(Object array_, x value_) { return remove_value(array_, value_, false); }

	public static <x> Object remove_value(Object array_, x value_, boolean normalise_) { return remove_key_value(array_, value_, normalise_, false); }

	public static <x, y> Object get_value(HashMap<x, y> array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(double[] array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(long[] array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(int[] array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(boolean[] array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(byte[] array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(char[] array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_value(Object array_, x key_) { return key_value_get_exists(array_, key_, true, true); }

	public static <x> Object get_key(double[] array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static <x> Object get_key(long[] array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static <x> Object get_key(int[] array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static <x> Object get_key(boolean[] array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static <x> Object get_key(byte[] array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static <x> Object get_key(char[] array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static <x> Object get_key(Object array_, x value_) { return key_value_get_exists(array_, value_, false, true); }

	public static boolean keys_exist(double[] array_, int[] keys_) { return keys_exist(to_big(array_), to_big(keys_)); }

	public static boolean keys_exist(long[] array_, int[] keys_) { return keys_exist(to_big(array_), to_big(keys_)); }

	public static boolean keys_exist(int[] array_, int[] keys_) { return keys_exist(to_big(array_), to_big(keys_)); }

	public static boolean keys_exist(boolean[] array_, int[] keys_) { return keys_exist(to_big(array_), to_big(keys_)); }

	public static boolean keys_exist(byte[] array_, int[] keys_) { return keys_exist(to_big(array_), to_big(keys_)); }

	public static boolean keys_exist(char[] array_, int[] keys_) { return keys_exist(to_big(array_), to_big(keys_)); }

	public static boolean keys_exist(double[] array_, Integer[] keys_) { return keys_exist(to_big(array_), keys_); }

	public static boolean keys_exist(long[] array_, Integer[] keys_) { return keys_exist(to_big(array_), keys_); }

	public static boolean keys_exist(int[] array_, Integer[] keys_) { return keys_exist(to_big(array_), keys_); }

	public static boolean keys_exist(boolean[] array_, Integer[] keys_) { return keys_exist(to_big(array_), keys_); }

	public static boolean keys_exist(byte[] array_, Integer[] keys_) { return keys_exist(to_big(array_), keys_); }

	public static boolean keys_exist(char[] array_, Integer[] keys_) { return keys_exist(to_big(array_), keys_); }

	public static boolean keys_exist(Object array_, double[] keys_) { return keys_exist(array_, to_big(keys_)); }

	public static boolean keys_exist(Object array_, long[] keys_) { return keys_exist(array_, to_big(keys_)); }

	public static boolean keys_exist(Object array_, int[] keys_) { return keys_exist(array_, to_big(keys_)); }

	public static boolean keys_exist(Object array_, boolean[] keys_) { return keys_exist(array_, to_big(keys_)); }

	public static boolean keys_exist(Object array_, byte[] keys_) { return keys_exist(array_, to_big(keys_)); }

	public static boolean keys_exist(Object array_, char[] keys_) { return keys_exist(array_, to_big(keys_)); }

	public static <x> boolean keys_exist(Object array_, x[] keys_) { return keys_values_exist(array_, keys_, true); }

	public static <x> boolean key_exists(double[] array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean key_exists(long[] array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean key_exists(int[] array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean key_exists(boolean[] array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean key_exists(byte[] array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean key_exists(char[] array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean key_exists(Object array_, x key_) { return (boolean)key_value_get_exists(array_, key_, true, false); }

	public static <x> boolean value_exists(double[] array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static <x> boolean value_exists(long[] array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static <x> boolean value_exists(int[] array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static <x> boolean value_exists(boolean[] array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static <x> boolean value_exists(byte[] array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static <x> boolean value_exists(char[] array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static <x> boolean value_exists(Object array_, x value_) { return (boolean)key_value_get_exists(array_, value_, false, false); }

	public static boolean values_exist(double[] array_, double[] values_) { return values_exist(to_big(array_), to_big(values_)); }

	public static boolean values_exist(long[] array_, long[] values_) { return values_exist(to_big(array_), to_big(values_)); }

	public static boolean values_exist(int[] array_, int[] values_) { return values_exist(to_big(array_), to_big(values_)); }

	public static boolean values_exist(boolean[] array_, boolean[] values_) { return values_exist(to_big(array_), to_big(values_)); }

	public static boolean values_exist(byte[] array_, byte[] values_) { return values_exist(to_big(array_), to_big(values_)); }

	public static boolean values_exist(char[] array_, char[] values_) { return values_exist(to_big(array_), to_big(values_)); }

	public static boolean values_exist(Object array_, double[] values_) { return values_exist(array_, to_big(values_)); }

	public static boolean values_exist(Object array_, long[] values_) { return values_exist(array_, to_big(values_)); }

	public static boolean values_exist(Object array_, int[] values_) { return values_exist(array_, to_big(values_)); }

	public static boolean values_exist(Object array_, boolean[] values_) { return values_exist(array_, to_big(values_)); }

	public static boolean values_exist(Object array_, byte[] values_) { return values_exist(array_, to_big(values_)); }

	public static boolean values_exist(Object array_, char[] values_) { return values_exist(array_, to_big(values_)); }

	public static <x> boolean values_exist(Object array_, x[] values_) { return keys_values_exist(array_, values_, false); }

	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> get_keys_hashmap(Object input_) { return (ArrayList<x>)get_keys_values_hashmap(input_, true); }

	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> get_values_hashmap(Object input_) { return (ArrayList<x>)get_keys_values_hashmap(input_, false); }

	public static <x, y> String to_string(HashMap<x, y> input_, String sep_items_, String sep_keyval_, String[] keys_ignore_) { return to_string(input_, sep_items_, sep_keyval_, keys_ignore_, true, true); }

	public static String to_string(Object input_, String sep_items_, String sep_keyval_, String[] keys_ignore_) { return (generic.are_equal(generic.get_class(input_), HashMap.class) ? to_string(input_, sep_items_, sep_keyval_, keys_ignore_, false, true) : strings.DEFAULT); }

	public static String to_string(double[] input_, String separator_) { return to_string(to_big(input_), separator_, true); }

	public static String to_string(long[] input_, String separator_) { return to_string(to_big(input_), separator_, true); }

	public static String to_string(int[] input_, String separator_) { return to_string(to_big(input_), separator_, true); }

	public static String to_string(boolean[] input_, String separator_) { return to_string(to_big(input_), separator_, true); }

	public static String to_string(byte[] input_, String separator_) { return strings.from_bytes_base64(input_); }

	public static String to_string(char[] input_, String separator_) { return to_string(to_big(input_), separator_, true); }

	public static <x> String to_string(x[] input_, String separator_) { return to_string(input_, separator_, true); }

	public static <x> String to_string(ArrayList<x> input_, String separator_) { return to_string(input_, separator_, true); }

	@SuppressWarnings("unchecked")
	public static <x> String to_string(Object input_, String separator_, boolean include_brackets_)
	{
		String output = strings.DEFAULT;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) return output;

		if (type.equals(double[].class)) return to_string((double[])input_, separator_);
		else if (type.equals(long[].class)) return to_string((long[])input_, separator_);
		else if (type.equals(int[].class)) return to_string((int[])input_, separator_);
		else if (type.equals(boolean[].class)) return to_string((boolean[])input_, separator_);
		else if (type.equals(byte[].class)) return to_string((byte[])input_, separator_);
		else if (type.equals(char[].class)) return to_string((char[])input_, separator_);

		boolean is_array = generic.is_array_class(type);
		boolean is_arraylist = generic.are_equal(type, ArrayList.class);
		if (!is_array && !is_arraylist) return output;

		output = "";
		String separator = (strings.is_ok(separator_) ? separator_ : misc.SEPARATOR_ITEM);
		boolean first_time = true;

		if (generic.are_equal(type, Byte[].class)) output = strings.from_bytes_base64((Byte[])input_);
		else if (is_array)
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

		if (include_brackets_ && strings.is_ok(output)) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;

		return output;
	}

	@SuppressWarnings("unchecked")
	public static <x, y> String to_string(Object input_, String sep_items_, String sep_keyval_, String[] keys_ignore_, boolean is_xy_, boolean include_brackets_)
	{
		if (!is_ok(input_) || !generic.are_equal(generic.get_class(input_), HashMap.class)) return strings.DEFAULT;

		String output = "";
		String separator1 = (strings.is_ok(sep_items_) ? sep_items_ : misc.SEPARATOR_ITEM);
		String separator2 = (strings.is_ok(sep_keyval_) ? sep_keyval_ : misc.SEPARATOR_KEYVAL);

		boolean first_time = true;

		Object input = get_new_hashmap(input_, is_xy_);
		
		for (Object item: (is_xy_ ? (HashMap<x, y>)input : (HashMap<x, x>)input).entrySet())
		{
			Object[] temp = get_entry_key_val(item, is_xy_);
			Object key = temp[0];
			Object val = temp[1];					
			if (!generic.is_ok(key) || value_exists(keys_ignore_, key)) continue; 

			if (first_time) first_time = false;
			else output += separator1;

			String key2 = (generic.is_array(key) ? key.toString() : strings.to_string(key));
			String val2 = to_string_val(val);

			output += (key2 + separator2 + val2);
		}

		if (include_brackets_ && strings.is_ok(output)) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;
		
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

	public static boolean is_small(Class<?> type_) 
	{ 
		for (Class<?> type: get_all_classes_small())
		{
			if (type == type_) return true;
		}
		
		return false; 
	}
	
	public static Double[] to_big(double[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Double[] output = new Double[tot];

		for (int i = 0; i < tot; i++) { output[i] = (Double)input_[i]; }

		return output;
	}

	public static Long[] to_big(long[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Long[] output = new Long[tot];

		for (int i = 0; i < tot; i++) { output[i] = (Long)input_[i]; }

		return output;
	}

	public static Integer[] to_big(int[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Integer[] output = new Integer[tot];

		for (int i = 0; i < tot; i++) { output[i] = (Integer)input_[i]; }

		return output;
	}

	public static Boolean[] to_big(boolean[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Boolean[] output = new Boolean[tot];

		for (int i = 0; i < tot; i++) { output[i] = (Boolean)input_[i]; }

		return output;
	}

	public static Byte[] to_big(byte[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Byte[] output = new Byte[tot];

		for (int i = 0; i < tot; i++) { output[i] = (Byte)input_[i]; }

		return output;
	}

	public static Character[] to_big(char[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Character[] output = new Character[tot];

		for (int i = 0; i < tot; i++) { output[i] = (Character)input_[i]; }

		return output;
	}
	
	public static Double[][] to_big(double[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Double[][] output = new Double[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new Double[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (Double)input_[i][i2];
		}

		return output;
	}

	public static Long[][] to_big(long[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Long[][] output = new Long[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new Long[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (Long)input_[i][i2];
		}

		return output;
	}

	public static Integer[][] to_big(int[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Integer[][] output = new Integer[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new Integer[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (Integer)input_[i][i2];
		}

		return output;
	}

	public static Boolean[][] to_big(boolean[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Boolean[][] output = new Boolean[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new Boolean[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (Boolean)input_[i][i2];
		}

		return output;
	}

	public static Byte[][] to_big(byte[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Byte[][] output = new Byte[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new Byte[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (Byte)input_[i][i2];
		}

		return output;
	}

	public static Character[][] to_big(char[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		Character[][] output = new Character[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new Character[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (Character)input_[i][i2];
		}

		return output;
	}

	public static double[] to_small(Double[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		double[] output = new double[tot];

		for (int i = 0; i < tot; i++) { output[i] = (input_[i] != null ? input_[i] : DEFAULT_DECIMAL); }

		return output;
	}

	public static long[] to_small(Long[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		long[] output = new long[tot];

		for (int i = 0; i < tot; i++) { output[i] = (input_[i] != null ? input_[i] : DEFAULT_LONG); }

		return output;
	}

	public static int[] to_small(Integer[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		int[] output = new int[tot];

		for (int i = 0; i < tot; i++) { output[i] = (input_[i] != null ? input_[i] : DEFAULT_INT); }

		return output;
	}

	public static boolean[] to_small(Boolean[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		boolean[] output = new boolean[tot];

		for (int i = 0; i < tot; i++) { output[i] = (input_[i] != null ? input_[i] : DEFAULT_BOOLEAN); }

		return output;
	}

	public static byte[] to_small(Byte[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		byte[] output = new byte[tot];

		for (int i = 0; i < tot; i++) { output[i] = (input_[i] != null ? input_[i] : DEFAULT_BYTE); }

		return output;
	}

	public static char[] to_small(Character[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		char[] output = new char[tot];

		for (int i = 0; i < tot; i++) { output[i] = (input_[i] != null ? input_[i] : DEFAULT_CHAR); }

		return output;
	}

	public static double[][] to_small(Double[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		double[][] output = new double[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new double[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (input_[i][i2] != null ? output[i][i2] : DEFAULT_DECIMAL);
		}

		return output;
	}

	public static long[][] to_small(Long[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		long[][] output = new long[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new long[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (input_[i][i2] != null ? output[i][i2] : DEFAULT_LONG);
		}

		return output;
	}

	public static int[][] to_small(Integer[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		int[][] output = new int[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new int[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (input_[i][i2] != null ? output[i][i2] : DEFAULT_INT);
		}

		return output;
	}

	public static boolean[][] to_small(Boolean[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		boolean[][] output = new boolean[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new boolean[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (input_[i][i2] != null ? output[i][i2] : DEFAULT_BOOLEAN);
		}

		return output;
	}

	public static byte[][] to_small(Byte[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		byte[][] output = new byte[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new byte[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (input_[i][i2] != null ? output[i][i2] : DEFAULT_BYTE);
		}

		return output;
	}

	public static char[][] to_small(Character[][] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;

		char[][] output = new char[tot][];

		for (int i = 0; i < tot; i++) 
		{ 
			if (input_[i] == null) continue;
			
			int tot2 = input_[i].length;
			
			output[i] = new char[tot2];
			
			for (int i2 = 0; i2 < tot2; i2++) output[i][i2] = (input_[i][i2] != null ? output[i][i2] : DEFAULT_CHAR);
		}

		return output;
	}
	
	public static <x, y> Class<?>[] get_classes_items(HashMap<x, y> input_)  { return get_classes_items(input_, true); }

	public static <x> Class<?>[] get_classes_items(Object input_) { return get_classes_items(input_, true); }

	public static Class<?> get_class_items(double[] input_) { return double.class; }

	public static Class<?> get_class_items(long[] input_) { return long.class; }

	public static Class<?> get_class_items(int[] input_) { return int.class; }

	public static Class<?> get_class_items(boolean[] input_) { return boolean.class; }

	public static Class<?> get_class_items(byte[] input_) { return byte.class; }

	public static Class<?> get_class_items(char[] input_) { return char.class; }

	public static <x> Class<?> get_class_items(Object input_) { return get_class_items(input_, true); }
	
	public static boolean hashmap_is_xy(Object input_)
	{
		Class<?>[] types = get_classes_items(input_, false);

		return (types != null && !generic.are_equal(types[0], types[1]));
	}
	
	public static boolean hashmap_is(Object input_, Class<?>[] types_) { return arrays.are_equal(types_, get_classes_items(input_, false)); }

	public static double[][] add_1d(double[][] main_, double[] new_) { return to_small(add_1d(to_big(main_), to_big(new_))); }

	public static long[][] add_1d(long[][] main_, long[] new_) { return to_small(add_1d(to_big(main_), to_big(new_))); }

	public static int[][] add_1d(int[][] main_, int[] new_) { return to_small(add_1d(to_big(main_), to_big(new_))); }

	public static boolean[][] add_1d(boolean[][] main_, boolean[] new_) { return to_small(add_1d(to_big(main_), to_big(new_))); }

	public static byte[][] add_1d(byte[][] main_, byte[] new_) { return to_small(add_1d(to_big(main_), to_big(new_))); }

	public static char[][] add_1d(char[][] main_, char[] new_) { return to_small(add_1d(to_big(main_), to_big(new_))); }

	@SuppressWarnings("unchecked")
	public static <x> x[][] add_1d(x[][] main_, x[] new_) { return (x[][])add_1d(main_, new_, true); }
	
	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x>[] add_1d_arraylist(ArrayList<x>[] main_, ArrayList<x> new_) { return (ArrayList<x>[])add_1d(main_, new_, false); }
	
	@SuppressWarnings("unchecked")
	public static <x> HashMap<x, x>[] add_1d_hashmap_xx(HashMap<x, x>[] main_, HashMap<x, x> new_) { return (HashMap<x, x>[])add_1d(main_, new_, false); }
	
	@SuppressWarnings("unchecked")
	public static <x, y> HashMap<x, y>[] add_1d_hashmap_xy(HashMap<x, y>[] main_, HashMap<x, y> new_) { return (HashMap<x, y>[])add_1d(main_, new_, false); }

	public static double[] get_1d(double[][] input_, int i_) { return to_small(get_1d(to_big(input_), i_)); }

	public static long[] get_1d(long[][] input_, int i_) { return to_small(get_1d(to_big(input_), i_)); }

	public static int[] get_1d(int[][] input_, int i_) { return to_small(get_1d(to_big(input_), i_)); }

	public static boolean[] get_1d(boolean[][] input_, int i_) { return to_small(get_1d(to_big(input_), i_)); }

	public static byte[] get_1d(byte[][] input_, int i_) { return to_small(get_1d(to_big(input_), i_)); }

	public static char[] get_1d(char[][] input_, int i_) { return to_small(get_1d(to_big(input_), i_)); }

	@SuppressWarnings("unchecked")
	public static <x> x[] get_1d(x[][] main_, int i_) { return (x[])get_1d(main_, i_, true); }
	
	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> get_1d_arraylist(ArrayList<x>[] main_, int i_) { return (ArrayList<x>)get_1d(main_, i_, false); }
	
	@SuppressWarnings("unchecked")
	public static <x> HashMap<x, x> get_1d_hashmap_xx(HashMap<x, x>[] main_, int i_) { return (HashMap<x, x>)get_1d(main_, i_, false); }
	
	@SuppressWarnings("unchecked")
	public static <x, y> HashMap<x, y> get_1d_hashmap_xy(HashMap<x, y>[] main_, int i_) { return (HashMap<x, y>)get_1d(main_, i_, false); }

	public static double[] redim(double[] array_) { return redim(array_, get_size(array_) + 1); }
	
	public static double[] redim(double[] array_, int new_size_) { return to_small(redim(to_big(array_))); }

	public static long[] redim(long[] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static long[] redim(long[] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static int[] redim(int[] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static int[] redim(int[] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static boolean[] redim(boolean[] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static boolean[] redim(boolean[] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static byte[] redim(byte[] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static byte[] redim(byte[] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static char[] redim(char[] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static char[] redim(char[] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static double[][] redim(double[][] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static double[][] redim(double[][] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static long[][] redim(long[][] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static long[][] redim(long[][] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static int[][] redim(int[][] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static int[][] redim(int[][] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static boolean[][] redim(boolean[][] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static boolean[][] redim(boolean[][] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static byte[][] redim(byte[][] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static byte[][] redim(byte[][] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static char[][] redim(char[][] array_) { return to_small(redim(to_big(array_), get_size(array_) + 1)); }
	
	public static char[][] redim(char[][] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }

	public static <x> x[] redim(x[] array_) { return redim(array_, get_size(array_) + 1); }
	
	public static <x> x[] redim(x[] array_, int new_size_) { return ((array_ != null && new_size_ > 0) ? Arrays.copyOf(array_, new_size_) : null); }
	
	static <x, y> Class<?>[] get_classes_items(HashMap<x, y> input_, boolean check_input_) { return new Class<?>[] { get_class_key_val(input_, true, true, check_input_), get_class_key_val(input_, false, true, check_input_) }; }

	static Class<?>[] get_classes_items(Object input_, boolean check_input_) 
	{ 
		Class<?> type = generic.get_class(input_);
		if (!generic.are_equal(type, HashMap.class)) return null;

		return new Class<?>[] { get_class_key_val(input_, true, false, check_input_), get_class_key_val(input_, false, false, check_input_) }; 
	}

	@SuppressWarnings("unchecked")
	static <x> Class<?> get_class_items(Object input_, boolean check_input_)
	{
		Class<?> output = null;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type) || generic.are_equal(type, HashMap.class)) return output;

		if (type.equals(double[].class)) return get_class_items((double[])input_, check_input_);
		else if (type.equals(long[].class)) return get_class_items((long[])input_, check_input_);
		else if (type.equals(int[].class)) return get_class_items((int[])input_, check_input_);
		else if (type.equals(boolean[].class)) return get_class_items((boolean[])input_, check_input_);
		else if (type.equals(byte[].class)) return get_class_items((byte[])input_, check_input_);
		else if (type.equals(char[].class)) return get_class_items((char[])input_, check_input_);
		else 
		{
			ArrayList<x> arraylist = null;
			x[] array = null;

			boolean is_arraylist = generic.are_equal(type, ArrayList.class);
			if (is_arraylist) arraylist = (ArrayList<x>)input_;
			else array = (x[])input_;

			for (int i = 0; i < (is_arraylist ? arraylist.size() : array.length); i++)
			{
				output = get_class_item((is_arraylist ? arraylist.get(i) : array[i]), output);
				if (generic.are_equal(output, Object.class)) break;
			}		
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	static <x> boolean is_ok(ArrayList<x> input_, boolean minimal_) 
	{ 
		if (minimal_) return (input_ != null);
		if (input_ == null) return false;

		Object input2 = input_;

		while (generic.are_equal(generic.get_class(input2), ArrayList.class))
		{
			if (get_size(input2) < 1) return false;

			input2 = ((ArrayList<x>)input2).get(0);
		}

		return true; 
	}

	static <x, y> boolean is_ok(HashMap<x, y> input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

	static boolean is_ok(double[] input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

	static boolean is_ok(long[] input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

	static boolean is_ok(int[] input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

	static boolean is_ok(boolean[] input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

	static boolean is_ok(byte[] input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

	static boolean is_ok(char[] input_, boolean minimal_) { return (minimal_ ? (input_ != null) : (get_size(input_) > 0)); }

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
		else if (type.equals(char[].class)) is_ok = is_ok((char[])input_);
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

	static Class<?>[] populate_all_classes_small() { return new Class<?>[] { double[].class, long[].class, int[].class, boolean[].class, byte[].class, char[].class }; }

	static Class<?>[] populate_all_classes_array()
	{
		return new Class<?>[]
		{
			Array.class, String[].class, Boolean[].class, Integer[].class, Long[].class, Double[].class, Byte[].class, Character[].class, 
			Class[].class, Method[].class, Exception[].class, LocalTime[].class, LocalDateTime[].class, size[].class, data[].class, 
			db_field[].class, db_where[].class, db_order[].class		
		};
	}

	static Class<?>[] populate_all_classes_numeric() { return new Class<?>[] { Double[].class, double[].class, Long[].class, long[].class, Integer[].class, int[].class }; }

	@SuppressWarnings("unchecked")
	private static <x> x[] get_new_array(x[] input_) 
	{ 
		x[] output = null;
		
		int tot = get_size(input_);
		if (tot == 0) return output;
		
		Class<?> type = generic.get_class(input_[0]);
		
		boolean is_hashmap = generic.are_equal(type, HashMap.class);
		
		if (is_hashmap || generic.are_equal(type, ArrayList.class)) 
		{
			output = (x[])(is_hashmap ? new HashMap[tot] : new ArrayList[tot]);
			
			for (int i = 0; i < tot; i++) output[i] = (x)get_new(input_[i]);
		}
		else output = Arrays.copyOfRange(input_, 0, tot);
		
		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x> ArrayList<x> get_new_arraylist(ArrayList<x> input_) 
	{
		ArrayList<x> output = new ArrayList<x>();
		
		int tot = get_size(input_);
		if (tot == 0) return output;
		
		Class<?> type = generic.get_class(input_.get(0));
		
		boolean is_hashmap = generic.are_equal(type, HashMap.class);
		
		if (is_hashmap || generic.are_equal(type, ArrayList.class)) 
		{
			for (int i = 0; i < tot; i++) output.add((x)(is_hashmap ? get_new_hashmap(input_.get(i)) : get_new_arraylist((ArrayList<x>)input_.get(i))));
		}
		else output = new ArrayList<x>(input_);
		
		return output;
	}

	private static Object __key_value_get_exists_sync(Object input_, Object key_val_, boolean is_key_, boolean is_get_) 
	{
		__lock();
		
		Class<?> type = generic.get_class(input_);
		Class<?> type2 = generic.get_class(key_val_);

		Object output = key_value_get_exists_return_wrong(input_, key_val_, is_key_, is_get_, type, type2);
		if (!generic.is_array(type) || type2 == null) 
		{
			__unlock();
			
			return output;
		}
		
		if (is_small(type)) output = key_value_get_exists_small(input_, key_val_, is_key_, is_get_, type, type2, output);
		else output = key_value_get_exists(input_, key_val_, is_key_, is_get_);
		
		__unlock();
		
		return output;
	}

	private static Object __remove_key_value_sync(Object input_, Object key_val_, boolean normalise_, boolean is_key_) 
	{				
		__lock();

		Object output = null; 
	
		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type)) 
		{
			__unlock();
			
			return input_;
		}

		if (is_small(type))
		{
			if (type.equals(double[].class)) output = remove_key_value(to_big((double[])input_), key_val_, normalise_, is_key_);
			else if (type.equals(long[].class)) output = remove_key_value(to_big((long[])input_), key_val_, normalise_, is_key_);
			else if (type.equals(int[].class)) output = remove_key_value(to_big((int[])input_), key_val_, normalise_, is_key_);
			else if (type.equals(boolean[].class)) output = remove_key_value(to_big((boolean[])input_), key_val_, normalise_, is_key_);
			else if (type.equals(byte[].class)) output = remove_key_value(to_big((byte[])input_), key_val_, normalise_, is_key_);
			else if (type.equals(char[].class)) output = remove_key_value(to_big((char[])input_), key_val_, normalise_, is_key_);
			
			__unlock();
			
			return output;
		}

		output = arrays.remove_key_value(input_, key_val_, normalise_, is_key_);
		
		__unlock();
		
		return output;
	}

	private static <x, y> boolean keys_values_exist(HashMap<x, y> array_, Object[] keys_values_, boolean are_keys_)
	{
		if (!is_ok(array_) || !is_ok(keys_values_)) return false;

		for (Object key: keys_values_)
		{
			if (are_keys_)
			{
				if (key_exists(array_, key)) return true;
			}
			else if (value_exists(array_, key)) return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private static <x, y> boolean keys_values_exist(Object array_, Object[] keys_values_, boolean are_keys_)
	{
		Class<?> type = generic.get_class(array_);		
		Class<?> type22 = get_class_items(keys_values_);
		if (!generic.is_array(type) || type22 == null) return false;

		Class<?> type21 = null;
		if (generic.are_equal(type, HashMap.class))
		{
			Class<?>[] types21 = get_classes_items(array_);
			if (!generic.are_equal(types21[0], types21[1])) return keys_values_exist((HashMap<x, y>)array_, keys_values_, are_keys_);

			type21 = types21[0];
		}
		else type21 = get_class_items(array_);

		if (!generic.are_equal(type21, type22)) return false;

		for (Object key: keys_values_)
		{
			if (are_keys_)
			{
				if (key_exists(array_, key)) return true;
			}
			else if (value_exists(array_, key)) return true;
		}

		return false;
	}

	private static <x, y> Object get_new_hashmap(Object input_) { return get_new_hashmap(input_, hashmap_is_xy(input_)); }

	@SuppressWarnings("unchecked")
	private static <x, y> Object get_new_hashmap(Object input_, boolean is_xy_) 
	{
		Object output = null;

		if (is_xy_) output = (!is_ok(input_) ? new HashMap<x, y>() : new HashMap<x, y>((HashMap<x, y>)input_)); 
		else output = (!is_ok(input_) ? new HashMap<x, x>() : new HashMap<x, x>((HashMap<x, x>)input_)); 

		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x, y> boolean are_equal_hashmap(Object input1_, Object input2_, boolean is_xy_)
	{
		Object input1 = get_new_hashmap(input1_, is_xy_);
		Object input2 = get_new_hashmap(input2_, is_xy_);

		if ((is_xy_ && (get_size((HashMap<x, y>)input1) != get_size((HashMap<x, y>)input2))) || (!is_xy_ && (get_size((HashMap<x, x>)input1) != get_size((HashMap<x, x>)input2)))) return false;
		if ((is_xy_ && !are_equal(get_classes_items((HashMap<x, y>)input1), get_classes_items((HashMap<x, y>)input2))) || (!is_xy_ && !are_equal(get_classes_items((HashMap<x, x>)input1), get_classes_items((HashMap<x, x>)input2)))) return false;
		if (input1.equals(input2)) return true;

		ArrayList<x> done = new ArrayList<x>();
		
		for (Object item1: (is_xy_ ? (HashMap<x, y>)input1 : (HashMap<x, x>)input1).entrySet())
		{
			Object[] temp = get_entry_key_val(item1, is_xy_);
			x key1 = (x)temp[0];
			Object val1 = temp[1];

			boolean found = false;
			boolean key1_is_ok = generic.is_ok(key1, true);
			boolean val1_is_ok = generic.is_ok(val1, true);
			
			for (Object item2: (is_xy_ ? (HashMap<x, y>)input2 : (HashMap<x, x>)input2).entrySet())
			{
				temp = get_entry_key_val(item2, is_xy_);
				x key2 = (x)temp[0];
				Object val2 = temp[1];

				boolean key2_is_ok = generic.is_ok(key2, true);
				boolean val2_is_ok = generic.is_ok(val2, true);

				if ((key1_is_ok != key2_is_ok) || (val1_is_ok != val2_is_ok)) continue;
				else if ((!generic.are_equal(key1, key2)) || (!generic.are_equal(val1, val2))) continue;

				found = true;
				done.add(key2);
			}

			if (!found) return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private static <x, y> Object[] get_entry_key_val(Object item_, boolean is_xy_)
	{
		Object key = null;
		Object val = null;

		if (is_xy_)
		{
			key = ((Entry<x, y>)item_).getKey();
			val = ((Entry<x, y>)item_).getValue();
		}
		else 
		{
			key = ((Entry<x, x>)item_).getKey();
			val = ((Entry<x, x>)item_).getValue();
		}

		return new Object[] { key, val };
	}

	private static Class<?> get_class_item(Object item_, Class<?> class_)
	{
		Class<?> output = class_;
		if (item_ == null) return output;
		
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
		else if (type.equals(byte[].class)) output = Arrays.toString((byte[])val_);
		else if (type.equals(char[].class)) output = Arrays.toString((char[])val_);
		else if (generic.are_equal(type, db_where[].class)) output = db_where.to_string((db_where[])val_);
		else if (generic.are_equal(type, db_order[].class)) output = db_order.to_string((db_order[])val_);			
		else if (generic.is_array_class(type)) output = Arrays.toString((x[])val_);
		else output = strings.to_string(val_);

		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x, y> Class<?> get_class_key_val(Object input_, boolean is_key_, boolean is_xy_, boolean check_input_)
	{
		Class<?> output = null;

		if (check_input_)
		{
			if (!is_ok(input_)) return output;			
		}

		Object input = (is_xy_ ? new HashMap<x, y>((HashMap<x, y>)input_) : new HashMap<x, x>((HashMap<x, x>)input_));
		
		for (Object item: (is_xy_ ? (HashMap<x, y>)input : (HashMap<x, x>)input).entrySet())
		{
			Object[] temp = get_entry_key_val(item, is_xy_);
			Object key = temp[0];
			Object val = temp[1];

			output = get_class_item((is_key_ ? key : val), output);
			if (generic.are_equal(output, Object.class)) break;
		}

		return output;
	}
	
	@SuppressWarnings("unchecked")
	private static <x> int index_of_internal(Object array_, x target_, int start_i_, int last_i_)
	{
		int output = WRONG_I;

		int last_i0 = get_size(array_) - 1;
		if (last_i0 < 0) return output;
					
		int last_i = ((last_i_ > WRONG_I && last_i_ < last_i0) ? last_i_ : last_i0);
		int start_i = (start_i_ > WRONG_I ? start_i_ : 0);		
		if (start_i > last_i) return output;
		
		boolean is_arraylist = generic.are_equal(generic.get_class(array_), ArrayList.class);

		for (int i = start_i; i <= last_i; i++)
		{
			Object val = (is_arraylist ? ((ArrayList<x>)array_).get(i) : ((x[])array_)[i]);

			if (generic.are_equal(target_, val)) return i;
		}

		return output;
	}

	private static <x> Object key_value_get_exists(double[] array_, x target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), target_, is_key_, get_); }

	private static <x> Object key_value_get_exists(long[] array_, x target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), target_, is_key_, get_); }

	private static <x> Object key_value_get_exists(int[] array_, x target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), target_, is_key_, get_); }

	private static <x> Object key_value_get_exists(boolean[] array_, x target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), target_, is_key_, get_); }

	private static <x> Object key_value_get_exists(byte[] array_, x target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), target_, is_key_, get_); }

	private static <x> Object key_value_get_exists(char[] array_, x target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), target_, is_key_, get_); }
		
	@SuppressWarnings("unchecked")
	private static <x, y> Object key_value_get_exists(Object array_, Object target_, boolean is_key_, boolean is_get_)
	{
		Class<?> type = generic.get_class(array_);
		Class<?> type2 = generic.get_class(target_);

		Object output = key_value_get_exists_return_wrong(array_, target_, is_key_, is_get_, type, type2);
		if (!generic.is_array(type) || type2 == null) return output;

		if (is_small(type)) output = key_value_get_exists_small(array_, target_, is_key_, is_get_, type, type2, output);
		else if (generic.are_equal(type, HashMap.class))
		{			
			Class<?>[] types22 = get_classes_items(array_);
			if ((is_key_ && !generic.are_equal(types22[0], type2)) || (!is_key_ && !generic.are_equal(types22[1], type2))) return output;

			boolean is_xy = !generic.are_equal(types22[0], types22[1]);
			Object array = get_new_hashmap(array_, is_xy);
			
			for (Object item: (is_xy ? (HashMap<x, y>)array : (HashMap<x, x>)array).entrySet())
			{
				Object[] temp = get_entry_key_val(item, is_xy);
				Object key = temp[0];
				Object val = temp[1];

				Object target2 = (is_key_ ? key : val);
				if (generic.are_equal(target_, target2)) return key_value_get_exists_return(key, val, is_key_, is_get_, true);
			}
		}
		else 
		{
			boolean is_arraylist = (generic.are_equal(type, ArrayList.class));

			for (int i = 0; i < (is_arraylist ? ((ArrayList<x>)array_).size() : ((x[])array_).length); i++)
			{
				Object key = i;
				Object val = (is_arraylist ? ((ArrayList<x>)array_).get(i) : ((x[])array_)[i]);
				
				Object target2 = (is_key_ ? key : val);
				if (generic.are_equal(target_, target2)) return key_value_get_exists_return(key, val, is_key_, is_get_, true);
			}
		}

		return output;
	}

	private static Object key_value_get_exists_return_wrong(Object array_, Object target_, boolean is_key_, boolean is_get_, Class<?> type_, Class<?> type2_)
	{
		if (!is_get_) return false;

		Object key = null;
		Object value = null;

		if (generic.are_equal(type_, HashMap.class)) 
		{
			Class<?>[] classes = get_classes_items(array_);

			key = _defaults.get(classes[0]);
			value = _defaults.get(classes[1]);
		}
		else
		{
			key = WRONG_I;
			value = _defaults.get(get_class_items(array_));
		}

		return key_value_get_exists_return(key, value, is_key_, is_get_, false);
	}

	private static Object key_value_get_exists_return(Object key_, Object val_, boolean is_key_, boolean is_get_, boolean matched_) { return (is_get_ ? (is_key_ ? val_ : key_) : matched_); }

	private static Object key_value_get_exists_small(Object array_, Object target_, boolean is_key_, boolean is_get_, Class<?> type_, Class<?> type2_, Object output_wrong_)
	{
		Object output = output_wrong_;
		if (is_key_ && !generic.are_equal(type2_, int.class)) return output;

		if (type_.equals(double[].class)) 
		{
			if (is_key_ || (!is_key_ && generic.are_equal(type2_, double.class))) output = key_value_get_exists((double[])array_, target_, is_key_, is_get_);
		}
		else if (type_.equals(long[].class)) 
		{
			if (is_key_ || (!is_key_ && generic.are_equal(type2_, long.class))) output = key_value_get_exists((long[])array_, target_, is_key_, is_get_);
		}
		else if (type_.equals(int[].class)) 
		{
			if (is_key_ || (!is_key_ && generic.are_equal(type2_, int.class))) output = key_value_get_exists((int[])array_, target_, is_key_, is_get_);
		}
		else if (type_.equals(boolean[].class)) 
		{
			if (is_key_ || (!is_key_ && generic.are_equal(type2_, boolean.class))) output = key_value_get_exists((boolean[])array_, target_, is_key_, is_get_);
		}
		else if (type_.equals(byte[].class)) 
		{
			if (is_key_ || (!is_key_ && generic.are_equal(type2_, byte.class))) output = key_value_get_exists((byte[])array_, target_, is_key_, is_get_);
		}
		else if (type_.equals(char[].class)) 
		{
			if (is_key_ || (!is_key_ && generic.are_equal(type2_, char.class))) output = key_value_get_exists((char[])array_, target_, is_key_, is_get_);
		}

		return output;
	}
	
	@SuppressWarnings("unchecked")
	private static <x, y> Object get_keys_values_hashmap(Object input_, boolean is_keys_) 
	{ 
		if (!generic.are_equal(generic.get_class(input_), HashMap.class)) return null;
		
		boolean is_xy = hashmap_is_xy(input_);
		Object input = get_new_hashmap(input_, is_xy);
		
		boolean out_is_y = (is_xy && !is_keys_);
		
		ArrayList<x> output_x = null;
		ArrayList<y> output_y = null;
		
		if (out_is_y) output_y = new ArrayList<y>();
		else output_x = new ArrayList<x>();
		
		for (Object item: (is_xy ? (HashMap<x, y>)input : (HashMap<x, x>)input).entrySet())
		{
			Object[] temp = get_entry_key_val(item, is_xy);

			x key1 = (x)temp[0];
			Object val1 = temp[1];
			
			Object out = (is_keys_ ? key1 : val1);

			if (out_is_y) output_y.add((y)out);
			else output_x.add((x)out);
		}
		
		return (out_is_y ? output_y : output_x); 
	}

	private static double[] remove_key_value(double[] array_, Object key_val_, boolean normalise_, boolean is_key_) { return to_small((Double[])remove_key_value(to_big(array_), key_val_, normalise_, is_key_)); }

	private static long[] remove_key_value(long[] array_, Object key_val_, boolean normalise_, boolean is_key_) { return to_small((Long[])remove_key_value(to_big(array_), key_val_, normalise_, is_key_)); }

	private static int[] remove_key_value(int[] array_, Object key_val_, boolean normalise_, boolean is_key_) { return to_small((Integer[])remove_key_value(to_big(array_), key_val_, normalise_, is_key_)); }

	private static boolean[] remove_key_value(boolean[] array_, Object key_val_, boolean normalise_, boolean is_key_) { return to_small((Boolean[])remove_key_value(to_big(array_), key_val_, normalise_, is_key_)); }

	private static byte[] remove_key_value(byte[] array_, Object key_val_, boolean normalise_, boolean is_key_) { return to_small((Byte[])remove_key_value(to_big(array_), key_val_, normalise_, is_key_)); }

	private static char[] remove_key_value(char[] array_, Object key_val_, boolean normalise_, boolean is_key_) { return to_small((Character[])remove_key_value(to_big(array_), key_val_, normalise_, is_key_)); }

	@SuppressWarnings("unchecked")
	private static <x, y> Object remove_key_value(Object array_, Object key_val_, boolean normalise_, boolean is_key_)
	{	
		Class<?> type = generic.get_class(array_);
		Class<?> type2 = generic.get_class(key_val_);
		if (!generic.is_array(type) || type2 == null) return array_;

		Object output = null;

		if (type.equals(double[].class)) return ((is_key_ && generic.are_equal(type2, int.class) || (!is_key_ && generic.are_equal(type2, double.class))) ? remove_key_value((double[])array_, key_val_, normalise_, is_key_) : output);
		else if (type.equals(long[].class)) return ((is_key_ && generic.are_equal(type2, int.class) || (!is_key_ && generic.are_equal(type2, long.class))) ? remove_key_value((long[])array_, key_val_, normalise_, is_key_) : output);
		else if (type.equals(int[].class)) return ((is_key_ && generic.are_equal(type2, int.class) || (!is_key_ && generic.are_equal(type2, int.class))) ? remove_key_value((int[])array_, key_val_, normalise_, is_key_) : output);
		else if (type.equals(boolean[].class)) return ((is_key_ && generic.are_equal(type2, int.class) || (!is_key_ && generic.are_equal(type2, boolean.class))) ? remove_key_value((boolean[])array_, key_val_, normalise_, is_key_) : output);
		else if (type.equals(byte[].class)) return ((is_key_ && generic.are_equal(type2, int.class) || (!is_key_ && generic.are_equal(type2, byte.class))) ? remove_key_value((byte[])array_, key_val_, normalise_, is_key_) : output);
		else if (type.equals(char[].class)) return ((is_key_ && generic.are_equal(type2, int.class) || (!is_key_ && generic.are_equal(type2, char.class))) ? remove_key_value((char[])array_, key_val_, normalise_, is_key_) : output);

		Object val01 = key_val_;

		if (generic.are_equal(type, HashMap.class))
		{
			boolean is_xy = hashmap_is_xy(array_);

			output = (is_xy ? new HashMap<x, y>() : new HashMap<x, x>());
			Object array = get_new_hashmap(array_, is_xy);
			
			for (Object item: (is_xy ? (HashMap<x, y>)array : (HashMap<x, x>)array).entrySet())
			{
				Object[] temp = get_entry_key_val(item, is_xy);
				Object key = temp[0];
				Object val = temp[1];					
				if (remove_key_value_loop(val01, (is_key_ ? key : val), normalise_)) continue;

				if (is_xy) ((HashMap<x, y>)output).put((x)key, (y)val);
				else ((HashMap<x, x>)output).put((x)key, (x)val);
			}
		}
		else
		{
			ArrayList<x> output2 = new ArrayList<x>();

			ArrayList<x> arraylist = null;
			x[] array = null;

			boolean is_arraylist = generic.are_equal(type, ArrayList.class);
			if (is_arraylist) arraylist = (ArrayList<x>)array_;
			else array = (x[])array_;

			for (int i = 0; i < (is_arraylist ? arraylist.size() : array.length); i++)
			{
				x val = (is_arraylist ? arraylist.get(i) : array[i]);
				if (remove_key_value_loop(val01, (is_key_ ? i : val), normalise_)) continue;
				
				output2.add(val);
			}

			output = (is_arraylist ? output2 : to_array(output2));	
		}

		return output;
	}

	private static boolean remove_key_value_loop(Object val01_, Object val02_, boolean normalise_)
	{
		boolean output = true;

		Class<?> type1 = generic.get_class(val01_);
		Class<?> type2 = generic.get_class(val02_);

		if (type2 != null && !generic.are_equal(val01_, val02_))
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
	
	@SuppressWarnings("unchecked")
	private static <x> Object add_1d(Object main_, Object new_, boolean is_array_)
	{
		int tot0 = get_size(main_);
		if (tot0 == 0) return null;
		
		int tot = get_size(new_);
		if (tot == 0) return get_new(main_);

		Object output = redim((is_array_ ? (x[][])main_ : (x[])main_));
		
		for (int i = 0; i <= tot0; i++)
		{
			Object item = (i == tot0 ? get_new(new_) : get_1d(main_, i, is_array_));
			if (item == null) continue;
			
			if (is_array_) ((x[][])output)[i] = (x[])get_new(item);
			else ((x[])output)[i] = (x)get_new(item);
		}
		
		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x> Object get_1d(Object input_, int i_, boolean is_array_)
	{
		int max_i = get_size(input_) - 1;
		
		return ((max_i >= 0 && i_ >= 0 && i_ <= max_i) ? get_new((is_array_ ? ((x[][])input_)[i_] : ((x[])input_)[i_])) : null);
	}
}