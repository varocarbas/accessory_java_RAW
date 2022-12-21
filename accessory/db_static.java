package accessory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;

abstract class db_static 
{
	private static boolean _is_ok = false;
	private static String _query_type = strings.DEFAULT;
	private static String _count_col = null;
	
	private static String _username = strings.DEFAULT;
	private static String _password = null;
	private static String _db_name = db.DEFAULT_DB_NAME;
	private static String _host = db.DEFAULT_HOST;
	private static String _user = null;
	private static String _max_pool = db.DEFAULT_MAX_POOL;

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
	
	public static Connection connect(String type_, String source_, Properties properties_, String db_name_, String host_)
	{
		Connection output = null;
		
		if (strings.are_equal(type_, db.MYSQL)) output = db_mysql.connect_internal_static(source_, properties_, db_name_, host_, true);
		
		return output;		
	}

	public static void manage_error(String type_error_, String query_, Exception e_, String message_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		if (strings.is_ok(query_)) info.put("query", strings.to_string(query_));
		
		info.put(db.HOST, strings.to_string(_host));
		info.put(db.USER, strings.to_string(_user));		
		info.put(db.NAME, strings.to_string(_db_name));
		info.put(errors.MESSAGE, strings.to_string(message_));

		manage_error(strings.to_string(type_error_), e_, info);
	}

	public static void manage_error(String type_error_, Exception e_, HashMap<String, Object> info_)
	{
		_is_ok = false;

		HashMap<String, Object> info = arrays.get_new_hashmap_xy(info_);
		info.put("query_type", strings.to_string(_query_type));
		
		errors.manage(type_error_, e_, info);
	}
	
	private static void populate_count_col(String source_) { _count_col = db.get_select_count_col(source_); }
}