package accessory;

import java.util.HashMap;

public class _keys extends parent_keys
{
	private static _keys _instance = new _keys(); 

	public _keys() { }
	public static void populate() { _instance.populate_internal(); }
	
	public static final String TYPE = get_key(_types.WHAT_TYPE);
	public static final String KEY = get_key(_types.WHAT_KEY);
	public static final String VALUE = get_key(_types.WHAT_VALUE);
	public static final String FURTHER = get_key(_types.WHAT_FURTHER);
	public static final String INFO = get_key(_types.WHAT_INFO);
	public static final String MIN = get_key(_types.WHAT_MIN);
	public static final String MAX = get_key(_types.WHAT_MAX);	
	public static final String USERNAME = get_key(_types.WHAT_USERNAME);
	public static final String PASSWORD = get_key(_types.WHAT_PASSWORD);
	public static final String ID = get_key(_types.WHAT_ID);
	public static final String USER = get_key(_types.WHAT_USER);
	public static final String QUERY = get_key(_types.WHAT_QUERY);
	public static final String INSTANCE = get_key(_types.WHAT_INSTANCE);
	public static final String MESSAGE = get_key(_types.WHAT_MESSAGE);
	public static final String PATH = get_key(_types.WHAT_PATH);
	
	protected HashMap<String, String> get_startup_roots() { return null; }
	
	protected HashMap<String, HashMap<String, String>> get_startup_merged_roots() { return null; }
	
	protected HashMap<String, HashMap<String, String>> get_startup_merged_types() { return null; }
}