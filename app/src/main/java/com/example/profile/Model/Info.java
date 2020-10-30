package com.example.profile.Model;

public class Info {
    String name,mobile,email,dob,aboutus,personality,tags,gen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAboutus() {
        return aboutus;
    }

    public void setAboutus(String aboutus) {
        this.aboutus = aboutus;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public Info(){


    }

    public Info(String name, String mobile, String email, String dob, String aboutus, String personality, String tags, String gen) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.dob = dob;
        this.aboutus = aboutus;
        this.personality = personality;
        this.tags = tags;
        this.gen = gen;
    }
}
