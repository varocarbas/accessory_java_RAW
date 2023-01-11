package accessory;

import java.util.HashMap;

public abstract class parent
{
	//Despite the abstract method absence, equals() is also expected to be always implemented.
	public abstract String toString();
	public abstract boolean is_ok();

	public static boolean is_ok(parent input_) { return (input_ == null ? false : input_.is_ok()); }

	public static String to_string(parent input_) { return (input_ != null ? input_.toString() : strings.DEFAULT); }

	protected static boolean are_equal_common(Object input1_, Object input2_) { return generic.are_equal(input1_, input2_); }

	protected static boolean val_is_ok_common(String val_, String type_, String default_) { return (strings.is_ok(check_val_common(val_, type_, default_))); }

	protected static boolean val_is_ok_common(String val_, HashMap<String, String[]> types_, String default_) { return (strings.is_ok(check_val_common(val_, types_, default_))); }

	protected static String check_val_common(String val_, String type_, String default_) { return check_internal(_types.check_type(val_, type_), default_); }

	protected static String check_val_common(String val_, HashMap<String, String[]> types_, String default_) { return check_internal(_types.check_multiple(val_, types_), default_); }

	protected static String val_to_string_common(String val_, String type_, String default_) { return (val_is_ok_common(val_, type_, default_) ? to_string_internal(_types.remove_type(val_, type_)) : strings.DEFAULT); }

	protected static String val_to_string_common(String val_, HashMap<String, String[]> types_, String default_)
	{
		String val = check_val_common(val_, types_, default_);

		return ((strings.is_ok(val) && arrays.key_exists(types_, val)) ? to_string_internal(types_.get(val)[0]) : strings.DEFAULT);
	}

	protected void instantiate_common() { }

	private static String check_internal(String output_, String default_)
	{
		String output = output_;

		if (!strings.is_ok(output) && strings.is_ok(default_)) output = default_;

		return output;
	}

	private static String to_string_internal(String output_)
	{
		String output = output_;

		if (strings.is_ok(output)) output = output.toUpperCase();

		return output;
	}
}