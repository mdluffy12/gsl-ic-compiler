/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.AST;

import java.util.ArrayList;
import java.util.List;

/**
 * FunctionParameters is an inner definition that represents a function
 * parameter. This is not a part of the AST by itself ( FuntionArguments is NOT
 * an ASTNode!)
 * 
 * @author Micha
 */

public class FunctionParameters {
	List<Formal> parameters;

	/**
	 * construct empty parameter list
	 * 
	 */
	public FunctionParameters() {
		this.parameters = new ArrayList<Formal>();

	}

	/**
	 * construct FunctionParameters from a single argument
	 * 
	 */
	public FunctionParameters(Formal parameter) {
		this.parameters = new ArrayList<Formal>();
		addParameter(parameter);
	}

	/**
	 * construct FunctionParameters from an argument expression list
	 * 
	 */
	public FunctionParameters(List<Formal> parameters) {
		this.parameters = new ArrayList<Formal>();
		addParameters(parameters);
	}

	public void addParameter(Formal parameter) {
		this.parameters.add(parameter);
	}

	public void addParameters(List<Formal> parameters) {
		this.parameters.addAll(parameters);
	}

	public List<Formal> getParameters() {
		return this.parameters;
	}
}
