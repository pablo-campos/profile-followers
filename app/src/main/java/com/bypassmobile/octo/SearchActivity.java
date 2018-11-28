package com.bypassmobile.octo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bypassmobile.octo.Utils.Utils;
import com.bypassmobile.octo.adapter.UserAdapter;
import com.bypassmobile.octo.adapter.UserClickListener;
import com.bypassmobile.octo.model.UserData;
import com.bypassmobile.octo.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends BaseActivity {



	public static final String EXTRA_USER_DATA = "EXTRA.USER.DATA";		// User selected

	private UserData userData;

	private SearchView searchView;
	private RecyclerView userList;
	private UserAdapter userAdapter;




	@Override
	public boolean onCreateOptionsMenu( Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		// Initialize the search functionality on the activity's toolbar
		final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) myActionMenuItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				userAdapter.getFilter().filter(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				userAdapter.getFilter().filter(query);
				return false;
			}
		});

		// In case of orientation changes, etc. we need to restore search query
		if (userData.getQuery() != null && !userData.getQuery().isEmpty()){
			MenuItemCompat.expandActionView(myActionMenuItem);		// We need to expand the search view before
			searchView.setQuery(userData.getQuery(), true);
		}

		return true;
	}



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Different scenarios:
		if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_USER_DATA)){		// Activity was destroyed, let's get the data we saved
			userData = (UserData) savedInstanceState.getSerializable(EXTRA_USER_DATA);
		} else if (getIntent().getSerializableExtra(EXTRA_USER_DATA) != null) {		// We were requested to look at a specific user's details
			userData = (UserData) getIntent().getSerializableExtra(EXTRA_USER_DATA);
		} else {		// First time we opened the app
			userData = new UserData();
		}

		// Initialize grid view
		final UserClickListener userClickListener = new UserClickListener() {
			@Override
			public void onClick (final User user) {

				// Open a new screen with the selected user's following information
				Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
				UserData selectedUser = new UserData(user);
				intent.putExtra(EXTRA_USER_DATA, selectedUser);
				startActivity(intent);
			}
		};

		userAdapter = new UserAdapter(userClickListener, userData.getFollowers());

		userList = findViewById(R.id.user_list);
		userList.setHasFixedSize(true);
		userList.setLayoutManager(new LinearLayoutManager(this));
		userList.setAdapter(userAdapter);

		// Are we seeing an specific user's followers?
		if (!userData.getUser().isBypass()) {

			// Initialize user's title
			TextView usersTitle = findViewById(R.id.selected_user);
			usersTitle.setVisibility(View.VISIBLE);
			usersTitle.setText(String.format(getResources().getString(R.string.users_title), userData.getUser().getName()));
			usersTitle.setTextColor(getResources().getColor(R.color.user_title_text_color));
			usersTitle.setBackgroundColor(getResources().getColor(R.color.user_title_background_color));

			// Load/Update user's following list
			if (!userData.isUpToDate()){
				loadUserDetails(userData.getUser());
			}

		} else {

			// Load/Update company members
			if (!userData.isUpToDate()){
				loadCompanyUsers(userData.getUser().getName());
			}
		}
	}



	@Override
	protected void onSaveInstanceState (final Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current activity's state
		userData.setQuery(searchView.getQuery().toString());
		outState.putSerializable(EXTRA_USER_DATA, userData);
	}



	private void loadCompanyUsers (final String companyId){

		if (Utils.checkInternet(SearchActivity.this)) {        // Check internet connection and then perform query
			getEndpoint().getOrganizationMembers(companyId, new Callback<List<User>>() {
				@Override
				public void success(List<User> users, Response response) {

					// Update current data and adapter
					updateData(users);
				}

				@Override
				public void failure(RetrofitError error) {

					// Display an error
					Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
				}
			});
		}
	}



	private void loadUserDetails (final User user){

		if (Utils.checkInternet(SearchActivity.this)) {        // Check internet connection and then perform query
			getEndpoint().getUserFollowing(user.getName(), new Callback<List<User>>() {
				@Override
				public void success(List<User> users, Response response) {

					// Update current data and adapter
					updateData(users);
				}

				@Override
				public void failure(RetrofitError error) {

					// Display an error
					Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
				}
			});
		}
	}



	private void updateData(final List<User> users){

		// No results found
		if (users.isEmpty()){
			Snackbar.make(findViewById(android.R.id.content), R.string.no_results_found, BaseTransientBottomBar.LENGTH_SHORT);
		}

		userData.setFollowers(users);
		userAdapter.updateData(users);
	}

}
