package com.snxun.book.greendaolib.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户表
 * Created by zhouL on 2018/5/9.
 */
@Entity
public class UserTable {

    /** 主键 */
    @Id
    private Long id;
    /** 账号 */
    @Unique
    @NotNull
    private String account;
    /** 密码 */
    private String pswd;
    /** 密保问题 */
    private String pswdQuestion;
    /** 密保答案 */
    private String pswdAnswer;
    @Generated(hash = 2025272696)
    public UserTable(Long id, @NotNull String account, String pswd,
            String pswdQuestion, String pswdAnswer) {
        this.id = id;
        this.account = account;
        this.pswd = pswd;
        this.pswdQuestion = pswdQuestion;
        this.pswdAnswer = pswdAnswer;
    }
    @Generated(hash = 726134616)
    public UserTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPswd() {
        return this.pswd;
    }
    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
    public String getPswdQuestion() {
        return this.pswdQuestion;
    }
    public void setPswdQuestion(String pswdQuestion) {
        this.pswdQuestion = pswdQuestion;
    }
    public String getPswdAnswer() {
        return this.pswdAnswer;
    }
    public void setPswdAnswer(String pswdAnswer) {
        this.pswdAnswer = pswdAnswer;
    }

}
