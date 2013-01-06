/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

import IC.AST.Expression;
import IC.Parser.SemanticError;

public interface ITypeEvaluator {
	
	public Type evaluateExpressionType(Expression exp) throws SemanticError;
	
}
