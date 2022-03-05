package accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class tests 
{
	public static boolean _running = false;
	
	private static int _overload = 0;
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_all()
	{	
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();
		
		String name0 = "accessory_all";
		System.out.println("-------- " + keys.START.toUpperCase() + " " + name0 + misc.NEW_LINE);

		outputs = run_accessory_basic(outputs);
		
		System.out.println(misc.NEW_LINE + keys.END.toUpperCase() + " " + name0 + misc.NEW_LINE);
		
		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_accessory_basic(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();
		if (arrays.is_ok(outputs_)) outputs = new HashMap<String, HashMap<String, Boolean>>(outputs_);
	
		outputs = run_accessory_strings(outputs);
		outputs = run_accessory_arrays(outputs);	
		outputs = run_accessory_dates(outputs);
		outputs = run_accessory_generic(outputs);
		outputs = run_accessory_io(outputs);
		outputs = run_accessory_numbers(outputs);
		outputs = run_accessory_paths(outputs);
		outputs = run_accessory_types(outputs);
		
		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_accessory_strings(HashMap<String, HashMap<String, Boolean>> outputs_)
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
		
		return run(type, args_all, targets, skip, outputs_);	
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_arrays(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(arrays.class, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_dates(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(dates.class, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_generic(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(generic.class, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_io(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(io.class, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_numbers(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(numbers.class, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_paths(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(paths.class, null, null, new String[] { "update_main_dir" }, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_types(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(types.class, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>>run(Class<?> class_, HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		return run(class_, null, null, null, outputs_);
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run
	(
		Class<?> class_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, 
		String[] skip_, HashMap<String, HashMap<String, Boolean>> runs_out_
	)
	{
		_running = true;
		
		HashMap<String, HashMap<String, Boolean>> run_outs = new HashMap<String, HashMap<String, Boolean>>();
		if (arrays.is_ok(runs_out_)) run_outs = new HashMap<String, HashMap<String, Boolean>>(runs_out_);
		
		Method[] methods = generic.get_all_methods(class_, null);
		if (!arrays.is_ok(methods))
		{
			errors.manage(types.ERROR_TEST_PARAMS, null, (generic.is_ok(class_) ? new String[] { class_.getName() } : null), false);
			
			_running = false;
			
			return run_outs;
		}
		
		String name0 = class_.getName();
		System.out.println("-------- " + keys.START.toUpperCase() + " " + name0 + misc.NEW_LINE);
		
		run_outs.put(name0, new HashMap<String, Boolean>());
		
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
				
				Object[] targets = arrays.get_value(targets_, name);
				if (errors._triggered || !output_is_ok(output, targets))
				{
					if (errors._triggered && !arrays.is_ok(targets)) 
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
				
				message += misc.SEPARATOR_CONTENT + strings.to_string(output) + misc.NEW_LINE;
				System.out.print(message);
				
				run_outs.get(name0).put(name, is_ok);
			}			
		}
		catch (Exception e)
		{
			errors.manage(types.ERROR_TEST_RUN, e, new String[] { class_.getName() }, false);		
		}
		
		System.out.println(misc.NEW_LINE + "-------- " + keys.END.toUpperCase() + " " + name0 + misc.NEW_LINE);
	
		_running = false;
		
		return run_outs;
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

		if (generic.are_equal(class_, Double.class)) output = numbers.get_random_decimal(-12345.67, 12345.67);
		else if (generic.are_equal(class_, Long.class)) output = numbers.get_random_long(-12345l, 12345l);
		else if (generic.are_equal(class_, Integer.class)) output = numbers.get_random_int(-12345, 12345); 
		else if (generic.are_equal(class_, String.class)) output = strings.get_random(strings.SIZE_SMALL);
		else output = generic.get_random(class_);

		return output;
	}
	
	private static boolean output_is_ok(Object output_, Object[] targets_)
	{
		boolean output_ok = generic.is_ok(output_);
		if (!arrays.is_ok(targets_)) return true;

		boolean is_ok = false;
		Object target = (targets_.length - 1 >= _overload ? targets_[_overload] : null);
		
		if (!generic.is_ok(target)) is_ok = !output_ok;
		else is_ok = generic.are_equal(target, output_);
		
		return is_ok;
	}
	
	private static Object[] get_args(Class<?>[] params_, ArrayList<ArrayList<Object>> args_all_)
	{
		_overload = 0;
		int size = arrays.get_size(params_);
		if (size < 1) return null;
		
		ArrayList<ArrayList<Object>> args_all = new ArrayList<ArrayList<Object>>();
		if (arrays.is_ok(args_all_)) args_all = new ArrayList<ArrayList<Object>>(args_all_);
		else args_all.add(null);
		
		ArrayList<Object> args0 = null;
		for (int i = 0; i < args_all.size(); i++)
		{
			ArrayList<Object> args = args_all.get(i);
			if (arrays.get_size(args) != size) continue;
			
			boolean is_ok = true;
			
			for (int i2 = 0; i2 < size; i2++)
			{
				if (!generic.are_equal(generic.get_class(args.get(i2)), params_[i2]))
				{
					is_ok = false;
					break;
				}
			}
			if (!is_ok) continue;
			
			_overload = i;
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