package com.moutamid.whatzboost.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moutamid.whatzboost.room.LastMessage;
import com.moutamid.whatzboost.services.Repository;

import java.util.List;

public class UnseenViewModel  extends ViewModel {
    private MutableLiveData<String> _text = new MutableLiveData<>();

    public UnseenViewModel() {
        _text.setValue("This is Unseen Fragment");
    }

    public LiveData<String> getText() {
        return _text;
    }

    public LiveData<List<LastMessage>> getAllLastMessages() {
        return Repository.INSTANCE.getAllLastMessages();
    }

//    public LiveData<List<BusinessLastMessage>> getAllBusinessLastMessages() {
//        return Repository.INSTANCE.getAllBusinessLastMessages();
//    }
}
