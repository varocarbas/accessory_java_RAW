package accessory;

import java.util.ArrayList;
import java.util.HashMap;

//Use the methods in this class very carefully! No checks of any sort are performed.
//Note that the setup information (e.g., host or credentials) isn't being checked either.
//These methods can only be used with sources associated with a setup where CONFIG_DB_SETUP_CREDENTIALS_MEMORY is true.
public abstract class db_quicker_mysql 
{
	public static final String TYPE = db.MYSQL;
	
	public static final String ERROR = _types.ERROR_DB_QUICKER;
	
	public static boolean is_ok() { return db_quicker.is_ok(); }
	
	public static String get_select_count_col() { return db_mysql.get_select_count_col_static(); }

	public static String get_username() { return db_quicker.get_username(); }

	public static String get_password() { return db_quicker.get_password(); }

	public static String get_db_name() { return db_quicker.get_db_name(); }

	public static String get_host() { return db_quicker.get_host(); }

	public static String get_max_pool() { return db_quicker.get_max_pool(); }

	public static String get_connect_timeout() { return db_quicker.get_connect_timeout(); }

	public static String get_socket_timeout() { return db_quicker.get_socket_timeout(); }
	
	public static boolean conn_info_is_ok() { return db_quicker.conn_info_is_ok(); }
	
	public static void update_conn_info(String source_) { db_quicker.update_conn_info(source_); }
	
	public static void update_conn_info(String username_, String password_, String db_name_, String host_, String user_, String max_pool_, String connect_timeout_, String socket_timeout_) { db_quicker.update_conn_info(username_, password_, db_name_, host_, user_, max_pool_, connect_timeout_, socket_timeout_); }
	
	public static boolean change_db_name_queries(String source_, String name_) { return db_quicker.change_db_name_queries(source_, name_); }
	
	public static boolean change_table_name_queries(String source_, String name_) { return db.change_table_name_queries(source_, name_); }
	
	public static boolean change_col_name_queries(String source_, String field_, String name_) { return db_quicker.change_col_name_queries(source_, field_, name_); }
	
	public static boolean exists(String source_, String where_cols_) { return db_quicker.exists(TYPE, source_, get_select_count_col(), where_cols_); }

	public static String select_one_string(String source_, String col_, String where_cols_, String order_cols_) { return db_quicker.select_one_string(TYPE, source_, col_, where_cols_, order_cols_); }

	public static double select_one_decimal(String source_, String col_, String where_cols_, String order_cols_) { return db_quicker.select_one_decimal(TYPE, source_, col_, where_cols_, order_cols_); }

	public static long select_one_long(String source_, String col_, String where_cols_, String order_cols_) { return db_quicker.select_one_long(TYPE, source_, col_, where_cols_, order_cols_); }

	public static int select_one_int(String source_, String col_, String where_cols_, String order_cols_) { return db_quicker.select_one_int(TYPE, source_, col_, where_cols_, order_cols_); }

	public static boolean select_one_boolean(String source_, String col_, String where_cols_, String order_cols_) { return db_quicker.select_one_boolean(TYPE, source_, col_, where_cols_, order_cols_); }
	
	public static HashMap<String, String> select_one(String source_, String[] cols_, String where_cols_, String order_cols_) { return db_quicker.select_one(TYPE, source_, cols_, where_cols_, order_cols_); }

	public static ArrayList<String> select_some_strings(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) { return db_quicker.select_some_strings(TYPE, source_, col_, where_cols_, max_rows_, order_cols_); }

	public static ArrayList<Double> select_some_decimals(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) { return db_quicker.select_some_decimals(TYPE, source_, col_, where_cols_, max_rows_, order_cols_); }

	public static ArrayList<Long> select_some_longs(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) { return db_quicker.select_some_longs(TYPE, source_, col_, where_cols_, max_rows_, order_cols_); }

	public static ArrayList<Integer> select_some_ints(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) { return db_quicker.select_some_ints(TYPE, source_, col_, where_cols_, max_rows_, order_cols_); }

	public static ArrayList<Boolean> select_some_booleans(String source_, String col_, String where_cols_, int max_rows_, String order_cols_) { return db_quicker.select_some_booleans(TYPE, source_, col_, where_cols_, max_rows_, order_cols_); }

	public static ArrayList<HashMap<String, String>> select(String source_, String[] cols_, String where_cols_, int max_rows_, String order_cols_) { return db_quicker.select(TYPE, source_, cols_, where_cols_, max_rows_, order_cols_); }
	
	public static int select_count(String source_, String where_cols_) { return db_quicker.select_count(TYPE, source_, get_select_count_col(), where_cols_); }

	public static void insert_update(String source_, String any_col_, HashMap<String, String> vals_, String where_cols_) { db_quicker.insert_update(TYPE, source_, get_select_count_col(), any_col_, vals_, where_cols_); }

	public static void insert(String source_, HashMap<String, String> vals_) { db_quicker.insert(TYPE, source_, vals_); }
	
	public static void update(String source_, HashMap<String, String> vals_, String where_cols_) { db_quicker.update(TYPE, source_, vals_, where_cols_); }

	static ArrayList<HashMap<String, String>> execute(String source_, String type_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_) { return db_mysql.execute_quicker(source_, type_, cols_, vals_, where_, max_rows_, order_, cols_info_); }

	static ArrayList<HashMap<String, String>> execute_query(String source_, String query_, boolean return_data_, String[] cols_) { return db_mysql.execute_query_quicker(source_, query_, return_data_, cols_); }
}