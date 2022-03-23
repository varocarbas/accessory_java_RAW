package accessory;

import java.util.ArrayList;
import java.util.HashMap;

abstract class parent 
{
	public boolean _is_ok = false;
	
	protected static ArrayList<Integer> _temp_size_all = new ArrayList<Integer>();
	protected static ArrayList<Integer> _temp_decimals_all = new ArrayList<Integer>();
	
	private static HashMap<Class<?>, Integer> _temp_i_all = new HashMap<Class<?>, Integer>();
	private static HashMap<Class<?>, Boolean> _temp_blocked = new HashMap<Class<?>, Boolean>();
	private static int _temp_i_max = 1000;
	private static int _temp_wait_max = 10;
	
	protected int start()
	{
		boolean is_ok = false;
		
		int count = 0;
		
		Class<?> type = this.getClass();
		if (!_temp_blocked.containsKey(type)) 
		{
			_temp_blocked.put(type, true);
			
			count = _temp_wait_max;
			is_ok = true;
		}
		
		while (count < _temp_wait_max)
		{
			count++;
			
			if (!_temp_blocked.get(type))
			{
				_temp_blocked.put(type, true);
				is_ok = true;
				
				break;
			}
			
			misc.pause_min();
		}

		return (is_ok ? start_temp(type) : -1);
	}
	
	private int start_temp(Class<?> class_)
	{
		int i = -1;

		if (class_.equals(db_field.class))
		{
			i = _temp_size_all.size();
			
			if (i > _temp_i_max)
			{
				i = 0;
				
				_temp_size_all = new ArrayList<Integer>();
				_temp_decimals_all = new ArrayList<Integer>();
			}
			
			_temp_size_all.add(0);
			_temp_decimals_all.add(0);
		}
		else return i;
		
		_temp_i_all.put(class_, i);

		return i;
	}
}