package accessory;

import java.io.File;
import java.util.ArrayList;

public abstract class paths extends parent_static 
{
	public static final String HOME = get_dir_home();

	public static final String DIR_APP = types.CONFIG_BASIC_DIR_APP;
	public static final String DIR_INI = types.CONFIG_BASIC_DIR_INI;
	public static final String DIR_LOGS = types.CONFIG_BASIC_DIR_LOGS;
	public static final String DIR_LOGS_ERRORS = types.CONFIG_BASIC_DIR_LOGS_ERRORS;
	public static final String DIR_LOGS_ACTIVITY = types.CONFIG_BASIC_DIR_LOGS_ACTIVITY;
	public static final String DIR_CREDENTIALS = types.CONFIG_BASIC_DIR_CREDENTIALS;
	public static final String DIR_CRYPTO = types.CONFIG_BASIC_DIR_CRYPTO;
	public static final String DIR_SOUNDS = types.CONFIG_BASIC_DIR_SOUNDS;
			
	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_JAR = ".jar";
	public static final String EXTENSION_INI = ".ini";
	public static final String EXTENSION_LOG = ".log";
	public static final String EXTENSION_WAV = ".wav";
	
	public static boolean exists(String path_) { return (strings.is_ok(path_) && (new File(path_)).exists()); }

	public static String build(String[] pieces_, boolean last_is_file_) { return build(arrays.to_arraylist(pieces_), last_is_file_, false); }
	
	public static String build(ArrayList<String> pieces_, boolean last_is_file_, boolean add_home_always_)
	{
		ArrayList<String> pieces = arrays.get_new(pieces_);
		if (add_home_always_ || (last_is_file_ && !arrays.is_ok(pieces))) pieces.add(0, get_dir_home());
		
		if (!arrays.is_ok(pieces)) return strings.DEFAULT;

		String path = "";
		int last_i = pieces.size() - 1;
		int tot = 0;

		for (int i = 0; i <= last_i; i++)
		{
			String piece = pieces.get(i);
			if (!strings.is_ok(piece)) continue;

			tot++;
			if (!last_is_file_ || i != last_i) piece = normalise_dir(piece);	
			path += piece;	
		}
		if (tot < 1) return strings.DEFAULT;

		if (!last_is_file_) path = normalise_dir(path);

		return path;
	}

	public static String normalise_dir(String dir_)
	{
		String dir = dir_;
		if (!strings.is_ok(dir)) dir = "";

		dir = dir.trim();
		if (!strings.are_equal(strings.get_end(dir), _basic.DIR_SEPARATOR)) dir += _basic.DIR_SEPARATOR;

		return dir;
	}

	public static String normalise_file(String file_)
	{
		String file = file_;
		if (!strings.is_ok(file)) return strings.DEFAULT;

		file = file.trim();
		if (strings.are_equal(strings.get_start(file), _basic.DIR_SEPARATOR))
		{ 
			file = strings.get_end(file, 1);
			if (!strings.is_ok(file)) return strings.DEFAULT;

			file = file.trim();
		}

		return file;
	}

	public static String get_dir(String type_) { return get_update_dir(type_, null, true); }

	public static void update_dir(String type_, String val_) { get_update_dir(type_, val_, false); }

	public static String get_file_full(String name_, String extension_)
	{
		String output = name_;
		if (!strings.is_ok(output)) return strings.DEFAULT;

		if (strings.is_ok(extension_)) output += extension_;

		return output;
	}

	public static String get_dir_home() { return normalise_dir(System.getProperty("user.home")); }	

	static String get_default_dir(String type_)
	{
		String type = types.check_type(type_, types.CONFIG_BASIC_DIR);

		if (!strings.is_ok(type)) return strings.DEFAULT;
		else if (strings.are_equal(type, types.CONFIG_BASIC_DIR_APP)) return get_dir_app_default();

		ArrayList<String> parts = new ArrayList<String>();
		parts.add(get_dir_home());

		if (types.is_subtype_of(type, types.CONFIG_BASIC_DIR_LOGS))
		{
			parts.add(types.remove_type(types.CONFIG_BASIC_DIR_LOGS, types.CONFIG_BASIC_DIR));
			parts.add(types.remove_type(type, types.CONFIG_BASIC_DIR_LOGS));
		}
		else parts.add(types.remove_type(type, types.CONFIG_BASIC_DIR)); 

		return build(arrays.to_array(parts), false);
	}

	private static String get_update_dir(String type_, String val_, boolean is_get_)
	{
		String output = strings.DEFAULT;

		String type = types.check_type(type_, types.CONFIG_BASIC_DIR);
		if (!strings.is_ok(type)) return output;

		if (is_get_) output = (String)config.get_basic(type);
		else config.update_basic(type, val_);

		return output;
	}

	private static String get_dir_app_default() { return normalise_dir(System.getProperty("user.dir")); }
}