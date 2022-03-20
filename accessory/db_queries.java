package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

abstract class db_queries 
{
	static { ini.load(); }
	
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
		String source = db.check_source_error(source_);
		if (!db._is_ok) return null;
		
		return select_internal(db.get_table(source), get_cols(source, fields_), where_cols_, max_rows_, order_cols_);
	}
	
	public static <x> void insert(String source_, HashMap<String, x> vals_raw_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;
		
		HashMap<String, String> vals = db.check_vals_error(source, vals_raw_);
		if (!db._is_ok) return;
		
		insert_internal(db.get_table(source), vals);
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_)
	{
		update(source_, vals_raw_, db_where.to_string(wheres_));
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;
		
		HashMap<String, String> vals = db.check_vals_error(source, vals_raw_);
		if (!db._is_ok) return;
		
		update_internal(db.get_table(source), vals, where_cols_);
	}
	
	public static void delete(String source_, db_where[] wheres_)
	{
		delete(source_, db_where.to_string(wheres_));
	}
	
	public static void delete(String source_, String where_cols_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;
		
		delete_internal(db.get_table(source), where_cols_);
	}

	public static boolean table_exists(String source_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return false;
		
		return table_exists_internal(db.get_table(source));
	}
	
	public static void create_table(String source_, boolean drop_it_)
	{
		create_table(source_, db.get_source_fields(source_), drop_it_);
	}
	
	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;

		String table = db.get_table(source);
		boolean exists = table_exists_internal(table);
		if (drop_it_) 
		{
			if (exists) drop_table_internal(table);
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

		create_table_internal(table, cols);
	}
	
	public static void drop_table(String source_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;
		
		String table = db.get_table(source);
		if (!table_exists(table)) return;
		
		drop_table_internal(table);		
	}
	
	public static void truncate_table(String source_)
	{
		String source = db.check_source_error(source_);
		if (!db._is_ok) return;
		
		truncate_table_internal(db.get_table(source));
	}

	public static ArrayList<HashMap<String, String>> execute_query(String query_)
	{
		db._is_ok = false;
		
		ArrayList<HashMap<String, String>> output = null;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = db_mysql.execute_query(query_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output;
	}
	
	static Object select_one_common(String source_, String field_, db_where[] wheres_, db_order[] orders_, String what_)
	{
		Object output = null;
		
		if (data.is_string(what_)) output = strings.DEFAULT;
		else if (what_.equals(types.DATA_DECIMAL)) output = numbers.DEFAULT_DEC;
		else if (what_.equals(types.DATA_LONG)) output = numbers.DEFAULT_LONG;
		else if (what_.equals(types.DATA_INT)) output = numbers.DEFAULT_INT;
		
		if (!strings.is_ok(field_)) 
		{
			db._is_ok = false;
			
			return output;
		}
		
		HashMap<String, String> temp = select_one(source_, new String[] { field_ }, wheres_, orders_);
		
		if (arrays.is_ok(temp))
		{
			String temp2 = temp.get(field_);
			
			if (data.is_string(what_)) output = temp2;
			else if (what_.equals(types.DATA_DECIMAL)) output = numbers.decimal_from_string(temp2);
			else if (what_.equals(types.DATA_LONG)) output = numbers.long_from_string(temp2);
			else if (what_.equals(types.DATA_INT)) output = numbers.int_from_string(temp2);	
		}
		
		return output;
	}
	
	private static ArrayList<HashMap<String, String>> select_internal(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		return adapt_outputs(table_to_source(table_), execute_type(types.DB_QUERY_SELECT, table_, cols_, null, where_, max_rows_, order_, null));
	}
	
	private static void insert_internal(String table_, HashMap<String, String> vals_)
	{
		execute_type(types.DB_QUERY_INSERT, table_, null, vals_, null, 0, null, null);
	}
	
	private static void update_internal(String table_, HashMap<String, String> vals_, String where_)
	{
		execute_type(types.DB_QUERY_UPDATE, table_, null, vals_, where_, 0, null, null);
	}

	private static void delete_internal(String table_, String where_)
	{
		execute_type(types.DB_QUERY_DELETE, table_, null, null, where_, 0, null, null);
	}
	
	private static boolean table_exists_internal(String table_)
	{
		return arrays.is_ok(execute_type(types.DB_QUERY_TABLE_EXISTS, table_, null, null, null, 0, null, null));
	} 
	
	private static void create_table_internal(String table_, HashMap<String, db_field> cols_)
	{
		execute_type(types.DB_QUERY_TABLE_CREATE, table_, null, null, null, 0, null, cols_);
	} 
	
	private static void drop_table_internal(String table_)
	{
		execute_type(types.DB_QUERY_TABLE_DROP, table_, null, null, null, 0, null, null);
	} 
	
	private static void truncate_table_internal(String table_)
	{
		execute_type(types.DB_QUERY_TABLE_TRUNCATE, table_, null, null, null, 0, null, null);
	}
	
	private static ArrayList<HashMap<String, String>> execute_type(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		ArrayList<HashMap<String, String>> output = null;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = db_mysql.execute(what_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output;
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
	
	private static String table_to_source(String table_)
	{
		if (!strings.is_ok(table_) || !arrays.is_ok(db._sources)) return strings.DEFAULT;
		
		for (Entry<String, HashMap<String, db_field>> source: db._sources.entrySet())
		{
			String key = source.getKey();
			String table = db.get_table(key);
			
			if (strings.are_equal(table_, table)) return key;
		}
		
		return strings.DEFAULT;
	}
}