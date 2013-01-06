package Types;

public class ArrayType extends Type {
	
 
private Type elemType;
 
	
	public ArrayType(Type elemType) {
		super("array of " + elemType.getName());
 
		this.setElemType(elemType);
	}

	public Type getElemType() {
		return elemType;
	}

	public void setElemType(Type elemType) {
		this.elemType = elemType;
	}
 
	
    @Override
	public String toString(){

		StringBuilder sb = new StringBuilder();
		
		sb.append(getElemType().toString() + " ");
		return null;
		
		/*
		int currDim = getDimension();
		while (currDim > 0) {
			sb.append("[]");
		}
		return sb.toString(); */
	}
 
}
