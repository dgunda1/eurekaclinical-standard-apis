/**
 * 
 */
package org.eurekaclinical.standardapis.jms;

/*-
 * #%L
 * Eureka! Clinical Standard APIs
 * %%
 * Copyright (C) 2016 - 2019 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener; 
import javax.jms.MessageProducer; 
import javax.jms.Session; 
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;



/**
 * @author Dileep Gunda
 *
 */
public class EurekaMessageService implements MessageService {

	private Session session; 
	private Connection connection;
	
	//private final String url;
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	
//	public EurekaMessageService(Session session, Destination destination,Connection connection) {
//		this.session = session;
//        this.destination = destination;
//        this.connection=connection;
	public EurekaMessageService(String url) {
		this.url = url;
	}

	public void init() throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public MessageProducer createProducerForQueue(String name) throws JMSException {
		Destination destination = session.createQueue(name);
		return session.createProducer(destination);
	}

	public MessageConsumer createConsumerForQueue(String name) throws JMSException {
		Destination queue = session.createQueue(name);
		return session.createConsumer(queue);
	}

	public MessageConsumer createConsumerForQueue(String name, MessageListener listener) throws JMSException {
		MessageConsumer result = createConsumerForQueue(name);
		result.setMessageListener(listener);
		return result;
	}

	public void sendMessage(String queue, String message) throws JMSException {
//		LOGGER.info("sending message {}", message);
//		LOGGER.info("to queue {}", queue);
		TextMessage message2 = session.createTextMessage(message);
		MessageProducer resultProducer = createProducerForQueue(queue);
		resultProducer.send(message2);

	}

	@Override
	public void send(MessageCreator messageCreator) throws JMSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message receive() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fun() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void send(MessageCreator messageCreator) throws JMSException {
//		Message message = messageCreator.createMessage(session);
//		session.createProducer(destination).send(message);
//	}
//
//	@Override
//	public Message receive() throws JMSException {
//		MessageConsumer consumer = session.createConsumer(destination);
//		connection.start();
//		return consumer.receive();
//	}


}
