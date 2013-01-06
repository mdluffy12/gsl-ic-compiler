package Types;

public class ArrayType extends Type {
	
	Type elemType;
	
	public ArrayType(Type elemType) {
		super("array of " + elemType.getName());
		this.elemType = elemType;
	}

	/**
	 * @return the elemType
	 */
	public Type getElemType() {
		return this.elemType;
	}
}
