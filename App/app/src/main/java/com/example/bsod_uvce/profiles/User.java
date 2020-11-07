package com.example.bsod_uvce.profiles;

import java.util.ArrayList;
import java.util.HashMap;

public class User
{
    String Name;
    String phoneNumber;
    ArrayList<String> skillSet;
    String location;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

   public String getLocation() {
        return location;
    }

    public void addNewSkill(String skill)
    {
        this.skillSet.add(skill);
    }

    public User() {
        this.skillSet=new ArrayList<>();
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
