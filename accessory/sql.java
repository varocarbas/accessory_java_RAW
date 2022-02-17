package accessory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

class sql 
{
	public static final String SELECT = "select";
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String TRUNCATE = "truncate";

	static { _ini.load(); }

	public static boolean params_are_ok(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_)
	{
		if 
		(
			!(
				!strings.is_ok(table_) || (what_.equals(DELETE) && !strings.is_ok(where_)) ||
				((what_.equals(INSERT) || what_.equals(UPDATE)) && !arrays.is_ok(vals_))					
			)
		)
		{ return true; }

		HashMap<String, String> items = new HashMap<String, String>();

		if (strings.is_ok(table_)) items.put(keys.TABLE, table_);
		if (arrays.is_ok(cols_)) items.put(keys.COL, arrays.to_string(cols_, null));
		if (arrays.is_ok(vals_)) items.put(keys.VALUE, arrays.to_string(vals_, null, null, null));
		if (strings.is_ok(where_)) items.put(keys.WHERE, where_);
		if (max_rows_ > 0) items.put(keys.MAX, strings.from_number_int(max_rows_));
		if (strings.is_ok(order_)) items.put(keys.ORDER, order_);

		String message = "Wrong " + what_.toUpperCase() + " query" + misc.SEPARATOR_CONTENT;
		String temp = arrays.to_string(items, null, null, null);
		if (strings.is_ok(temp)) message += temp;

		db.manage_error(types.ERROR_DB_QUERY, null, null, message);

		return false;
	}

	static ArrayList<HashMap<String, String>> execute_query(String query_, boolean return_data_, String[] cols_)
	{
		db._is_ok = false;

		ArrayList<HashMap<String, String>> output = null;

		Connection conn = connect();
		if (conn == null) return output;

		try 
		{
			PreparedStatement statement = conn.prepareStatement(query_);

			if (!return_data_) 
			{
				statement.executeUpdate();

				db._is_ok = true;

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

				db._is_ok = true;
			}
			catch (Exception e) 
			{
				db.manage_error(types.ERROR_DB_QUERY, query_, e, null);
			}
		} 
		catch (Exception e) 
		{
			db.manage_error(types.ERROR_DB_QUERY, query_, e, null);
		} 
		finally { disconnect(conn); }

		return output;
	}

	private static Connection connect() 
	{
		Properties properties = get_properties();
		if (properties == null) return null;

		return connect_type(properties);
	}

	private static Connection connect_type(Properties properties) 
	{
		Connection conn = null; 

		if (_config.matches(db._config_type, types._CONFIG_DB_TYPE, types.DB_MYSQL))
		{
			conn = mysql.connect(properties);
		}
		else db.manage_error(types.ERROR_DB_TYPE, null, null, null);

		return conn;
	}

	private static Properties get_properties() 
	{	
		HashMap<String, String> credentials = db.get_credentials();

		String username = arrays.get_value(credentials, keys.USERNAME);
		String password = arrays.get_value(credentials, keys.PASSWORD);
		String max_pool = _config.get(db._config_type, types._CONFIG_DB_MAX_POOL);

		String type = null;
		String message = ""; 

		if (!strings.is_integer(max_pool))
		{
			type = types.ERROR_DB_INFO;
			message = "MaxPooledStatements";
		}
		else if (!strings.is_ok(username) || !strings.is_ok(password))
		{
			type = types.ERROR_DB_CREDENTIALS;
			message = "credentials";
		}

		if (type != null)
		{
			message = "Wrong " + message;
			db.manage_error(type, null, null, message);

			return null;
		}

		Properties properties = new Properties();	
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("MaxPooledStatements", max_pool);

		return properties;
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
			db.manage_error(types.ERROR_DB_CONN, null, e, null);
		}
	}

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
			cols = null;

			db.manage_error(types.ERROR_DB_QUERY, strings.DEFAULT, e, "Impossible to retrieve table columns");
		}

		return arrays.to_array(cols);
	}
}