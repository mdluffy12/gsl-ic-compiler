/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.AST;

/**
 * FieldOrMethod is an inner definition that represents either a single field,
 * or a single method. This is not a part of the AST by itself ( FieldOrMethod
 * is NOT an ASTNode!)
 * 
 * @author Micha
 */
public class FieldOrMethod {

	private final Field field;
	private final Method method;

	/**
	 * construct FieldOrMethod from a field
	 * 
	 */
	public FieldOrMethod(Field field) {
		this.field = field;
		this.method = null;
	}

	/**
	 * construct FieldOrMethod from a method
	 * 
	 */
	public FieldOrMethod(Method method) {
		this.method = method;
		this.field = null;
	}

	public boolean isField() {
		return this.field != null;
	}

	public boolean isMethod() {
		return this.method != null;
	}

	public Field getField() {
		return this.field;
	}

	public Method getMethod() {
		return this.method;
	}

	@Override
	public String toString() {
		if (this.isField()) {
			return getField().toString();
		} else if (this.isMethod()) {
			return getMethod().toString();
		}

		return null;
	}

}
