package com.testtask.vk.dto.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkAddServerResponse (

    @SerializedName("response")
    val getServerIdResponse: VkGetServerIdResponse
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkGetServerIdResponse(

    @SerializedName("server_id")
    val server_id: Int
)


