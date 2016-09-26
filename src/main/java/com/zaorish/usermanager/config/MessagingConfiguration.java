package com.zaorish.usermanager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange("application-exchange");
	}

	@Bean
	public Queue userCreatedQueue() {
		return new Queue("userCreated", false);
	}

	@Bean
	public Queue userUpdatedNicknameQueue() {
		return new Queue("userUpdatedNickname", false);
	}

	@Bean
	public Binding userCreatedBinding(TopicExchange topicExchange, Queue userCreatedQueue) {
		return BindingBuilder.bind(userCreatedQueue).to(topicExchange).with("user.created");
	}

	@Bean
	public Binding userUpdatedNicknameBinding(TopicExchange topicExchange, Queue userUpdatedNicknameQueue) {
		return BindingBuilder.bind(userUpdatedNicknameQueue).to(topicExchange).with("user.updated.nickname");
	}

}
