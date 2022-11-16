package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class db_quick 
{
	private static ArrayList<String> _sources_quicker_mysql = new ArrayList<String>();
	
	public static void add_sources(String[] sources_)
	{
		if (!arrays.is_ok(sources_)) return;
		
		String any_source_mysql = null;
		
		for (String source: sources_) 
		{
			String type = db.get_valid_type(source);
			if (!strings.is_ok(type)) continue;
			
			if (strings.are_equal(type, db_quicker_mysql.TYPE)) 
			{
				_sources_quicker_mysql.add(source);
			
				if (any_source_mysql == null) any_source_mysql = source;
			}
		}

		if (any_source_mysql != null) db_quicker_mysql.update_conn_info(any_source_mysql);
	}
	
	public static boolean exists(String source_, String where_cols_) 
	{
		boolean output = db.WRONG_BOOLEAN;
		
		if (_sources_quicker_mysql.contains(source_)) db_quicker_mysql.exists(source_, where_cols_);
		else db.exists(source_, where_cols_);
		
		return output; 
	}

	public static String select_one_string(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		String output = db.WRONG_STRING;
		
		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_one_string(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_string_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}

	public static double select_one_decimal(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		double output = db.WRONG_DECIMAL;
		
		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_one_decimal(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_decimal_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static long select_one_long(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		long output = db.WRONG_LONG;
		
		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_one_long(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_long_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static int select_one_int(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		int output = db.WRONG_INT;
		
		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_one_int(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_int_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static boolean select_one_boolean(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		boolean output = db.WRONG_BOOLEAN;
		
		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_one_boolean(source_, col_, where_cols_, order_cols_);
		else output = db.select_one_boolean_quick(source_, col_, where_cols_, order_cols_);
	
		return output;
	}
	
	public static HashMap<String, String> select_one(String source_, String[] cols_, String where_cols_, String order_cols_) 
	{ 
		HashMap<String, String> output = new HashMap<String, String>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_one(source_, cols_, where_cols_, order_cols_);
		else output = db.select_one_quick(source_, cols_, where_cols_, order_cols_);

		return output;
	}

	public static ArrayList<String> select_some_strings(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<String> output = new ArrayList<String>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_some_strings(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_strings_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Double> select_some_decimals(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Double> output = new ArrayList<Double>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_some_decimals(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_decimals_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Long> select_some_longs(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Long> output = new ArrayList<Long>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_some_longs(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_longs_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Integer> select_some_ints(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Integer> output = new ArrayList<Integer>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_some_ints(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_ints_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<Boolean> select_some_booleans(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Boolean> output = new ArrayList<Boolean>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_some_booleans(source_, col_, where_cols_, max_rows_, order_cols_);
		else output = db.select_some_booleans_quick(source_, col_, where_cols_, max_rows_, order_cols_);

		return output;
	}

	public static ArrayList<HashMap<String, String>> select(String source_, String[] cols_, String where_cols_, int max_rows_, String order_cols_) 
	{	
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();

		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select(source_, cols_, where_cols_, max_rows_, order_cols_);
		else output = db.select_quick(source_, cols_, where_cols_, max_rows_, order_cols_);

		return output;
	}
	
	public static int select_count(String source_, String where_cols_) 
	{
		int output = 0;
		
		if (_sources_quicker_mysql.contains(source_)) output = db_quicker_mysql.select_count(source_, where_cols_);
		else output = db.select_count(source_, where_cols_);

		return output;
	}
	
	public static void insert_update(String source_, String any_col_, HashMap<String, String> vals_, String where_cols_) 
	{
		if (_sources_quicker_mysql.contains(source_)) db_quicker_mysql.insert_update(source_, any_col_, vals_, where_cols_); 
		else db.insert_update_quick(source_, vals_, where_cols_);
	}
	
	public static void insert(String source_, HashMap<String, String> vals_) 
	{
		if (_sources_quicker_mysql.contains(source_)) db_quicker_mysql.insert(source_, vals_); 
		else db.insert_quick(source_, vals_);		
	}
	
	public static void update(String source_, HashMap<String, String> vals_, String where_cols_) 
	{
		if (_sources_quicker_mysql.contains(source_)) db_quicker_mysql.update(source_, vals_, where_cols_); 
		else db.update_quick(source_, vals_, where_cols_);		
	}
	
	static void start() { add_sources(db_common.get_all_sources()); }
}