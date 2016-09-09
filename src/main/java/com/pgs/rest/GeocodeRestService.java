package com.pgs.rest;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Service;

import javax.validation.constraints.*;
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
                                @NotBlank(message = "{validation.constraint.getLocation.notblank.address}")
                                    String address) {
        return null;
    }

    @GET
    @Path("/locationinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocationInfo(@QueryParam("lat")
                                    @Min(value = -90, message = "{validation.constraint.getLocationInfo.minmax.latitude}")
                                    @Max(value = 90, message = "{validation.constraint.getLocationInfo.minmax.latitude}")
                                        String latitude,
                                    @QueryParam("lng")
                                    @Min(value = -180, message = "{validation.constraint.getLocationInfo.minmax.longitude}")
                                    @Max(value = 180, message = "{validation.constraint.getLocationInfo.minmax.longitude}")
                                        String longitude,
                                    @QueryParam("radius") @Min(value = 1, message = "{validation.constraint.getLocationInfo.min.radius}")
                                        int radius) {
        return null;
    }
}
