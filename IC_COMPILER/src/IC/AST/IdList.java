/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.AST;

import java.util.ArrayList;
import java.util.List;

/**
 * IdList represents and ID list (separated by ',' ) as an multiple definition
 * for a single type (for example : int a , b , c;) This is not a part of the
 * AST by itself (IdList is NOT an ASTNode!)
 */
public class IdList {

	List<String> ids;

	/**
	 * construct empty id list
	 * 
	 */

	public IdList() {
		this.ids = new ArrayList<String>();
	}

	/**
	 * construct IdList from a single ID
	 * 
	 */
	public IdList(String id) {
		this.ids = new ArrayList<String>();
		addID(id);
	}

	/**
	 * construct IdList from an ID list
	 * 
	 */
	public IdList(List<String> idList) {
		this.ids = new ArrayList<String>();
		addIDs(idList);
	}

	public void addID(String id) {
		this.ids.add(id);
	}

	public void addIDs(List<String> idList) {
		this.ids.addAll(idList);
	}

	public List<String> getIds() {
		return this.ids;
	}

	/**
	 * Constructs a field list from type and a list of ids
	 * 
	 * @param type
	 *            the type of all the fields which will be constructed from the
	 *            ID list
	 */
	public List<Field> ConstructFieldsFromIDs(Type type) {
		ArrayList<Field> fields = new ArrayList<Field>();
		Field f = null;
		for (String identifier : this.ids) {
			f = new Field(type, identifier);
			fields.add(f);
		}

		return fields;
	}
}
