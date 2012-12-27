package IC.AST;

import java.util.List;

import IC.Parser.SemanticError;

/**
 * Root AST node for an IC program.
 * 
 * @author Tovi Almozlino
 */
public class Program extends ASTNode {

	private List<ICClass> classes;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new program node.
	 * 
	 * @param classes
	 *            List of all classes declared in the program.
	 */
	public Program(List<ICClass> classes) {
		super(0);
		this.classes = classes;
	}

	public List<ICClass> getClasses() {
		return classes;
	}

	public void setClasses(List<ICClass> classes) {
		this.classes = classes;
		;
	}

	@Override
	public String toString() {
		return getClasses().toString();

	}

}
