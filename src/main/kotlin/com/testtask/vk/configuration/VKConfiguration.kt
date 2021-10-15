package com.testtask.vk.configuration

import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testtask.vk.VkClient
import com.testtask.vk.dto.CallbackMessage
import com.testtask.vk.dto.message.VkNewMessageRequest
import com.testtask.vk.dto.server.VkAddServerResponse
import com.testtask.vk.dto.server.VkGetGroupIdResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


@Component
class VKConfiguration {

    private val LOGGER: Logger = LoggerFactory.getLogger(VKConfiguration::class.java)

    private val gson: Gson = Gson()

    @Autowired
    val vkClient: VkClient = VkClient()

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Value("\${vk.token}")
    lateinit var token: String

    @Value("\${vk.version}")
    lateinit var version: String

    @Value("\${vk.webhook}")
    lateinit var webhook: String

    private val TYPE = object : TypeToken<VkGetGroupIdResponse>() {}.type

    @EventListener(ApplicationReadyEvent::class)
    fun getGroupId(): Int? {

        LOGGER.info("Get vk groupId for token: $token")
        val url: String = "https://api.vk.com/method/groups.getById?&access_token=$token&v=5.89"

        val response: ObjectNode? = try {
            restTemplate.getForObject(url, ObjectNode::class.java)
        } catch (e: Exception) {
            LOGGER.info("Error get groupId for token: $token")
            null
        }
        if (response != null) {
            val vkGetIdResponse: VkGetGroupIdResponse = gson.fromJson(response.toString(), TYPE)
            return vkGetIdResponse.vkGetGroupIdItem.get(0).id
        }
        return null
    }

    @EventListener(ApplicationReadyEvent::class)
    fun addCallbackServer(): Unit {
        LOGGER.info("add new vk server with id: ${getGroupId()}")
        val url: String = "https://api.vk.com/method/groups.addCallbackServer?group_id=${getGroupId()}&url=$webhook&title=VkServer&access_token=$token&v=5.89"
        var response: VkAddServerResponse? =
            try {
                restTemplate.getForObject(url, HttpMethod.GET, VkAddServerResponse::class.java)
            } catch (e: Exception) {
                null
            }
    }
}