/**
 * 
 */
package com.ideafactory.dao.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * @author jean-
 *
 */
public interface Dao<T> {
	Optional<T> get(int id);
	
	List<T> getAll();
	
	void save(T t);
	
	void update(T t, String[] params);
	
	void delete(T t);
}
