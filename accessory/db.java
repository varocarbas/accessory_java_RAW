package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class db 
{
	public static boolean _is_ok = true;

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

	public static void truncate_table(String table_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.truncate_table(table_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	} 

	public static ArrayList<HashMap<String, String>> select(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		ArrayList<HashMap<String, String>> vals = null;

		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			vals = mysql.select(table_, cols_, where_, max_rows_, order_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return vals;
	}

	public static void insert(String table_, HashMap<String, String> vals_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.insert(table_, vals_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static void update(String table_, HashMap<String, String> vals_, String where_)
	{
		if (_config.matches(_config.get_db(types._CONFIG_DB_SETUP), types._CONFIG_DB_TYPE, types._CONFIG_DB_TYPE_MYSQL))
		{
			mysql.update(table_, vals_, where_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
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

	public static boolean source_exists(String id_)
	{
		return (strings.is_ok(id_) && _sources.containsKey(id_));
	}
	
	public static void add_source(String id_, HashMap<String, field> fields_)
	{
		HashMap<String, field> fields = new HashMap<String, field>();
		
		for (Entry<String, field> item: fields_.entrySet())
		{
			fields.put(item.getKey(), new field(item.getValue()));
		}
		
		_sources.put(id_, fields);
	}
	
	public static void add_source_main(String source_, String main_)
	{
		if (!strings.are_ok(new String[] { source_, main_ })) return;
		
		_source_mains.put(source_, main_);
	}
	
	public static String get_source_main(String source_)
	{
		return ((strings.is_ok(source_) && _source_mains.containsKey(source_)) ? db._source_mains.get(source_) : strings.DEFAULT);
	}
	
	public static HashMap<String, field> get_source_fields(String id_)
	{
		return (source_exists(id_) ? _sources.get(id_) : null);
	}
	
	public static HashMap<String, field> get_default_fields()
	{
		HashMap<String, field> fields = new HashMap<String, field>();

		size temp = new size(0.0, time.get_time_pattern(time.DATE_TIME).length());
		fields.put(types._CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, new field(new data(accessory.types.DATA_STRING, temp), null));
		fields.put(types._CONFIG_DB_FIELDS_DEFAULT_ID, new field(new data(accessory.types.DATA_INTEGER, null), null));

		return fields;
	}
	
	public static String get_col(String source_, String field_)
	{
		String source = types.check_aliases(source_); 
		String field = types.check_aliases(field_);
		
		return _config.get(get_source_main(source), field);
	}
	
	public static String get_table(String source_)
	{
		String source = types.check_aliases(source_); 
		
		return _config.get(get_source_main(source), source);
	}
	
	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, double val_)
	{
		return adapt_inputs_input(source_, old_, field_, val_);
	}

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, int val_)
	{
		return adapt_inputs_input(source_, old_, field_, val_);
	}

	public static HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, String field_, String val_)
	{
		return adapt_inputs_input(source_, old_, field_, val_);
	}
	
	public static <x> HashMap<String, String> adapt_inputs(String source_, HashMap<String, String> old_, HashMap<String, x> new_)
	{
		String source = types.check_aliases(source_);
		if (!strings.is_ok(source) || !arrays.is_ok(new_)) return null;
		
		HashMap<String, field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields)) return null;
		
		HashMap<String, String> output = (arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>());
		
		for (Entry<String, x> item: new_.entrySet())
		{
			output = adapt_input(source_, output, item.getKey(), item.getValue(), fields);

			if (!arrays.is_ok(output)) return null;
		}
		
		return output;
	}

	public static <x> HashMap<String, String> adapt_input(String source_, HashMap<String, String> sofar_, String field_, x val_, HashMap<String, field> fields_)
	{
		HashMap<String, String> output = new HashMap<String, String>(sofar_);

		String id = types.check_aliases(field_);
		if (!fields_.containsKey(id) || !field.complies(val_, fields_.get(id))) return null;
		
		String val = sanitise_string(strings.to_string(val_));
		if (!strings.is_ok(val)) return null;

		String col = get_col(source_, id);
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
	
	private static <x> HashMap<String, String> adapt_inputs_input(String source_, HashMap<String, String> old_, String field_, x val_)
	{
		String source = types.check_aliases(source_);
		String field = types.check_aliases(field_);
		
		HashMap<String, field> fields = get_source_fields(source);
		if (!arrays.is_ok(fields) || !strings.is_ok(field)) return null;

		return adapt_input(source_, ((arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>())), field, val_, fields);
	}
}