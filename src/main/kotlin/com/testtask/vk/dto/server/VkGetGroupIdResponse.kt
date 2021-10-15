package com.testtask.vk.dto.server

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
class VkGetGroupIdResponse(

    @SerializedName("response")
    val vkGetGroupIdItem: List<VkGetGroupIdItem>
)
