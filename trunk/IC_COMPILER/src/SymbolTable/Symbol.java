package SymbolTable;

import java.util.Comparator;

import IC.AST.ASTNode;

/**
 * Symbol represents an entry to the symbol table 
 * 
 * @author Micha
 */
public class Symbol {

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- data members --------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * SymbolKind types:
	 * 
	 * 1) Class : class definition
	 * 
	 * 2) Field : field definition
	 * 
	 * 3) Param : function parameter definition
	 * 
	 * 4) Default_method : any method definition (this is temporary state of
	 * method in the class symbol table, and eventually updated with the real
	 * method type - static, virtual or library)
	 * 
	 * 5) Static_method : static method definition
	 * 
	 * 6) Virtual_method : virtual method definition
	 * 
	 * 7) Library_method : library method definition
	 * 
	 * 8) Variable : local variable definition
	 * 
	 */
	public enum SymbolKind {
		_class, _field, _param, _default_method, _static_method, _virtual_method, _library_method, _variable;

		@Override
		public String toString() {

			switch (this) {

			case _class: {
				return "Class";
			}

			case _field: {
				return "Field";
			}

			case _param: {
				return "Parameter";
			}

			case _default_method: {
				return "method";
			}

			case _static_method: {
				return "Static method";
			}

			case _virtual_method: {
				return "Virtual method";
			}

			case _library_method: {
				return "Static method";
			}

			case _variable: {
				return "Local variable";
			}

			default:
				return null;

			}
		}

	}

	/**
	 * symbol name
	 */
	private String idName;

	/**
	 * symbol kind (class/field/method..)
	 */
	private SymbolKind idKind;

	/**
	 * symbol type (int,string..)
	 */
	private Types.Type idType;

	/**
	 * symbol AST node
	 */
	private ASTNode idNode;

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- Constructors --------------------------
	 * -------------------------------------------------------------------
	 */

	public Symbol(String idName, SymbolKind idKind, Types.Type idType,
			ASTNode idNode) {
		this.setIdName(idName);
		this.setIdKind(idKind);
		this.setIdType(idType);
		this.setIdNode(idNode);
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------- member functions ------------------------
	 * -------------------------------------------------------------------
	 */

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public SymbolKind getIdKind() {
		return idKind;
	}

	public void setIdKind(SymbolKind idKind) {
		this.idKind = idKind;
	}

	public Types.Type getIdType() {
		return idType;
	}

	public void setIdType(Types.Type idType) {
		this.idType = idType;
	}

	public ASTNode getIdNode() {
		return idNode;
	}

	public void setIdNode(ASTNode idNode) {
		this.idNode = idNode;
	}

	public SymbolTable getSymbolTable() {
		return getIdNode().getEnclosingScope();
	}

	public int getLine() {
		return getIdNode().getLine();
	}

	/**
	 * clones symbol table
	 */
	@Override
	public Symbol clone() {

		return new Symbol(idName, idKind, idType, idNode);

	}

	// TODO: finish type handling
	@Override
	public String toString() {

		if (this.isMethod()) {
			return this.idKind.toString() + ": " + this.idName + " " + "{"
					+ this.getIdType().toString() + "}";
		}

		if (this.getIdType() == null) {
			return this.idKind.toString() + ": " + this.idName;
		}

		return this.idKind.toString() + ": " + this.getIdType().toString()
				+ " " + this.idName;
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------ SymbolKind functions ---------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * @return true iff symbol kind is class
	 */
	public boolean isClass() {
		return this.idKind == SymbolKind._class;
	}

	/**
	 * @return true iff symbol kind is field
	 */
	public boolean isField() {
		return this.idKind == SymbolKind._field;
	}

	/**
	 * @return true iff symbol kind is parameter
	 */
	public boolean isParameter() {
		return this.idKind == SymbolKind._param;
	}

	/**
	 * @return true iff symbol kind is static method
	 */
	public boolean isStaticMethod() {
		return this.idKind == SymbolKind._static_method;
	}

	/**
	 * @return true iff symbol kind is virtual method
	 */
	public boolean isVirtualMethod() {
		return this.idKind == SymbolKind._virtual_method;
	}

	/**
	 * @return true iff symbol kind is library method
	 */
	public boolean isLibraryMethod() {
		return this.idKind == SymbolKind._library_method;
	}

	/**
	 * @return true iff symbol kind is default method
	 */
	public boolean isDefaultMethod() {
		return this.idKind == SymbolKind._default_method;
	}

	/**
	 * @return true iff symbol kind is method (static, virtual or library)
	 */
	public boolean isMethod() {
		return this.isDefaultMethod() || this.isStaticMethod()
				|| this.isVirtualMethod() || this.isLibraryMethod();
	}

	/**
	 * @return true iff symbol kind is variable
	 */
	public boolean isVariable() {
		return this.idKind == SymbolKind._variable;
	}

	public static class SymbolComperator implements Comparator<Symbol> {

		@Override
		public int compare(Symbol s1, Symbol s2) {

			int s1Val = 0, s2Val = 0;

			switch (s1.idKind) {

			case _field:
				s1Val = 5;
			case _param:
				s1Val = 4;
			case _virtual_method:
				s1Val = 3;
			case _static_method:
				s1Val = 2;
			case _variable:
				s1Val = 1;
			default:
				s1Val = 0;
			}

			switch (s2.idKind) {

			case _field:
				s2Val = 5;
			case _param:
				s2Val = 4;
			case _virtual_method:
				s2Val = 3;
			case _static_method:
				s2Val = 2;
			case _variable:
				s2Val = 1;
			default:
				s2Val = 0;
			}

			return s1Val - s2Val;
		}

	}

}
