package accessory;

import java.util.HashMap;

//All the sizes and min./max. values in this class refer to specific DB types and, as such, are defined 
//according to the corresponding rules. For example, MySQL's data size is the max. number of characters 
//allowed in the given column. There might be still some peculiarities to support the intended intuitive 
//approach (e.g., MySQL decimal size being defined as (m+d, d) rather than as (m, d)).

public class db_field extends parent
{
	public static final String KEY_UNIQUE = types.DB_FIELD_FURTHER_KEY_UNIQUE;
	public static final String KEY_PRIMARY = types.DB_FIELD_FURTHER_KEY_PRIMARY;
	public static final String TIMESTAMP = types.DB_FIELD_FURTHER_TIMESTAMP;
	public static final String AUTO_INCREMENT = types.DB_FIELD_FURTHER_AUTO_INCREMENT;

	public static final int MAX_DECIMALS = size.MAX_DECIMALS;
	public static final int MIN_DECIMALS = size.MIN_DECIMALS;

	public static final String WRONG_TYPE = strings.DEFAULT;
	public static final long WRONG_SIZE = numbers.DEFAULT_LONG;
	public static final int WRONG_DECIMALS = size.WRONG_DECIMALS;

	public static final String DEFAULT_TYPE = data.STRING;
	public static final int DEFAULT_DECIMALS = numbers.DEFAULT_DECIMALS;

	private String _type = WRONG_TYPE; //It is a data type. The specific DB type is automatically determined from that type and the size right before interacting with the DB.
	private long _size = WRONG_SIZE;
	private int _decimals = WRONG_DECIMALS;
	private Object _default = null;
	private String[] _further = null;

	private String _temp_type = strings.DEFAULT;
	private long _temp_size = 0;
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

	public static long check_size(String source_, String type_, long size_)
	{
		long size = size_;

		HashMap<String, Object> info = db.get_data_type(type_);
		if (!arrays.is_ok(info)) return 0;

		long max = (long)info.get(_keys.MAX);
		if (size <= 0 || size > max) size = (strings.is_ok(source_) ? db.get_default_size(source_, type_) : db.get_default_size(type_));

		return size;
	}

	public static boolean default_is_ok(String source_, String type_, long size_, Object default_)
	{
		if (default_ == null) return true;

		long size = check_size(source_, type_, size_);		

		return (size > 0 ? data.complies(default_, to_data(type_, size, DEFAULT_DECIMALS)) : false);
	}	

	public static db_field adapt(String source_, db_field input_)
	{
		db_field output = new db_field(input_);
		if (!output.is_ok()) return output;

		output._size = check_size(source_, input_._type, input_._size);
		output._decimals = adapt_decimals(input_._decimals);

		return output;
	}

	public static int adapt_decimals(int decimals_) { return (numbers.is_ok(decimals_, MIN_DECIMALS, MAX_DECIMALS) ? decimals_ : DEFAULT_DECIMALS); }

	public static <x> Object adapt_value(String source_, x val_, String data_type_, boolean check_) { return adapt_value(source_, val_, new db_field(data_type_), check_); }

	public static <x> Object adapt_value(String source_, x val_, db_field field_, boolean check_) { return (is_ok(field_) ? adapt_value(source_, val_, field_._type, field_._size, field_._decimals, check_) : null); }

	public static <x> Object adapt_value(String source_, x val_, String data_type_, long size_, int decimals_, boolean check_) { return ((check_ && !complies(source_, val_, data_type_, size_, decimals_)) ? null : data.adapt_value(val_, to_data(data_type_, size_, decimals_), false)); }

	public static <x> boolean complies(String source_, x val_, String data_type_) { return complies(source_, val_, new db_field(data_type_)); }

	public static <x> boolean complies(String source_, x val_, db_field field_) { return (is_ok(field_) ? complies(source_, val_, field_._type, field_._size, field_._decimals) : false); }

	public static <x> boolean complies(String source_, x val_, String data_type_, long size_, int decimals_)
	{
		String type = data.check_type(data_type_);
		long size = check_size(source_, type, size_);

		return ((val_ == null || !strings.is_ok(type) || size <= 0) ? false : data.complies(val_, to_data(type, size, decimals_)));
	}

	public static data to_data(db_field field_) { return (is_ok(field_) ? to_data(field_._type, field_._size, field_._decimals) : null); }

	public static data to_data(String type_, long size_, int decimals_)
	{	
		String type = types.check_type(type_, types.DATA);
		if (!strings.is_ok(type) || size_ < 1 || decimals_ < 0) return null;

		double temp = db.get_max_value(type_);

		return new data(type_, new size(-1 * temp, temp, decimals_));
	}

	public db_field(db_field input_) { instantiate(input_); }

	public db_field(String type_) { instantiate(type_, WRONG_SIZE, DEFAULT_DECIMALS, null, null); }

	public db_field(String type_, String[] further_) { instantiate(type_, WRONG_SIZE, DEFAULT_DECIMALS, null, further_); }

	public db_field(String type_, long size_, String[] further_) { instantiate(type_, size_, DEFAULT_DECIMALS, null, further_); }

	public db_field(String type_, long size_) { instantiate(type_, size_, 0, null, null); }

	public db_field(String type_, long size_, int decimals_) { instantiate(type_, size_, decimals_, null, null); }

	public db_field(String type_, long size_, int decimals_, Object default_, String[] further_) { instantiate(type_, size_, decimals_, default_, further_); }

	public String get_type() { return _type; }

	public long get_size() { return _size; }

	public int get_decimals() { return _decimals; }

	public Object get_default() { return _default; }

	public String[] get_further() { return _further; }

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

	public boolean is_ok() { return is_ok(_type, _size, _decimals, _default, _further); }

	public static String[] get_all_types_no_size() { return _alls.DB_FIELD_TYPES_NO_SIZE; }

	static String[] populate_all_types_no_size() { return new String[] { data.TIMESTAMP, data.BOOLEAN, data.TINYINT }; }

	private void instantiate(db_field input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_type, input_._temp_size, input_._temp_decimals, input_._default, input_._further);
	}

	private void instantiate(String type_, long size_, int decimals_, Object default_, String[] further_)
	{
		instantiate_common();
		if (!is_ok(type_, size_, decimals_, default_, further_)) return;

		populate(_temp_type, _temp_size, _temp_decimals, default_, further_);
	}

	private boolean is_ok(String type_, long size_, int decimals_, Object default_, String[] further_)
	{
		_temp_type = data.check_type(type_);
		_temp_size = check_size(null, type_, size_);
		_temp_decimals = adapt_decimals(decimals_);

		return (default_is_ok(null, _temp_type, _temp_size, default_) && further_is_ok(further_));
	}

	private void populate(String type_, long size_, int decimals_, Object default_, String[] further_)
	{
		_type = type_;
		_size = size_;
		_decimals = decimals_;
		_default = default_;
		_further = (String[])arrays.get_new(further_);
	}
}