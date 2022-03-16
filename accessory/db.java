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
		return (String)select_one_common(source_, field_, wheres_, orders_, types.DATA_STRING);
	}
	
	public static double select_one_decimal(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (double)select_one_common(source_, field_, wheres_, orders_, types.DATA_DECIMAL);
	}
	
	public static long select_one_long(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (long)select_one_common(source_, field_, wheres_, orders_, types.DATA_LONG);
	}
	
	public static int select_one_int(String source_, String field_, db_where[] wheres_, db_order[] orders_)
	{
		return (int)select_one_common(source_, field_, wheres_, orders_, types.DATA_INT);
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
		String source = check_source_error(source_);
		if (!_is_ok) return null;
		
		return select_internal(get_table(source), get_cols(source, fields_), where_cols_, max_rows_, order_cols_);
	}
	
	public static <x> void insert(String source_, HashMap<String, x> vals_raw_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		HashMap<String, String> vals = check_vals_error(source, vals_raw_);
		if (!_is_ok) return;
		
		insert_internal(get_table(source), vals);
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, db_where[] wheres_)
	{
		update(source_, vals_raw_, db_where.to_string(wheres_));
	}
	
	public static <x> void update(String source_, HashMap<String, x> vals_raw_, String where_cols_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		HashMap<String, String> vals = check_vals_error(source, vals_raw_);
		if (!_is_ok) return;
		
		update_internal(get_table(source), vals, where_cols_);
	}
	
	public static void delete(String source_, db_where[] wheres_)
	{
		delete(source_, db_where.to_string(wheres_));
	}
	
	public static void delete(String source_, String where_cols_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return;
		
		delete_internal(get_table(source), where_cols_);
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

	public static String get_variable_table(String source_)
	{
		String source = check_source_error(source_);
		if (!_is_ok) return strings.DEFAULT;

		return get_variable(db.get_table(source));
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
			variable = mysql.get_variable(input_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return variable; 	
	} 

	public static HashMap<String, Object> get_data_type(String data_type_)
	{
		HashMap<String, Object> output = null;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.get_data_type(data_type_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output; 
	}

	public static int get_default_size(String type_)
	{
		int output = 0;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.get_default_size(type_);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);
		
		return output; 
	}

	public static int get_max_size(String type_)
	{
		int output = 0;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.get_max_size(type_);
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
		return strings.is_ok(db.get_col(source_, field_));
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
			output = mysql.sanitise_string(output);
		}
		else manage_error(types.ERROR_DB_TYPE, null, null, null);

		return output; 
	}

	public static ArrayList<HashMap<String, String>> execute_query(String query_)
	{
		ArrayList<HashMap<String, String>> output = null;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			output = mysql.execute_query(query_);
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

	private static Object select_one_common(String source_, String field_, db_where[] wheres_, db_order[] orders_, String what_)
	{
		Object output = null;
		
		if (data.is_string(what_)) output = strings.DEFAULT;
		else if (what_.equals(types.DATA_DECIMAL)) output = numbers.DEFAULT_DEC;
		else if (what_.equals(types.DATA_LONG)) output = numbers.DEFAULT_LONG;
		else if (what_.equals(types.DATA_INT)) output = numbers.DEFAULT_INT;
		
		if (!strings.is_ok(field_)) 
		{
			_is_ok = false;
			
			return output;
		}
		
		HashMap<String, String> temp = select_one(source_, new String[] { field_ }, wheres_, orders_);
		
		if (arrays.is_ok(temp))
		{
			String temp2 = temp.get(field_);
			
			if (data.is_string(what_)) output = temp2;
			else if (what_.equals(types.DATA_DECIMAL)) output = numbers.decimal_from_string(temp2);
			else if (what_.equals(types.DATA_LONG)) output = numbers.long_from_string(temp2);
			else if (what_.equals(types.DATA_INT)) output = numbers.int_from_string(temp2);	
		}
		
		return output;
	}
	
	private static String check_source_error(String source_)
	{
		String source = check_source(source_);
		if (!strings.is_ok(source)) manage_error(types.ERROR_DB_SOURCE, null, null, null);
		
		return source;
	}
	
	private static <x> HashMap<String, String> check_vals_error(String source_, HashMap<String, x> vals_raw_)
	{
		HashMap<String, String> vals = adapt_inputs(source_, null, vals_raw_);
		if (!arrays.is_ok(vals)) manage_error(types.ERROR_DB_VALS, null, null, null);
		
		return vals;
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