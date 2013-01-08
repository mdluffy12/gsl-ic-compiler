package IC.AST;

import IC.Parser.SemanticError;
import SymbolTable.SymbolTable;

/**
 * Abstract AST node base class.
 * 
 * @author Tovi Almozlino
 */
public abstract class ASTNode {

	protected int line;

	/**
	 * reference to symbol table of enclosing scope
	 */
	private SymbolTable enclosingScope;
	
	private boolean inLoop = false;

	/**
	 * Double dispatch method, to allow a visitor to visit a specific subclass.
	 * 
	 * @param visitor
	 *            The visitor.
	 * @return A value propagated by the visitor.
	 * @throws SemanticError
	 */
	public abstract Object accept(Visitor visitor) throws SemanticError;

	/**
	 * Constructs an AST node corresponding to a line number in the original
	 * code. Used by subclasses.
	 * 
	 * @param line
	 *            The line number.
	 */
	protected ASTNode(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public SymbolTable getEnclosingScope() {
		return enclosingScope;
	}

	public void setEnclosingScope(SymbolTable enclosingScope) {
		this.enclosingScope = enclosingScope;
	}

	public boolean isInLoop() {
		return inLoop;
	}

	public void setInLoop() {
		this.inLoop = true;
	}

}
