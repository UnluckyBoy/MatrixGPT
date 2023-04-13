package com.matrix.matrixgpt.Network.ResponseBean.BackService;

/**
 * @ClassName UserBean
 * @Author Create By Administrator
 * @Date 2023/4/11 0011 20:16
 */
public class UserBean {
    private int id;
    private String head;
    private String name;
    private String password;
    private String sex;
    private String account;
    private String phone;
    private String email;
    private int gptNum;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGptNum() {
        return gptNum;
    }

    public void setGptNum(int gptNum) {
        this.gptNum = gptNum;
    }
}
