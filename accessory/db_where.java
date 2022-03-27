package accessory;

import java.util.HashMap;

public class db_where extends parent
{	
	public static final String EQUAL = types.DB_WHERE_OPERAND_EQUAL;
	public static final String NOT_EQUAL = types.DB_WHERE_OPERAND_NOT_EQUAL;
	public static final String GREATER = types.DB_WHERE_OPERAND_GREATER;
	public static final String GREATER_EQUAL = types.DB_WHERE_OPERAND_GREATER_EQUAL;
	public static final String LESS = types.DB_WHERE_OPERAND_LESS;
	public static final String LESS_EQUAL = types.DB_WHERE_OPERAND_LESS_EQUAL;
	public static final String OPERATOR_EQUAL = EQUAL;
	public static final String OPERATOR_NOT_EQUAL = NOT_EQUAL;
	public static final String OPERATOR_GREATER = GREATER;
	public static final String OPERATOR_GREATER_EQUAL = GREATER_EQUAL;
	public static final String OPERATOR_LESS = LESS;
	public static final String OPERATOR_LESS_EQUAL = LESS_EQUAL;
	
	public static final String AND = types.DB_WHERE_LINK_AND;
	public static final String OR = types.DB_WHERE_LINK_OR;
	public static final String LINK_AND = AND;
	public static final String LINK_OR = OR;
	
	public static final String DEFAULT_OPERAND = _defaults.DB_WHERE_OPERAND;
	public static final String DEFAULT_LINK = _defaults.DB_WHERE_LINK;
	public static final boolean DEFAULT_LITERAL = _defaults.DB_WHERE_LITERAL;
	
	public String _source = strings.DEFAULT;
	public String _key = strings.DEFAULT;
	public String _operand = DEFAULT_OPERAND;
	public Object _value = null;
	public String _link = DEFAULT_LINK;
	public boolean _is_literal = _defaults.DB_WHERE_LITERAL; //_value as a literal (e.g., 5 -> '5') or something else (e.g., col + 1 -> col + 1).
	
	private static HashMap<String, String[]> _all_operands = new HashMap<String, String[]>();
	private static HashMap<String, String[]> _all_links = new HashMap<String, String[]>();	
	
	static { ini(); }

	public static boolean are_equal(db_where where1_, db_where where2_)
	{
		return are_equal_common(where1_, where2_);
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
			
			last_link = (link_is_ok(where._link) ? where._link : DEFAULT_LINK);
		}
		
		if (tot > 1) output = "(" + output + ")";
		
		return output;	
	}
	
	public static boolean operand_is_ok(String operand_)
	{
		return val_is_ok_common(operand_, types.DB_WHERE_OPERAND, DEFAULT_OPERAND);
	}
	
	public static String check_operand(String operand_)
	{
		return check_val_common(operand_, types.DB_WHERE_OPERAND, DEFAULT_OPERAND);
	}
	
	public static String operand_to_string(String operand_)
	{
		return val_to_string_common(operand_, _all_operands, DEFAULT_OPERAND);
	}
	
	public static boolean link_is_ok(String link_)
	{
		return val_is_ok_common(link_, types.DB_WHERE_LINK, DEFAULT_LINK);
	}
	
	public static String check_link(String link_)
	{
		return check_val_common(link_, types.DB_WHERE_LINK, DEFAULT_LINK);
	}
	
	public static String link_to_string(String link_)
	{
		return val_to_string_common(link_, types.DB_WHERE_LINK, DEFAULT_LINK);
	}

	public db_where(db_where input_)
	{
		instantiate(input_);
	}

	public db_where(String source_, String key_, Object value_)
	{
		instantiate(source_, key_, DEFAULT_OPERAND, value_, DEFAULT_LITERAL, DEFAULT_LINK);
	}	

	public db_where(String source_, String key_, String operand_, Object value_)
	{
		instantiate(source_, key_, operand_, value_, _defaults.DB_WHERE_LITERAL, DEFAULT_LINK);
	}
	
	public db_where(String source_, String key_, String operand_, Object value_, String link_)
	{
		instantiate(source_, key_, operand_, value_, DEFAULT_LITERAL, link_);
	}
	
	public db_where(String source_, String key_, String operand_, Object value_, boolean is_literal_, String link_)
	{
		instantiate(source_, key_, operand_, value_, is_literal_, link_);
	}
	
	public String toString()
	{
		if (!is_ok(_source, _key, _operand, _value, _link)) return strings.DEFAULT;
		
		String operand = operand_to_string(_temp_string2);
		String key = db.get_variable(db.get_col(_temp_string1, _key));
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
			db.sources_are_equal(_temp_string1, where2_._source) &&
			generic.are_equal(_key, where2_._key) &&
			generic.are_equal(_temp_string2, where2_._operand) &&
			generic.are_equal(_value, where2_._value) &&
			generic.are_equal(_temp_string3, where2_._link) &&
			(_is_literal == where2_._is_literal)
		);		
	}

	public boolean is_ok()
	{
		_is_ok = is_ok(_source, _key, _operand, _value, _link);

		return _is_ok;
	}

	private static void ini()
	{
		populate_globals();
	}

	private static void populate_globals()
	{
		if (_all_operands.size() > 0) return;
		
		_all_operands.put(EQUAL, new String[] { "=" });
		_all_operands.put(NOT_EQUAL, new String[] { "!=" });
		_all_operands.put(GREATER, new String[] { ">" });
		_all_operands.put(GREATER_EQUAL, new String[] { ">=" });
		_all_operands.put(LESS, new String[] { "<" });
		_all_operands.put(LESS_EQUAL, new String[] { "<=" });
		
		_all_links.put(AND, new String[] { "and" });
		_all_links.put(OR, new String[] { "or" });
	}
	
	private void instantiate(db_where input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_string1, input_._key, input_._temp_string2, input_._value, input_._temp_string3, input_._is_literal);
	}
	
	private void instantiate(String source_, String key_, String operand_, Object value_, boolean is_literal_, String link_)
	{
		instantiate_common();
		if (!is_ok(source_, key_, operand_, value_, link_)) return;

		populate(_temp_string1, key_, _temp_string2, value_, _temp_string3, is_literal_);
	}
	
	private boolean is_ok(String source_, String key_, String operand_, Object value_, String link_)
	{
		_temp_string1 = db.check_source(source_);
		_temp_string2 = check_operand(operand_);
		_temp_string3 = check_link(link_);
		
		return 
		( 
			strings.is_ok(_temp_string1) &&
			db.field_is_ok(_temp_string1, key_) &&
			strings.is_ok(_temp_string2) &&
			generic.is_ok(value_)
		);
	}
		
	private void populate(String source_, String key_, String operand_, Object value_, String link_, boolean is_literal_)
	{
		_is_ok = true;
		
		_source = source_;
		_key = key_;
		_operand = operand_;
		_value = value_;	
		_is_literal = is_literal_;
		_link = link_;
	}
}