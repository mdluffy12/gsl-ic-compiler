package SymbolTable;

/**
 * Defines an interface to the symbol table inner operations
 * 
 * Used to decouple the implementation of the symbol tables and it's usage in
 * the semantic checks and the type evaluation
 * 
 * @authors Micha,Roni
 */
public interface ISymbolTable {
	public Symbol lookup(String symbolName);
}
