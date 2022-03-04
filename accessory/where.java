package accessory;

public class where 
{
	public String _key = strings.DEFAULT;
	public String _operand = types.OPERAND_EQUAL;
	public Object _value = null;
	public String _link = null;
	
	public String toString()
	{
		return toString(null);
	}
	
	public String toString(String source_id_)
	{
		if (!operand_is_ok(_operand)) return strings.DEFAULT;
		
		boolean is_db = db.source_is_ok(source_id_);
		
		String key = (is_db ? db.get_variable(db.get_col(source_id_, _key)) : _key);
		String value = strings.to_string(_value);
		value = (is_db ? db.get_value(value) : value);
		
		String output = key + _operand + value;
		
		if (link_is_ok(_link)) output += _link.toUpperCase();
		
		if (!is_db) output = (misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE);
		
		return output;
	}

	public boolean equals(where where2_)
	{
		if (!is_ok(where2_)) return false;
		
		return 
		(
			generic.are_equal(_key, where2_._key) &&
			generic.are_equal(_operand, where2_._operand) &&
			generic.are_equal(_value, where2_._value) &&
			generic.are_equal(_link, where2_._link)
		);		
	}
	
	public static String to_string(where where_, String source_id_)
	{
		return (is_ok(where_) ? where_.toString(source_id_) : strings.DEFAULT);	
	}
	
	public static String to_string(where[] wheres_, String source_id_)
	{
		String output = "";
		if (!arrays.is_ok(wheres_)) return output;
		
		String last_link = null;
		
		for (where where: wheres_)
		{
			if (!is_ok(where)) continue;
			
			if (!link_is_ok(last_link) && !output.equals("")) output += " " + last_link + " ";
			output += where.toString(source_id_);
			
			last_link = where._link;
		}
		
		return output;	
	}
	
	public static boolean are_equal(where where1_, where where2_)
	{
		boolean is_ok1 = generic.is_ok(where1_);
		boolean is_ok2 = generic.is_ok(where2_);
		
		return ((!is_ok1 || !is_ok2) ? (is_ok1 == is_ok2) : where1_.equals(where2_));
	}
	
	public static boolean is_ok(where input_)
	{
		if (input_ == null) return false;
		
		return 
		( 
			strings.is_ok(input_._key) &&
			operand_is_ok(input_._operand) &&
			generic.is_ok(input_._value)
		);
	}

	public where(where input_)
	{
		if (!is_ok(input_)) return;

		_key = input_._key;
		_operand = input_._operand;
		_value = input_._value;
		_link = input_._link;
	}

	public where(String key_, String operand_, Object value_, String link_)
	{
		_key = key_;
		_operand = operand_;
		_value = value_;	
		_link = link_;	
	}
	
	public static boolean operand_is_ok(String operand_)
	{
		return (strings.is_ok(check_operand(operand_)));
	}

	public static boolean link_is_ok(String link_)
	{
		return (strings.is_ok(check_link(link_)));
	}
	
	public static String check_operand(String operand_)
	{
		return types.check_subtype(operand_, types.get_subtypes(types.OPERAND, null), null, null);
	}
	
	public static String check_link(String link_)
	{
		return types.check_subtype(link_, types.get_subtypes(types.LINK, null), null, null);
	}
}