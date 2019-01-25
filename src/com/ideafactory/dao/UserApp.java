/**
 * 
 */
package com.ideafactory.dao;

import java.util.Optional;

import com.ideafactory.dao.interfaces.Dao;

/**
 * @author jean-
 *
 */
public class UserApp {
	private static Dao userDao;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		userDao = new UserDao();
		
		User user1 = getUser(0);
		System.out.println("Utilisateur 1" + user1.name());
		
		// Try to update
		userDao.update(user1, new String[] {"Jean-Luc", "jla.idea@gmail.com"});

		User user2 = getUser(1);
		userDao.delete(user2);
		userDao.save(new User("Théo", "theo.plakar@laporte.net"));
		
		// Try to list
		userDao.getAll().forEach(user -> System.out.println(((User) user).name())); 
	}

	private static User getUser(int i) {
		Optional<User> user = userDao.get(i);
		
		return user.orElseGet( () -> new User("anonymous", "anonymous@anonymous"));
	}

}
