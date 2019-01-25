/**
 * 
 */
package com.ideafactory.dao;

import java.util.List;
import java.util.Optional;

import com.ideafactory.dao.interfaces.Dao;

/**
 * @author jean-
 *
 */
public class JpaUserDao implements Dao<User>{
	private EntityManager entityManager;
	
	@Override
	public Optional<User> get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(User t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(User t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(User t) {
		// TODO Auto-generated method stub
		
	}

}
