package com.testtask.vk

import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testtask.vk.dto.CallbackMessage
import com.testtask.vk.dto.VkCallbackConfirmationRequest
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
    private val TYPE_OF_CLASS_CONFIRAMTION = object : TypeToken<CallbackMessage<VkCallbackConfirmationRequest>>() {}.type
    private val TYPE_OF_CLASS_MESSAGE_NEW = object : TypeToken<CallbackMessage<VkNewMessageRequest>>() {}.type

    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun parseMessage(message: ObjectNode): CallbackMessage<Any>? {

        val type = message.path("type").asText()

        return when (type) {
            CALLBACK_EVENT_CONFIRMATION ->
                gson.fromJson(message.toString(), TYPE_OF_CLASS_CONFIRAMTION)
            CALLBACK_EVENT_MESSAGE_NEW ->
                gson.fromJson(message.toString(), TYPE_OF_CLASS_MESSAGE_NEW)
            else -> null
        }
    }

    fun sendMessage(userId: Int, text: String, token: String, version: String): ResponseEntity<Any> {

        val randomId: Int = (0..Int.MAX_VALUE).random()
        val message: String = "Вы сказали: $text"
        val url: String = "https://api.vk.com/method/messages.send?user_id={userId}&message={message}&random_id={randomId}&access_token={token}&v={version}"

        try {
            restTemplate.getForEntity(url, String.javaClass, userId, message, randomId, token, version)
            LOGGER.info("Send message from vk with parameters userId: $userId, message: $message")
        } catch (e: Exception) {
            LOGGER.info("Error send vk message: $e")
        }

        return ResponseEntity.ok("ok")
    }
}