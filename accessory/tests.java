package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class tests extends parent_tests 
{
	private static tests _instance = new tests();

	public tests() { }

	public static String get_class_id() { return types.get_id(types.ID_TESTS); }

	public static HashMap<String, HashMap<String, Boolean>> run_all() { return _instance.run_all_internal(); }

	public HashMap<String, HashMap<String, Boolean>> run_all_internal()
	{	
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		outputs = run_db(outputs);
		outputs = run_main(outputs);

		check_wrongs(outputs);

		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_db(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);
		if (!_test_db) return outputs;

		String name = "accessory_db";
		int level = 0;

		update_screen(name, true, level);

		outputs.put(name, run_internal(db.class));

		update_screen(name, false, level);

		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_main(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		String name = "accessory_main";
		int level = 0;

		update_screen(name, true, level);		

		Class<?>[] classes = new Class<?>[] 
		{ 
			crypto.class, strings.class, arrays.class, dates.class, generic.class,
			io.class, numbers.class, paths.class, types.class, credentials.class
		}; 

		for (Class<?> type: classes) { outputs.put(type.getName(), run_internal(type)); }

		update_screen(name, false, level);

		return outputs;
	}

	public static HashMap<String, Boolean> run_db()
	{			
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Object target = null;
		ArrayList<Object> args = null;
		boolean is_ok = false;

		Class<?> class0 = db.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);

		String source = types.CONFIG_TESTS_DB_SOURCE;

		String name = "create_table";

		String[] sources = new String[] { source, credentials.SOURCE };

		for (String source2: sources)
		{
			is_ok = create_table(source2);

			outputs.put(name, is_ok);
			if (!is_ok) return outputs;			
		}

		db._cur_source = source;

		name = "insert";

		HashMap<String, Object> vals = new HashMap<String, Object>();

		int max = 123456;
		vals.put(FIELD_INT, numbers.get_random_int(-1 * max, max));

		String val_string = strings.get_random(strings.SIZE_SMALL);
		vals.put(FIELD_STRING, val_string);

		double max2 = 123456789.123;
		vals.put(FIELD_DECIMAL, numbers.get_random_decimal(-1 * max2, max2));		
		vals.put(FIELD_BOOLEAN, generic.get_random_boolean());

		args = new ArrayList<Object>();
		args.add(source);
		args.add(vals);

		is_ok = run_method(class0, name, new Class<?>[] { String.class, HashMap.class }, args, target);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		name = "update";

		int val = numbers.get_random_int(-1 * max, max);

		vals.put(FIELD_INT, val);		

		db_where where = new db_where(FIELD_STRING, val_string);
		db_where[] wheres = new db_where[] { where };

		args.add(wheres);

		is_ok = run_method(class0, name, new Class<?>[] { String.class, HashMap.class, db_where[].class }, args, target);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		outputs = run_db_select_int(class0, source, wheres, val, outputs);

		name = "execute_query";

		String field = FIELD_INT;
		String col = db.get_col(source, field);

		String table = db.get_variable_table(source);

		db_order[] orders = new db_order[] { new db_order(field, db_order.DESC, true), new db_order(where.toString(), db_order.ASC, false) };

		String query = "SELECT " + db.get_variable(source, col) + " FROM " + table;
		query += " WHERE " + db_where.to_string(wheres);
		query += " ORDER BY " + db_order.to_string(orders);

		args = new ArrayList<Object>();
		args.add(query);

		target = val;

		ArrayList<HashMap<String, String>> target2 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> target22 = new HashMap<String, String>();
		target22.put(col, strings.to_string(target));		
		target2.add(target22);

		target = target2;

		is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, target);
		outputs.put(name, is_ok);

		name = "delete";

		args = new ArrayList<Object>();
		args.add(source);
		args.add(wheres);

		target = null;

		is_ok = run_method(class0, name, new Class<?>[] { String.class, db_where[].class }, args, target);
		outputs.put(name, is_ok);

		val = numbers.get_random_int(-1 * max, max);
		vals.put(FIELD_INT, val);

		//insert_update (insert).
		outputs = run_db_insert_update(class0, source, wheres, vals, outputs);

		outputs = run_db_select_int(class0, source, wheres, val, outputs);

		val = numbers.get_random_int(-1 * max, max);
		vals.put(FIELD_INT, val);

		//insert_update (update).
		outputs = run_db_insert_update(class0, source, wheres, vals, outputs);

		outputs = run_db_select_int(class0, source, wheres, val, outputs);

		update_screen(name0, false, 1);

		return outputs;	
	}
	
	public static boolean create_table(String source_)
	{
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(source_);
		args.add(true);

		return run_method(db.class, "create_table", new Class<?>[] { String.class, boolean.class }, args, null);		
	}
	
	private static HashMap<String, Boolean> run_db_select_int(Class<?> class0_, String source_, db_where[] wheres_, int target_, HashMap<String, Boolean> outputs_)
	{	
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>(outputs_);

		String name = "select_one_int";

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(source_);
		args.add(FIELD_INT);
		args.add(wheres_);
		args.add(null);

		boolean is_ok = run_method(class0_, name, new Class<?>[] { String.class, String.class, db_where[].class, db_order[].class }, args, target_);
		outputs.put(name, is_ok);

		return outputs;
	}

	private static HashMap<String, Boolean> run_db_insert_update(Class<?> class0_, String source_, db_where[] wheres_, HashMap<String, Object> vals_, HashMap<String, Boolean> outputs_)
	{	
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>(outputs_);

		String name = "insert_update";

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(source_);
		args.add(vals_);
		args.add(wheres_);

		boolean is_ok = run_method(class0_, name, new Class<?>[] { String.class, HashMap.class, db_where[].class }, args, null);
		outputs.put(name, is_ok);

		return outputs;
	}

	public static HashMap<String, Boolean> run_crypto()
	{		
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Class<?> class0 = crypto.class;
		String name0 = class0.getName();		

		update_screen(name0, true, 1);

		String name = "encrypt";

		String[] input = (String[])arrays.get_random(String[].class);
		String id = null;

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(input);
		args.add(id);

		Object target = null;

		boolean is_ok = run_method(class0, name, new Class<?>[] { String[].class, String.class }, args, target);

		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		String[] encrypted = (String[])_temp_output;

		name = "decrypt";

		args = new ArrayList<Object>();
		args.add(encrypted);
		args.add(id);

		target = input;

		is_ok = run_method(class0, name, new Class<?>[] { String[].class, String.class }, args, input);
		outputs.put(name, is_ok);

		update_screen(name0, false, 1);

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

		return run(type, args_all, targets, skip);
	}

	public static HashMap<String, Boolean> run_arrays() { return run(arrays.class); }

	public static HashMap<String, Boolean> run_dates() 
	{ 
		String[] skip = new String[] { "get_diff", "from_string", "update", "update_timestamp" };

		return run(dates.class, skip); 
	}

	public static HashMap<String, Boolean> run_generic()
	{
		String[] skip = new String[] { "get_method", "call_static_method" };

		return run(generic.class, skip);
	}

	public static HashMap<String, Boolean> run_io()
	{
		String[] skip = new String[] { "array_to_file", "line_to_file", "object_to_file", "bytes_to_file", "ini_to_file" };

		return run(io.class, skip);
	}

	public static HashMap<String, Boolean> run_numbers() { return run(numbers.class); }

	public static HashMap<String, Boolean> run_paths()
	{
		String[] skip = new String[] { "update_main_dir" };

		return run(paths.class, skip);
	}

	public static HashMap<String, Boolean> run_types()
	{
		String[] skip = new String[] { "check_multiple" };

		return run(types.class, skip);
	}

	public static HashMap<String, Boolean> run_credentials()
	{
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Class<?> class0 = credentials.class;
		String name0 = class0.getName();
		update_screen(name0, true, 1);

		String id = get_class_id();
		String user = credentials.DEFAULT_USER;
		String username = strings.get_random(strings.SIZE_SMALL);
		String password = strings.get_random(strings.SIZE_SMALL);

		for (boolean is_file: new boolean[] { true, false })
		{
			if (!is_file && !_test_db) continue;

			outputs = run_credentials_internal(class0, outputs, id, user, username, password, is_file);			
		}

		update_screen(name0, false, 1);

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

		boolean is_ok = run_method(class_, name, new Class<?>[] { String.class, String.class, String.class, String.class }, args, target);

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

		is_ok = run_method(class_, name, new Class<?>[] { String.class, String.class, boolean.class }, args, target);
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