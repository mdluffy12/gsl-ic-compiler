package IC.AST;

import java.util.List;

import IC.Parser.SemanticError;

/**
 * Static method AST node.
 * 
 * @author Tovi Almozlino
 */
public class StaticMethod extends Method {

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new static method node.
	 * 
	 * @param type
	 *            Data type returned by method.
	 * @param name
	 *            Name of method.
	 * @param formals
	 *            List of method parameters.
	 * @param statements
	 *            List of method's statements.
	 */
	public StaticMethod(Type type, String name, List<Formal> formals,
			List<Statement> statements) {
		super(type, name, formals, statements);
	}

	@Override
	public String toString() {

		if (getFormals() == null && getStatements() == null)
			return "<type = " + super.getType().getName() + "," + "ID = "
					+ super.getName();
		else if (getFormals() != null && getStatements() == null) {
			return "<type = " + super.getType().getName() + "," + "ID = "
					+ super.getName() + "," + "params = "
					+ super.getFormals().toString() + ">";
		} else if (getFormals() == null && getStatements() != null) {
			return "<type = " + super.getType().getName() + "," + "ID = "
					+ super.getName() + ">\n<body statments: "
					+ super.getStatements().toString() + ">";
		} else if (getFormals() != null && getStatements() != null) {
			return "<type = " + super.getType().getName() + "," + "ID = "
					+ super.getName() + "," + "params = "
					+ super.getFormals().toString() + ">\n<body statments: "
					+ super.getStatements().toString() + ">";
		}

		return null;

	}

}
