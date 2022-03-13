package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

class mysql 
{	
	static { ini.load(); }
	
	public static String sanitise_string(String input_)
	{
		return strings.remove_escape_many(new String[] { "'", "\"" }, input_, false);
	}
	
	public static ArrayList<HashMap<String, String>> execute(String what_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		String query = get_query(what_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_);

		boolean return_data = 
		(
			what_.equals(types.DB_QUERY_SELECT) || what_.equals(types.DB_QUERY_TABLE_EXISTS)
		);

		return (strings.is_ok(query) ? sql.execute_query(query, return_data, cols_) : null);
	}

	public static String get_data_type(db_field field_)
	{
		String output = "";
		if (!db_field.is_ok(field_)) return output;
		
		if (field_._data._type.equals(types.DATA_STRING)) output = types.MYSQL_VARCHAR;
		else if (field_._data._type.equals(types.DATA_TIMESTAMP)) output = types.MYSQL_TIMESTAMP;
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
		String output = strings.DEFAULT;

		int max = (field_._data._size._max > (double)numbers.MAX_INT ? 0 : (int)field_._data._size._max);
		String type = field_._data._type;
		if (type.equals(types.DATA_TIMESTAMP)) return output;
			
		HashMap<String, Object> info = get_mysql_data_type(type);
		int max2 = (int)info.get(keys.MAX);
		
		if (type.equals(types.DATA_BOOLEAN)) output = (String)info.get(keys.MAX);
		
		if (type.equals(types.DATA_DECIMAL))
		{
			int m = ((max > max2 || max < 1) ? defaults.MYSQL_DATA_SIZE_NUMBER : max);
			int d = field_._data._size._decimals;
			
			if (d < 0 || d > 30 || d > m)
			{
				d = defaults.MYSQL_DATA_SIZE_DECIMALS;
				if (d > m) d = m - 1;
			}
			
			output = (strings.to_string(m) + "," + strings.to_string(d));
		}
		else if (type.equals(types.DATA_INTEGER) || type.equals(types.DATA_LONG))
		{
			output = strings.to_string(max > max2 ? defaults.MYSQL_DATA_SIZE_NUMBER : max);
		}
		else if (type.equals(types.DATA_STRING))
		{
			output = strings.to_string(max > max2 ? defaults.MYSQL_DATA_SIZE_VARCHAR: max);
		}
		
		return output;
	}

	public static int get_data_max_size(String data_type_)
	{
		int max = 0;
		
		String data_type = data.check_type(data_type_);
		if (!strings.is_ok(data_type)) return max;
		
		if (data_type.equals(types.DATA_BOOLEAN)) max = 1;
		else if (data_type.equals(types.DATA_TIMESTAMP)) max = dates.get_time_pattern(dates.DATE_TIME).length();		
		else if (data_type.equals(types.DATA_DECIMAL)) max = 64;
		else if (data_type.equals(types.DATA_INTEGER)) max = numbers.MAX_DIGITS_INT;
		else if (data_type.equals(types.DATA_LONG)) max = numbers.MAX_DIGITS_INT;
		else if (data_type.equals(types.DATA_STRING)) max = 255;
		
		return max;
	}
	
	public static double get_numeric_data_max(String data_type_)
	{
		double max = 0.0;
		
		String data_type = data.check_type(data_type_);
		if (!data.is_numeric(data_type)) return max;
		
		if (data_type.equals(types.DATA_DECIMAL)) max = Math.pow(10, get_data_max_size(data_type));
		else if (data_type.equals(types.DATA_INTEGER)) max = (double)numbers.MAX_INT;
		else if (data_type.equals(types.DATA_LONG)) max = (double)numbers.MAX_INT;
		
		return max;
	}
	
	public static boolean numeric_val_size_is_ok(double val_, String data_type_)
	{
		String type = data.check_type(data_type_);
		if (!data.is_numeric(type)) return false;
		
		double max = get_numeric_data_max(type);
		
		return (val_ <= max && val_ >= -1 * max);
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
	
	private static HashMap<String, Object> get_mysql_data_type(String data_type_)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		String type = null;
		
		if (data_type_.equals(types.DATA_BOOLEAN)) type = types.MYSQL_TINYINT;
		else if (data_type_.equals(types.DATA_TIMESTAMP)) type = types.MYSQL_TIMESTAMP;
		else if (data_type_.equals(types.DATA_DECIMAL)) type = types.MYSQL_DECIMAL;
		else if (data_type_.equals(types.DATA_INTEGER)) type = types.MYSQL_INT;
		else if (data_type_.equals(types.DATA_LONG)) type = types.MYSQL_LONG;
		else if (data_type_.equals(types.DATA_STRING)) type = types.MYSQL_VARCHAR;
	
		output.put(keys.TYPE, type);
		output.put(keys.MAX, get_data_max_size(data_type_));
		
		return output;
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
				db_field field = item.getValue();
				String type = field._data._type;
				
				String type2 = get_data_type(field);
				if (!strings.is_ok(col) || !strings.is_ok(type2)) continue;
				
				if (!query.equals("")) query += ", ";
				String item2 = get_variable(col) + " " + type2;
				
				String[] further = create_table_check_further(field._further);	
				String def_val = strings.DEFAULT;
				
				if (type.equals(types.DATA_TIMESTAMP)) def_val = "current_timestamp";
				else if (!arrays.value_exists(further, types.DB_FIELD_FURTHER_AUTO_INCREMENT))
				{
					if (generic.is_ok(field._default)) def_val = strings.to_string(field._default);
					else if (data.is_numeric(type)) def_val = "0";
					else if (type.equals(types.DATA_STRING)) def_val = get_variable_value(" ", false);
					
					if (strings.is_ok(def_val)) def_val = get_value(def_val);
				}
				
				if (strings.is_ok(def_val)) item2 += " " + keys.DEFAULT + " " + def_val;
				
				String further2 = create_table_further_to_query(further);
				if (strings.is_ok(further2)) item2 += " " + further2;
					
				query += item2;
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

	private static String[] create_table_check_further(String[] further_)
	{
		ArrayList<String> output = new ArrayList<String>();
		if (!arrays.is_ok(further_)) return null;
		
		for (String further: further_)
		{
			String further2 = db_field.check_further(further);
			if (!strings.is_ok(further2)) continue;
			
			output.add(further2);
		}
		
		return arrays.to_array(output);
	}
	
	private static String create_table_further_to_query(String[] further_)
	{	
		String output = "";
		if (!arrays.is_ok(further_)) return output;
		
		for (String further: further_)
		{
			String further2 = db_field.check_further(further);
			if (!strings.is_ok(further2)) continue;
			
			if (further2.equals(types.DB_FIELD_FURTHER_KEY_PRIMARY))
			{
				output = "primary key";
				break;
			}
			else if (further2.equals(types.DB_FIELD_FURTHER_KEY_UNIQUE)) 
			{
				output = "unique key";
				break;
			}
		}
		
		if (arrays.value_exists(further_, types.DB_FIELD_FURTHER_AUTO_INCREMENT))
		{
			if (!output.equals("")) output += " ";
			output += types.remove_type(types.DB_FIELD_FURTHER_AUTO_INCREMENT, types.DB_FIELD_FURTHER);
		}
		else if (arrays.value_exists(further_, types.DB_FIELD_FURTHER_TIMESTAMP))
		{
			if (!output.equals("")) output += " ";
			output += "on update current_timestamp";
		}
		
		return output;
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