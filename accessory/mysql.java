package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

class mysql 
{  
	static { _ini.load(); }
	
    static String get_query_truncate_table(String table_)
    {
    	return "TRUNCATE TABLE " + get_variable(table_);
    }    
    
    static String get_query_select(String table_, String[] cols_, String where_, int limit_)
    {
    	String query = "SELECT ";
    	query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
    	query += " FROM " + get_variable(table_);
    	
    	if (strings.is_ok(where_)) query += " WHERE " + where_;
    	if (limit_ > 0) query += " LIMIT " + limit_;

    	return query;
    }

    static String get_query_insert(String table_, HashMap<String, String> vals_)
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

    static String get_query_update(String table_, HashMap<String, String> vals_, String where_)
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
        
    static String get_query_delete(String table_, String where_)
    {
    	String query = "DELETE FROM " + get_variable(table_);
    	query += " WHERE " + where_;
    	
    	return query;
    }
    
    static Connection connect(Properties properties) 
    {
    	Connection conn = null;
    
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(get_connect_url() , properties);
		} 
        catch (Exception e) 
        {
        	sql.manage_error(types.ERROR_SQL_CONN, null, e, null); 
        	conn = null;
		}
        
        return conn;
    }
    
    private static String get_connect_url()
    {   
    	String db = _config.get_sql(types._CONFIG_SQL_DB);
    	String host = _config.get_sql(types._CONFIG_SQL_HOST);
    	if (!strings.is_ok(db) || !strings.is_ok(host)) return strings.DEFAULT;
    	
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