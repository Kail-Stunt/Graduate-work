
package dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordRequest {

    @NotBlank
    @Size(min = 8, max = 16)
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;

    public NewPasswordRequest() {}

    public NewPasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
