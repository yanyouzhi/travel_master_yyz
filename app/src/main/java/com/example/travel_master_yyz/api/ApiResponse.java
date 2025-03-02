package com.example.travel_master_yyz.api;

public class ApiResponse {
    private int code;
    private String msg;
    private Data data;


    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public Data getData() { return data; }

    public static class Data {
        private String phone;
        private String email;
        private String token;

        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getToken() { return token; }
    }
}
