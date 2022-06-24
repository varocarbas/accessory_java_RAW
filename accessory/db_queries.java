package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

abstract class db_queries extends parent_static
{	
	public static HashMap<String, String> select_one(String source_, String[] fields_, db_where[] wheres_, db_order[] orders_) { return select_one(source_, fields_, db_where.to_string(wheres_), db_order.to_string(orders_)); }

	public static HashMap<String, String> select_one(String source_, String[] fields_, String wheres_cols_, String orders_cols_)
	{
		ArrayList<HashMap<String, String>> temp = select(source_, fields_, wheres_cols_, 1, orders_cols_);

		return (arrays.is_ok(temp) ? temp.get(0) : null);
	}

	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select(source_, fields_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, String where_cols_, int max_rows_, String order_cols_) { return select_internal(source_, get_cols(source_, fields_), where_cols_, max_rows_, order_cols_); }

	public static int select_count(String source_) 
	{ 
		ArrayList<HashMap<String, String>> output = select_count_internal(source_);

		return (arrays.is_ok(output) ? strings.to_number_int((String)arrays.get_value(output.get(0), db.get_select_count_col(source_))) : 0); 
	}

	public static <x> void insert(String source_, HashMap<String, x> vals_raw_)
	{
		HashMap<String, String> vals = db.check_vals_error(source_, vals_raw_);
		if (!arrays.is_ok(vals)) return;

		insert_internal(source_, vals);
	}

	public static void insert_quick(String source_, HashMap<String, String> vals_) { insert_internal(source_, vals_); }

	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_) { update(source_, vals_raw_, db_where.to_string(wheres_)); }

	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_)
	{
		HashMap<String, String> vals = db.check_vals_error(source_, vals_raw_);
		if (!arrays.is_ok(vals)) return;

		update_internal(source_, vals, where_cols_);
	}

	public static void update_quick(String source_, HashMap<String, String> vals_, String where_cols_) { update_internal(source_, vals_, where_cols_); }

	public static void delete(String source_, db_where[] wheres_) { delete(source_, db_where.to_string(wheres_)); }

	public static void delete(String source_, String where_cols_) { delete_internal(source_, where_cols_); }

	public static boolean table_exists(String source_) { return table_exists_internal(source_); }

	public static void create_table(String source_, boolean drop_it_) { create_table(source_, db.get_source_fields(source_), drop_it_); }

	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_)
	{
		String source = db.check_source_error(source_);
		if (!strings.is_ok(source)) return;

		boolean exists = table_exists_internal(source);
		if (exists)
		{
			if (drop_it_) drop_table_internal(source);
			else return;		
		}

		create_table_internal(source, get_cols(source_, fields_));
	}
	
	public static void __create_table_like(String table_name_, String source_like_, boolean drop_it_) 
	{ 
		String source = db.check_source_error(source_like_);
		if (!strings.is_ok(source) || !strings.is_ok(table_name_)) return;
		
		__lock();
		
		String cur_source = db._cur_source;
		db._cur_source = source;
		
		parent_db instance = db.get_valid_instance(source);
		
		create_table_like_internal(table_name_, source, instance, drop_it_); 
		
		db._cur_source = cur_source;
		
		__unlock();
	}
	public static void __backup_table(String source_, String backup_name_) 
	{ 
		String source = db.check_source_error(source_);
		if (!strings.is_ok(source)) return;
		
		String table_source = db.get_table(source);
		String table_backup = (strings.is_ok(backup_name_) ? backup_name_ : (table_source + "_backup"));
		
		__lock();
		
		String cur_source = db._cur_source;
		db._cur_source = source;
		
		parent_db instance = db.get_valid_instance(source);
		
		create_table_like_internal(table_backup, table_source, instance, true); 

		instance.backup_table(table_source, table_backup);
		
		db._cur_source = cur_source;
		
		__unlock();
	}

	public static void drop_table(String source_) { drop_table_internal(source_); }

	public static void truncate_table(String source_) { truncate_table_internal(source_); }
	
	public static ArrayList<HashMap<String, String>> execute_query(String source_, String query_) { return db.get_valid_instance(source_).execute_query(source_, query_); }

	static Object select_some_common(String source_, String field_, String wheres_cols_, int max_rows_, String orders_cols_, String what_) { return select_one_some_common(source_, field_, wheres_cols_, max_rows_, orders_cols_, what_, false); }

	static Object select_one_common(String source_, String field_, String wheres_cols_, String orders_cols_, String what_) { return select_one_some_common(source_, field_, wheres_cols_, 1, orders_cols_, what_, true); }

	private static void create_table_like_internal(String table_name_, String source_, parent_db instance_, boolean drop_it_) 
	{
		if (instance_.table_exists(table_name_))
		{
			if (drop_it_) instance_.drop_table(table_name_);
			else return;
		}
		
		if (db.table_exists(source_)) instance_.create_table_like(table_name_, db.get_table(source_));
		else instance_.create_table(table_name_, get_cols(source_, db.get_source_fields(source_)));		
	}

	private static HashMap<String, db_field> get_cols(String source_, HashMap<String, db_field> fields_)
	{
		HashMap<String, db_field> cols = new HashMap<String, db_field>();

		if (arrays.is_ok(fields_))
		{
			for (Entry<String, db_field> item: fields_.entrySet())
			{
				String field = item.getKey();
				String col = db.get_col(source_, field);
				if (!strings.is_ok(col)) continue;

				cols.put(col, new db_field(item.getValue()));
			}
		}

		return cols;
	}
	
	@SuppressWarnings("unchecked")
	private static Object select_one_some_common(String source_, String field_, String wheres_cols_, int max_rows_, String orders_cols_, String what_, boolean is_one_)
	{
		Object output = null;

		if (is_one_)
		{
			if (data.is_string(what_)) output = strings.DEFAULT;
			else if (what_.equals(data.DECIMAL)) output = numbers.DEFAULT_DECIMAL;
			else if (what_.equals(data.LONG)) output = numbers.DEFAULT_LONG;
			else if (what_.equals(data.INT) || what_.equals(data.TINYINT)) output = numbers.DEFAULT_INT;
			else if (data.is_boolean(what_)) output = _defaults.BOOLEAN;		
		}

		if (!strings.is_ok(field_)) 
		{
			db.is_ok(source_, false);

			return output;
		}

		Object temp = null; 
		
		if (is_one_) temp = select_one(source_, new String[] { field_ }, wheres_cols_, orders_cols_);		
		else temp = select(source_, new String[] { field_ }, wheres_cols_, max_rows_, orders_cols_);
		
		if (!arrays.is_ok(temp)) return output;
		
		if (is_one_) 
		{
			String val = ((HashMap<String, String>)temp).get(field_);
			
			if (data.is_string(what_)) output = val;
			else output = select_one_some_common_output(val, what_);
		}
		else
		{
			if (data.is_string(what_))
			{
				ArrayList<HashMap<String, String>> temp2 = (ArrayList<HashMap<String, String>>)temp;
				
				ArrayList<String> output2 = new ArrayList<String>();
				for (HashMap<String, String> item: temp2) { output2.add(item.get(field_)); }
				
				output = output2;
			}
			else
			{
				output = select_one_some_common_array(null, what_, null);
				
				ArrayList<HashMap<String, String>> temp2 = (ArrayList<HashMap<String, String>>)temp;
					
				for (HashMap<String, String> item: temp2) 
				{ 
					Object val = select_one_some_common_output(item.get(field_), what_);	
					output = select_one_some_common_array(val, what_, output); 
				}				
			}
		}
		
		return output;
	}

	@SuppressWarnings("unchecked")
	private static Object select_one_some_common_array(Object val_, String what_, Object output)
	{
		if (what_.equals(data.DECIMAL))
		{
			if (output == null) output = new ArrayList<Double>();
			else ((ArrayList<Double>)output).add((double)val_);
		}
		else if (what_.equals(data.LONG))
		{
			if (output == null) output = new ArrayList<Long>();
			else ((ArrayList<Long>)output).add((long)val_);
		}
		else if (what_.equals(data.INT) || what_.equals(data.TINYINT))
		{
			if (output == null) output = new ArrayList<Integer>();
			else ((ArrayList<Integer>)output).add((int)val_);
		}
		else if (data.is_boolean(what_))
		{
			if (output == null) output = new ArrayList<Boolean>();
			else ((ArrayList<Boolean>)output).add((boolean)val_);	
		}
		else
		{
			if (output == null) output = new ArrayList<String>();
			else ((ArrayList<String>)output).add((String)val_);	
		}
		
		return output;
	}
	
	private static Object select_one_some_common_output(String input_, String what_) { return db.output_to_object(input_, what_); }
	
	private static ArrayList<HashMap<String, String>> select_internal(String source_, String[] cols_, String where_, int max_rows_, String order_) { return adapt_outputs(source_, execute_type(source_, db.QUERY_SELECT, cols_, null, where_, max_rows_, order_, null)); }

	private static ArrayList<HashMap<String, String>> select_count_internal(String source_) { return execute_type(source_, db.QUERY_SELECT_COUNT, null, null, null, 0, null, null); }

	private static void insert_internal(String source_, HashMap<String, String> vals_) { execute_type(source_, db.QUERY_INSERT, null, vals_, null, 0, null, null); }

	private static void update_internal(String source_, HashMap<String, String> vals_, String where_) { execute_type(source_, db.QUERY_UPDATE, null, vals_, where_, 0, null, null); }

	private static void delete_internal(String source_, String where_) { execute_type(source_, db.QUERY_DELETE, null, null, where_, 0, null, null); }

	private static boolean table_exists_internal(String source_) { return arrays.is_ok(execute_type(source_, db.QUERY_TABLE_EXISTS, null, null, null, 0, null, null)); } 

	private static void create_table_internal(String source_, HashMap<String, db_field> cols_) { execute_type(source_, db.QUERY_TABLE_CREATE, null, null, null, 0, null, cols_); } 

	private static void drop_table_internal(String source_) { execute_type(source_, db.QUERY_TABLE_DROP, null, null, null, 0, null, null); } 

	private static void truncate_table_internal(String source_) { execute_type(source_, db.QUERY_TABLE_TRUNCATE, null, null, null, 0, null, null); }

	private static ArrayList<HashMap<String, String>> execute_type(String source_, String what_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		String source = db.check_source_error(source_);
		if (!strings.is_ok(source)) return null;

		return db.get_valid_instance(source).execute(source, what_, cols_, vals_, where_, max_rows_, order_, cols_info_);
	}

	private static String[] get_cols(String source_, String[] fields_)
	{
		String source = db.check_source(source_);

		int size = arrays.get_size(fields_);
		if (!strings.is_ok(source) || size < 1) return null;

		String[] cols = new String[size];
		for (int i = 0; i < size; i++) { cols[i] = db.get_col(source, fields_[i]); }

		return cols;
	}

	private static ArrayList<HashMap<String, String>> adapt_outputs(String source_, ArrayList<HashMap<String, String>> outputs_)
	{
		ArrayList<HashMap<String, String>> outputs = new ArrayList<HashMap<String, String>>();

		String source = db.check_source(source_);

		HashMap<String, db_field> fields = db.get_source_fields(source);
		if (!arrays.is_ok(fields) || !arrays.is_ok(outputs_)) return outputs;

		HashMap<String, String> col_fields = new HashMap<String, String>();
		boolean first_time = true;

		for (HashMap<String, String> item: outputs_)
		{
			HashMap<String, String> output = new HashMap<String, String>();

			for (Entry<String, String> item2: item.entrySet())
			{
				String col = item2.getKey();
				String field = null;

				if (first_time) 
				{
					field = col_to_field(source, col, fields);
					if (!strings.is_ok(field)) field = col;

					col_fields.put(col, field);
				}
				else field = col_fields.get(col);

				output.put(field, item2.getValue());
			}
			
			first_time = false;
			
			outputs.add(output);
		}

		return outputs;
	}

	private static String col_to_field(String source_, String col_, HashMap<String, db_field> fields_)
	{		
		for (Entry<String, db_field> field: fields_.entrySet())
		{
			String key = field.getKey();
			if (strings.are_equal(col_, db.get_col(source_, key))) return key;
		}

		return strings.DEFAULT;
	}
}