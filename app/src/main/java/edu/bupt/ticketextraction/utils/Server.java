package edu.bupt.ticketextraction.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/07
 *     desc   : Server工具类，用于调用服务端API，不可实例化
 *     version: 0.0.1
 * </pre>
 */
@SuppressWarnings("unused")
public final class Server {
    /**
     * 调用登录服务
     *
     * @param phoneNumber 账号
     * @param password    密码
     * @return 1-登录成功, 0-密码错误, -1-用户名不存在
     */
    @Contract(pure = true)
    public static int callLogin(String phoneNumber, String password) {
        // 加密密码
        String cipherText = Server.passwordEncrypt(password);
        return 1;
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
        // 加密密码
        String cipherText = Server.passwordEncrypt(password);
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

    @Contract(pure = true)
    public static void generateExcel() {
        //TODO 生成表格
    }

    @Contract(pure = true)
    public static void sendEmail() {
        //TODO 发送邮件
    }

    @Contract(pure = true)
    public static void callCheckTicketValid() {
        //TODO 验真
    }

    @Contract(pure = true)
    public static void callOcr() {
        //TODO 识别
    }

    /**
     * Server工具类，请不要实例化此类！
     *
     * @throws InstantiationException 实例化异常，因为该类不可实例化
     */
    private Server() throws InstantiationException {
        throw new InstantiationException();
    }

    /**
     * 获得加密后的密码
     *
     * @param plainText 明文
     * @return 密文
     */
    private static @NotNull String passwordEncrypt(@NotNull String plainText) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("sha");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // messageDigest必不为空，因为输入的算法一定是正确的
        assert messageDigest != null;
        messageDigest.update(plainText.getBytes());
        return new BigInteger(messageDigest.digest()).toString(32);
    }
}
