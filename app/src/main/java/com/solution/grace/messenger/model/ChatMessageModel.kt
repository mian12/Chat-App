package com.solution.grace.messenger.model

class ChatMessageModel(val id:String,val fromId:String, val text:String, val timeStamp:Long,val toId:String)
{
    constructor():this("","","",-1,"")
}