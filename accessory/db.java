package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class db 
{
	public static boolean _is_ok = true;

	static String _config_type = types._CONFIG_DB;

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
			variable = mysql.get_value(input_);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return variable; 	
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