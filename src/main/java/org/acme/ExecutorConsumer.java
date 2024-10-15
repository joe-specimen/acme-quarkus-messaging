package org.acme;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExecutorConsumer {

   private static final Logger log = Logger.getLogger(ExecutorConsumer.class);


   @Incoming("executor-in")
   @Blocking
   public void onMessage(Executor executor) {
      log.infof("Received executor: %s", executor);
   }

   /*
   @Incoming("executor-in")
   @Blocking
   public void onMessage(String executor) {
      log.infof("Received executor: %s", executor);
   }
    */
}
