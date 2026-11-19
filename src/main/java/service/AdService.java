
package service;

import org.springframework.stereotype.Service;
import dto.request.CreateOrUpdateAdRequest;
import dto.response.AdResponse;
import dto.response.AdsResponse;
import dto.response.ExtendedAdResponse;
import entity.Ad;
import entity.User;
import repository.AdRepository;
import repository.UserRepository;

import java.util.List;

@Service
public class AdService {

    public final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public AdService(AdRepository adRepository, UserRepository userRepository, ImageService imageService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    public AdsResponse getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdResponse> adResponses = ads.stream()
                .map(this::toAdResponse)
                .toList();
        AdsResponse response = new AdsResponse();
        response.setCount((int) adRepository.count());
        response.setResults(adResponses);
        return response;
    }

    public AdResponse addAd(CreateOrUpdateAdRequest request, User author, String imagePath) {
        Ad ad = new Ad();
        ad.setAuthor(author);
        ad.setImage(imagePath);
        ad.setPrice(request.getPrice());
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());

        Ad saved = adRepository.save(ad);
        return toAdResponse(saved);
    }

    public AdResponse updateAd(Integer adId, CreateOrUpdateAdRequest request, User author) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getAuthor().getId().equals(author.getId())) {
            throw new IllegalArgumentException("You can only update your own ads");
        }

        if (request.getTitle() != null) {
            ad.setTitle(request.getTitle());
        }
        if (request.getPrice() != null) {
            ad.setPrice(request.getPrice());
        }
        if (request.getDescription() != null) {
            ad.setDescription(request.getDescription());
        }

        Ad saved = adRepository.save(ad);
        return toAdResponse(saved);
    }

    public void updateAdImage(Integer adId, String imagePath, User author) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getAuthor().getId().equals(author.getId())) {
            throw new IllegalArgumentException("You can only update your own ad images");
        }

        if (ad.getImage() != null) {
            imageService.deleteImage(ad.getImage());
        }

        ad.setImage(imagePath);
        adRepository.save(ad);
    }

    public void removeAd(Integer adId, User author) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getAuthor().getId().equals(author.getId())) {
            throw new IllegalArgumentException("You can only delete your own ads");
        }

        adRepository.delete(ad);
    }

    public ExtendedAdResponse getAd(Integer adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        return toExtendedResponse(ad);
    }

    public AdsResponse getAdsByUser(User user) {
        List<Ad> ads = adRepository.findByAuthor(user);
        List<AdResponse> adResponses = ads.stream()
                .map(this::toAdResponse)
                .toList();
        AdsResponse response = new AdsResponse();
        response.setCount(ads.size());
        response.setResults(adResponses);
        return response;
    }

    public Ad getAdById(Integer adId) {
        return adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    private AdResponse toAdResponse(Ad ad) {
        AdResponse response = new AdResponse();
        response.setPk(ad.getPk());
        response.setAuthor(ad.getAuthor().getId());
        response.setImage(ad.getImage());
        response.setPrice(ad.getPrice());
        response.setTitle(ad.getTitle());
        return response;
    }

    private ExtendedAdResponse toExtendedResponse(Ad ad) {
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
