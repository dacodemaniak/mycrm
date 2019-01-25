package com.ideafactory.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ideafactory.dao.interfaces.Dao;

public class UserDao implements Dao<User> {
	
	private List<User> users = new ArrayList<>();
	
	public UserDao() {
		users.add(new User("JL", "jl@test.truc"));
		users.add(new User("Toto", "toto@bidule.fr"));
	}
	
	@Override
	public Optional<User> get(int id) {
		return Optional.ofNullable(users.get((int) id));
	}

	@Override
	public List<User> getAll() {
		return users;
	}

	@Override
	public void save(User user) {
		users.add(user);
	}

	@Override
	public void update(User user, String[] params) {
		user.name(Objects.requireNonNull(params[0], "Le nom ne peut pas être vide"));
		user.name(Objects.requireNonNull(params[1], "L'email ne peut pas être vide"));
		
		users.add(user);
	}

	@Override
	public void delete(User user) {
		users.remove(user);

	}

}
