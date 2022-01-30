package accessory;

import java.io.File;
import java.util.ArrayList;

public class paths 
{
	static { _ini.load(); }
	
	public static final String SEPARATOR_DIR = get_dir_separator();
	public static final String HOME = get_home_dir();
	
	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_JAR = ".jar";
	public static final String EXTENSION_INI = ".ini";

	public static boolean exists(String path_)
	{
		return (strings.is_ok(path_) && (new File(path_)).exists());
	}
	
	public static String build(ArrayList<String> pieces, boolean last_file_)
	{
		return (!arrays.is_ok(pieces) ? arrays.get_default() : build(arrays.to_array(pieces), last_file_));
	}
	
	public static String build(String[] pieces, boolean last_file_)
	{
		if (!arrays.is_ok(pieces)) return strings.get_default();
		
		String dir = "";
		int last_i = pieces.length - 1;
		
		for (int i = 0; i <= last_i; i++)
		{
			String piece = pieces[i];
			if (!strings.is_ok(piece)) continue;
			
			if (!last_file_ || i != last_i) piece = normalise_dir(piece);	
			dir += piece;	
		}
		
		if (!last_file_) dir = normalise_dir(dir);

		return dir;
	}
		
	public static String normalise_dir(String dir_)
	{
		String dir = dir_;
		if (!strings.is_ok(dir)) dir = "";
		
		dir = dir.trim();
	
		if (dir.substring(dir.length() - 1) != SEPARATOR_DIR) 
		{
			dir += SEPARATOR_DIR;
		}
		
		return dir;
	}

	public static String get_cur_dir(String what_)
	{
		String key = strings.get_default();
		
		if (!strings.is_ok(what_) || strings.are_equivalent(what_, keys.APP)) 
		{
			key = types._CONFIG_BASIC_DIR_APP;
		}
		else if (strings.are_equivalent(what_, keys.CREDENTIALS)) 
		{
			key = types._CONFIG_BASIC_DIR_CREDENTIALS;
		}
		else if (strings.are_equivalent(what_, keys.INI)) 
		{
			key = types._CONFIG_BASIC_DIR_INI;
		}
		else if (strings.are_equivalent(what_, keys.ERRORS)) 
		{
			key = types._CONFIG_BASIC_DIR_ERRORS;
		}

		return (strings.is_ok(key) ? _config.get_basic(key) : strings.get_default());
	}
	
	static String get_default_dir(String what_)
	{
		if (!strings.is_ok(what_)) return strings.get_default();
		if (strings.are_equivalent(what_, keys.APP)) return get_dir_app_default();
		
		String[] targets = new String[] 
		{
			keys.CREDENTIALS, keys.INI, keys.ERRORS	
		};
		
		for (String target: targets)
		{
			if (strings.are_equivalent(what_, target))
			{
				String[] parts = new String[] { HOME, target };
				if (target == keys.ERRORS) parts = new String[] { get_dir_app_default() };
				
		    	return build(parts, false);
			}
		}
		
		return strings.get_default();
	}

	private static String get_dir_separator()
	{
		return (strings.contains("win", System.getProperty("os.name"), true) ? "\\" : "/");
	}
	
	private static String get_dir_app_default()
	{
		return normalise_dir(System.getProperty("user.dir"));
	}
	
	private static String get_home_dir()
	{
		return normalise_dir(System.getProperty("user.home"));
	}
}
