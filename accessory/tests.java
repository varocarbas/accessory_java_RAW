package accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class tests 
{
	public static void run_accessory_all()
	{
		run_accessory_basic();
	}

	public static void run_accessory_basic()
	{
		run_accessory_strings();
		run_accessory_arrays();
		run_accessory_dates();
		run_accessory_generic();
		run_accessory_io();
		run_accessory_numbers();
		run_accessory_paths();
		run_accessory_types();
	}

	public static void run_accessory_strings()
	{
		Class<?> type = strings.class;
		
		HashMap<String, ArrayList<ArrayList<Object>>> args_all = new HashMap<String, ArrayList<ArrayList<Object>>>();
		
		String name = "get_random";
		
		ArrayList<ArrayList<Object>> args0 = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(strings.SIZE_DEFAULT);
		args.add(true);
		args.add(true);
		args.add(true);
		args0.add(args);
		
		args = new ArrayList<Object>();
		args.add(strings.SIZE_DEFAULT);
		args0.add(args);
		
		args_all.put(name, args0);
		
		HashMap<String, Object[]> targets = null; 
		String[] skip = null;
		
		run(type, args_all, targets, skip);	
	}
	
	public static void run_accessory_arrays()
	{
		run(arrays.class);
	}
	
	public static void run_accessory_dates()
	{
		run(dates.class);
	}
	
	public static void run_accessory_generic()
	{
		run(generic.class);
	}
	
	public static void run_accessory_io()
	{
		run(io.class);
	}
	
	public static void run_accessory_numbers()
	{
		run(numbers.class);
	}
	
	public static void run_accessory_paths()
	{
		run(paths.class);
	}
	
	public static void run_accessory_types()
	{
		run(types.class);
	}
	
	public static void run(Class<?> class_)
	{
		run(class_, null, null, null);
	}
	
	public static void run(Class<?> class_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] skip_)
	{
		Method[] methods = generic.get_all_methods(class_, null);
		if (!arrays.is_ok(methods))
		{
			errors.manage(types.ERROR_TEST_PARAMS, null, (generic.is_ok(class_) ? new String[] { class_.getName() } : null), false);
			
			return;
		}
		
		try
		{
			for (Method method: generic.get_all_methods(class_, null))
			{
				String name = method.getName();
				if (arrays.value_exists(skip_, name)) continue;
				
				System.out.print(name + misc.SEPARATOR_CONTENT);
				
				boolean is_ok = true;
				String message = "";

				Object[] args = get_args(method.getParameterTypes(), arrays.get_value(args_, name));			
				Object output = generic.call_static_method(method, args, false);
				
				if (errors._triggered || !output_is_ok(output, arrays.get_value(targets_, name)))
				{
					if (errors._triggered) 
					{
						message = keys.ERROR.toUpperCase();
						errors._triggered = false;
					}
					else 
					{
						is_ok = false;
						message = "targets not met";
					}
				}
				else message = keys.OK.toLowerCase();
				
				message += System.lineSeparator();
				System.out.print(message);
				
				if (!is_ok) return;
			}			
		}
		catch (Exception e)
		{
			errors.manage(types.ERROR_TEST_RUN, e, new String[] { class_.getName() }, false);		
		}
	}
	
	public static Object[] get_default_args(Class<?>[] params_)
	{
		if (!arrays.is_ok(params_)) return null;

		ArrayList<Object> output = new ArrayList<Object>();
		
		for (Class<?> param: params_)
		{
			output.add(get_default_arg(param));
		}
		
		return arrays.to_array(output);
	}
	
	public static Object get_default_arg(Class<?> class_)
	{
		Object output = null;
		if (!generic.is_ok(class_)) return output;

		if (generic.classes_are_equal(class_, Double.class)) output = numbers.get_random_class(class_);
		else if (generic.classes_are_equal(class_, Long.class)) output = (long)numbers.get_random_class(class_);
		else if (generic.classes_are_equal(class_, Integer.class)) output = (int)numbers.get_random_class(class_); 
		else if (generic.classes_are_equal(class_, String.class)) output = strings.get_random(strings.SIZE_DEFAULT);
		else if (generic.classes_are_equal(class_, Boolean.class)) output = false;

		return output;
	}
	
	private static boolean output_is_ok(Object output_, Object[] targets_)
	{
		boolean output_ok = generic.is_ok(output_);
		if (!arrays.is_ok(targets_)) return true;
		
		boolean all_wrong = true;
		
		for (Object target: targets_)
		{
			if (!generic.is_ok(target))
			{
				if (!output_ok) return true;
				else continue;
			}
			else 
			{
				all_wrong = false;
				if (target.equals(output_)) return true;
			}
		}
		
		return (all_wrong && !output_ok);
	}
	
	private static Object[] get_args(Class<?>[] params_, ArrayList<ArrayList<Object>> args_all_)
	{
		int size = arrays.get_size(params_);
		if (size < 1) return null;
		
		ArrayList<ArrayList<Object>> args_all = new ArrayList<ArrayList<Object>>();
		if (arrays.is_ok(args_all_)) args_all = new ArrayList<ArrayList<Object>>(args_all_);
		else args_all.add(null);
		
		ArrayList<Object> args0 = null;
		for (ArrayList<Object> args: args_all)
		{
			if (arrays.get_size(args) != size) continue;
			
			boolean is_ok = true;
			
			for (int i = 0; i < size; i++)
			{
				if (!generic.classes_are_equal(generic.get_class(args.get(i)), params_[i]))
				{
					is_ok = false;
					break;
				}
			}
			if (!is_ok) continue;
			
			args0 = new ArrayList<Object>(args);
			break;
		}
		
		boolean defaults = (!arrays.is_ok(args0));

		Object[] output = new Object[size];
		
		for (int i = 0; i < size; i++)
		{
			output[i] = ((defaults ? get_default_arg(params_[i]) : args0.get(i)));
		}
	
		return output;
	}
}