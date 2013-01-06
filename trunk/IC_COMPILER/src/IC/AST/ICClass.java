package IC.AST;

import java.util.ArrayList;
import java.util.List;

import IC.Parser.SemanticError;

/**
 * Class declaration AST node.
 * 
 * @author Tovi Almozlino
 */
public class ICClass extends ASTNode {

	private final String name;

	private String superClassName = null;

	private final List<Field> fields;

	private final List<Method> methods;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new class node.
	 * 
	 * @param line
	 *            Line number of class declaration.
	 * @param name
	 *            Class identifier name.
	 * @param fields
	 *            List of all fields in the class.
	 * @param methods
	 *            List of all methods in the class.
	 */
	public ICClass(int line, String name, List<Field> fields,
			List<Method> methods) {
		super(line);
		this.name = name;
		this.fields = fields;
		this.methods = methods;
	}

	/**
	 * Constructs a new class node, with a superclass.
	 * 
	 * @param line
	 *            Line number of class declaration.
	 * @param name
	 *            Class identifier name.
	 * @param superClassName
	 *            Superclass identifier name.
	 * @param fields
	 *            List of all fields in the class.
	 * @param methods
	 *            List of all methods in the class.
	 */
	public ICClass(int line, String name, String superClassName,
			List<Field> fields, List<Method> methods) {
		this(line, name, fields, methods);
		this.superClassName = superClassName;
	}

	public String getName() {
		return name;
	}

	public boolean hasSuperClass() {
		return (superClassName != null);
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<Method> getMethods() {
		return methods;
	}

	/**
	 * returns all the fields in a FieldOrMethod
	 * 
	 * @param foms
	 *            FieldOrMethod list from the class
	 */
	public static ArrayList<Field> GetFieldsFromFOMlist(List<FieldOrMethod> foms) {

		ArrayList<Field> fields = new ArrayList<Field>();
		for (FieldOrMethod fom : foms) {
			if (fom.isField())
				fields.add(fom.getField());
		}

		return fields;
	}

	/**
	 * returns all the methods in a FieldOrMethod
	 * 
	 * @param foms
	 *            FieldOrMethod list from the class
	 */
	public static ArrayList<Method> GetMethodsFromFOMlist(
			List<FieldOrMethod> foms) {
		ArrayList<Method> methods = new ArrayList<Method>();
		for (FieldOrMethod fom : foms) {
			if (fom.isMethod())
				methods.add(fom.getMethod());
		}

		return methods;

	}

	/**
	 * @return true iff class represents a library class
	 */
	public boolean isLibrary() {
		return this.getName().equals("Library");
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(this.getName() + ": \n");
		for (Field f : this.getFields()) {
			sb.append("field: " + f.getName() + "\n");
		}

		for (Method m : this.getMethods()) {
			sb.append("method: " + m.getName() + "\n");
		}

		return sb.toString();

	}

}
