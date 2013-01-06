package Types;

public class ArrayType extends Type {

	private Type elemType;

	public ArrayType(Type elemType) {
		super(elemType.getName() + "[]");

		this.setElemType(elemType);

	}

	public Type getElemType() {
		return elemType;
	}

	public void setElemType(Type elemType) {
		this.elemType = elemType;
	}

	/**
	 * @return get number of dimensions of array (int[][][] returns 3)
	 */
	public int getDimension() {

		String typeName = new String(getName());

		return (typeName.length() - typeName.replace("[]", "").length()) / 2;

	}

}
