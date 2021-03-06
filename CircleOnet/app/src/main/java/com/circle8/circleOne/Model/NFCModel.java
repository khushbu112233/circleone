package com.circle8.circleOne.Model;

/**
 * Created by admin on 06/26/2017.
 */

public class NFCModel {

    int id;
    String name;
    String company;
    String designation;
    String mob_no;
    String work_no;
    String ph_no;
    String email;
    String website;
    String address;
    String lat;
    String lng;
    String remark;
    String fb_id;
    String linkedin_id;
    String google_id;
    String twitter_id;
    String youtube_id;
    int card_front;
    int card_back;
    String active;
    String nfc_tag;
    int user_image;
    String date;

    public NFCModel() {
    }

    public NFCModel(String name, String company, String designation, String mob_no, String work_no, String ph_no, String email, String website, String address, String lat, String lng, String remark, String fb_id, String linkedin_id, String google_id, String twitter_id, String youtube_id, int card_front, int card_back, String active, String nfc_tag, int user_image, String date) {
        this.name = name;
        this.company = company;
        this.designation = designation;
        this.mob_no = mob_no;
        this.work_no = work_no;
        this.ph_no = ph_no;
        this.email = email;
        this.website = website;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.remark = remark;
        this.fb_id = fb_id;
        this.linkedin_id = linkedin_id;
        this.google_id = google_id;
        this.twitter_id = twitter_id;
        this.youtube_id = youtube_id;
        this.card_front = card_front;
        this.card_back = card_back;
        this.active = active;
        this.nfc_tag = nfc_tag;
        this.user_image = user_image;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getWork_no() {
        return work_no;
    }

    public void setWork_no(String work_no) {
        this.work_no = work_no;
    }

    public String getPh_no() {
        return ph_no;
    }

    public void setPh_no(String ph_no) {
        this.ph_no = ph_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getLinkedin_id() {
        return linkedin_id;
    }

    public void setLinkedin_id(String linkedin_id) {
        this.linkedin_id = linkedin_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public int getCard_front() {
        return card_front;
    }

    public void setCard_front(int card_front) {
        this.card_front = card_front;
    }

    public int getCard_back() {
        return card_back;
    }

    public void setCard_back(int card_back) {
        this.card_back = card_back;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getNfc_tag() {
        return nfc_tag;
    }

    public void setNfc_tag(String nfc_tag) {
        this.nfc_tag = nfc_tag;
    }

    public int getUser_image() {
        return user_image;
    }

    public void setUser_image(int user_image) {
        this.user_image = user_image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
