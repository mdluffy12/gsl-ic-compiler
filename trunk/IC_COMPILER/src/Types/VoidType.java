package Types;

public class VoidType extends Type {
	
	public VoidType(){
		super("void");
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		return false;
	}
}
