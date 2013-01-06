/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package SymbolTable;

import IC.AST.ASTNode;

/**
 * ClassSymbol defines a special symbol for a class (to be inserted only to the
 * global table)
 * 
 * ClassSymbol holds a special pointer to it's referenced class
 * 
 * @authors Micha
 */
public class ClassSymbol extends Symbol {

	private SymbolTable classTable;

	public ClassSymbol(String idName, SymbolKind idKind, Types.Type idType,
			ASTNode idNode, SymbolTable classTable) {
		super(idName, idKind, idType, idNode);
		this.classTable = classTable;
	}

	public void setClassTable(SymbolTable classTable) {
		this.classTable = classTable;
	}

	public SymbolTable getClassTable() {

		return classTable;
	}

	@Override
	public String toString() {
		return getIdKind().toString() + ": " + getIdName();
	}

}
