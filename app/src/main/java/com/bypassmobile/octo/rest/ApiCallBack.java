package com.bypassmobile.octo.rest;

import com.bypassmobile.octo.model.DataWrapper;
import com.bypassmobile.octo.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApiCallBack implements Callback<List<User>> {


	private final DataWrapper<User> dataWrapper;
	private final MutableLiveData<DataWrapper<User>> liveData;


	public ApiCallBack (final DataWrapper<User> dataWrapper, final MutableLiveData<DataWrapper<User>> liveData) {
		this.dataWrapper = dataWrapper;
		this.liveData = liveData;
	}


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
		dataWrapper.setData(new ArrayList<>());
		liveData.postValue(dataWrapper);
	}

}
