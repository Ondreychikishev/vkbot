package com.testtask.vk.dto.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkNewMessageRequest constructor(

    @SerializedName("type")
    val type: String,

    @SerializedName("object")
    val vkObject: VkObject

)
