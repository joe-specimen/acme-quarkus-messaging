package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpSender;
import jakarta.inject.Inject;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ExecutorConsumerTest {

   @ConfigProperty(name = "amqp-host")
   String host;
   @ConfigProperty(name = "amqp-port")
   int port;
   private AmqpClient client;

   @Inject
   ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      client = AmqpClient.create(new AmqpClientOptions().setHost(host).setPort(port));
   }

   @AfterEach
   void tearDown() {
      client.closeAndAwait();
   }

   @Test
   void onMessage() throws JsonProcessingException {
      AmqpConnection connection = client.connectAndAwait();
      AmqpSender sender = connection.createSenderAndAwait("executor-queue");

      Executor executor = new Executor();
      executor.setId(UUID.randomUUID());
      executor.setName("executor");
      executor.setPosition("CEO");
      executor.setOnVacation(false);
      String executorJson = objectMapper.writeValueAsString(executor);

      sender.sendWithAckAndAwait(AmqpMessage.create().address("executor-queue").withBody(executorJson).build());
   }
}