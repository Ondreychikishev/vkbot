package com.testtask.vk.dto.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkNewMessageRequest (

    @SerializedName("type")
    val type: String,

    @SerializedName("object")
    val vkNewMessageObject: VkNewMessageObject

)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkNewMessageObject (

    @SerializedName("message")
    val vkNewMessageObjectMessage: VkNewMessageObjectMessage
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkNewMessageObjectMessage (

    @SerializedName("from_id")
    val user_id: Int,

    @SerializedName("text")
    val text: String
)
