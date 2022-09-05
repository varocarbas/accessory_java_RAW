package accessory;

import java.util.ArrayList;
import java.util.HashMap;

//Use the methods in this class very carefully! No checks of any sort are performed.
public abstract class db_quicker_mysql 
{
	public static final String TYPE = db.MYSQL;
	
	public static final String ERROR = types.ERROR_DB_QUICKER;
	
	private static String _username = strings.DEFAULT;
	private static String _password = strings.DEFAULT;
	private static String _db_name = db.DEFAULT_DB_NAME;
	private static String _host = db.DEFAULT_HOST;
	private static String _max_pool = db.DEFAULT_MAX_POOL;
	
	public static boolean conn_info_is_ok() { return (strings.is_ok(_username) && _password != null && strings.is_ok(_db_name) && strings.is_ok(_host) && strings.is_int(_max_pool)); }
	
	public static void update_conn_info(String source_)
	{
		HashMap<String, String> credentials = db.get_credentials(source_);

		String username = (String)arrays.get_value(credentials, accessory.credentials.USERNAME);
		String password = (String)arrays.get_value(credentials, accessory.credentials.PASSWORD);

		String db_name = db.get_db_name(db.get_db(source_));
		
		String setup = db.get_valid_setup(source_);
		String host = db.get_host(setup);
		String max_pool = db.get_max_pool(setup);

		update_conn_info(username, password, db_name, host, max_pool);
	}
	
	public static void update_conn_info(String username_, String password_, String db_name_, String host_, String max_pool_)
	{
		if (strings.is_ok(username_)) _username = username_;	
		
		if (password_ != null) _password = password_;
		
		if (strings.is_ok(db_name_)) _db_name = db_name_;
		
		if (strings.is_ok(host_)) _host = host_;
		
		if (strings.is_int(max_pool_)) _max_pool = max_pool_;
	}
	
	public static boolean exists(String source_, String any_col_, String where_cols_) 
	{
		boolean output = false;
		
		try { output = arrays.is_ok(select_one(source_, new String[] { any_col_ }, where_cols_, db.DEFAULT_ORDER)); }
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
	
			info.put("source", strings.to_string(source_));
			info.put("any_col", strings.to_string(any_col_));
			info.put("where_cols", strings.to_string(where_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static String select_one_string(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		String output = db.WRONG_STRING;
		
		try
		{	
			Object temp = get_val(col_, select_one(source_, new String[] { col_ }, where_cols_, order_cols_), data.STRING);
		
			if (temp != null) output = (String)temp;
		}
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static double select_one_decimal(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		double output = db.WRONG_DECIMAL;
		
		try
		{	
			Object temp = get_val(col_, select_one(source_, new String[] { col_ }, where_cols_, order_cols_), data.DECIMAL);
			
			if (temp != null) output = (double)temp;
		}
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static long select_one_long(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		long output = db.WRONG_LONG;
		
		try
		{	
			Object temp = get_val(col_, select_one(source_, new String[] { col_ }, where_cols_, order_cols_), data.LONG);
			
			if (temp != null) output = (long)temp;
		}
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static int select_one_int(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		int output = db.WRONG_INT;
		
		try
		{	
			Object temp = get_val(col_, select_one(source_, new String[] { col_ }, where_cols_, order_cols_), data.INT);
			
			if (temp != null) output = (int)temp;
		}
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static boolean select_one_boolean(String source_, String col_, String where_cols_, String order_cols_) 
	{ 
		boolean output = db.WRONG_BOOLEAN;
		
		try
		{	
			Object temp = get_val(col_, select_one(source_, new String[] { col_ }, where_cols_, order_cols_), data.BOOLEAN);
			
			if (temp != null) output = (boolean)temp;
		}
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}
	
	public static HashMap<String, String> select_one(String source_, String[] cols_, String where_cols_, String order_cols_) 
	{ 
		HashMap<String, String> output = new HashMap<String, String>();
		
		try
		{	
			ArrayList<HashMap<String, String>> temp = select(source_, cols_, where_cols_, 1, order_cols_);	
		
			if (arrays.is_ok(temp)) output = temp.get(0);
		}
		catch (Exception e) 
		{
			output = new HashMap<String, String>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("cols", strings.to_string(cols_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static ArrayList<String> select_some_strings(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<String> output = new ArrayList<String>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{	
				for (HashMap<String, String> item: items) { output.add((String)get_val(col_, item, data.STRING)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<String>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("max_rows", max_rows_);
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static ArrayList<Double> select_some_decimals(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Double> output = new ArrayList<Double>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{
				for (HashMap<String, String> item: items) { output.add((double)get_val(col_, item, data.DECIMAL)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<Double>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("max_rows", max_rows_);
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static ArrayList<Long> select_some_longs(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Long> output = new ArrayList<Long>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{
				for (HashMap<String, String> item: items) { output.add((long)get_val(col_, item, data.LONG)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<Long>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("max_rows", max_rows_);
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static ArrayList<Integer> select_some_ints(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{	
				for (HashMap<String, String> item: items) { output.add((int)get_val(col_, item, data.INT)); }
			}
		}
		catch (Exception e) 
		{
			output = new ArrayList<Integer>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("max_rows", max_rows_);
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static ArrayList<Boolean> select_some_booleans(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) 
	{ 
		ArrayList<Boolean> output = new ArrayList<Boolean>();
		
		try 
		{ 
			ArrayList<HashMap<String, String>> items = select(source_, new String[] { col_ }, where_cols_, max_rows_, order_cols_); 
		
			if (arrays.is_ok(items))
			{	
				for (HashMap<String, String> item: items) { output.add((boolean)get_val(col_, item, data.BOOLEAN)); }
			}
		}
		catch (Exception e) 
		{ 
			output = new ArrayList<Boolean>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("col", strings.to_string(col_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("max_rows", max_rows_);
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static ArrayList<HashMap<String, String>> select(String source_, String[] cols_, String where_cols_, int max_rows_, String order_cols_) 
	{	
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();

		try { output = db_mysql.execute_static(source_, db.QUERY_SELECT, cols_, null, where_cols_, max_rows_, order_cols_, null, TYPE); }
		catch (Exception e) 
		{
			output = new ArrayList<HashMap<String, String>>();
			
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("cols", strings.to_string(cols_));
			info.put("where_cols", strings.to_string(where_cols_));
			info.put("max_rows", max_rows_);
			info.put("order_cols", strings.to_string(order_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}
		
		return output; 
	}

	public static void insert_update(String source_, String any_col_, HashMap<String, String> vals_, String where_cols_) 
	{	
		try
		{
			if (exists(source_, any_col_, where_cols_)) update(source_, vals_, where_cols_);
			else insert(source_, vals_);
		}
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("any_col", strings.to_string(any_col_));
			info.put("vals", strings.to_string(vals_));
			info.put("where_cols", strings.to_string(where_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}		
	}
	
	public static void insert(String source_, HashMap<String, String> vals_) 
	{ 
		try { db_mysql.execute_static(source_, db.QUERY_INSERT, null, vals_, db.DEFAULT_WHERE, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null, TYPE); }
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));
			info.put("vals", strings.to_string(vals_));
			
			db.manage_error(source_, ERROR, e, info); 
		}		
	}
	
	public static void update(String source_, HashMap<String, String> vals_, String where_cols_) 
	{
		try { db_mysql.execute_static(source_, db.QUERY_UPDATE, null, vals_, where_cols_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER, null, TYPE); }
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("source", strings.to_string(source_));	
			info.put("vals", strings.to_string(vals_));
			info.put("where_cols", strings.to_string(where_cols_));
			
			db.manage_error(source_, ERROR, e, info); 
		}		
	}

	static String get_username() { return _username; }

	static String get_password() { return _password; }

	static String get_db_name() { return _db_name; }

	static String get_host() { return _host; }

	static String get_max_pool() { return _max_pool; }

	private static Object get_val(String col_, HashMap<String, String> item_, String type_) 
	{ 
		Object output = null;
		if (!arrays.is_ok(item_)) return output;
		
		String val = item_.get(col_);
		
		return (data.is_string(type_) ? val : db.adapt_output_internal(val, type_)); 
	}	
}