package Types;

public class UserType extends ArrayType {

	private final String name;

	public UserType(String name, int dimension) {
		super(dimension);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(name);

		int currDim = dimension;
		while (currDim > 0) {
			sb.append("[]");
			currDim--;
		}
		return sb.toString();

	}
}
