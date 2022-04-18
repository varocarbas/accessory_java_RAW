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
	public static final int DEFAULT_SIZE = _defaults.ARRAYS_SIZE;
	
	static { _ini.load(); }

	public static Class<?>[] get_all_classes() { return _alls.ARRAYS_CLASSES; }

	//All these classes require a special treatment. In some cases, they are assumed to be equivalent to their big counterparts.
	//Their instances can only be used as Object/x[] after having been converted into their corresponding big counterparts.
	//Methods with Object/x[] parameters have to include specific overloads for all these classes. Additionally, they have 
	//to internally (i.e., in the overload including the Object parameter) account for these scenarios too, because some calls 
	//might not be reaching the specific overloads (e.g., Object val = new int[] { 1, 2, 3 } going to method(Object input_)).
	//!!!
	public static Class<?>[] get_all_classes_small() { return _alls.ARRAYS_CLASSES_SMALL; }
	
	//All these classes are equivalent to Array.class and their instances can be used as Object/x[].
	public static Class<?>[] get_all_classes_array() { return _alls.ARRAYS_CLASSES_ARRAY; }
	
	public static Class<?>[] get_all_classes_numeric() { return _alls.ARRAYS_CLASSES_NUMERIC; }
	
	public static boolean is_ok(double[] input_) { return is_ok(input_, false); }
	
	public static boolean is_ok(long[] input_) { return is_ok(input_, false); }
	
	public static boolean is_ok(int[] input_) { return is_ok(input_, false); }
	
	public static boolean is_ok(boolean[] input_) { return is_ok(input_, false); }
	
	public static boolean is_ok(byte[] input_) { return is_ok(input_, false); }
	
	public static boolean is_ok(Object input_) { return is_ok(input_, false); }
	
	public static int get_size(double[] input_) { return (input_ == null ? 0 : input_.length); }
	
	public static int get_size(long[] input_) { return (input_ == null ? 0 : input_.length); }
	
	public static int get_size(int[] input_) { return (input_ == null ? 0 : input_.length); }
	
	public static int get_size(boolean[] input_) { return (input_ == null ? 0 : input_.length); }
	
	public static int get_size(byte[] input_) { return (input_ == null ? 0 : input_.length); }
	
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
		else if (generic.are_equal(type, ArrayList.class)) size = ((ArrayList<x>)input_).size();
		else if (generic.are_equal(type, HashMap.class)) size = (is_xy(input_) ? (HashMap<x, y>)input_ : (HashMap<x, x>)input_).size();
		else size = ((Object[])input_).length;
		
		return size;
	}
		
	public static boolean are_equal(Double[] input1_, double[] input2_) { return are_equal(input1_, to_big(input2_)); }
	
	public static boolean are_equal(Long[] input1_, long[] input2_) { return are_equal(input1_, to_big(input2_)); }
	
	public static boolean are_equal(Integer[] input1_, int[] input2_) { return are_equal(input1_, to_big(input2_)); }
	
	public static boolean are_equal(Boolean[] input1_, boolean[] input2_) { return are_equal(input1_, to_big(input2_)); }
	
	public static boolean are_equal(Byte[] input1_, byte[] input2_) { return are_equal(input1_, to_big(input2_)); }
	
	public static boolean are_equal(double[] input1_, Double[] input2_) { return are_equal(to_big(input1_), input2_); }
	
	public static boolean are_equal(long[] input1_, Long[] input2_) { return are_equal(to_big(input1_), input2_); }
	
	public static boolean are_equal(int[] input1_, Integer[] input2_) { return are_equal(to_big(input1_), input2_); }
	
	public static boolean are_equal(boolean[] input1_, Boolean[] input2_) { return are_equal(to_big(input1_), input2_); }
	
	public static boolean are_equal(byte[] input1_, Byte[] input2_) { return are_equal(to_big(input1_), input2_); }
		
	public static boolean are_equal(double[] input1_, double[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }
	
	public static boolean are_equal(long[] input1_, long[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }
	
	public static boolean are_equal(int[] input1_, int[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }
	
	public static boolean are_equal(boolean[] input1_, boolean[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }
	
	public static boolean are_equal(byte[] input1_, byte[] input2_) { return are_equal(to_big(input1_), to_big(input2_)); }
	
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
			generic.are_equal(class_, Array.class) || 
			generic.are_equal(class_, String[].class) || 
			generic.are_equal(class_, Boolean[].class) || 
			generic.are_equal(class_, Class[].class) ||
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
			
			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = strings.get_random(strings.SIZE_SMALL); }
			
			output = temp;
		}
		else if (class_.equals(double[].class))
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
		else if (generic.are_equal(class_, Class[].class))
		{
			Class<?>[] temp = new Class<?>[DEFAULT_SIZE];
			
			for (int i = 0; i < DEFAULT_SIZE; i++) { temp[i] = generic.get_random_class(); }
			
			output = temp;
		}
		
		return output;
	}

	public static ArrayList<Object> get_random_arraylist()
	{
		ArrayList<Object> output = new ArrayList<Object>();
		
		for (int i = 0; i < DEFAULT_SIZE; i++) { output.add(strings.get_random(strings.SIZE_SMALL)); }
		
		return output;		
	}

	public static HashMap<Object, Object> get_random_hashmap()
	{
		HashMap<Object, Object> output = new HashMap<Object, Object>();
		
		for (int i = 0; i < DEFAULT_SIZE; i++) { output.put(Integer.toString(i), strings.get_random(strings.SIZE_SMALL)); }
		
		return output;	
	}
	
	public static ArrayList<Double> add(ArrayList<Double> main_, double[] new_) { return (ArrayList<Double>)add(main_, to_big(new_)); }
	
	public static ArrayList<Long> add(ArrayList<Long> main_, long[] new_) { return (ArrayList<Long>)add(main_, to_big(new_)); }
	
	public static ArrayList<Integer> add(ArrayList<Integer> main_, int[] new_) { return (ArrayList<Integer>)add(main_, to_big(new_)); }
	
	public static ArrayList<Boolean> add(ArrayList<Boolean> main_, boolean[] new_) { return (ArrayList<Boolean>)add(main_, to_big(new_)); }
	
	public static ArrayList<Byte> add(ArrayList<Byte> main_, byte[] new_) { return (ArrayList<Byte>)add(main_, to_big(new_)); }

	@SuppressWarnings("unchecked")
	public static <x> ArrayList<x> add(ArrayList<x> main_, x[] new_) { return (ArrayList<x>)add(main_, to_arraylist(new_)); }
	
	@SuppressWarnings("unchecked")
	public static <x, y> Object add(Object main_, Object new_)
	{
		Class<?> type = generic.get_class(main_);
		if (!generic.is_array(type)) return null;
		
		Object output = get_new(main_);
		if (!generic.are_equal(type, generic.get_class(new_))) return output;
		if (!generic.is_ok(main_)) return get_new(new_);
		if (!generic.is_ok(new_)) return output;
		
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
					if (output2.containsKey(key)) continue;
					
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
					if (output2.containsKey(key)) continue;
					
					output2.put(key, item.getValue());
				}
				
				return output2;
			}
		}
		else if (generic.are_equal(type, ArrayList.class)) 
		{
			if (!generic.are_equal(get_class_items(main_), get_class_items(new_))) return output;
			
			ArrayList<x> output2 = (ArrayList<x>)get_new(main_);
			ArrayList<x> new2 = (ArrayList<x>)get_new(new_);
			
			for (x val: new2)
			{
				if (output2.contains(val)) continue;
				output2.add(val);
			}
			
			return output2;
		}
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public static <x> x[] to_array(Object input_)
	{
		x[] output = null;
		if (!generic.are_equal(generic.get_class(input_), ArrayList.class)) return output;
		
		ArrayList<x> input = (ArrayList<x>)input_;
		Class<?> type = get_class_items(input);
		if (!generic.is_ok(type)) return output;

		int size = input.size();
		if (generic.are_equal(type, String.class)) output = (x[])input.toArray(new String[size]);
		else if (generic.are_equal(type, Integer.class)) output = (x[])input.toArray(new Integer[size]);
		else if (generic.are_equal(type, Long.class)) output = (x[])input.toArray(new Long[size]);
		else if (generic.are_equal(type, Double.class)) output = (x[])input.toArray(new Double[size]);
		else if (generic.are_equal(type, Boolean.class)) output = (x[])input.toArray(new Boolean[size]);
		else if (generic.are_equal(type, Byte.class)) output = (x[])input.toArray(new Byte[size]);
		else if (generic.are_equal(type, Class.class)) output = (x[])input.toArray(new Class<?>[size]);
		else if (generic.are_equal(type, Method.class)) output = (x[])input.toArray(new Method[size]);
		else if (generic.are_equal(type, Exception.class)) output = (x[])input.toArray(new Exception[size]);
		else if (generic.are_equal(type, size.class)) output = (x[])input.toArray(new size[size]);
		else if (generic.are_equal(type, data.class)) output = (x[])input.toArray(new data[size]);
		else if (generic.are_equal(type, db_field.class)) output = (x[])input.toArray(new db_field[size]);
		else if (generic.are_equal(type, db_where.class)) output = (x[])input.toArray(new db_where[size]);
		else if (generic.are_equal(type, db_order.class)) output = (x[])input.toArray(new db_order[size]);
		else if (generic.are_equal(type, Object.class)) output = (x[])input.toArray(new Object[size]);

		return output;
	}
	
	public static <x> ArrayList<x> get_new(ArrayList<x> input_) { return (!is_ok(input_) ? new ArrayList<x>() : new ArrayList<x>(input_)); }
	
	public static double[] get_new(double[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }
	
	public static long[] get_new(long[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }
	
	public static int[] get_new(int[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }
	
	public static boolean[] get_new(boolean[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }
	
	public static byte[] get_new(byte[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }
	
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
		else if (generic.are_equal(type, HashMap.class)) output = get_new_hashmap(input_); 
		else output = get_new_array((x[])input_);
		
		return output;
	}
	
	public static ArrayList<Double> to_arraylist(double[] input_) { return (is_ok(input_) ? new ArrayList<Double>(Arrays.asList((Double[])to_big(input_))) : new ArrayList<Double>()); }
	
	public static ArrayList<Long> to_arraylist(long[] input_) { return (is_ok(input_) ? new ArrayList<Long>(Arrays.asList((Long[])to_big(input_))) : new ArrayList<Long>()); }
	
	public static ArrayList<Integer> to_arraylist(int[] input_) { return (is_ok(input_) ? new ArrayList<Integer>(Arrays.asList((Integer[])to_big(input_))) : new ArrayList<Integer>()); }
	
	public static ArrayList<Boolean> to_arraylist(boolean[] input_) { return (is_ok(input_) ? new ArrayList<Boolean>(Arrays.asList((Boolean[])to_big(input_))) : new ArrayList<Boolean>()); }
	
	public static ArrayList<Byte> to_arraylist(byte[] input_) { return (is_ok(input_) ? new ArrayList<Byte>(Arrays.asList((Byte[])to_big(input_))) : new ArrayList<Byte>()); }
	
	public static <x> ArrayList<x> to_arraylist(x[] input_) { return (is_ok(input_) ? new ArrayList<x>(Arrays.asList(input_)) : new ArrayList<x>()); }	

	public static double[] get_range(double[] input_, int start_i, int size_) { return (double[])to_small((Double[])get_range((Double[])to_big(input_), start_i, size_)); }

	public static long[] get_range(long[] input_, int start_i, int size_) { return (long[])to_small((Long[])get_range((Long[])to_big(input_), start_i, size_)); }

	public static int[] get_range(int[] input_, int start_i, int size_) { return (int[])to_small((Integer[])get_range((Integer[])to_big(input_), start_i, size_)); }

	public static boolean[] get_range(boolean[] input_, int start_i, int size_) { return (boolean[])to_small((Boolean[])get_range((Boolean[])to_big(input_), start_i, size_)); }

	public static byte[] get_range(byte[] input_, int start_i, int size_) { return (byte[])to_small((Byte[])get_range((Byte[])to_big(input_), start_i, size_)); }
	
	@SuppressWarnings("unchecked")
	public static <x> Object get_range(Object input_, int start_i, int size_)
	{
		Object output = null;
		if (!is_ok(input_) || start_i < 0 || size_ < 0) return output;
		
		Class<?> type = generic.get_class(input_);
		if (!generic.is_array(type) || generic.are_equal(type, HashMap.class)) return output;
		
		int size0 = get_size(input_);
		int size = (size_ < 1 ? size0 : start_i + size_);
		if (size > size0) return output;
		
		if (generic.are_equal(type, ArrayList.class)) output = new ArrayList<x>(((ArrayList<x>)input_).subList(start_i, size));
		else output = Arrays.copyOfRange((x[])input_, start_i, size);
		
		return output;
	}

	public static double[] remove_key(double[] array_, double key_, boolean normalise_) { return (double[])remove_key_value(array_, key_, normalise_, true); }

	public static long[] remove_key(long[] array_, long key_, boolean normalise_) { return (long[])remove_key_value(array_, key_, normalise_, true); }

	public static int[] remove_key(int[] array_, int key_, boolean normalise_) { return (int[])remove_key_value(array_, key_, normalise_, true); }

	public static boolean[] remove_key(boolean[] array_, boolean key_, boolean normalise_) { return (boolean[])remove_key_value(array_, key_, normalise_, true); }

	public static byte[] remove_key(byte[] array_, byte key_, boolean normalise_) { return (byte[])remove_key_value(array_, key_, normalise_, true); }

	public static <x> Object remove_key(Object array_, x key_, boolean normalise_) { return remove_key_value(array_, key_, normalise_, true); }

	public static double[] remove_value(double[] array_, double value_, boolean normalise_) { return to_small((Double[])remove_key_value(to_big(array_), value_, normalise_, false)); }

	public static long[] remove_value(long[] array_, long value_, boolean normalise_) { return to_small((Long[])remove_key_value(to_big(array_), value_, normalise_, false)); }

	public static int[] remove_value(int[] array_, int value_, boolean normalise_) { return to_small((Integer[])remove_key_value(to_big(array_), value_, normalise_, false)); }

	public static boolean[] remove_value(boolean[] array_, boolean value_, boolean normalise_) { return to_small((Boolean[])remove_key_value(to_big(array_), value_, normalise_, false)); }

	public static byte[] remove_value(byte[] array_, byte value_, boolean normalise_) { return to_small((Byte[])remove_key_value(to_big(array_), value_, normalise_, false)); }
	
	public static <x> Object remove_value(Object array_, x value_, boolean normalise_) { return remove_key_value(array_, value_, normalise_, false); }
	
	@SuppressWarnings("unchecked")
	public static <x, y> y get_value(HashMap<x, y> array_, x key_) { return (y)(!is_ok(array_) ? null : key_value_get_exists(array_, key_, null, true, true)); }

	public static double get_value(double[] array_, double key_) { return get_value(to_big(array_), (Double)key_); }

	public static long get_value(long[] array_, long key_) { return get_value(to_big(array_), (Long)key_); }

	public static int get_value(int[] array_, int key_) { return get_value(to_big(array_), (Integer)key_); }

	public static boolean get_value(boolean[] array_, boolean key_) { return get_value(to_big(array_), (Boolean)key_); }

	public static byte get_value(byte[] array_, byte key_) { return get_value(to_big(array_), (Byte)key_); }

	@SuppressWarnings("unchecked")
	public static <x> x get_value(Object array_, x key_) { return (x)(!is_ok(array_) ? null : key_value_get_exists(array_, key_, false, true)); }

	public static boolean keys_exist(double[] array_, double[] keys_) { return keys_exist((Double[])to_big(array_), (Double[])to_big(keys_)); }
	
	public static boolean keys_exist(long[] array_, long[] keys_) { return keys_exist((Long[])to_big(array_), (Long[])to_big(keys_)); }
	
	public static boolean keys_exist(int[] array_, int[] keys_) { return keys_exist((Integer[])to_big(array_), (Integer[])to_big(keys_)); }
	
	public static boolean keys_exist(boolean[] array_, boolean[] keys_) { return keys_exist((Boolean[])to_big(array_), (Boolean[])to_big(keys_)); }

	public static boolean keys_exist(byte[] array_, byte[] keys_) { return keys_exist((Byte[])to_big(array_), (Byte[])to_big(keys_)); }

	public static <x> boolean keys_exist(double[] array_, x[] keys_) { return keys_exist((Double[])to_big(array_), keys_); }
	
	public static <x> boolean keys_exist(long[] array_, x[] keys_) { return keys_exist((Long[])to_big(array_), keys_); }
	
	public static <x> boolean keys_exist(int[] array_, x[] keys_) { return keys_exist((Integer[])to_big(array_), keys_); }
	
	public static <x> boolean keys_exist(boolean[] array_, x[] keys_) { return keys_exist((Boolean[])to_big(array_), keys_); }

	public static <x> boolean keys_exist(byte[] array_, x[] keys_) { return keys_exist((Byte[])to_big(array_), keys_); }

	public static boolean keys_exist(Object array_, double[] keys_) { return keys_exist(array_, (Double[])to_big(keys_)); }
	
	public static boolean keys_exist(Object array_, long[] keys_) { return keys_exist(array_, (Long[])to_big(keys_)); }
	
	public static boolean keys_exist(Object array_, int[] keys_) { return keys_exist(array_, (Integer[])to_big(keys_)); }
	
	public static boolean keys_exist(Object array_, boolean[] keys_) { return keys_exist(array_, (Boolean[])to_big(keys_)); }

	public static boolean keys_exist(Object array_, byte[] keys_) { return keys_exist(array_, (Byte[])to_big(keys_)); }

	public static <x, y> boolean keys_exist(HashMap<x, y> array_, x[] keys_) { return keys_values_exist(array_, keys_, true); }
	
	public static <x, y> boolean keys_exist(Object array_, x[] keys_) { return keys_values_exist(array_, keys_, true); }

	public static <x, y> boolean key_exists(HashMap<x, y> array_, x key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, null, true, false));	 }

	public static boolean key_exists(double[] array_, double key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false)); }
	
	public static boolean key_exists(long[] array_, long key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false)); }
	
	public static boolean key_exists(int[] array_, int key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false)); }
	
	public static boolean key_exists(boolean[] array_, boolean key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false)); }
	
	public static boolean key_exists(byte[] array_, byte key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false)); }

	public static <x> boolean key_exists(Object array_, x key_) { return ((!is_ok(array_) || !generic.is_ok(key_)) ? false : (boolean)key_value_get_exists(array_, key_, true, false)); }

	public static boolean values_exist(double[] array_, double[] values_) { return values_exist((Double[])to_big(array_), (Double[])to_big(values_)); }
	
	public static boolean values_exist(long[] array_, long[] values_) { return values_exist((Long[])to_big(array_), (Long[])to_big(values_)); }
	
	public static boolean values_exist(int[] array_, int[] values_) { return values_exist((Integer[])to_big(array_), (Integer[])to_big(values_)); }
	
	public static boolean values_exist(boolean[] array_, boolean[] values_) { return values_exist((Boolean[])to_big(array_), (Boolean[])to_big(values_)); }

	public static boolean values_exist(byte[] array_, byte[] values_) { return values_exist((Byte[])to_big(array_), (Byte[])to_big(values_)); }

	public static <x> boolean values_exist(double[] array_, x[] values_) { return values_exist((Double[])to_big(array_), values_); }
	
	public static <x> boolean values_exist(long[] array_, x[] values_) { return values_exist((Long[])to_big(array_), values_); }
	
	public static <x> boolean values_exist(int[] array_, x[] values_) { return values_exist((Integer[])to_big(array_), values_); }
	
	public static <x> boolean values_exist(boolean[] array_, x[] values_) { return values_exist((Boolean[])to_big(array_), values_); }

	public static <x> boolean values_exist(byte[] array_, x[] values_) { return values_exist((Byte[])to_big(array_), values_); }

	public static boolean values_exist(Object array_, double[] values_) { return values_exist(array_, (Double[])to_big(values_)); }
	
	public static boolean values_exist(Object array_, long[] values_) { return values_exist(array_, (Long[])to_big(values_)); }
	
	public static boolean values_exist(Object array_, int[] values_) { return values_exist(array_, (Integer[])to_big(values_)); }
	
	public static boolean values_exist(Object array_, boolean[] values_) { return values_exist(array_, (Boolean[])to_big(values_)); }

	public static boolean values_exist(Object array_, byte[] values_) { return values_exist(array_, (Byte[])to_big(values_)); }

	public static <x, y> boolean values_exist(HashMap<x, y> array_, y[] values_) { return keys_values_exist(array_, values_, false); }
	
	public static <x, y> boolean values_exist(Object array_, x[] values_) { return keys_values_exist(array_, values_, false); }

	public static <x, y> boolean value_exists(HashMap<x, y> array_, y value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, null, value_, false, false));	 }

	public static boolean value_exists(double[] array_, double value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false)); }
	
	public static boolean value_exists(long[] array_, long value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false)); }
	
	public static boolean value_exists(int[] array_, int value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false)); }
	
	public static boolean value_exists(boolean[] array_, boolean value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false)); }
	
	public static boolean value_exists(byte[] array_, byte value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false)); }

	public static <x> boolean value_exists(Object array_, x value_) { return ((!is_ok(array_) || !generic.is_ok(value_)) ? false : (boolean)key_value_get_exists(array_, value_, false, false)); }
		
	public static <x, y> String to_string(HashMap<x, y> input_, String separator1_, String separator2_, String[] keys_ignore_) { return to_string_internal(input_, separator1_, separator2_, keys_ignore_, false); }
	
	public static <x> String to_string(Object input_, String separator1_, String separator2_, String[] keys_ignore_) { return (generic.are_equal(generic.get_class(input_), HashMap.class) ? to_string_internal(input_, separator1_, separator2_, keys_ignore_, false) : strings.DEFAULT); }

	public static String to_string(double[] input_, String separator_) { return to_string(to_big(input_), separator_); }
	
	public static String to_string(long[] input_, String separator_) { return to_string(to_big(input_), separator_); }
	
	public static String to_string(int[] input_, String separator_) { return to_string(to_big(input_), separator_); }
	
	public static String to_string(boolean[] input_, String separator_) { return to_string(to_big(input_), separator_); }
	
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
	
	public static double[] to_small(Double[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		double[] output = new double[tot];
		
		for (int i = 0; i < tot; i++) { output[i] = (Double)input_[i]; }
		
		return output;
	}
	
	public static long[] to_small(Long[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		long[] output = new long[tot];
		
		for (int i = 0; i < tot; i++) { output[i] = (Long)input_[i]; }
		
		return output;
	}
	
	public static int[] to_small(Integer[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		int[] output = new int[tot];
		
		for (int i = 0; i < tot; i++) { output[i] = (Integer)input_[i]; }
		
		return output;
	}
	
	public static boolean[] to_small(Boolean[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		boolean[] output = new boolean[tot];
		
		for (int i = 0; i < tot; i++) { output[i] = (boolean)input_[i]; }
		
		return output;
	}
	
	public static byte[] to_small(Byte[] input_)
	{
		int tot = get_size(input_);
		if (tot < 1) return null;
		
		byte[] output = new byte[tot];
		
		for (int i = 0; i < tot; i++) { output[i] = (byte)input_[i]; }
		
		return output;
	}
	
	public static <x, y> Class<?>[] get_classes_items(HashMap<x, y> input_)  { return get_classes_items(input_, true); }

	public static <x> Class<?>[] get_classes_items(Object input_) { return get_classes_items(input_, true); }
	
	public static Class<?> get_class_items(double[] input_) { return double.class; }
	
	public static Class<?> get_class_items(long[] input_) { return long.class; }
	
	public static Class<?> get_class_items(int[] input_) { return int.class; }
	
	public static Class<?> get_class_items(boolean[] input_) { return boolean.class; }
	
	public static Class<?> get_class_items(byte[] input_) { return byte.class; }
	
	public static <x> Class<?> get_class_items(Object input_) { return get_class_items(input_, true); }

	static <x, y> Class<?>[] get_classes_items(HashMap<x, y> input_, boolean check_input_) 
	{ 
		return new Class<?>[] { get_class_key_val(input_, true, true, check_input_), get_class_key_val(input_, false, true, check_input_) }; 
	}

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
		else if (generic.are_equal(type, ArrayList.class)) 
		{
			for (x item: (ArrayList<x>)input_)
			{
				output = get_class_item(item, output);
				if (generic.are_equal(output, Object.class)) break;
			}			
		}
		else
		{
			for (x item: (x[])input_)
			{
				output = get_class_item(item, output);
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
	
	static Class<?>[] populate_all_classes_small() { return new Class<?>[] { double[].class, long[].class, int[].class, boolean[].class, byte[].class }; }
	
	static Class<?>[] populate_all_classes_array()
	{
		return new Class<?>[]
		{
			Array.class, String[].class, Boolean[].class, Integer[].class, Long[].class, Double[].class, Byte[].class, 
			Class[].class, Method[].class, Exception[].class, LocalTime[].class, LocalDateTime[].class, size[].class, 
			data[].class, db_field[].class, db_where[].class, db_order[].class		
		};
	}
	
	static Class<?>[] populate_all_classes_numeric() { return new Class<?>[] { Double[].class, double[].class, Long[].class, long[].class, Integer[].class, int[].class }; }

	private static boolean is_xy(Object input_)
	{
		Class<?>[] types = get_classes_items(input_, false);
		
		return generic.are_equal(types[0], types[1]);
	}
	
	private static <x> x[] get_new_array(x[] input_) { return (!is_ok(input_) ? null : Arrays.copyOfRange(input_, 0, input_.length)); }
	
	private static <x> ArrayList<x> get_new_arraylist(ArrayList<x> input_) { return (!is_ok(input_) ? new ArrayList<x>() : new ArrayList<x>(input_)); }

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
		if (!is_ok(array_) || !is_ok(keys_values_)) return false;
		
		Class<?> type = generic.get_class(array_);		
		Class<?> type22 = get_class_items(keys_values_);
		if (type == null || type22 == null) return false;
		
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

	@SuppressWarnings("unchecked")
	private static <x, y> Object get_new_hashmap(Object input_) 
	{
		Object output = null;
		
		if (is_xy(input_)) output = (!is_ok(input_) ? new HashMap<x, y>() : new HashMap<x, y>((HashMap<x, y>)input_)); 
		else output = (!is_ok(input_) ? new HashMap<x, x>() : new HashMap<x, x>((HashMap<x, x>)input_)); 
	
		return output;
	}
	
	@SuppressWarnings("unchecked")
	private static <x, y> boolean are_equal_hashmap(Object input1_, Object input2_, boolean is_xy_)
	{
		if ((is_xy_ && (get_size((HashMap<x, y>)input1_) != get_size((HashMap<x, y>)input2_))) || (!is_xy_ && (get_size((HashMap<x, x>)input1_) != get_size((HashMap<x, x>)input2_)))) return false;
		if ((is_xy_ && !are_equal(get_classes_items((HashMap<x, y>)input1_), get_classes_items((HashMap<x, y>)input2_))) || (!is_xy_ && !are_equal(get_classes_items((HashMap<x, x>)input1_), get_classes_items((HashMap<x, x>)input2_)))) return false;
		if (input1_.equals(input2_)) return true;
		
		ArrayList<x> done = new ArrayList<x>();
		
		for (Object item1: (is_xy_ ? (HashMap<x, y>)input1_ : (HashMap<x, x>)input1_).entrySet())
		{
			Object[] temp = get_entry_key_val(item1, is_xy_);
			x key1 = (x)temp[0];
			Object val1 = temp[1];
			
			boolean found = false;
			boolean key1_is_ok = generic.is_ok(key1, true);
			boolean val1_is_ok = generic.is_ok(val1, true);
			
			for (Object item2: (is_xy_ ? (HashMap<x, y>)input2_ : (HashMap<x, x>)input2_).entrySet())
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

	@SuppressWarnings("unchecked")
	private static <x, y> Class<?> get_class_key_val(Object input_, boolean is_key_, boolean is_xy_, boolean check_input_)
	{
		Class<?> output = null;
		
		if (check_input_)
		{
			if (!is_ok(input_)) return output;			
		}
		
		for (Object item: (is_xy_ ? (HashMap<x, y>)input_ : (HashMap<x, x>)input_).entrySet())
		{
			Object[] temp = get_entry_key_val(item, is_xy_);
			Object key = temp[0];
			Object val = temp[1];
			
			output = get_class_item((is_key_ ? key : val), output);
			if (generic.are_equal(output, Object.class)) break;
		}
		
		return output;
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

	private static Object key_value_get_exists(double[] array_, double target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), (Double)target_, is_key_, get_); }

	private static Object key_value_get_exists(long[] array_, long target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), (Long)target_, is_key_, get_); }

	private static Object key_value_get_exists(int[] array_, int target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), (Integer)target_, is_key_, get_); }

	private static Object key_value_get_exists(boolean[] array_, boolean target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), (Boolean)target_, is_key_, get_); }

	private static Object key_value_get_exists(byte[] array_, byte target_, boolean is_key_, boolean get_) { return key_value_get_exists(to_big(array_), (Byte)target_, is_key_, get_); }

	@SuppressWarnings("unchecked")
	private static <x, y> Object key_value_get_exists(Object array_, Object target_, boolean is_key_, boolean get_)
	{
		Object output = (get_ ? null : false);
		
		Class<?> type = generic.get_class(array_);
		if (!generic.is_ok(type)) return output;
		
		Class<?> type2 = generic.get_class(target_);
		if (type2 == null) return output;
		
		if (type.equals(double[].class)) return (generic.are_equal(type2, double.class) ? key_value_get_exists((double[])array_, (double)target_, is_key_, get_) : output);
		else if (type.equals(long[].class)) return (generic.are_equal(type2, long.class) ? key_value_get_exists((long[])array_, (long)target_, is_key_, get_) : output);
		else if (type.equals(int[].class)) return (generic.are_equal(type2, int.class) ? key_value_get_exists((int[])array_, (int)target_, is_key_, get_) : output);
		else if (type.equals(boolean[].class)) return (generic.are_equal(type2, boolean.class) ? key_value_get_exists((boolean[])array_, (boolean)target_, is_key_, get_) : output);
		else if (type.equals(byte[].class)) return (generic.are_equal(type2, byte.class) ? key_value_get_exists((byte[])array_, (byte)target_, is_key_, get_) : output);
		else if (generic.are_equal(type, HashMap.class))
		{
			Class<?>[] types22 = get_classes_items(array_);
			if ((is_key_ && !generic.are_equal(types22[0], type2)) || (!is_key_ && !generic.are_equal(types22[1], type2))) return output;
			
			boolean is_xy = !generic.are_equal(types22[0], types22[1]);
				
			for (Object item: (is_xy ? (HashMap<x, y>)array_ : (HashMap<x, x>)array_).entrySet())
			{
				Object[] temp = get_entry_key_val(item, is_xy);
				Object key = temp[0];
				Object val = temp[1];
				
				Object target2 = (is_key_ ? key : val);
				if (generic.are_equal(target_, target2)) return (get_ ? (is_key_ ? val : key) : true);
			}
		}
		else 
		{
			Class<?> type22 = generic.get_class(target_);
			if (!generic.are_equal(type2, type22)) return output;
			
			if (generic.are_equal(type, ArrayList.class))
			{	
				ArrayList<x> array2 = ((ArrayList<x>)array_);
				
				for (int i = 0; i < array2.size(); i++)
				{
					Object key = i;
					Object val = array2.get(i);
					
					Object target2 = (is_key_ ? key : val);
					if (generic.are_equal(target_, target2)) return (get_ ? (is_key_ ? val : key) : true);
				}		
			}
			else
			{
				x[] array2 = (x[])array_;
				
				for (int i = 0; i < array2.length; i++)
				{
					Object key = i;
					Object val = array2[i];
					
					Object target2 = (is_key_ ? key : val);
					if (generic.are_equal(target_, target2)) return (get_ ? (is_key_ ? val : key) : true);
				}		
			}
		}
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	private static <x, y> String to_string_internal(Object input_, String separator1_, String separator2_, String[] keys_ignore_, boolean is_xy_)
	{
		if (!is_ok(input_)) return strings.DEFAULT;

		String output = "";
		String separator1 = (strings.is_ok(separator1_) ? separator1_ : misc.SEPARATOR_ITEM);
		String separator2 = (strings.is_ok(separator2_) ? separator2_ : misc.SEPARATOR_KEYVAL);
		
		boolean first_time = true;
		
		for (Object item: (is_xy_ ? (HashMap<x, y>)input_ : (HashMap<x, x>)input_).entrySet())
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
		
		if (!first_time) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;
		
		return output;
	}

	@SuppressWarnings("unchecked")
	private static <x, y> Object remove_key_value(Object array_, Object key_val_, boolean normalise_, boolean is_key_)
	{
		Object output = null;
		
		Class<?> type = generic.get_class(array_);
		if (!generic.is_array(type)) return output;
		
		if (type.equals(double[].class)) return (generic.are_equal(generic.get_class(key_val_), double.class) ? remove_value((double[])array_, (double)key_val_, normalise_) : output);
		else if (type.equals(long[].class)) return (generic.are_equal(generic.get_class(key_val_), long.class) ? remove_value((long[])array_, (long)key_val_, normalise_) : output);
		else if (type.equals(int[].class)) return (generic.are_equal(generic.get_class(key_val_), int.class) ? remove_value((int[])array_, (int)key_val_, normalise_) : output);
		else if (type.equals(boolean[].class)) return (generic.are_equal(generic.get_class(key_val_), boolean.class) ? remove_value((boolean[])array_, (boolean)key_val_, normalise_) : output);
		else if (type.equals(byte[].class)) return (generic.are_equal(generic.get_class(key_val_), byte.class) ? remove_value((byte[])array_, (byte)key_val_, normalise_) : output);
		
		Object val01 = key_val_;
		
		if (generic.are_equal(type, HashMap.class))
		{
			boolean is_xy = is_xy(array_);
			
			output = (is_xy ? new HashMap<x, y>() : new HashMap<x, x>());
			
			for (Object item: (is_xy ? (HashMap<x, y>)array_ : (HashMap<x, x>)array_).entrySet())
			{
				Object[] temp = get_entry_key_val(item, is_xy);
				Object key = temp[0];
				Object val = temp[1];					
				if (remove_key_value_loop(val01, (is_key_ ? key : val), normalise_)) continue;
				
				if (is_xy) ((HashMap<x, y>)output).put((x)key, (y)val);
				else ((HashMap<x, x>)output).put((x)key, (x)val);
			}
		}
		else if (generic.are_equal(type, ArrayList.class))
		{
			ArrayList<x> output2 = new ArrayList<x>();
			
			for (x item: (ArrayList<x>)array_)
			{
				if (remove_key_value_loop(val01, item, normalise_)) continue;

				output2.add(item);
			}
			
			output = output2;
		}
		else
		{
			ArrayList<x> output2 = new ArrayList<x>();
			
			for (x item: (x[])array_)
			{
				if (remove_key_value_loop(val01, item, normalise_)) continue;
				
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