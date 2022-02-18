package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class db 
{
	public static boolean _is_ok = true;
	
	static String _config_type = types._CONFIG_DB;

	//--- Populated via the corresponding _ini method (e.g., _ini.load_tables()).
	private static HashMap<String, HashMap<String, col>> _tables = new HashMap<String, HashMap<String, col>>();
	private static HashMap<String, String> _table_types = new HashMap<String, String>();
	//------
	
	static { _ini.load(); }

	public static boolean update_config_type(String type_)
	{
		String type = types.check_aliases(type_);
		boolean is_ok = strings.are_equal(_config.get_linked_main(type), types._CONFIG_DB);

		if (is_ok) _config_type = type;

		return is_ok;
	}

	public static HashMap<String, Boolean> update_conn_info(HashMap<String, String> params_)
	{
		return _config.update_db_conn_info(params_, _config_type);
	}

	public static boolean change_host(String host_)
	{
		return _config.update(_config_type, types._CONFIG_DB_HOST, host_);
	}

	public static boolean change_user(String user_)
	{
		return _config.update(_config_type, types._CONFIG_DB_USER, user_);
	}

	public static boolean change_error_exit(boolean error_exit_)
	{
		return _config.update(_config_type, types._CONFIG_DB_ERROR_EXIT, error_exit_);
	}

	public static void truncate_table(String table_)
	{
		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			mysql.truncate_table(table_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	} 

	public static ArrayList<HashMap<String, String>> select(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		ArrayList<HashMap<String, String>> vals = null;

		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			vals = mysql.select(table_, cols_, where_, max_rows_, order_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return vals;
	}

	public static void insert(String table_, HashMap<String, String> vals_)
	{
		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			mysql.insert(table_, vals_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static void update(String table_, HashMap<String, String> vals_, String where_)
	{
		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			mysql.update(table_, vals_, where_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static void delete(String table_, String where_)
	{
		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			mysql.delete(table_, where_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);
	}

	public static String get_value(String input_)
	{
		String value = strings.DEFAULT;

		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			value = mysql.get_value(input_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return value;
	}

	public static String get_variable(String input_)
	{
		String variable = strings.DEFAULT;

		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			variable = mysql.get_variable(input_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return variable; 	
	} 

	public static boolean table_exists(String id_)
	{
		return (strings.is_ok(id_) && _tables.containsKey(id_));
	}
	
	public static void remove_table(String id_)
	{
		if (!table_exists(id_)) return;
		
		_tables.remove(id_);
	}

	public static HashMap<String, col> get_table_cols(String id_)
	{
		return (table_exists(id_) ? _tables.get(id_) : null);
	}
	
	public static HashMap<String, col> get_default_cols()
	{
		HashMap<String, col> cols = new HashMap<String, col>();

		size temp = new size(0.0, time.get_time_pattern(time.DATE_TIME).length());
		cols.put(types._CONFIG_DB_COLS_DEFAULT_TIMESTAMP, new col(new data(accessory.types.DATA_STRING, temp), null));
		cols.put(types._CONFIG_DB_COLS_DEFAULT_ID, new col(new data(accessory.types.DATA_INTEGER, null), null));

		return cols;
	}
	
	public static String get_col_name(String table_, String col_)
	{
		String table = types.check_aliases(table_); 
		String col = types.check_aliases(col_);
		
		return _config.get(get_table_type(table), col);
	}
	
	public static String get_table_name(String table_)
	{
		String table = types.check_aliases(table_); 
		
		return _config.get(get_table_type(table), table);
	}
	
	public static HashMap<String, String> get_table_vals(String table_, HashMap<String, String> old_, String col_, double val_)
	{
		return get_table_vals_common(table_, old_, col_, val_);
	}

	public static HashMap<String, String> get_table_vals(String table_, HashMap<String, String> old_, String col_, int val_)
	{
		return get_table_vals_common(table_, old_, col_, val_);
	}

	public static HashMap<String, String> get_table_vals(String table_, HashMap<String, String> old_, String col_, String val_)
	{
		return get_table_vals_common(table_, old_, col_, val_);
	}

	public static <x> HashMap<String, String> get_table_vals_common(String table_, HashMap<String, String> old_, String col_, x val_)
	{
		String table = types.check_aliases(table_);
		String col = types.check_aliases(col_);
		
		HashMap<String, col> cols = get_table_cols(table);
		if (!arrays.is_ok(cols) || !strings.is_ok(col)) return null;

		return get_table_val(table_, ((arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>())), col, val_, cols);
	}
	
	public static <x> HashMap<String, String> get_table_vals(String table_, HashMap<String, String> old_, HashMap<String, x> new_)
	{
		String table = types.check_aliases(table_);
		if (!strings.is_ok(table) || !arrays.is_ok(new_)) return null;
		
		HashMap<String, col> cols = get_table_cols(table);
		if (!arrays.is_ok(cols)) return null;
		
		HashMap<String, String> output = (arrays.is_ok(old_) ? new HashMap<String, String>(old_) : new HashMap<String, String>());
		
		for (Entry<String, x> item: new_.entrySet())
		{
			output = get_table_val(table_, output, item.getKey(), item.getValue(), cols);

			if (!arrays.is_ok(output)) return null;
		}
		
		return output;
	}

	public static <x> HashMap<String, String> get_table_val(String table_, HashMap<String, String> sofar_, String col_, x val_, HashMap<String, col> cols_)
	{
		HashMap<String, String> output = new HashMap<String, String>(sofar_);

		String id = types.check_aliases(col_);
		if (!cols_.containsKey(id) || !col.complies(val_, cols_.get(id))) return null;
		
		String val = sanitise_string(strings.to_string(val_));
		if (!strings.is_ok(val)) return null;

		String col = get_col_name(table_, id);
		if (!strings.is_ok(col)) return null;
		
		output.put(col, val);

		return output;
	}
	
	public static String sanitise_string(String input_)
	{
		String output = input_;
		if (!strings.is_ok(output)) return output;
		
		if (_config.matches(_config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			output = mysql.sanitise_string(output);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return output; 
	}
	
	public static void add_table(String id_, HashMap<String, col> cols_)
	{
		HashMap<String, col> cols = new HashMap<String, col>();
		
		for (Entry<String, col> item: cols_.entrySet())
		{
			cols.put(item.getKey(), new col(item.getValue()));
		}
		
		_tables.put(id_, cols);
	}
	
	public static void add_table_type(String table_, String type_)
	{
		if (!strings.are_ok(new String[] { table_, type_ })) return;
		
		_table_types.put(table_, type_);
	}
	
	public static String get_table_type(String table_)
	{
		return ((strings.is_ok(table_) && _table_types.containsKey(table_)) ? db._table_types.get(table_) : strings.DEFAULT);
	}
	
	static HashMap<String, String> get_credentials()
	{
		HashMap<String, String> output = new HashMap<String, String>();

		String user = _config.get(_config_type, types._CONFIG_DB_USER);
		String username = _config.get(_config_type, types._CONFIG_DB_CREDENTIALS_USERNAME);
		String password = _config.get(_config_type, types._CONFIG_DB_CREDENTIALS_PASSWORD);

		if (strings.is_ok(username) && strings.is_ok(password))
		{
			output.put(keys.USERNAME, username);
			output.put(keys.PASSWORD, password);
		}
		else if (strings.is_ok(user))
		{
			output = credentials.get
			(
				_config.get(_config_type, types._CONFIG_DB_CREDENTIALS_TYPE), user, 
				strings.to_boolean(_config.get(_config_type, types._CONFIG_DB_CREDENTIALS_ENCRYPTED)),
				_config.get(_config_type, types._CONFIG_DB_CREDENTIALS_WHERE)
			);
		}

		return output;
	}

	static void manage_error(String type_, String query_, Exception e_, String message_)
	{
		_is_ok = false;

		String type = types.check_aliases(type_);

		errors.manage_sql(type, query_, e_, message_);
	}
}