package Types;

public class MethodType extends Type {
	Type[] paramTypes;
	Type returnType;
	
	public MethodType(Type[] paramTypes, Type returnType)
	{
		super(buildName(paramTypes, returnType));
		this.paramTypes = paramTypes;
		this.returnType = returnType;
	}
	
	private static String buildName(Type[] paramTypes, Type returnType)
	{
		String name = "Function with parameters: ";
		boolean first = true;
		for(Type paramType : paramTypes)
		{
			name += (!first ? ", " : "") + paramType.getName();
			first = false;
		}
		name += "; return " + returnType.getName();
		return name;
	}
	
	public boolean equals(IC.AST.Method method) throws UndefinedClassException
	{
		if(method.getFormals().size() != this.paramTypes.length)
			return false;
		if(!TypeAdapter.adaptType(method.getType()).equals(this.returnType))
			return false;
		for(int i=0; i<this.paramTypes.length; i++)
			if(!TypeAdapter.adaptType(method.getFormals().get(i)).equals(this.paramTypes[i]))
				return false;
		return true;
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		return false;
	}
}
