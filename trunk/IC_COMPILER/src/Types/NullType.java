/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

public class NullType extends Type {
	
	public NullType(int id){
		super("null", id);
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
