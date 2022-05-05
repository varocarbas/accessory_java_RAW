package accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class tests 
{		
	public static final String SOURCE = types.CONFIG_TESTS_DB_SOURCE;
	public static final String FIELD_DECIMAL = types.CONFIG_TESTS_DB_FIELD_DECIMAL;
	public static final String FIELD_INT = types.CONFIG_TESTS_DB_FIELD_INT;
	public static final String FIELD_STRING = types.CONFIG_TESTS_DB_FIELD_STRING;
	public static final String FIELD_BOOLEAN = types.CONFIG_TESTS_DB_FIELD_BOOLEAN;

	public static final String ERROR_RUN = types.ERROR_TEST_RUN;
	
	public static boolean _report_all_errors = true;
	public static boolean _test_db = true;
	public static Object _temp_output = null;
	
	private static boolean _is_running = false;
	private static int _overload = 0;
	
	public static boolean is_running() { return _is_running; }
	
	static { _ini.start(); }
	public static final String _ID = types.get_id(types.ID_TESTS);
	
	public static HashMap<String, Boolean> run(Class<?> class_) { return run(class_, null, null, null); }
	
	public static HashMap<String, Boolean> run(Class<?> class_, String[] skip_) { return run(class_, null, null, skip_); }

	public static HashMap<String, Boolean> run(Class<?> class_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] skip_) { return run(class_, null, args_, targets_, skip_); }
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Boolean> run(Class<?> class_, Method[] methods_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] skip_)
	{
		HashMap<String, Boolean> run_outs = new HashMap<String, Boolean>();
		
		int level = 1;
		_is_running = true;
		
		Method[] methods = (arrays.is_ok(methods_) ? methods_ : generic.get_all_methods(class_, null));
		
		if (!arrays.is_ok(methods))
		{
			errors.manage(types.ERROR_TEST_RUN, null, (generic.is_ok(class_) ? new String[] { class_.getName() } : null));
			
			return run_outs;
		}
		
		String name0 = class_.getName();
		update_console(name0, true, level);
		
		run_outs = new HashMap<String, Boolean>();
		
		String name_prev = strings.DEFAULT;
		int overload = 0;
		
		for (int i = 0; i < methods.length; i++)
		{
			boolean is_ok = false;
			
			String name = "method" + misc.SEPARATOR_NAME + i;
			Method method = methods[i];
			
			if (!method_is_ok(class_, method, name))
			{
				run_outs.put(name, is_ok);
				
				continue;
			}
			
			name = method.getName();
			if (arrays.value_exists(skip_, name) || strings.are_equivalent(name, "load")) continue;
			
			String temp = name;
			if (temp.equals(name_prev))
			{
				overload++;
				temp += "(" + overload + ")";
			}
			else overload = 0;
			
			name_prev = name;
			name = temp;
			
			run_outs.put(name, run_method(class_, method, name, (ArrayList<ArrayList<Object>>)arrays.get_value(args_, name), (Object[])arrays.get_value(targets_, name)));
		}
		
		update_console(name0, false, level);
		
		_is_running = false;
		
		return run_outs;
	}
	
	public static boolean run_method(Class<?> class_, String method_name_, Class<?>[] params_, ArrayList<Object> args_, Object target_)
	{
		ArrayList<ArrayList<Object>> args = null;
		
		if (arrays.is_ok(args_))
		{
			args = new ArrayList<ArrayList<Object>>();
			args.add(args_);			
		}
	
		Object[] targets = null;
		if (target_ != null) targets = new Object[] { target_ };
		
		return run_method(class_, generic.get_method(class_, method_name_, params_), method_name_, args, targets);
	}
	
	public static boolean run_method(Class<?> class_, Method method_, String method_name_, ArrayList<ArrayList<Object>> args_, Object[] targets_)
	{
		boolean is_ok = false;

		_is_running = true;
		_temp_output = null;
		
		if (!method_is_ok(class_, method_, method_name_)) return is_ok;

		Class<?>[] params = (Class<?>[])arrays.get_new(method_.getParameterTypes());
		
		update_console(method_name_ + " " + strings.to_string(params), true, 2);		

		is_ok = true;
		String result = strings.DEFAULT;

		Object[] targets = (Object[])arrays.get_new(targets_);
		Object[] args = get_args(params, arrays.get_new(args_));			
		Object output = generic.call_static_method(method_, args);
		_temp_output = output;
		
		boolean out_is_ok = output_is_ok(output, targets);
		
		if (errors.triggered() || !out_is_ok)
		{
			is_ok = false;
			result = "ERROR";
			
			if (arrays.is_ok(targets)) result += " (targets not met)";
			else if (!_report_all_errors) is_ok = true;
		}
		else result = "OK";

		System.out.println(result);
		
		String in = "IN: ";
		if (arrays.is_ok(args)) in += strings.to_string(args);
		System.out.println(in);
		
		String target = "TARGETS: ";
		if (arrays.is_ok(targets)) target += strings.to_string(targets);
		System.out.println(target);
		
		String out = "OUT: ";
		if (generic.is_ok(output)) out += strings.to_string(output);
		System.out.println(out);

		update_console(method_name_, false, 2);
		
		_is_running = false;
		
		return is_ok;
	}

	public static Object[] get_default_args(Class<?>[] params_)
	{
		if (!arrays.is_ok(params_)) return null;

		ArrayList<Object> output = new ArrayList<Object>();
		
		for (Class<?> param: params_) { output.add(get_default_arg(param)); }
		
		return arrays.to_array(output);
	}
	
	public static Object get_default_arg(Class<?> class_)
	{
		Object output = null;
		if (class_ == null) return output;

		if (generic.are_equal(class_, Double.class)) output = numbers.get_random_decimal(-12345.67, 12345.67);
		else if (generic.are_equal(class_, Long.class)) output = numbers.get_random_long(-12345l, 12345l);
		else if (generic.are_equal(class_, Integer.class)) output = numbers.get_random_int(-123, 123); 
		else if (generic.are_equal(class_, String.class)) output = strings.get_random(strings.SIZE_SMALL);
		else output = generic.get_random(class_);

		return output;
	}

	public static void update_console(String name_, boolean is_start_, int level_)
	{
		String output = "";
		
		int max = 2;
		int count = max;
		boolean is_last = (level_ == count);
		
		while (count >= level_)
		{
			count--;
			output += "---";
		}

		if (!is_last) output += " " + (is_start_ ? "START" : "END") + " " + name_ + misc.NEW_LINE;
		else
		{
			if (is_start_) output += " " + name_;
			else output += misc.NEW_LINE;
		}

		System.out.println(output);
	}

	private static void manage_error(String type_, Class<?> class_, String method_name_)
	{
		HashMap<String, String> info = new HashMap<String, String>();
		
		info.put(generic.TYPE, type_);
		if (class_ != null) info.put("class", strings.to_string(class_));
		if (strings.is_ok(method_name_)) info.put("method", method_name_);

		errors.manage(info);
		
		_is_running = false;
	}

	private static boolean method_is_ok(Class<?> class_, Method method_, String method_name_)
	{
		boolean is_ok = true;
		
		if (!generic.is_ok(method_))
		{
			manage_error(ERROR_RUN, class_, method_name_);

			is_ok = false;
		}	
		
		return is_ok;
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

		boolean args_ok = false;
		ArrayList<ArrayList<Object>> args_all = (ArrayList<ArrayList<Object>>)arrays.get_new(args_all_);
		if (!arrays.is_ok(args_all)) args_all.add(null);
		else args_ok = true;
		
		ArrayList<Object> args0 = null;
		for (int i = 0; i < args_all.size(); i++)
		{
			ArrayList<Object> args = (ArrayList<Object>)arrays.get_new(args_all.get(i));
			if (arrays.get_size(args) != size) continue;

			boolean is_ok = true;

			for (int i2 = 0; i2 < size; i2++)
			{
				if (args.get(i2) == null) continue;
				
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
		if (defaults && args_ok) return null;
		
		Object[] output = new Object[size];
		
		for (int i = 0; i < size; i++) { output[i] = ((defaults ? get_default_arg(params_[i]) : args0.get(i))); }
	
		return output;
	}
}