package accessory;

import java.util.ArrayList;
import java.util.HashMap;

abstract class db_quicker 
{	
	private static String[] MYSQL = new String[0];
	
	static String[] _tables = new String[0];

	public static void initialise() { db_static.initialise(); }

	public static boolean is_ok(String source_) { return is_mysql(source_); }
	
	public static boolean is_mysql(String source_) 
	{
		boolean output = false;
		
		if (arrays_quick.value_exists(MYSQL, source_)) output = true; 
		
		return output;
	}
	
	public static void is_ok(boolean is_ok_) { db_static.is_ok(is_ok_); }
	
	public static boolean is_ok() { return db_static.is_ok(); }
	
	public static String get_select_count_col(String type_) 
	{ 
		String output = strings.DEFAULT;
		
		if (type_.equals(db_quicker_mysql.TYPE)) output = db_quicker_mysql.get_select_count_col();
		
		return output; 
	}
	
	public static void update_query_type(String query_type_) { db_static.update_query_type(query_type_); }

	public static String get_query_type() { return db_static.get_query_type(); }
	
	public static String get_username() { return db_static.get_username(); }

	public static String get_password() { return db_static.get_password(); }

	public static String get_db_name() { return db_static.get_db_name(); }

	public static String get_host() { return db_static.get_host(); }

	public static String get_max_pool() { return db_static.get_max_pool(); }
	
	public static boolean conn_info_is_ok() { return db_static.conn_info_is_ok(); }
	
	public static void update_conn_info(String source_) { db_static.update_conn_info(source_); }
	
	public static void update_conn_info(String username_, String password_, String db_name_, String host_, String user_, String max_pool_) { db_static.update_conn_info(username_, password_, db_name_, host_, user_, max_pool_); }
	
	public static boolean change_db_name_queries(String source_, String name_) { return change_db_name_queries(source_, name_); }
	
	public static boolean change_table_name_queries(String source_, String name_) { return db_static.change_table_name_queries(source_, name_); }
	
	public static boolean change_col_name_queries(String source_, String field_, String name_) { return db_static.change_col_name_queries(source_, field_, name_); }
	
	public static String get_table(String source_) 
	{ 
		int i = db.get_source_i(source_);
		
		return (i > arrays.WRONG_I ? _tables[i] : db.get_table(source_)); 
	}
	
	public static void update_table(String source_, String table_) { populate_table(source_, table_, arrays.WRONG_I); }

	public static boolean input_is_ok(Object input_) { return db_quick.input_is_ok(input_); }
	
	public static boolean exists(String type_, String source_, String count_col_, String where_cols_) 
	{
		initialise();

		boolean output = false;
		
		try { output = (select_count(type_, source_, count_col_, where_cols_) > 0); }
		catch (Exception e) 
		{ 
			output = false;
			
			manage_error(type_, strings.to_string(source_), e, null, null, null, strings.to_string(where_cols_), null, db.WRONG_INT, null);
		}
		
		return output; 
	}

	public static String select_one_string(String type_, String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		initialise();
		
		String output = db.WRONG_STRING;

		try
		{	
			Object temp = get_val(col_, select_one(type_, source_, new String[] { col_ }, where_cols_, order_cols_), data.STRING);
		
			if (temp != null) output = (String)temp;
		}
		catch (Exception e) 
		{
			output = db.WRONG_STRING;
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), db.WRONG_INT, null); 
		}
		
		return output; 
	}

	public static double select_one_decimal(String type_, String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		initialise();
		
		double output = db.WRONG_DECIMAL;
		
		try
		{	
			Object temp = get_val(col_, select_one(type_, source_, new String[] { col_ }, where_cols_, order_cols_), data.DECIMAL);
			
			if (temp != null) output = (double)temp;
		}
		catch (Exception e) 
		{
			output = db.WRONG_DECIMAL;
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), db.WRONG_INT, null); 
		}
		
		return output; 
	}

	public static long select_one_long(String type_, String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		initialise();
		
		long output = db.WRONG_LONG;
		
		try
		{	
			Object temp = get_val(col_, select_one(type_, source_, new String[] { col_ }, where_cols_, order_cols_), data.LONG);
			
			if (temp != null) output = (long)temp;
		}
		catch (Exception e) 
		{
			output = db.WRONG_LONG;
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), db.WRONG_INT, null); 
		}
		
		return output; 
	}

	public static int select_one_int(String type_, String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		initialise();
		
		int output = db.WRONG_INT;
		
		try
		{	
			Object temp = get_val(col_, select_one(type_, source_, new String[] { col_ }, where_cols_, order_cols_), data.INT);
			
			if (temp != null) output = (int)temp;
		}
		catch (Exception e) 
		{
			output = db.WRONG_INT;
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), db.WRONG_INT, null); 
		}
		
		return output; 
	}

	public static boolean select_one_boolean(String type_, String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		initialise();
		
		boolean output = db.WRONG_BOOLEAN;
		
		try
		{	
			Object temp = get_val(col_, select_one(type_, source_, new String[] { col_ }, where_cols_, order_cols_), data.BOOLEAN);
			
			if (temp != null) output = (boolean)temp;
		}
		catch (Exception e) 
		{
			output = db.WRONG_BOOLEAN;
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), db.WRONG_INT, null); 
		}
		
		return output; 
	}
	
	public static HashMap<String, String> select_one(String type_, String source_, String[] cols_, String where_cols_, String order_cols_) 
	{ 
		initialise();
		
		HashMap<String, String> output = new HashMap<String, String>();
		
		try
		{	
			ArrayList<HashMap<String, String>> temp = select(type_, source_, cols_, where_cols_, 1, order_cols_);	
		
			if (arrays.is_ok(temp)) output = temp.get(0);
		}
		catch (Exception e) 
		{
			output = new HashMap<String, String>();
			
			manage_error(type_, strings.to_string(source_), e, null, null, cols_, strings.to_string(where_cols_), strings.to_string(order_cols_), db.WRONG_INT, null);
		}
		
		return output; 
	}

	public static ArrayList<String> select_some_strings(String type_, String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		initialise();
		
		ArrayList<String> output = new ArrayList<String>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(type_, source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{	
				for (HashMap<String, String> item: items) { output.add((String)get_val(col_, item, data.STRING)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<String>();
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null);
		}
		
		return output; 
	}

	public static ArrayList<Double> select_some_decimals(String type_, String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		initialise();
		
		ArrayList<Double> output = new ArrayList<Double>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(type_, source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{
				for (HashMap<String, String> item: items) { output.add((double)get_val(col_, item, data.DECIMAL)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<Double>();
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null);
		}
		
		return output; 
	}

	public static ArrayList<Long> select_some_longs(String type_, String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		initialise();
		
		ArrayList<Long> output = new ArrayList<Long>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(type_, source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{
				for (HashMap<String, String> item: items) { output.add((long)get_val(col_, item, data.LONG)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<Long>();
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null);
		}
		
		return output; 
	}

	public static ArrayList<Integer> select_some_ints(String type_, String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		initialise();
		
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(type_, source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{	
				for (HashMap<String, String> item: items) { output.add((int)get_val(col_, item, data.INT)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<Integer>();
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null);
		}
		
		return output; 
	}

	public static ArrayList<Boolean> select_some_booleans(String type_, String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		initialise();
		
		ArrayList<Boolean> output = new ArrayList<Boolean>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(type_, source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{	
				for (HashMap<String, String> item: items) { output.add((boolean)get_val(col_, item, data.BOOLEAN)); }
			}
		}
		catch (Exception e) 
		{ 
			output = new ArrayList<Boolean>();
			
			manage_error(type_, strings.to_string(source_), e, strings.to_string(col_), null, null, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null); 
		}
		
		return output; 
	}

	public static ArrayList<HashMap<String, String>> select(String type_, String source_, String[] cols_, String where_cols_, int max_rows_, String order_cols_) 
	{	
		initialise();

		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();

		try { output = execute(type_, source_, db.QUERY_SELECT, cols_, null, where_cols_, max_rows_, order_cols_, null); }
		catch (Exception e) 
		{
			output = new ArrayList<HashMap<String, String>>();
			
			manage_error(type_, strings.to_string(source_), e, null, null, cols_, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null);
		}
		
		return output; 
	}

	public static int select_count(String type_, String source_, String count_col_, String where_cols_) 
	{	
		initialise();
		
		int output = 0;

		try 
		{ 
			ArrayList<HashMap<String, String>> temp = execute(type_, source_, db.QUERY_SELECT_COUNT, db.DEFAULT_FIELDS_COLS, null, where_cols_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null); 

			if (temp != null && temp.size() > 0) output = Integer.parseInt(temp.get(0).get(count_col_));
		}		
		catch (Exception e) 
		{
			output = 0;
			
			manage_error(type_, strings.to_string(source_), e, null, null, null, strings.to_string(where_cols_), null, db.WRONG_INT, null);
		}
		
		return output; 
	}

	public static void insert_update(String type_, String source_, String count_col_, String any_col_, HashMap<String, String> vals_, String where_cols_) 
	{	
		initialise();
		
		try
		{
			if (exists(type_, source_, count_col_, where_cols_)) update(type_, source_, vals_, where_cols_);
			else insert(type_, source_, vals_);
		}
		catch (Exception e) { manage_error(type_, strings.to_string(source_), e, null, strings.to_string(any_col_), null, strings.to_string(where_cols_), null, db.WRONG_INT, vals_); }		
	}

	public static void insert(String type_, String source_, HashMap<String, String> vals_) 
	{ 
		initialise();
		
		try { execute(type_, source_, db.QUERY_INSERT, null, vals_, db.DEFAULT_WHERE, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null); }
		catch (Exception e) { manage_error(type_, strings.to_string(source_), e, null, null, null, null, null, db.WRONG_INT, vals_); }		
	}
	
	public static void update(String type_, String source_, HashMap<String, String> vals_, String where_cols_) 
	{
		initialise();

		try { execute(type_, source_, db.QUERY_UPDATE, null, vals_, where_cols_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null); }
		catch (Exception e) { manage_error(type_, strings.to_string(source_), e, null, null, null, strings.to_string(where_cols_), null, db.WRONG_INT, vals_); }		
	}
	
	public static ArrayList<HashMap<String, String>> execute(String type_, String source_, String query_type_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_) 
	{ 
		ArrayList<HashMap<String, String>> output = null;
	
		if (type_.equals(db_quicker_mysql.TYPE)) output = db_quicker_mysql.execute(source_, query_type_, cols_, vals_, where_, max_rows_, order_, cols_info_); 
	
		return output;
	}
	
	public static ArrayList<HashMap<String, String>> execute_query(String type_, String source_, String query_, boolean return_data_, String[] cols_) 
	{ 
		ArrayList<HashMap<String, String>> output = null;
		
		if (type_.equals(db_quicker_mysql.TYPE)) output = db_quicker_mysql.execute_query(source_, query_, return_data_, cols_); 
	
		return output; 
	}
	
	public static void populate_types_tables_ini()
	{
		for (int i = 0; i < db.SOURCES.length; i++) 
		{
			String source = db.SOURCES[i];
			if (!strings.is_ok(source)) continue;
			
			populate_table(source, db.get_table(source), i);
			
			String type = db.get_valid_type(source);
			if (!strings.is_ok(type)) continue;
			
			if (strings.are_equal(type, db_quicker_mysql.TYPE)) 
			{
				if (!arrays_quick.value_exists(MYSQL, source)) MYSQL = arrays_quick.add(MYSQL, source);
			}
		}
	}

	private static void populate_table(String source_, String table_, int i_)
	{
		int i = (i_ > arrays.WRONG_I ? i_ : db.get_source_i(source_));
		
		if (i > arrays.WRONG_I && strings.is_ok(table_)) _tables = arrays_quick.add(_tables, i, table_);
	}
	
	private static Object get_val(String col_, HashMap<String, String> item_, String type_) 
	{ 
		Object output = null;
		if (!arrays.is_ok(item_)) return output;
		
		String val = item_.get(col_);
		
		return (data.is_string(type_) ? val : db.adapt_output_internal(val, type_)); 
	}

	private static void manage_error(String type_, String source_, Exception e_, String col_, String any_col_, String[] cols_, String where_cols_, String order_cols_, int max_rows_, HashMap<String, String> vals_) 
	{ 
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		if (source_ != null) info.put("source", source_);	
		if (col_ != null) info.put("col", col_);
		if (any_col_ != null) info.put("any_col", any_col_);
		if (cols_ != null) info.put("cols", cols_);
		if (where_cols_ != null) info.put("where_cols", where_cols_);
		if (order_cols_ != null) info.put("order_cols", order_cols_);
		if (max_rows_ != db.WRONG_INT) info.put("max_rows", max_rows_);
		if (vals_ != null) info.put("vals", strings.to_string(vals_));

		db_static.manage_error(get_type_error(type_), e_, info); 
	}
	
	private static String get_type_error(String type_)
	{
		String output = null;
		if (type_.equals(db_quicker_mysql.TYPE)) output = db_quicker_mysql.ERROR;
	
		return output;
	}
}