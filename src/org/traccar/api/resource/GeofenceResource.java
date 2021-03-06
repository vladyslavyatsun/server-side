package org.traccar.api.resource;

import org.traccar.Context;
import org.traccar.api.BaseResource;
import org.traccar.helper.Log;
import org.traccar.model.Geofence;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Collection;

@Path("geofence")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeofenceResource extends BaseResource {

    @GET
    public Collection<Geofence> get() throws SQLException {
        if (Context.getPermissionsManager().isAdmin(getUserId())) {
            return Context.getDataManager().getAllGeofences();
        }
        return Context.getDataManager().getGeofencesByUserId(getUserId());
    }

    @POST
    public Response add(Geofence entity) throws SQLException {
        entity.setUserId(getUserId());
        Context.getDataManager().addGeofence(entity);
        return Response.ok(entity).build();
    }

    @Path("{id}")
    @PUT
    public Response update(@PathParam("id") long id, Geofence entity) throws SQLException {
        Geofence geofence = Context.getDataManager().getGeofenceById(id);
        if (geofence == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Context.getPermissionsManager().checkGepfence(getUserId(), geofence.getUserId());

        Context.getDataManager().updateGeofence(entity);
        return Response.ok(entity).build();
    }

    @Path("{id}")
    @DELETE
    public Response remove(@PathParam("id") long id) throws SQLException {
        Geofence geofence = Context.getDataManager().getGeofenceById(id);
        if (geofence == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Context.getPermissionsManager().checkGepfence(getUserId(), geofence.getUserId());
        Context.getDataManager().removeGeofence(id);
        return Response.noContent().build();
    }
}

