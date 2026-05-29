package se.fk.github.templatebff.model;

import jakarta.validation.constraints.NotBlank;

public class TaskRequest
{
   @NotBlank
   public String handlaggningId;
}
