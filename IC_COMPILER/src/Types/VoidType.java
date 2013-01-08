/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

public class VoidType extends Type {
	
	public VoidType(int id){
		super("void", id);
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		return false;
	}
 
}
