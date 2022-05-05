package accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class tests_accessory 
{
	static { _ini.start(); }
	public static final String _ID = types.get_id(types.ID_TESTS_ACCESSORY);
	
	public static HashMap<String, HashMap<String, Boolean>> run_all()
	{	
		tests._test_db = true;
		tests._report_all_errors = true;
		
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		outputs = run_db(outputs);
		outputs = run_main(outputs);
		
		check_wrongs(outputs);
		
		return outputs;
	}

	private static void check_wrongs(HashMap<String, HashMap<String, Boolean>> outputs_)
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
		
		System.out.println("TOTAL ERRORS: " + tot);
		if (tot > 0) System.out.println(strings.to_string(wrongs));		
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_db(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);
		if (!tests._test_db) return outputs;
		
		String name = "accessory_db";
		int level = 0;
		
		tests.update_console(name, true, level);
		outputs.put(name, run_internal(db.class));
		tests.update_console(name, false, level);
		
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_main(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		String name = "accessory_main";
		int level = 0;
		
		tests.update_console(name, true, level);		
		
		Class<?>[] classes = new Class<?>[] 
		{ 
			crypto.class, strings.class, arrays.class, dates.class, generic.class,
			io.class, numbers.class, paths.class, types.class, credentials.class
		}; 
		
		for (Class<?> type: classes) { outputs.put(type.getName(), run_internal(type)); }

		tests.update_console(name, false, level);
		
		return outputs;
	}

	public static HashMap<String, Boolean> run_db()
	{			
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();
		Object[] targets = null;
		
		ArrayList<ArrayList<Object>> args = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> args2 = new ArrayList<Object>();

		Class<?> type = db.class;

		String name0 = type.getName();
		tests.update_console(name0, true, 1);
		
		String source = types.CONFIG_TESTS_DB_SOURCE;
		
		String name = "create_table";
		Class<?>[] params = new Class<?>[] { String.class, boolean.class };
		Method method = generic.get_method(type, name, params);		
		
		boolean is_ok = false;
		String[] sources = new String[] { source, credentials.SOURCE };
		
		for (String source2: sources)
		{
			args = new ArrayList<ArrayList<Object>>();
			args2 = new ArrayList<Object>();
			args2.add(source2);
			args2.add(true);
			args.add(args2);
			
			db._cur_source = source2;
			
			is_ok = tests.run_method(type, method, name, args, targets);
			outputs.put(name, is_ok);
			if (!is_ok) return outputs;			
		}

		db._cur_source = source;
		
		name = "insert";
		params = new Class<?>[] { String.class, HashMap.class };
		method = generic.get_method(type, name, params);		

		args = new ArrayList<ArrayList<Object>>();
		
		args2 = new ArrayList<Object>();
		args2.add(source);
		
		HashMap<String, Object> vals = new HashMap<String, Object>();
		int max = 123456;
		vals.put(tests.FIELD_INT, numbers.get_random_int(-1 * max, max));
		vals.put(tests.FIELD_STRING, strings.get_random(strings.SIZE_SMALL));
		double max2 = 123456789.123;
		vals.put(tests.FIELD_DECIMAL, numbers.get_random_decimal(-1 * max2, max2));		
		vals.put(tests.FIELD_BOOLEAN, generic.get_random_boolean());
		
		args2.add(vals);
		args.add(args2);

		is_ok = tests.run_method(type, method, name, args, targets);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		name = "update";
		params = new Class<?>[] { String.class, HashMap.class, db_where[].class };
		method = generic.get_method(type, name, params);	
				
		int val = numbers.get_random_int(-1 * max, max);

		vals.put(tests.FIELD_INT, val);		

		db._cur_source = source;
		db_where where = new db_where(null, db.FIELD_ID, 1);
		db_where[] wheres = new db_where[] { where };
		
		args2.add(wheres);
		
		is_ok = tests.run_method(type, method, name, args, targets);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		name = "select_one_int";
		params = new Class<?>[] { String.class, String.class, db_where[].class, db_order[].class };
		method = generic.get_method(type, name, params);	
		
		args = new ArrayList<ArrayList<Object>>();
		
		args2 = new ArrayList<Object>();
		args2.add(source);
		args2.add(tests.FIELD_INT);
		args2.add(wheres);
		args2.add(null);

		args.add(args2);
		
		int target = val;
		
		is_ok = tests.run_method(type, method, name, args, new Object[] { target });
		outputs.put(name, is_ok);

		name = "execute_query";
		params = new Class<?>[] { String.class };
		method = generic.get_method(type, name, params);	

		String field = tests.FIELD_INT;
		String col = db.get_col(source, field);

		String table = db.get_variable_table(source);

		db._cur_source = source;
		db_order[] orders = new db_order[] 
		{ 
			new db_order(null, field, db_order.DESC, true), 
			new db_order(null, where.toString(), db_order.ASC, false)
		};
		
		String query = "SELECT " + db.get_variable(source, col) + " FROM " + table;
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
		
		is_ok = tests.run_method(type, method, name, args, new Object[] { target2 });
		outputs.put(name, is_ok);
		
		tests.update_console(name0, false, 1);
		
		return outputs;	
	}
	
	public static HashMap<String, Boolean> run_crypto()
	{		
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();
		
		Class<?> class0 = crypto.class;
		String name0 = class0.getName();		
		tests.update_console(name0, true, 1);
		
		String name = "encrypt";
		
		String[] input = (String[])arrays.get_random(String[].class);
		String id = null;
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(input);
		args.add(id);
		
		Object target = null;
		
		boolean is_ok = tests.run_method(class0, name, new Class<?>[] { String[].class, String.class }, args, target);

		outputs.put(name, is_ok);
		if (!is_ok) return outputs;
		
		String[] encrypted = (String[])tests._temp_output;
		
		name = "decrypt";

		args = new ArrayList<Object>();
		args.add(encrypted);
		args.add(id);
		
		target = input;

		is_ok = tests.run_method(class0, name, new Class<?>[] { String[].class, String.class }, args, input);
		outputs.put(name, is_ok);

		tests.update_console(name0, false, 1);
		
		return outputs;	
	}

	public static HashMap<String, Boolean> run_strings()
	{		
		Class<?> type = strings.class;
		
		HashMap<String, ArrayList<ArrayList<Object>>> args_all = new HashMap<String, ArrayList<ArrayList<Object>>>();
		
		String name = "get_random";
		
		ArrayList<ArrayList<Object>> args = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> args2 = new ArrayList<Object>();
		args2.add(strings.DEFAULT_SIZE);
		args2.add(true);
		args2.add(true);
		args2.add(true);
		args.add(args2);
		
		args2 = new ArrayList<Object>();
		args2.add(strings.DEFAULT_SIZE);
		args.add(args2);
		
		args_all.put(name, args);
		
		HashMap<String, Object[]> targets = null; 
		String[] skip = null;
		
		return tests.run(type, args_all, targets, skip);
	}
				
	public static HashMap<String, Boolean> run_arrays() { return tests.run(arrays.class); }
	
	public static HashMap<String, Boolean> run_dates() { return tests.run(dates.class); }
	
	public static HashMap<String, Boolean> run_generic()
	{
		String[] skip = new String[] { "get_method", "call_static_method" };
		
		return tests.run(generic.class, skip);
	}
	
	public static HashMap<String, Boolean> run_io()
	{
		String[] skip = new String[] { "array_to_file", "line_to_file", "object_to_file", "bytes_to_file" };
		
		return tests.run(io.class, skip);
	}
	
	public static HashMap<String, Boolean> run_numbers() { return tests.run(numbers.class); }
	
	public static HashMap<String, Boolean> run_paths()
	{
		String[] skip = new String[] { "update_main_dir" };
		
		return tests.run(paths.class, skip);
	}
	
	public static HashMap<String, Boolean> run_types()
	{
		String[] skip = new String[] { "check_multiple" };
		
		return tests.run(types.class, skip);
	}
	
	public static HashMap<String, Boolean> run_credentials()
	{
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Class<?> class0 = credentials.class;
		String name0 = class0.getName();
		tests.update_console(name0, true, 1);
		
		String id = _ID;
		String user = credentials.DEFAULT_USER;
		String username = strings.get_random(strings.SIZE_SMALL);
		String password = strings.get_random(strings.SIZE_SMALL);
		
		for (boolean is_file: new boolean[] { true, false })
		{
			if (!is_file && !tests._test_db) continue;
			
			outputs = run_credentials_internal(class0, outputs, id, user, username, password, is_file);			
		}

		tests.update_console(name0, false, 1);
		
		return outputs;
	}
		
	@SuppressWarnings("unchecked")
	private static HashMap<String, Boolean> run_credentials_internal(Class<?> class_, HashMap<String, Boolean> outputs_, String id_, String user_, String username_, String password_, boolean is_file_)
	{
		HashMap<String, Boolean> outputs = (HashMap<String, Boolean>)arrays.get_new(outputs_);
		
		String name = "encrypt_username_password_" + (is_file_ ? "file" : "db");
			
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(id_);
		args.add(user_);
		args.add(username_);
		args.add(password_);

		Object target = null;
		
		boolean is_ok = tests.run_method(class_, name, new Class<?>[] { String.class, String.class, String.class, String.class }, args, target);
		
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;
		
		name = "get_username_password_" + (is_file_ ? "file" : "db");
	
		args = new ArrayList<Object>();
		args.add(id_);
		args.add(user_);
		args.add(true);
		
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put(credentials.USERNAME, username_);
		temp.put(credentials.PASSWORD, password_);
		
		target = temp; 
			
		is_ok = tests.run_method(class_, name, new Class<?>[] { String.class, String.class, boolean.class }, args, target);
		outputs.put(name, is_ok);
		
		return outputs;
	}
	
	private static HashMap<String, Boolean> run_internal(Class<?> class_)
	{
		HashMap<String, Boolean> output = null;
		
		if (class_.equals(crypto.class)) output = run_crypto();
		else if (class_.equals(strings.class)) output = run_strings();
		else if (class_.equals(arrays.class)) output = run_arrays();
		else if (class_.equals(dates.class)) output = run_dates();
		else if (class_.equals(generic.class)) output = run_generic();
		else if (class_.equals(io.class)) output = run_io();
		else if (class_.equals(numbers.class)) output = run_numbers();
		else if (class_.equals(paths.class)) output = run_paths();
		else if (class_.equals(types.class)) output = run_types();
		else if (class_.equals(credentials.class)) output = run_credentials();
		else if (class_.equals(db.class)) output = run_db();

		return output;
	}
}