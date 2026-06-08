package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class UserRegistrationDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@.\\-_=+&%$#@!:;/?])[A-Za-z\\d@.\\-_=+&%$#@!:;/?]{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, a number and a special character (@.-_=+&%$#!:;/?)"
    )
    private String password;

    @NotBlank(message = "Geographic zone is required")
    private String geographicZone;


    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String name, String email, String password, String geographicZone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.geographicZone = geographicZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGeographicZone() {
        return geographicZone;
    }

    public void setGeographicZone(String geographicZone) {
        this.geographicZone = geographicZone;
    }
}



