package edu.bupt.ticketextraction.server;

import org.jetbrains.annotations.Contract;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/07
 *     desc   : 本类用于调用服务端API
 *     version: 0.0.1
 * </pre>
 */
@SuppressWarnings("unused")
public class Server {
    /**
     * 调用登录服务
     *
     * @param phoneNumber 账号
     * @param password    密码
     * @return 登录成功与否
     */
    @Contract(pure = true)
    public static boolean callLogin(String phoneNumber, String password) {
        return true;
    }

    /**
     * 调用注册服务
     *
     * @param phoneNumber      账号
     * @param password         密码
     * @param verificationCode 验证码
     * @return 注册成功与否
     */
    @Contract(pure = true)
    public static boolean callRegister(String phoneNumber, String password, String verificationCode) {
        return true;
    }

    /**
     * 验证手机号是否为本人手机号
     *
     * @param phoneNumber      账户
     * @param verificationCode 验证码
     * @return 验证码是否匹配
     */
    @Contract(pure = true)
    public static boolean callAccountVerification(String phoneNumber, String verificationCode) {
        return true;
    }

    /**
     * 发送验证码短信到指定手机号上
     *
     * @param phoneNumber 手机号
     */
    @Contract(pure = true)
    public static void callVerificationSending(String phoneNumber) {

    }
}
