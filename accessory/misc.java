package accessory;

public abstract class misc extends parent_static
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

	public static void pause_loop() { pause_tiny(); }

	public static void pause_tiny() { pause_milli(50); }

	public static void pause_mins(int mins_) { pause_secs(60 * mins_); }

	public static void pause_secs(int secs_) { pause_milli(1000 * secs_); }

	public static void pause_milli(int milli_)
	{
		if (milli_ < 1) return;

		try { Thread.sleep(milli_); } 
		catch (Exception e) { }		
	}
	
	public static boolean play_alarm(String file_) { return play_alarm(file_, false); }
	
	public static boolean play_alarm(String file_, boolean is_exiting_) { return io.play_sound_short(file_, is_exiting_); }
}