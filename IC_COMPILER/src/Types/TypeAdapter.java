/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

public class TypeAdapter {

	// This class is responsible for adapting types from the AST package to
	// types from the Types package
	public static Types.Type adaptType(IC.AST.ASTNode astNode)
			throws UndefinedClassException {

		if (astNode instanceof IC.AST.ICClass) {
			ClassType result = TypeTable.classType(((IC.AST.ICClass) astNode)
					.getName());

			if (result.getId() == Type.unititializedTypeID)
				result.setId(TypeTable.getCurIdAndIncrement());
		}

		else if (astNode instanceof IC.AST.UserType) {
			IC.AST.UserType uType = (IC.AST.UserType) astNode;

			return handleDimension(Types.TypeTable.classType(uType.getName()),
					uType.getDimension());

		} else if (astNode instanceof IC.AST.PrimitiveType) {
			IC.AST.PrimitiveType pType = (IC.AST.PrimitiveType) astNode;

			Types.Type baseType = null;
			String typeName = pType.getName();

			if (typeName.equals("int")) {
				
				baseType = Types.TypeTable.intType;
				
			} else if (typeName.equals("boolean")) {
				
				baseType = Types.TypeTable.boolType;
				
			} else if (typeName.equals("string")) {
				
				baseType = Types.TypeTable.stringType;
				
			} else if (typeName.equals("void")) {
				
				baseType = Types.TypeTable.voidType;
				
			} else {
				// should'nt really get here
				System.out.println("Unexpected primitive type in TypeAdapter!");
				return null;
			}

			return handleDimension(baseType, pType.getDimension());
		} else if (astNode instanceof IC.AST.Method) {
			IC.AST.Method method = (IC.AST.Method) astNode;
			return TypeTable.methodType(method);
		}
		return null;
	}

	// This helper function deals with the translation of dimension in AST
	// package to ArrayTypes in Types package
	private static Types.Type handleDimension(Types.Type baseType, int dimension) {
		if (dimension > 0) {
			Types.ArrayType resType = null;
			boolean first = true;
			for (int i = 0; i < dimension; i++) {
				Type elemType = first ? baseType : resType;
				resType = TypeTable.arrayType(elemType);
				first = false;
			}

			return resType;
		} else
			return baseType;
	}
}
