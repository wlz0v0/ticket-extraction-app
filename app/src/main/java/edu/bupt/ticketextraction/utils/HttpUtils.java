package edu.bupt.ticketextraction.utils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import edu.bupt.ticketextraction.bill.tickets.CabTicket;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.setting.contact.Contact;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import edu.bupt.ticketextraction.utils.ocr.Ocr;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/07
 *     desc   : Server工具类，用于调用服务端API，不可实例化
 *     version: 0.0.1
 * </pre>
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public final class HttpUtils {
    public static final String PERMISSION_REFUSED = "refused";
    private final static String securityCode = "";
    @SuppressWarnings("HttpUrlsUsage")
    private final static String SERVER_URL = "http://ubuntu@crepusculumx.icu:8888";
    private final static String SEND_EMAIL_URL = SERVER_URL + "/mail";
    private final static String LOGIN_URL = SERVER_URL + "/login";
    private final static String REGISTER_URL = SERVER_URL + "/register";
    private final static String GET_CONTACT_URL = SERVER_URL + "/getMails";
    private final static String SET_CONTACT_URL = SERVER_URL + "/setMails";
    private final static String GET_VERSION_NUM = SERVER_URL + "/checkVersion";
    private final static String DOWNLOAD_APK = SERVER_URL + "/TaxiReceiptAPK";
    private final static String TRUE = "True";
    private static volatile boolean stopFlag = true;
    private static volatile CabTicket curTicket;
    private static volatile String postResult;
    private static volatile String getResult;

    /**
     * HttpUtils工具类，请不要实例化此类！
     */
    private HttpUtils() {
        throw new AssertionError();
    }

    public static @NotNull CabTicket callExtract(@NotNull AutoPushPopActivity activity, @NotNull File sourceFile, String walletName) {
        new Thread(() -> {
            curTicket = Ocr.extract(sourceFile, walletName);
            stopFlag = false;
            Log.e("thread", "done");
        }).start();
        HttpUtils.threadBlocking("正在识别中，请稍等", activity);
        return curTicket;
    }

    /**
     * @return 最新的版本号
     */
    public static String getLatestVersionNum(@NotNull AutoPushPopActivity activity) {
        return asyncGet(GET_VERSION_NUM);
    }

    /**
     * 下载最新版的APK
     */
    public static void downloadLatestApk(@NotNull AutoPushPopActivity activity) {
        // 检查是否存在安装包，存在则直接安装
        File file = new File(FileFactory.EXTERNAL_FILE_DIR + FileFactory.APK_PATH);
        if (file.exists()) {
            DownloadReceiver.install(activity, file.getPath());
            return;
        }
        DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(DOWNLOAD_APK);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(activity, null, FileFactory.APK_PATH);
        // 下载时和下载完成通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("发票识别");
        request.setDescription("正在下载最新版app");
        // 指定文件类型为apk
        request.setMimeType("application/vnd.android.package-archive");

        dm.enqueue(request);
        Log.e("download", "start");
        activity.showBottomToast(activity, "开始下载", 5);
    }

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
        String cipherText = HttpUtils.passwordEncrypt(password);
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("key", cipherText);
        String res = HttpUtils.asyncPost(LOGIN_URL, map);
        return Integer.parseInt(res);
    }

    /**
     * @param phoneNumber 手机号
     * @return 从数据库中查到的联系人
     */
    @Contract(pure = true)
    public static Contact @NotNull [] callGetContacts(@NotNull String phoneNumber) {
        Contact[] contacts = new Contact[4];
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        String res = HttpUtils.asyncPost(GET_CONTACT_URL, map);
        String[] nameAndEmails = res.split(" ");
        for (int i = 0; i < contacts.length; ++i) {
            contacts[i].setName(nameAndEmails[i * 2]);
            contacts[i].setEmail(nameAndEmails[i * 2 + 1]);
        }
        return contacts;
    }

    /**
     * @param phoneNumber 手机号
     * @return 是否成功
     */
    @Contract(pure = true)
    public static boolean callSetContacts(@NotNull String phoneNumber, Contact @NotNull [] contacts) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        for (int i = 0; i < contacts.length; ++i) {
            map.put("name" + i, contacts[i].getName());
            map.put("mail" + i, contacts[i].getEmail());
        }
        String res = HttpUtils.asyncPost(SET_CONTACT_URL, map);
        return res.equals(TRUE);
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
        String cipherText = HttpUtils.passwordEncrypt(password);
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber);
        map.put("key", cipherText);
        String res = HttpUtils.asyncPost(REGISTER_URL, map);
        return res.equals(TRUE);
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
     * @return 返回的验证码
     */
    @Contract(pure = true)
    public static @NotNull String callVerificationSending(String phoneNumber) {
        return "6626";
    }

    /**
     * 把出租车发票的信息转换成表格
     *
     * @param tickets 待发送的所有票据信息
     * @param email   目标邮箱
     * @return 是否成功
     */
    @Contract(pure = true)
    public static boolean sendEmail(@NotNull ArrayList<CabTicket> tickets, String email, @NotNull AutoPushPopActivity activity) {
        // 每个发票一行，再加第一行的说明
        final int rowCnt = tickets.size() + 1;
        Log.e("mail", "row count" + rowCnt);
        // 第一列的序号，再加上单价、距离、总价、日期四列
        final int columnCnt = 7;
        String[] firstRow = {"发票", "发票号码", "发票代码", "单价", "距离", "总价", "日期"};

        // 第一行说明信息
        StringBuilder sb = new StringBuilder();
        for (String s : firstRow) {
            sb.append(s).append(" ");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("mail", email);
        map.put("0", String.valueOf(rowCnt));
        map.put("1", sb.toString());
        for (int i = 2; i <= rowCnt; ++i) {
            // 序号、单价、距离、总价、日期
            CabTicket ticket = tickets.get(i - 2);
            String sb2 = i - 1 + " " + ticket.getTicketNumber() + " " + ticket.getTicketCode() + " " + ticket.getUnitPrice() + " " + ticket.getDistance() + " " + ticket.getTotalPrice() + " " + ticket.getDate();
            map.put(String.valueOf(i), sb2);
        }
        Log.e("mail", map.toString());

        // post调用发送邮件服务
        String res = asyncPost(SEND_EMAIL_URL, map);
        return res.equals(TRUE);
    }

    @Contract(pure = true)
    public static void callCheckTicketValid() {
        //TODO 验真
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
        // messageDigest必不为空，因为输入sha算法一定是正确的
        assert messageDigest != null;
        messageDigest.update(plainText.getBytes());
        return new BigInteger(messageDigest.digest()).toString(32);
    }

    /**
     * 弹出弹窗并阻塞线程
     *
     * @param dialogText 弹窗信息
     * @param activity   弹窗弹出的上下文
     */
    private static void threadBlocking(String dialogText, AutoPushPopActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false).setMessage(dialogText);
        AlertDialog dialog = builder.create();
        dialog.show();
        //noinspection StatementWithEmptyBody
        while (stopFlag) {
        }
        stopFlag = true;
        dialog.dismiss();
    }

    private static String asyncGet(String urlStr) {
        new Thread(() -> {
            getResult = get(urlStr);
            stopFlag = false;
        }).start();
        //noinspection StatementWithEmptyBody
        while (stopFlag) {
        }
        stopFlag = true;
        return getResult;
    }

    private static String asyncPost(String urlStr, Map<String, String> params) {
        new Thread(() -> {
            postResult = post(urlStr, params);
            stopFlag = false;
        }).start();
        //noinspection StatementWithEmptyBody
        while (stopFlag) {
        }
        stopFlag = true;
        return postResult;
    }

    private static String post(@NotNull String urlStr, @NotNull Map<String, String> params) {
        StringBuilder s = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //以下两行必须加否则报错.
            conn.setDoInput(true);
            conn.setDoOutput(true);
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb = new StringBuilder(sb.substring(0, sb.lastIndexOf("&")));
            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            writer.write(sb + "\r\n");

            writer.flush();
            String line;
            reader = conn.getResponseCode() > 400 ? new BufferedReader(new InputStreamReader(conn.getErrorStream())) : new BufferedReader(new InputStreamReader(conn.getInputStream()));
            s = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                s.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assert s != null;
        return s.toString();
    }

    private static String get(String urlStr) {
        StringBuilder s = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String line;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            s = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                s.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assert s != null;
        return s.toString();
    }
}
