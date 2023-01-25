package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

//Use the methods in this class very carefully! Low-to-no checks are performed.
public abstract class db_quick 
{		
	private static ArrayList<String> QUICKER_MYSQL = null;
		
	public static boolean is_ok(String source_) { return (is_quicker(source_) ? db_quicker.is_ok() : db.is_ok(source_)); }

	public static boolean is_quicker(String source_) 
	{
		boolean output = false;
		
		if (QUICKER_MYSQL.contains(source_)) output = true; 
		
		return output;
	}
	
	public static void update_conn_info(String source_)
	{
		if (is_quicker(source_)) db_quicker.update_conn_info(source_);
	}
	
	public static void update_conn_info(String source_, String username_, String password_, String db_name_, String host_, String user_, String max_pool_)
	{
		if (is_quicker(source_)) db_quicker.update_conn_info(username_, password_, db_name_, host_, user_, max_pool_);
	}
	
	public static boolean change_db_name_queries(String source_, String name_) 
	{ 
		boolean output = db.change_db_name_queries(source_, name_); 
		
		if (output) update_conn_info(source_);
		
		return output;
	}
	
	public static boolean change_table_name_queries(String source_, String name_)
	{	
		boolean output = db.change_table_name_queries(source_, name_); 

		if (output) update_conn_info(source_);
		
		return output;
	}
	
	public static boolean change_col_name_queries(String source_, String field_, String name_)
	{	
		boolean output = db.change_col_name_queries(source_, field_, name_); 

		if (output) update_conn_info(source_);
		
		return output;
	}

	public static String get_col(String source_, String field_) 
	{ 
		HashMap<String, String> fields_cols = db_common.get_fields_quick_cols(source_);
		
		return ((fields_cols != null && fields_cols.containsKey(field_)) ? fields_cols.get(field_) : db.get_col(source_, field_)); 
	}

	public static String[] get_cols(String source_) 
	{ 
		int i = db_common.get_i(source_);

		return (i >= 0 ? (String[])arrays.get_new(db_common.COLS[i]) : null);
	}

	public static String[] get_cols(String source_, String[] fields_) 
	{ 
		if (!arrays.is_ok(fields_)) return null;
		
		HashMap<String, String> fields_cols = db_common.get_fields_quick_cols(source_);
		if (!arrays.is_ok(fields_cols)) return null;
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for (String field: fields_) 
		{ 
			if (fields_cols.containsKey(field)) temp.add(fields_cols.get(field));
		}
		
		return arrays.to_array(temp);
	}
	
	public static void update_cols(String source_) { db_common.populate_fields_cols(source_, db.get_source_fields(source_)); }
	
	public static boolean input_is_ok(Object input_) { return (db.input_is_ok(input_) && !arrays.hashmap_is_xy(input_)); }

	public static boolean exists(String source_, String where_cols_) 
	{
		boolean output = db.WRONG_BOOLEAN;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.exists(source_, where_cols_);
		else output = db.exists(source_, where_cols_);
		
		return output; 
	}

	public static String select_one_string(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		String output = db.WRONG_STRING;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_one_string(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_string_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}

	public static double select_one_decimal(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		double output = db.WRONG_DECIMAL;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_one_decimal(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_decimal_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static long select_one_long(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		long output = db.WRONG_LONG;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_one_long(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_long_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static int select_one_int(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		int output = db.WRONG_INT;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_one_int(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_int_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static boolean select_one_boolean(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		boolean output = db.WRONG_BOOLEAN;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_one_boolean(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_boolean_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static HashMap<String, String> select_one(String source_, String[] cols_, String where_cols_, String order_cols_) 
	{ 
		HashMap<String, String> output = new HashMap<String, String>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_one(source_, cols_, where_cols_, order_cols_);
		else output = db.select_one_quick(source_, cols_, where_cols_, order_cols_);

		return output;
	}

	public static ArrayList<String> select_some_strings(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<String> output = new ArrayList<String>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_some_strings(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_strings_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Double> select_some_decimals(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Double> output = new ArrayList<Double>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_some_decimals(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_decimals_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Long> select_some_longs(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Long> output = new ArrayList<Long>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_some_longs(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_longs_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Integer> select_some_ints(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Integer> output = new ArrayList<Integer>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_some_ints(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_ints_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Boolean> select_some_booleans(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Boolean> output = new ArrayList<Boolean>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_some_booleans(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_booleans_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<HashMap<String, String>> select(String source_, String[] cols_, String where_cols_, int max_rows_, String order_cols_) 
	{	
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();

		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select(source_, cols_, where_cols_, max_rows_, order_cols_);
		else output = db.select_quick(source_, cols_, where_cols_, max_rows_, order_cols_);

		return output;
	}
	
	public static int select_count(String source_, String where_cols_) 
	{
		int output = 0;
		
		if (QUICKER_MYSQL.contains(source_)) output = db_quicker_mysql.select_count(source_, where_cols_);
		else output = db.select_count(source_, where_cols_);

		return output;
	}
	
	public static void insert_update(String source_, String any_col_, HashMap<String, String> vals_, String where_cols_) 
	{
		if (QUICKER_MYSQL.contains(source_)) db_quicker_mysql.insert_update(source_, any_col_, vals_, where_cols_); 
		else db.insert_update_quick(source_, vals_, where_cols_);
	}
	
	public static void insert(String source_, HashMap<String, String> vals_) 
	{
		if (QUICKER_MYSQL.contains(source_)) db_quicker_mysql.insert(source_, vals_); 
		else db.insert_quick(source_, vals_);		
	}
	
	public static void update(String source_, HashMap<String, String> vals_, String where_cols_) 
	{
		if (QUICKER_MYSQL.contains(source_)) db_quicker_mysql.update(source_, vals_, where_cols_); 
		else db.update_quick(source_, vals_, where_cols_);		
	}

	static void populate_quicker_ini()
	{
		if (QUICKER_MYSQL == null) QUICKER_MYSQL = new ArrayList<String>();
		
		int tot = db.SOURCES.size();
		if (tot == 0) return;
 
		for (Entry<String, HashMap<String, db_field>> item: db.SOURCES.entrySet()) 
		{
			String source = item.getKey();
			
			String type = db.get_valid_type(source);
			if (!strings.is_ok(type)) continue;
			
			if (strings.are_equal(type, db_quicker_mysql.TYPE)) 
			{
				if (!QUICKER_MYSQL.contains(source)) QUICKER_MYSQL.add(source);
			}
		}
	}
}