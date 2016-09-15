package com.pgs.rest;

import com.pgs.hal.annotation.HalLink;
import com.pgs.hal.annotation.HalLinks;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by jpolitowicz on 12.08.2016.
 */
@Service("geocodeService")
@Path("/geoservice/")
public class GeocodeRestService {

    @GET
    @Path("/location")
    @Produces(RepresentationFactory.HAL_JSON)
    @HalLinks(@HalLink(name = "next", uris = "/some/static/url"))
    public Response getLocation(@QueryParam("address")
                                @NotBlank(message = "{validation.constraint.getLocation.notblank.address}")
                                    String address) {
        return null;
    }

    @GET
    @Path("/locationinfo")
    @Produces(RepresentationFactory.HAL_JSON)
    @HalLinks
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
