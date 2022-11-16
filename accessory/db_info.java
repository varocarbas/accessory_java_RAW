package accessory;

public abstract class db_info 
{
	public static final String SOURCE = types.CONFIG_INFO_DB_SOURCE;
	
	public static final String KEY = types.CONFIG_INFO_DB_FIELD_KEY;
	public static final String VALUE = types.CONFIG_INFO_DB_FIELD_VALUE;
	public static final String IS_ENC = types.CONFIG_INFO_DB_FIELD_IS_ENC;

	private static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	public static boolean is_quick() { return _is_quick; }
	
	public static void is_quick(boolean is_quick_) { _is_quick = is_quick_; }

	static void start() { }
}