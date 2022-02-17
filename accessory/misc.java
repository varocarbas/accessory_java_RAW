package accessory;

public class misc 
{	
	public static final String SEPARATOR_CONTENT = " --- ";
	public static final String SEPARATOR_SCREEN = SEPARATOR_CONTENT;
	public static final String SEPARATOR_FILE = System.lineSeparator();	
	public static final String SEPARATOR_NAME = "_";	
	public static final String SEPARATOR_KEYVAL = ": ";
	public static final String SEPARATOR_ITEM = ", ";

	public static final String SEPARATOR_DIR = paths.SEPARATOR_DIR;

	static { _ini.load(); }

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