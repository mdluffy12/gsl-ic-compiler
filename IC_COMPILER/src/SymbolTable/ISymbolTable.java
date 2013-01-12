/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package SymbolTable;

import IC.AST.ASTNode;

/**
 * Defines an interface to the symbol table inner operations
 * 
 * Used to decouple the implementation of the symbol tables and it's usage in
 * the semantic checks and the type evaluation
 * 
 * @authors Micha,Roni
 */
public interface ISymbolTable {
	
	public String getId();
	
	public Symbol lookup(String symbolName, ASTNode astNode);
	
	public Symbol localLookup(String symbolName);
	
	public Symbol getMethodParent();
	
	public boolean isClassTable();
	
	public SymbolTable getParentSymbolTable();
}
