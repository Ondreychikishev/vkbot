package com.testtask.vk.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import com.testtask.vk.VkClient
import com.testtask.vk.VkMessageProcessor
import com.testtask.vk.configuration.VKConfiguration
import com.testtask.vk.dto.CallbackMessage
import com.testtask.vk.dto.MessageType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class VkController {

    @Autowired
    lateinit var vkConfiguration: VKConfiguration

    @Autowired
    val vkMessageProcessor: VkMessageProcessor = VkMessageProcessor();

    @Autowired
    val vkClient: VkClient = VkClient()

    private val LOGGER: Logger = LoggerFactory.getLogger(VkController::class.java)

    private val VK_API_ENDPOINT: String = "https://api.vk.com/method/"

    @RequestMapping(value = arrayOf("/"), method = arrayOf(RequestMethod.POST))
    fun event(@RequestBody raw: ObjectNode): ResponseEntity<Any> {
        LOGGER.info("Incoming message from VK: $raw");

        val message: CallbackMessage<Any>? = vkMessageProcessor.parseMessage(raw)

        LOGGER.info("MessageType: ${message?.type}");

        var code: String? = null

        if (message?.type?.equals(MessageType.CONFIRMATION) == true) {
            val groupId: Int? = vkConfiguration.getGroupId()
            code = groupId?.let {
                vkClient.getCallbackConfirmationCode(VK_API_ENDPOINT, vkConfiguration.token, vkConfiguration.version,
                    it
                )
            }
        }

        var response: ResponseEntity<Any> =  when (message?.type) {
            MessageType.CONFIRMATION ->
                ResponseEntity.ok(code)
            MessageType.MESSAGE_NEW ->
                vkMessageProcessor.sendMessage(VK_API_ENDPOINT, vkConfiguration.token, vkConfiguration.version, message.vkObject.vkMessageObject.user_id, message.vkObject.vkMessageObject.text)
            else -> ResponseEntity.ok("ok")
        }

        return response
    }
}