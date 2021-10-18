package edu.bupt.ticketextraction.export.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/30
 *     desc   : 自定义密码验证类
 *     version: 0.0.1
 * </pre>
 */
public class MyAuthenticator extends Authenticator {
    private final String username;
    private final String password;

    public MyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
