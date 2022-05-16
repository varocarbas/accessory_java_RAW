package accessory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class io extends parent_static 
{
	public static final String _ID = types.get_id(types.ID_IO);

	public static final String ERROR_WRITE = types.ERROR_FILE_WRITE;
	public static final String ERROR_READ = types.ERROR_FILE_READ;
	
	public static void array_to_file(String path_, String[] vals_, boolean append_)
	{
		_is_ok = false;
		
		if (!strings.is_ok(path_) || !arrays.is_ok(vals_)) return;

		try (FileWriter writer = new FileWriter(path_, append_)) 
		{
			for (String val: vals_) { line_to_file(path_, val, append_, writer); }
		} 
		catch (Exception e) 
		{ 
			if (!ignore_errors_internal()) errors.manage_io(ERROR_WRITE, path_, e); 
		}
		
		_is_ok = true;
	}

	public static String[] file_to_array(String path_)
	{
		_is_ok = false;
		
		if (!paths.exists(path_)) return null;

		ArrayList<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new FileReader(path_))) 
		{
			while (scanner.hasNext()) { lines.add(scanner.nextLine().trim()); }
		} 
		catch (Exception e) 
		{ 
			lines = null;
			
			if (!ignore_errors_internal()) errors.manage_io(ERROR_READ, path_, e);
		}

		_is_ok = true;
		
		return (arrays.is_ok(lines) ? arrays.to_array(lines) : null);
	}
	
	public static String file_to_string(String path_, boolean only_first_)
	{
		String[] lines = file_to_array(path_);
		if (!is_ok() || arrays.get_size(lines) < 1) return strings.DEFAULT;

		return (only_first_ ? lines[0] : arrays.lines_to_string(lines));
	}
	
	public static void line_to_file(String path_, String line_, boolean append_)
	{
		_is_ok = false;
		
		line_to_file(path_, line_, append_, null);
		
		_is_ok = true;
	}

	public static HashMap<String, String> ini_to_array(String path_)
	{
		HashMap<String, String> ini = null;
		if (!strings.contains_end(paths.EXTENSION_INI, path_, true)) return ini;

		String[] lines = file_to_array(path_);
		if (!arrays.is_ok(lines)) return ini;

		ini = new HashMap<String, String>();

		for (String line: lines)
		{
			String[] temp = strings.split(misc.SEPARATOR_KEYVAL, line, true);
			if (arrays.get_size(temp) < 2) continue;

			String key = temp[0];
			String value = temp[1];
			if (!strings.is_ok(key) || !strings.is_ok(value)) continue;

			ini.put(key, value);
		}

		return ini;
	}

	public static void bytes_to_file(String path_, byte[] vals_)
	{
		_is_ok = false;
		
		if (!strings.is_ok(path_) || !arrays.is_ok(vals_)) return;
		
		try (FileOutputStream stream = new FileOutputStream(new File(path_))) { stream.write(vals_); } 
		catch (Exception e) { if (!ignore_errors_internal()) errors.manage_io(ERROR_WRITE, path_, e); }
		
		_is_ok = true;
	}

	public static byte[] file_to_bytes(String path_)
	{
		_is_ok = false;
		
		byte[] output = null;
		if (!paths.exists(path_)) return output;
		
		try { output = Files.readAllBytes(Paths.get(path_)); } 
		catch (Exception e) { if (!ignore_errors_internal()) errors.manage_io(ERROR_READ, path_, e); }
		
		_is_ok = true;
		
		return output;
	}

	public static void object_to_file(String path_, Object vals_)
	{
		_is_ok = false;

		if (!strings.is_ok(path_) || vals_ == null) return;
		
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path_))) { stream.writeObject(vals_); } 
		catch (Exception e) { if (!ignore_errors_internal()) errors.manage_io(ERROR_WRITE, path_, e); }
		
		_is_ok = true;
	}

	public static Object file_to_object(String path_)
	{
		_is_ok = false;
		
		Object output = null;
		if (!paths.exists(path_)) return output;
		
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path_))) { output = stream.readObject(); } 
		catch (Exception e) { if (!ignore_errors_internal()) errors.manage_io(ERROR_WRITE, path_, e); }
		
		_is_ok = true;
		
		return output;
	}

	private static void line_to_file(String path_, String line_, boolean append_, FileWriter writer_)
	{
		if (!strings.is_ok(path_) || !strings.is_ok(line_)) return;

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
		catch (Exception e) 
		{ 
			if (!ignore_errors_internal()) errors.manage_io(ERROR_WRITE, path_, e); 
		}
	}
}