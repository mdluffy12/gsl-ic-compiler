package SymbolTable;

import IC.AST.ASTNode;
import IC.AST.Type;

/**
 * TODO add info
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
	 * TODO add info
	 */
	private String idName;

	/**
	 * TODO add info
	 */
	private SymbolKind idKind;

	/**
	 * TODO add info
	 */
	private Type idType;

	/**
	 * TODO add info
	 */
	private ASTNode idNode;

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- Constructors --------------------------
	 * -------------------------------------------------------------------
	 */

	public Symbol(String idName, SymbolKind idKind, Type idType, ASTNode idNode) {
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

	public Type getIdType() {
		return idType;
	}

	public void setIdType(Type idType) {
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

	// TODO: change representation, meanwhile just for debug
	@Override
	public String toString() {

		return this.idKind.toString() + ": " + this.idName;
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
				|| this.isVirtualMethod() || this.isVirtualMethod();
	}

	/**
	 * @return true iff symbol kind is variable
	 */
	public boolean isVariable() {
		return this.idKind == SymbolKind._variable;
	}

}
