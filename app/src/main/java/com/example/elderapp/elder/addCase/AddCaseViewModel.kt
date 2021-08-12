package com.example.elderapp.elder.addCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddCaseViewModel : ViewModel() {

    var place = MutableLiveData<Int>()
    var date = MutableLiveData<String>()
    var invite = MutableLiveData<String>()
    var public = MutableLiveData<String?>()

    fun setPlace(place: Int) {
        this.place.value = place
    }

    fun setDate(date: String) {
        this.date.value = date
    }

    fun setInvite(invite: String) {
        this.invite.value = invite
    }

    fun setPublic(public:String?) {
        this.public.value = public
    }
}