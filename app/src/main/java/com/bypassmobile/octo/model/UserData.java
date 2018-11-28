package com.bypassmobile.octo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Serializable {



	private final String BYPASS_MOBILE_ID = "bypasslane";		// Bypass Mobile ID


	private String query;
	private User user;
	private List<User> followers;
	private boolean upToDate;



	public UserData () {
		User bypassMobileUser = new User(BYPASS_MOBILE_ID, null);
		bypassMobileUser.setBypass(true);
		this.user = bypassMobileUser;
		this.followers = new ArrayList<>();
	}



	public UserData (final User user) {
		this.user = user;
		this.followers = new ArrayList<>();
	}



	public String getQuery () {
		return query;
	}



	public void setQuery (final String query) {
		this.query = query;
	}



	public User getUser () {
		return user;
	}



	public void setUser (final User user) {
		this.user = user;
	}



	public List<User> getFollowers () {
		return followers;
	}



	public void setFollowers (final List<User> followers) {
		this.followers = followers;
		this.upToDate = true;
	}



	public boolean isUpToDate () {
		return upToDate;
	}
}
