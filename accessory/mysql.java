package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

class mysql 
{  
	static { _ini.load(); }

	public static void truncate_table(String table_)
	{
		execute(sql.TRUNCATE, table_, null, null, null, 0, null);
	} 

	public static ArrayList<HashMap<String, String>> select(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		return execute(sql.SELECT, table_, cols_, null, where_, max_rows_, order_);
	}

	public static void insert(String table_, HashMap<String, String> vals_)
	{
		execute(sql.INSERT, table_, null, vals_, null, 0, null);
	}

	public static void update(String table_, HashMap<String, String> vals_, String where_)
	{
		execute(sql.UPDATE, table_, null, vals_, where_, 0, null);
	}

	public static void delete(String table_, String where_)
	{
		execute(sql.DELETE, table_, null, null, where_, 0, null);
	}
	
	public static String sanitise_string(String input_)
	{
		return strings.remove_escape_many(new String[] { "'", "\"" }, input_, false);
	}
	
	private static ArrayList<HashMap<String, String>> execute(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_)
	{
		String query = get_query(what_, table_, cols_, vals_, where_, max_rows_, order_);

		boolean return_data = false;
		if (what_.equals(sql.SELECT)) return_data = true;

		return (strings.is_ok(query) ? sql.execute_query(query, return_data, cols_) : null);
	}

	private static String get_query(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_)
	{	
		String query = strings.DEFAULT;
		if (!sql.params_are_ok(what_, table_, cols_, vals_, where_, max_rows_, order_)) return query;

		boolean is_ok = false;

		if (what_.equals(sql.TRUNCATE))
		{
			query = "TRUNCATE TABLE " + get_variable(table_);			
			is_ok = true;
		}
		else if (what_.equals(sql.SELECT))
		{
			query = "SELECT ";
			query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
			query += " FROM " + get_variable(table_);

			if (strings.is_ok(where_)) query += " WHERE " + where_;
			if (strings.is_ok(order_)) query += " ORDER BY " + order_;
			if (max_rows_ > 0) query += " LIMIT " + max_rows_;	

			is_ok = true;
		}
		else if (what_.equals(sql.INSERT))
		{
			query = "INSERT INTO " + get_variable(table_); 
			String temp = get_query_cols(vals_, keys.KEY);

			if (strings.is_ok(temp)) 
			{
				query += "(" + temp + ")";

				temp = get_query_cols(vals_, keys.VALUE);
				if (strings.is_ok(temp)) 
				{
					query += " VALUES (" + temp + ")"; 
					is_ok = true;
				}      		
			} 
		}
		else if (what_.equals(sql.UPDATE))
		{
			query = "UPDATE " + get_variable(table_); 
			
			String temp = get_query_cols(vals_, keys.ALL);
			if (strings.is_ok(temp)) 
			{
				query += " SET " + temp;
				is_ok = true;     		
			}
			
			if (strings.is_ok(where_)) query += " WHERE " + where_;
		}
		else if (what_.equals(sql.DELETE))
		{
			query = "DELETE FROM " + get_variable(table_);
			query += " WHERE " + where_;

			is_ok = true;
		}

		return (is_ok ? query : strings.DEFAULT);
	}

	static String get_value(String input_)
	{
		return get_variable_value(input_, false);
	}

	static String get_variable(String input_)
	{
		return get_variable_value(input_, true);   	
	} 

	static Connection connect(Properties properties) 
	{
		Connection conn = null;

		String url = get_connect_url();
		if (!strings.is_ok(url)) return conn;

		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, properties);
		} 
		catch (Exception e) 
		{
			db.manage_error(types.ERROR_DB_CONN, null, e, null); 
			conn = null;
		}

		return conn;
	} 

	private static String get_connect_url()
	{   
		String host = _config.get_db(types._CONFIG_DB_HOST);
		String name = _config.get_db(types._CONFIG_DB_NAME);

		String message = ""; 
		if (!strings.is_ok(name)) message = "WRONG " + keys.DB;
		else if (!strings.is_ok(host)) message = "WRONG " + keys.HOST;

		if (!message.equals(""))
		{
			db.manage_error(types.ERROR_DB_INFO, null, null, message);

			return strings.DEFAULT;
		}

		String url = "jdbc:mysql://" + host + ":3306/" + name;
		url += "?useUnicode=true&useJDBCCompliantTimezoneShift=true";
		url += "&useLegacyDatetimeCode=false&serverTimezone=UTC";

		return url;
	}

	private static String get_variable_value(String input_, boolean is_variable_)
	{
		String quote = (is_variable_ ? "`" : "'");

		return (quote + input_ + quote);
	}

	private static String get_query_cols(String[] array_)
	{
		String output = "";	

		for (String item: array_)
		{
			if (!strings.is_ok(item)) continue;

			if (!output.equals("")) output += ",";
			output += get_variable(item);			
		}

		return output;
	}

	private static String get_query_cols(HashMap<String, String> array_, String type_)
	{
		String output = "";

		for (Entry<String, String> entry: array_.entrySet())
		{    		
			String item = "";

			if (type_.equals(keys.KEY)) 
			{
				item = get_variable(entry.getKey());
			}
			else if (type_.equals(keys.VALUE)) 
			{
				item = get_value(entry.getValue());
			}
			else if (type_.equals(keys.ALL)) 
			{
				item = get_variable(entry.getKey()) + "=";
				item += get_value(entry.getValue());
			}
			if (item.equals("")) continue;

			if (!output.equals("")) output += ",";
			output += item;
		}

		return output;
	}
}