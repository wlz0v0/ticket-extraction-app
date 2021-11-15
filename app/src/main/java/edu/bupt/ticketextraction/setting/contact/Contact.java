package edu.bupt.ticketextraction.setting.contact;

import java.io.Serializable;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/12
 *     desc   : 联系人类，包含姓名和邮箱
 *     version: 0.0.1
 * </pre>
 */
public final class Contact implements Serializable {
    private String name;
    private String email;

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
