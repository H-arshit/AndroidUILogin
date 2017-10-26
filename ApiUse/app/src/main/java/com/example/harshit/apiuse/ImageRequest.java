package com.example.harshit.apiuse;

/**
 * Created by harshit on 25/10/17.
 */

public class ImageRequest {

    String email , auth_token , image ;

    public String getEmail() {
        return email;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public String getImage() {
        return image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
