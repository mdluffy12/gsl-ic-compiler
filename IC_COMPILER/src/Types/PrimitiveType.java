package Types;

public class PrimitiveType extends ArrayType {
	private final PrimitiveTypeSpecific kind;

	public PrimitiveType(PrimitiveTypeSpecific kind, int dimension) {
		super(dimension);
		this.kind = kind;
	}

	public PrimitiveTypeSpecific getKind() {
		return kind;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(kind.toString());

		int currDim = dimension;
		while (currDim > 0) {
			sb.append("[]");
			currDim--;
		}
		return sb.toString();

	}
}