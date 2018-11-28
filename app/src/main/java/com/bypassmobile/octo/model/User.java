package com.bypassmobile.octo.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

	private boolean isBypass;

	@SerializedName("login")
	private final String name;

	@SerializedName("avatar_url")
	private final String profileURL;



	public User (String name, String profileURL) {
		this.name = name;
		this.profileURL = profileURL;
	}



	public String getName () {
		return name;
	}



	public String getProfileURL () {
		return profileURL;
	}



	public boolean isBypass () {
		return isBypass;
	}



	public void setBypass (final boolean bypass) {
		isBypass = bypass;
	}
}
