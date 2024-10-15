package org.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.MessageConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class ExecutorConverter implements MessageConverter {

   @Inject
   ObjectMapper objectMapper;

   @Override
   public boolean canConvert(Message<?> in, Type target) {
      return in.getPayload() instanceof String && isAssignableFrom(target, Executor.class);
   }

   @Override
   public Message<?> convert(Message<?> in, Type target) {
      String payload = (String) in.getPayload();
      Executor executor = convertFrom(payload);
      return in.withPayload(executor);
   }

   private Executor convertFrom(String payload) {
      try {
         return objectMapper.readValue(payload.getBytes(StandardCharsets.UTF_8), Executor.class);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private boolean isAssignableFrom(Type type, Class<?> clazz) {
      return classFrom(type)
          .map(aClass -> aClass.isAssignableFrom(Executor.class))
          .orElse(false);
   }

   private Optional<Class<?>> classFrom(Type type) {
      try {
         return Optional.of(Class.forName(type.getTypeName()));
      } catch (ClassNotFoundException e) {
         return Optional.empty();
      }
   }
}
