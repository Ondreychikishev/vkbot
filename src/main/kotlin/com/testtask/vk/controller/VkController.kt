package com.testtask.vk.controller

import com.fasterxml.jackson.databind.node.ObjectNode
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
    lateinit var vkMessageProcessor: VkMessageProcessor

    private val LOGGER: Logger = LoggerFactory.getLogger(VkController::class.java)

    @RequestMapping(value = arrayOf("/"), method = arrayOf(RequestMethod.POST))
    fun callback(@RequestBody raw: ObjectNode): ResponseEntity<Any> {
        LOGGER.info("Incoming message from VK: $raw");

        val message: CallbackMessage<Any>? = vkMessageProcessor.parseMessage(raw)

        var code: String? = null

        if (message?.type?.equals(MessageType.CONFIRMATION) == true) {
            val groupId: Int? = vkConfiguration.getGroupId(vkConfiguration.token, vkConfiguration.version)
            code = groupId?.let {
                vkConfiguration.getCallbackConfirmationCode(it, vkConfiguration.token, vkConfiguration.version)
            }
        }

        val response: ResponseEntity<Any> =  when (message?.type) {
            MessageType.CONFIRMATION ->
                ResponseEntity.ok(code)
            MessageType.MESSAGE_NEW ->
                vkMessageProcessor.sendMessage(message.vkNewMessageObject.vkNewMessageObjectMessage.user_id, message.vkNewMessageObject.vkNewMessageObjectMessage.text, vkConfiguration.token, vkConfiguration.version)
            else -> ResponseEntity.ok("ok")
        }

        return response
    }
}