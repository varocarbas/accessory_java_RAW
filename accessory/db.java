package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db 
{
	public static final String FIELD_ID = types.CONFIG_DB_DEFAULT_FIELD_ID;
	public static final String FIELD_TIMESTAMP = types.CONFIG_DB_DEFAULT_FIELD_TIMESTAMP;
	
	public static final String MAX_POOL = types.CONFIG_DB_MAX_POOL;
	public static final String NAME = types.CONFIG_DB_NAME;
	public static final String HOST = types.CONFIG_DB_HOST;
	public static final String USERNAME = types.CONFIG_CREDENTIALS_FILE_USERNAME;
	public static final String PASSWORD = types.CONFIG_CREDENTIALS_FILE_PASSWORD;
	public static final String USER = types.CONFIG_DB_CREDENTIALS_USER;
	public static final String CREDENTIALS_USERNAME = USERNAME;
	public static final String CREDENTIALS_PASSWORD = PASSWORD;
	public static final String CREDENTIALS_USER = USER;
	public static final String CREDENTIALS_ENCRYPTED = types.CONFIG_DB_CREDENTIALS_ENCRYPTED;
	public static final String TYPE = types.CONFIG_DB_TYPE;
	public static final String MYSQL = types.CONFIG_DB_TYPE_MYSQL;
	public static final String TYPE_MYSQL = MYSQL;
	public static final String SETUP = types.CONFIG_DB_SETUP;
	
	public static final String SELECT = types.DB_QUERY_SELECT;
	public static final String INSERT = types.DB_QUERY_INSERT;
	public static final String UPDATE = types.DB_QUERY_UPDATE;
	public static final String DELETE = types.DB_QUERY_DELETE;	
	public static final String TABLE_EXISTS = types.DB_QUERY_TABLE_EXISTS;
	public static final String TABLE_CREATE = types.DB_QUERY_TABLE_CREATE;
	public static final String TABLE_DROP = types.DB_QUERY_TABLE_DROP;
	public static final String TABLE_TRUNCATE = types.DB_QUERY_TABLE_TRUNCATE;
	public static final String QUERY_SELECT = SELECT;
	public static final String QUERY_INSERT = INSERT;
	public static final String QUERY_UPDATE = UPDATE;
	public static final String QUERY_DELETE = DELETE;	
	public static final String QUERY_TABLE_EXISTS = TABLE_EXISTS;
	public static final String QUERY_TABLE_CREATE = TABLE_CREATE;
	public static final String QUERY_TABLE_DROP = TABLE_DROP;
	public static final String QUERY_TABLE_TRUNCATE = TABLE_TRUNCATE;
	
	public static final String DEFAULT_TYPE = _defaults.DB_TYPE;
	public static final String DEFAULT_MAX_POOL = _defaults.DB_MAX_POOL;
	public static final String DEFAULT_NAME = _defaults.DB_NAME;
	public static final String DEFAULT_HOST = _defaults.DB_HOST;
	public static final String DEFAULT_USER = _defaults.DB_USER;
	public static final String DEFAULT_USERNAME = _defaults.DB_CREDENTIALS_USERNAME;
	public static final String DEFAULT_PASSWORD = _defaults.DB_CREDENTIALS_PASSWORD;
	
	public static boolean _is_ok = false;
	public static String _cur_source = strings.DEFAULT;

	//--- Populated via the corresponding db_ini method (e.g., load_sources()).
	static HashMap<String, HashMap<String, db_field>> _sources = new HashMap<String, HashMap<String, db_field>>();
	private static HashMap<String, HashMap<String, Object>> _source_setups = new HashMap<String, HashMap<String, Object>>();
	//---
	
	static { ini.load(); }
	
	public static boolean update_db(String db_name_, String host_) { return update_conn_info(null, db_name_, host_, false); }
	
	public static boolean update_conn_info(String user_, String database_, String host_, boolean encrypted_)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		
		if (strings.is_ok(user_)) params.put(USER, user_);
		if (strings.is_ok(database_)) params.put(NAME, database_);
		if (strings.is_ok(host_)) params.put(HOST, host_); 
		params.put(CREDENTIALS_ENCRYPTED, strings.from_boolean(encrypted_));
		
		update_conn_info(params);

		return true;
	}
	
	public static boolean update_conn_info(String username_, String password_, String database_, String host_)
	{
		HashMap<String, String> params = new HashMap<String, String>();

		if (strings.is_ok(username_)) params.put(db.USERNAME, username_);
		if (password_ != null) params.put(db.PASSWORD, password_);
		if (strings.is_ok(database_)) params.put(db.NAME, database_);
		if (strings.is_ok(host_)) params.put(db.HOST, host_);
		
		return update_conn_info(params);
	}

	public static String select_one_string(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (String)db_queries.select_one_common(source_, field_, wheres_, orders_, data.STRING);
	}
	
	public static double select_one_decimal(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (double)db_queries.select_one_common(source_, field_, wheres_, orders_, data.DECIMAL);
	}
	
	public static long select_one_long(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (long)db_queries.select_one_common(source_, field_, wheres_, orders_, data.LONG);
	}
	
	public static int select_one_int(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (int)db_queries.select_one_common(source_, field_, wheres_, orders_, data.INT);
	}
	
	public static HashMap<String, String> select_one(String source_, String[] fields_, db_where[] wheres_, db_order[] orders_)
	{
		ArrayList<HashMap<String, String>> temp = select(source_, fields_, wheres_, 1, orders_);
		
		return (arrays.is_ok(temp) ? temp.get(0) : null);
	}
	
	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, db_where[] wheres_, int max_rows_, db_order[] orders_)
	{	
		return select(source_, fields_, db_where.to_string(wheres_), max_rows_, db_order.to_string(orders_));
	}
	
	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, String where_cols_, int max_rows_, String order_cols_)
	{	
		return db_queries.select(source_, fields_, where_cols_, max_rows_, order_cols_);
	}
	
	public static <x> void insert(String source_, HashMap<String, x> vals_raw_) { db_queries.insert(source_, vals_raw_); }
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_) { update(source_, vals_raw_, db_where.to_string(wheres_)); }
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_) { db_queries.update(source_, vals_raw_, where_cols_); }
	
	public static void delete(String source_, db_where[] wheres_) { delete(source_, db_where.to_string(wheres_)); }
	
	public static void delete(String source_, String where_cols_) { db_queries.delete(source_, where_cols_); }

	public static boolean table_exists(String source_) { return db_queries.table_exists(source_); }
	
	public static void create_table(String source_, boolean drop_it_) { create_table(source_, get_source_fields(source_), drop_it_); }
	
	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_) { db_queries.create_table(source_, fields_, drop_it_); }
	
	public static void drop_table(String source_) { db_queries.drop_table(source_); }
	
	public static void truncate_table(String source_) { db_queries.truncate_table(source_); }

	public static ArrayList<HashMap<String, String>> execute_query(String query_) { return db_queries.execute_query(query_); }

	static parent_db get_current_instance()
	{
		parent_db instance = get_instance(_cur_source);
		
		return (instance != null ? instance : db.get_instance_ini(DEFAULT_TYPE));
	}
	
	public static String get_value(String input_)
	{
		return get_current_instance().get_value(input_);
	}

	public static String get_variable_table(String source_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return strings.DEFAULT;

		return get_variable(get_table(source));
	}
	
	public static String get_variable_col(String source_, String col_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return strings.DEFAULT;

		return get_variable(get_col(source, col_));
	}
	
	public static String get_variable(String input_)
	{
		return get_current_instance().get_variable(input_);	
	} 

	public static HashMap<String, Object> get_data_type(String data_type_)
	{
		return get_current_instance().get_data_type(data_type_); 
	}

	public static int get_default_size(String type_)
	{
		return get_current_instance().get_default_size(type_);
	}

	public static int get_max_size(String type_)
	{
		return get_current_instance().get_max_size(type_);
	}
	
	public static boolean source_is_ok(String source_)
	{
		return (strings.is_ok(check_source(source_)));
	}
	
	public static String check_source(String source_)
	{
		if (!arrays.is_ok(_sources)) 
		{
			_cur_source = strings.DEFAULT;
			
			return _cur_source;
		}
		
		if (!strings.is_ok(_cur_source) || !_sources.containsKey(_cur_source)) _cur_source = strings.DEFAULT;
		
		String source = source_;
		if (!strings.is_ok(source) || !_sources.containsKey(source)) source = strings.DEFAULT;
		
		if (!strings.is_ok(source) && strings.is_ok(_cur_source)) source = _cur_source;
		
		return source;
	}

	public static boolean sources_are_equal(String source1_, String source2_)
	{
		return generic.are_equal(check_source(source1_), check_source(source2_));
	}
	
	public static boolean field_is_ok(String source_, String field_)
	{
		return strings.is_ok(get_col(source_, field_));
	}
	
	public static boolean add_source_ini(String source_, HashMap<String, db_field> fields_, HashMap<String, Object> info_)
	{
		if (!arrays.is_ok(_sources)) _sources = new HashMap<String, HashMap<String, db_field>>();
		if (!strings.is_ok(source_) || _sources.containsKey(source_)) return false;
		
		HashMap<String, db_field> fields = new HashMap<String, db_field>();
		
		for (Entry<String, db_field> item: fields_.entrySet())
		{
			db_field field = db_field.adapt(new db_field(item.getValue()));			
			if (!field._is_ok)
			{
				manage_error(types.ERROR_DB_FIELD, null, null, field.toString());
				
				return false;
			}
			
			fields.put(item.getKey(), field);
		}
		
		_sources.put(source_, fields);
	
		if (!arrays.is_ok(info_))
		{
			manage_error(types.ERROR_DB_SOURCE, null, null, "Wrong info for source " + source_);
			
			return false;
		}
	
		String source = check_source(source_);
		if (!strings.is_ok(source) || !arrays.is_ok(info_)) return false;
		
		if (!arrays.is_ok(_source_setups)) _source_setups = new HashMap<String, HashMap<String, Object>>();
		_source_setups.put(source, (HashMap<String, Object>)arrays.get_new(info_));

		return true;
	}
	
	public static String get_db(String source_)
	{
		String source = check_source(source_);
		
		return ((strings.is_ok(source) && _source_setups.containsKey(source)) ? (String)_source_setups.get(source).get(types.CONFIG_DB) : strings.DEFAULT);
	}
	
	public static String get_db_name(String source_)
	{
		String source = check_source(source_);
		
		return ((strings.is_ok(source) && _source_setups.containsKey(source)) ? (String)_source_setups.get(source).get(types.CONFIG_DB_NAME) : strings.DEFAULT);
	}
	
	public static String get_setup(String source_)
	{
		String source = check_source(source_);
		
		return ((strings.is_ok(source) && _source_setups.containsKey(source)) ? (String)_source_setups.get(source).get(types.CONFIG_DB_SETUP) : strings.DEFAULT);
	}
	
	public static parent_db get_instance(String source_)
	{
		String source = check_source(source_);
		
		return ((strings.is_ok(source) && _source_setups.containsKey(source)) ? (parent_db)_source_setups.get(source).get(generic.INSTANCE) : null);
	}
	
	public static HashMap<String, db_field> get_source_fields(String source_)
	{
		String source = check_source(source_);

		return (strings.is_ok(source) ? _sources.get(source) : null);
	}
	
	public static String get_col(String source_, String field_)
	{
		return config.get(get_db(source_), field_);
	}

	public static String get_field(String source_, String field_)
	{
		return config.get(get_db(source_), field_);
	}
	
	public static String get_table(String source_)
	{
		String source = check_source(source_); 

		return config.get(get_db(source), source);
	}
	
	public static boolean update_table(String source_, String table_)
	{	
		String source = check_source(source_); 

		return ((!strings.is_ok(source) || !strings.is_ok(table_)) ? false : config.update(get_db(source), source, table_));
	}
	
	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, double val_)
	{
		String source = check_source(source_);
		
		return adapt_inputs_input(source, old_, field_, val_);
	}

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, int val_)
	{
		String source = check_source(source_);
		
		return adapt_inputs_input(source, old_, field_, val_);
	}

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, String val_)
	{
		String source = check_source(source_);
		
		return adapt_inputs_input(source, old_, field_, val_);
	}
	
	public static <x> HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, HashMap<String, x> new_)
	{
		String source = check_source(source_);
		if (!strings.is_ok(source) || !arrays.is_ok(new_)) return null;

		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields)) return null;

		HashMap<String, String> output = (arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>());
		
		for (Entry<String, x> item: new_.entrySet())
		{
			output = adapt_input(source, output, item.getKey(), item.getValue(), fields);
			if (!arrays.is_ok(output)) return null;
		}

		return output;
	}

	public static <x> HashMap<String, String> adapt_input(String source_, HashMap<String, String> sofar_, String field_, x val_, HashMap<String, db_field> fields_)
	{
		HashMap<String, String> output = new HashMap<String, String>(sofar_);

		String source = check_source(source_);
		
		String id = field_;
		if (!fields_.containsKey(id)) return null;
	
		db_field field = arrays.get_value(fields_, id);
		if (!generic.is_ok(field) || !db_field.complies(val_, field)) return null;

		String val2 = strings.DEFAULT;
		if (data.is_number(field._type)) val2 = strings.to_string(val_);
		else val2 = sanitise_string(strings.to_string(val_));
		
		if (!strings.is_ok(val2)) return null;

		String col = get_col(source, id);
		if (!strings.is_ok(col)) return null;

		output.put(col, val2);
		
		return output;
	}
	
	public static String sanitise_string(String input_) { return get_current_instance().sanitise_string(input_); }

	public static String check_type(String input_) { return types.check_type(input_, types.get_subtypes(types.DB_QUERY), types.ACTIONS_ADD, types.DB_QUERY); }

	public static String get_id_encrypted(String user_) { return credentials.get_encryption_id(get_id_encrypted_type(), user_); }
	
	public static String get_id_encrypted_type() { return types.remove_type(get_current_type(), TYPE); }
	
	static void update_is_ok(boolean _is_ok) { get_current_instance().update_is_ok(_is_ok); }
	
	static HashMap<String, String> get_credentials()
	{
		HashMap<String, String> output = new HashMap<String, String>();
		
		String setup = get_current_setup();
		
		String user = config.get(setup, USER);
		String username = config.get(setup, USERNAME);
		String password = config.get(setup, PASSWORD);
		boolean encrypted = strings.to_boolean(config.get(setup, CREDENTIALS_ENCRYPTED));

		if (strings.is_ok(username) && password != null) output = credentials.get_username_password(username, password);
		else if (strings.is_ok(user))
		{
			output = credentials.get_username_password(get_id_encrypted_type(), user, encrypted, types.CONFIG_CREDENTIALS_WHERE_FILE);
		}

		return output;
	}

	static String get_host() { return config.get(get_current_setup(), HOST); }

	static String get_max_pool() { return config.get(get_current_setup(), MAX_POOL); }
	
	static String get_name() { return config.get(get_current_setup(), NAME); }

	static String get_current_db()
	{
		String db = get_db(_cur_source);
		
		return (strings.is_ok(db) ? db : types.CONFIG_DB);
	}
	
	static String get_current_setup()
	{
		String setup = get_setup(_cur_source);

		return (strings.is_ok(setup) ? setup : types.CONFIG_DB_SETUP_DEFAULT);
	}

	static void manage_error(String type_, String query_, Exception e_, String message_)
	{
		update_is_ok(false);

		errors.manage_db(type_, query_, e_, message_);
	}
	
	static boolean query_returns_data(String type_)
	{
		String type = types.check_type(type_, types.get_subtypes(types.DB_QUERY));
		if (!strings.is_ok(type)) return false;
		
		String[] targets = new String[] { SELECT, TABLE_EXISTS };
		
		for (String target: targets)
		{
			if (target.equals(type)) return true;
		}
		
		return false;
	}
	
	static String check_source_error(String source_)
	{
		update_is_ok(true);
		
		String source = check_source(source_);
		if (!strings.is_ok(source)) manage_error(types.ERROR_DB_SOURCE, null, null, null);
		
		return source;
	}
	
	static <x> HashMap<String, String> check_vals_error(String source_, HashMap<String, x> vals_raw_)
	{
		HashMap<String, String> vals = adapt_inputs(source_, null, vals_raw_);
		if (!arrays.is_ok(vals)) manage_error(types.ERROR_DB_VALS, null, null, null);
		
		return vals;
	}

	static parent_db get_instance_ini(String type_) 
	{ 
		parent_db output = null;
		
		String type = types.check_type(type_, types.get_subtypes(TYPE));
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;
		
		if (type.equals(MYSQL)) output = new db_mysql();
		
		return output;
	}

	private static boolean update_conn_info(HashMap<String, String> params_)
	{		
		String error = "";

		HashMap<String, Boolean> output = (arrays.is_ok(params_) ? config.update_db_conn_info(get_current_setup(), params_) : null);
		
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
		
		error = (error.equals("") ? "ERROR" : "WRONG " + error);
		
		manage_error(types.ERROR_DB_INFO, null, null, error);
		
		return false;
	}

	private static <x> HashMap<String, String> adapt_inputs_input(String source_, HashMap<String, String> old_, String field_, x val_)
	{
		String source = check_source(source_);
		
		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields) || !strings.is_ok(field_)) return null;

		return adapt_input(source, ((arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>())), field_, val_, fields);
	}
	
	private static String get_current_type()
	{
		String type = config.get(get_current_setup(), TYPE);
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;
		
		return type;
	}
}