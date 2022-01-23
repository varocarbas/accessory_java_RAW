package accessory;

import java.io.File;
import java.util.ArrayList;

public class paths 
{
	public static final String EXT_TEXT = ".txt";
	public static final String EXT_JAR = ".jar";
	public static final String EXT_INI = ".ini";
	
	public static final String sep_dir = misc.SEPARATOR_DIR;
	
	private static String cur_dir = null;
	
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
	
	public static String get_home_dir()
	{
		return normalise_dir(System.getProperty("user.home"));
	}
	
	public static void update_cur_dir(String dir_)
	{
		if (paths.exists(dir_)) cur_dir = paths.normalise_dir(dir_);
	}
	
	public static String get_default_dir(String what_)
	{
		if (!strings.is_ok(what_)) return strings.get_default();

		String[] targets = new String[] 
		{
			keys.CREDENTIALS, keys.INI	
		};
		
		for (String target: targets)
		{
			if (strings.are_equivalent(what_, target))
			{
		    	return build(new String[] { get_home_dir(), target }, false);
			}
		}
		
		return strings.get_default();
	}
	
	public static String get_cur_dir()
	{
		return (strings.is_ok(cur_dir) ? cur_dir : normalise_dir(System.getProperty("user.dir")));
	}
	
	public static String normalise_dir(String dir_)
	{
		String dir = dir_;
		if (!strings.is_ok(dir)) dir = "";
		
		dir = dir.trim();
	
		if (dir.substring(dir.length() - 1) != sep_dir) 
		{
			dir += sep_dir;
		}
		
		return dir;
	}
}
