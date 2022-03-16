package accessory;

import java.util.HashMap;

public class db_field 
{
	public boolean _is_ok = true;
	
	public String _type = strings.DEFAULT;
	public int _size = 0;
	public int _decimals = 0;
	public Object _default = null;
	public String[] _further = null;
	
	private static int _size_temp = 0;
	private static int _decimals_temp = 0;
	
	public db_field(db_field input_)
	{
		_is_ok = false;
		if (!is_ok(input_)) return;

		_is_ok = true;
		_type = input_._type;
		_size = _size_temp;
		_decimals = _decimals_temp;
		_default = generic.get_new(input_._default);
		_further = (String[])generic.get_new(input_._further);
	}

	public db_field(String type_)
	{
		_is_ok = false;
		if (!is_ok(type_, 0, 0, null)) return;

		_is_ok = true;
		_type = data.check_type(type_);
		_size = _size_temp;
		_decimals = _decimals_temp;
		_default = null;
		_further = null;	
	}
	
	public db_field(String type_, String[] further_)
	{
		_is_ok = false;
		if (!is_ok(type_, 0, 0, further_)) return;

		_is_ok = true;
		_type = data.check_type(type_);
		_size = _size_temp;
		_decimals = _decimals_temp;
		_default = null;
		_further = (String[])generic.get_new(further_);
	}
	
	public db_field(String type_, int size_, int decimals_)
	{
		_is_ok = false;
		if (!is_ok(type_, size_, decimals_, null)) return;

		_is_ok = true;
		_type = data.check_type(type_);
		_size = _size_temp;
		_decimals = _decimals_temp;
		_default = null;
		_further = null;
	}

	public db_field(String type_, int size_, int decimals_, Object default_, String[] further_)
	{
		_is_ok = false;
		if (!is_ok(type_, size_, decimals_, default_, further_)) return;

		_is_ok = true;
		_type = data.check_type(type_);
		_size = _size_temp;
		_decimals = _decimals_temp;
		_default = generic.get_new(default_);
		_further = (String[])generic.get_new(further_);
	}
	
	public String toString()
	{
		String output = "";
		
		String type = data.check_type(_type);
		if (strings.is_ok(type)) output = type.toString();		
		if (!output.equals("")) output += misc.SEPARATOR_ITEM;
		
		String size = strings.to_string(_size);
		if (_decimals > 0) size += ", " + _decimals;
		output += "(" + size + ")";
				
		if (generic.is_ok(_further)) 
		{
			output += misc.SEPARATOR_ITEM;
			output += arrays.to_string(_further, null);
		} 
		if (!strings.is_ok(output)) return strings.DEFAULT;

		return (misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE);
	}

	public boolean equals(db_field field2_)
	{
		if (!is_ok(field2_)) return false;
		
		return 
		(
			strings.are_equal(data.check_type(_type), field2_._type) &&
			(_size == field2_._size) && 
			(_decimals == field2_._decimals) &&
			generic.are_equal(_default, field2_._default) &&
			arrays.are_equal(_further, field2_._further)
		);		
	}
	
	public static String to_string(db_field field_)
	{
		return (is_ok(field_) ? field_.toString() : strings.DEFAULT);	
	}
	
	public static boolean are_equal(db_field field1_, db_field field2_)
	{
		return generic.are_equal(field1_, field2_);
	}
	
	public static boolean is_ok(db_field input_)
	{
		return (input_ != null && db_field.is_ok(input_._type, input_._size, input_._decimals, input_._default, input_._further));
	}

	public static db_field adapt(db_field input_)
	{
		db_field output = new db_field(input_);
		if (!output._is_ok) return output;
		
		check_size(input_._type, input_._size, input_._decimals);
		
		output._size = _size_temp;
		output._decimals = _decimals_temp;
		
		return output;
	}
	
	public static <x> boolean complies(x val_, db_field field_)
	{
		if (!generic.is_ok(val_) || !is_ok(field_)) return false;
		
		double val = Math.pow(10, field_._size);
		size size = new size(-1 * val, val, field_._decimals);

		return data.complies(val_, new data(field_._type, size));
	}

	public static boolean further_is_ok(String[] further_)
	{
		if (!arrays.is_ok(further_)) return true;
		
		for (String further: further_)
		{
			if (!further_is_ok(further)) return false;
		}
		
		return true;
	}
	
	public static boolean further_is_ok(String further_)
	{
		return (!strings.is_ok(further_) || strings.is_ok(check_further(further_)));
	}
	
	public static String check_further(String further_)
	{
		return types.check_subtype(further_, types.get_subtypes(types.DB_FIELD_FURTHER, null), null, null);
	}
	
	public static int check_size(String type_, int size_)
	{
		check_size(type_, size_, 0);
		
		return _size_temp;
	}
	
	public static boolean default_is_ok(String type_, int size_, Object default_)
	{
		check_size(type_, size_);		
	
		if (_size_temp <= 0) return false;
		if (default_ == null) return true;
		
		boolean is_ok = false;		
		int max = _size_temp;
		if (max <= 0) return is_ok;
		
		if (data.is_numeric(type_) && generic.is_number(default_)) 
		{
			is_ok = (numbers.to_number(default_) <= max);
		}
		else if (data.is_string(type_) && generic.is_string(default_)) 
		{
			is_ok = (strings.get_length((String)default_) <= max);
		}
		else if (type_.equals(types.DATA_BOOLEAN))
		{
			if (generic.is_boolean(default_)) is_ok = true;
			else if (generic.is_number(default_))
			{
				double temp = numbers.to_number(default_);
				if (temp == 0 || temp == 1) is_ok = true;
			}
			else if (generic.is_string(default_))
			{
				String temp = (String)default_;
				if (strings.is_boolean(temp)) is_ok = true;
				else if (strings.is_number(temp))
				{
					double temp2 = numbers.decimal_from_string(temp);
					if (temp2 == 0.0 || temp2 == 1.0) is_ok = true;
				}
			}
		}
		
		return is_ok;
	}
	
	private static void check_size(String type_, int size_, int decimals_)
	{
		HashMap<String, Object> info = db.get_data_type(type_);
		if (!arrays.is_ok(info))
		{
			update_temp(type_, 0, 0);
			
			return;
		}
		
		int size = size_;
		int max = (int)info.get(keys.MAX);
		if (size > max) size = max;
		
		update_temp(type_, size, decimals_);
	}
	
	private static void update_temp(String type_, int size_, int decimals_)
	{
		_size_temp = (size_ > 0 ? size_ : db.get_default_size(type_));
		_decimals_temp = (decimals_ > 0 ? decimals_ : 0);
	}
	
	private static boolean is_ok(String type_, int size_, int decimals_, Object default_, String[] further_)
	{
		check_size(type_, size_, decimals_);
		if (_size_temp < 1) return false;

		return (default_is_ok(type_, _size_temp, default_) && further_is_ok(further_));
	}

	private static boolean is_ok(String type_, int size_, int decimals_, String[] further_)
	{
		update_temp(type_, size_, decimals_);
		
		return (data.type_is_ok(type_) && further_is_ok(further_));
	}
}