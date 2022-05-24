package accessory;

public abstract class parent_ini_first
{
	protected boolean _populated = false;
			
	protected abstract void populate_internal();
	
	protected void populate_internal_common()
	{
		if (_populated) return;
		
		populate_internal();
		
		_populated = true;
	}
}