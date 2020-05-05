# Reactive streams in message-handle chain system using Spring WebFlux.

## Description

This is a multi-module project in which one service generates big amount of
messages per unit of time, and other two services sequentially process these messages. Handler 
services work slower than generator service, so the system handle backpressure. Crash of one of the 
services in the chain not leads to whole system failure. All this processing uses reactive streams
with WebFlux. All these services use custom actuator endpoint, so you have a chance to see ongoing process in real time via dashboard service.

The project consist of following modules:
 - message-center - message generator;
 - mr-analyst, police - message handlers (first one just send messages to another);
 - dashboard - shows generated and handled message amount for each of the service in a simple Swing form
 - message-starter - spring boot starter with Actuator endpoint functionality. 
