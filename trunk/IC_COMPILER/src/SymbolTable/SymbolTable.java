package SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import IC.AST.ASTNode;

/**
 * TODO add info
 */

public class SymbolTable {

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
	 * TODO add info
	 */
	private Map<String, Symbol> entries;

	/**
	 * TODO add info
	 */
	private String id;

	/**
	 * TODO add info
	 */
	private SymbolTable parentSymbolTable;

	/**
	 * TODO add info
	 */
	private TableType table_type;

	/**
	 * TODO add info
	 */
	private List<SymbolTable> childrenTables;

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- Constructors --------------------------
	 * -------------------------------------------------------------------
	 */

	public SymbolTable(String id) {
		this.setId(id);
		setEntries(new HashMap<String, Symbol>());
		setChildrenTables(new ArrayList<SymbolTable>());

	}

	public SymbolTable(String id, TableType table_type) {
		this.setId(id);
		this.setTable_type(table_type);
		setEntries(new HashMap<String, Symbol>());
		setChildrenTables(new ArrayList<SymbolTable>());
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

	private static StringBuilder tableStr;

	// temporary definition - just for debug
	private static void getTableRepresentation(SymbolTable symTable) {

		tableStr.append("table id : " + symTable.getId() + "\n");

		tableStr.append("table type : " + symTable.getTable_type().toString()
				+ "\n");

		Iterator<Entry<String, Symbol>> it = symTable.getEntries().entrySet()
				.iterator();

		if (symTable.hasSymbols()) {
			tableStr.append(" ---- symbols ---- \n");
		}

		while (it.hasNext()) {

			// get next entry
			Map.Entry<String, Symbol> entry = it.next();

			// get symbol tag name
			tableStr.append("symbol name: " + entry.getKey() + "\n");

			// get symbol content
			tableStr.append("symbol value: \n" + entry.getValue().toString()
					+ "\n");
		}

		if (symTable.hasChildTables()) {

			tableStr.append(" ---- children tables ---- \n");
			StringBuilder childrenSb = new StringBuilder();
			for (SymbolTable childTable : symTable.getChildrenTables()) {
				childrenSb.append(childTable.getId());
				childrenSb.append(",");
			}

			tableStr.append("children names: "
					+ childrenSb.toString().substring(0,
							childrenSb.toString().length() - 1) + "\n");

			for (SymbolTable childTable : symTable.getChildrenTables()) {
				tableStr.append("child table : \n");
				getTableRepresentation(childTable);
			}
		}

	}

	// temporary definition - just for debug
	@Override
	public String toString() {

		tableStr = new StringBuilder();
		getTableRepresentation(this);
		return tableStr.toString();
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
	public Symbol localLookup(String idName) {
		return entries.get(idName);
	}

	/**
	 * lookups symbol in parent
	 * 
	 * @return the symbol if found, or null in case not found or parent is root
	 */
	private Symbol lookupParent(String idName, ASTNode idNode) {

		// check if parent is root
		if (isRoot())
			return null;

		return parentSymbolTable.lookup(idName, idNode);
	}

	/**
	 * executes lookup on a symbol name in symbol tables
	 * 
	 * @return the symbol if found in scope or in any other parent symbol table,
	 *         or null otherwise
	 */
	public Symbol lookup(String idName, ASTNode idNode) {

		// lookup id name in local scope
		Symbol local_symbol = localLookup(idName);

		if (local_symbol != null) {

			// in case id name found in local table

			if (idNode.getLine() >= local_symbol.getIdNode().getLine()
					|| local_symbol.isMethod() || local_symbol.isField()) {
				return local_symbol;
			}

		}

		return lookupParent(idName, idNode);

	}

}
