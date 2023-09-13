package com.example.registrationapp.ui.GetCurrentLocation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GetLocationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GetLocationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is GetLocationViewModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}