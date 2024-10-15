package org.acme;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Executor {

   private UUID id;
   private String name;
   private String position;
   private Boolean onVacation;
}
