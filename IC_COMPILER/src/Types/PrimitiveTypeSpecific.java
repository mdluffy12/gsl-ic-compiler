package Types;

public enum PrimitiveTypeSpecific {
	INTEGER, BOOLEAN, STRING;

	@Override
	public String toString() {
		switch (this) {
		case INTEGER: {
			return "int";
		}

		case BOOLEAN: {
			return "boolean";
		}

		case STRING: {
			return "string";
		}

		}
		return null;
	}

};
