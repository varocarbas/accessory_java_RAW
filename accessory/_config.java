package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class _config 
{	
    //--- Initiliased via _ini.load().
	
	private static HashMap<String, String> basic = new HashMap<String, String>();
    private static HashMap<String, String> sql = new HashMap<String, String>(); 
    private static HashMap<String, String> credentials = new HashMap<String, String>();
    
	static { _ini.load(); }
	
    //--------- 
    
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
    
	public static String get_credentials(String key_)
    {
		return get(types._CONFIG_CREDENTIALS, key_);
    }
	
    public static boolean update_credentials(String key_, String val_)
    {
    	return update(types._CONFIG_CREDENTIALS, key_, val_);
    }
	
    public static boolean matches_credentials(String key_, String val_)
    {
    	return matches(types._CONFIG_CREDENTIALS, key_, val_);
    }
    
    @SuppressWarnings("unchecked")
	public static <x> x get(String type_, String key_)
    {
    	x val = null;
    	if (!strings.is_ok(type_) || !strings.is_ok(key_)) return defaults.get_generic();
    	
    	if (type_.equals(types._CONFIG_BASIC) && basic.containsKey(key_)) val = (x)basic.get(key_);
    	else if (type_.equals(types._CONFIG_SQL) && sql.containsKey(key_)) val = (x)sql.get(key_);
    	else if (type_.equals(types._CONFIG_CREDENTIALS) && credentials.containsKey(key_)) val = (x)credentials.get(key_);
    	
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
    	
    	if (val_ instanceof String)
    	{
    		if (type_.equals(types._CONFIG_BASIC)) 
        	{
        		is_ok = update_matches_basic(key_, (String)val_, update_, ini_);
        	} 
    		else if (type_.equals(types._CONFIG_SQL)) 
        	{
        		is_ok = update_matches_sql(key_, (String)val_, update_, ini_);
        	}   
    		else if (type_.equals(types._CONFIG_CREDENTIALS)) 
        	{
        		is_ok = update_matches_credentials(key_, (String)val_, update_, ini_);
        	}  
    	}
    	
    	return is_ok;
    }
    
    private static boolean update_matches_basic(String key_, String val_, boolean update_, boolean ini_)
    {
    	String[] keys = update_matches_get_keys(key_, ini_, types._CONFIG_BASIC, null);
    	
    	for (String key: keys)
    	{
        	boolean key_is_ok = basic.containsKey(key);
        	boolean val_is_ok = strings.is_ok(val_);

    		if (update_) 
    		{
    			if ((!ini_ && key_is_ok && val_is_ok) || (ini_ && !key_is_ok)) 
    			{ 
    				basic.put(key, (val_is_ok ? val_ : strings.DEFAULT)); 
    				return true;
    			}
    		}
    		else if (key_is_ok) return strings.are_equal(basic.get(key), val_);	
    	}
		
		return false;
    }
    
    private static boolean update_matches_sql(String key_, String val_, boolean update_, boolean ini_)
    {
    	String[] keys = update_matches_get_keys
    	(
    		key_, ini_, types._CONFIG_SQL, new String[] { types._CONFIG_SQL_CREDENTIALS }
    	);
    	
    	for (String key: keys)
    	{
        	boolean key_is_ok = sql.containsKey(key);
        	boolean val_is_ok = strings.is_ok(val_);

    		if (update_) 
    		{
    			if ((!ini_ && key_is_ok && val_is_ok) || (ini_ && !key_is_ok)) 
    			{ 
    				sql.put(key, (val_is_ok ? val_ : strings.DEFAULT)); 
    				return true;
    			}
    		}
    		else if (key_is_ok) return strings.are_equal(sql.get(key), val_);	
    	}
		
		return false;
    }
    
    private static boolean update_matches_credentials(String key_, String val_, boolean update_, boolean ini_)
    {
    	String[] keys = update_matches_get_keys
    	(
    		key_, ini_, types._CONFIG_CREDENTIALS, new String[] { types._CONFIG_CREDENTIALS_FILE }
    	);
    	
    	for (String key: keys)
    	{
        	boolean key_is_ok = credentials.containsKey(key);
        	boolean val_is_ok = strings.is_ok(val_);

    		if (update_) 
    		{
    			if ((!ini_ && key_is_ok && val_is_ok) || (ini_ && !key_is_ok)) 
    			{ 
    				credentials.put(key, (val_is_ok ? val_ : strings.DEFAULT)); 
    				return true;
    			}
    		}
    		else if (key_is_ok) return strings.are_equal(credentials.get(key), val_);	
    	}
		
		return false;
    }
    
    private static String[] update_matches_get_keys(String key_, boolean ini_, String type_, String[] subtypes_)
    {
    	ArrayList<String> all_keys = new ArrayList<String>();
    	all_keys.add(key_);    	
    	if (!ini_) all_keys.add(types.add_type(key_, update_matches_get_keys_type(key_, type_, subtypes_)));  

    	return arrays.to_array(all_keys);
    }
    
    private static String update_matches_get_keys_type(String key_, String type_, String[] subtypes_)
    {
    	String type = type_;
    	if (!arrays.is_ok(subtypes_)) return type;
    
    	for (String subtype: subtypes_)
    	{
    		for (String type2: types.get_subtypes(subtype, null))
    		{
    			if (strings.are_equivalent(key_, types.remove_type(type2, subtype)))
    			{
    				type = subtype;
    				break;
    			}
    		}
    	}
    	
    	return type;
    }
}