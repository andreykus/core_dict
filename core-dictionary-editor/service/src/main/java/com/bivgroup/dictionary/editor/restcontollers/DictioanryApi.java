package com.bivgroup.dictionary.editor.restcontollers;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bush on 17.01.2017.
 */
@Path("/dictionary")
public class DictioanryApi {


    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAll() {
        List out = new ArrayList();
        out.add(new String("exampl"));
        if (1==1)  throw new NotFoundException();
        return new ArrayList();
    }
}
