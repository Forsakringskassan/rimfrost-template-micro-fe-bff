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
import java.util.function.Supplier;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateBFFController
{
   private static final Logger LOGGER = LoggerFactory.getLogger(TemplateBFFController.class);

   @Inject
   @RestClient
   private BackendClient backendClient;

   @GET
   @Path("/health")
   public Response health()
   {
      return Response.ok(Map.of("status", "ok", "timestamp", Instant.now().toString())).build();
   }

   @POST
   @Path("/task")
   public Response getTask(@Valid TaskRequest body)
   {
      LOGGER.debug("POST /api/task handlaggningId={}", body.handlaggningId());
      return call(body.handlaggningId(), () -> {
         JsonNode data = backendClient.getTask(body.handlaggningId());
         return Response.ok(data).build();
      });
   }

   @PATCH
   @Path("/task")
   public Response patchTask(@Valid PatchTaskRequest body)
   {
      LOGGER.debug("PATCH /api/task handlaggningId={}", body.handlaggningId());
      return call(body.handlaggningId(), () -> {
         backendClient.patchTask(body.handlaggningId(), new PatchTaskBody(body.ersattningId(), body.yrkandestatus()));
         return Response.noContent().build();
      });
   }

   @POST
   @Path("/task/done")
   public Response taskDone(@Valid TaskRequest body, @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization)
   {
      LOGGER.debug("POST /api/task/done handlaggningId={}", body.handlaggningId());
      if (authorization == null || authorization.isBlank())
      {
         return Response.status(401).entity(Map.of("error", "Authorization header required")).build();
      }
      return call(body.handlaggningId(), () -> {
         backendClient.taskDone(body.handlaggningId(), authorization);
         return Response.noContent().build();
      });
   }

   @GET
   @Path("/uppgiftsbeskrivning")
   public Response getUppgiftsbeskrivning()
   {
      LOGGER.debug("GET /api/uppgiftsbeskrivning");
      return call(null, () -> {
         JsonNode data = backendClient.getUppgiftsbeskrivning();
         return Response.ok(data).build();
      });
   }

   private Response call(String contextId, Supplier<Response> action)
   {
      try
      {
         return action.get();
      }
      catch (WebApplicationException e)
      {
         LOGGER.error("Upstream error contextId={}, status={}", contextId, e.getResponse().getStatus(), e);
         return Response.status(e.getResponse().getStatus()).entity(Map.of("error", "Upstream error")).build();
      }
      catch (Exception e)
      {
         LOGGER.error("Internal error contextId={}", contextId, e);
         return Response.status(500).entity(Map.of("error", "Internal server error")).build();
      }
   }
}
