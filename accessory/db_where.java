package accessory;

public class db_where 
{
	public String _source = strings.DEFAULT;
	public String _key = strings.DEFAULT;
	public String _operand = defaults.DB_WHERE_OPERAND;
	public Object _value = null;
	public String _link = defaults.DB_WHERE_LINK;
	public boolean _is_literal = defaults.DB_WHERE_LITERAL; //_value as a literal (e.g., 5 -> '5') or something else (e.g., col + 1 -> col + 1).
	
	public String toString()
	{
		if (!is_ok(_source, _key, _operand, _value)) return strings.DEFAULT;
		
		String operand = operand_to_string(_operand);
		String key = db.get_variable(db.get_col(_source, _key));
		String value = strings.to_string(_value);
		value = (_is_literal ? db.get_value(value) : value);
		
		String output = key + operand + value;
				
		return output;
	}

	public boolean equals(db_where where2_)
	{
		if (!is_ok(where2_)) return false;
		
		return 
		(
			generic.are_equal(_source, where2_._source) &&
			generic.are_equal(_key, where2_._key) &&
			generic.are_equal(_operand, where2_._operand) &&
			generic.are_equal(_value, where2_._value) &&
			generic.are_equal(_link, where2_._link)
		);		
	}
	
	public static String to_string(db_where where_)
	{
		return (is_ok(where_) ? where_.toString() : strings.DEFAULT);	
	}
	
	public static String to_string(db_where[] wheres_, String source_id_)
	{
		String output = "";
		if (!arrays.is_ok(wheres_)) return output;
		
		String last_link = null;
		
		for (db_where where: wheres_)
		{
			if (!is_ok(where)) continue;
			
			if (!output.equals("")) output += " " + last_link + " ";
			output += "(" + where.toString() + ")";
			
			last_link = (link_is_ok(where._link) ? where._link : defaults.DB_WHERE_LINK);
		}
		
		return output;	
	}
	
	public static String operand_to_string(String operand_)
	{
		String output = strings.DEFAULT;
		
		String operand = check_operand(operand_);
		if (!strings.is_ok(operand)) return output;
		
		if (operand.equals(types.DB_WHERE_OPERAND_EQUAL)) output = "=";
		else if (operand.equals(types.DB_WHERE_OPERAND_NOT_EQUAL)) output = "!=";
		else if (operand.equals(types.DB_WHERE_OPERAND_GREATER)) output = ">";
		else if (operand.equals(types.DB_WHERE_OPERAND_GREATER_EQUAL)) output = ">=";
		else if (operand.equals(types.DB_WHERE_OPERAND_LESS)) output = "<";
		else if (operand.equals(types.DB_WHERE_OPERAND_LESS_EQUAL)) output = "<=";
		
		return output;
	}
	
	public static boolean are_equal(db_where where1_, db_where where2_)
	{
		return generic.are_equal(where1_, where2_);
	}
	
	public static boolean is_ok(db_where input_)
	{
		return (input_ != null && is_ok(input_._source, input_._key, input_._operand, input_._value));
	}

	public db_where(db_where input_)
	{
		if (!is_ok(input_)) return;

		_source = input_._source;
		_key = input_._key;
		_operand = input_._operand;
		_value = input_._value;
		_link = input_._link;
	}

	public db_where(String source_, String key_, String operand_, Object value_, String link_)
	{
		if (!is_ok(source_, key_, operand_, value_)) return;
		
		_source = source_;
		_key = key_;
		_operand = check_operand(operand_);
		_value = value_;	
		_link = check_operand(link_);	
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
		return types.check_subtype(operand_, types.get_subtypes(types.DB_WHERE_OPERAND, null), null, null);
	}
	
	public static String check_link(String link_)
	{
		return types.check_subtype(link_, types.get_subtypes(types.DB_WHERE_LINK, null), null, null);
	}
	
	private static boolean is_ok(String source_, String key_, String operand_, Object value_)
	{
		return 
		( 
			db.source_is_ok(source_) &&
			strings.is_ok(db.get_col(source_, key_)) &&
			operand_is_ok(operand_) &&
			generic.is_ok(value_)
		);
	}
}