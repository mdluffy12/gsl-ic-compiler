package Types;

public abstract class Type {
	private final String name;

	public Type(String name)
	{
		this.name = name;
	}
	
	public boolean subTypeOf(Type otherType) {
		return this.equals(otherType);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
