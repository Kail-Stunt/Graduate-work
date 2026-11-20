
package dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateOrUpdateAdRequest {

    @NotBlank
    @Size(min = 4, max = 32)
    private String title;

    @Min(0)
    @Max(10000000)
    private Integer price;

    @NotBlank
    @Size(min = 8, max = 64)
    private String description;

    public CreateOrUpdateAdRequest() {}

    public CreateOrUpdateAdRequest(String title, Integer price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
