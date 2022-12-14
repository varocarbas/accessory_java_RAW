package accessory;

import java.util.ArrayList;
import java.util.HashMap;

abstract class db_static 
{
	private static boolean _is_ok = false;
	private static String _query_type = strings.DEFAULT;
	private static String _username = strings.DEFAULT;
	private static String _password = null;
	private static String _db_name = db.DEFAULT_DB_NAME;
	private static String _host = db.DEFAULT_HOST;
	private static String _user = null;
	private static String _max_pool = db.DEFAULT_MAX_POOL;
	private static String _count_col = null;

	public static void is_ok(boolean is_ok_) { _is_ok = is_ok_; }
	
	public static boolean is_ok() { return _is_ok; }

	public static void update_query_type(String query_type_) { _query_type = query_type_; }

	public static String get_query_type() { return _query_type; }

	public static String get_count_col(String source_) 
	{ 
		if (!strings.is_ok(_count_col)) populate_count_col(source_);
		
		return _count_col; 
	}

	public static String get_username() { return _username; }

	public static String get_password() { return _password; }

	public static String get_db_name() { return _db_name; }

	public static String get_host() { return _host; }

	public static String get_max_pool() { return _max_pool; }

	public static void initialise()
	{
		_is_ok = false;
		
		_query_type = strings.DEFAULT;
	}
	
	public static boolean conn_info_is_ok() { return (strings.is_ok(_username) && _password != null && strings.is_ok(_db_name) && strings.is_ok(_host) && strings.is_int(_max_pool)); }
	
	public static void update_conn_info(String source_)
	{
		populate_count_col(source_);
		
		HashMap<String, String> credentials = db.get_credentials(source_);
	
		String username = (String)arrays.get_value(credentials, accessory.credentials.USERNAME);
		String password = (String)arrays.get_value(credentials, accessory.credentials.PASSWORD);
	
		String db_name = db.get_db_name(db.get_db(source_));
		
		String setup = db.get_valid_setup(source_);
		String host = db.get_host(setup);
		String user = db.get_user(setup);
		
		String max_pool = db.get_max_pool(setup);
	
		update_conn_info(username, password, db_name, host, user, max_pool);
	}
	
	public static void update_conn_info(String username_, String password_, String db_name_, String host_, String user_, String max_pool_)
	{
		if (strings.is_ok(username_)) _username = username_;	
		
		if (password_ != null) _password = password_;
		
		if (strings.is_ok(db_name_)) _db_name = db_name_;
		
		if (strings.is_ok(host_)) _host = host_;
		
		if (strings.is_ok(user_)) _user = user_;
		
		if (strings.is_int(max_pool_)) _max_pool = max_pool_;
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
	
	public static boolean exists(String type_, String source_, String where_cols_) 
	{
		initialise();
		
		boolean output = false;
		
		try { output = (select_count(type_, source_, where_cols_) > 0); }
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

		try { output = db_mysql.execute_static(source_, db.QUERY_SELECT, cols_, null, where_cols_, max_rows_, order_cols_, null, type_); }
		catch (Exception e) 
		{
			output = new ArrayList<HashMap<String, String>>();
			
			manage_error(type_, strings.to_string(source_), e, null, null, cols_, strings.to_string(where_cols_), strings.to_string(order_cols_), max_rows_, null);
		}
		
		return output; 
	}

	public static int select_count(String type_, String source_, String where_cols_) 
	{	
		initialise();
		
		int output = 0;

		try 
		{ 
			String col = get_count_col(source_);
			
			ArrayList<HashMap<String, String>> temp = db_mysql.execute_static(source_, db.QUERY_SELECT_COUNT, db.DEFAULT_FIELDS_COLS, null, where_cols_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null, type_); 

			if (temp != null && temp.size() > 0) output = Integer.parseInt(temp.get(0).get(col));
		}		
		catch (Exception e) 
		{
			output = 0;
			
			manage_error(type_, strings.to_string(source_), e, null, null, null, strings.to_string(where_cols_), null, db.WRONG_INT, null);
		}
		
		return output; 
	}

	public static void insert_update(String type_, String source_, String any_col_, HashMap<String, String> vals_, String where_cols_) 
	{	
		initialise();
		
		try
		{
			if (exists(type_, source_, where_cols_)) update(type_, source_, vals_, where_cols_);
			else insert(type_, source_, vals_);
		}
		catch (Exception e) { manage_error(type_, strings.to_string(source_), e, null, strings.to_string(any_col_), null, strings.to_string(where_cols_), null, db.WRONG_INT, vals_); }		
	}

	public static void insert(String type_, String source_, HashMap<String, String> vals_) 
	{ 
		initialise();
		
		try { db_mysql.execute_static(source_, db.QUERY_INSERT, null, vals_, db.DEFAULT_WHERE, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null, type_); }
		catch (Exception e) { manage_error(type_, strings.to_string(source_), e, null, null, null, null, null, db.WRONG_INT, vals_); }		
	}
	
	public static void update(String type_, String source_, HashMap<String, String> vals_, String where_cols_) 
	{
		initialise();

		try { db_mysql.execute_static(source_, db.QUERY_UPDATE, null, vals_, where_cols_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null, type_); }
		catch (Exception e) { manage_error(type_, strings.to_string(source_), e, null, null, null, strings.to_string(where_cols_), null, db.WRONG_INT, vals_); }		
	}

	public static void manage_error(String type_error_, String query_, Exception e_, String message_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		if (strings.is_ok(query_)) info.put("query", strings.to_string(query_));
		
		info.put(db.HOST, strings.to_string(_host));
		info.put(db.USER, strings.to_string(_user));		
		info.put(db.NAME, strings.to_string(_db_name));
		info.put(errors.MESSAGE, strings.to_string(message_));

		manage_error_internal(strings.to_string(type_error_), e_, info);
	}

	public static ArrayList<HashMap<String, String>> execute_query(String type_, String source_, String query_, boolean return_data_, String[] cols_) { return db_sql.execute_query_static(type_, source_, query_, return_data_, cols_, get_username(), get_password(), get_db_name(), get_host(), get_max_pool()); }
	
	private static Object get_val(String col_, HashMap<String, String> item_, String type_) 
	{ 
		Object output = null;
		if (!arrays.is_ok(item_)) return output;
		
		String val = item_.get(col_);
		
		return (data.is_string(type_) ? val : db.adapt_output_internal(val, type_)); 
	}

	private static void populate_count_col(String source_) { _count_col = db.get_select_count_col(source_); }

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

		manage_error_internal(get_type_error(type_), e_, info); 
	}
	
	private static void manage_error_internal(String type_error_, Exception e_, HashMap<String, Object> info_)
	{
		_is_ok = false;

		HashMap<String, Object> info = arrays.get_new_hashmap_xy(info_);
		info.put("query_type", strings.to_string(_query_type));
		
		errors.manage(type_error_, e_, info);
	}
	
	private static String get_type_error(String type_)
	{
		String output = null;
		if (type_.equals(db_quicker_mysql.TYPE)) output = db_quicker_mysql.ERROR;
	
		return output;
	}
}