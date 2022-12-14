package accessory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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

	public static final int MAX_SECS_SOUND_SHORT = 5;

	public static final int INDEX_PROCESS_COMMAND_UNIX = 10;
	public static final int INDEX_WINDOW_TITLE_UNIX = 3;
	
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
	
	public static boolean sound_format_is_supported(String file_) { return sound_format_is_supported(file_, null); }

	public static boolean sound_format_is_supported(String file_, String[] valid_extensions_) 
	{ 
		for (String extension: (arrays.is_ok(valid_extensions_) ? valid_extensions_ : get_all_supported_sound_extensions()))
		{
			if (strings.contains_end(extension, file_, true)) return true;
		}
		
		return false; 
	}

	public static boolean play_alarm(String file_name_) { return play_sound_short(file_name_); }

	public static boolean play_sound_short(String file_name_) { return play_sound_short(file_name_, false); }
	
	public static boolean play_sound_short(String file_name_, boolean use_java_)
	{
		boolean output = false;

		String path = get_sound_path(file_name_);
		if (path == null) return output;
		
		if (use_java_) output = play_sound_short_java(path);
		else play_sound_internal(path);
		
		return output;
	}	
	
	public static boolean play_sound(String file_name_) 
	{ 
		String path = get_sound_path(file_name_);
		
		return (path != null ? play_sound_internal(path) : false);
	}

	public static boolean app_is_running(String app_, String[] keywords_, String[] keywords_to_ignore_) { return arrays.is_ok(get_running_processes(app_, keywords_, keywords_to_ignore_, false)); }

	public static String[] get_running_processes() { return get_running_processes(null, null, null, false); }
	
	public static String[] get_running_processes(String app_, String[] keywords_, String[] keywords_to_ignore_, boolean only_commands_)
	{
		String[] output = null;
		
		if (!_basic.is_windows()) output = get_running_processes_unix(app_, keywords_, keywords_to_ignore_, only_commands_);
		
		return output;
	}

	public static String[] get_running_windows() { return get_running_windows(null, null, false); }

	public static String[] get_running_windows(String[] keywords_, String[] keywords_to_ignore_, boolean only_titles_)
	{
		String[] output = null;
		
		if (!_basic.is_windows()) output = get_running_windows_unix(keywords_, keywords_to_ignore_, only_titles_);
		
		return output;
	}
	
	public static boolean execute_bash(String command_, boolean wait_for_it_) { return (boolean)execute_bash(command_, wait_for_it_, false); }
	
	public static boolean execute_command(String[] args_, boolean wait_for_it_) { return (boolean)execute_command(args_, wait_for_it_, false); }	
	
	public static Object execute_bash(String command_, boolean wait_for_it_, boolean return_outputs_) { return (strings.is_ok(command_) ? execute_command(new String[] { "/bin/bash", "-c" , command_ }, wait_for_it_, return_outputs_) : false); }
	
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
	
	static String[] populate_all_supported_sound_extensions() { return new String[] { paths.EXTENSION_WAV }; }
	
	private static String[] get_all_supported_sound_extensions() { return _alls.MISC_SUPPORTED_SOUND_EXTENSIONS; }
	
	private static String get_sound_path(String file_name_)
	{
		String output = null;
		
		if (misc.sound_format_is_supported(file_name_))
		{
			output = paths.get_sound_path(file_name_);
			
			if (!paths.exists(output)) output = null;
		}

		return output;
	}

	private static boolean play_sound_internal(String path_) 
	{ 
		boolean output = false;
		
		if (_basic.is_linux()) output = (boolean)execute_command(new String[] { "aplay", "-q", path_ }, true);
		
		return output;
	}
	
	private static boolean play_sound_short_java(String path_)
	{
		boolean output = false;
		
		try 
		{						
	        Clip clip = AudioSystem.getClip();

	        clip.open(AudioSystem.getAudioInputStream(new File(path_)));

	        clip.start();
	        
	        misc.pause_secs(MAX_SECS_SOUND_SHORT);
		} 
		catch (Exception e) { output = false; }
		
		return output;
	}
		
	private static String[] get_running_processes_unix(String app_, String[] keywords_, String[] keywords_to_ignore_, boolean only_commands_)
	{
		String[] keywords = (arrays.is_ok(keywords_) ? (String[])arrays.get_new(keywords_) : null);
		String[] keywords_to_ignore = null;

		String command = "ps aux";
		
		if (strings.is_ok(app_)) 
		{
			String temp = "grep " + app_;
			command += " | " + temp;
			
			ArrayList<String> temp2 = arrays.to_arraylist(keywords_to_ignore_);
			temp2.add(temp);
			
			keywords_to_ignore = arrays.to_array(temp2);
		}
		else keywords_to_ignore = (arrays.is_ok(keywords_to_ignore_) ? (String[])arrays.get_new(keywords_to_ignore_) : null);
		
		return get_running_processes_windows_unix(command, keywords, keywords_to_ignore, only_commands_, true);
	}

	private static String get_running_process_command_unix(String process_, boolean only_start_) { return get_running_command_window_unix(process_, INDEX_PROCESS_COMMAND_UNIX, only_start_); }
	
	private static String[] get_running_windows_unix(String[] keywords_, String[] keywords_to_ignore_, boolean only_titles_) { return get_running_processes_windows_unix("wmctrl -l", keywords_, keywords_to_ignore_, only_titles_, false); }

	@SuppressWarnings("unchecked")
	private static String[] get_running_processes_windows_unix(String command_, String[] keywords_, String[] keywords_to_ignore_, boolean only_command_title_, boolean are_processes_)
	{		
		ArrayList<String> output = (ArrayList<String>)misc.execute_bash(command_, true, true); 
		if (!arrays.is_ok(output) || (!only_command_title_ && (keywords_ == null && keywords_to_ignore_ == null))) return arrays.to_array(output);
		
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
			
			String item2 = get_running_command_title_unix(item, are_processes_);
			
			if ((keywords_to_ignore_ != null && strings.contains_any(keywords_to_ignore_, item2, true)) || (keywords_ != null && !strings.contains_any(keywords_, item2, true))) output.remove(item);
			else if (only_command_title_)
			{
				output.remove(item);
				output.add(item2);
			}
		}
		
		return arrays.to_array(output);		
	}

	private static String get_running_command_title_unix(String item_, boolean is_command_) { return (is_command_ ? get_running_process_command_unix(item_, false) : get_running_window_title_unix(item_)); }
	
	private static String get_running_window_title_unix(String window_) { return get_running_command_window_unix(window_, INDEX_WINDOW_TITLE_UNIX, false); }
	
	private static String get_running_command_window_unix(String input_, int index_, boolean only_start_)
	{
		String[] temp = strings.split_spaces(input_);			
		int tot = arrays.get_size(temp);
		
		return (tot > index_ ? (only_start_ ? temp[index_] : String.join(" ", (String[])arrays.get_range(temp, index_, 0))) : null);		
	}
}