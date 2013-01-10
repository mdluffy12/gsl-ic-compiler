/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Visitors;

import java.util.List;

import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.LocalVariable;
import IC.AST.Method;
import IC.Parser.SemanticError;
import SymbolTable.ClassSymbol;
import SymbolTable.ISymbolTableOperations;
import SymbolTable.Symbol;
import SymbolTable.Symbol.SymbolKind;
import SymbolTable.SymbolTable;
import Types.MethodType;
import Types.TypeAdapter;
import Types.TypeTable;
import Types.UndefinedClassException;

/**
 * SymTableUtils handles all functional utilities regarding the symbol table
 * construction and usage
 * 
 * @author Micha
 */
public class SymTableUtils implements ISymbolTableOperations {

	public SymTableUtils() {
	}

	public static void initUtils() {
	}

	/*
	 * -------------------------------------------------------------------
	 * -------------------------- ic class Utils -------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * @return true iff fields are equal (according to the IC rules)
	 */
	private static boolean compareFields(Field f1, Field f2) {

		return f1.getName().equals(f2.getName());

	}

	/**
	 * @return true iff methods are equal (according to the IC rules)
	 */
	private static boolean compareMethods(Method m1, Method m2) {

		return m1.getName().equals(m2.getName());
	}

	/**
	 * @return true iff all fields are declared legally
	 * @throws SemanticError
	 */
	private static void CheckFieldCollisions(String className,
			List<Field> fields) throws SemanticError {

		for (Field field : fields) {
			for (Field iterated_field : fields) {
				if (field != iterated_field
						&& compareFields(field, iterated_field) == true) {
					throw new SemanticError("duplicated field "
							+ iterated_field.getName() + " in type "
							+ className, iterated_field);
				}
			}
		}

	}

	/**
	 * @return true iff all methods are declared legally
	 * @throws SemanticError
	 */
	private static void CheckMethodCollisions(String className,
			List<Method> methods) throws SemanticError {
		for (Method method : methods) {
			for (Method iterated_method : methods) {
				if (method != iterated_method
						&& compareMethods(method, iterated_method)) {
					throw new SemanticError("duplicated method "
							+ iterated_method.getName() + " in type "
							+ className, iterated_method);
				}
			}
		}

	}

	public static void CheckFieldMethodCollisions(ICClass icClass)
			throws SemanticError {

		String className = icClass.getName();
		List<Method> methods = icClass.getMethods();
		List<Field> fields = icClass.getFields();

		// look for duplicate fields
		CheckFieldCollisions(className, fields);

		// look for duplicate methods
		CheckMethodCollisions(className, methods);

		// look for any duplicate combination
		for (Method method : methods) {
			for (Field field : fields) {
				if (field.getName().equals(method.getName())) {
					throw new SemanticError(
							"method and field are declared with the same name \""
									+ field.getName() + "\"" + " in type "
									+ className, field);
				}
			}
		}

	}

	/*
	 * -------------------------------------------------------------------
	 * ---------------------------- method Utils -------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * check if method represents a legal main method
	 */
	public static boolean isMainMethod(Method m, SymbolKind methodKind)
			throws SemanticError {

		List<Formal> params = m.getFormals();
		int numParams = params.size();
		Formal firstParam = null;
		if (params != null && numParams > 0) {
			firstParam = params.get(0);
		} else
			return false;

		return m.getName().equals("main")
				&& numParams == 1
				&& ((MethodType) TypeAdapter.adaptType(m)).getReturnType()
						.equals(TypeTable.voidType)
				&& methodKind == SymbolKind._static_method
				&& firstParam.getType().getName().equals("string")
				&& firstParam.getType().getDimension() == 1;

	}

	/*
	 * -------------------------------------------------------------------
	 * ---------------------------- scope Utils --------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * @return true iff class is defined in scope
	 */
	public static boolean isClassDefined(ICClass icClass, SymbolTable scope) {
		Symbol classSymbol = scope.localLookup(icClass.getName());
		return classSymbol != null;
	}

	/**
	 * @return true iff function parameter is declared in scope
	 */
	public static boolean isParameterDefinedInScope(Formal param,
			SymbolTable scope) {
		Symbol paramSymbol = scope.localLookup(param.getName());
		return paramSymbol != null;
	}

	/**
	 * @return true iff local variable is declared in scope
	 */
	public static boolean isVariableDefinedInScope(LocalVariable localVariable,
			SymbolTable scope) {
		String variableName = localVariable.getName();
		Symbol localVarSymbol = scope.localLookup(variableName);
		return localVarSymbol != null;
	}

	/*
	 * -------------------------------------------------------------------
	 * ---------------------------- type Utils --------------------------
	 * -------------------------------------------------------------------
	 */

	public static Types.Type getNodeType(ASTNode node) throws SemanticError {
		Types.Type type = null;
		try {
			type = TypeAdapter.adaptType(node);
		}
		catch(UndefinedClassException e) {
			throw new SemanticError(e.getClassname() + " cannot be resolved to a type", node);
		}
		return type;
	}

	/*
	 * -------------------------------------------------------------------
	 * -------------------------- table Utils -------------------------
	 * -------------------------------------------------------------------
	 */

	public static void printTable(SymbolTable symbolTable) {
		System.out.print("\n" + symbolTable.toString());
	}

	@Override
	public SymbolTable findSymbolTable(ASTNode node) {
		return node.getEnclosingScope();
	}

	@Override
	public SymbolTable findClassEnvironment(String className) {

		// get global table
		SymbolTable globalTable = SymbolTable.getRoot();

		// get class symbol
		ClassSymbol classSymbol = (ClassSymbol) globalTable
				.localLookup(className);

		if (classSymbol == null) {
			// class not found!
			return null;
		}

		// get referenced class table
		return classSymbol.getClassTable();

	}

	@Override
	public String findClassName(ASTNode node) {

		// get current scope
		SymbolTable currentScope = node.getEnclosingScope();

		// get class in which the node was defined
		Symbol classSymbol = currentScope.getClassParent();

		return classSymbol.getIdName();
	}

}
