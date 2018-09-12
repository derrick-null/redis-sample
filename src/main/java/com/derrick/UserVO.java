package com.derrick;

import java.io.Serializable;

/**
 * Created by Derrick on 2018-08-30.
 * VO
 */
public class UserVO implements Serializable{

    private static final long serialVersionUID = -3595702104638588177L;

    private Long id;

    private String name;

    private String gender;

    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
