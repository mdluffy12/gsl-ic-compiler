package Types;

public class PrimitiveType extends ArrayType {
	private final PrimitiveTypeSpecific kind;

	public PrimitiveType(PrimitiveTypeSpecific kind, int dimension) {
		super(dimension);
		this.kind = kind;
	}
}