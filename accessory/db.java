package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db 
{
	//Sources/fields are constant, used internally, stored in memory (e.g., in _sources). 
	//Tables/cols are variable and refer to the actual DB.
	//The basic table/col structures, like number, type and size of columns, are assumed to be constant and defined via sources/fields.
	//On the other hand, table/col names are assumed to be variable and managed via tables/cols.
	//A valid source is one included within the sources currently considered by the library, in memory.
	//A valid table is one really existing in the given DB.
	
	public static final String FIELD_ID = types.CONFIG_DB_DEFAULT_FIELD_ID;
	public static final String FIELD_TIMESTAMP = types.CONFIG_DB_DEFAULT_FIELD_TIMESTAMP;
	
	public static final String MAX_POOL = types.CONFIG_DB_MAX_POOL;
	public static final String NAME = types.CONFIG_DB_NAME;
	public static final String HOST = types.CONFIG_DB_HOST;
	public static final String USER = types.CONFIG_DB_USER; //Only indirectly related to credentials (e.g., file name containing credentials).
	public static final String USERNAME = types.CONFIG_CREDENTIALS_FILE_USERNAME;
	public static final String PASSWORD = types.CONFIG_CREDENTIALS_FILE_PASSWORD;
	public static final String CREDENTIALS_USERNAME = USERNAME;
	public static final String CREDENTIALS_PASSWORD = PASSWORD;
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
	
	public static final String DEFAULT_COL_ID = _defaults.DB_DEFAULT_COL_ID;
	public static final String DEFAULT_COL_TIMESTAMP = _defaults.DB_DEFAULT_COL_TIMESTAMP;
	
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
	
	private static HashMap<String, String> _source_mains = new HashMap<String, String>();
	//---
	
	static { ini.load(); }
	
	public static boolean update_conn_info(String user_, String database_, String host_, boolean encrypted_)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		
		if (strings.is_ok(user_)) params.put(USER, user_);
		if (strings.is_ok(database_)) params.put(NAME, database_);
		if (strings.is_ok(host_)) params.put(HOST, host_); 
		//params.put(CREDENTIALS_ENCRYPTED, strings.from_boolean(encrypted_));
		
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
	
	public static <x> void insert(String source_, HashMap<String, x> vals_raw_)
	{
		db_queries.insert(source_, vals_raw_);
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_)
	{
		update(source_, vals_raw_, db_where.to_string(wheres_));
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_)
	{
		db_queries.update(source_, vals_raw_, where_cols_);
	}
	
	public static void delete(String source_, db_where[] wheres_)
	{
		delete(source_, db_where.to_string(wheres_));
	}
	
	public static void delete(String source_, String where_cols_)
	{
		db_queries.delete(source_, where_cols_);
	}

	public static boolean table_exists(String source_)
	{
		return db_queries.table_exists(source_);
	}
	
	public static void create_table(String source_, boolean drop_it_)
	{
		create_table(source_, get_source_fields(source_), drop_it_);
	}
	
	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_)
	{
		db_queries.create_table(source_, fields_, drop_it_);
	}
	
	public static void drop_table(String source_)
	{
		db_queries.drop_table(source_);		
	}
	
	public static void truncate_table(String source_)
	{
		db_queries.truncate_table(source_);
	}

	public static ArrayList<HashMap<String, String>> execute_query(String query_)
	{
		return db_queries.execute_query(query_);
	}

	public static String get_value(String input_)
	{
		return get_current_db().get_value(input_);
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
		return get_current_db().get_variable(input_);	
	} 

	public static HashMap<String, Object> get_data_type(String data_type_)
	{
		return get_current_db().get_data_type(data_type_); 
	}

	public static int get_default_size(String type_)
	{
		return get_current_db().get_default_size(type_);
	}

	public static int get_max_size(String type_)
	{
		return get_current_db().get_max_size(type_);
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
	
	public static void add_source(String source_, HashMap<String, db_field> fields_)
	{
		if (!arrays.is_ok(_sources)) _sources = new HashMap<String, HashMap<String, db_field>>();
		if (!strings.is_ok(source_) || _sources.containsKey(source_)) return;
		
		HashMap<String, db_field> fields = new HashMap<String, db_field>();
		
		for (Entry<String, db_field> item: fields_.entrySet())
		{
			db_field field = db_field.adapt(new db_field(item.getValue()));			
			if (!field._is_ok)
			{
				manage_error(types.ERROR_DB_FIELD, null, null, field.toString());
				
				return;
			}
			
			fields.put(item.getKey(), field);
		}
		
		_sources.put(source_, fields);
	}
	
	public static void add_source_main(String source_, String main_)
	{
		String source = check_source(source_);
		if (!strings.are_ok(new String[] { source, main_ })) return;
		
		if (!arrays.is_ok(_source_mains)) _source_mains = new HashMap<String, String>();
		
		_source_mains.put(source, main_);
	}
	
	public static String get_source_main(String source_)
	{
		String source = check_source(source_);
		
		return ((strings.is_ok(source) && _source_mains.containsKey(source)) ? _source_mains.get(source) : strings.DEFAULT);
	}
	
	public static HashMap<String, db_field> get_source_fields(String source_)
	{
		String source = check_source(source_);

		return (strings.is_ok(source) ? _sources.get(source) : null);
	}
	
	public static HashMap<String, db_field> get_default_fields()
	{
		HashMap<String, db_field> fields = new HashMap<String, db_field>();

		fields.put(FIELD_TIMESTAMP, new db_field(data.TIMESTAMP, new String[] { db_field.TIMESTAMP }));
		fields.put(FIELD_ID, new db_field(data.INT, new String[] { db_field.KEY_PRIMARY, db_field.AUTO_INCREMENT }));

		return fields;
	}
	
	public static String get_col(String source_, String field_)
	{
		String source = check_source(source_); 
	
		return config.get(get_source_main(source), field_);
	}

	public static String get_field(String source_, String field_)
	{
		String source = check_source(source_); 
		
		return config.get(get_source_main(source), field_);
	}
	
	public static String get_table(String source_)
	{
		String source = check_source(source_); 

		return config.get(get_source_main(source), source);
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
	
	public static String sanitise_string(String input_)
	{
		return get_current_db().sanitise_string(input_);
	}

	public static String check_type(String input_)
	{
		return types.check_subtype
		(
			input_, types.get_subtypes(types.DB_QUERY, null), 
			types.ACTIONS_ADD, types.DB_QUERY
		);
	}

	public static parent_db get_current_db()
	{	
		parent_db output = arrays.get_value(get_all_dbs(), get_current_setup());
		
		return (output != null ? output : instantiate_db(DEFAULT_TYPE));
	}
	
	static void update_is_ok(boolean _is_ok)
	{
		get_current_db().update_is_ok(_is_ok);
	}
	
	static HashMap<String, String> get_credentials()
	{
		HashMap<String, String> output = new HashMap<String, String>();
		
		String setup = db.get_current_setup();
		
		String user = config.get(setup, USER);
		String username = config.get(setup, USERNAME);
		String password = config.get(setup, PASSWORD);
		boolean encrypted = strings.to_boolean(config.get(setup, CREDENTIALS_ENCRYPTED));

		if (strings.is_ok(username) && password != null) output = credentials.get(username, password);
		else if (strings.is_ok(user))
		{
			output = credentials.get(types.remove_type(get_current_type(), TYPE), user, encrypted, types.CONFIG_CREDENTIALS_WHERE_FILE);
		}

		return output;
	}

	static String get_host()
	{
		return config.get(get_current_setup(), HOST);
	}

	static String get_name()
	{
		return config.get(get_current_setup(), NAME);
	}

	static String get_max_pool()
	{
		return config.get(get_current_setup(), MAX_POOL);
	}
	
	static void manage_error(String type_, String query_, Exception e_, String message_)
	{
		update_is_ok(false);

		errors.manage_db(type_, query_, e_, message_);
	}
	
	static boolean query_returns_data(String type_)
	{
		String type = types.check_subtype(type_, types.get_subtypes(types.DB_QUERY, null), null, null);
		if (!strings.is_ok(type)) return false;
		
		String[] targets = new String[] { SELECT, TABLE_EXISTS };
		
		for (String target: targets)
		{
			if (target.equals(type)) return true;
		}
		
		return false;
	}
	
	static parent_db instantiate_db(String type_)
	{
		parent_db output = null;
		
		String type = types.check_subtype(type_, types.get_subtypes(TYPE, null), null, null);
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;
		
		if (type.equals(MYSQL)) output = new db_mysql();
		
		return output;
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
	
	static HashMap<String, parent_db> populate_all_dbs()
	{
		HashMap<String, parent_db> all_db = new HashMap<String, parent_db>();
		
		String[] setups = new String[] { types.CONFIG_DB_SETUP_MAIN, types.CONFIG_DB_SETUP_LOGS };
		
		for (String setup: setups)
		{
			String type = config.get(setup, TYPE);
			
			all_db.put(setup, instantiate_db(type));
		}
		
		return all_db;
	}
	
	static HashMap<String, parent_db> get_all_dbs()
	{
		return _alls._db_dbs;
	}

	private static boolean update_conn_info(HashMap<String, String> params_)
	{		
		String error = "";
	
		HashMap<String, Boolean> output = (arrays.is_ok(params_) ? config.update_db_conn_info(params_, get_current_setup()) : null);

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
	
	private static String get_current_setup()
	{
		String type = SETUP;
		
		String output = config.get_db(type);
		if (!strings.is_ok(types.check_subtype(output, types.get_subtypes(type, null), null, null))) output = DEFAULT_TYPE;

		return output;
	}
	
	private static String get_current_type()
	{
		String type = config.get(get_current_setup(), TYPE);
		if (!strings.is_ok(type)) type = DEFAULT_TYPE;
		
		return type;
	}
}