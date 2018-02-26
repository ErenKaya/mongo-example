package kim.eren.infonalquest.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

public class User {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    @Indexed
    private String phoneNumber;
    @Transient
    private String captcha;

    public User(String name, String surname, String phoneNumber, String captcha) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.captcha = captcha;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
