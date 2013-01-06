/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.AST;

import java.util.ArrayList;
import java.util.List;

/**
 * CallArguments represents expression list as an arguments for a function call
 * This is not a part of the AST by itself (CallArguments is NOT an ASTNode!) 
 * @author Micha
 */

public class CallArguments {
	List<Expression> arguments;

	/**
	 * construct empty argument list
	 * 
	 */

	public CallArguments() {
		this.arguments = new ArrayList<Expression>();
	}

	/**
	 * construct CallArguments from a single argument
	 * 
	 */
	public CallArguments(Expression argument) {
		this.arguments = new ArrayList<Expression>();
		addArgument(argument);
	}

	/**
	 * construct CallArguments from an argument expression list
	 * 
	 */
	public CallArguments(List<Expression> arguments) {
		this.arguments = new ArrayList<Expression>();
		addArguments(arguments);
	}

	public void addArgument(Expression argument) {
		this.arguments.add(argument);
	}

	public void addArguments(List<Expression> arguments) {
		this.arguments.addAll(arguments);
	}

	public List<Expression> getArguments() {
		return this.arguments;
	}
}
