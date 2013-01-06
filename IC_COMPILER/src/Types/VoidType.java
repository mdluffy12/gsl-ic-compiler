package Types;

public class VoidType extends Type {
	
	public VoidType(){
		super("Void");
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		return false;
	}
 
}
