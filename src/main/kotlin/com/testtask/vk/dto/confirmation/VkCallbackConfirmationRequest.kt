package com.testtask.vk.dto.confirmation

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkCallbackConfirmationRequest constructor(

    @SerializedName("type")
    val type: String,

    @SerializedName("group_id")
    val group_id: Int
)