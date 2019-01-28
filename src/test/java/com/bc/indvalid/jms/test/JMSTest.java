package com.bc.indvalid.jms.test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import com.bc.indvalid.jms.IndvalidJmsConfig;
import com.bc.indvalid.jms.model.Message;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ContextHierarchy({
    @ContextConfiguration(classes = {IndvalidJmsConfig.class})})
public class JMSTest {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Test
	public void sendSaveJms() throws InterruptedException {
		log.info("---------------------------------------------------------Sending a message.");
        Message message = new Message();
        message.setBody("Prueba de mensaje");
        message.setDate(LocalDateTime.now());
//        for(int indice = 3; indice >= 0; indice--){
//        	log.info("AAR indice: "+indice);
//        	message.setId(indice);
//        	try {
//        		jmsTemplate.convertAndSend("indvalid.queue", message);
//        	} catch(Exception e){
//        		log.info("AAR Muere del lado del cliente: ",e.getMessage());
//        	}
//        	
//        }
		jmsTemplate.convertAndSend("indvalid.queue", message);

        log.info("Waiting for all ActiveMQ JMS Messages to be consumed");
        TimeUnit.SECONDS.sleep(15);
        System.exit(-1);
        
	}
	
}
