package Types;

public class ArrayType extends Type {
	
 
private Type elemType;
 
	
	public ArrayType(Type elemType) {
		super(elemType.toString() + "[]");
 
		this.setElemType(elemType);
	}

	public Type getElemType() {
		return elemType;
	}

	public void setElemType(Type elemType) {
		this.elemType = elemType;
	}
 
}
