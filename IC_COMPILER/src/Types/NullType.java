package Types;

public class NullType extends Type {
	
	public NullType(){
		super("null");
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		if(otherType.equals(TypeTable.intType) ||
				otherType.equals(TypeTable.boolType) ||
				otherType.equals(TypeTable.voidType))
			return false;
		
		return true;
			
	}
}
