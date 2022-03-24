package accessory;

public class db_order extends parent 
{	
	public static final String ASC = types.DB_ORDER_ASC;
	public static final String DESC = types.DB_ORDER_DESC;
	public static final String ORDER_ASC = ASC;
	public static final String ORDER_DESC = DESC;
	
	public static final String DEFAULT = _defaults.DB_ORDER;
	public static final String DEFAULT_ORDER = DEFAULT;
	
	public String _source = strings.DEFAULT;
	public String _value = strings.DEFAULT; //Always stored as a field and only converted to col before outputting a string.
	public String _order = DEFAULT;
	public boolean _is_field = _defaults.DB_ORDER_FIELD; //_value being a field/col vs. something else like a condition.
	
	public static boolean is_ok(db_order input_)
	{
		return (input_ != null && input_.is_ok());
	}

	public static String to_string(db_order order_)
	{
		return (is_ok(order_) ? order_.toString() : strings.DEFAULT);	
	}

	public static boolean are_equal(db_order order1_, db_order order2_)
	{
		return generic.are_equal(order1_, order2_);
	}
		
	public static String to_string(db_order[] orders_)
	{
		String output = "";
		if (!arrays.is_ok(orders_)) return output;
		
		for (db_order order: orders_)
		{
			if (!is_ok(order)) continue;
			
			if (!output.equals("")) output += ", ";
			output += order.toString();
		}

		return output;
	}
	
	public static boolean order_is_ok(String order_)
	{
		return val_is_ok_common(order_, types.DB_ORDER, DEFAULT);
	}
	
	public static String check_order(String order_)
	{
		return check_val_common(order_, types.DB_ORDER, DEFAULT);
	}

	public static String order_to_string(String order_)
	{
		return val_to_string_common(order_, types.DB_ORDER, DEFAULT);
	}
	
	public db_order(db_order input_)
	{
		instantiate(input_._source, input_._value, input_._order, input_._is_field);
	}
	
	public db_order(String source_, String value_, String order_, boolean is_field_)
	{
		instantiate(source_, value_, order_, is_field_);
	}
	
	public String toString()
	{	
		if (!is_ok(_source, _value, _order)) return strings.DEFAULT;

		String key = (_is_field ? db.get_variable(db.get_col(_temp_string1, _value)) : _value);
		
		String output = key + " " + order_to_string(_order);
		
		return output;
	}

	public boolean equals(db_order order2_)
	{
		if (!is_ok(order2_)) return false;
		
		return 
		(
			db.sources_are_equal(_temp_string1, order2_._source) &&
			generic.are_equal(_value, order2_._value) && 
			generic.are_equal(_order, order2_._order) &&
			(_is_field == order2_._is_field)
		);		
	}

	public boolean is_ok()
	{
		 return is_ok(_source, _value, _order);
	}
	
	private void instantiate(String source_, String value_, String order_, boolean is_field_)
	{
		instantiate_common();
		if (!is_ok(source_, value_, order_)) return;

		populate(value_, order_, is_field_);
	}
	
	private boolean is_ok(String source_, String value_, String order_)
	{
		_temp_string1 = db.check_source(source_);
		
		return (strings.is_ok(_temp_string1) && strings.is_ok(value_) && order_is_ok(order_));
	}
	
	private void populate(String value_, String order_, boolean is_field_)
	{
		_is_ok = true;
		
		_source = _temp_string1;
		_value = value_;
		_order = check_order(order_);
		_is_field = is_field_;
	}
}