package com.example.elderapp.adapter

class User(id: Int, name: String, phone: String){
    var id: Int = id
    var name: String = name
    var phone: String = phone
    var identity: Number = 0
    var sex: String = "F" // 'F', 'M', 'N'
    var headshot: String = "default_m"

    fun SetupInvitedVolunteer(headshot:String) :User{
        this.identity = 1
        this.headshot = headshot
        return this
    }

}
