package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class db 
{
	public static boolean _is_ok = true;
	public static String _cur_source = strings.DEFAULT;
	
	//--- Populated via the corresponding _ini method (e.g., _ini.load_sources()).
	private static HashMap<String, HashMap<String, field>> _sources = new HashMap<String, HashMap<String, field>>();
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
	
	public static void truncate_table_raw(String source_)
	{
		truncate_table(get_table(source_));
	}
	
	public static void truncate_table(String table_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.truncate_table(table_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	} 


	public static ArrayList<HashMap<String, String>> select_raw(String source_, String[] fields_, where[] wheres_, int max_rows_, order order_)
	{	
		String source = check_source(source_);
		
		return select(get_table(source), get_cols(source, fields_), where.to_string(wheres_, source), max_rows_, order.to_string(order_));
	}

	public static ArrayList<HashMap<String, String>> select_raw(String source_, String[] fields_, where where_, int max_rows_, order order_)
	{
		String source = check_source(source_);
		
		return select(get_table(source), get_cols(source, fields_), where.to_string(where_, source), max_rows_, order.to_string(order_));
	}
	
	public static ArrayList<HashMap<String, String>> select(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		ArrayList<HashMap<String, String>> vals = null;
		
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			vals = mysql.select(table_, cols_, where_, max_rows_, order_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return adapt_outputs(table_to_source(table_), vals);
	}

	public static <x> void insert_raw(String source_, HashMap<String, x> vals_raw_)
	{
		String source = check_source(source_);
		
		insert(get_table(source_), adapt_inputs(source, null, vals_raw_));
	}
	
	public static void insert(String table_, HashMap<String, String> vals_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.insert(table_, vals_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static <x> void update_raw(String source_, HashMap<String, x> vals_raw_, where[] wheres_)
	{
		String source = check_source(source_);
		
		update(get_table(source), adapt_inputs(source, null, vals_raw_), where.to_string(wheres_, source));
	}

	public static void update_raw(String source_, HashMap<String, String> vals_raw_, where where_)
	{
		String source = check_source(source_);
		
		update(get_table(source), adapt_inputs(source, null, vals_raw_), where.to_string(where_, source));
	}
	
	public static void update(String table_, HashMap<String, String> vals_, String where_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.update(table_, vals_, where_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static void delete_raw(String source_, where[] wheres_)
	{
		String source = check_source(source_);
		
		delete(get_table(source), where.to_string(wheres_, source));
	}
	
	public static void delete_raw(String source_, where where_)
	{
		String source = check_source(source_);
		
		delete(get_table(source), where.to_string(where_, source));
	}
		
	public static void delete(String table_, String where_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.delete(table_, where_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static String get_value(String input_)
	{
		String value = strings.DEFAULT;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			value = mysql.get_value(input_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return value;
	}

	public static String get_variable(String input_)
	{
		String variable = strings.DEFAULT;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			variable = mysql.get_variable(input_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return variable; 	
	} 

	public static boolean source_is_ok(String source_)
	{
		return (strings.is_ok(check_source(source_)));
	}
	
	public static void add_source(String source_, HashMap<String, field> fields_)
	{
		if (!arrays.is_ok(_sources)) _sources = new HashMap<String, HashMap<String, field>>();
		if (!strings.is_ok(source_) || _sources.containsKey(source_)) return;
		
		HashMap<String, field> fields = new HashMap<String, field>();
		
		for (Entry<String, field> item: fields_.entrySet())
		{
			fields.put(item.getKey(), new field(item.getValue()));
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
	
	public static HashMap<String, field> get_source_fields(String source_)
	{
		String source = check_source(source_);
		
		return (source_is_ok(source) ? _sources.get(source) : null);
	}
	
	public static HashMap<String, field> get_default_fields()
	{
		HashMap<String, field> fields = new HashMap<String, field>();

		size temp = new size(0.0, dates.get_time_pattern(dates.DATE_TIME).length());
		fields.put(types._CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, new field(new data(accessory.types.DATA_STRING, temp), null));
		fields.put(types._CONFIG_DB_FIELDS_DEFAULT_ID, new field(new data(accessory.types.DATA_INTEGER, null), null));

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
		
		HashMap<String, field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields)) return null;
		
		HashMap<String, String> output = (arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>());
		
		for (Entry<String, x> item: new_.entrySet())
		{
			output = adapt_input(source, output, item.getKey(), item.getValue(), fields);

			if (!arrays.is_ok(output)) return null;
		}
		
		return output;
	}

	public static <x> HashMap<String, String> adapt_input(String source_, HashMap<String, String> sofar_, String field_, x val_, HashMap<String, field> fields_)
	{
		HashMap<String, String> output = new HashMap<String, String>(sofar_);

		String source = check_source(source_);
		
		String id = types.check_aliases(field_);
		if (!fields_.containsKey(id) || !field.complies(val_, fields_.get(id))) return null;
		
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
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

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
	
	private static String[] get_cols(String source_, String[] fields_)
	{
		String source = check_source(source_);
		
		int size = arrays.get_size(fields_);
		if (!source_is_ok(source) || size < 1) return null;
		
		String[] cols = new String[size];
		
		for (int i = 0; i < size; i++)
		{
			cols[i] = get_col(source, fields_[i]);
		}
		
		return cols;
	}
	
	private static String check_source(String source_)
	{
		_cur_source = strings.DEFAULT;
		if (!arrays.is_ok(_sources)) return _cur_source;
		
		String source = types.check_aliases(source_);

		if (_sources.containsKey(source)) _cur_source = source;
		else if (!_sources.containsKey(_cur_source)) _cur_source = strings.DEFAULT;
		
		return _cur_source;
	}
	
	private static <x> HashMap<String, String> adapt_inputs_input(String source_, HashMap<String, String> old_, String field_, x val_)
	{
		String source = check_source(source_);
		String field = types.check_aliases(field_);
		
		HashMap<String, field> fields = get_source_fields(source);
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
				output.put(col_to_field(source_, item2.getKey()), item2.getValue());
			}
			
			outputs.add(output);
		}
		
		return outputs;
	}
	
	private static String col_to_field(String source_, String col_)
	{
		String source = check_source(source_);
		if (!strings.is_ok(source) || !strings.is_ok(col_)) return strings.DEFAULT;
		
		HashMap<String, field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields)) return strings.DEFAULT;
		
		for (Entry<String, field> field: fields.entrySet())
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
		
		for (Entry<String, HashMap<String, field>> source: _sources.entrySet())
		{
			String key = source.getKey();
			String table = get_table(key);
			
			if (strings.are_equal(table_, table)) return key;
		}
		
		return strings.DEFAULT;
	}
}