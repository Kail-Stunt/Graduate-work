package mapper;

import dto.response.AdResponse;
import dto.response.ExtendedAdResponse;
import entity.Ad;
import org.springframework.stereotype.Component;

@Component
public class AdMapper {

    public AdResponse toAdResponse(Ad ad) {
        AdResponse response = new AdResponse();
        response.setPk(ad.getPk());
        response.setAuthor(ad.getAuthor().getId());
        response.setImage(ad.getImage());
        response.setPrice(ad.getPrice());
        response.setTitle(ad.getTitle());
        return response;
    }

    public ExtendedAdResponse toExtendedResponse(Ad ad) {
        ExtendedAdResponse response = new ExtendedAdResponse();
        response.setPk(ad.getPk());
        response.setAuthorFirstName(ad.getAuthor().getFirstName());
        response.setAuthorLastName(ad.getAuthor().getLastName());
        response.setDescription(ad.getDescription());
        response.setEmail(ad.getAuthor().getUsername());
        response.setImage(ad.getImage());
        response.setPhone(ad.getAuthor().getPhone());
        response.setPrice(ad.getPrice());
        response.setTitle(ad.getTitle());
        return response;
    }
}
