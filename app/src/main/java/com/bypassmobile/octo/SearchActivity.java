package com.bypassmobile.octo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bypassmobile.octo.Utils.Utils;
import com.bypassmobile.octo.adapter.UserAdapter;
import com.bypassmobile.octo.adapter.UserClickListener;
import com.bypassmobile.octo.model.DataWrapper;
import com.bypassmobile.octo.model.User;
import com.bypassmobile.octo.model.DataViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {



	public static final String EXTRA_USER = "EXTRA.USER";		// User selected
	public static final String EXTRA_CURRENT_QUERY = "EXTRA.CURRENT_QUERY";		// User selected



	private String query;
	private UserAdapter userAdapter;
	private DataViewModel dataViewModel;

	private SearchView searchView;
	private RecyclerView userList;
	private ProgressBar progressBar;




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
		if (query != null && !query.isEmpty()){
			MenuItemCompat.expandActionView(myActionMenuItem);		// We need to expand the search view before
			searchView.setQuery(query, true);
		}

		return true;
	}



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Update query (in case there was one)
		if (savedInstanceState != null){
			query = savedInstanceState.getString(EXTRA_CURRENT_QUERY, "");
		}

		User user = null;
		if (getIntent().getSerializableExtra(EXTRA_USER) != null) {		// We were requested to look at a specific user's details
			user = (User) getIntent().getSerializableExtra(EXTRA_USER);
		}

		// Initialize grid view
		final UserClickListener userClickListener = new UserClickListener() {
			@Override
			public void onClick (final User user) {

				// Open a new screen with the selected user's following information
				Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
				intent.putExtra(EXTRA_USER, user);
				startActivity(intent);
			}
		};

		userAdapter = new UserAdapter(userClickListener);

		userList = findViewById(R.id.user_list);
		userList.setHasFixedSize(true);
		userList.setLayoutManager(new LinearLayoutManager(this));
		userList.setAdapter(userAdapter);

		progressBar = findViewById(R.id.progress_bar);

		// Initialize ViewModel
		dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

		// Attempt to load data if this device has internet
		if (Utils.checkInternet(this)){
			dataViewModel.getLiveData(user).observe(this, new Observer<DataWrapper<User>>() {
				@Override
				public void onChanged (@Nullable final DataWrapper<User> dataWrapper) {

					// Update adapter
					userAdapter.updateData(dataWrapper.getDataList());

					// Update status
					switch (dataWrapper.getStatus()){
						case NONE:
							progressBar.setVisibility(View.GONE);
							break;
						case LOADING:
							progressBar.setVisibility(View.VISIBLE);
							break;
						case ERROR:
							progressBar.setVisibility(View.GONE);
							Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, Snackbar.LENGTH_SHORT);
							break;
					}
				}
			});
		}

		// Are we seeing an specific user's followers?
		if (user != null) {

			// Initialize user's title
			TextView usersTitle = findViewById(R.id.selected_user);
			usersTitle.setVisibility(View.VISIBLE);
			usersTitle.setText(String.format(getResources().getString(R.string.users_title), user.getName()));
			usersTitle.setTextColor(getResources().getColor(R.color.user_title_text_color));
			usersTitle.setBackgroundColor(getResources().getColor(R.color.user_title_background_color));
		}
	}



	@Override
	protected void onSaveInstanceState (final Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current activity's state
		outState.putString(EXTRA_CURRENT_QUERY, searchView.getQuery().toString());
	}

}
