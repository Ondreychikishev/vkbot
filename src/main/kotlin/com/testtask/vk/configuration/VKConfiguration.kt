package com.testtask.vk.configuration

import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testtask.vk.dto.confirmation.VkCallbackConfirmationResponse
import com.testtask.vk.dto.server.VkAddServerResponse
import com.testtask.vk.dto.server.VkGetCallbackServersResponse
import com.testtask.vk.dto.server.VkGetCallbackServersResponseItem
import com.testtask.vk.dto.server.VkGetGroupIdResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class VKConfiguration {

    private val LOGGER: Logger = LoggerFactory.getLogger(VKConfiguration::class.java)

    private val gson: Gson = Gson()

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Value("\${vk.token}")
    lateinit var token: String

    @Value("\${vk.version}")
    lateinit var version: String

    @Value("\${vk.webhook}")
    lateinit var webhook: String

    private val TYPE_GET_GROUP_ID = object : TypeToken<VkGetGroupIdResponse>() {}.type
    private val TYPE_ADD_SERVER = object : TypeToken<VkAddServerResponse>() {}.type
    private val TYPE_GET_CALLBACK_SERVERS = object : TypeToken<VkGetCallbackServersResponse>() {}.type
    private val TYPE_GET_CONFIRMATION_TOKEN = object : TypeToken<VkCallbackConfirmationResponse>() {}.type

    @EventListener(ApplicationReadyEvent::class)
    fun init() {

        val groupId:Int? = getGroupId(token, version)
        val items: List<VkGetCallbackServersResponseItem>? = getCallbackServers(groupId, token, version)
        for ((index, item) in items?.withIndex()!!) {
            LOGGER.info("server№: $index, serverId: ${item.id}")
            deleteCallbackServer(groupId!!, item.id, token, version)
        }
        val serverId: Int? = addCallbackServer(groupId, webhook, token, version)
        setCallbackSettings(groupId, serverId, version)
    }

    fun getGroupId(token: String, version: String): Int? {

        LOGGER.info("Get vk groupId for token: $token")
        val url: String = "https://api.vk.com/method/groups.getById?&access_token={token}&v={version}"

        val response: ObjectNode? = try {
            restTemplate.getForObject(url, ObjectNode::class.java, token, version)
        } catch (e: Exception) {
            LOGGER.info("Error get groupId for token: $token")
            null
        }
        if (response != null) {
            val vkGetIdResponse: VkGetGroupIdResponse = gson.fromJson(response.toString(), TYPE_GET_GROUP_ID)
            return vkGetIdResponse.vkGetGroupIdItem.get(0).id
        }
        return null
    }

    fun addCallbackServer(groupId: Int?, webhook: String, token: String, version: String): Int? {

        LOGGER.info("add new vk server with id: groupId")
        val url: String = "https://api.vk.com/method/groups.addCallbackServer?group_id={groupId}&url={webhook}&title=VkServer&access_token={token}&v={version}"
        var response: ObjectNode? =
            try {
                restTemplate.getForObject(url, ObjectNode::class.java, groupId, webhook, token, version)
            } catch (e: Exception) {
                null
            }
        if (response != null) {
            val vkAddServerResponse: VkAddServerResponse = gson.fromJson(response.toString(), TYPE_ADD_SERVER)
            LOGGER.info("serverId : ${vkAddServerResponse.getServerIdResponse.server_id}")
            return vkAddServerResponse.getServerIdResponse.server_id
        }
        return null
    }

    fun setCallbackSettings(groupId: Int?, serverId: Int?, version: String) {

        LOGGER.info("set event message_new for vk server with id: $groupId")
        val url: String = "https://api.vk.com/method/groups.setCallbackSettings?group_id={groupId}&server_id={serverId}&message_new=1&access_token={token}&v={version}"
        var response: ObjectNode? =
            try {
                restTemplate.getForObject(url, ObjectNode::class.java, groupId, serverId, token, version)
            } catch (e: Exception) {
                null
            }
    }

    fun getCallbackServers(groupId: Int?, token: String, version: String): List<VkGetCallbackServersResponseItem>? {

        LOGGER.info("get all exist servers with groupId: $groupId")
        val url: String = "https://api.vk.com/method/groups.getCallbackServers?group_id={groupId}&access_token={token}&v={version}"
        var response: ObjectNode? =
            try {
                restTemplate.getForObject(url, ObjectNode::class.java, groupId, token, version)
            } catch (e: Exception) {
                null
            }
        if (response != null) {
            val items: VkGetCallbackServersResponse = gson.fromJson(response.toString(), TYPE_GET_CALLBACK_SERVERS)
            return items.vkGetCallbackServersResponseObject.items
        }
        return null
    }

    fun deleteCallbackServer(groupId: Int, serverId: Int, token: String, version: String) {

        LOGGER.info("delete server with groupId: $groupId, and serverId: $serverId")
        val url: String = "https://api.vk.com/method/groups.deleteCallbackServer?group_id={groupId}&server_id={serverId}&access_token={token}&v={version}"
        var response: ObjectNode? =
            try {
                restTemplate.getForObject(url, ObjectNode::class.java, groupId, serverId, token, version)
            } catch (e: Exception) {
                null
            }
    }

    fun getCallbackConfirmationCode(groupId: Int, token: String, version: String): String? {

        val url: String = "https://api.vk.com/method/groups.getCallbackConfirmationCode?group_id={groupId}&access_token={token}&v={version}"
        val response: ObjectNode? = try {
            LOGGER.info("Get confirmation code for token: $token")
            restTemplate.getForObject(url, ObjectNode::class.java, groupId, token, version)
        } catch (e: Exception) {
            LOGGER.info("Error get confirmation code for token: $token")
            null
        }
        if (response != null) {
            val vkGetConfirmationCodeResponse: VkCallbackConfirmationResponse = gson.fromJson(response.toString(), TYPE_GET_CONFIRMATION_TOKEN)
            return vkGetConfirmationCodeResponse.getServerCodeResponse.code
        }
        return null
    }
}