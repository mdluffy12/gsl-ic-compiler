package SymbolTable;

import IC.AST.ASTNode;

public interface ISymbolTableOperations {
	public ISymbolTable findSymbolTable(ASTNode node);
	public ISymbolTable findClassEnvironment(ISymbolTable currentScope,String name);
	public String findClassName(ASTNode node);
}
