package com.example.elderapp.adapter

class Event (var id:Int,var title:String, var location:String, val content:String,
             var holder:String, var holderUid:Int, var date:String, var attendee: MutableList<Int>, var status:Int)