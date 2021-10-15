package com.testtask.vk.dto.confirmation

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkCallbackConfirmationResponse constructor(

    @SerializedName("response")
    val getServerCodeResponse: VkGetServerCodeResponse
)