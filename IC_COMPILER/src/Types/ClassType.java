package Types;

import IC.AST.ICClass;

public class ClassType extends Type {
	
	private Type superClassType;

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
	
	
}
