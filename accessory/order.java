package accessory;

public class order 
{
	public String _key = strings.DEFAULT;
	public String _order = types.ORDER_ASC;
	
	public String toString()
	{
		return toString(null);
	}
	
	public String toString(String source_)
	{	
		if (!order_is_ok(_order)) return strings.DEFAULT;
		
		boolean is_db = db.source_is_ok(source_);

		String key = (is_db ? db.get_variable(db.get_col(source_, _key)) : _key);
		
		String output = key + " " + _order;

		if (!is_db) output = (misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE);
		
		return output;
	}

	public boolean equals(order order2_)
	{
		if (!is_ok(order2_)) return false;
		
		return (generic.are_equal(_key, order2_._key) && generic.are_equal(_order, order2_._order));		
	}
	
	public static String to_string(order order_)
	{
		return (is_ok(order_) ? order_.toString() : strings.DEFAULT);	
	}
	
	public static boolean are_equal(order order1_, order order2_)
	{
		boolean is_ok1 = generic.is_ok(order1_);
		boolean is_ok2 = generic.is_ok(order2_);
		
		return ((!is_ok1 || !is_ok2) ? (is_ok1 == is_ok2) : order1_.equals(order2_));
	}
	
	public static boolean is_ok(order input_)
	{
		if (input_ == null) return false;
		
		return (strings.is_ok(input_._key) && order_is_ok(input_._order));
	}

	public order(order input_)
	{
		if (!is_ok(input_)) return;

		_key = input_._key;
		_order = input_._order;
	}

	public order(String key_, String order_)
	{
		_key = key_;
		_order = order_;
	}
	
	public static boolean order_is_ok(String order_)
	{
		return (strings.is_ok(check_order(order_)));
	}
	
	public static String check_order(String order_)
	{
		return types.check_subtype(order_, types.get_subtypes(types.ORDER, null), null, null);
	}
}
