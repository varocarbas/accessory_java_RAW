package accessory;

public abstract class misc 
{	
	public static final String NEW_LINE = System.lineSeparator();
	
	public static final String BRACKET_MAIN_OPEN = "{ ";
	public static final String BRACKET_MAIN_CLOSE = " }";
	public static final String BRACKET_SEC_OPEN = "(";
	public static final String BRACKET_SEC_CLOSE = ")";
	
	public static final String SEPARATOR_CONTENT = " --- ";
	public static final String SEPARATOR_SCREEN = SEPARATOR_CONTENT;
	public static final String SEPARATOR_FILE = NEW_LINE;	
	public static final String SEPARATOR_NAME = "_";	
	public static final String SEPARATOR_KEYVAL = ": ";
	public static final String SEPARATOR_ITEM = ", ";
	
	static { _ini.load(); }
	public static final String _ID = types.get_id(types.ID_MISC);
	
	public static void pause_min()
	{
		pause_milli(50);
	}

	public static void pause_mins(int mins_)
	{
		pause_secs(60 * mins_);
	}

	public static void pause_secs(int secs_)
	{
		pause_milli(1000 * secs_);
	}

	public static void pause_milli(int milli_)
	{
		if (milli_ < 1) return;

		try 
		{
			Thread.sleep(milli_);
		} 
		catch (Exception e) { }		
	}
}