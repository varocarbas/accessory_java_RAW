package accessory;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class paths extends parent_static 
{
	public static final String HOME = get_dir_home();

	public static final String DIR = _types.CONFIG_BASIC_DIR;
	public static final String DIR_APP = _types.CONFIG_BASIC_DIR_APP;
	public static final String DIR_INI = _types.CONFIG_BASIC_DIR_INI;
	public static final String DIR_LOGS = _types.CONFIG_BASIC_DIR_LOGS;
	public static final String DIR_LOGS_ERRORS = _types.CONFIG_BASIC_DIR_LOGS_ERRORS;
	public static final String DIR_LOGS_ACTIVITY = _types.CONFIG_BASIC_DIR_LOGS_ACTIVITY;
	public static final String DIR_CREDENTIALS = _types.CONFIG_BASIC_DIR_CREDENTIALS;
	public static final String DIR_CRYPTO = _types.CONFIG_BASIC_DIR_CRYPTO;
	public static final String DIR_SOUNDS = _types.CONFIG_BASIC_DIR_SOUNDS;
	public static final String DIR_BACKUPS = _types.CONFIG_BASIC_DIR_BACKUPS;
	public static final String DIR_BACKUPS_DBS = _types.CONFIG_BASIC_DIR_BACKUPS_DBS;
	public static final String DIR_BACKUPS_FILES = _types.CONFIG_BASIC_DIR_BACKUPS_FILES;
	public static final String DIR_INFO = _types.CONFIG_BASIC_DIR_INFO;
	
	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_JAR = ".jar";
	public static final String EXTENSION_INI = ".ini";
	public static final String EXTENSION_LOG = ".log";
	public static final String EXTENSION_WAV = ".wav";
	public static final String EXTENSION_SQL = ".sql";

	public static final boolean DEFAULT_ADD_DIR = false;
	
	public static boolean file_exists(String path_) 
	{ 
		if (!strings.is_ok(path_)) return false;
		
		File file = new File(path_);
		
		return (file.exists() && file.isFile()); 
	}
	
	public static boolean dir_exists(String path_) 
	{ 
		if (!strings.is_ok(path_)) return false;
		
		File file = new File(path_);
		
		return (file.exists() && file.isDirectory()); 
	}
	
	public static boolean exists(String path_) { return (strings.is_ok(path_) && (new File(path_)).exists()); }
	
	public static boolean is_file(String path_) { return (strings.is_ok(path_) && (new File(path_)).isFile()); }

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
		String separator = os.get_dir_separator();
		
		String dir = dir_;
		if (!strings.is_ok(dir)) dir = "";

		dir = dir.trim();
		if (!strings.are_equal(strings.get_end(dir), separator)) dir += separator;

		return dir;
	}

	public static String normalise_file(String file_)
	{
		String file = file_;
		if (!strings.is_ok(file)) return strings.DEFAULT;

		file = file.trim();
		if (strings.are_equal(strings.get_start(file), os.get_dir_separator()))
		{ 
			file = strings.get_end(file, 1);
			if (!strings.is_ok(file)) return strings.DEFAULT;

			file = file.trim();
		}

		return file;
	}

	public static String get_sound_path(String file_) { return (strings.is_ok(file_) ? build(new String[] { get_dir(DIR_SOUNDS), file_ }, true) : strings.DEFAULT); }
	
	public static String get_dir(String type_) { return (String)get_update_dir(type_, null, true); }

	public static boolean update_dir(String type_, String val_) { return (boolean)get_update_dir(type_, val_, false); }

	public static String get_file_full(String name_, String extension_)
	{
		String output = name_;
		if (!strings.is_ok(output)) return strings.DEFAULT;

		if (strings.is_ok(extension_)) output += extension_;

		return output;
	}

	public static String get_dir_home() 
	{ 
		if (strings.is_ok(HOME)) return HOME;
		
		return normalise_dir((os.is_linux() ? os_linux.get_home() : System.getProperty("user.home"))); 
	}	
	
	public static ArrayList<String> get_all_files(String dir_) { return get_all_files(dir_, DEFAULT_ADD_DIR); }

	public static ArrayList<String> get_all_files(String dir_, boolean add_dir_) { return get_all_files(dir_, null, add_dir_); }

	public static ArrayList<String> get_all_files(String dir_, String[] keywords_) { return get_all_files(dir_, keywords_, DEFAULT_ADD_DIR); }
	
	public static ArrayList<String> get_all_files(String dir_, String[] keywords_, boolean add_dir_)
	{
		ArrayList<String> output = new ArrayList<String>();
		if (!strings.is_ok(dir_)) return output;
		
		File[] all = new File(dir_).listFiles();
		if (all == null || all.length == 0) return output;
		
		boolean check_keywords = arrays.is_ok(keywords_);
		
		for (File item: all) 
		{ 
			if (!item.isFile()) continue;
			
			String file = item.getName();
			String path = (add_dir_ ? build(new String[] { dir_, file }, true) : file);

			if (check_keywords && !strings.contains_all(keywords_, path, true)) continue;
			
			output.add(path); 
		}
		
		return output;
	}

	public static HashMap<String, LocalDateTime> get_timestamps(String dir_, String[] keywords_) { return get_timestamps(dir_, keywords_, DEFAULT_ADD_DIR); }

	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalDateTime> get_timestamps(String dir_, String[] keywords_, boolean add_dir_) { return (HashMap<String, LocalDateTime>)get_dates_times(dir_, keywords_, dates.DEFAULT_FORMAT_TIMESTAMP, add_dir_); }

	public static HashMap<String, LocalDate> get_dates(String dir_, String[] keywords_) { return get_dates(dir_, keywords_, DEFAULT_ADD_DIR); }

	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalDate> get_dates(String dir_, String[] keywords_, boolean add_dir_) { return (HashMap<String, LocalDate>)get_dates_times(dir_, keywords_, dates.DEFAULT_FORMAT_DATE, add_dir_); }

	public static HashMap<String, LocalTime> get_times(String dir_, String[] keywords_) { return get_times(dir_, keywords_, DEFAULT_ADD_DIR); }

	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalTime> get_times(String dir_, String[] keywords_, boolean add_dir_) { return (HashMap<String, LocalTime>)get_dates_times(dir_, keywords_, dates.DEFAULT_FORMAT_TIME, add_dir_); }
	
	static String get_default_dir(String type_)
	{
		String type = _types.check_type(type_, DIR);

		if (!strings.is_ok(type)) return strings.DEFAULT;
		else if (strings.are_equal(type, DIR_APP)) return get_dir_app_default();

		ArrayList<String> parts = new ArrayList<String>();
		parts.add(get_dir_home());

		if (_types.is_subtype_of(type, DIR_LOGS))
		{
			parts.add(_types.remove_type(DIR_LOGS, DIR));
			parts.add(_types.remove_type(type, DIR_LOGS));
		}
		else if (_types.is_subtype_of(type, DIR_BACKUPS))
		{
			parts.add(_types.remove_type(DIR_BACKUPS, DIR));
			parts.add(_types.remove_type(type, DIR_BACKUPS));
		}
		else parts.add(_types.remove_type(type, DIR)); 

		return build(arrays.to_array(parts), false);
	}

	private static Object get_update_dir(String type_, String val_, boolean is_get_)
	{
		Object output = (is_get_ ? strings.DEFAULT : false);

		String type = _types.check_type(type_, DIR);
		if (!strings.is_ok(type)) return output;

		if (is_get_) output = (String)config.get_basic(type);
		else if (dir_exists(val_)) output = config.update_basic(type, val_);

		return output;
	}

	private static String get_dir_app_default() { return normalise_dir(System.getProperty("user.dir")); }

	@SuppressWarnings("unchecked")
	private static Object get_dates_times(String dir_, String[] keywords_, String format_, boolean add_dir_)
	{	
		Object output = null;
	
		boolean is_date_time = dates.is_date_time(format_);
		boolean is_date = dates.is_date(format_);
		boolean is_time = dates.is_time(format_);
		
		if (is_date_time) output = new HashMap<String, LocalDateTime>();
		else if (is_date) output = new HashMap<String, LocalDate>();
		else if (is_time) output = new HashMap<String, LocalTime>();
		
		if (!is_date_time && !is_date && !is_time) return output;
		
		ArrayList<String> files = get_all_files(dir_);
		if (!arrays.is_ok(files)) return output;

		boolean check_keywords = arrays.is_ok(keywords_);
		
		for (String file: files)
		{
			if (check_keywords)
			{
				boolean skip = false;
				
				for (String keyword: keywords_)
				{
					if (!strings.contains(keyword, file, true))
					{
						skip = true;
						
						break;
					}
				}
				
				if (skip) continue;
			}

			Object item = dates.get(file, format_, true);
			if (item == null) continue;
			
			String path = (add_dir_ ? build(new String[] { dir_, file }, true) : file);
			
			if (is_date_time) ((HashMap<String, LocalDateTime>)output).put(path, (LocalDateTime)item);
			else if (is_date) ((HashMap<String, LocalDate>)output).put(path, (LocalDate)item);
			else if (is_time) ((HashMap<String, LocalTime>)output).put(path, (LocalTime)item);
		}
		
		return output;
	}
}