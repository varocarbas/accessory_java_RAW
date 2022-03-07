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
	
	public static String sanitise_string(String input_)
	{
		return strings.remove_escape_many(new String[] { "'", "\"" }, input_, false);
	}
	
	public static ArrayList<HashMap<String, String>> execute(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		String query = get_query(what_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_);

		boolean return_data = false;
		if (what_.equals(types.DB_QUERY_SELECT)) return_data = true;

		return (strings.is_ok(query) ? sql.execute_query(query, return_data, cols_) : null);
	}

	public static String get_data_type(db_field field_)
	{
		String output = "";
		if (!db_field.is_ok(field_)) return output;
		
		if 
		(
			field_._data._type.equals(types.DATA_STRING) || 
			field_._data._type.equals(types.DATA_TIMESTAMP)
		)
		{
			output = types.MYSQL_VARCHAR;
		}
		else if (field_._data._type.equals(types.DATA_INTEGER)) output = types.MYSQL_INT;
		else if (field_._data._type.equals(types.DATA_LONG)) output = types.MYSQL_LONG;
		else if (field_._data._type.equals(types.DATA_DECIMAL)) output = types.MYSQL_DECIMAL;
		else if (field_._data._type.equals(types.DATA_BOOLEAN)) output = types.MYSQL_TINYINT;
		else return output;
		
		output = types.remove_type(output, types.MYSQL);
		String size = get_data_type_size(field_);
		if (!strings.is_ok(size)) return output;
		
		output += "(" + size + ")";
		
		return output;
	}

	public static String get_data_type_size(db_field field_)
	{
		String size = "";
		int max = (field_._data._size._max > (double)numbers.MAX_INT ? 0 : (int)field_._data._size._max);
		
		if (field_._data._type.equals(types.DATA_BOOLEAN)) size = "1";
		else if (field_._data._type.equals(types.DATA_TIMESTAMP)) 
		{
			size = strings.to_string(dates.get_time_pattern(dates.DATE_TIME).length());
		}
		else if (field_._data._type.equals(types.DATA_DECIMAL))
		{
			int m = ((max > 65 || max < 1) ? defaults.MYSQL_DATA_SIZE_NUMBER : max);
			int d = field_._data._size._decimals;
			
			if (d < 0 || d > 30 || d > m)
			{
				d = defaults.MYSQL_DATA_SIZE_DECIMALS;
				if (d > m) d = m - 1;
			}
			
			size = (strings.to_string(m) + "," + strings.to_string(d));
		}
		else if (field_._data._type.equals(types.DATA_INTEGER) || field_._data._type.equals(types.DATA_LONG))
		{
			size = strings.to_string(max > numbers.MAX_DIGITS_INT ? defaults.MYSQL_DATA_SIZE_NUMBER : max);
		}
		else if (field_._data._type.equals(types.DATA_STRING))
		{
			size = strings.to_string(max > 255 ? defaults.MYSQL_DATA_SIZE_STRING : max);
		}
		
		return size;
	}
	
	private static String get_query(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{	
		String query = strings.DEFAULT;
		if (!sql.params_are_ok(what_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_)) return query;

		boolean is_ok = false;

		if (what_.equals(types.DB_QUERY_SELECT))
		{
			query = "SELECT ";
			query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
			query += " FROM " + get_variable(table_);

			if (strings.is_ok(where_)) query += " WHERE " + where_;
			if (strings.is_ok(order_)) query += " ORDER BY " + order_;
			if (max_rows_ > 0) query += " LIMIT " + max_rows_;	

			is_ok = true;
		}
		else if (what_.equals(types.DB_QUERY_INSERT))
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
		else if (what_.equals(types.DB_QUERY_UPDATE))
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
		else if (what_.equals(types.DB_QUERY_DELETE))
		{
			query = "DELETE FROM " + get_variable(table_);
			query += " WHERE " + where_;

			is_ok = true;
		}
		else if (what_.equals(types.DB_QUERY_TABLE_EXISTS))
		{
			query = "SHOW TABLES LIKE " + get_value(table_);			
			is_ok = true;
		}
		else if (what_.equals(types.DB_QUERY_TABLE_CREATE))
		{
			query = "";
			
			for (Entry<String, db_field> item: cols_info_.entrySet())
			{
				String col = item.getKey();
				String type = get_data_type(item.getValue());
				if (!strings.is_ok(col) || !strings.is_ok(type)) continue;
				
				if (!query.equals("")) query += ", ";
				query += get_variable(col) + " " + type;
			}

			if (!query.equals("")) 
			{
				query = "CREATE TABLE " + get_variable(table_) + " (" + query + ")";	
				is_ok = true;
			}
			else is_ok = false;
		}
		else if (what_.equals(types.DB_QUERY_TABLE_DROP))
		{
			query = "DROP TABLE " + get_variable(table_);			
			is_ok = true;
		}
		else if (what_.equals(types.DB_QUERY_TABLE_TRUNCATE))
		{
			query = "TRUNCATE TABLE " + get_variable(table_);			
			is_ok = true;
		}

		if (!is_ok)
		{
			db.manage_error(types.ERROR_DB_QUERY, null, null, query);
			query = strings.DEFAULT;
		}
		
		return query;
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