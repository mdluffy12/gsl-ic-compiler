package SymbolTable;

import IC.AST.ASTNode;
import IC.AST.This;

public interface ISymbolTableOperations {
	public ISymbolTable findSymbolTable(ASTNode node);
	
	public ISymbolTable findClassEnvironment(String className);
	
	public String findClassNameOfThis(This thisExpression);
}
