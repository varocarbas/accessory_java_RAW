package accessory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

abstract class db_sql 
{
	static { ini.load(); }

	public static boolean params_are_ok(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		if 
		(
			!(
				!strings.is_ok(table_) || (what_.equals(db.DELETE) && !strings.is_ok(where_)) ||
				((what_.equals(db.INSERT) || what_.equals(db.UPDATE)) && !arrays.is_ok(vals_)) ||
				(what_.equals(db.TABLE_CREATE) && !arrays.is_ok(cols_info_))
			)
		)
		{ return true; }

		HashMap<String, String> items = new HashMap<String, String>();

		items.put("table", strings.to_string(table_));
		
		if (what_.equals(db.SELECT)) 
		{
			items.put("cols", strings.to_string(cols_));
			items.put("max_rows", strings.to_string(max_rows_));
			items.put("order", strings.to_string(order_));
		}
		
		if (what_.equals(db.DELETE) || what_.equals(db.UPDATE) || what_.equals(db.SELECT)) 
		{
			items.put("where", strings.to_string(where_));
		}
		
		if (what_.equals(db.INSERT) || what_.equals(db.UPDATE)) items.put(generic.VALUE, strings.to_string(vals_));
		if (what_.equals(db.TABLE_CREATE)) items.put(generic.INFO, strings.to_string(cols_info_));
		
		String message = "Wrong " + types.remove_type(what_, types.DB_QUERY).toUpperCase() + " query" + misc.SEPARATOR_CONTENT;
		String temp = strings.to_string(items);
		if (strings.is_ok(temp)) message += temp;

		db.manage_error(types.ERROR_DB_QUERY, null, null, message);

		return false;
	}

	static ArrayList<HashMap<String, String>> execute_query(String query_, boolean return_data_, String[] cols_)
	{
		db.update_is_ok(false);

		ArrayList<HashMap<String, String>> output = null;

		Connection conn = connect();
		if (conn == null) return output;

		try 
		{
			PreparedStatement statement = conn.prepareStatement(query_);

			if (!return_data_) 
			{
				statement.executeUpdate();

				db.update_is_ok(true);

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

				db.update_is_ok(true);
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
		return db.get_current_instance().connect(properties);
	}

	private static Properties get_properties() 
	{	
		HashMap<String, String> credentials = db.get_credentials();

		String username = arrays.get_value(credentials, generic.USERNAME);
		String password = arrays.get_value(credentials, generic.PASSWORD);
		String max_pool = db.get_max_pool();

		String type = null;
		String message = ""; 

		if (!strings.is_int(max_pool))
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