package Types;

import IC.AST.ICClass;

public class ClassType extends Type {
	
	private ClassType superClassType = null;

	public ClassType(ICClass classAST) throws UndefinedSuperClassException {
		super(classAST.getName());
		if(classAST.hasSuperClass()) {
			//Since extending classes appear after their super class, the super class should have already
			//been added to the TypeTable
			try
			{
			this.superClassType = TypeTable.classType(classAST.getSuperClassName());
			}
			catch(UndefinedClassException e)
			{
				throw new UndefinedSuperClassException();
			}
		}
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
	
	
}
