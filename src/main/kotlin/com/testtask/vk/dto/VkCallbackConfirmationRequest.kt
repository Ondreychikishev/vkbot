package com.testtask.vk.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkCallbackConfirmationRequest (

    @SerializedName("type")
    val type: String,

    @SerializedName("group_id")
    val group_id: Int
)