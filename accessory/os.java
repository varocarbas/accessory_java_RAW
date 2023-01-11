package accessory;

import java.util.HashMap;

public abstract class os 
{
	public static final int WRONG_WINDOW_ID = -1;

	public static final String ERROR_EXECUTE = _types.ERROR_OS_EXECUTE;

	public static boolean is_windows() { return _basic.is_windows(); }

	public static boolean is_linux() { return _basic.is_linux(); }
	
	public static String get_dir_separator() { return _basic.get_dir_separator(); }

	public static boolean app_is_running(String app_, String[] keywords_, String[] keywords_ignore_) { return arrays.is_ok(get_running_processes(app_, keywords_, keywords_ignore_, false)); }

	public static String[] get_running_processes() { return get_running_processes(null, null, null, false); }

	public static String[] get_running_processes(String app_, String[] keywords_, String[] keywords_ignore_, boolean only_commands_)
	{
		String[] output = null;

		if (!is_windows()) output = os_unix.get_running_processes(app_, keywords_, keywords_ignore_, only_commands_);

		return output;
	}

	public static String[] get_running_windows() { return get_running_windows(null, null, false); }

	public static String[] get_running_windows(String[] keywords_, String[] keywords_ignore_, boolean only_titles_)
	{
		String[] output = null;

		if (!is_windows()) output = os_unix.get_running_windows(keywords_, keywords_ignore_, only_titles_);

		return output;
	}

	public static int get_running_window_id(String title_)
	{
		int output = WRONG_WINDOW_ID;
		if (!strings.is_ok(title_)) return output;
		
		if (is_linux()) output = os_linux.get_running_window_id(title_);

		return output;
	}
	
	public static boolean fill_running_form(String window_title_, String[] inputs_)
	{
		return fill_running_form(get_running_window_id(window_title_), inputs_);
	}
	
	public static boolean fill_running_form(int window_id_, String[] inputs_)
	{
		boolean output = false;
		if (window_id_ <= WRONG_WINDOW_ID || !arrays.is_ok(inputs_)) return output;
		
		if (is_linux()) output = os_linux.fill_running_form(window_id_, inputs_);
		
		return output;
	}
	
	public static boolean execute_command(String[] args_, boolean wait_for_it_) { return (boolean)execute_command(args_, wait_for_it_, false); }	

	public static Object execute_command(String[] args_, boolean wait_for_it_, boolean return_outputs_) 
	{
		Object output = (return_outputs_ ? null : false);

		if (!arrays.is_ok(args_)) return output;

		try 
		{
			Process process = Runtime.getRuntime().exec(args_);

			if ((wait_for_it_ ? process.waitFor() : process.exitValue()) == 0)
			{
				if (return_outputs_) output = io.get_lines(process.getInputStream());
				else output = true;				
			}
		} 
		catch (Exception e) 
		{ 
			HashMap<String, Object> info = new HashMap<String, Object>();

			info.put("args", strings.to_string(args_));
			info.put("wait_for_it", wait_for_it_);

			errors.manage(ERROR_EXECUTE, e, info); 
		}

		return output;
	}
}