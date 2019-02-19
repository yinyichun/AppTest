package com.yin.model;

import lombok.Data;

@Data
public class User {
    private String userName;
    private String password;
    private String sex;
    private int age;
    private int isDelete;
    private int permission;

}
