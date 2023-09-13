package com.example.registrationapp.ui.recordDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecordDeatilsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RecordDeatilsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is RecordDetails fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}