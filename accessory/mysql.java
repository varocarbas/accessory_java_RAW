package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

class mysql 
{  
	static { _ini.load(); }

	public static String get_query(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_)
	{
		String query = strings.DEFAULT;
		
		if (what_.equals(sql.TRUNCATE)) query = get_query_truncate_table(table_);
		else if (what_.equals(sql.SELECT)) query = get_query_select(table_, cols_, where_, max_rows_, order_);
		else if (what_.equals(sql.INSERT)) query = get_query_insert(table_, vals_);	
		else if (what_.equals(sql.UPDATE)) query = get_query_update(table_, vals_, where_);	
		else if (what_.equals(sql.DELETE)) query = get_query_delete(table_, where_);	
		
		return query;
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
			sql.manage_error(types.ERROR_SQL_CONN, null, e, null); 
			conn = null;
		}

		return conn;
	}
	
	private static String get_query_truncate_table(String table_)
	{
		return "TRUNCATE TABLE " + get_variable(table_);
	}    

	private static String get_query_select(String table_, String[] cols_, String where_, int max_rows_, String order_)
	{
		String query = "SELECT ";
		query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
		query += " FROM " + get_variable(table_);

		if (strings.is_ok(where_)) query += " WHERE " + where_;
		if (strings.is_ok(order_)) query += " ORDER BY " + order_;
		if (max_rows_ > 0) query += " LIMIT " + max_rows_;

		return query;
	}

	private static String get_query_insert(String table_, HashMap<String, String> vals_)
	{
		boolean is_ok = false;

		String query = "INSERT INTO " + get_variable(table_); 
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

		return (is_ok ? query : strings.DEFAULT);
	}

	private static String get_query_update(String table_, HashMap<String, String> vals_, String where_)
	{
		boolean is_ok = false;
		String query = "";

		String temp = get_query_cols(vals_, keys.ALL);
		if (strings.is_ok(temp)) 
		{
			query += " SET " + temp;
			is_ok = true;     		
		} 

		return (is_ok ? query : strings.DEFAULT);    	
	}

	private static String get_query_delete(String table_, String where_)
	{
		String query = "DELETE FROM " + get_variable(table_);
		query += " WHERE " + where_;

		return query;
	}

	private static String get_connect_url()
	{   
		String host = _config.get_sql(types._CONFIG_SQL_HOST);
		String db = _config.get_sql(types._CONFIG_SQL_DB);
		
		String message = ""; 
		if (!strings.is_ok(db)) message = "WRONG " + keys.DB;
		else if (!strings.is_ok(host)) message = "WRONG " + keys.HOST;
		
		if (!message.equals(""))
		{
			sql.manage_error(types.ERROR_SQL_INFO, null, null, message);
			
			return strings.DEFAULT;
		}
		
		String url = "jdbc:mysql://" + host + ":3306/" + db;
		url += "?useUnicode=true&useJDBCCompliantTimezoneShift=true";
		url += "&useLegacyDatetimeCode=false&serverTimezone=UTC";

		return url;
	}

	private static String get_value(String input_)
	{
		return get_variable_value(input_, false);
	}

	private static String get_variable(String input_)
	{
		return get_variable_value(input_, true);   	
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