package Types;

import java.util.List;

public class MethodType extends Type {
	private final List<Type> parameterTypes;
	private final Type returnType;

	public MethodType(List<Type> parameterTypes, Type returnType) {
		super();
		this.parameterTypes = parameterTypes;
		this.returnType = returnType;
	}

	public List<Type> getParameterTypes() {
		return parameterTypes;
	}

	public Type getReturnType() {
		return returnType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (Type t : parameterTypes) {

			sb.append(((!first) ? ", " : "") + t.toString());
			first = false;
		}

		sb.append(" -> " + returnType.toString());

		return sb.toString();

	}

}
