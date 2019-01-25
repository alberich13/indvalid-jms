package com.bc.indvalid.jms.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.bc.indvalid.jms.model.Message;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IndvalidService {
	
	@JmsListener(destination = "indvalid.queue", containerFactory = "myFactory")
	public void process(final Message message) throws Exception{
		log.info("Aqui se ejecuta algo periodicamente", message.getBody());
		if(true){
			throw new Exception("Fallamos a proposito");
		}
	}
}
