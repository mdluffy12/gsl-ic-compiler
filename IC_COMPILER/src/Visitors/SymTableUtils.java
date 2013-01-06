package Visitors;

import java.util.List;

import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.LocalVariable;
import IC.AST.Method;
import IC.AST.Type;
import IC.Parser.SemanticError;
import SymbolTable.ISymbolTable;
import SymbolTable.ISymbolTableOperations;
import SymbolTable.Symbol;
import SymbolTable.Symbol.SymbolKind;
import SymbolTable.SymbolTable;
import Types.TypeAdapter;

/**
 * SymTableUtils handles all functional utilities regarding the symbol table
 * construction and usage
 * 
 * @author micha
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

	private static boolean compareTypes(Type t1, Type t2) {
		// TODO check if this is the kind of type comparison we need to do
		return t1.getName().equals(t2.getName())
				&& t1.getDimension() == t2.getDimension();
	}

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

		// check if this is the type we need to check
		return m1.getName().equals(m2.getName());

	}

	/**
	 * @return true iff all fields are declared legally
	 */
	private static boolean CheckFieldCollisions(List<Field> fields) {

		for (Field field : fields) {
			for (Field iterated_field : fields) {
				if (field != iterated_field
						&& compareFields(field, iterated_field) == true) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @return true iff all methods are declared legally
	 */
	private static boolean CheckMethodCollisions(List<Method> methods) {
		for (Method method : methods) {
			for (Method iterated_method : methods) {
				if (method != iterated_method
						&& compareMethods(method, iterated_method)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void CheckFieldMethodCollisions(List<Field> fields,
			List<Method> methods) throws SemanticError {

		if (CheckFieldCollisions(fields)) {
			// TODO throw exception
			System.out.println("duplicate fields");
		}

		if (CheckMethodCollisions(methods)) {
			// TODO throw exception
			System.out.println("duplicate methods");
		}

		for (Method method : methods) {
			for (Field field : fields) {
				if (field.getName().equals(method.getName())) {
					System.out.println("method and field with same name");
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
	public static boolean isMainMethod(Method m, SymbolKind methodKind) {

		List<Formal> params = m.getFormals();
		int numParams = params.size();
		Formal firstParam = null;
		if (params != null && numParams > 0) {
			firstParam = params.get(0);
		} else
			return false;

		// TODO , in addition check:
		// 1) method type is void (after type table is done)
		return m.getName().equals("main") && numParams == 1
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

	public static Types.Type getNodeType(ASTNode node) {
		Types.Type type = null;
		try {
			type = TypeAdapter.adaptType(node);
		} catch (Exception e) {
			// System.out.println(e);
		}
		return type;
	}

	/*
	 * -------------------------------------------------------------------
	 * -------------------------- table Utils -------------------------
	 * -------------------------------------------------------------------
	 */

	public static void printTable(SymbolTable symbolTable) {
		System.out.println("\n" + symbolTable.toString());
	}

	@Override
	public SymbolTable findSymbolTable(ASTNode node) {
		return node.getEnclosingScope();
	}

	@Override
	public SymbolTable findClassEnvironment(ISymbolTable currentScope, String name) {

		// search name in current symbol table path to root
		Symbol symbol = currentScope.lookup(name);

		if (symbol == null)
			return null;

		return symbol.getSymbolTable();

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
