package Types;

public interface ITypeAdapter {

	public Types.Type adaptType(IC.AST.Type astType);

	public Types.MethodType adaptFunctionType(IC.AST.Method method);

}