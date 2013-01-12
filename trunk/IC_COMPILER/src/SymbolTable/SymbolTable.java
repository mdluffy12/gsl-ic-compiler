/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package SymbolTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import IC.AST.ASTNode;

/**
 * SymbolTable represents a map from a symbol name to a symbol value as well of
 * an hierarchy of all inherited tables
 * 
 * @author Micha
 */
public class SymbolTable implements ISymbolTable {

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- data members --------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * SymbolTable types:
	 * 
	 * 1) Global : names of all classes
	 * 
	 * 2) Class : field and methods
	 * 
	 * 3) Method : formals (parameters) and locals
	 * 
	 * 4) Block : variables defined in a block
	 */
	public enum TableType {
		_global, _class, _method, _block;

		@Override
		public String toString() {

			switch (this) {

			case _global: {
				return "Global Symbol Table";
			}

			case _class: {
				return "Class Symbol Table";
			}

			case _method: {
				return "Method Symbol Table";
			}

			case _block: {
				return "Statement Block Symbol Table";
			}

			default:
				return null;

			}
		}
	}

	/**
	 * map between a symbol name, and it's corresponding value
	 */
	private Map<String, Symbol> entries;

	/**
	 * symbol id
	 */
	private String id;

	/**
	 * symbol table parent (always single)
	 */
	private SymbolTable parentSymbolTable;

	/**
	 * symbol table type (global/class/method/block)
	 */
	private TableType table_type;

	/**
	 * a list consisting of all table children (can be more then one)
	 */
	private List<SymbolTable> childrenTables;

	/**
	 * is true iff symbol table is a block symbol table AND is defined within a
	 * loop (hence in 'while)
	 */
	private boolean isLoop;

	/**
	 * holds the root of all the symbol table
	 */
	private static SymbolTable root;

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- Constructors --------------------------
	 * -------------------------------------------------------------------
	 */

	public SymbolTable(String id) {
		this.setId(id);
		setEntries(new LinkedHashMap<String, Symbol>());
		setChildrenTables(new ArrayList<SymbolTable>());

		isLoop = false;

	}

	public SymbolTable(String id, TableType table_type) {
		this.setId(id);
		this.setTable_type(table_type);
		setEntries(new LinkedHashMap<String, Symbol>());
		setChildrenTables(new ArrayList<SymbolTable>());
		isLoop = false;
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------- member functions ------------------------
	 * -------------------------------------------------------------------
	 */

	public Map<String, Symbol> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, Symbol> entries) {
		this.entries = entries;
	}

	@Override
	public SymbolTable getParentSymbolTable() {
		return parentSymbolTable;
	}

	public void setParentSymbolTable(SymbolTable parentSymbolTable) {
		this.parentSymbolTable = parentSymbolTable;

		// add current symbol table as child table for parent table
		if (parentSymbolTable != null) {
			parentSymbolTable.addChildTable(this);
		}

	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TableType getTable_type() {
		return table_type;
	}

	public void setTable_type(TableType table_type) {
		this.table_type = table_type;
	}

	public List<SymbolTable> getChildrenTables() {
		return childrenTables;
	}

	public void setChildrenTables(List<SymbolTable> childrenTables) {
		this.childrenTables = childrenTables;
	}

	public boolean isLoop() {
		return this.isLoop;
	}

	public void setLoop() {
		this.isLoop = true;
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------- table functions ------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * @return true iff symbol table is the root of the symbol table hierarchy
	 *         (table has no Reference to a parent)
	 */
	public boolean isRoot() {
		return this.parentSymbolTable == null;
	}

	/*
	 * -------------------------------------------------------------------
	 * ---------------------- symbol table features ----------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * adds symbol to current scope
	 * 
	 * @param symbol
	 *            new symbol to add to the table entries
	 */
	public void addSymbol(Symbol symbol) {
		this.entries.put(symbol.getIdName(), symbol);
	}

	/**
	 * removes symbol from current scope
	 * 
	 * @param idName
	 *            idName of a symbol to remove from the table entries
	 */
	public void removeSymbol(String idName) {
		this.entries.remove(idName);
	}

	/**
	 * adds child table to current parent table
	 * 
	 * @param childTable
	 *            new SymbolTable to be added as child
	 */
	public void addChildTable(SymbolTable childTable) {
		this.childrenTables.add(childTable);
	}

	/**
	 * @return true iff table has at least one child table
	 */
	public boolean hasChildTables() {
		return !this.childrenTables.isEmpty();
	}

	/**
	 * @return true iff table has at least one symbol
	 */
	public boolean hasSymbols() {
		return !this.getEntries().isEmpty();
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------ TableType functions ----------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * @return true iff table type is global
	 */
	public boolean isGlobalTable() {
		return this.table_type == TableType._global;
	}

	/**
	 * @return true iff table type is class
	 */
	@Override
	public boolean isClassTable() {
		return this.table_type == TableType._class;
	}

	/**
	 * @return true iff table type is method
	 */
	public boolean isMethodTable() {
		return this.table_type == TableType._method;
	}

	/**
	 * @return true iff table type is block
	 */
	public boolean isBlockTable() {
		return this.table_type == TableType._block;
	}

	/**
	 * clones symbol table
	 */
	@Override
	public SymbolTable clone() {
		SymbolTable copiedSymbolTable = new SymbolTable(getId(),
				getTable_type());
		copiedSymbolTable.setChildrenTables(getChildrenTables());
		copiedSymbolTable.setEntries(getEntries());
		copiedSymbolTable.setParentSymbolTable(getParentSymbolTable());

		if (isLoop()) {
			copiedSymbolTable.setLoop();
		}

		return copiedSymbolTable;
	}

	public void sortEntries() {
		List<Symbol> sortedSymbolList = new LinkedList<Symbol>(
				this.entries.values());

		Collections.sort(sortedSymbolList, new Symbol.SymbolComperator());

		Map<String, Symbol> sortedEntries = new LinkedHashMap<String, Symbol>();

		for (Symbol symbol : sortedSymbolList) {
			sortedEntries.put(symbol.getIdName(), symbol);
		}

		this.setEntries(sortedEntries);
	}

	/**
	 * handles statement block string representation
	 */
	public String getStatemtentBlockRep(SymbolTable stmtBlockTable,
			boolean AsChild) {
		StringBuilder stmtBlockStr = new StringBuilder();
		SymbolTable parent = stmtBlockTable.getParentSymbolTable();

		if (AsChild) {
			stmtBlockStr.append(stmtBlockTable.getId().toString());

		} else {
			stmtBlockStr.append(stmtBlockTable.getTable_type().toString());

			stmtBlockStr.append(" ( " + "located");
		}

		while (parent != null) {
			if (parent.isMethodTable()) {
				stmtBlockStr.append(" in " + parent.getId());
				break;
			}
			stmtBlockStr.append(" in " + parent.getId());
			parent = parent.getParentSymbolTable();
		}

		if (!AsChild) {
			stmtBlockStr.append(" )");
		}
		return stmtBlockStr.toString();
	}

	@Override
	public String toString() {

		StringBuilder resStr = new StringBuilder();

		if (this.isBlockTable()) {
			resStr.append(getStatemtentBlockRep(this, false));
		} else {
			resStr.append(this.table_type.toString() + ": "
					+ this.id.toString());
		}

		if (this.entries != null && this.entries.values() != null) {
			for (Symbol symbol : this.entries.values())
				resStr.append("\n    " + symbol.toString());
		}

		if (this.childrenTables != null && this.childrenTables.size() > 0) {
			resStr.append("\nChildren tables: ");
			boolean first = true;
			for (SymbolTable childSymTable : this.childrenTables) {
				resStr.append(((!first) ? ", " : "")
						+ ((!childSymTable.isBlockTable()) ? childSymTable.id
								: getStatemtentBlockRep(childSymTable, true)));

				first = false;
			}

			resStr.append("\n\n");
			for (SymbolTable childSymTable : this.childrenTables) {
				resStr.append(childSymTable.toString());

			}

		}

		else
			resStr.append("\n\n");

		return resStr.toString();

	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------ lookup functions ----------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * @param idName
	 *            symbol name
	 * @return the symbol if found in current table or null otherwise
	 * 
	 */
	@Override
	public Symbol localLookup(String idName) {

		return entries.get(idName);
	}

	/**
	 * executes lookup on a symbol name in symbol tables
	 * 
	 * @return the symbol if found in scope or in any other parent symbol table,
	 *         or null otherwise
	 */
	@Override
	public Symbol lookup(String idName, ASTNode idNode) {

		Symbol local_symbol = null;
		SymbolTable currentSymbolTable = this;

		while (currentSymbolTable != null) {
			local_symbol = currentSymbolTable.localLookup(idName);
			/*
			 * return the local symbol in case it is found and is used after it
			 * is declared in the local scope
			 */
			if (local_symbol != null
					&& (local_symbol.isMethod() || local_symbol.isClass())) {
				return local_symbol;
			}

			if (local_symbol != null
					&& (idNode.getLine() >= local_symbol.getLine())) {

				return local_symbol;
			}

			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}

		return null;
	}

	public Symbol lookup(String symbolName) {
		Symbol local_symbol = null;
		SymbolTable currentSymbolTable = this;

		while (currentSymbolTable != null) {
			local_symbol = currentSymbolTable.localLookup(symbolName);
			/*
			 * return the local symbol in case it is found and is used after it
			 * is declared in the local scope
			 */
			if (local_symbol != null) {
				return local_symbol;
			}

			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}

		return null;
	}

	/**
	 * @return method symbol of an encapsulated block symbol table
	 */
	@Override
	public Symbol getMethodParent() {

		SymbolTable currentSymbolTable = this;

		while (currentSymbolTable != null) {
			if (currentSymbolTable.isMethodTable()) {
				return currentSymbolTable.lookup(currentSymbolTable.getId());
			}

			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}

		return null;
	}

	/**
	 * @return class symbol of an encapsulated symbol table
	 */
	public Symbol getClassParent() {

		SymbolTable currentSymbolTable = this;

		while (currentSymbolTable != null) {
			if (currentSymbolTable.isClassTable()) {
				return currentSymbolTable.lookup(currentSymbolTable.getId());
			}

			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}

		return null;
	}

	/**
	 * sets root symbol table
	 */
	public static void setRoot(SymbolTable rootSymbolTable) {
		root = rootSymbolTable;
	}

	/**
	 * @return root symbol table
	 */
	public static SymbolTable getRoot() {
		return root;
	}
}
