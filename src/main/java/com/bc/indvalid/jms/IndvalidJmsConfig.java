package com.bc.indvalid.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.bc.indvalid.jms.error.JmsErrorHandler;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJms
public class IndvalidJmsConfig {
	
	@Bean
    public JmsListenerContainerFactory<?> myFactory(
                            ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        factory.setCacheLevelName("CACHE_CONNECTION");
        factory.setConcurrency("2-100");
        factory.setErrorHandler(new JmsErrorHandler());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//        configurer.configure(factory, connectionFactory);
        configurer.configure(factory, connectionFactory());
        return factory;
    }
	
	@Bean
    public ConnectionFactory connectionFactory() {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("vm://embedded?broker.persistent=true,useShutdownHook=false");

        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setInitialRedeliveryDelay(0);
        policy.setBackOffMultiplier(2);
        policy.setUseExponentialBackOff(true);
        policy.setMaximumRedeliveries(4);
        policy.setRedeliveryDelay(500);
//        policy.setMaximumRedeliveryDelay(6000);
        policy.setUseCollisionAvoidance(Boolean.TRUE);
        activeMQConnectionFactory.setRedeliveryPolicy(policy);

        return activeMQConnectionFactory;

    }
 
    @Bean
    public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
