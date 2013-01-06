package SymbolTable;

import IC.AST.ASTNode;

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
