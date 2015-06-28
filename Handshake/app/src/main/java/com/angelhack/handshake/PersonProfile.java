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
    String mac_address;
    String email;
    String phoneNumber;
    String location;


    public PersonProfile(String fn, String ln, String tl, String id, String purl, String macAddress) {
        this.first_name = (fn != null && !fn.isEmpty()) ? fn : "FirstNm";
        this.last_name = (ln != null && !ln.isEmpty()) ? ln : "LastAne";
        this.tag_line = (tl != null && !tl.isEmpty()) ? tl : "Title";
        this.user_id = (id != null && !id.isEmpty()) ? id : "You really messed up";
        this.picture_url = (purl != null && !purl.isEmpty()) ? purl : "no image";
        this.email = "email@email.email";
        this.phoneNumber = "(555) 555 - 5555";
        this.location = "Seattle, WA";
        this.mac_address = (macAddress != null && !macAddress.isEmpty()) ? macAddress : "Da:rn:yo:uf:or:th:is";
    }

    public PersonProfile() {

    }

    public PersonProfile(String fn, String ln, String tl, String id, String purl, String macAddress, String em, String pn, String loc) {
        this.first_name = (fn != null && !fn.isEmpty()) ? fn : "FirstNm";
        this.last_name = (ln != null && !ln.isEmpty()) ? ln : "LastAne";
        this.tag_line = (tl != null && !tl.isEmpty()) ? tl : "Title";
        this.user_id = (id != null && !id.isEmpty()) ? id : "You really messed up";
        this.picture_url = (purl != null && !purl.isEmpty()) ? purl : "no image";
        this.email = (em != null && !em.isEmpty()) ? em : "email@email.email";
        this.phoneNumber = (pn != null && !pn.isEmpty()) ? pn : "(555) 555 - 5555";
        this.location = (loc != null && !loc.isEmpty()) ? loc : "Seattle, WA";
        this.mac_address = (macAddress != null && !macAddress.isEmpty()) ? macAddress : "Da:rn:yo:uf:or:th:is";
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

    public String getMac_Address() {
        return this.mac_address;
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
