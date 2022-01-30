package accessory;

import java.util.HashMap;

public class _config 
{	
    //--- Initiliased via _ini.load().
	  
    private static HashMap<String, String> sql = new HashMap<String, String>(); 
    private static HashMap<String, String> basic = new HashMap<String, String>();
    
	static { _ini.load(); }
	
    //--------- 
        
	public static String get_sql(String key_)
    {
		return get(types._CONFIG_SQL, key_);
    }
	
    public static boolean update_sql(String key_, String val_)
    {
    	return update(types._CONFIG_SQL, key_, val_);
    }
	
    public static boolean matches_sql(String key_, String val_)
    {
    	return matches(types._CONFIG_SQL, key_, val_);
    }
    
	public static String get_basic(String key_)
    {
		return get(types._CONFIG_BASIC, key_);
    }
	
    public static boolean update_basic(String key_, String val_)
    {
    	return update(types._CONFIG_BASIC, key_, val_);
    }
	
    public static boolean matches_basic(String key_, String val_)
    {
    	return matches(types._CONFIG_BASIC, key_, val_);
    }
    
    @SuppressWarnings("unchecked")
	public static <x> x get(String type_, String key_)
    {
    	x val = null;
    	if (!strings.is_ok(type_) || !strings.is_ok(key_)) return defaults.get_generic();
    	
    	if (type_.equals(types._CONFIG_SQL) && sql.containsKey(key_)) val = (x)sql.get(key_);
    	else if (type_.equals(types._CONFIG_BASIC) && basic.containsKey(key_)) val = (x)basic.get(key_);
     	
    	return val;
    }
    
    public static <x> boolean update(String type_, String key_, x val_)
    {
    	return update_matches(type_, key_, val_, true, false);
    }
    
    public static <x> boolean matches(String type_, String key_, x val_)
    {
    	return update_matches(type_, key_, val_, false, false);
    }
    
    static <x> boolean update_ini(String type_, String key_, x val_)
    {
    	return update_matches(type_, key_, val_, true, true);
    }
    
    private static <x> boolean update_matches(String type_, String key_, x val_, boolean update_, boolean ini_)
    {
    	boolean is_ok = false;
    	if (!strings.is_ok(type_) || !strings.is_ok(key_)) return is_ok;
    	
    	if (type_.equals(types._CONFIG_SQL) && sql.containsKey(key_) && strings.is_ok(val_)) 
    	{
    		if (update_) 
    		{
    			is_ok = true;
    			if (!ini_ || (ini_ && !sql.containsKey(key_))) sql.put(key_, (String)val_);
    		}
    		else is_ok = sql.get(key_).equals(val_);
    	}
    	else if (type_.equals(types._CONFIG_BASIC) && basic.containsKey(key_) && strings.is_ok(val_)) 
    	{
    		if (update_) 
    		{
    			is_ok = true;
    			if (!ini_ || (ini_ && !basic.containsKey(key_))) basic.put(key_, (String)val_);
    		}
    		else is_ok = basic.get(key_).equals(val_);
    	}
    	
    	return is_ok;
    }
}