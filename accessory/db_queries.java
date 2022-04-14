package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

abstract class db_queries 
{
	static { _ini.load(); }
	
	public static HashMap<String, String> select_one(String source_, String[] fields_, db_where[] wheres_, db_order[] orders_)
	{
		ArrayList<HashMap<String, String>> temp = select(source_, fields_, wheres_, 1, orders_);
		
		return (arrays.is_ok(temp) ? temp.get(0) : null);
	}
	
	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, db_where[] wheres_, int max_rows_, db_order[] orders_)
	{	
		return select(source_, fields_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_));
	}
	
	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, String where_cols_, int max_rows_, String order_cols_)
	{	
		return select_internal(source_, get_cols(source_, fields_), where_cols_, max_rows_, order_cols_);
	}
	
	public static <x> void insert(String source_, HashMap<String, x> vals_raw_)
	{
		HashMap<String, String> vals = db.check_vals_error(source_, vals_raw_);
		if (!db._is_ok) return;
		
		insert_internal(source_, vals);
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_) { update(source_, vals_raw_, db_where.to_string(wheres_)); }
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_)
	{
		HashMap<String, String> vals = db.check_vals_error(source_, vals_raw_);
		if (!db._is_ok) return;
		
		update_internal(source_, vals, where_cols_);
	}
	
	public static void delete(String source_, db_where[] wheres_) { delete(source_, db_where.to_string(wheres_)); }
	
	public static void delete(String source_, String where_cols_) { delete_internal(source_, where_cols_); }

	public static boolean table_exists(String source_) { return table_exists_internal(source_); }
	
	public static void create_table(String source_, boolean drop_it_) { create_table(source_, db.get_source_fields(source_), drop_it_); }
	
	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;

		boolean exists = table_exists_internal(source);
		if (drop_it_) 
		{
			if (exists) drop_table_internal(source);
		}
		else if (exists) return;
		
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

		create_table_internal(source, cols);
	}
	
	public static void drop_table(String source_) { drop_table_internal(source_); }
	
	public static void truncate_table(String source_) { truncate_table_internal(source_); }

	public static ArrayList<HashMap<String, String>> execute_query(String source_, String query_) { return db.get_valid_instance(source_).execute_query(source_, query_); }
	
	static Object select_one_common(String source_, String field_, db_where[] wheres_, db_order[] orders_, String what_)
	{
		Object output = null;
		
		if (data.is_string(what_)) output = strings.DEFAULT;
		else if (what_.equals(data.DECIMAL)) output = numbers.DEFAULT_DECIMAL;
		else if (what_.equals(data.LONG)) output = numbers.DEFAULT_LONG;
		else if (what_.equals(data.INT)) output = numbers.DEFAULT_INT;
		
		if (!strings.is_ok(field_)) 
		{
			db.update_is_ok(source_, false);
			
			return output;
		}
		
		HashMap<String, String> temp = select_one(source_, new String[] { field_ }, wheres_, orders_);
		
		if (arrays.is_ok(temp))
		{
			String temp2 = temp.get(field_);
			
			if (data.is_string(what_)) output = temp2;
			else if (what_.equals(data.DECIMAL)) output = numbers.decimal_from_string(temp2);
			else if (what_.equals(data.LONG)) output = numbers.long_from_string(temp2);
			else if (what_.equals(data.INT)) output = numbers.int_from_string(temp2);	
		}
		
		return output;
	}
	
	private static ArrayList<HashMap<String, String>> select_internal(String source_, String[] cols_, String where_, int max_rows_, String order_)
	{
		return adapt_outputs(source_, execute_type(source_, db.SELECT, cols_, null, where_, max_rows_, order_, null));
	}
	
	private static void insert_internal(String source_, HashMap<String, String> vals_) { execute_type(source_, db.INSERT, null, vals_, null, 0, null, null); }
	
	private static void update_internal(String source_, HashMap<String, String> vals_, String where_) { execute_type(source_, db.UPDATE, null, vals_, where_, 0, null, null); }

	private static void delete_internal(String source_, String where_) { execute_type(source_, db.DELETE, null, null, where_, 0, null, null); }
	
	private static boolean table_exists_internal(String source_) { return arrays.is_ok(execute_type(source_, db.TABLE_EXISTS, null, null, null, 0, null, null)); } 
	
	private static void create_table_internal(String source_, HashMap<String, db_field> cols_) { execute_type(source_, db.TABLE_CREATE, null, null, null, 0, null, cols_); } 
	
	private static void drop_table_internal(String source_) { execute_type(source_, db.TABLE_DROP, null, null, null, 0, null, null); } 
	
	private static void truncate_table_internal(String source_) { execute_type(source_, db.TABLE_TRUNCATE, null, null, null, 0, null, null); }
	
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
		
		for (int i = 0; i < size; i++)
		{
			cols[i] = db.get_col(source, fields_[i]);
		}
		
		return cols;
	}
	
	private static ArrayList<HashMap<String, String>> adapt_outputs(String source_, ArrayList<HashMap<String, String>> outputs_)
	{
		ArrayList<HashMap<String, String>> outputs = new ArrayList<HashMap<String, String>>();
		
		String source = db.check_source(source_);
		if (!strings.is_ok(source)) return outputs;
		
		for (HashMap<String, String> item: outputs_)
		{
			HashMap<String, String> output = new HashMap<String, String>();
			
			for (Entry<String, String> item2: item.entrySet())
			{
				String key = col_to_field(source_, item2.getKey());
				if (!strings.is_ok(key)) continue;

				output.put(key, item2.getValue());
			}
			
			outputs.add(output);
		}

		return outputs;
	}
	
	private static String col_to_field(String source_, String col_)
	{
		String source = db.check_source(source_);
		if (!strings.is_ok(source) || !strings.is_ok(col_)) return strings.DEFAULT;
		
		HashMap<String, db_field> fields = db.get_source_fields(source);
		if (!arrays.is_ok(fields)) return strings.DEFAULT;
		
		for (Entry<String, db_field> field: fields.entrySet())
		{
			String key = field.getKey();
			String col = db.get_col(source_, key);
			if (strings.are_equal(col_, col)) return key;
		}
		
		return strings.DEFAULT;
	}
}