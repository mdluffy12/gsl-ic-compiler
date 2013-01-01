package Types;

/**
 * 
 * @author Roni Lichtman
 * 
 *         A type that can be used in an array
 */
public class ArrayType extends Type {

	protected final int dimension;

	public ArrayType(int dimension) {
		super();
		this.dimension = dimension;
	}

	public int getDimension() {
		return dimension;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		int currDim = dimension;
		while (currDim > 0) {
			sb.append("[]");
		}
		return sb.toString();
	}

}
