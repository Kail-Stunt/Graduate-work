package service;

import org.springframework.stereotype.Service;
import dto.request.CreateOrUpdateAdRequest;
import dto.response.AdResponse;
import dto.response.AdsResponse;
import dto.response.ExtendedAdResponse;
import entity.Ad;
import entity.User;
import mapper.AdMapper;
import repository.AdRepository;
import repository.UserRepository;

import java.util.List;

@Service
public class AdService {

    public final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdMapper adMapper;

    public AdService(AdRepository adRepository, UserRepository userRepository, ImageService imageService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.adMapper = adMapper;
    }

    public AdsResponse getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdResponse> adResponses = ads.stream()
                .map(adMapper::toAdResponse)
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
        return adMapper.toAdResponse(saved);
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
        return adMapper.toAdResponse(saved);
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
        return adMapper.toExtendedResponse(ad);
    }

    public AdsResponse getAdsByUser(User user) {
        List<Ad> ads = adRepository.findByAuthor(user);
        List<AdResponse> adResponses = ads.stream()
                .map(adMapper::toAdResponse)
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
}
