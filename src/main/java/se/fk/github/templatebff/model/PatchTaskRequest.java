package se.fk.github.templatebff.model;

import jakarta.validation.constraints.NotBlank;

public class PatchTaskRequest
{
   @NotBlank
   public String handlaggningId;
   public String ersattningId;
   public String yrkandestatus;
}
