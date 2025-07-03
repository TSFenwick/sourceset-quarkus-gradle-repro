package my.groupId;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import my.groupId.gen.api.PetsApi;
import my.groupId.gen.dtos.Pet;

import java.util.List;

@Path("/pets")
public class PetsResource implements PetsApi
{
  @Override
  public Response createPets()
  {
    return Response.ok().build();
  }

  @Override
  public Response listPets(Integer limit)
  {
    return Response.ok(List.of(new Pet(1L, "doggy"))).build();
  }

  @Override
  public Response showPetById(String petId)
  {
    if (petId.equals("1")) {
      return Response.ok(new Pet(1L, "doggy")).build();
    }
    else return Response.ok(List.of()).build();
  }
}
