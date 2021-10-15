package com.testtask.vk

import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testtask.vk.controller.VkController
import com.testtask.vk.dto.CallbackMessage
import com.testtask.vk.dto.confirmation.VkCallbackConfirmationRequest
import com.testtask.vk.dto.message.VkNewMessageRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class VkMessageProcessor {

    private val LOGGER: Logger = LoggerFactory.getLogger(VkMessageProcessor::class.java)
    private val gson: Gson = Gson()
    private val CALLBACK_EVENT_MESSAGE_NEW = "message_new"
    private val CALLBACK_EVENT_CONFIRMATION = "confirmation"
    private val TYPE_OF_CLASS = object : TypeToken<CallbackMessage<VkCallbackConfirmationRequest>>() {}.type
    private val TYPE_OF_CLASS_MESSAGE = object : TypeToken<CallbackMessage<VkNewMessageRequest>>() {}.type

    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun parseMessage(message: ObjectNode): CallbackMessage<Any>? {

        val type = message.path("type").asText()

        return when (type) {
            CALLBACK_EVENT_CONFIRMATION ->
                gson.fromJson(message.toString(), TYPE_OF_CLASS)
            CALLBACK_EVENT_MESSAGE_NEW ->
                gson.fromJson(message.toString(), TYPE_OF_CLASS_MESSAGE)
            else -> null
        }
    }

    fun sendMessage(apiEndpoint: String, accessToken: String, version: String, user_id: Int, text: String): ResponseEntity<Any> {

        val url: String = "${apiEndpoint}messages.send?user_id=$user_id&message=вы сказали: $text&access_token=$accessToken&v=$version"
        try {
            restTemplate.getForEntity(url, String.javaClass)
        } catch (e: Exception) {
            LOGGER.info("Error send vk message: $e")
        }

        return ResponseEntity.ok("ok")
    }
}