package accessory;

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
	private static String _connect_timeout = db.DEFAULT_CONNECT_TIMEOUT;
	private static String _socket_timeout = db.DEFAULT_SOCKET_TIMEOUT;

	public static void is_ok(boolean is_ok_) { _is_ok = is_ok_; }
	
	public static boolean is_ok() { return _is_ok; }

	public static void update_query_type(String query_type_) { _query_type = query_type_; }
	
	public static String get_query_type() { return _query_type; }

	public static String get_username() { return _username; }

	public static String get_password() { return _password; }

	public static String get_db_name() { return _db_name; }

	public static String get_host() { return _host; }

	public static String get_max_pool() { return _max_pool; }

	public static String get_connect_timeout() { return _connect_timeout; }

	public static String get_socket_timeout() { return _socket_timeout; }
	
	public static void initialise()
	{
		_is_ok = false;
		
		_query_type = strings.DEFAULT;
	}
	
	public static boolean conn_info_is_ok() { return (strings.is_ok(_username) && _password != null && strings.is_ok(_db_name) && strings.is_ok(_host) && strings.is_int(_max_pool)); }
	
	public static void update_conn_info(String source_)
	{
		if (!db.credentials_in_memory(source_)) return;
		
		String[] temp = db.get_credentials(source_);
	
		String username = credentials.get_username(temp);
		String password = credentials.get_password(temp);
	
		String db_name = db.get_db_name(db.get_db(source_));
		
		String setup = db.get_valid_setup(source_);
		String host = db.get_host(setup);
		String user = db.get_user(setup);
		
		String max_pool = db.get_max_pool(setup);
		String connect_timeout = db.get_connect_timeout(setup);
		String socket_timeout = db.get_socket_timeout(setup);
	
		update_conn_info(username, password, db_name, host, user, max_pool, connect_timeout, socket_timeout);
	}
	
	public static void update_conn_info(String username_, String password_, String db_name_, String host_, String user_, String max_pool_, String connect_timeout_, String socket_timeout_)
	{
		if (strings.is_ok(username_)) _username = username_;	
		
		if (password_ != null) _password = password_;
		
		if (strings.is_ok(db_name_)) _db_name = db_name_;
		
		if (strings.is_ok(host_)) _host = host_;
		
		if (strings.is_ok(user_)) _user = user_;
		
		if (strings.is_int(max_pool_)) _max_pool = max_pool_;
		
		if (strings.is_int(connect_timeout_)) _connect_timeout = connect_timeout_;
		
		if (strings.is_int(socket_timeout_)) _socket_timeout = socket_timeout_;
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
}