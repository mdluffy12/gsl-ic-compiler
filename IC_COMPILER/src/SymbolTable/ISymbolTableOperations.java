/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package SymbolTable;

import IC.AST.ASTNode;

/**
 * Defines an interface to the symbol table operations
 * 
 * Used to decouple the implementation of the symbol tables and it's usage in
 * the semantic checks and the type evaluation
 * 
 * @authors Micha,Roni
 */
public interface ISymbolTableOperations {

	/**
	 * @return the symbol table to which the current AST node is referenced to
	 *         (In a practical sense : the scope of the node)
	 */
	public ISymbolTable findSymbolTable(ASTNode node);

	/**
	 * @return the class symbol table which the class name is referenced to
	 */
	public ISymbolTable findClassEnvironment(String className);

	/**
	 * @return the node's class name where he is defined
	 */
	public String findClassName(ASTNode node);
}
