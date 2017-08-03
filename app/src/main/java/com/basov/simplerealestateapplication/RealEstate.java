/*
 * Copyright (c) 2016. All Rights Reserved.
 */

package com.basov.simplerealestateapplication;


/*
Data Model to hold real estate values
 */
public class RealEstate {

    // define variables for the columns
    private int _id;
    private String name;
    private String rooms;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String lat;
    private String lng;

    // default constructor
    public RealEstate() {

    }

    // getters
    public int get_id() {
        return _id;
    }

    public String get_name() {
        return name;
    }

    public String get_rooms() {
        return rooms;
    }

    public String get_address() {
        return address;
    }

    public String get_city() {
        return city;
    }

    public String get_state() {
        return state;
    }

    public String get_zip() {
        return zip;
    }

    public String get_lat() {
        return lat;
    }

    public String get_lng() {
        return lng;
    }


    // setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public void set_rooms(String rooms) {
        this.rooms = rooms;
    }

    public void set_address(String address) {
        this.address = address;
    }

    public void set_city(String city) {
        this.city = city;
    }

    public void set_state(String state) {
        this.state = state;
    }

    public void set_zip(String zip) {
        this.zip = zip;
    }

    public void set_lat(String lat) {
        this.lat = lat;
    }

    public void set_lng(String lng) {
        this.lng = lng;
    }
}