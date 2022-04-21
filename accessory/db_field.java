package accessory;

import java.util.HashMap;

//All the min/max/size values in this class refer to specific DB types and, as such, are defined 
//according to the given rules. For example, MySQL's data size being the max number of characters 
//allowed in the given column.

public class db_field extends parent
{
	public static final String KEY_UNIQUE = types.DB_FIELD_FURTHER_KEY_UNIQUE;
	public static final String AUTO_INCREMENT = types.DB_FIELD_FURTHER_AUTO_INCREMENT;
	
	public static final int MAX_DECIMALS = size.MAX_DECIMALS;
	public static final int MIN_DECIMALS = size.MIN_DECIMALS;
	public static final int MIN_SIZE = 0;

	public static final String WRONG_TYPE = strings.DEFAULT;
	public static final int WRONG_SIZE = numbers.DEFAULT_INT;
	public static final int WRONG_DECIMALS = size.WRONG_DECIMALS;

	public static final String DEFAULT_TYPE = data.TYPE_STRING;
	public static final int DEFAULT_DECIMALS = size.DEFAULT_DECIMALS;
	public static final int DEFAULT_SIZE = _defaults.DB_SIZE_STRING;
	public static final double DEFAULT_SIZE_DECIMAL = _defaults.DB_SIZE_DECIMAL;
	public static final double DEFAULT_SIZE_LONG = _defaults.DB_SIZE_LONG;
	public static final double DEFAULT_SIZE_INT = _defaults.DB_SIZE_INT;
	public static final int DEFAULT_SIZE_STRING = _defaults.DB_SIZE_STRING;
	public static final int DEFAULT_SIZE_STRING_BIG = _defaults.DB_SIZE_STRING_BIG;
	public static final int DEFAULT_SIZE_BOOLEAN = _defaults.DB_SIZE_BOOLEAN;
	public static final int DEFAULT_SIZE_TIMESTAMP = _defaults.DB_SIZE_TIMESTAMP;

	public String _type = WRONG_TYPE;
	public int _size = WRONG_SIZE;
	public int _decimals = WRONG_DECIMALS;
	public Object _default = null;
	public String[] _further = null;

	//--- Only added via default fields.
	static final String KEY_PRIMARY = types.DB_FIELD_FURTHER_KEY_PRIMARY;
	static final String TIMESTAMP = types.DB_FIELD_FURTHER_TIMESTAMP;
	//---

	private String _temp_type = strings.DEFAULT;
	private int _temp_size = 0;
	private int _temp_decimals = 0;
	
	public static boolean are_equal(db_field field1_, db_field field2_) { return are_equal_common(field1_, field2_); }
	
	public static boolean further_is_ok(String[] further_)
	{
		if (!arrays.is_ok(further_)) return true;

		for (String further: further_)
		{
			if (!further_is_ok(further)) return false;
		}

		return true;
	}

	public static boolean further_is_ok(String further_) { return val_is_ok_common(further_, types.DB_FIELD_FURTHER, strings.DEFAULT); }

	public static String check_further(String further_) { return check_val_common(further_, types.DB_FIELD_FURTHER, strings.DEFAULT); }

	public static int check_size(String type_, int size_)
	{
		int size = size_;

		HashMap<String, Object> info = db.get_data_type(type_);
		if (!arrays.is_ok(info)) return DEFAULT_SIZE;

		int max = (int)info.get(generic.MAX);
		if (size <= MIN_SIZE || size > max) size = db.get_default_size(type_);
		
		return size;
	}

	public static boolean default_is_ok(String type_, int size_, Object default_)
	{
		if (default_ == null) return true;

		int size = check_size(type_, size_);		
		if (size <= 0) return false;

		boolean is_ok = false;		
		int max = size;
		if (max <= 0) return is_ok;

		if (data.is_number(type_) && generic.is_number(default_)) is_ok = (numbers.to_number(default_) <= max);
		else if (data.is_string(type_) && generic.is_string(default_)) is_ok = (strings.get_length((String)default_) <= max);
		else if (type_.equals(data.BOOLEAN))
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
					double temp2 = numbers.to_decimal(temp);
					if (temp2 == 0.0 || temp2 == 1.0) is_ok = true;
				}
			}
		}

		return is_ok;
	}	

	public static db_field adapt(db_field input_)
	{
		db_field output = new db_field(input_);
		if (!output._is_ok) return output;

		output._size = check_size(input_._type, input_._size);
		output._decimals = adapt_decimals(input_._decimals);

		return output;
	}

	public static int adapt_decimals(int decimals_) { return (numbers.is_ok(decimals_, MIN_DECIMALS, MAX_DECIMALS) ? decimals_ : DEFAULT_DECIMALS); }

	public static <x> boolean complies(x val_, db_field field_)
	{
		if (!generic.is_ok(val_) || !is_ok(field_)) return false;

		double val = Math.pow(10, field_._size);
		size size = new size(-1 * val, val, field_._decimals);

		return data.complies(val_, new data(field_._type, size));
	}
	
	public db_field(db_field input_) { instantiate(input_); }

	public db_field(String type_) { instantiate(type_, WRONG_SIZE, DEFAULT_DECIMALS, null, null); }

	public db_field(String type_, String[] further_) { instantiate(type_, WRONG_SIZE, DEFAULT_DECIMALS, null, further_); }

	public db_field(String type_, int size_, int decimals_) { instantiate(type_, size_, decimals_, null, null); }

	public db_field(String type_, int size_, int decimals_, Object default_, String[] further_) { instantiate(type_, size_, decimals_, default_, further_); }

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
			(_size == field2_._size) && (_decimals == field2_._decimals) &&
			generic.are_equal(_default, field2_._default) && 
			arrays.are_equal(_further, field2_._further)
		);		
	}

	public boolean is_ok()
	{
		_is_ok = is_ok(_type, _size, _decimals, _default, _further);

		return _is_ok;
	}

	private void instantiate(db_field input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_type, input_._temp_size, input_._temp_decimals, input_._default, input_._further);
	}

	private void instantiate(String type_, int size_, int decimals_, Object default_, String[] further_)
	{
		instantiate_common();
		if (!is_ok(type_, size_, decimals_, default_, further_)) return;

		populate(_temp_type, _temp_size, _temp_decimals, default_, further_);
	}

	private boolean is_ok(String type_, int size_, int decimals_, Object default_, String[] further_)
	{
		_temp_type = data.check_type(type_);
		_temp_size = check_size(type_, size_);
		_temp_decimals = adapt_decimals(decimals_);
		
		return (default_is_ok(_temp_type, _temp_size, default_) && further_is_ok(further_));
	}

	private void populate(String type_, int size_, int decimals_, Object default_, String[] further_)
	{
		_is_ok = true;

		_type = type_;
		_size = size_;
		_decimals = decimals_;
		_default = generic.get_new(default_);
		_further = (String[])generic.get_new(further_);
	}
}