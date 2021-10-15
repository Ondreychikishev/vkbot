package com.testtask.vk.dto.message

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkObject constructor(

    @SerializedName("message")
    val vkMessageObject: VkMessageObject
)
