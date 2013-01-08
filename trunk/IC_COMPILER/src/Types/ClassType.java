/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

import IC.AST.ICClass;

public class ClassType extends Type {
	
	private ClassType superClassType = null;

	public ClassType(ICClass classAST, int id) throws UndefinedSuperClassException {
		super(classAST.getName(), id);
		if(classAST.hasSuperClass()) {
			//Since extending classes appear after their super class, the super class should have already
			//been added to the TypeTable
			try
			{
			this.setSuperClassType(TypeTable.classType(classAST.getSuperClassName()));
			}
			catch(UndefinedClassException e)
			{
				throw new UndefinedSuperClassException("undefined super class");
			}
		}
	}

	public Type getSuperClassType() {
		return superClassType;
	}

	public void setSuperClassType(Type superClassType) {
		this.superClassType = (ClassType) superClassType;
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		ClassType ancestorClassType = this;
		while(ancestorClassType != null)
		{
			if(ancestorClassType.equals(otherType))
				return true;
			ancestorClassType = ancestorClassType.superClassType;
		}
		
		return false;
	}
	
	public boolean hasSuperClass()
	{
		return this.superClassType!=null;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	
}
