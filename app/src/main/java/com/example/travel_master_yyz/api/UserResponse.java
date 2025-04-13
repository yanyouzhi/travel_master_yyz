 package com.example.travel_master_yyz.api;

public class UserResponse {
    private int code;
    private String msg;
    private UserData data;

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public UserData getData() { return data; }

    public static class UserData {
        private String name;
        private String login;
        private int collect_landscape_number;
        private String occupation;
        private String description;
        private String photo; // Base64编码头像
        private int collect_my; // 我的关注数
        private String addr;
        private String contact;
        private String email;
        private int sex;
        private String phone;
        private int praise;
        private String time;
        private String id;

        public String getLogin() {
            return login;
        }

        public String getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public int getCollect_landscape_number() {
            return collect_landscape_number;
        }

        public String getPhone() {
            return phone;
        }

        public String getName() { return name; }
        public String getOccupation() { return occupation; }
        public String getDescription() { return description; }
        public String getPhoto() { return photo; }
        public int getCollectMy() { return collect_my; }
        public String getAddr() { return addr; }
        public String getContact() { return contact; }
        public String getEmail() { return email; }
        public int getSex() { return sex; }

        public int getCollect_my() {
            return collect_my;
        }

        public int getPraise() {
            return praise;
        }
    }
}
