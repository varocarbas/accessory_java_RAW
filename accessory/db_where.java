package accessory;

public class db_where 
{
	public boolean _is_ok = false;
	
	public String _source = strings.DEFAULT;
	public String _key = strings.DEFAULT;
	public String _operand = _defaults.DB_WHERE_OPERAND;
	public Object _value = null;
	public String _link = _defaults.DB_WHERE_LINK;
	public boolean _is_literal = _defaults.DB_WHERE_LITERAL; //_value as a literal (e.g., 5 -> '5') or something else (e.g., col + 1 -> col + 1).
	
	private static String _source_temp = strings.DEFAULT;

	public db_where(db_where input_)
	{
		_is_ok = false;
		if (!is_ok(input_)) return;

		_is_ok = true;
		_source = _source_temp;
		_key = input_._key;
		_operand = input_._operand;
		_value = input_._value;
		_is_literal = input_._is_literal;
		_link = input_._link;
	}

	public db_where(String source_, String key_, Object value_)
	{
		_is_ok = false;
		
		String operand = _defaults.DB_WHERE_OPERAND;
		if (!is_ok(source_, key_, operand, value_)) return;
		
		_is_ok = true;
		_source = _source_temp;
		_key = key_;
		_operand = operand;
		_value = value_;	
		_is_literal = _defaults.DB_WHERE_LITERAL;
		_link = _defaults.DB_WHERE_LINK;
	}
	
	public db_where(String source_, String key_, String operand_, Object value_, boolean is_literal_, String link_)
	{
		_is_ok = false;
		if (!is_ok(source_, key_, operand_, value_)) return;
		
		_is_ok = true;
		_source = _source_temp;
		_key = key_;
		_operand = check_operand(operand_);
		_value = value_;	
		_is_literal = is_literal_;
		_link = check_operand(link_);
	}
	
	public String toString()
	{
		if (!is_ok(_source, _key, _operand, _value)) return strings.DEFAULT;
		
		String operand = operand_to_string(_operand);
		String key = db.get_variable(db.get_col(_source_temp, _key));
		String value = strings.to_string(_value);
		value = (_is_literal ? db.get_value(value) : value);
		
		String output = "(" + key + operand + value + ")";
				
		return output;
	}

	public boolean equals(db_where where2_)
	{
		if (!is_ok(where2_)) return false;
		
		return 
		(
			db.sources_are_equal(_source_temp, where2_._source) &&
			generic.are_equal(_key, where2_._key) &&
			generic.are_equal(_operand, where2_._operand) &&
			generic.are_equal(_value, where2_._value) &&
			generic.are_equal(_link, where2_._link) &&
			(_is_literal == where2_._is_literal)
		);		
	}
	
	public static String to_string(db_where where_)
	{
		return (is_ok(where_) ? where_.toString() : strings.DEFAULT);	
	}
	
	public static String to_string(db_where[] wheres_)
	{
		String output = "";
		if (!arrays.is_ok(wheres_)) return output;
		
		String last_link = null;
		int tot = 0;
		
		for (db_where where: wheres_)
		{
			if (!is_ok(where)) continue;
			tot++;
			
			if (!output.equals("")) output += " " + link_to_string(last_link) + " ";
			output += where.toString();
			
			last_link = (link_is_ok(where._link) ? where._link : _defaults.DB_WHERE_LINK);
		}
		
		if (tot > 1) output = "(" + output + ")";
		
		return output;	
	}
	
	public static String operand_to_string(String operand_)
	{
		String output = strings.DEFAULT;
		
		String operand = check_operand(operand_);
		if (!strings.is_ok(operand)) return output;
		
		if (operand.equals(_types.DB_WHERE_OPERAND_EQUAL)) output = "=";
		else if (operand.equals(_types.DB_WHERE_OPERAND_NOT_EQUAL)) output = "!=";
		else if (operand.equals(_types.DB_WHERE_OPERAND_GREATER)) output = ">";
		else if (operand.equals(_types.DB_WHERE_OPERAND_GREATER_EQUAL)) output = ">=";
		else if (operand.equals(_types.DB_WHERE_OPERAND_LESS)) output = "<";
		else if (operand.equals(_types.DB_WHERE_OPERAND_LESS_EQUAL)) output = "<=";
		
		return output;
	}
	
	public static String link_to_string(String link_)
	{
		return (link_is_ok(link_) ? _types.remove_type(link_, _types.DB_WHERE_LINK).toUpperCase() : strings.DEFAULT);
	}
	
	public static boolean are_equal(db_where where1_, db_where where2_)
	{
		return generic.are_equal(where1_, where2_);
	}
	
	public static boolean is_ok(db_where input_)
	{
		return (input_ != null && is_ok(input_._source, input_._key, input_._operand, input_._value));
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
		return _types.check_subtype(operand_, _types.get_subtypes(_types.DB_WHERE_OPERAND, null), null, null);
	}
	
	public static String check_link(String link_)
	{
		return _types.check_subtype(link_, _types.get_subtypes(_types.DB_WHERE_LINK, null), null, null);
	}
	
	private static boolean is_ok(String source_, String key_, String operand_, Object value_)
	{
		_source_temp = db.check_source(source_);

		return 
		( 
			strings.is_ok(_source_temp) &&
			db.field_is_ok(_source_temp, key_) &&
			operand_is_ok(operand_) &&
			generic.is_ok(value_)
		);
	}
}