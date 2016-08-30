package com.pgs.rest;

import com.pgs.model.Location;
import com.pgs.model.LocationInfo;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by jpolitowicz on 12.08.2016.
 */
@Service("geocodeService")
@Path("/geoservice/")
public class GeocodeRestService {

    @GET
    @Path("/location")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocation(@QueryParam("address")
                                @NotNull String address) {
        return null;
    }

    @GET
    @Path("/locationinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocationInfo(@QueryParam("lat") @DecimalMin("-90") @DecimalMax("90")
                                        String latitude,
                                        @QueryParam("lng") @DecimalMin("-180") @DecimalMax("180")
                                        String longitude,
                                        @QueryParam("radius") @Min(1)
                                        int radius) {
        return null;
    }
}
