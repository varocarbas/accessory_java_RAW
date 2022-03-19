package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

abstract class db_mysql 
{	
	public static String QUOTE_VARIABLE = "`";
	public static String QUOTE_VALUE = "'";

	static { ini.load(); }
	
	public static ArrayList<HashMap<String, String>> execute_query(String query_)
	{
		String type = strings.DEFAULT;
				
		String[] temp = strings.split(query_, " ");
		if (arrays.get_size(temp) >= 2) type = db.check_type(temp[0]);

		if (!strings.is_ok(type))
		{
			db.manage_error(types.ERROR_DB_QUERY, query_, null, null);
			
			return null;
		}
 
		String[] cols = null;
		if (type.equals(types.DB_QUERY_SELECT))
		{
			String[] temp2 = strings.split(temp[1], ",");
			if (arrays.get_size(temp2) >= 1 && !temp2[0].trim().equals("*"))
			{
				ArrayList<String> cols2 = new ArrayList<String>();
				
				for (String item: temp2)
				{
					if (!strings.is_ok(item)) continue;
									
					int i = strings.index_of_outside(" from ", item, true, QUOTE_VARIABLE, QUOTE_VARIABLE);
					String item2 = strings.remove_escape(QUOTE_VARIABLE, item, true);

					if (i < 0) cols2.add(item2);
					else 
					{
						cols2.add(strings.substring(item2, 0, i));
						break;
					}
				}
				
				cols = arrays.to_array(cols2);
			}
		}
		
		return db_sql.execute_query(query_, db.query_returns_data(type), cols);
	}
	
	public static String sanitise_string(String input_)
	{
		return strings.remove_escape_many(new String[] { "'", "\"" }, input_, false);
	}
	
	public static ArrayList<HashMap<String, String>> execute(String type_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		String query = get_query(type_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_);

		return (strings.is_ok(query) ? db_sql.execute_query(query, db.query_returns_data(type_), cols_) : null);
	}
	
	public static HashMap<String, Object> get_data_type(String data_type_)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		String data_type = types.check_subtype(data_type_, types.get_subtypes(types.DATA, null), null, null);
		if (!strings.is_ok(data_type)) return output;
		
		String type = null;
		if (data_type.equals(types.DATA_BOOLEAN)) type = types.DB_MYSQL_DATA_TINYINT;
		else if (data_type.equals(types.DATA_STRING)) type = types.DB_MYSQL_DATA_VARCHAR;
		else if (data_type.equals(types.DATA_STRING_BIG)) type = types.DB_MYSQL_DATA_TEXT;
		else if (data_type.equals(types.DATA_TIMESTAMP)) type = types.DB_MYSQL_DATA_TIMESTAMP;
		else if (data_type.equals(types.DATA_INT)) type = types.DB_MYSQL_DATA_INT;
		else if (data_type.equals(types.DATA_LONG)) type = types.DB_MYSQL_DATA_BIGINT;
		else if (data_type.equals(types.DATA_DECIMAL)) type = types.DB_MYSQL_DATA_DECIMAL;
		else return output;
	
		output.put(keys.TYPE, type);
		output.put(keys.MAX, get_max_size(data_type_));
		
		return output;
	}
	
	public static int get_default_size(String data_type_)
	{
		int size = 0;
		
		String type = data.check_type(data_type_);
		if (!strings.is_ok(type)) return size;
		
		if (type.equals(types.DATA_BOOLEAN)) size = 1;
		else if (type.equals(types.DATA_STRING)) size = defaults.SIZE_MYSQL_VARCHAR;
		else if (type.equals(types.DATA_STRING_BIG)) size = defaults.SIZE_MYSQL_TEXT;
		else if (type.equals(types.DATA_TIMESTAMP)) size = dates.get_time_pattern(dates.DATE_TIME).length();
		else if (data.is_numeric(type)) size = defaults.SIZE_MYSQL_NUMBER;
		
		return size;
	}

	public static int get_max_size(String data_type_)
	{
		int max = 0;
		
		String data_type = data.check_type(data_type_);
		if (!strings.is_ok(data_type)) return max;
		
		if (data_type.equals(types.DATA_BOOLEAN)) max = 1;
		else if (data_type.equals(types.DATA_TIMESTAMP)) max = get_default_size(data_type_);		
		else if (data_type.equals(types.DATA_DECIMAL)) max = 64;
		else if (data_type.equals(types.DATA_INT)) max = numbers.MAX_DIGITS_INT;
		else if (data_type.equals(types.DATA_LONG)) max = numbers.MAX_DIGITS_LONG;
		else if (data_type.equals(types.DATA_STRING)) max = 255;
		else if (data_type.equals(types.DATA_STRING_BIG)) max = 65535;
		
		return max;
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

	private static String data_type_to_string(db_field field_)
	{
		String output = "";
		if (!db_field.is_ok(field_)) return output;

		HashMap<String, Object> info = get_data_type(field_._type);
		if (!arrays.is_ok(info)) return output;
		
		output = types.remove_type((String)info.get(keys.TYPE), types.DB_MYSQL_DATA);
		String size = get_data_type_size(field_);
		if (!strings.is_ok(size)) return output;
		
		output += "(" + size + ")";
		
		return output;
	}
	
	private static String get_data_type_size(db_field field_)
	{
		String output = strings.DEFAULT;

		int max = (field_._size > (double)numbers.MAX_INT ? 0 : field_._size);
		String type = field_._type;
		if (type.equals(types.DATA_TIMESTAMP)) return output;
			
		HashMap<String, Object> info = get_data_type(type);
		int max2 = (int)info.get(keys.MAX);
		
		int size_def = get_default_size(type);
		
		if (type.equals(types.DATA_BOOLEAN)) output = (String)info.get(keys.MAX);
		else if (type.equals(types.DATA_DECIMAL))
		{
			int m = ((max > max2 || max < 1) ? size_def : max);
			int d = field_._decimals;
			
			if (d < 0 || d > 30 || d > m)
			{
				d = defaults.SIZE_MYSQL_DECIMALS;
				if (d > m) d = m - 1;
			}
			
			output = (strings.to_string(m) + "," + strings.to_string(d));
		}
		else if (data.is_numeric(type) || data.is_string(type))
		{
			output = strings.to_string(max > max2 ? size_def : max);
		}
		
		return output;
	}

	private static String get_query(String type_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{	
		String query = strings.DEFAULT;
		if (!db_sql.params_are_ok(type_, table_, cols_, vals_, where_, max_rows_, order_, cols_info_)) return query;

		boolean is_ok = false;

		if (type_.equals(types.DB_QUERY_SELECT))
		{
			query = "SELECT ";
			query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
			query += " FROM " + get_variable(table_);

			if (strings.is_ok(where_)) query += " WHERE " + where_;
			if (strings.is_ok(order_)) query += " ORDER BY " + order_;
			if (max_rows_ > 0) query += " LIMIT " + max_rows_;	

			is_ok = true;
		}
		else if (type_.equals(types.DB_QUERY_INSERT))
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
		else if (type_.equals(types.DB_QUERY_UPDATE))
		{
			query = "UPDATE " + get_variable(table_); 
			
			String temp = get_query_cols(vals_, keys.FURTHER);
			if (strings.is_ok(temp)) 
			{
				query += " SET " + temp;
				is_ok = true;     		
			}
			
			if (strings.is_ok(where_)) query += " WHERE " + where_;
		}
		else if (type_.equals(types.DB_QUERY_DELETE))
		{
			query = "DELETE FROM " + get_variable(table_);
			query += " WHERE " + where_;

			is_ok = true;
		}
		else if (type_.equals(types.DB_QUERY_TABLE_EXISTS))
		{
			query = "SHOW TABLES LIKE " + get_value(table_);			
			is_ok = true;
		}
		else if (type_.equals(types.DB_QUERY_TABLE_CREATE))
		{
			query = "";

			for (Entry<String, db_field> item: cols_info_.entrySet())
			{
				String col = item.getKey();
				db_field field = item.getValue();
				String type = field._type;

				String type2 = data_type_to_string(field);
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
					else if (data.is_string(type)) def_val = get_variable_value(" ", false);
					
					if (strings.is_ok(def_val)) def_val = get_value(def_val);
				}
				
				if (strings.is_ok(def_val)) item2 += " default " + def_val;
				
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
		else if (type_.equals(types.DB_QUERY_TABLE_DROP))
		{
			query = "DROP TABLE " + get_variable(table_);			
			is_ok = true;
		}
		else if (type_.equals(types.DB_QUERY_TABLE_TRUNCATE))
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
		if (!strings.is_ok(name)) message = "WRONG DB";
		else if (!strings.is_ok(host)) message = "WRONG host";

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
		String quote = (is_variable_ ? QUOTE_VARIABLE : QUOTE_VALUE);

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
			else if (type_.equals(keys.FURTHER)) 
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