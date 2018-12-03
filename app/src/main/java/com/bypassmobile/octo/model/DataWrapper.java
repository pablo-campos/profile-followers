package com.bypassmobile.octo.model;

import java.util.ArrayList;
import java.util.List;

public class DataWrapper  <T> {


	public enum Status {
		NONE,
		LOADING,
		ERROR
	}


	private Status status;
	private List<T> dataList;



	public DataWrapper () {
		status = Status.NONE;
		dataList = new ArrayList<>();
	}



	public Status getStatus () {
		return status;
	}



	public void setStatus (final Status status) {
		this.status = status;
	}



	public List<T> getDataList () {
		return dataList;
	}



	public void setData (final List<T> dataList) {
		this.dataList = dataList;
	}
}
