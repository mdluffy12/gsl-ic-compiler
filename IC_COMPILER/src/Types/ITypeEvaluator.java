package Types;

import IC.AST.Expression;
import IC.Parser.SemanticError;

public interface ITypeEvaluator {
	
	public Type evaluateExpressionType(Expression exp) throws SemanticError;
	
}
