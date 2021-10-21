package com.testtask.vk.dto.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkGetCallbackServersResponse(

    @SerializedName("response")
    val vkGetCallbackServersResponseObject: VkGetCallbackServersResponseObject
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkGetCallbackServersResponseObject(

    @SerializedName("items")
    val items: List<VkGetCallbackServersResponseItem>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkGetCallbackServersResponseItem(

    @SerializedName("id")
    val id: Int
)
