package com.testtask.vk.dto

import com.google.gson.annotations.SerializedName

enum class MessageType(val type: String) {

    @SerializedName("confirmation")
    CONFIRMATION("confirmation"),

    @SerializedName("message_new")
    MESSAGE_NEW("message_new")
}