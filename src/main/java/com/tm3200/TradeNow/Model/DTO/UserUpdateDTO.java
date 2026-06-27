package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @Size(max = 255, message = "Photo URL must not exceed 255 characters")
    private String photo;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "Geographic zone is required")
    private String geographicZone;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String photo, String description, String geographicZone) {
        this.photo = photo;
        this.description = description;
        this.geographicZone = geographicZone;
    }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGeographicZone() { return geographicZone; }
    public void setGeographicZone(String geographicZone) { this.geographicZone = geographicZone; }

}
