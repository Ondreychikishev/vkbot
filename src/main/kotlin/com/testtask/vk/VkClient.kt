package com.testtask.vk


import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testtask.vk.dto.confirmation.VkCallbackConfirmationResponse
import com.testtask.vk.dto.confirmation.VkGetServerCodeResponse
import com.testtask.vk.dto.server.VkAddServerResponse
import com.testtask.vk.dto.server.VkGetGroupIdResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class VkClient {

    private val gson: Gson = Gson()

    private val TYPE = object : TypeToken<VkCallbackConfirmationResponse>() {}.type

    @Autowired
    private lateinit var restTemplate: RestTemplate

    private val LOGGER: Logger = LoggerFactory.getLogger(VkClient::class.java)

    fun getCallbackConfirmationCode(apiEndpoint: String, accessToken: String, version: String, group_id: Int): String? {
        val url: String = "https://api.vk.com/method/groups.getCallbackConfirmationCode?group_id=$group_id&access_token=$accessToken&v=$version"

        val response: ObjectNode? = try {
            LOGGER.info("Get confirmation code for token: $accessToken")
            restTemplate.getForObject(url, ObjectNode::class.java)
        } catch (e: Exception) {
            LOGGER.info("Error get confirmation code for token: $accessToken")
            null
        }
        if (response != null) {
            val vkGetConfirmationCodeResponse: VkCallbackConfirmationResponse = gson.fromJson(response.toString(), TYPE)
            return vkGetConfirmationCodeResponse.getServerCodeResponse.code
        }
        return null
    }
}