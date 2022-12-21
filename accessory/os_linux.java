package accessory;

import java.util.ArrayList;

abstract class os_linux 
{
	@SuppressWarnings("unchecked")
	public static int get_running_window_id(String title_) 
	{ 
		int output = os.WRONG_WINDOW_ID;
		
		String command = "xdotool search --onlyvisible --name '" + title_ + "'";
		ArrayList<String> temp = (ArrayList<String>)os_unix.execute_bash(command, true, true);
		
		if (arrays.is_ok(temp)) 
		{
			String val = temp.get(0);
			if (strings.is_int(val)) output = Integer.parseInt(val);
		}
		
		return output;
	}
	
	public static boolean fill_running_form(int window_id_, String[] inputs_)
	{
		String command0 = "xdotool windowactivate --sync " + window_id_;
		
		boolean is_first = true;
		
		for (String input: inputs_)
		{
			if (is_first) is_first = false;
			else 
			{
				if (!fill_running_form_input(command0, "Tab", true)) return false;	
				misc.pause_tiny();
			}
			
			if (!fill_running_form_input(command0, input, false)) return false;
			misc.pause_tiny();
		}
		
		fill_running_form_input(command0, "Return", true);
		
		return true;
	}
	
	public static boolean fill_running_form_input(String command0_, String input_, boolean is_key_)
	{
		String command = command0_ + " " + (is_key_ ? "key " : "type ") + strings.to_string(input_);
		
		return os_unix.execute_bash(command, true);
	}
}