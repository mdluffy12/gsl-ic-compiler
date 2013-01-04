package Types;

public class ArrayType extends Type {
	
	Type elemType;
	int dimension;
	
	public ArrayType(Type elemType) {
		super("array of " + elemType.getName());
		this.elemType = elemType;
	}
}
