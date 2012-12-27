package IC.AST;

import IC.DataTypes;
import IC.Parser.SemanticError;

/**
 * Primitive data type AST node.
 * 
 * @author Tovi Almozlino
 */
public class PrimitiveType extends Type {

	private final DataTypes type;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new primitive data type node.
	 * 
	 * @param line
	 *            Line number of type declaration.
	 * @param type
	 *            Specific primitive data type.
	 */
	public PrimitiveType(int line, DataTypes type) {
		super(line);
		this.type = type;
	}

	@Override
	public String getName() {
		return type.getDescription();
	}

	@Override
	public String toString() {
		int dim = super.getDimension();
		if (dim == 0)
			return getName();

		return "<type = " + getName() + " , dimension = " + dim + ">";
	}

}