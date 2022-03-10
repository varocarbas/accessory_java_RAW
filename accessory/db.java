package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class db 
{
	//Sources/fields are constant, used internally, stored in memory (e.g., in _sources). 
	//Tables/cols are variable and refer to the actual DB.
	//The basic table/col structures, like number, type and size of columns, are assumed to be constant and defined via sources/fields.
	//On the other hand, table/col names are assumed to be variable and managed via tables/cols.
	//A valid source is one included within the sources currently considered by the library, in memory.
	//A valid table is one really existing in the given DB.

	public static boolean _is_ok = true;
	public static String _cur_source = strings.DEFAULT;
	
	//--- Populated via the corresponding db_ini method (e.g., load_sources()).
	private static HashMap<String, HashMap<String, db_field>> _sources = new HashMap<String, HashMap<String, db_field>>();
	private static HashMap<String, String> _source_mains = new HashMap<String, String>();
	//------
	
	static { _ini.load(); }

	public static HashMap<String, Boolean> update_conn_info(HashMap<String, String> params_)
	{
		return _config.update_db_conn_info(params_, _config.get_db(types._CONFIG_DB_SETUP));
	}

	public static boolean update_host(String host_)
	{
		return _config.update(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_HOST, host_);
	}

	public static boolean update_user(String user_)
	{
		return _config.update(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_USER, user_);
	}

	public static boolean update_error_exit(boolean error_exit_)
	{
		return _config.update(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_ERROR_EXIT, error_exit_);
	}
	
	public static ArrayList<HashMap<String, String>> select(String source_, String[] fields_, db_where[] wheres_, int max_rows_, db_order order_)
	{	
		String source = check_source_error(source_);
		if (!_is_ok) return null;
		
		return select_internal(get_table(source), get_cols(source, fields_), db_where.to_string(wheres_), max_rows_, db_order.to_string(order_));
	}
	
	public static <x> void insert(String source_, HashMap<String, x> vals_raw_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		insert_internal(get_table(source_), adapt_inputs(source, null, vals_raw_));
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		update_internal(get_table(source), adapt_inputs(source, null, vals_raw_), db_where.to_string(wheres_));
	}
	
	public static void delete(String source_, db_where[] wheres_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		delete_internal(get_table(source), db_where.to_string(wheres_));
	}

	public static boolean table_exists(String source_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return false;
		
		return table_exists_internal(get_table(source));
	}
	
	public static void create_table(String source_, boolean drop_it_)
	{
		create_table(source_, get_source_fields(source_), drop_it_);
	}
	
	public static void create_table(String source_, HashMap<String, db_field> fields_, boolean drop_it_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;

		String table = get_table(source);
		boolean exists = table_exists_internal(table);
		if (drop_it_) 
		{
			if (exists) drop_table_internal(table);
		}
		else if (exists) return;
		
		HashMap<String, db_field> cols = new HashMap<String, db_field>();

		if (arrays.is_ok(fields_))
		{
			for (Entry<String, db_field> item: fields_.entrySet())
			{
				String field = item.getKey();
				String col = db.get_col(source_, field);
				if (!strings.is_ok(col)) continue;
				
				cols.put(col, new db_field(item.getValue()));
			}
		}

		create_table_internal(table, cols);
	}
	
	public static void drop_table(String source_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		String table = get_table(source);
		if (!table_exists(table)) return;
		
		drop_table_internal(table);		
	}
	
	public static void truncate_table(String source_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		truncate_table_internal(get_table(source));
	}
	
	public static String get_value(String input_)
	{
		String value = strings.DEFAULT;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			value = mysql.get_value(input_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);

		return value;
	}

	public static String get_variable(String input_)
	{
		String variable = strings.DEFAULT;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			variable = mysql.get_variable(input_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);

		return variable; 	
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
		
		String source = types.check_aliases(source_);

		if (strings.is_ok(source))
		{
			_cur_source = (_sources.containsKey(source) ? source : strings.DEFAULT);
		}
		else if (!_sources.containsKey(_cur_source)) _cur_source = strings.DEFAULT;
		
		return _cur_source;
	}

	public static boolean sources_are_equal(String source1_, String source2_)
	{
		return generic.are_equal(check_source(source1_), check_source(source2_));
	}
	
	public static boolean field_is_ok(String source_, String field_)
	{
		return strings.is_ok(db.get_col(source_, field_));
	}
	
	public static void add_source(String source_, HashMap<String, db_field> fields_)
	{
		if (!arrays.is_ok(_sources)) _sources = new HashMap<String, HashMap<String, db_field>>();
		if (!strings.is_ok(source_) || _sources.containsKey(source_)) return;
		
		HashMap<String, db_field> fields = new HashMap<String, db_field>();
		
		for (Entry<String, db_field> item: fields_.entrySet())
		{
			fields.put(item.getKey(), new db_field(item.getValue()));
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
		
		return ((strings.is_ok(source) && _source_mains.containsKey(source)) ? db._source_mains.get(source) : strings.DEFAULT);
	}
	
	public static HashMap<String, db_field> get_source_fields(String source_)
	{
		String source = check_source(source_);

		return (strings.is_ok(source) ? _sources.get(source) : null);
	}
	
	public static HashMap<String, db_field> get_default_fields()
	{
		HashMap<String, db_field> fields = new HashMap<String, db_field>();

		size temp = new size(0.0, dates.get_time_pattern(dates.DATE_TIME).length(), 0);
		fields.put(types._CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, new db_field(new data(accessory.types.DATA_STRING, temp), null));
		fields.put(types._CONFIG_DB_FIELDS_DEFAULT_ID, new db_field(new data(accessory.types.DATA_INTEGER, null), null));

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
		if (!fields_.containsKey(id) || !db_field.complies(val_, fields_.get(id))) return null;
		
		String val = sanitise_string(strings.to_string(val_));
		if (!strings.is_ok(val)) return null;

		String col = get_col(source, id);
		if (!strings.is_ok(col)) return null;
		
		output.put(col, val);

		return output;
	}
	
	public static String sanitise_string(String input_)
	{
		String output = input_;
		if (!strings.is_ok(output)) return output;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.sanitise_string(output);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);

		return output; 
	}

	public static String get_data_type(db_field field_)
	{
		String output = "";
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.get_data_type(field_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output;
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

		String type = types.check_aliases(type_);

		errors.manage_db(type, query_, e_, message_);
	}
	
	public static String check_source_error(String source_)
	{
		String source = check_source(source_);
		if (!strings.is_ok(source)) manage_error(types.ERROR_DB_SOURCE, null, null, null);
		
		return source;
	}
	
	private static ArrayList<HashMap<String, String>> select_internal(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		return adapt_outputs(table_to_source(table_), execute_type(types.DB_QUERY_SELECT, table_, cols_, null, where_, max_rows_, order_, null));
	}
	
	private static void insert_internal(String table_, HashMap<String, String> vals_)
	{
		execute_type(types.DB_QUERY_INSERT, table_, null, vals_, null, 0, null, null);
	}
	
	private static void update_internal(String table_, HashMap<String, String> vals_, String where_)
	{
		execute_type(types.DB_QUERY_UPDATE, table_, null, vals_, where_, 0, null, null);
	}

	private static void delete_internal(String table_, String where_)
	{
		execute_type(types.DB_QUERY_DELETE, table_, null, null, where_, 0, null, null);
	}
	
	private static boolean table_exists_internal(String table_)
	{
		return arrays.is_ok(execute_type(types.DB_QUERY_TABLE_EXISTS, table_, null, null, null, 0, null, null));
	} 
	
	private static void create_table_internal(String table_, HashMap<String, db_field> cols_)
	{
		execute_type(types.DB_QUERY_TABLE_CREATE, table_, null, null, null, 0, null, cols_);
	} 
	
	private static void drop_table_internal(String table_)
	{
		execute_type(types.DB_QUERY_TABLE_DROP, table_, null, null, null, 0, null, null);
	} 
	
	private static void truncate_table_internal(String table_)
	{
		execute_type(types.DB_QUERY_TABLE_TRUNCATE, table_, null, null, null, 0, null, null);
	} 
	
	private static ArrayList<HashMap<String, String>> execute_type(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		ArrayList<HashMap<String, String>> output = null;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.execute(what_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output;
	}
	
	private static String[] get_cols(String source_, String[] fields_)
	{
		String source = check_source(source_);
		
		int size = arrays.get_size(fields_);
		if (!strings.is_ok(source) || size < 1) return null;
		
		String[] cols = new String[size];
		
		for (int i = 0; i < size; i++)
		{
			cols[i] = get_col(source, fields_[i]);
		}
		
		return cols;
	}
	
	private static <x> HashMap<String, String> adapt_inputs_input(String source_, HashMap<String, String> old_, String field_, x val_)
	{
		String source = check_source(source_);
		String field = types.check_aliases(field_);
		
		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields) || !strings.is_ok(field)) return null;

		return adapt_input(source, ((arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>())), field, val_, fields);
	}
	
	private static ArrayList<HashMap<String, String>> adapt_outputs(String source_, ArrayList<HashMap<String, String>> outputs_)
	{
		ArrayList<HashMap<String, String>> outputs = new ArrayList<HashMap<String, String>>();
		
		String source = check_source(source_);
		if (!strings.is_ok(source)) return outputs;
		
		for (HashMap<String, String> item: outputs_)
		{
			HashMap<String, String> output = new HashMap<String, String>();
			
			for (Entry<String, String> item2: item.entrySet())
			{
				String key = col_to_field(source_, item2.getKey());
				if (!strings.is_ok(key)) continue;

				output.put(key, item2.getValue());
			}
			
			outputs.add(output);
		}

		return outputs;
	}
	
	private static String col_to_field(String source_, String col_)
	{
		String source = check_source(source_);
		if (!strings.is_ok(source) || !strings.is_ok(col_)) return strings.DEFAULT;
		
		HashMap<String, db_field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields)) return strings.DEFAULT;
		
		for (Entry<String, db_field> field: fields.entrySet())
		{
			String key = field.getKey();
			String col = get_col(source_, key);
			if (strings.are_equal(col_, col)) return key;
		}
		
		return strings.DEFAULT;
	}
	
	private static String table_to_source(String table_)
	{
		if (!strings.is_ok(table_) || !arrays.is_ok(_sources)) return strings.DEFAULT;
		
		for (Entry<String, HashMap<String, db_field>> source: _sources.entrySet())
		{
			String key = source.getKey();
			String table = get_table(key);
			
			if (strings.are_equal(table_, table)) return key;
		}
		
		return strings.DEFAULT;
	}
}