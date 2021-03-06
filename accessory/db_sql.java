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
	public static boolean params_are_ok(String source_, String what_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		String table = db.get_table(source_);

		if 
		(
			!(
				!strings.is_ok(table) || (what_.equals(db.QUERY_DELETE) && !strings.is_ok(where_)) ||
				((what_.equals(db.QUERY_INSERT) || what_.equals(db.QUERY_UPDATE)) && !arrays.is_ok(vals_)) ||
				(what_.equals(db.QUERY_TABLE_CREATE) && !arrays.is_ok(cols_info_))
			)
		)
		{ return true; }

		HashMap<String, String> items = new HashMap<String, String>();

		items.put("table", table);

		if (what_.equals(db.QUERY_SELECT)) 
		{
			items.put("cols", strings.to_string(cols_));
			items.put("max_rows", strings.to_string(max_rows_));
			items.put("order", strings.to_string(order_));
		}

		if (what_.equals(db.QUERY_DELETE) || what_.equals(db.QUERY_UPDATE) || what_.equals(db.QUERY_SELECT)) items.put("where", strings.to_string(where_));
		if (what_.equals(db.QUERY_INSERT) || what_.equals(db.QUERY_UPDATE)) items.put("values", strings.to_string(vals_));
		if (what_.equals(db.QUERY_TABLE_CREATE)) items.put(_keys.INFO, strings.to_string(cols_info_));

		String message = "Wrong " + types.remove_type(what_, types.DB_QUERY).toUpperCase() + " query" + misc.SEPARATOR_CONTENT;
		String temp = strings.to_string(items);
		if (strings.is_ok(temp)) message += temp;
		
		db.manage_error(source_, db.ERROR_QUERY, null, null, message);

		return false;
	}

	static ArrayList<HashMap<String, String>> execute_query(String source_, String query_, boolean return_data_, String[] cols_)
	{
		db.is_ok(source_, false);

		ArrayList<HashMap<String, String>> output = null;

		Connection conn = connect(source_);
		if (conn == null) return output;

		try 
		{
			PreparedStatement statement = conn.prepareStatement(query_);

			if (!return_data_) 
			{
				statement.executeUpdate();

				db.is_ok(source_, true);

				return output;
			}

			output = new ArrayList<HashMap<String, String>>();
			try
			{
				ResultSet data = statement.executeQuery();

				String[] cols = execute_query_get_cols(source_, data, cols_);
				if (!arrays.is_ok(cols)) return output;

				while (data.next()) 
				{
					HashMap<String, String> row = new HashMap<String, String>();

					for (String col : cols)
					{
						String val = ((strings.is_ok(col) && data.findColumn(col) > -1) ? data.getString(col) : strings.DEFAULT);
						if (!strings.is_ok(val)) val = strings.DEFAULT;

						row.put(col, val);
					}

					output.add(row);
				}	

				db.is_ok(source_, true);
			}
			catch (Exception e) { db.manage_error(source_, db.ERROR_QUERY, query_, e, null); }
		} 
		catch (Exception e) { db.manage_error(source_, db.ERROR_QUERY, query_, e, null); } 
		finally { disconnect(source_, conn); }

		return output;
	}

	private static Connection connect(String source_) 
	{
		Properties properties = get_properties(source_);
		if (properties == null) return null;

		return connect_type(source_, properties);
	}

	private static Connection connect_type(String source_, Properties properties) { return db.get_valid_instance(source_).connect(source_, properties); }

	private static Properties get_properties(String source_) 
	{	
		HashMap<String, String> credentials = db.get_credentials(source_);

		String username = (String)arrays.get_value(credentials, accessory.credentials.USERNAME);
		String password = (String)arrays.get_value(credentials, accessory.credentials.PASSWORD);
		String max_pool = db.get_max_pool(db.get_valid_setup(source_));

		String type = null;
		String message = ""; 

		if (!strings.is_ok(username) || !strings.is_ok(password))
		{
			type = db.ERROR_CREDENTIALS;
			message = "credentials";
		}
		else if (!strings.is_int(max_pool))
		{
			type = db.ERROR_INFO;
			message = "MaxPooledStatements";
		}

		if (type != null)
		{
			message = "Wrong " + message;
			db.manage_error(source_, type, null, null, message);

			return null;
		}

		Properties properties = new Properties();	
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("MaxPooledStatements", max_pool);

		return properties;
	}

	private static void disconnect(String source_, Connection conn_) 
	{
		if (conn_ == null) return; 

		try 
		{
			conn_.close();
			conn_ = null;
		} 
		catch (Exception e) { db.manage_error(source_, db.ERROR_CONN, null, e, null); }
	}

	private static String[] execute_query_get_cols(String source_, ResultSet data_, String[] cols_)
	{
		if (arrays.is_ok(cols_)) return cols_;

		ArrayList<String> cols = new ArrayList<String>();

		try 
		{
			ResultSetMetaData info = data_.getMetaData();

			for (int i = 1; i <= info.getColumnCount(); i++) { cols.add(info.getColumnName(i)); }
		} 
		catch (Exception e) 
		{
			cols = null;

			db.manage_error(source_, db.ERROR_QUERY, strings.DEFAULT, e, "Impossible to retrieve table columns");
		}

		return arrays.to_array(cols);
	}
}