package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

class db_mysql extends parent_db
{	
	private static final String VARCHAR = types.DB_MYSQL_DATA_VARCHAR;
	private static final String TEXT = types.DB_MYSQL_DATA_TEXT;
	private static final String INT = types.DB_MYSQL_DATA_INT;
	private static final String TINYINT = types.DB_MYSQL_DATA_TINYINT;
	private static final String BIGINT = types.DB_MYSQL_DATA_BIGINT;
	private static final String DECIMAL = types.DB_MYSQL_DATA_DECIMAL;
	private static final String TIMESTAMP = types.DB_MYSQL_DATA_TIMESTAMP;
	
	private static final String QUOTE_VARIABLE = "`";
	private static final String QUOTE_VALUE = "'";

	private static final int DEFAULT_SIZE_NUMBER = _defaults.DB_SIZE_MYSQL_NUMBER;
	private static final int DEFAULT_SIZE_DECIMALS = _defaults.DB_SIZE_MYSQL_DECIMALS;
	private static final int DEFAULT_SIZE_VARCHAR = _defaults.DB_SIZE_MYSQL_VARCHAR;
	private static final int DEFAULT_SIZE_TEXT = _defaults.DB_SIZE_MYSQL_TEXT;
	
	public ArrayList<HashMap<String, String>> execute_query(String source_, String query_)
	{
		String type = db.check_type(strings.substring_before(" ", query_, true));

		if (!strings.is_ok(type))
		{
			db.manage_error(source_, db.ERROR_QUERY, query_, null, null);
			
			return null;
		}
		
		String query = strings.substring_after(type, query_, true);
		
		String[] cols = null;
		if (type.equals(db.SELECT))
		{
			String temp = strings.substring_before(query, strings.index_of_outside(" from ", query, true, QUOTE_VARIABLE, QUOTE_VARIABLE));

			if (strings.is_ok(temp))
			{
				String[] temp2 = strings.split(",", temp);
				if (arrays.get_size(temp2) > 1)
				{
					ArrayList<String> cols2 = new ArrayList<String>();
					
					for (String item: temp2)
					{
						if (!strings.is_ok(item)) continue;

						String item2 = strings.remove(QUOTE_VARIABLE, item).trim();
						cols2.add(item2); 
					}
					
					cols = arrays.to_array(cols2);
				}
				else 
				{
					String item = temp.trim();
					if (!item.equals("*")) cols = new String[] { item };
				}
			}
		}
		
		return db_sql.execute_query(source_, query_, db.query_returns_data(type), cols);
	}
	
	public String sanitise_string(String input_) { return strings.escape(new String[] { "'", "\"" }, input_); }
	
	public ArrayList<HashMap<String, String>> execute(String source_, String type_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{
		String query = get_query(source_, type_, cols_, vals_, where_, max_rows_, order_, cols_info_);

		return (strings.is_ok(query) ? db_sql.execute_query(source_, query, db.query_returns_data(type_), cols_) : null);
	}
	
	public HashMap<String, Object> get_data_type(String data_type_)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		String data_type = data.check_type(data_type_);
		if (!strings.is_ok(data_type)) return output;
		
		String type = null;
		if (data.is_boolean(data_type)) type = TINYINT;
		else if (data_type.equals(data.STRING_SMALL)) type = VARCHAR;
		else if (data_type.equals(data.STRING_BIG)) type = TEXT;
		else if (data_type.equals(data.TIMESTAMP)) type = TIMESTAMP;
		else if (data_type.equals(data.TINYINT)) type = TINYINT;
		else if (data_type.equals(data.INT)) type = INT;
		else if (data_type.equals(data.LONG)) type = BIGINT;
		else if (data_type.equals(data.DECIMAL)) type = DECIMAL;
		else return output;
	
		output.put(_keys.TYPE, type);
		output.put(_keys.MAX, get_max_size(data_type_));
		
		return output;
	}
	
	public long get_default_size(String data_type_)
	{
		long size = 0;
		
		String type = data.check_type(data_type_);
		if (!strings.is_ok(type)) return size;
		
		if (type.equals(data.BOOLEAN)) size = 1;
		else if (type.equals(data.STRING_SMALL)) size = DEFAULT_SIZE_VARCHAR;
		else if (type.equals(data.STRING_BIG)) size = DEFAULT_SIZE_TEXT;
		else if (type.equals(data.TIMESTAMP)) size = dates.get_length(dates.FORMAT_TIMESTAMP);
		else if (data.is_number(type)) size = DEFAULT_SIZE_NUMBER;
		
		return size;
	}

	public long get_max_size(String data_type_)
	{
		long max = 0;
		
		String data_type = data.check_type(data_type_);
		if (!strings.is_ok(data_type)) return max;
		
		if (data.is_boolean(data_type)) max = 1;
		else if (data_type.equals(data.TIMESTAMP)) max = dates.get_length(dates.FORMAT_TIMESTAMP);		
		else if (data_type.equals(data.DECIMAL)) max = 64;
		else if (data_type.equals(data.TINYINT)) max = 127;
		else if (data_type.equals(data.INT)) max = numbers.MAX_INT;
		else if (data_type.equals(data.LONG)) max = numbers.MAX_LONG;
		else if (data_type.equals(data.STRING_SMALL)) max = 255;
		else if (data_type.equals(data.STRING_BIG)) max = 65535;

		return max;
	}
	
	public String get_value(String input_) { return get_variable_value(input_, false); }

	public String get_variable(String input_) { return get_variable_value(input_, true); } 

	protected Connection connect_internal(String source_, Properties properties_) 
	{
		Connection conn = null;

		String url = get_connect_url(source_);
		if (!strings.is_ok(url)) return conn;

		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, properties_);
		} 
		catch (Exception e) 
		{
			db.manage_error(source_, db.ERROR_CONN, null, e, null); 
			conn = null;
		}

		return conn;
	} 

	private String get_connect_url(String source_)
	{   
		String host = db.get_host(db.get_setup(source_));
		String name = db.get_db_name(db.get_db(source_));

		String message = ""; 
		if (!strings.is_ok(name)) message = "WRONG DB";
		else if (!strings.is_ok(host)) message = "WRONG host";

		if (!message.equals(""))
		{
			db.manage_error(source_, db.ERROR_INFO, null, null, message);

			return strings.DEFAULT;
		}

		String url = "jdbc:mysql://" + host + ":3306/" + name;
		url += "?useUnicode=true&useJDBCCompliantTimezoneShift=true";
		url += "&useLegacyDatetimeCode=false&serverTimezone=UTC";

		return url;
	}
	
	private String data_type_to_string(db_field field_)
	{
		String output = "";
		if (!db_field.is_ok(field_)) return output;

		HashMap<String, Object> info = get_data_type(field_.get_type());
		if (!arrays.is_ok(info)) return output;
		
		output = types.remove_type((String)info.get(_keys.TYPE), types.DB_MYSQL_DATA);
		String size = get_data_type_size(field_);
		if (!strings.is_ok(size)) return output;
		
		output += "(" + size + ")";
		
		return output;
	}
	
	private String get_data_type_size(db_field field_)
	{
		String output = strings.DEFAULT;

		long max = (field_.get_size() > get_max_size(data.LONG) ? 0 : field_.get_size());
		String type = field_.get_type();
		if (type.equals(data.TIMESTAMP)) return output;
			
		HashMap<String, Object> info = get_data_type(type);
		long max2 = (long)info.get(_keys.MAX);
		
		long size_def = get_default_size(type);
		
		if (data.is_boolean(type)) output = strings.to_string(info.get(_keys.MAX));
		else if (type.equals(data.DECIMAL))
		{
			long m = ((max > max2 || max < 1) ? size_def : max);
			long d = field_.get_decimals();
			
			if (d < 0 || d > 30 || d > m)
			{
				d = DEFAULT_SIZE_DECIMALS;
				if (d > m) d = m - 1;
			}
			
			output = (strings.to_string(m) + "," + strings.to_string(d));
		}
		else if (data.is_number(type) || data.is_string(type))
		{
			output = strings.to_string(max > max2 ? size_def : max);
		}
		
		return output;
	}

	private String get_query(String source_, String type_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_)
	{	
		String query = strings.DEFAULT;
		if (!db_sql.params_are_ok(source_, type_, cols_, vals_, where_, max_rows_, order_, cols_info_)) return query;

		boolean is_ok = false;
		String table = db.get_table(source_);

		if (type_.equals(db.SELECT))
		{
			query = "SELECT ";
			query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
			query += " FROM " + get_variable(table);

			if (strings.is_ok(where_)) query += " WHERE " + where_;
			if (strings.is_ok(order_)) query += " ORDER BY " + order_;
			if (max_rows_ > 0) query += " LIMIT " + max_rows_;	

			is_ok = true;
		}
		else if (type_.equals(db.INSERT))
		{
			query = "INSERT INTO " + get_variable(table); 
			String temp = get_query_cols(vals_, _keys.KEY);

			if (strings.is_ok(temp)) 
			{
				query += "(" + temp + ")";

				temp = get_query_cols(vals_, _keys.VALUE);
				if (strings.is_ok(temp)) 
				{
					query += " VALUES (" + temp + ")"; 
					is_ok = true;
				}      		
			} 
		}
		else if (type_.equals(db.UPDATE))
		{
			query = "UPDATE " + get_variable(table); 
			
			String temp = get_query_cols(vals_, _keys.FURTHER);
			if (strings.is_ok(temp)) 
			{
				query += " SET " + temp;
				is_ok = true;     		
			}
			
			if (strings.is_ok(where_)) query += " WHERE " + where_;
		}
		else if (type_.equals(db.DELETE))
		{
			query = "DELETE FROM " + get_variable(table);
			query += " WHERE " + where_;

			is_ok = true;
		}
		else if (type_.equals(db.TABLE_EXISTS))
		{
			query = "SHOW TABLES LIKE " + get_value(table);			
			is_ok = true;
		}
		else if (type_.equals(db.TABLE_CREATE))
		{
			query = "";

			for (Entry<String, db_field> item: cols_info_.entrySet())
			{
				String col = item.getKey();
				db_field field = item.getValue();
				String type = field.get_type();

				String type2 = data_type_to_string(field);
				if (!strings.is_ok(col) || !strings.is_ok(type2)) continue;
				
				if (!query.equals("")) query += ", ";
				String item2 = get_variable(col) + " " + type2;

				String[] further = create_table_check_further(field.get_further());					
				String def_val = strings.DEFAULT;
				
				if (type.equals(data.TIMESTAMP)) def_val = "current_timestamp";
				else if (!arrays.value_exists(further, db_field.AUTO_INCREMENT))
				{
					def_val = db.input_to_string(source_, field.get_default(), type, true);	
					
					if (!strings.is_ok(def_val))
					{
						if (data.is_number(type) || data.is_boolean(type)) def_val = "0";
						else if (data.is_string(type)) def_val = " ";						
					}	
					
					if (strings.is_ok(def_val, true)) def_val = get_variable_value(def_val, false);
				}
				if (strings.is_ok(def_val)) item2 += " default " + def_val;
				
				String further2 = create_table_further_to_query(further);
				if (strings.is_ok(further2)) item2 += " " + further2;
					
				query += item2;
			}
		
			if (!query.equals("")) 
			{
				query = "CREATE TABLE " + get_variable(table) + " (" + query + ")";	
				is_ok = true;
			}
			else is_ok = false;	
		}
		else if (type_.equals(db.TABLE_DROP))
		{
			query = "DROP TABLE " + get_variable(table);			
			is_ok = true;
		}
		else if (type_.equals(db.TABLE_TRUNCATE))
		{
			query = "TRUNCATE TABLE " + get_variable(table);			
			is_ok = true;
		}

		if (!is_ok)
		{
			db.manage_error(source_, db.ERROR_QUERY, null, null, query);
			query = strings.DEFAULT;
		}
		
		return query;
	}

	private String[] create_table_check_further(String[] further_)
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
	
	private String create_table_further_to_query(String[] further_)
	{	
		String output = "";
		if (!arrays.is_ok(further_)) return output;
		
		for (String further: further_)
		{
			String further2 = db_field.check_further(further);
			if (!strings.is_ok(further2)) continue;
			
			if (further2.equals(db_field.KEY_PRIMARY))
			{
				output = "primary key";
				break;
			}
			else if (further2.equals(db_field.KEY_UNIQUE)) 
			{
				output = "unique key";
				break;
			}
		}
		
		if (arrays.value_exists(further_, db_field.AUTO_INCREMENT))
		{
			if (!output.equals("")) output += " ";
			output += types.remove_type(db_field.AUTO_INCREMENT, types.DB_FIELD_FURTHER);
		}
		else if (arrays.value_exists(further_, db_field.TIMESTAMP))
		{
			if (!output.equals("")) output += " ";
			output += "on update current_timestamp";
		}
		
		return output;
	}

	private String get_variable_value(String input_, boolean is_variable_)
	{
		String quote = (is_variable_ ? QUOTE_VARIABLE : QUOTE_VALUE);

		return (quote + input_ + quote);
	}

	private String get_query_cols(String[] array_)
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

	private String get_query_cols(HashMap<String, String> array_, String type_)
	{
		String output = "";

		for (Entry<String, String> entry: array_.entrySet())
		{    		
			String item = "";

			if (type_.equals(_keys.KEY)) 
			{
				item = get_variable(entry.getKey());
			}
			else if (type_.equals(_keys.VALUE)) 
			{
				item = get_value(entry.getValue());
			}
			else if (type_.equals(_keys.FURTHER)) 
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