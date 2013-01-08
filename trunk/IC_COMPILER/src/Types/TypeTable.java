/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import IC.AST.ICClass;
import IC.AST.Program;

public class TypeTable {
	private static Map<Type, ArrayType> uniqueArrayTypes;
	private static Map<String, ClassType> uniqueClassTypes;
	private static List<MethodType> uniqueMethodTypes;

	public static Type intType;
	public static Type boolType;
	public static Type nullType;
	public static Type stringType;
	public static Type voidType;
	
	private static int curId;
	private static String fileName = "UNINITIALIZED FILENAME";
	private static String fourSpace = "    ";
	
	public static void initialize(Program program, String fileName)
			throws UndefinedSuperClassException {
		// Initializes the TypeTable by adding uniqueClassTypes by going over
		// top of AST
		
		//Initialize data structures
		uniqueArrayTypes = new LinkedHashMap<Type, ArrayType>();
		uniqueClassTypes = new LinkedHashMap<String, ClassType>();
		uniqueMethodTypes = new ArrayList<MethodType>();

		intType = new IntType(1);
		boolType = new BoolType(2);
		nullType = new NullType(3);
		stringType = new StringType(4);
		voidType = new VoidType(5);
		
		curId = 6;
		
		
		for (ICClass icClass : program.getClasses())
			addClassType(icClass);
		
		TypeTable.fileName = fileName;
		
		uniqueArrayTypes.put(TypeTable.stringType, new ArrayType(TypeTable.stringType, curId++));
		
		Type[] mainArgs = { TypeTable.arrayType(TypeTable.stringType) };
		uniqueMethodTypes.add(new MethodType(mainArgs, TypeTable.voidType, curId++));
	}

	public static ArrayType arrayType(Type elemType) {

		ArrayType arrType = null;

		if (uniqueArrayTypes.containsKey(elemType)) {
			// array type obj already created - we'll return it
			return uniqueArrayTypes.get(elemType);
		} else {
			// array type doesn't exist - create and return it
			// TODO : micha asks : what about dimension here? Am I missing something?
			arrType = new ArrayType(elemType, curId++);
			uniqueArrayTypes.put(elemType, arrType);
			return arrType;
		}
	}

	public static ClassType classType(String name)
			throws UndefinedClassException {
		if (uniqueClassTypes.containsKey(name)) {
			// class type obj already created - we'll return it
			
			return uniqueClassTypes.get(name);
		} else {
			// class type doesn't exist - error
			throw new UndefinedClassException("undefined class");
		}
	}

	private static void addClassType(ICClass icClass)
			throws UndefinedSuperClassException {
		ClassType classType = new ClassType(icClass, -1);
		uniqueClassTypes.put(classType.getName(), classType);
	}

	public static MethodType methodType(IC.AST.Method method)
			throws UndefinedClassException {
		for (Types.MethodType methodType : TypeTable.uniqueMethodTypes)
			if (methodType.equals(method))
				return methodType;

		Types.Type[] methodParams = new Types.Type[method.getFormals().size()];

		for (int i = 0; i < methodParams.length; i++)
			methodParams[i] = TypeAdapter.adaptType(method.getFormals().get(i)
					.getType());

		MethodType methodType = new MethodType(methodParams,
				TypeAdapter.adaptType(method.getType()), curId++);
		TypeTable.uniqueMethodTypes.add(methodType);
		return methodType;
	}
	
	public static String asString()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("Type Table: " + TypeTable.fileName.substring(TypeTable.fileName.lastIndexOf(File.separator) + 1) + "\n");
		
		List<Type> primitiveTypes = TypeTable.primitiveTypes();
		
		for(Type primType : primitiveTypes)
			result.append(fourSpace + primType.getId() + ": Primitive type: " + primType.toString() + "\n");
		
		for(ClassType classType : TypeTable.uniqueClassTypes.values())
			result.append(fourSpace + classType.getId() + ": Class: " + classType.toString() +
					(classType.hasSuperClass() ? (", Superclass ID: " + classType.getSuperClassType().getId()) : "") + "\n");
		
		for(Type arrType : TypeTable.uniqueArrayTypes.values())
			result.append(fourSpace + arrType.getId() + ": Array type: " + arrType.toString() + "\n");
		
		for(Type methodType : TypeTable.uniqueMethodTypes)
			result.append(fourSpace + methodType.getId() + ": Method type: " + methodType.toString() + "\n");
		
		return result.toString();
	}
	
	private static List<Type> primitiveTypes()
	{		
		List<Type> result = new ArrayList<Type>();
		result.add(TypeTable.intType);
		result.add(TypeTable.boolType);
		result.add(TypeTable.nullType);
		result.add(TypeTable.stringType);
		result.add(TypeTable.voidType);

		return result;
	}
	
	public static int getCurIdAndIncrement()
	{
		return TypeTable.curId++;
	}
}
