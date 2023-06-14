package accessory;

import java.util.HashMap;

//Use the methods in this class very carefully! Relevant inputs are assumed correct and few validity checks are performed.
public abstract class db_cache_mysql 
{
	private static final String TYPE = db.MYSQL;
	private static final String SELECT_COUNT_COL = db_mysql.get_select_count_col_static();
	private static final String QUOTE_VALUE = db_mysql.QUOTE_VALUE;
	private static final String QUOTE_VARIABLE = db_mysql.QUOTE_VARIABLE;
	
	public static String add_placeholders(String source_, String query_, String[] cols_) { return db_cache.add_placeholders(source_, query_, cols_, QUOTE_VALUE, QUOTE_VARIABLE); }
	
	public static String add_placeholders(String source_, String query_, String col_, int id_) { return db_cache.add_placeholders(source_, query_, col_, id_, QUOTE_VALUE, QUOTE_VARIABLE); }
	
	public static int add(String source_, String query_, boolean return_data_, boolean is_quick_) { return db_cache.add(source_, query_, return_data_, is_quick_, QUOTE_VALUE, QUOTE_VARIABLE); }

	public static int add_select_count(String source_, String col_, int col_id_, boolean is_quick_) { return db_cache.add_select_count(source_, col_, col_id_, is_quick_, QUOTE_VALUE, QUOTE_VARIABLE); }

	public static int add_select_count(String source_, String col_, int col_id_, String where_, boolean is_quick_) { return db_cache.add_select_count(source_, col_, col_id_, where_, is_quick_, QUOTE_VALUE, QUOTE_VARIABLE); }

	public static String get_query_select_count(String source_, String where_col_, int where_col_id_, String where_) { return db_cache.get_query_select_count(source_, where_col_, where_col_id_, where_, QUOTE_VARIABLE); }
	
	public static String get_query_select_count(String source_, String where_) { return db_cache.get_query_select_count(source_, where_, QUOTE_VARIABLE); }
	
	public static String get_query_update(String source_, String[] cols_ids_, String where_) { return db_cache.get_query_update(source_, cols_ids_, where_, QUOTE_VARIABLE); }

	public static String get_query_insert(String source_, String[] cols_ids_) { return db_cache.get_query_insert(source_, cols_ids_, QUOTE_VARIABLE); }

	public static String get_query_where(String source_, String col_, int col_id_, String where_, boolean add_where_) { return db_cache.get_query_where(source_, col_, col_id_, where_, add_where_, QUOTE_VARIABLE); }
	
	public static boolean exists_simple(int id_select_count_query_, HashMap<Integer, String> changing_vals_) { return db_cache.exists_simple(id_select_count_query_, changing_vals_, TYPE, QUOTE_VALUE, SELECT_COUNT_COL); }
	
	public static int select_count_simple(int id_, HashMap<Integer, String> changing_vals_) { return db_cache.select_count_simple(id_, changing_vals_, TYPE, QUOTE_VALUE, SELECT_COUNT_COL); }

	public static void insert_update_simple(int id_select_count_query_, int id_update_query_, int id_insert_query_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_, String select_count_col_) 
	{	
		if (exists_simple(id_select_count_query_, changing_vals_)) update_simple(id_update_query_, changing_vals_, type_, quote_value_);
		else insert_simple(id_insert_query_, changing_vals_, type_, quote_value_);
	}

	public static void insert_simple(int id_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_) { db_cache.insert_simple(id_, changing_vals_, type_, quote_value_); }

	public static void update_simple(int id_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_) { db_cache.update_simple(id_, changing_vals_, type_, quote_value_); }
}