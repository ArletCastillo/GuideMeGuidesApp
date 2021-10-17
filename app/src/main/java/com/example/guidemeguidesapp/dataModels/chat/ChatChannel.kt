package com.example.guidemeguidesapp.dataModels.chat

data class ChatChannel(
    var sentBy_Id: String = "", //User who sends a message
    var sentTo_Id: String = "", //User who receives a massage
)
