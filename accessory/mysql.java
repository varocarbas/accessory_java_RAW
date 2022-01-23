package accessory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Properties;

public class mysql 
{
    public static void update_info(String db_, String host_, String user_)
    {
    	sql.update_info(db_, host_, user_);
    }
    
    public static void update_error(boolean error_exit_)
    {
    	sql.update_error(error_exit_);
    }
    
    public static void truncate_table(String table_)
    {
    	sql.truncate_table(table_);
    }    
    
    public static HashMap<String, String>[] select(String table_, String[] cols_, String where_, int limit_)
    {
    	return sql.select(table_, cols_, where_, limit_);
    }

    public static void insert(String table_, HashMap<String, String> vals_)
    {
    	sql.insert(table_, vals_);
    }

    public static void update(String table_, HashMap<String, String> vals_, String where_)
    {
    	sql.update(table_, vals_, where_);
    }
        
    public static void delete(String table_, String where_)
    {
    	sql.delete(table_, where_);
    }
    
    static Connection connect(Properties properties) 
    {
    	Connection conn = null;
    
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(get_url() , properties);
		} 
        catch (Exception e) 
        {
        	sql.manage_error(types.ERROR_SQL_CONN, null, e); 
        	conn = null;
		}
        
        return conn;
    }
    
    private static String get_url()
    {    	
    	if (!strings.is_ok(sql.host) || !strings.is_ok(sql.db)) return strings.get_default();
    	
    	String url = "jdbc:mysql://" + sql.host + ":3306/" + sql.db;
    	url += "?useUnicode=true&useJDBCCompliantTimezoneShift=true";
    	url += "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    	
    	return url;
    }
}