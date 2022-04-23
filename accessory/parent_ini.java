package accessory;

import java.util.HashMap;

public abstract class parent_ini 
{
	protected boolean _populated = false;
	
	protected abstract void populate_first_basic();
	protected abstract void populate_first_alls();
	protected abstract void populate_first_defaults();
	protected abstract void populate_config();
	protected abstract void populate_db();
	
	public static void manage_error(String type_)
	{
		errors._exit = true;
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(get_generic_key(types.WHAT_TYPE), (strings.is_ok(type_) ? type_ : strings.DEFAULT));
		
		errors.manage(info);
	}
	
	//The keys in the generic class might not have been loaded yet, so better getting them directly from types.
	public static String get_generic_key(String what_)
	{
		String what = types.check_type(what_, types.get_subtypes(types.WHAT));
		
		return (strings.is_ok(what) ? types.what_to_key(what) : strings.DEFAULT);
	}
	
	protected void populate_all() 
	{
		if (_populated) return;
		
		populate_first();
		populate_config();
		populate_db();
		
		_populated = true;
	}
	
	//Loading all the first classes, the ones whose names start with "_".
	private void populate_first()
	{
		populate_first_basic();
		populate_first_alls();
		populate_first_defaults();
	}
}