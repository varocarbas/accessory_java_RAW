package accessory;

public abstract class db_common 
{
	public static final int MAX_SIZE_KEY = 30;
	public static final int MAX_SIZE_VALUE = 2 * MAX_SIZE_KEY;
	public static final int MAX_SIZE_ERROR = 150;

	public static final int WRONG_MAX_SIZE = 0;
	public static final int WRONG_MAX_LENGTH = 0;
	public static final double WRONG_MAX_VAL = 0.0;
	
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final int DEFAULT_SIZE_DECIMAL = 7;
	public static final int DEFAULT_SIZE_STRING = 30;

	public static String[] populate_all_sources() { return new String[] { db_tests.SOURCE, db_credentials.SOURCE, db_info.SOURCE }; }

	public static String[] get_all_sources() { return _alls.DB_SOURCES; }

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
	
	public static db_field get_field_is_enc() { return get_field_boolean(false); }
	
	public static db_field get_field_boolean(boolean default_) { return new db_field(data.BOOLEAN, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, default_, null); }

	public static db_field get_field_time(String format_) { return new db_field(data.STRING, dates.get_length(format_), db_field.WRONG_DECIMALS, dates.get_default(format_), null); }
	
	public static db_field get_field_date(String format_) { return new db_field(data.STRING, dates.get_length(format_), db_field.WRONG_DECIMALS, dates.get_default(format_), null); }
	
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
	
	public static String get_field_col(String source_, String field_, boolean is_quick_) { return (is_quick_ ? get_col(source_, field_) : field_); }

	public static String get_col(String source_, String field_) { return db.get_col(source_, field_); }

	static void start()
	{
		db_tests.start();
		
		db_credentials.start();
	
		db_info.start();
	}
}