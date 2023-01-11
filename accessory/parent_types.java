package accessory;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class parent_types 
{
	protected boolean _populated = false;
		
	protected abstract String[] get_constant_names_to_ignore();
	
	public static String[] get_types() { return _types.TYPES; }
	
	public static void add_types(Class<?> class_, String[] constant_names_to_ignore_) { add_types(get_types(class_, constant_names_to_ignore_)); }
	
	public static void add_types(String[] types_)
	{
		if (types_ != null && types_.length > 0) 
		{
			if (_types.TYPES == null) _types.TYPES = Arrays.copyOfRange(types_, 0, types_.length);
			else
			{
				ArrayList<String[]> types_all = new ArrayList<String[]>();
				types_all.add(Arrays.copyOfRange(_types.TYPES, 0, _types.TYPES.length));
				types_all.add(Arrays.copyOfRange(types_, 0, types_.length));

				int tot = types_all.get(0).length + types_all.get(1).length;
				
				_types.TYPES = new String[tot];

				int i2 = -1;
				
				for (String[] types2: types_all)
				{
					for (int i = 0; i < types2.length; i++) 
					{
						i2++;
						
						_types.TYPES[i2] = types2[i];
					}
				}
			}
		}
	}
	
	public static String[] get_types(Class<?> class_, String[] constant_names_to_ignore_) { return generic.get_class_variables_constants(class_, constant_names_to_ignore_, new Class<?>[] { String.class }, false, true, true, false, true, true); }
	
	protected void populate_internal()
	{
		if (_populated) return;
		
		String[] types = generic.get_class_variables_constants(getClass(), get_constant_names_to_ignore(), new Class<?>[] { String.class }, false, true, true, false, true, true);

		add_types(types);

		_populated = true;
	}
}