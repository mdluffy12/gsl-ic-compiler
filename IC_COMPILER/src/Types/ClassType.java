/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

import IC.AST.ICClass;
import IC.Parser.SemanticError;

public class ClassType extends Type {

	private ClassType superClassType = null;

	public ClassType(ICClass classAST, int id) throws SemanticError {
		super(classAST.getName(), id);
		if (classAST.hasSuperClass()) {
			// Since extending classes appear after their super class, the super
			// class should have already
			// been added to the TypeTable

			String superName = classAST.getSuperClassName();

			try {
				this.setSuperClassType(TypeTable.classType(superName));
			} catch (UndefinedClassException e) {

				// check if class extends itself
				if (classAST.getName().equals(superName)) {

					throw new SemanticError("cycle detected: the type "
							+ classAST.getName() + " cannot extend itself",classAST);
				}

				throw new SemanticError("super class " + superName
						+ " is undefined", classAST);

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
	public boolean subTypeOf(Type otherType) {
		ClassType ancestorClassType = this;
		while (ancestorClassType != null) {
			if (ancestorClassType.equals(otherType))
				return true;
			ancestorClassType = ancestorClassType.superClassType;
		}

		return false;
	}

	public boolean hasSuperClass() {
		return this.superClassType != null;
	}

	public void setId(int id) {
		this.id = id;
	}

}
