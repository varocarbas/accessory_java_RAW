package accessory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.util.Scanner;

public abstract class io extends parent_static 
{
	public static final int MAX_SECS_SOUND_SHORT = 5;
	
	public static final String ERROR_WRITE = types.ERROR_FILE_WRITE;
	public static final String ERROR_READ = types.ERROR_FILE_READ;

	public static void array_to_file(String path_, String[] vals_, boolean append_)
	{
		method_start();

		if (!strings.is_ok(path_)) return;

		if (!arrays.is_ok(vals_))
		{
			empty_file(path_);
			
			return;
		}
		
		try (FileWriter writer = new FileWriter(path_, append_)) 
		{
			for (String val: vals_) { line_to_file(path_, val, append_, writer); }
		} 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();	
	}

	public static String[] file_to_array(String path_) { return file_web_to_array(path_, true); }

	public static String[] web_to_array(String url_) { return file_web_to_array(url_, false); }

	public static ArrayList<HashMap<String, String>> file_to_hashmap(String path_, String[] cols_, String separator_, boolean normalise_, boolean ignore_first_)
	{
		method_start();

		int tot = arrays.get_size(cols_);
		if (tot < 1 || !paths.exists(path_)) return null;

		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();

		boolean is_first = true;
		
		try (Scanner scanner = new Scanner(new FileReader(path_))) 
		{
			while (scanner.hasNext()) 
			{ 
				String line = scanner.nextLine().trim();
				
				if (is_first) 
				{
					is_first = false;
					
					if (ignore_first_) continue;
				}
				
				String[] vals = strings.split(separator_, line);
				if (!arrays.is_ok(vals)) continue;
				
				HashMap<String, String> item = new HashMap<String, String>();
				
				for (int i = 0; i < tot; i++) { item.put(cols_[i], vals[i].trim()); }

				output.add(item);
			}
		} 
		catch (Exception e) 
		{ 
			output = null;

			manage_error_io(ERROR_READ, e, path_);
		}

		method_end();

		return (arrays.is_ok(output) ? output : null);		
	}

	public static void hashmap_to_file(String path_, ArrayList<HashMap<String, String>> vals_, String separator_, boolean cols_to_first_)
	{
		method_start();

		if (!strings.is_ok(path_)) return;
		
		if (!arrays.is_ok(vals_))
		{
			empty_file(path_);
			
			return;
		}
		
		boolean is_first = true;
		
		try (FileWriter writer = new FileWriter(path_, false)) 
		{
			for (HashMap<String, String> item: vals_)
			{				
				if (is_first) 
				{
					if (cols_to_first_) line_to_file(path_, arrays.to_string(arrays.get_keys_hashmap(item), separator_), false, writer);
					
					is_first = false;
				}
				
				line_to_file(path_, arrays.to_string(arrays.get_values_hashmap(item), separator_), false, writer);
			}
		} 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();
	}
	
	public static String file_to_string(String path_, boolean only_first_)
	{	
		String output = strings.DEFAULT;

		String[] lines = file_to_array(path_);		
		if (arrays.get_size(lines) > 0) output = (only_first_ ? lines[0] : arrays.lines_to_string(lines)); 

		return output;
	}

	public static void line_to_file(String path_, String line_, boolean append_) { line_to_file(path_, line_, append_, null); }

	public static void empty_file(String path_) { line_to_file(path_, "", false); }

	//Ini files are assumed to contain text stored as key-value pairs, one per line. 
	//A descriptive example --> this is key: and all this, including :, is value.
	public static HashMap<String, String> ini_to_array(String path_)
	{
		method_start();

		HashMap<String, String> ini = null;
		if (!strings.contains_end(paths.EXTENSION_INI, path_, true)) return ini;

		String[] lines = file_to_array(path_);
		if (!arrays.is_ok(lines)) return ini;

		ini = new HashMap<String, String>();

		for (String line: lines)
		{
			String[] temp = strings.split(misc.SEPARATOR_KEYVAL, line, true, true);
			if (arrays.get_size(temp) < 2) continue;

			String key = temp[0];
			String value = temp[1];
			if (!strings.is_ok(key) || !strings.is_ok(value)) continue;

			ini.put(key.trim(), value.trim());
		}

		method_end();
		
		return ini;
	}

	public static void ini_to_file(String path_, HashMap<String, String> ini_)
	{
		method_start();

		if (!strings.is_ok(path_)) return;

		if (!arrays.is_ok(ini_))
		{
			empty_file(path_);
			
			return;
		}
		
		try (FileWriter writer = new FileWriter(path_, false)) 
		{
			for (Entry<String, String> item: ini_.entrySet())
			{
				String line = item.getKey() + misc.SEPARATOR_KEYVAL + item.getValue();
				line_to_file(path_, line, false, writer);
			}
		} 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();
	}

	public static void bytes_to_file(String path_, byte[] vals_)
	{
		method_start();

		if (!strings.is_ok(path_)) return;

		if (!arrays.is_ok(vals_))
		{
			empty_file(path_);
			
			return;
		}
		
		try (FileOutputStream stream = new FileOutputStream(new File(path_))) { stream.write(vals_); } 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();
	}

	public static byte[] file_to_bytes(String path_)
	{
		method_start();

		byte[] output = null;
		if (!paths.exists(path_)) return output;

		try { output = Files.readAllBytes(Paths.get(path_)); } 
		catch (Exception e) { manage_error_io(ERROR_READ, e, path_); }

		method_end();

		return output;
	}

	public static void object_to_file(String path_, Object vals_)
	{
		method_start();

		if (!strings.is_ok(path_)) return;

		if (vals_ == null)
		{
			empty_file(path_);
			
			return;
		}
		
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path_))) { stream.writeObject(vals_); } 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();
	}

	public static Object file_to_object(String path_)
	{
		method_start();

		Object output = null;
		if (!paths.exists(path_)) return output;

		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path_))) { output = stream.readObject(); } 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();

		return output;
	}
	
	public static boolean play_sound_short(String file_)
	{
		_is_ok = false;
		
		if (strings.contains_end(paths.EXTENSION_WAV, file_, true)) play_sound_short_wav(file_);
		
		return _is_ok;
	}
	
	private static String[] file_web_to_array(String path_url_, boolean is_file_)
	{
		method_start();

		ArrayList<String> lines = null;
		
		try
		{			
			if (!strings.is_ok(path_url_)) return null;

			Scanner scanner = null;

			if (is_file_)
			{
				if (paths.exists(path_url_)) scanner = new Scanner(new FileReader(path_url_));
			}
			else
			{
				URL url = new URL(path_url_);
				
				scanner = new Scanner(url.openStream());
			}			
			if (scanner == null) return null;

			lines = new ArrayList<>();
						
			while (scanner.hasNext()) { lines.add(scanner.nextLine().trim()); }

			scanner.close();				
		} 
		catch (Exception e) 
		{ 
			lines = null;

			manage_error_io(ERROR_READ, e, path_url_);
		}

		method_end();

		return (arrays.is_ok(lines) ? arrays.to_array(lines) : null);
	}

	private static boolean play_sound_short_wav(String file_)
	{
		_is_ok = false;
		
		try 
		{	
			String path = (strings.is_ok(file_) ? paths.build(new String[] { paths.get_dir(paths.DIR_SOUNDS), file_ }, true) : strings.DEFAULT);
			if (!strings.is_ok(path) || !paths.exists(path)) return _is_ok;
					
	        Clip clip = AudioSystem.getClip();

	        clip.open(AudioSystem.getAudioInputStream(new File(path)));

	        clip.start();
	        
	        misc.pause_secs(MAX_SECS_SOUND_SHORT);
		} 
		catch (Exception e) { _is_ok = false; }
		
		return _is_ok;
	}
	
	private static void line_to_file(String path_, String line_, boolean append_, FileWriter writer_)
	{
		method_start();

		if (!strings.is_ok(path_) || line_ == null) return;

		try 
		{
			Boolean is_new = false;
			FileWriter writer = writer_;

			if (writer == null)
			{
				if (!strings.is_ok(path_)) return;

				is_new = true;
				writer = new FileWriter(path_, append_);
			}

			writer.write(line_ + misc.NEW_LINE);

			if (is_new) writer.close();
		} 
		catch (Exception e) { manage_error_io(ERROR_WRITE, e, path_); }

		method_end();
	}	

	private static void manage_error_io(String type_, Exception e_, String path_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put(_keys.PATH, path_);

		manage_error(type_, e_, info);
	}
}