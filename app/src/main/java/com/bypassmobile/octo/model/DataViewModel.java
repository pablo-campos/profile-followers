package com.bypassmobile.octo.model;


import com.bypassmobile.octo.SearchActivity;
import com.bypassmobile.octo.Utils.Utils;
import com.bypassmobile.octo.rest.GithubEndpoint;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DataViewModel extends ViewModel {



	private final String BYPASS_MOBILE_ID = "bypasslane";		// Bypass Mobile ID



	private DataWrapper<User> dataWrapper;
	private MutableLiveData<DataWrapper<User>> liveData;

	private GithubEndpoint endpoint;



	public MutableLiveData<DataWrapper<User>> getLiveData (final User user) {

		RestAdapter adapter = new RestAdapter.Builder()
				.setServer(GithubEndpoint.SERVER)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		endpoint = adapter.create(GithubEndpoint.class);

		if (liveData == null){
			dataWrapper = new DataWrapper<>();
			liveData = new MutableLiveData<>();

			if (user != null){
				loadUserDetails(user);
			} else {
				loadCompanyUsers();
			}
		}

		return liveData;
	}



	private void loadCompanyUsers (){

		// Let UI know we need to display progress bar
		dataWrapper.setStatus(DataWrapper.Status.LOADING);
		liveData.postValue(dataWrapper);

		endpoint.getOrganizationMembers(BYPASS_MOBILE_ID, new Callback<List<User>>() {
			@Override
			public void success(List<User> users, Response response) {

				// Stop refresh status and update data
				dataWrapper.setStatus(DataWrapper.Status.NONE);
				dataWrapper.setData(users);
				liveData.postValue(dataWrapper);
			}

			@Override
			public void failure(RetrofitError error) {

				// Stop refresh status
				dataWrapper.setStatus(DataWrapper.Status.ERROR);
				dataWrapper.setData(new ArrayList<User>());
				liveData.postValue(dataWrapper);
			}
		});
	}



	private void loadUserDetails (final User user){

		// Let UI know we need to display progress bar
		dataWrapper.setStatus(DataWrapper.Status.LOADING);
		liveData.postValue(dataWrapper);

		endpoint.getUserFollowing(user.getName(), new Callback<List<User>>() {
			@Override
			public void success(List<User> users, Response response) {

				// Stop refresh status and update data
				dataWrapper.setStatus(DataWrapper.Status.NONE);
				dataWrapper.setData(users);
				liveData.postValue(dataWrapper);
			}

			@Override
			public void failure(RetrofitError error) {

				// Stop refresh status
				dataWrapper.setStatus(DataWrapper.Status.ERROR);
				dataWrapper.setData(new ArrayList<User>());
				liveData.postValue(dataWrapper);
			}
		});
	}

}
