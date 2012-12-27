package Types;

public class PrimitiveType extends Type {
	private final PrimitiveTypeSpecific kind;

	public PrimitiveType(PrimitiveTypeSpecific kind) {
		this.kind = kind;
	}
}