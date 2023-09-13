package com.example.registrationapp.ui.setCurrentLocation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SetLocationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SetLocationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SetLocationViewModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
