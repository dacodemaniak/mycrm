/**
 * 
 */
package com.crm.models;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author jean-
 *
 */
public interface ModelInterface<T> {
	public T find(int id);
	
	public HashMap<Integer, T> find();
	
	public T persist() throws IllegalArgumentException, IllegalAccessException, SQLException;
	
	public boolean remove();
}
