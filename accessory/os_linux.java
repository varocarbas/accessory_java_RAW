package accessory;

import java.util.ArrayList;

abstract class os_linux 
{
	public static final int MIN_WAITING_SECS = 1;
	
	public static final int WRONG_WINDOW_ID = -1;
	public static final int WRONG_COORDINATE = -1;
	
	public static int get_running_window_id_app(String app_or_dir_, String[] keywords_, String[] keywords_ignore_) { return get_running_window_id(os_unix.get_process_id(app_or_dir_, keywords_, keywords_ignore_)); }
	
	public static int get_running_window_id(int process_id_) { return (process_id_ > os_unix.WRONG_PROCESS_ID ? get_running_window_id(process_id_, false) : WRONG_WINDOW_ID); }
	
	public static int get_running_window_id(String title_) { return (strings.is_ok(title_) ? get_running_window_id(title_, true) : WRONG_WINDOW_ID); }
	
	public static boolean click_running_window(int window_id_, xy xy_) { return move(window_id_, xy_, true); }	
	
	public static boolean fill_running_form(int window_id_, String[] inputs_, int waiting_secs_) { return fill_running_form(window_id_, inputs_, null, waiting_secs_); }
	
	public static boolean fill_running_form(int window_id_, String[] inputs_, xy[] xys_, int waiting_secs_)
	{
		int max_i = arrays.get_size(inputs_) - 1;
		if (window_id_ <= WRONG_WINDOW_ID || max_i < 0) return false;
		
		int max_i_xys = arrays.get_size(xys_) - 1;
		
		boolean use_coordinates = (max_i_xys >= 0);
		
		if (use_coordinates && max_i_xys < max_i) return false;
		
		boolean click_submit = (max_i_xys > max_i);
		
		xy xy = null;
				
		boolean is_first = true;		
		
		int i = 0;
		
		for (i = 0; i <= max_i; i++)
		{
			if (use_coordinates) xy = new xy(xys_[i]);
			
			if (is_first) is_first = false;
			else 
			{
				if (!fill_running_form_input(window_id_, "Tab", true, xy)) return false;
				
				wait_secs();
			}
			
			if (!fill_running_form_input(window_id_, inputs_[i], false, xy)) return false;

			wait_secs(waiting_secs_);
		}

		return fill_running_form_input(window_id_, "Return", true, (click_submit ? new xy(xys_[i]) : null));
	}
	
	private static boolean fill_running_form_input(int window_id_, String input_, boolean is_key_, xy xy_)
	{
		if (!strings.is_ok(input_)) return false;
			
		if (click_running_window(window_id_, xy_)) 
		{
			if (is_key_) return true;
			
			wait_secs();
		}

		String command = get_command_xdotool_key_type(window_id_, input_, is_key_);
		
		return (strings.is_ok(command) ? os_unix.execute_bash(command, true) : false);
	}
	
	private static boolean move(int window_id_, xy xy_, boolean click_)
	{
		String command = get_command_xdotool_move(window_id_, xy_, click_);
		
		return (strings.is_ok(command) ? os_unix.execute_bash(command, true) : false);
	}
	
	@SuppressWarnings("unchecked")
	private static int get_running_window_id(Object title_process_id_, boolean is_title_) 
	{ 
		int output = WRONG_WINDOW_ID;
		
		String command = get_command_xdotool_search(title_process_id_, is_title_);

		ArrayList<String> temp = (ArrayList<String>)os_unix.execute_bash(command, true, true);
		
		if (arrays.is_ok(temp)) 
		{
			String val = temp.get(0);
			
			if (strings.is_int(val)) output = Integer.parseInt(val);
		}
		
		return output;
	}
	
	private static String get_command_xdotool_search(Object title_process_id_, boolean is_title_) 
	{ 
		String output = get_command_xdotool_start() + "search --onlyvisible ";
		
		if (is_title_) output += "--name '" + title_process_id_ + "'";
		else output += "--pid " + title_process_id_;
		
		return output;
	}
	
	private static String get_command_xdotool_key_type(int window_id_, String input_, boolean is_key_) 
	{ 
		String output = get_command_xdotool_start(window_id_);
		if (!strings.are_ok(new String[] { output, input_ })) return strings.DEFAULT;
		
		String input = input_;		
		
		if (is_key_) 
		{
			output += "key ";
		}
		else
		{
			output += "type ";
			
			input = "'" + input + "'";
		}
		
		output += "--delay 0 " + input;
		
		return output; 
	}
	
	private static String get_command_xdotool_move(int window_id_, xy xy_, boolean click_) 
	{ 
		String output = get_command_xdotool_start(window_id_);
		if (!strings.is_ok(output) || !xy.is_ok(xy_)) return strings.DEFAULT;
		
		output += "mousemove --window " + window_id_;
		output += " " + xy_.get_x() + " " + xy_.get_y();
		
		if (click_) output += " click 1";
		
		return output; 
	}
	
	private static String get_command_xdotool_start(int window_id_) { return (window_id_ > WRONG_WINDOW_ID ? (get_command_xdotool_start() + "windowactivate --sync " + window_id_ + " ") : strings.DEFAULT); }
	
	private static String get_command_xdotool_start() { return "xdotool "; }

	private static void wait_secs() { wait_secs(0); }

	private static void wait_secs(int waiting_secs_) { misc.pause_secs((waiting_secs_ <= 0 ? MIN_WAITING_SECS : waiting_secs_)); }
}