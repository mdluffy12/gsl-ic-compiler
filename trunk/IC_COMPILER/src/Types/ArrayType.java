package Types;

/**
 * 
 * @author Roni Lichtman
 * 
 *         A type that can be used in an array
 */
public class ArrayType extends Type {

	private final int dimension;

	public ArrayType(int dimension) {
		super();
		this.dimension = dimension;
	}

}
