package Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.AST.ICClass;
import IC.AST.Program;

public class TypeTable {
	private static Map<Type, ArrayType> uniqueArrayTypes = new HashMap<Type, ArrayType>();
	private static Map<String, ClassType> uniqueClassTypes = new HashMap<String, ClassType>();
	private static List<MethodType> uniqueMethodTypes = new ArrayList<MethodType>();

	public static Type boolType = new BoolType();
	public static Type intType = new IntType();
	public static Type stringType = new StringType();
	public static Type voidType = new VoidType();
	public static Type nullType = new NullType();
	

	public static void initialize(Program program) throws UndefinedSuperClassException {
		// Initializes the TypeTable by adding uniqueClassTypes by going over
		// top of AST

		for(ICClass icClass : program.getClasses())
			addClassType(icClass);		
	}

	public static ArrayType arrayType(Type elemType) {
		if (uniqueArrayTypes.containsKey(elemType)) {
			// array type obj already created - we'll return it
			return uniqueArrayTypes.get(elemType);
		} else {
			// array type doesn't exist - create and return it
			ArrayType arrType = new ArrayType(elemType);
			uniqueArrayTypes.put(elemType, arrType);
			return arrType;
		}
	}

	public static ClassType classType(String name) throws UndefinedClassException {
		if (uniqueClassTypes.containsKey(name)) {
			// class type obj already created - we'll return it
			return uniqueClassTypes.get(name);
		} else {
			// class type doesn't exist - error
			throw new UndefinedClassException("undefined class");
		}
	}

	private static void addClassType(ICClass icClass)  throws UndefinedSuperClassException
	{
		ClassType classType = new ClassType(icClass);
		uniqueClassTypes.put(classType.getName(), classType);
	}
	
	public static MethodType methodType(IC.AST.Method method) throws UndefinedClassException 
	{
		for(Types.MethodType methodType : TypeTable.uniqueMethodTypes)
			if(methodType.equals(method))
				return methodType;
		
		
		Types.Type[] methodParams = new Types.Type[method.getFormals().size()];
		for(int i=0; i<methodParams.length; i++)
			methodParams[i] = TypeAdapter.adaptType(method.getFormals().get(i));
			
		MethodType methodType = new MethodType(methodParams, TypeAdapter.adaptType(method.getType()));
		TypeTable.uniqueMethodTypes.add(methodType);
		return methodType;
	}
}
