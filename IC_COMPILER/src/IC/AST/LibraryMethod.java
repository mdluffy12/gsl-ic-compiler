package IC.AST;

import java.util.ArrayList;
import java.util.List;

import IC.Parser.SemanticError;

/**
 * Library method declaration AST node.
 * 
 * @author Tovi Almozlino
 */
public class LibraryMethod extends Method {

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new library method declaration node.
	 * 
	 * @param type
	 *            Data type returned by method.
	 * @param name
	 *            Name of method.
	 * @param formals
	 *            List of method parameters.
	 */
	public LibraryMethod(Type type, String name, List<Formal> formals) {
		super(type, name, formals, new ArrayList<Statement>());
	}

	@Override
	public String toString() {
		if (getFormals() != null) {
			return "<type = " + super.getType().getName() + "," + "ID = "
					+ super.getName() + "," + "params = "
					+ super.getFormals().toString() + ">";
		}

		return "<type = " + super.getType().getName() + "," + "ID = "
				+ super.getName();
	}

}