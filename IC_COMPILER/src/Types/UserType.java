package Types;

public class UserType extends ArrayType {

	private final String name;

	public UserType(String name, int dimension) {
		super(dimension);
		this.name = name;
	}
}
