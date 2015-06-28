package com.angelhack.handshake;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class PersonProfile {

    String user_id;
    String first_name;
    String last_name;
    String tag_line;
    String picture_url;

    public PersonProfile(String fn, String ln, String tl, String id, String purl) {
        this.first_name = fn;
        this.last_name = ln;
        this.tag_line = tl;
        this.user_id = id;
        this.picture_url = purl;
    }

    public PersonProfile() {

    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public String getFullName() { return this.first_name + " " + this.last_name;}

    public String getUser_id() {
        return this.user_id;
    }

    public String getTag_line() {
        return this.tag_line;
    }

    public String getPicture_url() {
        return this.picture_url;
    }

}
