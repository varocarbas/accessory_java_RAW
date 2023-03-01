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
	public static boolean params_are_ok(String source_, String what_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_, boolean is_static_)
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

		String message = "Wrong " + _types.remove_type(what_, _types.DB_QUERY).toUpperCase() + " query" + misc.SEPARATOR_CONTENT;
		String temp = strings.to_string(items);
		if (strings.is_ok(temp)) message += temp;
		
		db.manage_error(source_, db.ERROR_QUERY, null, null, message, is_static_);

		return false;
	}

	public static ArrayList<HashMap<String, String>> execute_query(String source_, String query_, boolean return_data_, String[] cols_) { return execute_query(source_, query_, return_data_, cols_, connect(source_), false); }
	
	public static ArrayList<HashMap<String, String>> execute_query_quicker(String type_, String source_, String query_, boolean return_data_, String[] cols_, String username_, String password_, String db_name_, String host_, String max_pool_) { return execute_query(source_, query_, return_data_, cols_, connect_static(type_, source_, username_, password_, db_name_, host_, max_pool_), true); }

	public static String get_backup_file_extension() { return paths.EXTENSION_SQL; }
	
	public static boolean is_sql(String type_) { return get_all_types().contains(type_); }
	
	public static ArrayList<String> get_all_types() { return _alls.DB_SQL_TYPES; }
	
	public static ArrayList<String> populate_all_types() { return arrays.to_arraylist(new String[] { db.MYSQL }); }
	
	private static ArrayList<HashMap<String, String>> execute_query(String source_, String query_, boolean return_data_, String[] cols_, Connection conn_, boolean is_static_)
	{
		db.update_is_ok(source_, false, is_static_);

		db._last_query = query_;
		
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		if (conn_ == null) return output;
		
		try 
		{
			PreparedStatement statement = conn_.prepareStatement(query_);

			if (!return_data_) 
			{
				statement.executeUpdate();

				db.update_is_ok(source_, true, is_static_);

				return output;
			}

			try
			{
				ResultSet data = statement.executeQuery();

				String[] cols = execute_query_get_cols(source_, data, cols_, is_static_);
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

				db.update_is_ok(source_, true, is_static_);
			}
			catch (Exception e) { db.manage_error(source_, db.ERROR_QUERY, query_, e, null, is_static_); }
		} 
		catch (Exception e) { db.manage_error(source_, db.ERROR_QUERY, query_, e, null, is_static_); } 
		finally { disconnect(source_, conn_, is_static_); }

		return output;
	}

	private static Connection connect(String source_) 
	{
		Properties properties = get_properties(source_);

		return (properties != null ? db.get_valid_instance(source_).connect(source_, properties) : null);
	}

	private static Connection connect_static(String type_, String source_, String username_, String password_, String db_name_, String host_, String max_pool_) 
	{
		Properties properties = get_properties_static(username_, password_, max_pool_);

		return (properties != null ? connect_static_internal(type_, source_, properties, db_name_, host_) : null);
	}

	private static Connection connect_static_internal(String type_, String source_, Properties properties_, String db_name_, String host_) 
	{
		Connection output = null;
		
		if (strings.are_equal(type_, db.MYSQL)) output = db_mysql.connect_internal_static(source_, properties_, db_name_, host_, true);
		
		return output;	
	}
	
	private static Properties get_properties(String source_) 
	{
		String[] temp = db.get_credentials(source_);
		
		String username = credentials.get_username(temp);
		String password = credentials.get_password(temp);
		
		String max_pool = db.get_max_pool(db.get_valid_setup(source_));

		return get_properties_static(username, password, max_pool);
	}

	private static Properties get_properties_static(String username_, String password_, String max_pool_) 
	{	
		String type = null;
		String message = ""; 

		if (!strings.is_ok(username_) || !strings.is_ok(password_))
		{
			type = db.ERROR_CREDENTIALS;
			message = "credentials";
		}
		else if (!strings.is_int(max_pool_))
		{
			type = db.ERROR_INFO;
			message = "MaxPooledStatements";
		}

		if (type != null)
		{
			message = "Wrong " + message;
			db_static.manage_error(type, null, null, message);

			return null;
		}

		Properties properties = new Properties();	
		properties.setProperty("user", username_);
		properties.setProperty("password", password_);
		properties.setProperty("MaxPooledStatements", max_pool_);

		return properties;
	}

	private static void disconnect(String source_, Connection conn_, boolean is_static_) 
	{
		if (conn_ == null) return; 

		try 
		{
			conn_.close();
			conn_ = null;
		} 
		catch (Exception e) { db.manage_error(source_, db.ERROR_CONN, null, e, null, is_static_); }
	}

	private static String[] execute_query_get_cols(String source_, ResultSet data_, String[] cols_, boolean is_static_)
	{
		if (arrays.is_ok(cols_)) return cols_;

		String[] cols = null;
		
		try 
		{
			ResultSetMetaData info = data_.getMetaData();
			
			int tot = info.getColumnCount();			
			cols = new String[tot];
			
			int max_i = tot;
			int i2 = -1;
			
			for (int i = 1; i <= max_i; i++) 
			{
				i2++;
				cols[i2] = info.getColumnName(i); 
			}
		} 
		catch (Exception e) 
		{
			cols = null;

			db.manage_error(source_, db.ERROR_QUERY, strings.DEFAULT, e, "Impossible to retrieve table columns", is_static_);
		}

		return cols;
	}
}