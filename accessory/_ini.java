package accessory;

class _ini extends parent_ini
{
	private static _ini _instance = new _ini();
	
	public _ini() { }
	
	public static void populate() { _instance.populate_all(); }
	
	protected void populate_first_basic() { _basic.populate(); }
	
	protected void populate_first_alls() { _alls.populate(); }

	protected void populate_first_defaults() { _defaults.populate(); }
	
	protected void populate_config() { _ini_config.populate(); }
	
	protected void populate_db() { _ini_db.populate(); }
}