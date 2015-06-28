package com.angelhack.handshake;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class PersonProfile {

    private String firstName;
    private String lastName;
    private String tagline;
    private String linkedinID;
    private String pictureURL;
    private long dateMet;

    public PersonProfile(String fn, String ln, String tl, String id, String purl, long dt) {
        this.firstName = fn;
        this.lastName = ln;
        this.tagline = tl;
        this.linkedinID = id;
        this.pictureURL = purl;
        this.dateMet = dt;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() { return this.firstName + " " + this.lastName;}

    public String getLinkedinURL() {
        return "https://www.linkedin.com/profile/view?id=" + this.linkedinID;
    }

    public String getLinkedinID() {
        return this.linkedinID;
    }

    public String getTagline() {
        return this.tagline;
    }

    public String getPictureURL() {
        return this.pictureURL;
    }

    public String getDateMet() {
        return "convert to string: " + dateMet;
    }

}
