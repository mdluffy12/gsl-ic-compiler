/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

public abstract class Type {
	
	public static int unititializedTypeID = -1;
	
	private final String name;
	protected int id = unititializedTypeID; //Identifies when this type was added to the type table
	
	public Type(String name, int id)
	{
		this.name = name;
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
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
