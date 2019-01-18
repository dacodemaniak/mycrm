/**
 * 
 */
package com.crm.models;

import java.util.HashMap;

/**
 * @author jean-
 *
 */
public interface ModelInterface<T> {
	public T find(int id);
	
	public HashMap<Integer, T> find();
	
	public T persist();
	
	public boolean remove();
}
