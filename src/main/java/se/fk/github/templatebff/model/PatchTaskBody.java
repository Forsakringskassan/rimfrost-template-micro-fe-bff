package se.fk.github.templatebff.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchTaskBody
{
   @JsonProperty("ersattning_id")
   public String ersattningId;
   public String yrkandestatus;
}
