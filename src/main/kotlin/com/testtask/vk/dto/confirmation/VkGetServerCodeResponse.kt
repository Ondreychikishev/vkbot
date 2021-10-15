package com.testtask.vk.dto.confirmation

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkGetServerCodeResponse constructor(

    @SerializedName("code")
    val code: String,
)