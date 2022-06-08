package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db 
{
	public static final String NAME = types.CONFIG_DB_NAME;
	public static final String HOST = types.CONFIG_DB_SETUP_HOST;
	public static final String USERNAME = types.CONFIG_DB_SETUP_CREDENTIALS_USERNAME;
	public static final String PASSWORD = types.CONFIG_DB_SETUP_CREDENTIALS_PASSWORD;
	public static final String USER = types.CONFIG_DB_SETUP_CREDENTIALS_USER;
	public static final String CREDENTIALS_USERNAME = USERNAME;
	public static final String CREDENTIALS_PASSWORD = PASSWORD;
	public static final String CREDENTIALS_USER = USER;
	public static final String CREDENTIALS_ENCRYPTED = types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED;
	public static final String CREDENTIALS_MEMORY = types.CONFIG_DB_SETUP_CREDENTIALS_MEMORY;
	public static final String MAX_POOL = types.CONFIG_DB_SETUP_MAX_POOL;
	public static final String TYPE = types.CONFIG_DB_SETUP_TYPE;
	public static final String MYSQL = types.CONFIG_DB_SETUP_TYPE_MYSQL;
	public static final String TYPE_MYSQL = MYSQL;
	public static final String SETUP = types.CONFIG_DB_SETUP;

	public static final String FIELD_ID = types.CONFIG_DB_DEFAULT_FIELD_ID;
	public static final String FIELD_TIMESTAMP = types.CONFIG_DB_DEFAULT_FIELD_TIMESTAMP;

	public static final String QUERY_SELECT = types.DB_QUERY_SELECT;
	public static final String QUERY_SELECT_COUNT = types.DB_QUERY_SELECT_COUNT;
	public static final String QUERY_INSERT = types.DB_QUERY_INSERT;
	public static final String QUERY_UPDATE = types.DB_QUERY_UPDATE;
	public static final String QUERY_DELETE = types.DB_QUERY_DELETE;	
	public static final String QUERY_TABLE_EXISTS = types.DB_QUERY_TABLE_EXISTS;
	public static final String QUERY_TABLE_CREATE = types.DB_QUERY_TABLE_CREATE;
	public static final String QUERY_TABLE_DROP = types.DB_QUERY_TABLE_DROP;
	public static final String QUERY_TABLE_TRUNCATE = types.DB_QUERY_TABLE_TRUNCATE;

	public static final String ERROR_INFO = types.ERROR_DB_INFO;
	public static final String ERROR_CREDENTIALS = types.ERROR_DB_CREDENTIALS;
	public static final String ERROR_TYPE = types.ERROR_DB_TYPE;
	public static final String ERROR_CONN = types.ERROR_DB_CONN;
	public static final String ERROR_QUERY = types.ERROR_DB_QUERY;
	public static final String ERROR_SOURCE = types.ERROR_DB_SOURCE;
	public static final String ERROR_FIELD = types.ERROR_DB_FIELD;
	public static final String ERROR_VALS = types.ERROR_DB_VALS;

	public static final String DEFAULT_DB = _defaults.DB;
	public static final String DEFAULT_DB_NAME = _defaults.DB_NAME;
	public static final String DEFAULT_SOURCE = _defaults.DB_SOURCE;
	public static final String DEFAULT_SETUP = _defaults.DB_SETUP;
	public static final String DEFAULT_TYPE = _defaults.DB_TYPE;
	public static final String DEFAULT_MAX_POOL = _defaults.DB_MAX_POOL;
	public static final String DEFAULT_HOST = _defaults.DB_HOST;

	public static String _cur_source = strings.DEFAULT;

	public static String get_class_id() { return types.get_id(types.ID_DB); }

	static HashMap<String, HashMap<String, db_field>> _sources = new HashMap<String, HashMap<String, db_field>>();

	private static HashMap<String, HashMap<String, Object>> _source_setups = new HashMap<String, HashMap<String, Object>>();
	private static HashMap<String, String> _db_setups = new HashMap<String, String>();
	private static HashMap<String, String> _credentials = new HashMap<String, String>();

	public static boolean encrypt_credentials(String source_, String user_, String username_, String password_)  { return credentials.encrypt_username_password_file(get_type(source_), user_, username_, password_);  }

	public static boolean update_db(String db_, String db_name_) 
	{ 
		if (!types.is_config_db(db_) || !strings.is_ok(db_name_)) return manage_error("Wrong DB");

		return config.update(db_, NAME, db_name_);
	}

	public static boolean update_conn_info(String db_, String user_, String db_name_, String host_, boolean encrypted_)
	{
		if (strings.is_ok(db_name_)) 
		{
			if (!update_db(db_, db_name_)) return false;
		}

		HashMap<String, Object> vals = new HashMap<String, Object>();

		if (strings.is_ok(user_)) vals.put(USER, user_);
		if (strings.is_ok(host_)) vals.put(HOST, host_); 
		vals.put(CREDENTIALS_ENCRYPTED, encrypted_);

		return update_vals(get_setup_from_db(db_), vals);
	}

	public static boolean update_conn_info(String db_, String username_, String password_, String db_name_, String host_)
	{
		if (strings.is_ok(db_name_)) 
		{
			if (!update_db(db_, db_name_)) return false;
		}

		HashMap<String, Object> vals = new HashMap<String, Object>();

		if (strings.is_ok(username_)) vals.put(USERNAME, username_);
		if (password_ != null) vals.put(PASSWORD, password_);
		if (strings.is_ok(host_)) vals.put(HOST, host_);

		return update_vals(get_setup_from_db(db_), vals);
	}

	public static boolean update_setup(String db_, HashMap<String, Object> setup_vals_)
	{
		String db = config.check_type(db_);
		if (!strings.is_ok(db) || !config.vals_are_ok(setup_vals_, types.CONFIG_DB_SETUP)) return false;

		return update_vals(get_setup_from_db(db), setup_vals_);
	}

	public static boolean exists(String source_, db_where[] wheres_) { return arrays.is_ok(select_one(source_, null, wheres_, null)); }

	public static boolean exists(String source_, String where_cols_) { return arrays.is_ok(select_one(source_, null, where_cols_, null)); }

	public static String select_one_string(String source_, String field_) { return select_one_string(source_, field_, (String)null, null); }

	public static String select_one_string(String source_, String field_, db_where[] wheres_, db_order[] orders_) { return select_one_string(source_, field_, db_where.to_string(wheres_), db_order.to_string(orders_)); }

	public static String select_one_string(String source_, String field_, String where_cols_, String order_cols_) { return (String)db_queries.select_one_common(source_, field_, where_cols_, order_cols_, data.STRING_SMALL); }

	public static double select_one_decimal(String source_, String field_) { return select_one_decimal(source_, field_, (String)null, null); }

	public static double select_one_decimal(String source_, String field_, db_where[] wheres_, db_order[] orders_) { return select_one_decimal(source_, field_, db_where.to_string(wheres_), db_order.to_string(orders_)); }

	public static double select_one_decimal(String source_, String field_, String wheres_cols_, String orders_cols_) { return (double)db_queries.select_one_common(source_, field_, wheres_cols_, orders_cols_, data.DECIMAL); }

	public static long select_one_long(String source_, String field_) { return select_one_long(source_, field_, (String)null, null); }

	public static long select_one_long(String source_, String field_, db_where[] wheres_, db_order[] orders_) { return select_one_long(source_, field_, db_where.to_string(wheres_), db_order.to_string(orders_)); }

	public static long select_one_long(String source_, String field_, String wheres_cols_, String orders_cols_) { return (long)db_queries.select_one_common(source_, field_, wheres_cols_, orders_cols_, data.LONG); }

	public static int select_one_int(String source_, String field_) { return select_one_int(source_, field_, (String)null, null); }

	public static int select_one_int(String source_, String field_, db_where[] wheres_, db_order[] orders_) { return select_one_int(source_, field_, db_where.to_string(wheres_), db_order.to_string(orders_)); }

	public static int select_one_int(String source_, String field_, String wheres_cols_, String orders_cols_) { return (int)db_queries.select_one_common(source_, field_, wheres_cols_, orders_cols_, data.INT); }

	public static boolean select_one_boolean(String source_, String field_) { return select_one_boolean(source_, field_, (String)null, null); }

	public static boolean select_one_boolean(String source_, String field_, db_where[] wheres_, db_order[] orders_) { return select_one_boolean(source_, field_, db_where.to_string(wheres_), db_order.to_string(orders_)); }

	public static boolean select_one_boolean(String source_, String field_, String wheres_cols_, String orders_cols_) { return (boolean)db_queries.select_one_common(source_, field_, wheres_cols_, orders_cols_, data.BOOLEAN); }

	public static HashMap<String, String> select_one(String source_, String[] fields_, db_where[] wheres_, db_order[] orders_) { return db_queries.select_one(source_, fields_, wheres_, orders_); }

	public static HashMap<String, String> select_one(String source_, String[] fields_, String wheres_cols_, String orders_cols_) { return db_queries.select_one(source_, fields_, wheres_cols_, orders_cols_); }

	public static ArrayList<String> select_some_strings(String source_, String field_) { return select_some_strings(source_, field_, (String)null, 0, null); }

	public static ArrayList<String> select_some_strings(String source_, String field_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select_some_strings(source_, field_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	@SuppressWarnings("unchecked")
	public static ArrayList<String> select_some_strings(String source_, String field_, String where_cols_, int max_rows_, String order_cols_) { return (ArrayList<String>)db_queries.select_some_common(source_, field_, where_cols_, max_rows_, order_cols_, data.STRING_SMALL); }

	public static ArrayList<Double> select_some_decimals(String source_, String field_) { return select_some_decimals(source_, field_, (String)null, 0, null); }

	public static ArrayList<Double> select_some_decimals(String source_, String field_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select_some_decimals(source_, field_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	@SuppressWarnings("unchecked")
	public static ArrayList<Double> select_some_decimals(String source_, String field_, String where_cols_, int max_rows_, String order_cols_) { return (ArrayList<Double>)db_queries.select_some_common(source_, field_, where_cols_, max_rows_, order_cols_, data.DECIMAL); }

	public static ArrayList<Long> select_some_longs(String source_, String field_) { return select_some_longs(source_, field_, (String)null, 0, null); }

	public static ArrayList<Long> select_some_longs(String source_, String field_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select_some_longs(source_, field_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	@SuppressWarnings("unchecked")
	public static ArrayList<Long> select_some_longs(String source_, String field_, String where_cols_, int max_rows_, String order_cols_) { return (ArrayList<Long>)db_queries.select_some_common(source_, field_, where_cols_, max_rows_, order_cols_, data.LONG); }

	public static ArrayList<Integer> select_some_ints(String source_, String field_) { return select_some_ints(source_, field_, (String)null, 0, null); }

	public static ArrayList<Integer> select_some_ints(String source_, String field_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select_some_ints(source_, field_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> select_some_ints(String source_, String field_, String where_cols_, int max_rows_, String order_cols_) { return (ArrayList<Integer>)db_queries.select_some_common(source_, field_, where_cols_, max_rows_, order_cols_, data.INT); }

	public static ArrayList<Boolean> select_some_booleans(String source_, String field_) { return select_some_booleans(source_, field_, (String)null, 0, null); }

	public static ArrayList<Boolean> select_some_booleans(String source_, String field_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select_some_booleans(source_, field_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	@SuppressWarnings("unchecked")
	public static ArrayList<Boolean> select_some_booleans(String source_, String field_, String where_cols_, int max_rows_, String order_cols_) { return (ArrayList<Boolean>)db_queries.select_some_common(source_, field_, where_cols_, max_rows_, order_cols_, data.BOOLEAN); }

	public static ArrayList<HashMap<String, String>> select(String source_, db_where[] wheres_) { return select(source_, null, wheres_, 0, null); }

	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, db_where[] wheres_) { return select(source_, fields_, wheres_, 0, null); }

	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, db_where[] wheres_, int max_rows_, db_order[] orders_) { return select(source_, fields_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_)); }

	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, String where_cols_, int max_rows_, String order_cols_) { return db_queries.select(source_, fields_, where_cols_, max_rows_, order_cols_); }

	public static int select_count(String source_) { return db_queries.select_count(source_); }

	public static <x> void insert_update(String source_, HashMap<String, x> vals_raw_, db_where[] where_) { insert_update(source_, vals_raw_, db_where.to_string(where_)); }

	public static <x> void insert_update(String source_, HashMap<String, x> vals_raw_, String where_cols_) 
	{ 
		if (exists(source_, where_cols_)) update(source_, vals_raw_, where_cols_);
		else insert(source_, vals_raw_);
	}

	public static <x> void insert(String source_, HashMap<String, x> vals_raw_) { db_queries.insert(source_, vals_raw_); }
	
	//Use this method carefully! No data checks or field/col conversions are performed.
	public static void insert_quick(String source_, HashMap<String, String> vals_) { db_queries.insert_quick(source_, vals_); }

	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_) { update(source_, vals_raw_, db_where.to_string(wheres_)); }

	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_) { db_queries.update(source_, vals_raw_, where_cols_); }

	//Use this method carefully! No data checks or field/col conversions are performed.
	public static void update_quick(String source_, HashMap<String, String> vals_, String where_cols_) { db_queries.update_quick(source_, vals_, where_cols_); }

	public static void delete(String source_, db_where[] wheres_) { delete(source_, db_where.to_string(wheres_)); }

	public static void delete(String source_, String where_cols_) { db_queries.delete(source_, where_cols_); }

	public static boolean table_exists(String source_) { return db_queries.table_exists(source_); }

	public static void create_table(String source_, boolean drop_it_) { create_table(source_, get_source_fields(source_), drop_it_); }

	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_) { db_queries.create_table(source_, fields_, drop_it_); }

	public static void drop_table(String source_) { db_queries.drop_table(source_); }

	public static void truncate_table(String source_) { db_queries.truncate_table(source_); }

	public static ArrayList<HashMap<String, String>> execute_query(String query_) { return execute_query(get_current_source(), query_); }

	public static ArrayList<HashMap<String, String>> execute_query(String source_, String query_) { return db_queries.execute_query(get_valid_source(source_), query_); }

	public static String get_variable_table(String source_)
	{
		String source = check_source_error(source_);

		return (strings.is_ok(source) ? get_variable(source, get_table(source)) : strings.DEFAULT);
	}

	public static String get_variable_col(String source_, String col_)
	{
		String source = check_source_error(source_);

		return (strings.is_ok(source) ? get_variable(source, get_col(source, col_)) : strings.DEFAULT);
	}

	public static <x> db_where[] get_where(String source_, String field_, x val_, boolean check_inputs_) 
	{ 
		String source = source_;

		String field = field_;
		x val = val_;

		if (check_inputs_)
		{
			source = check_source(source_);
			if (!strings.is_ok(source) || !field_is_ok(source, field) || val == null) return null;
		}

		return new db_where[] { new db_where(source, field, val) }; 
	}

	public static String get_variable(String input_) { return get_variable(get_current_source(), input_); } 

	public static String get_variable(String source_, String input_) { return get_valid_instance(source_).get_variable(input_); } 

	public static String get_value(String input_) { return get_value(get_current_source(), input_); }

	public static String get_value(String source_, String input_) { return get_valid_instance(source_).get_value(input_); }

	public static HashMap<String, Object> get_data_type(String data_type_) { return get_data_type(get_current_source(), data_type_); }

	public static HashMap<String, Object> get_data_type(String source_, String data_type_) { return get_valid_instance(source_).get_data_type(data_type_); }

	public static long get_default_size(String type_) { return get_default_size(get_current_source(), type_); }

	public static long get_default_size(String source_, String type_) { return get_valid_instance(source_).get_default_size(type_); }

	public static long get_max_size(String type_) { return get_max_size(get_current_source(), type_); }

	public static long get_max_size(String source_, String type_) { return get_valid_instance(source_).get_max_size(type_); }

	public static double get_max_value(String type_) { return get_max_value(get_current_source(), type_); }

	public static double get_max_value(String source_, String type_) { return get_valid_instance(source_).get_max_value(type_); }

	public static boolean source_is_ok(String source_) { return (strings.is_ok(check_source(source_))); }

	public static String check_source(String source_)
	{
		if (!arrays.is_ok(_sources)) 
		{
			_cur_source = strings.DEFAULT;

			return _cur_source;
		}
		if (!strings.is_ok(_cur_source) || !_sources.containsKey(_cur_source)) _cur_source = strings.DEFAULT;

		String source = source_;
		if (!strings.is_ok(source)) source = strings.DEFAULT;
		else if (!_sources.containsKey(source))
		{
			String temp = strings.normalise(source);
			source = (_sources.containsKey(temp) ? temp : strings.DEFAULT);
		}
		if (!strings.is_ok(source) && strings.is_ok(_cur_source)) source = _cur_source;

		return source;
	}

	public static boolean sources_are_equal(String source1_, String source2_) { return generic.are_equal(check_source(source1_), check_source(source2_)); }

	public static boolean field_is_ok(String source_, String field_) { return (get_field(source_, field_) != null); }

	@SuppressWarnings("unchecked")
	public static db_field get_field(String source_, String field_) { return (db_field)arrays.get_value((HashMap<String, db_field>)arrays.get_value(_sources, check_source(source_)), field_); }

	@SuppressWarnings("unchecked")
	public static String check_field(String source_, String field_)
	{
		String output = strings.DEFAULT;
		if (!strings.is_ok(field_)) return output; 

		HashMap<String, db_field> fields = (HashMap<String, db_field>)arrays.get_value(_sources, check_source(source_));
		if (!arrays.is_ok(fields)) return output;

		for (Entry<String, db_field> item: fields.entrySet())
		{
			String field21 = item.getKey();
			String field22 = strings.normalise(field21);

			if (field_.equals(field21) || field_.equals(field22)) return (field_.equals(field21) ? field21 : field22);
		}

		return output;
	}

	public static boolean add_source_ini(String source_, HashMap<String, db_field> fields_, HashMap<String, Object> setup_vals_)
	{
		if (!arrays.is_ok(_sources)) _sources = new HashMap<String, HashMap<String, db_field>>();
		if (!strings.is_ok(source_)) return false;

		HashMap<String, db_field> fields = new HashMap<String, db_field>();

		for (Entry<String, db_field> item: fields_.entrySet())
		{
			db_field field = db_field.adapt(source_, new db_field(item.getValue()));			

			if (!field.is_ok())
			{
				manage_error(source_, ERROR_FIELD, null, null, field.toString());

				return false;
			}

			fields.put(item.getKey(), field);
		}

		_sources.put(source_, fields);

		if (!_ini_db.setup_vals_are_ok(setup_vals_))
		{
			manage_error(source_, ERROR_SOURCE, null, null, "Wrong setup vals for source " + source_);

			return false;
		}

		String setup = (String)setup_vals_.get(types.CONFIG_DB_SETUP);
		String db = (String)setup_vals_.get(types.CONFIG_DB);

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_DB, db);
		vals.put(types.CONFIG_DB_SETUP, setup);
		vals.put(types.CONFIG_DB_SETUP_TYPE, (String)setup_vals_.get(types.CONFIG_DB_SETUP_TYPE));
		String instance = _keys.get_key(types.WHAT_INSTANCE);
		vals.put(instance, setup_vals_.get(instance));

		if (!arrays.is_ok(_source_setups)) _source_setups = new HashMap<String, HashMap<String, Object>>();
		_source_setups.put(source_, vals);

		if (!arrays.is_ok(_db_setups)) _db_setups = new HashMap<String, String>();
		if (strings.is_ok(db) && !_db_setups.containsKey(source_)) _db_setups.put(db, setup);

		return true;
	}

	public static String get_setup_from_db(String db_)
	{
		String db = config.check_type(db_);

		return ((strings.is_ok(db) && _db_setups.containsKey(db)) ? _db_setups.get(db) : strings.DEFAULT);
	}

	public static String get_current_db() { return get_db(get_current_source()); }

	public static String get_db(String source_) { return (String)get_setup_common(get_valid_source(source_), types.CONFIG_DB); }

	public static String get_current_setup() { return get_setup(get_current_source()); }

	public static String get_setup(String source_) { return (String)get_setup_common(get_valid_source(source_), types.CONFIG_DB_SETUP); }

	public static String get_valid_type(String source_) 
	{ 
		String output = get_type(get_valid_source(source_));

		return (strings.is_ok(output) ? output : _defaults.DB_TYPE);
	}

	public static String get_current_type() { return get_type(get_current_source()); }

	public static String get_type(String source_) 
	{ 
		String source = check_source(source_);

		return (strings.is_ok(source) ? (String)get_setup_common(source, types.CONFIG_DB_SETUP_TYPE) : strings.DEFAULT); 
	}

	public static HashMap<String, db_field> get_source_fields(String source_)
	{
		String source = check_source(source_);

		return (strings.is_ok(source) ? _sources.get(source) : null);
	}

	public static String get_col(String source_, String field_) { return (String)config.get(get_db(source_), field_); }

	public static String get_table(String source_)
	{
		String source = check_source(source_); 

		return (String)config.get(get_db(source), source);
	}
	
	public static boolean change_table_name_queries(String source_, String name_)
	{	
		String source = check_source(source_); 

		return ((!strings.is_ok(source) || !strings.is_ok(name_)) ? false : config.update(get_db(source), source, name_));
	}
	
	public static boolean change_col_name_queries(String source_, String field_, String name_)
	{	
		String source = check_source(source_); 
		String field = check_field(source, field_);

		return ((!strings.is_ok(source) || !strings.is_ok(field) || !strings.is_ok(name_)) ? false : config.update(get_db(source), field, name_));
	}

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, double val_) { return adapt_inputs_input(check_source(source_), old_, field_, val_); }

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, int val_) { return adapt_inputs_input(check_source(source_), old_, field_, val_); }

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, String val_) { return adapt_inputs_input(check_source(source_), old_, field_, val_); }

	public static <x> HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, HashMap<String, x> new_)
	{
		String source = check_source(source_);
		if (!strings.is_ok(source) || !arrays.is_ok(new_)) return null;

		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields)) return null;

		HashMap<String, String> output = (arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>());

		for (Entry<String, x> item: new_.entrySet())
		{
			String key = item.getKey();
			if (key.equals(FIELD_TIMESTAMP) || key.equals(FIELD_ID)) continue;

			output = adapt_input(source, output, key, item.getValue(), fields);
			if (!arrays.is_ok(output)) return null;
		}

		return output;
	}

	public static <x> HashMap<String, String> adapt_input(String source_, HashMap<String, String> sofar_, String field_, x val_, HashMap<String, db_field> fields_)
	{
		String val2 = adapt_input(source_, (db_field)arrays.get_value(fields_, field_), val_);
		if (val2 == null) 
		{
			errors.update_temp("wrong field", strings.to_string(field_) + " (" + strings.to_string(val_) + ")");
			
			return null;
		}

		String col = get_col(source_, field_);
		if (!strings.is_ok(col)) return null;

		HashMap<String, String> output = new HashMap<String, String>(sofar_);
		output.put(col, val2);

		return output;
	}

	public static <x> String adapt_input(String source_, String field_, x val_) { return adapt_input(source_, get_field(source_, field_), val_); }

	public static <x> String adapt_input(String source_, db_field field_, x val_) 
	{
		if (!db_field.is_ok(field_) || val_ == null) return null;

		String source = check_source(source_);

		return (strings.is_ok(source) ? input_to_string(source, val_, field_.get_type(), false) : null); 
	}

	public static <x> String input_to_string(x val_, String data_type_) { return input_to_string(get_current_source(), val_, data_type_, true); }

	public static <x> String input_to_string(String source_, x val_, String data_type_, boolean check_)
	{
		String output = strings.DEFAULT;
		if (val_ == null) return output;

		String type = data.check_type(data_type_);
		if (!strings.is_ok(type)) return output;
		
		Object val0 = db_field.adapt_value(source_, val_, type, check_);
		if (val0 == null) return output;

		Object val = (data.is_boolean(type) ? generic.boolean_to_int((boolean)val0) : val0);
		output = strings.to_string(val);
		output = sanitise_string(source_, output);

		return output;
	}

	public static Object adapt_output(String source_, String field_, String val_) { return adapt_output(source_, get_field(source_, field_), val_); }

	public static Object adapt_output(String source_, db_field field_, String val_) 
	{
		if (!db_field.is_ok(field_) || !strings.is_ok(val_)) return null;

		String source = check_source(source_);

		return (strings.is_ok(source) ? output_to_object(source, val_, field_.get_type(), false) : null); 
	}

	public static Object output_to_object(String val_, String data_type_) { return output_to_object(get_current_source(), val_, data_type_, true); }

	public static Object output_to_object(String source_, String val_, String data_type_, boolean check_)
	{
		Object output = null;
		if (!strings.is_ok(val_)) return output;

		String type = data.check_type(data_type_);
		if (!strings.is_ok(type)) return output;
		
		if (type.equals(data.DECIMAL)) output = numbers.to_decimal(val_);
		else if (type.equals(data.LONG)) output = numbers.to_long(val_);
		else if (type.equals(data.INT) || type.equals(data.TINYINT)) output = numbers.to_int(val_);
		else if (data.is_boolean(type)) output = strings.to_boolean(val_);
		else output = val_;

		return output;
	}

	public static String sanitise_string(String input_) { return sanitise_string(get_current_source(), input_); }

	public static String sanitise_string(String source_, String input_) { return get_valid_instance(source_).sanitise_string(input_); }

	public static String check_type(String input_) { return types.check_type(input_, types.get_subtypes(types.DB_QUERY), types.ACTION_ADD, types.DB_QUERY); }

	public static String get_current_encryption_id() { return get_encryption_id(get_current_source()); }

	public static String get_encryption_id(String source_) { return types.remove_type(get_type(source_), TYPE); }

	public static String get_current_host() { return get_host(get_current_setup()); }

	public static String get_current_max_pool() { return get_max_pool(get_current_setup()); }

	public static String get_current_db_name() { return get_db_name(get_current_db()); }

	public static boolean is_ok() { return is_ok(get_current_source()); }

	public static boolean is_ok(String source_) { return get_valid_instance(source_).is_ok(); }

	static String[] populate_all_queries_data() { return new String[] { QUERY_SELECT, QUERY_SELECT_COUNT, QUERY_TABLE_EXISTS }; }

	static void is_ok(boolean is_ok_) { is_ok(get_current_source(), is_ok_); }

	static void is_ok(String source_, boolean is_ok_) { get_valid_instance(source_).is_ok(is_ok_); }

	static String get_host(String setup_) { return (String)config.get(setup_, HOST); }

	static String get_max_pool(String setup_) { return (String)config.get(setup_, MAX_POOL); }

	static String get_db_name(String db_) { return (String)config.get(db_, NAME); }

	static void manage_error(String source_, String type_, String query_, Exception e_, String message_)
	{
		is_ok(source_, false);

		HashMap<String, Object> info = new HashMap<String, Object>();
		if (strings.is_ok(query_)) info.put("query", query_);
		
		String setup = get_setup(source_);
		info.put(HOST, get_host(setup));
		info.put(NAME, get_db_name(get_db(source_)));
		info.put(USER, config.get(setup, types.CONFIG_DB_SETUP_CREDENTIALS_USER));

		errors.manage(type_, e_, info);
	}

	static boolean query_returns_data(String type_)
	{
		String type = types.check_type(type_, types.DB_QUERY);
		if (!strings.is_ok(type)) return false;

		for (String target: get_all_queries_data())
		{
			if (target.equals(type)) return true;
		}

		return false;
	}

	static String check_source_error(String source_)
	{
		is_ok(source_, true);

		String source = check_source(source_);

		if (!strings.is_ok(source)) manage_error(source, ERROR_SOURCE, null, null, null);
		else is_ok(source, true);

		return source;
	}

	static <x> HashMap<String, String> check_vals_error(String source_, HashMap<String, x> vals_raw_)
	{
		HashMap<String, String> vals = adapt_inputs(source_, null, vals_raw_);
		if (!arrays.is_ok(vals)) manage_error(source_, ERROR_VALS, null, null, null);

		return vals;
	}

	static parent_db get_instance_ini(String type_) 
	{ 
		parent_db output = null;

		String type = types.check_type(type_, TYPE);
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;

		if (type.equals(MYSQL)) output = new db_mysql();

		return output;
	}

	static HashMap<String, String> get_credentials(String source_)
	{
		if (arrays.is_ok(_credentials) && credentials_in_memory(source_)) return _credentials;
			
		String setup = get_setup(source_);

		String user = (String)config.get(setup, USER);
		String username = (String)config.get(setup, USERNAME);
		String password = (String)config.get(setup, PASSWORD);
		boolean encrypted = (boolean)config.get(setup, CREDENTIALS_ENCRYPTED);

		HashMap<String, String> temp = new HashMap<String, String>();
		if (strings.is_ok(username) && password != null) temp = credentials.get_username_password(username, password);
		else if (strings.is_ok(user)) temp = credentials.get_username_password(get_encryption_id(source_), user, encrypted, types.CONFIG_CREDENTIALS_WHERE_FILE);					
		
		if (!arrays.is_ok(temp)) return null;
		
		_credentials = new HashMap<String, String>(temp);

		return _credentials;
	}

	static parent_db get_valid_instance(String source_) 
	{ 
		String source = check_source(source_);
		parent_db instance = get_instance((strings.is_ok(source) ? source : get_current_source()));

		return (instance != null ? instance : get_instance_ini(get_type(source)));
	}

	static String get_select_count_col(String source_) { return get_valid_instance(source_).get_select_count_col(); }

	private static boolean credentials_in_memory(String source_) { return (boolean)config.get(get_setup(source_), CREDENTIALS_MEMORY); }
	
	private static String[] get_all_queries_data() { return _alls.DB_QUERIES_DATA; }

	private static parent_db get_instance(String source_) { return (parent_db)get_setup_common(get_valid_source(source_), _keys.INSTANCE); }

	private static boolean update_vals(String type_, HashMap<String, Object> vals_) { return manage_error((arrays.is_ok(vals_) ? config.update(type_, vals_) : null)); }

	private static String get_valid_source(String source_) 
	{ 
		String source = check_source(source_);

		return (strings.is_ok(source) ? source : get_current_source());
	}

	private static String get_current_source() 
	{ 
		String source = check_source(_cur_source);

		return (strings.is_ok(source) ? source : DEFAULT_SOURCE); 
	}

	private static Object get_setup_common(String source_, String type_)
	{
		String source = check_source(source_);

		return ((strings.is_ok(source) && _source_setups.containsKey(source)) ? _source_setups.get(source).get(type_) : null);
	}

	private static boolean manage_error(HashMap<String, Boolean> output)
	{
		String error = "";

		if (arrays.is_ok(output))
		{
			for (Entry<String, Boolean> item: output.entrySet())
			{
				if (!item.getValue()) 
				{
					if (!error.equals("")) error += ",";
					error += item.getKey();
				}
			}	
			if (error.equals("")) return true;
		}	

		return manage_error(error);
	}

	private static boolean manage_error(String message_)
	{
		String message = (!strings.is_ok(message_) ? "ERROR" : message_);

		manage_error(null, ERROR_INFO, null, null, message);

		return false;
	}

	private static <x> HashMap<String, String> adapt_inputs_input(String source_, HashMap<String, String> old_, String field_, x val_)
	{
		String source = check_source(source_);

		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields) || !strings.is_ok(field_)) return null;

		return adapt_input(source, ((arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>())), field_, val_, fields);
	}
}