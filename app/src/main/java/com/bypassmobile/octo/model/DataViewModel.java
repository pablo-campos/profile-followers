package com.bypassmobile.octo.model;


import com.bypassmobile.octo.rest.ApiCallBack;
import com.bypassmobile.octo.rest.GithubEndpoint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit.RestAdapter;

public class DataViewModel extends ViewModel {



	private final String GOOGLE_ID = "google";		// Bypass Mobile ID



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
		endpoint.getOrganizationMembers(GOOGLE_ID, new ApiCallBack(dataWrapper, liveData));
	}



	private void loadUserDetails (final User user){

		// Let UI know we need to display progress bar
		dataWrapper.setStatus(DataWrapper.Status.LOADING);
		liveData.postValue(dataWrapper);
		endpoint.getUserFollowing(user.getName(), new ApiCallBack(dataWrapper, liveData));
	}

}
