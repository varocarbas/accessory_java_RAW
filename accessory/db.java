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

	public static boolean _is_ok = false;
	public static String _cur_source = strings.DEFAULT;
	
	//--- Populated via the corresponding db_ini method (e.g., load_sources()).
	static HashMap<String, HashMap<String, db_field>> _sources = new HashMap<String, HashMap<String, db_field>>();
	
	private static HashMap<String, String> _source_mains = new HashMap<String, String>();
	//------
	
	static { ini.load(); }

	public static boolean update_conn_info(String username_, String password_, String database_, String host_)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		if (strings.is_ok(username_)) params.put(types._CONFIG_DB_CREDENTIALS_USERNAME, username_);
		if (password_ != null) params.put(types._CONFIG_DB_CREDENTIALS_PASSWORD, password_);
		if (strings.is_ok(database_)) params.put(types._CONFIG_DB_NAME, database_);
		if (strings.is_ok(host_)) params.put(types._CONFIG_DB_HOST, host_);
		
		HashMap<String, Boolean> temp = update_conn_info(params);
		
		String error = "";
		
		for (Entry<String, Boolean> item: temp.entrySet())
		{
			if (!error.equals("")) error += ",";
			if (!item.getValue()) error += item.getKey();
		}
		
		if (error.equals("")) return true;
		
		error = "WRONG " + error;
		
		manage_error(types.ERROR_DB_INFO, null, null, error);
		
		return false;
	}

	public static HashMap<String, Boolean> update_conn_info(HashMap<String, String> params_)
	{
		return _config.update_db_conn_info(params_, _config.get_db(types._CONFIG_DB_SETUP));
	}

	public static String select_one_string(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (String)db_queries.select_one_common(source_, field_, wheres_, orders_, types.DATA_STRING);
	}
	
	public static double select_one_decimal(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (double)db_queries.select_one_common(source_, field_, wheres_, orders_, types.DATA_DECIMAL);
	}
	
	public static long select_one_long(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (long)db_queries.select_one_common(source_, field_, wheres_, orders_, types.DATA_LONG);
	}
	
	public static int select_one_int(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (int)db_queries.select_one_common(source_, field_, wheres_, orders_, types.DATA_INT);
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
		String value = strings.DEFAULT;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			value = db_mysql.get_value(input_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);

		return value;
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
		String variable = strings.DEFAULT;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			variable = db_mysql.get_variable(input_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return variable; 	
	} 

	public static HashMap<String, Object> get_data_type(String data_type_)
	{
		HashMap<String, Object> output = null;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = db_mysql.get_data_type(data_type_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output; 
	}

	public static int get_default_size(String type_)
	{
		int output = 0;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = db_mysql.get_default_size(type_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output; 
	}

	public static int get_max_size(String type_)
	{
		int output = 0;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = db_mysql.get_max_size(type_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output; 
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
		
		String source = types.check_aliases(source_);
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

		fields.put
		(
			types._CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, new db_field
			(
				types.DATA_TIMESTAMP, new String[] { types.DB_FIELD_FURTHER_TIMESTAMP }
			)
		);
		
		fields.put
		(
			types._CONFIG_DB_FIELDS_DEFAULT_ID, new db_field
			(
				types.DATA_INT, new String[] 
				{ 
					types.DB_FIELD_FURTHER_KEY_PRIMARY, types.DB_FIELD_FURTHER_AUTO_INCREMENT 
				}
			)
		);

		return fields;
	}
	
	public static String get_col(String source_, String field_)
	{
		String source = check_source(source_); 
		String field = types.check_aliases(field_);

		return _config.get(get_source_main(source), field);
	}

	public static String get_field(String source_, String field_)
	{
		String source = check_source(source_); 
		String field = types.check_aliases(field_);
		
		return _config.get(get_source_main(source), field);
	}
	
	public static String get_table(String source_)
	{
		String source = check_source(source_); 

		return _config.get(get_source_main(source), source);
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
		
		String id = types.check_aliases(field_);
		if (!fields_.containsKey(id)) return null;
	
		db_field field = arrays.get_value(fields_, id);
		if (!generic.is_ok(field) || !db_field.complies(val_, field)) return null;

		String val2 = strings.DEFAULT;
		if (data.is_numeric(field._type)) val2 = strings.to_string(val_);
		else val2 = sanitise_string(strings.to_string(val_));
		
		if (!strings.is_ok(val2)) return null;

		String col = get_col(source, id);
		if (!strings.is_ok(col)) return null;

		output.put(col, val2);
		
		return output;
	}
	
	public static String sanitise_string(String input_)
	{
		String output = input_;
		if (!strings.is_ok(output)) return output;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = db_mysql.sanitise_string(output);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);

		return output; 
	}

	public static String check_type(String input_)
	{
		return types.check_subtype
		(
			input_, types.get_subtypes(types.DB_QUERY, null), 
			types.ACTIONS_ADD, types.DB_QUERY
		);
	}
	
	static HashMap<String, String> get_credentials()
	{
		HashMap<String, String> output = new HashMap<String, String>();

		String user = _config.get(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_USER);
		String username = _config.get(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_CREDENTIALS_USERNAME);
		String password = _config.get(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_CREDENTIALS_PASSWORD);

		if (strings.is_ok(username) && strings.is_ok(password))
		{
			output.put(keys.USERNAME, username);
			output.put(keys.PASSWORD, password);
		}
		else if (strings.is_ok(user))
		{
			output = credentials.get
			(
				_config.get(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_CREDENTIALS_TYPE), user, 
				strings.to_boolean(_config.get(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_CREDENTIALS_ENCRYPTED)),
				_config.get(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_CREDENTIALS_WHERE)
			);
		}

		return output;
	}

	static void manage_error(String type_, String query_, Exception e_, String message_)
	{
		_is_ok = false;

		errors.manage_db(type_, query_, e_, message_);
	}
	
	static boolean query_returns_data(String type_)
	{
		String type = types.check_subtype(type_, types.get_subtypes(types.DB_QUERY, null), null, null);
		if (!strings.is_ok(type)) return false;
		
		String[] targets = new String[] { types.DB_QUERY_SELECT, types.DB_QUERY_TABLE_EXISTS };
		
		for (String target: targets)
		{
			if (target.equals(type)) return true;
		}
		
		return false;
	}
	
	private static <x> HashMap<String, String> adapt_inputs_input(String source_, HashMap<String, String> old_, String field_, x val_)
	{
		String source = check_source(source_);
		String field = types.check_aliases(field_);
		
		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields) || !strings.is_ok(field)) return null;

		return adapt_input(source, ((arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>())), field, val_, fields);
	}
	
	static String check_source_error(String source_)
	{
		_is_ok = true;
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
}