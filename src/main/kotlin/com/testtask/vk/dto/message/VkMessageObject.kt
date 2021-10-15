package com.testtask.vk.dto.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkMessageObject constructor(

    @SerializedName("from_id")
    val user_id: Int,

    @SerializedName("text")
    val text: String
)
