package se.fk.github.templatebff;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fk.github.templatebff.integration.BackendClient;
import se.fk.github.templatebff.model.PatchTaskBody;
import se.fk.github.templatebff.model.PatchTaskRequest;
import se.fk.github.templatebff.model.TaskRequest;

import java.time.Instant;
import java.util.Map;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateBFFController
{
   private static final Logger LOGGER = LoggerFactory.getLogger(TemplateBFFController.class);

   @Inject
   @RestClient
   BackendClient backendClient;

   // POST /api/task
   // Fetches task data from the backend for the given handlaggningId
   @POST
   @Path("/task")
   public Response getTask(@Valid TaskRequest body)
   {
      LOGGER.debug("POST /api/task handlaggningId={}", body.handlaggningId);
      try
      {
         JsonNode data = backendClient.getTask(body.handlaggningId);
         return Response.ok(data).build();
      }
      catch (WebApplicationException e)
      {
         LOGGER.error("Backend error fetching task for handlaggningId={}, status={}", body.handlaggningId, e.getResponse().getStatus(), e);
         return Response.status(e.getResponse().getStatus()).entity(Map.of("error", "Upstream error")).build();
      }
      catch (Exception e)
      {
         LOGGER.error("Error fetching task for handlaggningId={}", body.handlaggningId, e);
         return Response.status(500).entity(Map.of("error", "Internal server error")).build();
      }
   }

   // PATCH /api/task
   // Updates task fields (ersattningId, yrkandestatus) for the given handlaggningId
   @PATCH
   @Path("/task")
   public Response patchTask(@Valid PatchTaskRequest body)
   {
      LOGGER.debug("PATCH /api/task handlaggningId={}", body.handlaggningId);
      PatchTaskBody patchBody = new PatchTaskBody();
      patchBody.ersattningId = body.ersattningId;
      patchBody.yrkandestatus = body.yrkandestatus;
      try
      {
         backendClient.patchTask(body.handlaggningId, patchBody);
         return Response.ok().build();
      }
      catch (WebApplicationException e)
      {
         LOGGER.error("Backend error patching task for handlaggningId={}, status={}", body.handlaggningId, e.getResponse().getStatus(), e);
         return Response.status(e.getResponse().getStatus()).entity(Map.of("error", "Upstream error")).build();
      }
      catch (Exception e)
      {
         LOGGER.error("Error patching task for handlaggningId={}", body.handlaggningId, e);
         return Response.status(500).entity(Map.of("error", "Internal server error")).build();
      }
   }

   // POST /api/task/done
   // Marks a task as done, forwarding the Authorization header to the backend
   @POST
   @Path("/task/done")
   public Response taskDone(@Valid TaskRequest body, @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization)
   {
      LOGGER.debug("POST /api/task/done handlaggningId={}", body.handlaggningId);
      try
      {
         backendClient.taskDone(body.handlaggningId, authorization);
         return Response.noContent().build();
      }
      catch (WebApplicationException e)
      {
         LOGGER.error("Backend error calling done for handlaggningId={}, status={}", body.handlaggningId, e.getResponse().getStatus(), e);
         return Response.status(e.getResponse().getStatus()).entity(Map.of("error", "Upstream error")).build();
      }
      catch (Exception e)
      {
         LOGGER.error("Error calling done for handlaggningId={}", body.handlaggningId, e);
         return Response.status(500).entity(Map.of("error", "Internal server error")).build();
      }
   }

   // GET /api/uppgiftsbeskrivning
   // Fetches extended task descriptions from the backend
   @GET
   @Path("/uppgiftsbeskrivning")
   public Response getUppgiftsbeskrivning()
   {
      LOGGER.debug("GET /api/uppgiftsbeskrivning");
      try
      {
         JsonNode data = backendClient.getUppgiftsbeskrivning();
         return Response.ok(data).build();
      }
      catch (WebApplicationException e)
      {
         LOGGER.error("Backend error fetching uppgiftsbeskrivning, status={}", e.getResponse().getStatus(), e);
         return Response.status(e.getResponse().getStatus()).entity(Map.of("error", "Upstream error")).build();
      }
      catch (Exception e)
      {
         LOGGER.error("Error fetching uppgiftsbeskrivning", e);
         return Response.status(502).entity(Map.of("error", "Backend service unavailable")).build();
      }
   }
}
