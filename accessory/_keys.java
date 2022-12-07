package accessory;

public abstract class _keys 
{
	public static final String TYPE = get_key(types.WHAT_TYPE);
	public static final String KEY = get_key(types.WHAT_KEY);
	public static final String VALUE = get_key(types.WHAT_VALUE);
	public static final String FURTHER = get_key(types.WHAT_FURTHER);
	public static final String INFO = get_key(types.WHAT_INFO);
	public static final String MIN = get_key(types.WHAT_MIN);
	public static final String MAX = get_key(types.WHAT_MAX);	
	public static final String USERNAME = get_key(types.WHAT_USERNAME);
	public static final String PASSWORD = get_key(types.WHAT_PASSWORD);
	public static final String ID = get_key(types.WHAT_ID);
	public static final String USER = get_key(types.WHAT_USER);
	public static final String QUERY = get_key(types.WHAT_QUERY);
	public static final String INSTANCE = get_key(types.WHAT_INSTANCE);
	public static final String MESSAGE = get_key(types.WHAT_MESSAGE);
	public static final String PATH = get_key(types.WHAT_PATH);
	
	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).

	public static String get_key(String what_) { return types.what_to_key(what_); }

	public static String get_key(String type_, String root_) { return types.check_type(type_, types.get_subtypes(root_), types.ACTION_REMOVE, root_); }
}