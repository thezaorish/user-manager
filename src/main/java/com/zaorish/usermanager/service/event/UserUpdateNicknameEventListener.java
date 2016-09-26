package com.zaorish.usermanager.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateNicknameEventListener implements ApplicationListener<UserUpdateNicknameEvent> {

	private static final Logger logger = LoggerFactory.getLogger(UserUpdateNicknameEventListener.class);

	private static final String ROUTING_KEY = "user.updated.nickname";

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private TopicExchange topicExchange;

	@Override
	public void onApplicationEvent(UserUpdateNicknameEvent event) {
		logger.info("Received user: {}", event.getUser());

		// sending the event on rabbit template with json serialization FIXME
		rabbitTemplate.convertAndSend(topicExchange.getName(), ROUTING_KEY, event.getUser().getNickname());
	}

}
