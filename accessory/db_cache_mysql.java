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
	
	public static int add(String source_, String query_, boolean return_data_, boolean is_quick_) { return db_cache.add(source_, query_, TYPE, return_data_, is_quick_, QUOTE_VALUE, QUOTE_VARIABLE); }
	
	public static boolean exists_simple(int id_select_count_query_, HashMap<Integer, String> changing_vals_) { return db_cache.exists_simple(id_select_count_query_, changing_vals_, TYPE, QUOTE_VALUE, SELECT_COUNT_COL); }
	
	public static int select_count_simple(int id_, HashMap<Integer, String> changing_vals_) { return db_cache.select_count_simple(id_, changing_vals_, TYPE, QUOTE_VALUE, SELECT_COUNT_COL); }
}