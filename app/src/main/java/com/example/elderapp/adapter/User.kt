package com.example.elderapp.adapter

open class User(id: Int, name: String, phone: String){
    var id: Int = id
    var name: String = name
    var phone: String = phone
    var identity: Number = 0
    var sex: String = "F" // 'F', 'M', 'N'
    open var headshot: String = "default_m"

    fun setupInvitedVolunteer(headshot:String) :User{
        this.identity = 1
        this.headshot = headshot
        return this
    }

    fun setHeadshot(headshot:String) :User {
        this.headshot = headshot
        return this
    }

}
