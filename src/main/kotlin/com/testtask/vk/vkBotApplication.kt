package com.testtask.vk

import com.testtask.vk.controller.VkController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class VkApplication

@Bean
fun controller() = VkController()

fun main(args: Array<String>) {
	runApplication<VkApplication>(*args)

}
