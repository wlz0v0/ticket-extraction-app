package edu.bupt.ticketextraction.utils.ocr;

import android.util.Log;
import edu.bupt.ticketextraction.bill.tickets.CabTicket;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/07
 *     desc   : Ocr工具类，调用ocr识别发票信息，不可实例化
 *     version: 0.0.1
 * </pre>
 */
@SuppressWarnings("SpellCheckingInspection")
public final class Ocr {
    private final static File directory = new File(FileFactory.EXTERNAL_FILE_DIR);
    private final static File assessTokenFile = new File(directory + "/assess_token.dat");

    private Ocr() {
        throw new AssertionError();
    }

    /**
     * 调用ocr识别
     * TODO: 区分是图片还是视频，设置一个枚举类区分吧
     *
     * @param sourceFile 资源文件，图片或视频
     * @param walletName 资源文件所在钱包名
     * @return 识别得到的发票信息
     */
    public static @NotNull CabTicket extract(@NotNull File sourceFile, String walletName) {
        String res = taxiReceipt(sourceFile);
        String number = "识别异常";
        String code = "识别异常";
        String date = "识别异常";
        double unitPrice = 0.0;
        double distance = 0.0;
        double totalPrice = 0.0;
        JSONObject jsonObject = null;
        try {
            if (res != null) {
                // 解析识别结果
                JSONObject result = new JSONObject(res);
                jsonObject = new JSONObject(result.getJSONObject("words_result").toString());
                number = jsonObject.getString("InvoiceNum");
                // 若识别失败则字符串为空，设置内容为识别异常
                number = requiresNotEmpty(number, "识别异常");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            // 每个字段单独解析，以免万一有一个拉了剩下的全都解析不了
            try {
                code = jsonObject.getString("InvoiceCode");
                // 若识别失败则字符串为空，设置内容为识别异常
                code = requiresNotEmpty(code, "识别异常");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                date = jsonObject.getString("Date");
                // 若识别失败则字符串为空，设置内容为识别异常
                date = requiresNotEmpty(date, "识别异常");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String unitPriceStr = jsonObject.getString("PricePerkm");
                unitPriceStr = requiresNotEmpty(unitPriceStr, "0.0");
                unitPrice = Double.parseDouble(unitPriceStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String distanceStr = jsonObject.getString("Distance");
                distanceStr = requiresNotEmpty(distanceStr, "0.0");
                distance = Double.parseDouble(distanceStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String totalPriceStr = jsonObject.getString("TotalFare");
                totalPriceStr = requiresNotEmpty(totalPriceStr, "0.0");
                totalPrice = Double.parseDouble(totalPriceStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // 根据结果生成
        CabTicket.Builder builder = new CabTicket.Builder(walletName, sourceFile.getAbsolutePath(), number, code);
        builder.setUnitPrice(unitPrice).
                setTotalPrice(totalPrice).
                setDate(date).
                setDistance(distance);
        return builder.create();
    }

    /**
     * {
     * "log_id":2034039896,
     * "words_result_num":16,
     * "words_result":
     * {
     * "Date":"2017-11-26",
     * "Fare":"¥153.30元",
     * "Location": "北京",
     * "InvoiceCode":"111001681009",
     * "InvoiceNum":"90769610",
     * "TaxiNum":"BV2062",
     * "Time":"20:42-21:07",
     * "PickupTime":"20:42",
     * "DropoffTime":"21:07",
     * "FuelOilSurcharge": "¥1.00",
     * "CallServiceSurcharge": "¥1.00",
     * "TotalFare":"¥155.30元",
     * "Province": "北京",
     * "City": "北京市",
     * "PricePerkm": "2.50元/KM",
     * "Distance": "4.5KM"
     * }
     * }
     */
    public static @Nullable String taxiReceipt(File file) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/taxi_receipt";
        try {
            // 本地文件路径
            String filePath = file.getAbsolutePath();
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            String accessToken = getAuth();

            return HttpUtil.post(url, accessToken, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.<br>
     * 使用synchronized是因为在启动时主线程会调用此函数，需要同步一下
     *
     * @return assess_token 示例：
     */
    public static synchronized @Nullable String getAuth() {
        // 检查目录是否存在 不存在则创建
        if (!directory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();
        }
        // 检查文件是否存在 不存在则创建
        if (!assessTokenFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                assessTokenFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String line;
        String assessToken = null;
        Date lastDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(assessTokenFile)))) {
            // 读入assess_token
            // 如果为空则说明这是第一次读取，从服务器获取assess_token并缓存到本地
            line = reader.readLine();
            if (line == null) {
                Log.e("assess_token", "post");
                return getAuthFromBaidu();
            }
            assessToken = line;

            // 能读到这说明文件不为空，日期一定存在
            // 读入assess_token获取的日期
            line = reader.readLine();
            lastDate = format.parse(line);
            Date curDate = new Date();
            // lastDate转换后必不为null
            assert lastDate != null;
            long diff = curDate.getTime() - lastDate.getTime();
            // 如果日期大于30天则获取新的
            if (diff / (24 * 60 * 60 * 1000) >= 30) {
                return getAuthFromBaidu();
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return assessToken;
    }

    private static @Nullable String getAuthFromBaidu() {
        String ak = "3n6XC5aCjUq37vcKqZE1qgde";
        String sk = "ZcrDxscvXHLCawooVqz0xw9cA7x2EbsR";
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try (FileOutputStream outputStream = new FileOutputStream(assessTokenFile)) {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            /*
             * 返回结果
             */
            JSONObject jsonObject = new JSONObject(result.toString());
            String res = jsonObject.getString("access_token") + "\n";
            // 把结果缓存到文件 assess_token + date
            outputStream.write(res.getBytes(StandardCharsets.UTF_8));
            outputStream.write(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date())
                    .getBytes(StandardCharsets.UTF_8));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当字符串为空串""时，返回识别异常
     *
     * @param str 要检查的字符串
     * @return 当字符串为空串""时，返回识别异常;否则返回字符串本身
     */
    private static String requiresNotEmpty(@NotNull String str, @NotNull String defaultVal) {
        return str.equals("") ? defaultVal : str;
    }
}
