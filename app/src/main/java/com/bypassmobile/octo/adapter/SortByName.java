package com.bypassmobile.octo.adapter;

import com.bypassmobile.octo.model.User;

import java.util.Comparator;

public class SortByName implements Comparator<User> {

	@Override
	public int compare (final User user1, final User user2) {
		return user1.getName().compareToIgnoreCase(user2.getName());
	}
}
