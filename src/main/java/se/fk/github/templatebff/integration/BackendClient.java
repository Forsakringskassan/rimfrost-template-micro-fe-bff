package se.fk.github.templatebff.integration;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import se.fk.github.templatebff.model.PatchTaskBody;

@RegisterRestClient(configKey = "backend")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BackendClient
{
   @GET
   @Path("/{handlaggningId}")
   JsonNode getTask(@PathParam("handlaggningId") String handlaggningId);

   @PATCH
   @Path("/{handlaggningId}")
   void patchTask(
         @PathParam("handlaggningId") String handlaggningId,
         PatchTaskBody body);

   @POST
   @Path("/{handlaggningId}/done")
   void taskDone(
         @PathParam("handlaggningId") String handlaggningId,
         @HeaderParam("Authorization") String authorization);

   @GET
   @Path("/utokadUppgiftsbeskrivning")
   JsonNode getUppgiftsbeskrivning();
}
