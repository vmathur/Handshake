package com.angelhack.handshake;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class PersonProfile {

    private String first_name;
    private String last_name;
    private String tag_line;
    private String linkedin_id;
    private String picture_url;
    private String email;
    private String phoneNumber;
    private String location;

    public PersonProfile(String fn, String ln, String tl, String id, String purl) {
        this.first_name = fn;
        this.last_name = ln;
        this.tag_line = tl;
        this.linkedin_id = id;
        this.picture_url = purl;
    }

    public PersonProfile(String fn, String ln, String tl, String id, String purl, String em, String pn, String loc) {
        this.first_name = fn;
        this.last_name = ln;
        this.tag_line = tl;
        this.linkedin_id = id;
        this.picture_url = purl;
        this.email = em;
        this.phoneNumber = pn;
        this.location = loc;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public String getFullName() { return this.first_name + " " + this.last_name;}

    public String getLinkedin_id() {
        return this.linkedin_id;
    }

    public String getTag_line() {
        return this.tag_line;
    }

    public String getPicture_url() {
        return this.picture_url;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }
}
