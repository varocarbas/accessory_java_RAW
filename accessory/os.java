package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class os extends parent_static 
{
	public static final String CONFIG_IS_VIRTUAL_MACHINE = _types.CONFIG_OS_IS_VIRTUAL_MACHINE;
	
	public static final int WRONG_PROCESS_ID = -1;
	public static final int WRONG_WINDOW_ID = -1;

	public static final int DEFAULT_FORM_WAITING_SECS = 30;
	public static final boolean DEFAULT_IS_VIRTUAL_MACHINE = false;
	public static final boolean DEFAULT_EXECUTE_WAIT = true;
	public static final boolean DEFAULT_EXECUTE_RETURN_OUTPUTS = false;
	
	public static final String ERROR_EXECUTE = _types.ERROR_OS_EXECUTE;
	
	public static boolean is_virtual_machine() { return config.get_os_boolean(CONFIG_IS_VIRTUAL_MACHINE); }
	
	public static void is_virtual_machine(boolean is_virtual_machine_) { config.update_os(CONFIG_IS_VIRTUAL_MACHINE, is_virtual_machine_); }

	public static boolean is_windows() { return _basic.is_windows(); }

	public static boolean is_linux() { return _basic.is_linux(); }
	
	public static String get_dir_separator() { return _basic.get_dir_separator(); }

	public static boolean app_is_running(String app_, String[] keywords_, String[] keywords_ignore_) { return arrays.is_ok(get_running_processes(app_, keywords_, keywords_ignore_, false)); }

	public static boolean can_move_mouse() 
	{
		boolean output = false;
		
		if (is_linux()) output = os_linux.can_move_mouse();
		
		return output; 
	}

	public static int get_process_id(String app_) { return get_process_id(app_, null, null); }
	
	public static int get_process_id(String app_, String[] keywords_, String[] keywords_ignore_)
	{
		method_start();
		
		int output = WRONG_PROCESS_ID;

		if (!is_windows()) 
		{
			output = os_unix.get_process_id(app_, keywords_, keywords_ignore_);
			
			if (output <= os_unix.WRONG_PROCESS_ID) output = WRONG_PROCESS_ID;
		}
		
		if (output > WRONG_PROCESS_ID) method_end();

		return output;
	}
	
	public static String[] get_running_processes() { return get_running_processes(null, null, null, false); }

	public static String[] get_running_processes(String app_, String[] keywords_, String[] keywords_ignore_, boolean only_commands_)
	{
		method_start();
		
		String[] output = null;

		if (!is_windows()) output = os_unix.get_running_processes(app_, keywords_, keywords_ignore_, only_commands_);

		method_end();
		
		return output;
	}

	public static String[] get_running_windows() { return get_running_windows(null, null, false); }

	public static String[] get_running_windows(String[] keywords_, String[] keywords_ignore_, boolean only_titles_)
	{
		method_start();
		
		String[] output = null;

		if (!is_windows()) 
		{
			output = os_unix.get_running_windows(keywords_, keywords_ignore_, only_titles_);
			
			if (os_unix.is_ok()) method_end();
		}
		
		return output;
	}

	public static int get_running_window_id_app(String app_) { return get_running_window_id_app(app_, null, null); }
	
	public static int get_running_window_id_app(String app_, String[] keywords_, String[] keywords_ignore_)
	{
		method_start();
		
		int output = WRONG_WINDOW_ID;
		if (!strings.is_ok(app_)) return output;
		
		if (is_linux()) 
		{
			output = os_linux.get_running_window_id_app(app_, keywords_, keywords_ignore_);
			
			if (os_linux.is_ok()) method_end();
			else output = WRONG_WINDOW_ID;
		}

		return output;
	}
	
	public static int get_running_window_id(int process_id_)
	{
		method_start();
		
		int output = WRONG_WINDOW_ID;
		if (process_id_ <= WRONG_PROCESS_ID) return output;
		
		if (is_linux()) 
		{
			output = os_linux.get_running_window_id(process_id_);
			
			if (os_linux.is_ok()) method_end();
			else output = WRONG_WINDOW_ID;
		}
		
		return output;
	}
	
	public static int get_running_window_id(String title_)
	{
		method_start();
		
		int output = WRONG_WINDOW_ID;
		if (!strings.is_ok(title_)) return output;
		
		if (is_linux()) 
		{
			output = os_linux.get_running_window_id(title_);
			
			if (os_linux.is_ok()) method_end();
			else output = WRONG_WINDOW_ID;
		}
		
		return output;
	}
	
	public static boolean click_running_window_app(String app_, xy xy_) { return click_running_window_app(app_, null, null, xy_); }
	
	public static boolean click_running_window_app(String app_, String[] keywords_, String[] keywords_ignore_, xy xy_) { return click_running_window(get_running_window_id_app(app_, keywords_, keywords_ignore_), xy_); }
	
	public static boolean click_running_window(int window_id_, xy xy_) 
	{
		method_start();
		
		boolean output = false;
		if (window_id_ <= WRONG_WINDOW_ID) return output;

		if (is_linux()) 
		{
			output = os_linux.click_running_window(window_id_, xy_);
			
			if (os_linux.is_ok()) method_end();
		}
		
		return output;
	}
	
	public static boolean fill_running_form_app(String app_, String[] inputs_) { return fill_running_form_app(app_, null, null, inputs_, DEFAULT_FORM_WAITING_SECS); }

	public static boolean fill_running_form_app(String app_, String[] keywords_, String[] keywords_ignore_, String[] inputs_, int waiting_secs_) { return fill_running_form(get_running_window_id_app(app_, keywords_, keywords_ignore_), inputs_, waiting_secs_); }

	public static boolean fill_running_form(String window_title_, String[] inputs_, int waiting_secs_) { return fill_running_form(get_running_window_id(window_title_), inputs_, waiting_secs_); }
	
	public static boolean fill_running_form(int window_id_, String[] inputs_) { return fill_running_form(window_id_, inputs_, DEFAULT_FORM_WAITING_SECS); }
	
	public static boolean fill_running_form_app(String app_, String[] keywords_, String[] keywords_ignore_, String[] inputs_, xy[] xys_, int waiting_secs_) { return fill_running_form(get_running_window_id_app(app_, keywords_, keywords_ignore_), inputs_, xys_, waiting_secs_); }

	public static boolean fill_running_form(int window_id_, String[] inputs_, int waiting_secs_) { return fill_running_form(window_id_, inputs_, null, waiting_secs_); }

	public static boolean fill_running_form(int window_id_, String[] inputs_, xy[] xys_) { return fill_running_form(window_id_, inputs_, xys_, DEFAULT_FORM_WAITING_SECS); }
	
	public static boolean fill_running_form(int window_id_, String[] inputs_, xy[] xys_, int waiting_secs_)
	{
		method_start();
		
		boolean output = false;
		if (window_id_ <= WRONG_WINDOW_ID || !arrays.is_ok(inputs_)) return output;
		
		if (is_linux()) 
		{
			output = os_linux.fill_running_form(window_id_, inputs_, xys_, waiting_secs_);
			
			if (os_linux.is_ok()) method_end();
		}
		
		return output;
	}
	
	public static void kill_app(String app_) { kill_app(app_, null, null); }
	
	public static void kill_app(String app_, String[] keywords_, String[] keywords_to_ignore_) 
	{
		method_start();
		
		if (!is_windows()) 
		{
			os_unix.kill_app(app_, keywords_, keywords_to_ignore_);
		
			if (os_unix.is_ok()) method_end();
		}
	}
	
	public static void kill_process(int process_id_)
	{
		method_start();
		
		if (!is_windows()) 
		{
			os_unix.kill_process(process_id_);
			
			if (!os_unix.is_ok()) method_end();
		}
	}
	
	public static boolean execute_command(String command_) { return execute_command(command_, DEFAULT_EXECUTE_WAIT); }	
	
	public static boolean execute_command(String command_, boolean wait_for_it_) { return (is_linux() ? os_linux.execute_bash(command_, wait_for_it_) : execute_command_internal(command_, wait_for_it_)); }	
	
	public static boolean execute_command(String[] args_, boolean wait_for_it_) { return (boolean)execute_command(args_, wait_for_it_, DEFAULT_EXECUTE_RETURN_OUTPUTS); }	

	public static Object execute_command(String[] args_, boolean wait_for_it_, boolean return_outputs_) { return (is_linux() ? os_linux.execute_bash(args_, wait_for_it_, return_outputs_) : execute_command_internal(args_, wait_for_it_, return_outputs_)); }

	static boolean execute_command_internal(String command_, boolean wait_for_it_) { return (strings.is_ok(command_) ? execute_command(new String[] { command_ }, wait_for_it_) : false); }

	static Object execute_command_internal(String[] args_, boolean wait_for_it_, boolean return_outputs_) 
	{
		method_start();
		
		Object output = (return_outputs_ ? new ArrayList<String>() : false);

		if (!arrays.is_ok(args_)) return output;

		HashMap<String, Object> error_info = new HashMap<String, Object>();
		error_info.put("args", strings.to_string(args_));
		error_info.put("wait_for_it", wait_for_it_);
	
		try 
		{
			boolean is_ok = false;

			Process process = Runtime.getRuntime().exec(args_);
			
			if (wait_for_it_) is_ok = (process.waitFor() == 0);
			else
			{
				try { is_ok = (process.exitValue() == 0); }
				catch (IllegalThreadStateException e) { is_ok = true; }
			}

			if (return_outputs_) 
			{
				if (is_ok) output = io.get_lines(process.getInputStream());
			}
			else output = is_ok;
			
			if (is_ok) method_end();
			else
			{
				ArrayList<String> error = io.get_lines(process.getErrorStream());
				
				if (return_outputs_) output = arrays.get_new(error);
				
				error_info.put(errors.MESSAGE, strings.to_string(error));

				manage_error(ERROR_EXECUTE, null, error_info); 
			}
		} 
		catch (Exception e) { manage_error(ERROR_EXECUTE, e, error_info); }

		return output;
	}
}