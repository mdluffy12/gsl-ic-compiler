/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

public class MethodType extends Type {
	private final Type[] paramTypes;
	private final Type returnType;
	
	public MethodType(Type[] paramTypes, Type returnType)
	{
		super(buildName(paramTypes, returnType));
	 
		this.paramTypes = paramTypes;
		this.returnType = returnType;
	}
	
	private static String buildName(Type[] paramTypes, Type returnType)
	{
		/* ---- > changed 
		String name = "Function with parameters: ";
		boolean first = true;
		for(Type paramType : paramTypes)
		{
			name += (!first ? ", " : "") + paramType.getName();
			first = false;
		}
		name += "; return " + returnType.getName();
		return name; */
		
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (Type t : paramTypes) {

			sb.append(((!first) ? ", " : "") + t.toString());
			first = false;
		}

		sb.append(" -> " + returnType.toString());

		return sb.toString();
		
		
	}
	
	/**
	 * @return the paramTypes
	 */
	public Type[] getParamTypes() {
		return this.paramTypes;
	}

	/**
	 * @return the returnType
	 */
	public Type getReturnType() {
		return this.returnType;
	}

	public boolean equals(IC.AST.Method method) throws UndefinedClassException
	{
		if(method.getFormals().size() != this.paramTypes.length)
			return false;
		if(!TypeAdapter.adaptType(method.getType()).equals(this.returnType))
			return false;
		for(int i=0; i<this.paramTypes.length; i++)
			if(!TypeAdapter.adaptType(method.getFormals().get(i).getType()).equals(this.paramTypes[i]))
				return false;
		return true;
	}
	
	@Override
	public boolean subTypeOf(Type otherType)
	{
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (Type t : paramTypes) {

			sb.append(((!first) ? ", " : "") + t.toString());
			first = false;
		}

		sb.append(" -> " + returnType.toString());

		return sb.toString();

	}
}
