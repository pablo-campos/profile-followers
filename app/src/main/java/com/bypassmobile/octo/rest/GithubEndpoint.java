package com.bypassmobile.octo.rest;


import com.bypassmobile.octo.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GithubEndpoint {

	String SERVER = "https://api.github.com";

	@GET("/users/{id}/following")
	void getUserFollowing (@Path("id") String user, Callback<List<User>> callback);

	@GET("/orgs/{id}/members")
	void getOrganizationMembers (@Path("id") String organization, Callback<List<User>> callback);
}
