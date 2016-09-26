package com.zaorish.usermanager.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventListener implements ApplicationListener<UserCreatedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(UserCreatedEventListener.class);

	private static final String ROUTING_KEY = "user.created";

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private TopicExchange topicExchange;

	@Override
	public void onApplicationEvent(UserCreatedEvent event) {
		logger.info("Received user: {}", event.getUser());

		// sending the event on rabbit template with json serialization FIXME
		rabbitTemplate.convertAndSend(topicExchange.getName(), ROUTING_KEY, event.getUser().getNickname());
	}

}
