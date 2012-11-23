package IC.AST;

import java.util.ArrayList;
import java.util.List;

/**
 * Statements block AST node.
 * 
 * @author Tovi Almozlino
 */
public class StatementsBlock extends Statement {

	private final List<Statement> statements;

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new empty statements block node.
	 * 
	 * @param line
	 *            Line number where block begins.
	 */
	public StatementsBlock(int line) {
		super(line);
		this.statements = new ArrayList<Statement>();
	}

	/**
	 * Constructs a new statements block node.
	 * 
	 * @param line
	 *            Line number where block begins.
	 * @param statements
	 *            List of all statements in block.
	 */
	public StatementsBlock(int line, List<Statement> statements) {
		super(line);
		this.statements = statements;
	}

	/**
	 * adds a new statement to statement block
	 * 
	 * @param statement
	 *            add statement to statement list
	 */
	public void addStatement(Statement statement) {
		this.statements.add(statement);
	}

	@Override
	public void setLine(int line) {
		super.setLine(line);
	}

	public List<Statement> getStatements() {
		return statements;
	}

}
