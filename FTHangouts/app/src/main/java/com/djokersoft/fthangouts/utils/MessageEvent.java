package com.djokersoft.fthangouts.utils;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MessageEvent {
    private static final MutableLiveData<Long> newMessageEvent = new MutableLiveData<>();


    public static void notifyNewMessage(long contactId) {
        newMessageEvent.postValue(contactId);
    }


    public static LiveData<Long> getNewMessageEvent() {
        return newMessageEvent;
    }
}