package Types;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapter implements ITypeAdapter {

	@Override
	public Types.Type adaptType(IC.AST.Type astType) {
		if (astType instanceof IC.AST.UserType) {
			IC.AST.UserType astUserType = (IC.AST.UserType) astType;
			return new Types.UserType(astUserType.getName(),
					astUserType.getDimension());
		} else if (astType instanceof IC.AST.PrimitiveType) {
			IC.AST.PrimitiveType astPrimitiveType = (IC.AST.PrimitiveType) astType;
			if (astPrimitiveType.getName().equals("void")) {
				return new VoidType();
			} else {
				return new Types.PrimitiveType(
						adaptPrimitiveTypeSpecific(astPrimitiveType.getName()),
						astPrimitiveType.getDimension());
			}
		}

		return null; // Dead code
	}

	private PrimitiveTypeSpecific adaptPrimitiveTypeSpecific(String name) {
		switch (name) {
		case "int":
			return PrimitiveTypeSpecific.INTEGER;
		case "boolean":
			return PrimitiveTypeSpecific.BOOLEAN;
		case "string":
			return PrimitiveTypeSpecific.STRING;
		default:
			return null;
		}
	}

	@Override
	public Types.MethodType adaptFunctionType(IC.AST.Method method) {
		List<Types.Type> parameterTypes = new ArrayList<Types.Type>();
		for (IC.AST.Formal formal : method.getFormals())
			parameterTypes.add(adaptType(formal.getType()));

		return new Types.MethodType(parameterTypes, adaptType(method.getType()));
	}

}
