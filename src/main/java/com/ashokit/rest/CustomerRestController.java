package com.ashokit.rest;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.ashokit.binding.CustomerEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class CustomerRestController {
	@GetMapping("/event")
	public Mono<CustomerEvent> getCustomerEvent(){
		CustomerEvent event = new CustomerEvent("smith",new Date());
	 Mono<CustomerEvent> mono = Mono.just(event);
		return mono;
		
	}
      @GetMapping(value = "/events",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<Flux<CustomerEvent>> getCustomerEvents(){
    	   //Creating Customer data in the form of object 
    		CustomerEvent event = new CustomerEvent("smith",new Date());
    		//Create Stream object to send the data
    		Stream<CustomerEvent> customerStream= Stream.generate(() -> event);
    		//create Flux object with Stream
    		Flux<CustomerEvent> customerFlux = Flux.fromStream(customerStream);
    		//Setting response interval
    		   Flux<Long> interval = Flux.interval(Duration.ofSeconds(3));
    		 //Combine Flux Interval and Customer Flux
    		Flux<Tuple2<Long, CustomerEvent>> zip= Flux.zip(interval, customerFlux);
    		//Getting Flux value from  the Zip
    		Flux<CustomerEvent>Fluxmap = zip.map(Tuple2::getT2);
    		//returning Flux Response
    		return new ResponseEntity<>(Fluxmap, HttpStatus.OK);
    		
    		
    	  
		
	}
}
