package accessory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class sql 
{	
	public static boolean _is_ok = true;

	public static final String SELECT = "select";
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String TRUNCATE = "truncate";

	//To be synced with get_valid_config_types().
	private static String config_type = types._CONFIG_SQL;

	static { _ini.load(); }

	public static boolean update_config_type(String type_)
	{
		String type = types.check_aliases(type_);
		boolean is_ok = strings.are_equal(_config.get_linked_main(type), types._CONFIG_SQL);

		if (is_ok) config_type = type;

		return is_ok;
	}

	public static HashMap<String, Boolean> update_conn_info(HashMap<String, String> params_)
	{
		return _config.update_conn_info(params_, config_type);
	}

	public static boolean change_host(String host_)
	{
		return _config.update(config_type, types._CONFIG_SQL_HOST, host_);
	}

	public static boolean change_user(String user_)
	{
		return _config.update(config_type, types._CONFIG_SQL_USER, user_);
	}

	public static boolean change_error_exit(boolean error_exit_)
	{
		return _config.update(config_type, types._CONFIG_SQL_ERROR_EXIT, error_exit_);
	}

	public static void truncate_table(String table_)
	{
		_is_ok = true;

		String query = strings.DEFAULT;

		if (strings.is_ok(table_))
		{
			if (_config.matches(config_type, types._CONFIG_SQL_TYPE, types.SQL_MYSQL))
			{
				query = mysql.get_query_truncate_table(table_);
			}
		}

		execute_query_common(query, TRUNCATE);
	}    

	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, String>> select(String table_, String[] cols_, String where_, int limit_)
	{
		_is_ok = true;

		ArrayList<HashMap<String, String>> output = (ArrayList<HashMap<String, String>>)arrays.DEFAULT;

		String query = strings.DEFAULT;

		if (strings.is_ok(table_))
		{
			if (_config.matches(config_type, types._CONFIG_SQL_TYPE, types.SQL_MYSQL))
			{
				query = mysql.get_query_select(table_, cols_, where_, limit_);
			}
		}

		if (strings.is_ok(query)) output = execute_query(query, true, cols_);
		else manage_error(types.ERROR_SQL_QUERY, query, null, "Wrong SELECT query");

		return output;
	}

	public static void insert(String table_, HashMap<String, String> vals_)
	{
		_is_ok = true;

		String query = strings.DEFAULT;

		if (strings.is_ok(table_) && arrays.is_ok(vals_))
		{
			if (_config.matches(config_type, types._CONFIG_SQL_TYPE, types.SQL_MYSQL))
			{
				query = mysql.get_query_insert(table_, vals_);
			}
		}

		execute_query_common(query, INSERT);
	}

	public static void update(String table_, HashMap<String, String> vals_, String where_)
	{
		_is_ok = true;

		String query = "";

		if (strings.is_ok(table_) && arrays.is_ok(vals_))
		{
			if (_config.matches(config_type, types._CONFIG_SQL_TYPE, types.SQL_MYSQL))
			{
				query = mysql.get_query_update(table_, vals_, where_);
			}
		}

		execute_query_common(query, UPDATE);
	}

	public static void delete(String table_, String where_)
	{
		_is_ok = true;

		String query = strings.DEFAULT;

		if (strings.is_ok(table_) && strings.is_ok(where_)) 
		{
			if (_config.matches(config_type, types._CONFIG_SQL_TYPE, types.SQL_MYSQL))
			{
				query = mysql.get_query_delete(table_, where_); 
			}
		}

		execute_query_common(query, DELETE);
	}

	public static void manage_error(String type_, String query_, Exception e_, String message_)
	{
		_is_ok = false;

		String type = types.check_aliases(type_);

		errors.manage_sql(type, query_, e_, message_);
	}

	private static void execute_query_common(String query_, String what_)
	{
		if (strings.is_ok(query_)) execute_query(query_, false, null);
		else 
		{
			manage_error
			(
				types.ERROR_SQL_QUERY, query_, null, 
				"Wrong " + what_.toUpperCase() + " query"
			);
		}
	}

	private static Connection connect() 
	{
		Connection conn = null; 

		Properties properties = get_properties();
		if (properties == null)
		{
			manage_error(types.ERROR_SQL_CREDENTIALS, null, null, null);

			return conn;
		}

		return connect_type(properties);
	}

	private static Connection connect_type(Properties properties) 
	{
		Connection conn = null; 

		if (properties == null)
		{
			manage_error(types.ERROR_SQL_TYPE, null, null, null);

			return conn;
		}

		if (_config.matches(config_type, types._CONFIG_SQL_TYPE, types.SQL_MYSQL))
		{
			conn = mysql.connect(properties);
		}

		return conn;
	}

	private static Properties get_properties() 
	{	
		HashMap<String, String> credentials = get_credentials();

		String username = arrays.get_value(credentials, keys.USERNAME);
		String password = arrays.get_value(credentials, keys.PASSWORD);
		String max_pool = _config.get(config_type, types._CONFIG_SQL_MAX_POOL);

		if (!strings.is_ok(username) || !strings.is_ok(password) || !strings.is_integer(max_pool)) return null;

		Properties properties = new Properties();	
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("MaxPooledStatements", max_pool);

		return properties;
	}

	private static HashMap<String, String> get_credentials()
	{
		HashMap<String, String> output = new HashMap<String, String>();

		String user = _config.get(config_type, types._CONFIG_SQL_USER);
		String username = _config.get(config_type, types._CONFIG_SQL_CREDENTIALS_USERNAME);
		String password = _config.get(config_type, types._CONFIG_SQL_CREDENTIALS_PASSWORD);

		if (strings.is_ok(username) && strings.is_ok(password))
		{
			output.put(keys.USERNAME, username);
			output.put(keys.PASSWORD, password);
		}
		else if (strings.is_ok(user))
		{
			output = credentials.get
			(
				_config.get(config_type, types._CONFIG_SQL_CREDENTIALS_TYPE), user, 
				strings.to_boolean(_config.get(config_type, types._CONFIG_SQL_CREDENTIALS_ENCRYPTED)),
				_config.get(config_type, types._CONFIG_SQL_CREDENTIALS_WHERE)
			);
		}

		return output;
	}

	private static void disconnect(Connection conn_) 
	{
		if (conn_ == null) return; 

		try 
		{
			conn_.close();
			conn_ = null;
		} 
		catch (Exception e) 
		{
			manage_error(types.ERROR_SQL_CONN, null, e, null);
		}
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<HashMap<String, String>> execute_query(String query_, boolean return_data_, String[] cols_)
	{
		ArrayList<HashMap<String, String>> output = (ArrayList<HashMap<String, String>>)arrays.DEFAULT;
		if (!strings.is_ok(query_)) 
		{
			manage_error(types.ERROR_SQL_QUERY, strings.DEFAULT, null, "No query");

			return output;
		}

		Connection conn = connect();
		if (conn == null) return output;

		try 
		{
			PreparedStatement statement = conn.prepareStatement(query_);

			if (!return_data_) 
			{
				statement.executeUpdate();

				return output;
			}

			output = new ArrayList<HashMap<String, String>>();

			try
			{
				ResultSet data = statement.executeQuery();

				String[] cols = execute_query_get_cols(data, cols_);
				if (!arrays.is_ok(cols)) return output;

				while (data.next()) 
				{ 
					HashMap<String, String> row = new HashMap<String, String>();

					for (String col : cols)
					{
						String val = 
						(
							(
								strings.is_ok(col) && 
								data.findColumn(col) > -1
							) 
							? data.getString(col) : strings.DEFAULT
						);
						if (!strings.is_ok(val)) val = strings.DEFAULT;

						row.put(col, val);
					}

					output.add(row);
				}	
			}
			catch (Exception e) 
			{
				manage_error(types.ERROR_SQL_QUERY, query_, e, null);
			}
		} 
		catch (Exception e) 
		{
			manage_error(types.ERROR_SQL_QUERY, query_, e, null);
		} 
		finally { disconnect(conn); }

		return output;
	}

	@SuppressWarnings("unchecked")
	private static String[] execute_query_get_cols(ResultSet data_, String[] cols_)
	{
		if (arrays.is_ok(cols_)) return cols_;

		ArrayList<String> cols = new ArrayList<String>();

		try 
		{
			ResultSetMetaData info = data_.getMetaData();

			for (int i = 1; i <= info.getColumnCount(); i++)
			{
				cols.add(info.getColumnName(i));
			}
		} 
		catch (Exception e) 
		{
			cols = (ArrayList<String>)arrays.DEFAULT;

			manage_error
			(
				types.ERROR_SQL_QUERY, strings.DEFAULT, e, 
				"Impossible to retrieve the table's columns"
			);
		}

		return arrays.to_array(cols);
	}
}