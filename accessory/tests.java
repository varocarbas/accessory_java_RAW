package accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class tests 
{
	public static boolean _running = false;
	
	private static int _overload = 0;
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_all(boolean db_too_)
	{	
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();
		
		String name0 = "accessory_main";
		System.out.println("-------- " + keys.START.toUpperCase() + " " + name0);

		outputs = run_accessory_main();
	
		System.out.println(keys.END.toUpperCase() + " " + name0);
		System.out.println();
		
		if (!db_too_) return outputs;
		
		name0 = "accessory_db";
		System.out.println("-------- " + keys.START.toUpperCase() + " " + name0);

		outputs.put(name0, run_accessory_internal(db.class));
		
		System.out.println(keys.END.toUpperCase() + " " + name0);
		System.out.println();
		
		ArrayList<String> wrongs = new ArrayList<String>();
		
		for (Entry<String, HashMap<String, Boolean>> output: outputs.entrySet())
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
		
		int tot = wrongs.size();
		System.out.println("TOTAL ERRORS: " + tot);
		if (tot > 0) System.out.println(strings.to_string(wrongs));
		
		return outputs;
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_accessory_main()
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();
		
		Class<?>[] classes = new Class<?>[] 
		{ 
			strings.class, arrays.class, dates.class, generic.class,
			io.class, numbers.class, paths.class, types.class
		}; 
		
		for (Class<?> type: classes)
		{
			outputs.put(type.getName(), run_accessory_internal(type));
		}
		
		return outputs;
	}

	public static HashMap<String, Boolean> run_accessory_db()
	{
		Class<?> type = db.class;
		
		String source = types._CONFIG_TESTS_DB_SOURCE;
				
		String name = "create_table";
		Class<?>[] params = new Class<?>[] { String.class, boolean.class };
		Method method = generic.get_method(type, name, params, false);		

		ArrayList<ArrayList<Object>> args = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> args2 = new ArrayList<Object>();
		args2.add(source);
		args2.add(true);
		args.add(args2);
		
		Object[] targets = null;
		
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();
		
		boolean is_ok = run_method(type, method, name, args, targets, false);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		name = "insert";
		params = new Class<?>[] { String.class, HashMap.class };
		method = generic.get_method(type, name, params, false);		

		args = new ArrayList<ArrayList<Object>>();
		
		args2 = new ArrayList<Object>();
		args2.add(source);
		
		HashMap<String, Object> vals = new HashMap<String, Object>();
		int max = 123456;
		vals.put(types._CONFIG_TESTS_DB_FIELD_INT, numbers.get_random_int(-1 * max, max));
		vals.put(types._CONFIG_TESTS_DB_FIELD_STRING, strings.get_random(strings.SIZE_SMALL));
		double max2 = 123456789.123;
		vals.put(types._CONFIG_TESTS_DB_FIELD_DECIMAL, numbers.get_random_decimal(-1 * max2, max2));		
		args2.add(vals);
		
		args.add(args2);

		is_ok = run_method(type, method, name, args, targets, false);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		name = "update";
		params = new Class<?>[] { String.class, HashMap.class, db_where[].class };
		method = generic.get_method(type, name, params, false);	
				
		int val = numbers.get_random_int(-1 * max, max);

		vals.put(types._CONFIG_TESTS_DB_FIELD_INT, val);		

		db_where[] wheres = new db_where[] { new db_where(source, types._CONFIG_DB_FIELDS_DEFAULT_ID, 1) };
		
		args2.add(wheres);
		
		is_ok = run_method(type, method, name, args, targets, false);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		name = "select_one_int";
		params = new Class<?>[] { String.class, String.class, db_where[].class, db_order[].class };
		method = generic.get_method(type, name, params, false);	
		
		args = new ArrayList<ArrayList<Object>>();
		
		args2 = new ArrayList<Object>();
		args2.add(source);
		args2.add(types._CONFIG_TESTS_DB_FIELD_INT);
		args2.add(wheres);
		args2.add(null);
		args.add(args2);
		
		int target = val;
		
		is_ok = run_method(type, method, name, args, new Object[] { target }, false);
		outputs.put(name, is_ok);

		name = "execute_query";
		params = new Class<?>[] { String.class };
		method = generic.get_method(type, name, params, false);	

		String field = types._CONFIG_TESTS_DB_FIELD_INT;
		String col = db.get_col(source, field);
		//String col_var = db.get_variable_col(source, field);

		String table = db.get_variable_table(source);

		db_order[] orders = new db_order[] 
		{ 
			new db_order(source, field, types.DB_ORDER_DESC, true)
			//new db_order(source, col, types.DB_ORDER_DESC, false)
		};
		
		String query = "SELECT " + db.get_variable(col) + " FROM " + table;
		query += " WHERE " + db_where.to_string(wheres);
		query += " ORDER BY " + db_order.to_string(orders);
		
		args = new ArrayList<ArrayList<Object>>();
		
		args2 = new ArrayList<Object>();
		args2.add(query);
		args.add(args2);
		
		ArrayList<HashMap<String, String>> target2 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> target22 = new HashMap<String, String>();
		target22.put(col, strings.to_string(target));		
		target2.add(target22);
		
		is_ok = run_method(type, method, name, args, new Object[] { target2 }, false);
		outputs.put(name, is_ok);
		
		return outputs;	
	}

	public static HashMap<String, Boolean> run_accessory_strings()
	{		
		Class<?> type = strings.class;
		
		HashMap<String, ArrayList<ArrayList<Object>>> args_all = new HashMap<String, ArrayList<ArrayList<Object>>>();
		
		String name = "get_random";
		
		ArrayList<ArrayList<Object>> args = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> args2 = new ArrayList<Object>();
		args2.add(strings.SIZE_DEFAULT);
		args2.add(true);
		args2.add(true);
		args2.add(true);
		args.add(args2);
		
		args2 = new ArrayList<Object>();
		args2.add(strings.SIZE_DEFAULT);
		args.add(args2);
		
		args_all.put(name, args);
		
		HashMap<String, Object[]> targets = null; 
		String[] skip = null;
		
		return run(type, args_all, targets, skip);
	}
		
	public static HashMap<String, Boolean> run_accessory_arrays()
	{
		return run(arrays.class);
	}
	
	public static HashMap<String, Boolean> run_accessory_dates()
	{
		return run(dates.class);
	}
	
	public static HashMap<String, Boolean> run_accessory_generic()
	{
		return run(generic.class);
	}
	
	public static HashMap<String, Boolean> run_accessory_io()
	{
		return run(io.class);
	}
	
	public static HashMap<String, Boolean> run_accessory_numbers()
	{
		return run(numbers.class);
	}
	
	public static HashMap<String, Boolean> run_accessory_paths()
	{
		return run(paths.class, null, null, new String[] { "update_main_dir" });
	}
	
	public static HashMap<String, Boolean> run_accessory_types()
	{
		return run(types.class);
	}
	
	public static HashMap<String, Boolean> run(Class<?> class_)
	{
		return run(class_, null, null, null);
	}
	
	public static HashMap<String, Boolean> run
	(
		Class<?> class_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] skip_
	)
	{
		return run(class_, null, args_, targets_, skip_);
	}

	public static HashMap<String, Boolean> run
	(
		Class<?> class_, Method[] methods_, HashMap<String, ArrayList<ArrayList<Object>>> args_, HashMap<String, Object[]> targets_, String[] skip_
	)
	{
		HashMap<String, Boolean> run_outs = new HashMap<String, Boolean>();

		Method[] methods = (arrays.is_ok(methods_) ? methods_ : generic.get_all_methods(class_, null));
		
		if (!arrays.is_ok(methods))
		{
			errors.manage(types.ERROR_TEST_RUN, null, (generic.is_ok(class_) ? new String[] { class_.getName() } : null), false);
			
			return run_outs;
		}
		
		String name0 = class_.getName();
		System.out.println("-------- " + keys.START.toUpperCase() + " " + name0 + misc.NEW_LINE);
		
		run_outs = new HashMap<String, Boolean>();
		
		boolean first_time = true;
		
		for (int i = 0; i < methods.length; i++)
		{
			boolean is_ok = false;
			
			String name = keys.METHOD + misc.SEPARATOR_NAME + i;
			Method method = methods[i];
			
			if (!method_is_ok(class_, method, name))
			{
				run_outs.put(name, is_ok);
				
				continue;
			}
			
			name = method.getName();
			if (arrays.value_exists(skip_, name)) continue;
		
			if (first_time) first_time = false;
			else System.out.println("");
					
			run_outs.put(name, run_method(class_, method, name, arrays.get_value(args_, name), arrays.get_value(targets_, name), true));
		}
		
		System.out.println(misc.NEW_LINE + "-------- " + keys.END.toUpperCase() + " " + name0 + misc.NEW_LINE);
		
		return run_outs;
	}

	public static boolean run_method(Class<?> class_, Method method_, String method_name_, ArrayList<ArrayList<Object>> args_, Object[] targets_, boolean errors_allowed_)
	{
		boolean is_ok = false;
		if (!method_is_ok(class_, method_, method_name_)) return is_ok;

		System.out.println(method_name_);
		
		is_ok = true;
		String result = strings.DEFAULT;

		Object[] targets = (Object[])arrays.get_new(targets_);
		Object[] args = get_args(method_.getParameterTypes(), arrays.get_new(args_));			
		Object output = generic.call_static_method(method_, args, false);

		boolean out_is_ok = output_is_ok(output, targets);
		
		if (errors._triggered || !out_is_ok)
		{
			result = "ERROR";
			if (errors._triggered && !arrays.is_ok(targets)) 
			{
				if (errors._triggered && !errors_allowed_) is_ok = false;
				errors._triggered = false;
			}
			else 
			{
				is_ok = false;
				result += " (targets not met)";
			}
		}
		else result = keys.OK.toUpperCase();

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

		System.out.println("------");
		
		_running = false;
		
		return is_ok;
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
	
	private static HashMap<String, Boolean> run_accessory_internal(Class<?> class_)
	{
		HashMap<String, Boolean> output = null;
		
		if (class_.equals(strings.class)) output = run_accessory_strings();
		else if (class_.equals(arrays.class)) output = run_accessory_arrays();
		else if (class_.equals(dates.class)) output = run_accessory_dates();
		else if (class_.equals(generic.class)) output = run_accessory_generic();
		else if (class_.equals(io.class)) output = run_accessory_io();
		else if (class_.equals(numbers.class)) output = run_accessory_numbers();
		else if (class_.equals(paths.class)) output = run_accessory_paths();
		else if (class_.equals(types.class)) output = run_accessory_types();
		else if (class_.equals(db.class)) output = run_accessory_db();

		return output;
	}

	private static boolean method_is_ok(Class<?> class_, Method method_, String method_name_)
	{
		boolean is_ok = true;
		
		if (!generic.is_ok(method_))
		{
			HashMap<String, String> info = new HashMap<String, String>();
			info.put(keys.TYPE, types.ERROR_TEST_RUN);
			info.put(keys.METHOD, strings.to_string(method_name_));
			info.put(keys.CLASS, (generic.is_ok(class_) ? class_.getName() : strings.DEFAULT));

			errors.manage(info, false);
		}	
		
		_running = is_ok;
		errors._triggered = false;
		
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
	
	@SuppressWarnings("unchecked")
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
				
				if (!generic.are_equal(generic.get_class_specific(args.get(i2)), params_[i2]))
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
		
		for (int i = 0; i < size; i++)
		{
			output[i] = ((defaults ? get_default_arg(params_[i]) : args0.get(i)));
		}
	
		return output;
	}
}