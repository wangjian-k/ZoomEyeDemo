package com.simple.zoom.vo;

import java.util.ArrayList;

/**
 * Created by kart0l on 2016/4/20.
 */
public class HostQueryResult {

    public ArrayList<result> matches;

    public static class result {
        public GeoInfo geoinfo;
        public PortInfo portinfo;
        public String ip;
        public String timestamp;
    }

    public static class GeoInfo {
        public Country country;
    }

    public static class Country {
        public String code;
        public Name names;
    }

    public static class Name {
        public String en;
    }

    public static class PortInfo {
        public String product;
        public String hostname;
        public String service;
        public String os;
        public String banner;
        public int port;
    }
}
