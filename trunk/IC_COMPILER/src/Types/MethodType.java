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

}
