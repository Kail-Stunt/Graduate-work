
package dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateOrUpdateCommentRequest {

    @NotBlank
    @Size(min = 8, max = 64)
    private String text;

    public CreateOrUpdateCommentRequest() {}

    public CreateOrUpdateCommentRequest(String text) {
        this.text = text;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
