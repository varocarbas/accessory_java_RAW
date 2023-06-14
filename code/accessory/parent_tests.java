package accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class parent_tests 
{	
	public static final String ERROR_RUN = _types.ERROR_TEST_RUN;

	public static final boolean DEFAULT_REPORT_ALL_ERRORS = true;
	public static final boolean DEFAULT_TEST_DB = true;
	public static final boolean DEFAULT_DROP_DB_TABLES = true;
	
	protected boolean _report_all_errors = DEFAULT_REPORT_ALL_ERRORS;
	protected boolean _test_db = DEFAULT_TEST_DB;
	protected boolean _drop_db_tables = DEFAULT_DROP_DB_TABLES;
	protected Object _temp_output = null;
	
	private static boolean _is_running = false;
	private static int _overload = 0;
	
	public static boolean is_running() { return _is_running; }

	public boolean create_table(String source_) { return create_table(source_, _drop_db_tables); }

	public boolean create_table(String source_, boolean drop_it_)
	{
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(source_);
		args.add(drop_it_);

		return run_method(db.class, "create_table", new Class<?>[] { String.class, boolean.class }, args, null);		
	}
	
	protected abstract HashMap<String, HashMap<String, Boolean>> run_all_internal();
	
	protected HashMap<String, Boolean> run_methods(Class<?> class_, String[] names_) { return run(class_, null, null, null, null, names_); }

	protected HashMap<String, Boolean> run(Class<?> class_) { return run(class_, null, null, null); }
	
	protected HashMap<String, Boolean> run(Class<?> class_, String[] names_skip_) { return run(class_, null, null, names_skip_); }

	protected HashMap<String, Boolean> run(Class<?> class_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] names_skip_) { return run(class_, null, args_, targets_, names_skip_); }
	
	protected HashMap<String, Boolean> run(Class<?> class_, Method[] methods_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] names_skip_) { return run(class_, methods_, args_, targets_, names_skip_, null); }
	
	@SuppressWarnings("unchecked")
	private HashMap<String, Boolean> run(Class<?> class_, Method[] methods_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] names_skip_, String[] names_all_)
	{
		HashMap<String, Boolean> run_outs = new HashMap<String, Boolean>();
		
		int level = 1;
		_is_running = true;
		
		Method[] methods = (arrays.is_ok(methods_) ? methods_ : generic.get_all_methods(class_, null));
		
		if (!arrays.is_ok(methods))
		{
			errors.manage(_types.ERROR_TEST_RUN, null, (generic.is_ok(class_) ? new String[] { class_.getName() } : null));
			
			return run_outs;
		}
		
		String name0 = class_.getName();
		update_screen(name0, true, level);
		
		run_outs = new HashMap<String, Boolean>();
		
		String name_prev = strings.DEFAULT;
		int overload = 0;
	
		boolean is_all = arrays.is_ok(names_all_);
		
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
			if ((is_all && !arrays.value_exists(names_all_, name)) || arrays.value_exists(names_skip_, name)) continue;
			
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
		
		update_screen(name0, false, level);
		
		_is_running = false;
		
		return run_outs;
	}
	
	protected boolean run_method(Class<?> class_, String method_name_, Class<?>[] params_, ArrayList<Object> args_, Object target_)
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
	
	protected boolean run_method(Class<?> class_, Method method_, String method_name_, ArrayList<ArrayList<Object>> args_, Object[] targets_)
	{
		boolean is_ok = false;

		_is_running = true;
		_temp_output = null;
		
		if (!method_is_ok(class_, method_, method_name_)) return is_ok;

		Class<?>[] params = (Class<?>[])arrays.get_new(method_.getParameterTypes());
		
		update_screen(method_name_ + " " + strings.to_string(params), true, 2);		

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

		generic.to_screen(result);
		
		String in = "IN: ";
		if (arrays.is_ok(args)) in += strings.to_string(args);
		generic.to_screen(in);
		
		String target = "TARGETS: ";
		if (arrays.is_ok(targets)) target += strings.to_string(targets);
		generic.to_screen(target);
		
		String out = "OUT: ";
		if (generic.is_ok(output)) out += strings.to_string(output);
		generic.to_screen(out);

		update_screen(method_name_, false, 2);
		
		_is_running = false;
		
		return is_ok;
	}

	protected static Object[] get_default_args(Class<?>[] params_)
	{
		if (!arrays.is_ok(params_)) return null;

		ArrayList<Object> output = new ArrayList<Object>();
		
		for (Class<?> param: params_) { output.add(get_default_arg(param)); }
		
		return arrays.to_array(output);
	}
	
	protected static Object get_default_arg(Class<?> class_)
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

	protected static void update_screen(String name_, boolean is_start_, int level_)
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

		generic.to_screen(output);
	}

	protected static void check_wrongs(HashMap<String, HashMap<String, Boolean>> outputs_)
	{	
		ArrayList<String> wrongs = new ArrayList<String>();
		
		if (arrays.is_ok(outputs_))
		{
			for (Entry<String, HashMap<String, Boolean>> output: outputs_.entrySet())
			{
				HashMap<String, Boolean> vals = output.getValue();
				if (!arrays.is_ok(vals)) continue;
				
				for (Entry<String, Boolean> val: vals.entrySet())
				{
					String name = val.getKey();
					if (!strings.is_ok(name)) continue;
					
					if (!val.getValue()) wrongs.add(name);
				}
			}			
		}
		
		int tot = wrongs.size();
		
		generic.to_screen("TOTAL ERRORS: " + tot);
		if (tot > 0) generic.to_screen(wrongs);		
	}
	
	private static void manage_error(String type_, Class<?> class_, String method_name_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		info.put(_keys.TYPE, type_);
		if (class_ != null) info.put("class", class_);
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