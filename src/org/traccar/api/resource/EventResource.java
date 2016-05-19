package org.traccar.api.resource;

import org.traccar.Context;
import org.traccar.api.BaseResource;
import org.traccar.model.*;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Collection;


@Path("events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource extends BaseResource {

    @PermitAll
    @GET
    public Collection<AbstractEvent> get() throws SQLException {
        /*Collection<Device> devices = Context.getDataManager().getDevices(getUserId());*/
        Collection<AbstractEvent> eventsByUserId = Context.getDataManager().getEventsByDeviceId(1);
        return eventsByUserId;
    }


}