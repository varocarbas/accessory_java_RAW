package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class tests extends parent_tests 
{
	public static final String _ID = "tests";
	
	private static tests _instance = new tests();

	public tests() { }

	public static boolean report_all_errors() { return _instance._report_all_errors; }

	public static void report_all_errors(boolean report_all_errors_) { _instance._report_all_errors = report_all_errors_; }

	public static boolean test_db() { return _instance._test_db; }

	public static void test_db(boolean test_db_) { _instance._test_db = test_db_; }

	public static boolean drop_db_tables() { return _instance._drop_db_tables; }

	public static void drop_db_tables(boolean drop_db_tables_) { _instance._drop_db_tables = drop_db_tables_; }

	public static HashMap<String, HashMap<String, Boolean>> run_all() { return _instance.run_all_internal(); }

	public HashMap<String, HashMap<String, Boolean>> run_all_internal()
	{	
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		outputs = run_db(outputs);
		outputs = run_main(outputs);

		check_wrongs(outputs);

		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_db(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);
		if (!_instance._test_db) return outputs;

		String name = "accessory_db";
		int level = 0;

		update_screen(name, true, level);

		outputs.put(name, run_internal(db.class));

		update_screen(name, false, level);

		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_main(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

		String name = "accessory_main";
		int level = 0;

		update_screen(name, true, level);		

		Class<?>[] classes = new Class<?>[] 
		{ 
			crypto.class, strings.class, arrays.class, dates.class, generic.class,
			io.class, numbers.class, paths.class, _types.class, credentials.class
		}; 

		for (Class<?> type: classes) { outputs.put(type.getName(), run_internal(type)); }

		update_screen(name, false, level);

		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Boolean> run_db()
	{			
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Object target = null;
		ArrayList<Object> args = null;
		boolean is_ok = false;

		Class<?> class0 = db.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);

		String source = db_tests.SOURCE;

		String name = "create_table";

		String[] sources = new String[] { source }; //, db_credentials.SOURCE, db_info.SOURCE, db_crypto.SOURCE };

		for (String source2: sources)
		{
			is_ok = _instance.create_table(source2);

			outputs.put(name, is_ok);
			if (!is_ok) return outputs;			
		}

		db._cur_source = source;

		name = "__create_table_like";
		
		String table_name = "tests2";

		args = new ArrayList<Object>();
		args.add(table_name);
		args.add(source);
		args.add(true);

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, String.class, boolean.class }, args, target);
		outputs.put(name, is_ok);

		name = "__backup_table";

		table_name = null; 
		
		args = new ArrayList<Object>();
		args.add(source);
		args.add(table_name);

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, String.class }, args, target);
		outputs.put(name, is_ok);
		
		db_common.is_quick(source, false);
		
		name = "insert";

		HashMap<String, Object> vals = (HashMap<String, Object>)get_vals(source);

		args = new ArrayList<Object>();
		args.add(source);
		args.add(vals);

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, HashMap.class }, args, target);
		outputs.put(name, is_ok);

		if (!is_ok) return outputs;

		db_common.is_quick(source, true);
		
		name = "insert_quick";

		HashMap<String, String> vals_quick = (HashMap<String, String>)get_vals(source);

		args = new ArrayList<Object>();
		args.add(source);
		args.add(vals_quick);

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, HashMap.class }, args, target);
		outputs.put(name, is_ok);

		db_common.is_quick(source, false);
		
		name = "update";

		int val = (int)vals.get(db_tests.INT) + 20;

		vals.put(db_tests.INT, val);
		
		db_where where = new db_where(db_tests.STRING, vals.get(db_tests.STRING));
		db_where[] wheres = new db_where[] { where };

		db_where where_quick = new db_where(source, db.get_col(source, db_tests.STRING), db_where.OPERAND_EQUAL, vals.get(db_tests.STRING), db_where.DEFAULT_IS_LITERAL, db_where.DEFAULT_LINK, true);
		db_where[] wheres_quick = new db_where[] { where_quick };

		args = new ArrayList<Object>();
		args.add(source);
		args.add(vals);
		args.add(wheres);

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, HashMap.class, db_where[].class }, args, target);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		db_common.is_quick(source, true);
		
		name = "update_quick";

		HashMap<String, String> temp = get_val_quick(source, db_tests.INT, val, vals_quick);
		
		String col = db.get_col(source, db_tests.INT);
		
		vals_quick = new HashMap<String, String>();
		vals_quick.put(col, temp.get(col));
		
		args = new ArrayList<Object>();
		args.add(source);
		args.add(vals_quick);
		args.add(db_where.to_string(wheres_quick));

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, HashMap.class, String.class }, args, target);
		outputs.put(name, is_ok);
		if (!is_ok) return outputs;
		
		outputs = run_db_select_ints(class0, source, wheres, wheres_quick, val, outputs, false);

		outputs = run_db_selects(class0, source, wheres, wheres_quick, outputs);
		
		name = "execute_query";

		String field = db_tests.INT;
		col = db.get_col(source, field);

		String table = db.get_variable_table(source);

		db_order[] orders = new db_order[] { new db_order(field, db_order.ORDER_DESC, true), new db_order(where.toString(), db_order.ORDER_ASC, false) };

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

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class }, args, target);
		outputs.put(name, is_ok);

		name = "delete";

		args = new ArrayList<Object>();
		args.add(source);
		args.add(wheres);

		target = null;

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class, db_where[].class }, args, target);
		outputs.put(name, is_ok);

		val -= 5;
		vals.put(db_tests.INT, val);

		//insert_update (insert).
		outputs = run_db_insert_update(class0, source, wheres, vals, outputs);

		outputs = run_db_select_ints(class0, source, wheres, wheres_quick, val, outputs, false);

		val += 15;
		vals.put(db_tests.INT, val);

		//insert_update (update).
		outputs = run_db_insert_update(class0, source, wheres, vals, outputs);

		outputs = run_db_select_ints(class0, source, wheres, wheres_quick, val, outputs, false);

		update_screen(name0, false, 1);

		return outputs;	
	}
	
	private static Object get_vals(String source_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		HashMap<String, String> vals_quick = new HashMap<String, String>();

		boolean is_quick = db_common.is_quick(source_);

		int max = 123456;
		double max2 = 123456789.123;
		
		String[] fields = new String[] { db_tests.INT, db_tests.DECIMAL, db_tests.STRING, db_tests.BOOLEAN };
		
		for (String field: fields)
		{
			Object val = null;
			
			if (field.equals(db_tests.INT)) val = numbers.get_random_int(-1 * max, max);
			else if (field.equals(db_tests.DECIMAL)) val = numbers.get_random_decimal(-1 * max2, max2);
			else if (field.equals(db_tests.STRING)) val = strings.get_random(strings.SIZE_SMALL);
			else if (field.equals(db_tests.BOOLEAN)) val = generic.get_random_boolean();
			
			if (is_quick) vals_quick = get_val_quick(source_, field, val, vals_quick);
			else vals.put(field, val);
		}
		
		return (is_quick ? vals_quick : vals);
	}

	private static HashMap<String, String> get_val_quick(String source_, String field_, Object val_, HashMap<String, String> all_)
	{
		HashMap<String, String> all = new HashMap<String, String>(all_);
		
		all.put(db.get_col(source_, field_), db.adapt_input(source_, field_, val_));
		
		return all;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, Boolean> run_db_selects(Class<?> class0_, String source_, db_where[] wheres_, db_where[] wheres_quick_, HashMap<String, Boolean> outputs_)
	{	
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>(outputs_);		
		
		int max_rows = 0;
		String order = null;
		
		ArrayList<Object> args0 = new ArrayList<Object>();
		args0.add(source_);
		args0.add(null);
		args0.add(null);
		args0.add(max_rows);
		args0.add(order);
		
		String[] names = new String[] { "select", "select_quick" };
		
		Object target = null;		
		
		String field = db_tests.INT;
		String col = db.get_col(source_, field);
		
		for (String name: names)
		{
			String[] fields_cols = null;
			db_where[] wheres = null;
		
			if (name.equals("select"))
			{
				fields_cols = new String[] { field };
				wheres = (db_where[])arrays.get_new(wheres_);
			}
			else
			{
				fields_cols = new String[] { col };
				wheres = (db_where[])arrays.get_new(wheres_quick_);	
			}
			
			ArrayList<Object> args = new ArrayList<Object>(args0);
			args.set(1, fields_cols);
			args.set(2, db_where.to_string(wheres));

			boolean is_ok = _instance.run_method(class0_, name, new Class<?>[] { String.class, String[].class, String.class, int.class, String.class }, args, target);
			outputs.put(name, is_ok);
			
			if (!is_ok) break;

			if (target == null) 
			{
				if (arrays.get_size(_instance._temp_output) > 0)
				{
					HashMap<String, String> temp = ((ArrayList<HashMap<String, String>>)_instance._temp_output).get(0);
					
					HashMap<String, String> temp2 = new HashMap<String, String>();
					temp2.put(col, temp.get(field));
					
					ArrayList<HashMap<String, String>> temp3 = new ArrayList<HashMap<String, String>>();
					temp3.add(temp2);

					target = temp3;
				}	
			}
			else outputs.put(name, arrays.are_equal(target, _instance._temp_output));
		}

		return outputs;
	}

	private static HashMap<String, Boolean> run_db_select_ints(Class<?> class0_, String source_, db_where[] wheres_, db_where[] wheres_quick_, int target_, HashMap<String, Boolean> outputs_, boolean just_one_)
	{	
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>(outputs_);		
		
		ArrayList<Object> args0 = new ArrayList<Object>();
		args0.add(source_);
		args0.add(null);
		args0.add(null);
		args0.add(null);

		String[] names = new String[] { "select_one_int", "select_one_int_quick" };
		
		String field = db_tests.INT;
		
		for (String name: names)
		{
			String field_col = null;
			db_where[] wheres = null;
		
			if (name.equals("select_one_int"))
			{
				field_col = field;
				wheres = (db_where[])arrays.get_new(wheres_);
			}
			else
			{
				field_col = db.get_col(source_, field);
				wheres = (db_where[])arrays.get_new(wheres_quick_);	
			}
			
			ArrayList<Object> args = new ArrayList<Object>(args0);
			args.set(1, field_col);
			args.set(2, db_where.to_string(wheres));

			boolean is_ok = _instance.run_method(class0_, name, new Class<?>[] { String.class, String.class, String.class, String.class }, args, target_);
			outputs.put(name, is_ok);
			
			if (!is_ok || just_one_) break;
		}

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

		boolean is_ok = _instance.run_method(class0_, name, new Class<?>[] { String.class, HashMap.class, db_where[].class }, args, null);
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

		boolean is_ok = _instance.run_method(class0, name, new Class<?>[] { String[].class, String.class }, args, target);

		outputs.put(name, is_ok);
		if (!is_ok) return outputs;

		String[] encrypted = (String[])_instance._temp_output;

		name = "decrypt";

		args = new ArrayList<Object>();
		args.add(encrypted);
		args.add(id);

		target = input;

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String[].class, String.class }, args, input);
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

		return _instance.run(type, args_all, targets, skip);
	}

	public static HashMap<String, Boolean> run_arrays() { return _instance.run(arrays.class); }

	public static HashMap<String, Boolean> run_dates() 
	{ 
		String[] skip = new String[] { "get_diff", "from_string", "time_from_string", "date_from_string", "update", "update_timestamp" };

		return _instance.run(dates.class, skip); 
	}
	
	public static HashMap<String, Boolean> run_generic()
	{
		String[] skip = new String[] { "get_method", "call_static_method" };

		return _instance.run(generic.class, skip);
	}

	public static HashMap<String, Boolean> run_io()
	{
		String[] skip = new String[] { "array_to_file", "line_to_file", "object_to_file", "bytes_to_file", "ini_to_file", "hashmap_to_file", "empty_file", "web_to_array", "delete_file" };

		return _instance.run(io.class, skip);
	}

	public static HashMap<String, Boolean> run_numbers() { return _instance.run(numbers.class); }

	public static HashMap<String, Boolean> run_paths()
	{
		String[] skip = new String[] { "update_main_dir" };

		return _instance.run(paths.class, skip);
	}

	public static HashMap<String, Boolean> run_types()
	{
		String[] skip = new String[] { "check_multiple" };

		return _instance.run(_types.class, skip);
	}

	public static HashMap<String, Boolean> run_credentials()
	{
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Class<?> class0 = credentials.class;
		String name0 = class0.getName();
		update_screen(name0, true, 1);

		String id = _ID;
		String user = credentials.DEFAULT_USER;
		String username = strings.get_random(strings.SIZE_SMALL);
		String password = strings.get_random(strings.SIZE_SMALL);

		for (boolean is_file: new boolean[] { true, false })
		{
			if (!is_file && !_instance._test_db) continue;

			outputs = run_credentials_internal(class0, outputs, id, user, username, password, is_file);			
		}

		update_screen(name0, false, 1);

		return outputs;
	}

	private static HashMap<String, Boolean> run_credentials_internal(Class<?> class_, HashMap<String, Boolean> outputs_, String id_, String user_, String username_, String password_, boolean is_file_)
	{
		HashMap<String, Boolean> outputs = arrays.get_new_hashmap_xy(outputs_);

		String name = "encrypt_username_password_" + (is_file_ ? "file" : "db");

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(id_);
		args.add(user_);
		args.add(username_);
		args.add(password_);

		Object target = null;

		boolean is_ok = _instance.run_method(class_, name, new Class<?>[] { String.class, String.class, String.class, String.class }, args, target);

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

		is_ok = _instance.run_method(class_, name, new Class<?>[] { String.class, String.class, boolean.class }, args, target);
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
		else if (class_.equals(_types.class)) output = run_types();
		else if (class_.equals(credentials.class)) output = run_credentials();
		else if (class_.equals(db.class)) output = run_db();

		return output;
	}
}