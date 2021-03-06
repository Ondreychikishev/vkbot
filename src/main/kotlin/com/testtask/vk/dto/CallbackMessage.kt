package com.testtask.vk.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import com.testtask.vk.dto.message.VkNewMessageObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class CallbackMessage<out T> constructor(

    @SerializedName("type")
    val type: MessageType,

    @SerializedName("object")
    val vkNewMessageObject: VkNewMessageObject
)