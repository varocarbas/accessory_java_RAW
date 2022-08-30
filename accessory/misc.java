package accessory;

import java.util.HashMap;

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

	public static final String ERROR_EXECUTE = types.ERROR_MISC_EXECUTE;
	
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
	
	public static boolean play_alarm(String file_) { return io.play_sound_short(file_); }

	public static boolean execute_bash(String command_, boolean wait_for_it_) { return (strings.is_ok(command_) ? execute_command(new String[] { "/bin/bash", "-c" , command_ }, wait_for_it_) : false); }
	
	public static boolean execute_command(String[] args_, boolean wait_for_it_) 
	{
		boolean is_ok = false;
		if (!arrays.is_ok(args_)) return is_ok;
		
		try 
		{
			Process process = Runtime.getRuntime().exec(args_);
			
			int exit_val = (wait_for_it_ ? process.waitFor() : process.exitValue());
			
			if (exit_val == 0) is_ok = true;
		} 
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("args", strings.to_string(args_));
			info.put("wait_for_it", wait_for_it_);
			
			errors.manage(ERROR_EXECUTE, e, info); 
		}
		
		return is_ok;
	}
}