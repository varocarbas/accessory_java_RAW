package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class db_common 
{
	public static final int MAX_SIZE_KEY = 30;
	public static final int MAX_SIZE_VALUE = 2 * MAX_SIZE_KEY;
	public static final int MAX_SIZE_ERROR = 150;

	public static final int WRONG_MAX_SIZE = 0;
	public static final int WRONG_MAX_LENGTH = 0;
	public static final double WRONG_MAX_VAL = 0.0;
	
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final boolean DEFAULT_IS_FIELD = true;
	public static final int DEFAULT_SIZE_DECIMAL = 7;
	public static final int DEFAULT_SIZE_STRING = 30;
	
	private static ArrayList<String> IS_QUICK = new ArrayList<String>();

	public static String[] get_fields(String source_) { return (String[])db.get_source_fields(source_, true, false); }
	
	public static HashMap<String, String> get_fields_cols(String source_, String[] fields_) { return db.get_fields_cols(source_, fields_); }

	public static boolean is_quick(String source_) { return IS_QUICK.contains(source_); }
	
	public static void is_quick(String source_, boolean is_quick_) 
	{ 
		if (!strings.is_ok(source_)) return;
		
		if (is_quick_)
		{
			if (!IS_QUICK.contains(source_)) IS_QUICK.add(source_);
		}
		else IS_QUICK.remove(source_);
	}

	public static String get_field_quick_col(String source_, String field_col_) { return get_field_quick_col(source_, field_col_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static String get_field_quick_col(String source_, String field_col_, boolean is_field_, boolean is_quick_) { return ((is_field_ && is_quick_) ? db_quick.get_col(source_, field_col_) : field_col_); }

	public static void update_quick_cols(String source_) { db_quick.update_cols(source_); }
	
	public static boolean is_empty(String source_) { return (get_count(source_, db.DEFAULT_WHERE) == 0); }

	public static int get_count(String source_, String where_) { return db_quick.select_count(source_, where_); }

	public static boolean exists(String source_, String where_) { return db_quick.exists(source_, where_); }

	public static String get_string(String source_, String field_col_, String where_, String wrong_) { return get_string(source_, field_col_, where_, wrong_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static String get_string(String source_, String field_col_, String where_, String wrong_, boolean is_field_, boolean is_quick_) 
	{ 
		String output = (is_quick_ ? db_quick.select_one_string(source_, get_field_quick_col(source_, field_col_, is_field_, is_quick_), where_, db.DEFAULT_ORDER) : db.select_one_string(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (!db.WRONG_STRING.equals(output) ? output : wrong_);
	}
	
	public static int get_int(String source_, String field_col_, String where_, int wrong_) { return get_int(source_, field_col_, where_, wrong_, DEFAULT_IS_FIELD, is_quick(source_)); }	
	
	public static int get_int(String source_, String field_col_, String where_, int wrong_, boolean is_field_, boolean is_quick_) 
	{ 
		int output = (is_quick_ ? db_quick.select_one_int(source_, get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_ORDER) : db.select_one_int(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (output != db.WRONG_INT ? output : wrong_);
	}

	public static double get_decimal(String source_, String field_col_, String where_, double wrong_) { return get_decimal(source_, field_col_, where_, wrong_, DEFAULT_IS_FIELD, is_quick(source_)); }
	
	public static double get_decimal(String source_, String field_col_, String where_, double wrong_, boolean is_field_, boolean is_quick_) 
	{ 
		double output = (is_quick_ ? db_quick.select_one_decimal(source_, get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_ORDER) : db.select_one_decimal(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (output != db.WRONG_DECIMAL ? output : wrong_);
	}

	public static long get_long(String source_, String field_col_, String where_, long wrong_) { return get_long(source_, field_col_, where_, wrong_, DEFAULT_IS_FIELD, is_quick(source_)); }
	
	public static long get_long(String source_, String field_col_, String where_, long wrong_, boolean is_field_, boolean is_quick_) 
	{ 
		long output = (is_quick_ ? db_quick.select_one_long(source_, get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_ORDER) : db.select_one_long(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (output != db.WRONG_LONG ? output : wrong_);
	}
	
	public static boolean get_boolean(String source_, String field_col_, String where_) { return get_boolean(source_, field_col_, where_, DEFAULT_IS_FIELD, is_quick(source_)); }
	
	public static boolean get_boolean(String source_, String field_col_, String where_, boolean is_field_, boolean is_quick_) { return (is_quick_ ? db_quick.select_one_boolean(source_, get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_ORDER) : db.select_one_boolean(source_, field_col_, where_, db.DEFAULT_ORDER)); }

	public static ArrayList<Double> get_all_decimals(String source_, String field_col_, String where_) { return get_all_decimals(source_, field_col_, where_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static ArrayList<Double> get_all_decimals(String source_, String field_col_, String where_, boolean is_field_, boolean is_quick_) { return (is_quick_ ? db_quick.select_some_decimals(source_, db_common.get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_decimals(source_, field_col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<String> get_all_strings(String source_, String field_col_, String where_) { return get_all_strings(source_, field_col_, where_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static ArrayList<String> get_all_strings(String source_, String field_col_, String where_, boolean is_field_, boolean is_quick_) { return (is_quick_ ? db_quick.select_some_strings(source_, db_common.get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_strings(source_, field_col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<Integer> get_all_ints(String source_, String field_col_, String where_) { return get_all_ints(source_, field_col_, where_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static ArrayList<Integer> get_all_ints(String source_, String field_col_, String where_, boolean is_field_, boolean is_quick_) { return (is_quick_ ? db_quick.select_some_ints(source_, db_common.get_field_quick_col(source_, field_col_, is_field_, true), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_ints(source_, field_col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_cols_, String where_) { return get_all_vals(source_, fields_cols_, where_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_cols_, String where_, boolean are_fields_, boolean is_quick_) { return (is_quick_ ? db_quick.select(source_, (are_fields_ ? db_quick.get_cols(source_, fields_cols_) : fields_cols_), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : accessory.db.select(source_, fields_cols_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_cols_, String where_) { return get_vals(source_, fields_cols_, where_, db.DEFAULT_ORDER); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_cols_, String where_, String order_) { return get_vals(source_, fields_cols_, where_, order_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_cols_, String where_, String order_, boolean are_fields_, boolean is_quick_) { return (is_quick_ ? db_quick.select_one(source_, (are_fields_ ? db_quick.get_cols(source_, fields_cols_) : fields_cols_), where_, order_) : db.select_one(source_, fields_cols_, where_, order_)); }

	public static boolean insert(String source_, Object vals_) { return insert(source_, vals_, is_quick(source_)); }
	
	@SuppressWarnings("unchecked")
	public static boolean insert(String source_, Object vals_, boolean is_quick_) 
	{ 
		if (!arrays.is_ok(vals_)) return false;
		
		if (is_quick_) db_quick.insert(source_, (HashMap<String, String>)vals_);
		else db.insert(source_, (HashMap<String, Object>)vals_);

		return db.is_ok(source_, is_quick_);
	}

	public static boolean update(String source_, String field_col_, Object val_, String where_) { return update(source_, field_col_, val_, where_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static boolean update(String source_, String field_col_, Object val_, String where_, boolean is_field_, boolean is_quick_) { return update(source_, add_to_vals(source_, field_col_, val_, null, is_field_, is_quick_), where_, is_quick(source_)); }
	
	public static boolean update(String source_, Object vals_, String where_) { return update(source_, vals_, where_, is_quick(source_)); }
	
	@SuppressWarnings("unchecked")
	public static boolean update(String source_, Object vals_, String where_, boolean is_quick_)
	{	
		if (!vals_are_ok(source_, vals_, is_quick_)) return false;
		
		if (is_quick_) db_quick.update(source_, (HashMap<String, String>)vals_, where_);
		else db.update(source_, (HashMap<String, Object>)vals_, where_);
		
		return db.is_ok(source_, is_quick_);
	}
	
	public static boolean insert_update(String source_, String field_col_, Object val_, String where_, String field_where_) { return insert_update(source_, field_col_, val_, where_, field_where_, DEFAULT_IS_FIELD, is_quick(source_)); }

	public static boolean insert_update(String source_, String field_col_, Object val_, String where_, String field_where_, boolean is_field_, boolean is_quick_) { return insert_update(source_, add_to_vals(source_, field_col_, val_, null, is_field_, is_quick_), where_, field_where_, is_quick_); }

	public static boolean insert_update(String source_, Object vals_, String where_, String field_where_) { return insert_update(source_, vals_, where_, field_where_, is_quick(source_)); }
	
	@SuppressWarnings("unchecked")
	public static boolean insert_update(String source_, Object vals_, String where_, String field_where_, boolean is_quick_)
	{	
		if (!vals_are_ok(source_, vals_, is_quick_)) return false;
		
		if (is_quick_) db_quick.insert_update(source_, db_quick.get_col(source_, field_where_), (HashMap<String, String>)vals_, where_);
		else 
		{
			if (strings.are_equal(field_where_, db.FIELD_ID)) db.insert_update_id(source_, (HashMap<String, Object>)vals_, where_);
			else db.insert_update(source_, (HashMap<String, Object>)vals_, where_);
		}
		
		return db.is_ok(source_, is_quick_);
	}

	public static boolean delete(String source_, String where_)
	{
		db.delete(source_, where_);
		
		return db.is_ok(source_);
	}
	
	public static Object add_to_vals(String source_, String field_, Object val_, Object vals_) { return add_to_vals(source_, field_, val_, vals_, DEFAULT_IS_FIELD, is_quick(source_)); }
	
	@SuppressWarnings("unchecked")
	public static Object add_to_vals(String source_, String field_col_, Object val_, Object vals_, boolean is_field_, boolean is_quick_) 
	{ 
		Object output = null;
		
		String field_col = get_field_quick_col(source_, field_col_, is_field_, is_quick_);

		if (is_quick_)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			vals.put(field_col, accessory.db.adapt_input(val_));

			output = new HashMap<String, String>(vals);
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
			vals.put(field_col, val_);
			
			output = new HashMap<String, Object>(vals);
		}
		
		return output;
	}
	
	public static Object get_from_vals(String source_, String field_col_, Object vals_) { return get_from_vals(source_, field_col_, vals_, DEFAULT_IS_FIELD, is_quick(source_)); }
	
	@SuppressWarnings("unchecked")
	public static Object get_from_vals(String source_, String field_col_, Object vals_, boolean is_field_, boolean is_quick_) 
	{ 
		Object output = null;
		if (!vals_are_ok(source_, vals_, is_quick_)) return output;
		
		if (is_quick_)
		{
			HashMap<String, String> vals = arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			
			if (vals.containsKey(field_col_)) output = vals.get(field_col_);
		}
		else
		{
			HashMap<String, Object> vals = arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
		
			if (vals.containsKey(field_col_)) output = vals.get(field_col_);				
		}
		
		return output;
	}
	
	public static Object get_input(String source_, Object input_) { return get_input(source_, input_, is_quick(source_)); }

	public static Object get_input(String source_, Object input_, boolean is_quick_) { return (is_quick_ ? db.adapt_input(input_) : input_); }

	public static double adapt_number(double val_, int max_) { return adapt_number(val_, max_, false); }
	
	public static String adapt_string(String val_, int max_) 
	{
		int max = get_max_length(max_);
		
		return (strings.get_length(val_) <= max ? val_ : strings.truncate(val_, max)); 
	}

	public static double adapt_number(double val_, int max_, boolean is_negative_)
	{
		double output = numbers.round(val_);
		if (max_ <= WRONG_MAX_SIZE) return output;
		
		double max = get_max_val(max_);
		double min = -1 * max;
		
		if (is_negative_)
		{
			if (output < min) output = min;
		}
		else if (output <= 0.0) output = 0.0;
			
		if (output > max) output = max;
		
		return output;
	}
	
	public static double get_max_val(int max_) { return (max_ <= WRONG_MAX_SIZE ? WRONG_MAX_VAL : (Math.pow(10.0, ((double)max_ + 1.0)) - 1.0)); }
	
	public static int get_max_length(int max_) { return (max_ <= WRONG_MAX_SIZE ? WRONG_MAX_LENGTH : max_); }

	public static boolean vals_are_ok(String source_, Object vals_) { return vals_are_ok(source_, vals_, is_quick(source_)); }

	public static boolean vals_are_ok(String source_, Object vals_, boolean is_quick_) { return (arrays.is_ok(vals_) && (is_quick_ ? db_quick.input_is_ok(vals_) : db.input_is_ok(vals_))); }

	public static String[] get_all_sources_inbuilt() { return _alls.DB_SOURCES_INBUILT; }

	public static Object get_val(String source_, Object val_) { return (is_quick(source_) ? db.adapt_input(val_) : val_); }

	public static String get_where(String source_, String field_, String val_) { return (db.get_variable(db_quick.get_col(source_, field_)) + "=" + db.get_value(val_)); }

	public static String join_wheres(String where1_, String where2_) { return join_wheres(where1_, where2_, db_where.LINK_AND); }

	public static String join_wheres(String where1_, String where2_, String link_) { return db_where.join(where1_, where2_, link_); }

	public static db_field get_field_decimal_tiny() { return get_field_decimal(3); }

	public static db_field get_field_decimal() { return get_field_decimal(DEFAULT_SIZE_DECIMAL); }
	
	public static db_field get_field_decimal(int size_) { return new db_field(data.DECIMAL, size_, numbers.DEFAULT_DECIMALS); }
	
	public static db_field get_field_tiny() { return get_field_tiny(false); }
	
	public static db_field get_field_tiny(boolean is_unique_) { return (is_unique_ ? new db_field(data.TINYINT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, db_field.WRONG_DEFAULT, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.TINYINT)); }
	
	public static db_field get_field_error() { return get_field_string(MAX_SIZE_ERROR); }
	
	public static db_field get_field_int() { return get_field_int(false); }
	
	public static db_field get_field_int(boolean is_unique_) { return new db_field(data.INT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, db_field.DEFAULT_DEFAULT, (is_unique_ ? new String[] { db_field.KEY_UNIQUE } : null)); }
	
	public static db_field get_field_string() { return get_field_string(DEFAULT_SIZE_STRING); }
	
	public static db_field get_field_string(int size_) { return get_field_string(size_, false); }
	
	public static db_field get_field_string(boolean is_unique_) { return get_field_string(DEFAULT_SIZE_STRING, is_unique_); }
	
	public static db_field get_field_string(int size_, boolean is_unique_) { return get_field_string(size_, is_unique_, null); }
	
	public static db_field get_field_string(int size_, boolean is_unique_, String default_) { return new db_field(data.STRING, size_, db_field.WRONG_DECIMALS, (strings.is_ok(default_) ? default_ : db_field.WRONG_DEFAULT), (is_unique_ ? new String[] { db_field.KEY_UNIQUE } : null)); }
	
	public static db_field get_field_string_big() { return new db_field(data.STRING_BIG, 0, db_field.WRONG_DECIMALS, null, null); }

	public static db_field get_field_is_enc() { return get_field_boolean(false); }
	
	public static db_field get_field_boolean(boolean default_) { return new db_field(data.BOOLEAN, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, default_, null); }

	public static db_field get_field_time(String format_) { return new db_field(data.STRING, dates.get_length(format_), db_field.WRONG_DECIMALS, dates.get_default(format_), null); }
	
	public static db_field get_field_date(String format_) { return new db_field(data.STRING, dates.get_length(format_), db_field.WRONG_DECIMALS, dates.get_default(format_), null); }

	static String[] populate_all_sources_inbuilt() { return new String[] { db_tests.SOURCE, db_credentials.SOURCE, db_crypto.SOURCE, db_info.SOURCE }; }

	static void populate_is_quick_ini()
	{
		if (!DEFAULT_IS_QUICK) return;
		
		for (String source: db.SOURCES) 
		{
			if (!IS_QUICK.contains(source)) IS_QUICK.add(source);
		}
	}
}