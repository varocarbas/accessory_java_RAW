package accessory;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class io 
{	
	static { _ini.load(); }
	
	public static void array_to_file
	(
		String path_, ArrayList<String> vals_, boolean append_, boolean errors_to_file_
	)
	{
		array_to_file
		(
			path_, 
			(
				!arrays.is_ok(vals_) ? 
				(String[])arrays.DEFAULT : 
				arrays.to_array(vals_)
			), 
			append_, errors_to_file_
		);
	}
	
	public static void array_to_file
	(
		String path_, String[] vals_, boolean append_, boolean errors_to_file_
	)
	{
		if (!strings.is_ok(path_) || !arrays.is_ok(vals_)) return;
		
		try 
		{
			FileWriter writer = new FileWriter(path_, append_);
			
			for (String val: vals_)
			{
				line_to_file(path_, val, append_, writer, errors_to_file_);
			}
			
			writer.close();
		} 
		catch (Exception e) 
		{
			errors.manage_io(types.ERROR_FILE_WRITE, path_, e, errors_to_file_, false);
		}
	}
	
	public static void line_to_file
	(
		String path_, String line_, boolean append_, FileWriter writer_, boolean errors_to_file_
	)
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
			
			writer.write(line_ + System.lineSeparator());
			
			if (is_new) writer.close();
		} 
		catch (Exception e) 
		{
			errors.manage_io(types.ERROR_FILE_WRITE, path_, e, errors_to_file_, false);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> ini_to_array(String path_)
	{
		HashMap<String, String> ini = (HashMap<String, String>)arrays.DEFAULT;
		if (!strings.contains_end(paths.EXTENSION_INI, path_, true)) return ini;
	
		String[] lines = file_to_array(path_, true);
		if (!arrays.is_ok(lines)) return ini;
		
		ini = new HashMap<String, String>();
		
		for (String line: lines)
		{
			String[] temp = strings.split(line, misc.SEPARATOR_KEYVAL, false, 2, true, true);
			if (!arrays.is_ok(temp)) continue;
			
			String key = temp[0];
			String value = temp[1];
			if (!strings.is_ok(key) || !strings.is_ok(value)) continue;

			ini.put(key, value);
		}
		
		return ini;
	}
	
	public static String[] file_to_array(String path_, boolean errors_to_file_)
	{
		if (!paths.exists(path_)) return (String[])arrays.DEFAULT;
		
		ArrayList<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new FileReader(path_))) 
		{
		    while (scanner.hasNext()) 
		    {
		    	lines.add(scanner.nextLine().trim());
		    }
		} 
		catch (Exception e) 
		{ 
			lines = null; 
			errors.manage_io(types.ERROR_FILE_READ, path_, e, errors_to_file_, false);
		}
		
		return (arrays.is_ok(lines) ? arrays.to_array(lines) : (String[])arrays.DEFAULT);
	}
}
