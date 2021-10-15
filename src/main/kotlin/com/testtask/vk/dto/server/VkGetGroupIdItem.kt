package com.testtask.vk.dto.server

import com.google.gson.annotations.SerializedName

data class VkGetGroupIdItem constructor(

    @SerializedName("id")
    val id: Int
)
