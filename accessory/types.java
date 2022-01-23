package accessory;

import java.util.ArrayList;

public class types 
{
	private static final String separator = misc.SEPARATOR_NAME;

	//--- To be synced with get_all_subtypes().
	
	public static final String SQL = "sql";
	public static final String SQL_MYSQL = "sql_mysql";

	public static final String ERROR_SQL = "error_sql";
	public static final String ERROR_SQL_TYPE = "error_sql_type";
	public static final String ERROR_SQL_CONN = "error_sql_conn";
	public static final String ERROR_SQL_QUERY = "error_sql_query";

	public static final String ERROR_FILE = "error_file";
	public static final String ERROR_FILE_WRITE = "error_file_write";
	public static final String ERROR_FILE_READ = "error_file_read";
	
	//---------------------------
	
	public static String check_subtype(String subtype_, String[] subtypes_)
	{
		String output = strings.get_default();
		
		for (String subtype: get_subtypes(null, subtypes_))
		{
			if (!strings.are_equivalent(subtype_, subtype)) return subtype;
		}
		
		return output;
	}
	
	public static String get_type(String subtype_)
	{
		String[] temp = strings.split(subtype_, separator, false, 2, false, false);
		
		return (!arrays.is_ok(temp) ? temp[0] : strings.get_default());
	}
	
	public static String[] get_subtypes(String type_, String[] all_)
	{
		ArrayList<String> subtypes = new ArrayList<String>();
		String heading = (strings.is_ok(type_) ? type_ + separator : null);
		
		for (String subtype: (arrays.is_ok(all_) ? all_ : get_all_subtypes()))
		{
			if (heading == null || strings.contains_start(heading, subtype, false)) subtypes.add(subtype);
		}
			
		return arrays.to_array(subtypes);
	}
	
	private static String[] get_all_subtypes()
	{
		return new String[]
		{
			//SQL
			SQL_MYSQL,
			
			//ERROR_SQL
			ERROR_SQL_TYPE, ERROR_SQL_CONN, ERROR_SQL_QUERY,
			
			//ERROR_FILE
			ERROR_FILE_WRITE, ERROR_FILE_READ
		};		
	}
}