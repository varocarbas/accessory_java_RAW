package accessory;

import java.util.ArrayList;

abstract class os_unix 
{
	public static final int INDEX_PROCESS_COMMAND = 10;
	public static final int INDEX_WINDOW_TITLE = 3;

	public static boolean execute_bash(String command_, boolean wait_for_it_) { return (boolean)execute_bash(command_, wait_for_it_, false); }

	public static Object execute_bash(String command_, boolean wait_for_it_, boolean return_outputs_) { return (strings.is_ok(command_) ? os.execute_command(new String[] { "/bin/bash", "-c" , command_ }, wait_for_it_, return_outputs_) : false); }

	public static String[] get_running_processes(String app_, String[] keywords_, String[] keywords_ignore_, boolean only_commands_)
	{
		String[] keywords = (arrays.is_ok(keywords_) ? (String[])arrays.get_new(keywords_) : null);
		String[] keywords_ignore = null;

		String command = "ps aux";

		if (strings.is_ok(app_)) 
		{
			String temp = "grep " + app_;
			command += " | " + temp;

			ArrayList<String> temp2 = arrays.to_arraylist(keywords_ignore_);
			temp2.add(temp);

			keywords_ignore = arrays.to_array(temp2);
		}
		else keywords_ignore = (arrays.is_ok(keywords_ignore_) ? (String[])arrays.get_new(keywords_ignore_) : null);

		return get_running_processes_windows(command, keywords, keywords_ignore, only_commands_, true);
	}

	public static String get_running_process_command(String process_, boolean only_start_) { return get_running_command_window(process_, INDEX_PROCESS_COMMAND, only_start_); }

	public static String[] get_running_windows(String[] keywords_, String[] keywords_ignore_, boolean only_titles_) { return get_running_processes_windows("wmctrl -l", keywords_, keywords_ignore_, only_titles_, false); }

	@SuppressWarnings("unchecked")
	private static String[] get_running_processes_windows(String command_, String[] keywords_, String[] keywords_ignore_, boolean only_command_title_, boolean are_processes_)
	{		
		ArrayList<String> output = (ArrayList<String>)execute_bash(command_, true, true); 
		if (!arrays.is_ok(output) || (!only_command_title_ && (keywords_ == null && keywords_ignore_ == null))) return arrays.to_array(output);

		boolean is_first = true;

		for (String item: new ArrayList<String>(output))
		{
			if (is_first)
			{
				is_first = false;

				if (are_processes_) 
				{
					output.remove(item);

					continue;
				}
			}

			String item2 = get_running_command_title(item, are_processes_);

			if ((keywords_ignore_ != null && strings.contains_any(keywords_ignore_, item2, true)) || (keywords_ != null && !strings.contains_any(keywords_, item2, true))) output.remove(item);
			else if (only_command_title_)
			{
				output.remove(item);
				output.add(item2);
			}
		}

		return arrays.to_array(output);		
	}

	private static String get_running_command_title(String item_, boolean is_command_) { return (is_command_ ? get_running_process_command(item_, false) : get_running_window_title(item_)); }

	private static String get_running_window_title(String window_) { return get_running_command_window(window_, INDEX_WINDOW_TITLE, false); }

	private static String get_running_command_window(String input_, int index_, boolean only_start_)
	{
		String[] temp = strings.split_spaces(input_);			
		int tot = arrays.get_size(temp);

		return (tot > index_ ? (only_start_ ? temp[index_] : String.join(" ", (String[])arrays.get_range(temp, index_, 0))) : null);		
	}
}