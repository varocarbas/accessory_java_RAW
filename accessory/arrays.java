package accessory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class arrays 
{
	public static final int SIZE_DEFAULT = 5;
	
	static { _ini.load(); }

	public static final Class<?>[] get_all_classes()
	{
		return new Class<?>[] 
		{ 
			HashMap.class, ArrayList.class, Array.class,
			double[].class, long[].class, int[].class, boolean[].class //!!!
		};
	}
	
	public static <x> boolean is_ok(ArrayList<ArrayList<x>> input_)
	{
		return (get_size(input_) > 0 && get_size(input_.get(0)) > 0);
	}
	
	public static <x, y> boolean is_ok(HashMap<x, y> input_)
	{
		return (get_size(input_) > 0);
	}
	
	public static boolean is_ok(Object input_)
	{
		return (get_size(input_) > 0);
	}
	
	public static <x> int get_size(ArrayList<ArrayList<x>> input_)
	{
		return (input_ == null ? 0 : get_size_arraylist(input_));		
	}
	
	public static <x, y> int get_size(HashMap<x, y> input_)
	{
		return (input_ == null ? 0 : get_size_hashmap_xy(input_));		
	}
	
	@SuppressWarnings("unchecked")
	//To be synced with get_all_classes().
	public static <x> int get_size(Object input_)
	{
		int size = 0;
		if (input_ == null || !generic.is_array(input_)) return size;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_ok(type)) return size;
		
		if (is_small_big(type)) 
		{
			if (input_ instanceof double[]) size = ((double[])input_).length;
			else if (input_ instanceof long[]) size = ((long[])input_).length;
			else if (input_ instanceof int[]) size = ((int[])input_).length;
			else if (input_ instanceof boolean[]) size = ((boolean[])input_).length;
			else size = ((Object[])input_).length;
		}
		else if (generic.are_equal(type, ArrayList.class)) size = get_size_arraylist((ArrayList<x>)input_);
		else if (generic.are_equal(type, HashMap.class)) size = get_size_hashmap_xx((HashMap<x, x>)input_); 

		return size;
	}
	
	public static <x> boolean are_equal(ArrayList<ArrayList<x>> input1_, ArrayList<ArrayList<x>> input2_)
	{
		boolean is_ok1 = is_ok(input1_);
		boolean is_ok2 = is_ok(input2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		return 
		(
			(
				(
					get_size(input1_) != get_size(input2_)
				) 
				|| !generic.are_equal
				(
					get_class_items(input1_), get_class_items(input2_)
				)
			) 
			? false : input1_.equals(input2_)
		);
	}
	
	public static <x, y> boolean are_equal(HashMap<x, y> input1_, HashMap<x, y> input2_)
	{
		boolean is_ok1 = is_ok(input1_);
		boolean is_ok2 = is_ok(input2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);

		int size = get_size(input1_);
		Class<?> type_key = get_class_key_xy(input1_);
		Class<?> type_val = get_class_val_xy(input1_);
		
		return
		(
			(
				(size == get_size(input2_)) &&
				generic.are_equal(type_key, get_class_key_xy(input2_)) &&
				generic.are_equal(type_val, get_class_val_xy(input2_))
			)
			? input1_.equals(input2_) : false
		);
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
	
	@SuppressWarnings("unchecked")
	public static <x> boolean are_equal(Object input1_, Object input2_)
	{
		boolean is_ok1 = is_ok(input1_);
		boolean is_ok2 = is_ok(input2_);
		if (!is_ok1 || !is_ok2) return (is_ok1 == is_ok2);
	
		int size = get_size(input1_);
		Class<?> type = generic.get_class(input1_);
		if (!generic.is_array(type) || size != get_size(input2_)) return false;
		
		if (generic.are_equal(type, HashMap.class))
		{
			return
			(
				(
					generic.are_equal(get_class_key_xx((HashMap<x, x>)input1_), get_class_key_xx((HashMap<x, x>)input2_)) &&
					generic.are_equal(get_class_key_xx((HashMap<x, x>)input1_), get_class_val_xx((HashMap<x, x>)input2_))
				)
				? input1_.equals(input2_) : false
			);
		}
		else
		{
			if (generic.are_equal(type, ArrayList.class))
			{
				return 
				(
					(
						generic.are_equal
						(
							get_class_items((ArrayList<x>)input1_), 
							get_class_items((ArrayList<x>)input2_)
						)
					) 
					? input1_.equals(input2_) : false		
				);
			}
			
			if (!generic.are_equal(get_class_items((x[])input1_), get_class_items((x[])input2_))) return false;
		}
		
		x[] input1 = (x[])input1_;
		x[] input2 = (x[])input2_;
		
		for (int i = 0; i < size; i++)
		{
			if (!generic.is_ok(input1[i])) continue;
			if (!input1[i].equals(input2[i])) return false;
		}
				
		return true;
	}

	//To be synced with get_all_classes().
	public static Object get_random(Class<?> class_)
	{
		Object output = null;
		if (!generic.is_array(class_)) return output;
		
		if (generic.are_equal(class_, Array.class))
		{
			String[] temp = new String[SIZE_DEFAULT];
			
			for (int i = 0; i < SIZE_DEFAULT; i++)
			{
				temp[i] = strings.get_random(strings.SIZE_SMALL);
			}
			
			output = (String[])temp;
		}
		else if (generic.are_equal(class_, ArrayList.class))
		{
			ArrayList<String> temp = new ArrayList<String>();
			
			for (int i = 0; i < SIZE_DEFAULT; i++)
			{
				temp.add(strings.get_random(strings.SIZE_SMALL));
			}
			
			output = (ArrayList<String>)temp;
		}
		else if (generic.are_equal(class_, HashMap.class))
		{
			HashMap<String, String> temp = new HashMap<String, String>();
			
			for (int i = 0; i < SIZE_DEFAULT; i++)
			{
				temp.put(Integer.toString(i), strings.get_random(strings.SIZE_SMALL));
			}
			
			output = (HashMap<String, String>)temp;
		}
		
		return output;
	}

	public static <x> x[] to_array(ArrayList<ArrayList<x>> input_)
	{
		return null;
	}
	
	//To be synced with generic.get_class().
	@SuppressWarnings("unchecked")
	public static <x> x[] to_array(Object input_)
	{
		x[] output = null;
		if (input_ == null || !generic.are_equal(generic.get_class(input_), ArrayList.class)) return output;
		
		Class<?> type = get_class_items((ArrayList<x>)input_);
		if (!generic.is_ok(type)) return output;

		int size = ((ArrayList<x>)input_).size();
		if (generic.are_equal(type, String.class)) output = (x[])((ArrayList<x>)input_).toArray(new String[size]);
		else if (generic.are_equal(type, Integer.class)) output = (x[])((ArrayList<x>)input_).toArray(new Integer[size]);
		else if (generic.are_equal(type, Long.class)) output = (x[])((ArrayList<x>)input_).toArray(new Long[size]);
		else if (generic.are_equal(type, Double.class)) output = (x[])((ArrayList<x>)input_).toArray(new Double[size]);
		else if (generic.are_equal(type, Boolean.class)) output = (x[])((ArrayList<x>)input_).toArray(new Boolean[size]);
		else if (generic.are_equal(type, Class.class)) output = (x[])((ArrayList<x>)input_).toArray(new Class<?>[size]);
		else if (generic.are_equal(type, Method.class)) output = (x[])((ArrayList<x>)input_).toArray(new Method[size]);
		else if (generic.are_equal(type, Exception.class)) output = (x[])((ArrayList<x>)input_).toArray(new Exception[size]);
		else if (generic.are_equal(type, size.class)) output = (x[])((ArrayList<x>)input_).toArray(new size[size]);
		else if (generic.are_equal(type, data.class)) output = (x[])((ArrayList<x>)input_).toArray(new data[size]);
		else if (generic.are_equal(type, db_field.class)) output = (x[])((ArrayList<x>)input_).toArray(new db_field[size]);
		else if (generic.are_equal(type, db_where.class)) output = (x[])((ArrayList<x>)input_).toArray(new db_where[size]);
		else if (generic.are_equal(type, db_order.class)) output = (x[])((ArrayList<x>)input_).toArray(new db_order[size]);

		return output;
	}
	
	public static <x, y> HashMap<x, y> get_new(HashMap<x, y> input_)
	{
		return (!is_ok(input_) ? new HashMap<x, y>() : new HashMap<x, y>(input_));
	}
	
	public static <x> ArrayList<ArrayList<x>> get_new(ArrayList<ArrayList<x>> input_)
	{
		return (!is_ok(input_) ? new ArrayList<ArrayList<x>>() : new ArrayList<ArrayList<x>>(input_));
	}
	
	@SuppressWarnings("unchecked")
	//To be synced with get_all_classes().
	public static <x> Object get_new(Object input_)
	{
		Object output = null;
		if (!is_ok(input_)) return output;

		Class<?> type = generic.get_class(input_);
		if (!generic.is_ok(type)) return output;
		
		if (generic.are_equal(type, double[].class)) output = get_new_array((double[])input_);
		else if (generic.are_equal(type, long[].class)) output = get_new_array((long[])input_);
		else if (generic.are_equal(type, int[].class)) output = get_new_array((int[])input_);
		else if (generic.are_equal(type, boolean[].class)) output = get_new_array((boolean[])input_);
		else if (generic.are_equal(type, Array.class)) output = get_new_array((x[])input_);
		else if (generic.are_equal(type, ArrayList.class))  output = get_new_arraylist((ArrayList<x>)input_);
		else if (generic.are_equal(type, HashMap.class)) output = get_new_hashmap((HashMap<x, x>)input_); 

		return output;
	}

	public static double[] get_new_array(double[] input_)
	{
		return (double[])to_small(get_new_array((Double[])to_big(input_)));
	}
	
	public static long[] get_new_array(long[] input_)
	{
		return (long[])to_small(get_new_array((Long[])to_big(input_)));
	}
	
	public static int[] get_new_array(int[] input_)
	{
		return (int[])to_small(get_new_array((Integer[])to_big(input_)));
	}
	
	public static boolean[] get_new_array(boolean[] input_)
	{
		return (boolean[])to_small(get_new_array((Boolean[])to_big(input_)));
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
	
	public static <x> Class<?> get_class_key_xx(HashMap<x, x> input_)
	{
		return get_class_key_val_xx(input_, true);
	}
	
	public static <x> Class<?> get_class_val_xx(HashMap<x, x> input_)
	{
		return get_class_key_val_xx(input_, false);
	}
	
	public static <x, y> Class<?> get_class_key_xy(HashMap<x, y> input_)
	{
		return get_class_key_val_xy(input_, true);
	}
	
	public static <x, y> Class<?> get_class_val_xy(HashMap<x, y> input_)
	{
		return get_class_key_val_xy(input_, false);
	}

	public static <x> Class<?> get_class_items(ArrayList<ArrayList<x>> input_)
	{
		return (!is_ok(input_) ? null : generic.get_class(input_.get(0).get(0)));
	}

	@SuppressWarnings("unchecked")
	public static <x> Class<?> get_class_items(Object input_)
	{
		return 
		(
			!generic.are_equal(generic.get_class(input_), ArrayList.class) ? 
			null : generic.get_class(((ArrayList<x>)input_).get(0))
		);
	}

	public static Class<?> get_class_items(double[] input_)
	{
		return double[].class;
	}
	
	public static Class<?> get_class_items(long[] input_)
	{
		return long[].class;
	}
	
	public static Class<?> get_class_items(int[] input_)
	{
		return int[].class;
	}
	
	public static Class<?> get_class_items(boolean[] input_)
	{
		return boolean[].class;
	}
	
	public static <x> Class<?> get_class_items(x[] input_)
	{
		return (!is_ok(input_) ? null : generic.get_class(input_[0]));
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
	
	public static <x> ArrayList<x> to_arraylist(x[] input_)
	{
		return (is_ok(input_) ? new ArrayList<x>(Arrays.asList(input_)) : new ArrayList<x>());
	}	
	
	public static ArrayList<String> clean(ArrayList<String> input_, boolean trim_, boolean remove_wrong_)
	{
		if (!is_ok(input_)) return null;

		ArrayList<String> output = new ArrayList<String>();

		for (String item: input_)
		{
			output = clean_internal(output, item, trim_, remove_wrong_);
		}

		return output;
	}

	public static String[] clean(String[] input_, boolean trim_, boolean remove_wrong_)
	{
		if (!is_ok(input_)) return null;

		ArrayList<String> output = new ArrayList<String>();

		for (String item: input_)
		{
			output = clean_internal(output, item, trim_, remove_wrong_);
		}

		return to_array(output);
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
	
	public static <x> x[] get_range(x[] input_, int start_i, int size_)
	{
		int size0 = get_size(input_);
		int size = (size_ <= 0 ? size0 : start_i + size_);

		return 
		(
			(start_i < 0 || size > size0) ? null : 
			Arrays.copyOfRange(input_, start_i, size)
		);
	}

	@SuppressWarnings("unchecked")
	public static <x, y> y get_value(HashMap<x, y> array_, x key_)
	{
		return (y)(!arrays.is_ok(array_) ? null : key_value_get_exists(array_, key_, null, true, true));
	}

	@SuppressWarnings("unchecked")
	public static <x> x get_value(Object array_, x key_)
	{
		return (x)(!arrays.is_ok(array_) ? null : key_value_get_exists(array_, key_, false, true));
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
		if (!arrays.is_ok(array_) || !arrays.is_ok(keys_)) return false;

		for (x key: keys_)
		{
			if (key_exists(array_, key)) return true;
		}

		return false;
	}

	public static <x, y> boolean key_exists(HashMap<x, y> array_, x key_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(key_)) ? false : 
			(boolean)key_value_get_exists(array_, key_, null, true, false)
		);	
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
		if (!arrays.is_ok(array_) || !arrays.is_ok(keys_) || !generic.are_equal(generic.get_class(array_), HashMap.class)) return false;

		for (x key: keys_)
		{
			if (key_exists(array_, key)) return true;
		}

		return false;
	}

	public static <x> boolean key_exists(Object array_, x key_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(key_)) ? false : 
			(boolean)key_value_get_exists(array_, key_, true, false)
		);		
	}

	public static <x, y> boolean value_exists(HashMap<x, y> array_, y value_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(value_)) ? false : 
			(boolean)key_value_get_exists(array_, null, value_, false, false)
		);
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
	
	public static <x> boolean value_exists(Object array_, x value_)
	{
		return 
		(
			(!arrays.is_ok(array_) || !generic.is_ok(value_)) ? false : 
			(boolean)key_value_get_exists(array_, value_, false, false)
		);
	}

	public static <x, y> String to_string(HashMap<x, y> input_, String separator1_, String separator2_, String[] keys_ignore_)
	{
		if (!arrays.is_ok(input_)) return strings.DEFAULT;

		String output = "";
		String separator1 = (strings.is_ok(separator1_) ? separator1_ : misc.SEPARATOR_ITEM);
		String separator2 = (strings.is_ok(separator2_) ? separator2_ : misc.SEPARATOR_KEYVAL);

		for (Entry<x, y> item: input_.entrySet())
		{
			x key = item.getKey();
			y val = item.getValue();
			if (!generic.is_ok(key) || value_exists(keys_ignore_, key)) continue; 

			if (!output.equals("")) output += separator1;
			
			String key2 = (generic.is_array(key) ? key.toString() : strings.to_string(key));
			String val2 = strings.DEFAULT;
			if (generic.is_array(val))
			{
				if (is_ok(val)) val2 = val.toString();
			}
			else val2 = strings.to_string(val);
			
			output += (key2 + separator2 + val2);
		}

		if (strings.is_ok(output)) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;
		
		return output;
	}

	@SuppressWarnings("unchecked")
	public static <x> String to_string(Object input_, String separator1_, String separator2_, String[] keys_ignore_)
	{
		if (!generic.are_equal(generic.get_class(input_), HashMap.class)) return strings.DEFAULT;

		HashMap<x, x> input = (HashMap<x, x>)input_;
		
		String output = "";
		String separator1 = (strings.is_ok(separator1_) ? separator1_ : misc.SEPARATOR_ITEM);
		String separator2 = (strings.is_ok(separator2_) ? separator2_ : misc.SEPARATOR_KEYVAL);

		for (Entry<x, x> item: input.entrySet())
		{
			x key = item.getKey();
			x val = item.getValue();
			if (!generic.is_ok(key) || !generic.is_ok(val) || value_exists(keys_ignore_, key)) continue; 

			if (!output.equals("")) output += separator1;
			
			String key2 = (generic.is_array(key) ? key.toString() : strings.to_string(key));
			String val2 = (generic.is_array(val) ? key.toString() : strings.to_string(val));
			output += (key2 + separator2 + val2);
		}

		if (strings.is_ok(output)) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;

		return output;
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
		if (!arrays.is_ok(input_)) return output;
	
		Class<?> type = generic.get_class(input_);
		boolean is_array = is_small_big(type);
		boolean is_arraylist = generic.are_equal(type, ArrayList.class);
		if (!is_array && !is_arraylist) return output;
	
		output = "";
		String separator = (strings.is_ok(separator_) ? separator_ : misc.SEPARATOR_ITEM);

		if (is_array)
		{
			for (x item: (x[])input_)
			{
				if (!generic.is_ok(item)) continue;
				if (!output.equals("")) output += separator;

				output += (generic.is_array(item) ? item.toString() : strings.to_string(item));
			}	
		}
		else if (is_arraylist)
		{
			for (x item: (ArrayList<x>)input_)
			{
				if (!generic.is_ok(item)) continue;
				if (!output.equals("")) output += separator;

				output += (generic.is_array(item) ? item.toString() : strings.to_string(item));
			}
		}

		if (strings.is_ok(output)) output = misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE;

		return output;
	}	
	
	private static <x> Class<?> get_class_key_val_xx(HashMap<x, x> input_, boolean is_key_)
	{
		if (!is_ok(input_)) return null;
		
		for (Entry<x, x> item: input_.entrySet())
		{
			return generic.get_class((is_key_ ? item.getKey() : item.getValue()));
		}
		
		return null;
	}
	
	private static <x, y> Class<?> get_class_key_val_xy(HashMap<x, y> input_, boolean is_key_)
	{
		if (!is_ok(input_)) return null;

		for (Entry<x, y> item: input_.entrySet())
		{
			return generic.get_class((is_key_ ? item.getKey() : item.getValue()));
		}
		
		return null;
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

	private static ArrayList<String> clean_internal(ArrayList<String> output_, String item_, boolean trim_, boolean remove_wrong_)
	{
		boolean add = true;

		String item2 = item_;
		if (strings.is_ok(item2))
		{
			if (trim_) item2 = item2.trim();
		}
		else if (remove_wrong_) add = false;

		if (add) output_.add(item2);

		return output_;
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
	
	public static Object to_small(Object input_)
	{
		Object output = null;
		
		if (input_ == null) return output;
		if (!get_all_big_small().containsKey(input_.getClass())) return input_;

		if (input_ instanceof Double[])
		{
			double[] output2 = null;

			if (input_ != null)
			{
				Double[] temp = (Double[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new double[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		else if (input_ instanceof Long[])
		{
			long[] output2 = null;

			if (input_ != null)
			{
				Long[] temp = (Long[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new long[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		else if (input_ instanceof Integer[])
		{
			int[] output2 = null;

			if (input_ != null)
			{
				Integer[] temp = (Integer[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new int[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		else if (input_ instanceof Boolean[])
		{
			boolean[] output2 = null;

			if (input_ != null)
			{
				Boolean[] temp = (Boolean[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new boolean[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		
		return null;
	}
	
	public static Object to_big(Object input_)
	{
		Object output = null;
		
		if (input_ == null) return output;
		if (!get_all_big_small().containsValue(input_.getClass())) return input_;

		if (input_ instanceof double[])
		{
			Double[] output2 = null;

			if (input_ != null)
			{
				double[] temp = (double[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new Double[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		else if (input_ instanceof long[])
		{
			Long[] output2 = null;

			if (input_ != null)
			{
				long[] temp = (long[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new Long[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		else if (input_ instanceof int[])
		{
			Integer[] output2 = null;

			if (input_ != null)
			{
				int[] temp = (int[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new Integer[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		else if (input_ instanceof boolean[])
		{
			Boolean[] output2 = null;

			if (input_ != null)
			{
				boolean[] temp = (boolean[])input_;
				
				int tot = temp.length;
				if (tot < 1) return output;
				
				output2 = new Boolean[tot];
				
				for (int i = 0; i < tot; i++)
				{
					output2[i] = temp[i];
				}
				
				output = output2;
			}
		}
		
		return output;
	}
	
	//To be synced with arrays.get_all_classes().
	private static boolean is_small_big(Class<?> input_)
	{
		Class<?>[] classes = new Class<?>[] { Array.class, double[].class, long[].class, int[].class, boolean[].class };
		
		for (Class<?> type: classes)
		{
			if (generic.are_equal(input_, type)) return true;
		}

		return false;
	}

	@SuppressWarnings("unused")
	private static boolean is_small_big(double[] input_)
	{
		return true;
	}

	@SuppressWarnings("unused")
	private static boolean is_small_big(long[] input_)
	{
		return true;
	}

	@SuppressWarnings("unused")
	private static boolean is_small_big(int[] input_)
	{
		return true;
	}

	@SuppressWarnings("unused")
	private static boolean is_small_big(boolean[] input_)
	{
		return true;
	}
	
	@SuppressWarnings("unused")
	private static <x> boolean is_small_big(x input_)
	{
		return generic.is_instance(input_, Array.class);
	}

	private static HashMap<Class<?>, Class<?>> get_all_big_small()
	{
		HashMap<Class<?>, Class<?>> big_small = new HashMap<Class<?>, Class<?>>();
		
		big_small.put(Double[].class, double[].class);
		big_small.put(Long[].class, long[].class);
		big_small.put(Integer[].class, int[].class);
		big_small.put(Boolean[].class, boolean[].class);

		return big_small;
	}
}