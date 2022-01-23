package accessory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

class sql 
{
	static final String type_sql = types.SQL_MYSQL;
	
    static final String MAX_POOL = "500";

    static String db = strings.get_default();
    static String host = strings.get_default();
    static String user = strings.get_default();
    static boolean error_exit = true;

    static void update_info(String db_, String host_, String user_)
    {
    	if (strings.is_ok(db_)) db = db_;
    	if (strings.is_ok(host_)) host = host_;
    	if (strings.is_ok(user_)) user = user_;
    }
    
    static void update_error(boolean error_exit_)
    {
    	error_exit = error_exit_;
    }
    
    static void truncate_table(String table_)
    {
    	if (!strings.is_ok(table_)) 
    	{
    		manage_error(types.ERROR_SQL_QUERY, "Wrong TRUNCATE query", null);
    		
    		return;
    	}
    	
    	String query = "TRUNCATE TABLE " + get_variable(table_);
    	
    	execute_query(query, null);
    }    
    
    static HashMap<String, String>[] select(String table_, String[] cols_, String where_, int limit_)
    {
    	if (!strings.is_ok(table_)) 
    	{
    		manage_error(types.ERROR_SQL_QUERY, "Wrong SELECT query", null);
    		
    		return null;
    	}

    	String query = "SELECT ";
    	query += (arrays.is_ok(cols_) ? get_query_cols(cols_) : "*");     	
    	query += " FROM " + get_variable(table_);
    	
    	if (strings.is_ok(where_)) query += " WHERE " + where_;
    	if (limit_ > 0) query += " LIMIT " + limit_;

    	return execute_query(query, cols_);
    }

    static void insert(String table_, HashMap<String, String> vals_)
    {
    	boolean is_ok = false;
    	String query = "";
    	
    	if (strings.is_ok(table_) && arrays.is_ok(vals_))
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
    	
    	if (is_ok) execute_query(query, null);
    	else manage_error(types.ERROR_SQL_QUERY, "Wrong INSERT query", null);
    }

    static void update(String table_, HashMap<String, String> vals_, String where_)
    {
    	boolean is_ok = false;
    	String query = "";
    	
    	if (strings.is_ok(table_) && arrays.is_ok(vals_))
    	{
        	String temp = get_query_cols(vals_, keys.ALL);
        	if (strings.is_ok(temp)) 
        	{
        		query += " SET " + temp;
        		is_ok = true;     		
        	}        		
    	}
    	
    	if (is_ok) 
    	{
        	if (strings.is_ok(where_)) query += " WHERE " + where_;
        	
    		execute_query(query, null);
    	}
    	else manage_error(types.ERROR_SQL_QUERY, "Wrong UPDATE query", null);
    }
        
    static void delete(String table_, String where_)
    {
    	if (!strings.is_ok(table_) || !strings.is_ok(where_)) 
    	{
    		manage_error(types.ERROR_SQL_QUERY, "Wrong DELETE query", null);
    		
    		return; 
    	}
    	
    	String query = "DELETE FROM " + get_variable(table_);
    	query += " WHERE " + where_;
    	
    	execute_query(query, null);
    }

    static void manage_error(String type_, String query_, Exception e_)
    {
		errors.manage_db(type_, host, db, user, query_, e_, true, error_exit);
    }
    
    private static Connection connect() 
    {
    	Connection conn = null; 
    	
    	Properties properties = get_properties();
    	if (properties == null)
    	{
    		manage_error(types.ERROR_SQL_CONN, null, null);
    		
    		return conn;
    	}
    	
    	return connect_type(properties);
    }
    
    private static Connection connect_type(Properties properties) 
    {
    	Connection conn = null; 
    	
    	if (!strings.is_ok(types.check_subtype(type_sql, null)))
    	{
    		manage_error(types.ERROR_SQL_CONN, null, null);
    		  		
    		return conn;
    	}
    	
    	
    	if (strings.are_equal(type_sql, types.SQL_MYSQL))
    	{
    		conn = mysql.connect(properties);
    	}
    	
    	return conn;
    }
    
    private static Properties get_properties() 
    {	
    	String password = credentials.get_password(user, false, null);
    	if (!strings.is_ok(password)) return null;
    
    	Properties properties = new Properties();
    	
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("MaxPooledStatements", MAX_POOL);
        
        return properties;
    }
    
    private static void disconnect(Connection conn_) 
    {
        if (conn_ != null) 
        {
            try 
            {
            	conn_.close();
            	conn_ = null;
            } 
            catch (Exception e) 
            {
            	manage_error(types.ERROR_SQL_CONN, null, e);
            }
        }
    }
    
	private static HashMap<String, String>[] execute_query(String query_, String[] out_cols_)
	{
		if (!strings.is_ok(query_)) 
		{
			manage_error(types.ERROR_SQL_QUERY, "No query", null);
			
			return arrays.get_default();
		}

		Connection conn = connect();
		if (conn == null) return arrays.get_default();

		ArrayList<HashMap<String, String>> output = null;
		try 
		{
		    PreparedStatement statement = conn.prepareStatement(query_);

		    if (!arrays.is_ok(out_cols_)) 
		    {
		    	statement.executeUpdate();

		    	return arrays.get_default();
		    }
		    
		    output = new ArrayList<HashMap<String, String>>();
		    
		    try
		    {
			    ResultSet data = statement.executeQuery();
			    
			    String wrong = strings.get_default();
			    
	            while (data.next()) 
	            { 
	            	HashMap<String, String> row = new HashMap<String, String>();
	            	
	            	for (String col : out_cols_)
	            	{
	            		String val = 
	            		(
	            			(
	            				strings.is_ok(col) && 
	            				data.findColumn(col) > -1
	            			) 
	            			? data.getString(col) : wrong
	            		);
	            		if (!strings.is_ok(val)) val = wrong;
	            		
	            		row.put(col, val);
	            	}
	            	
	            	output.add(row);
	            }	
		    }
		    catch (Exception e) 
		    {
		    	manage_error(types.ERROR_SQL_QUERY, query_, e);
		    }
		} 
		catch (Exception e) 
		{
			manage_error(types.ERROR_SQL_QUERY, query_, e);
		} 
		finally { disconnect(conn); }
		
		return (!arrays.is_ok(output) ? arrays.get_default() : arrays.to_array(output));
	}
    
    private static String get_value(String input_)
    {
    	return get_variable_value(input_, false);
    }
    
    private static String get_variable(String input_)
    {
    	return get_variable_value(input_, true);   	
    }

	private static String get_query_cols(String[] array_)
	{
		if (!arrays.is_ok(array_)) return strings.get_default();
		
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
		if (!arrays.is_ok(array_)) return strings.get_default();
		
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
    
    private static String get_variable_value(String input_, boolean is_variable_)
    {
    	if (!strings.is_ok(input_)) return strings.get_default();
    	
    	String quote = (is_variable_ ? "`" : "'");
    	
    	return (quote + input_ + quote);
    }
}