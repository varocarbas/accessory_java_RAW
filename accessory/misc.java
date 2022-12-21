package accessory;

import java.io.File;

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

		if (os.is_linux()) output = (boolean)os.execute_command(new String[] { "aplay", "-q", path_ }, true);

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
}