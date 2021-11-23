package edu.bupt.ticketextraction.utils.ocr;

import android.util.Log;
import edu.bupt.ticketextraction.bill.tickets.CabTicket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
                Log.e("json", jsonObject.toString());
                number = jsonObject.getString("InvoiceNum");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            try {
                code = jsonObject.getString("InvoiceCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                date = jsonObject.getString("Date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                unitPrice = jsonObject.getDouble("PricePerkm");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                distance = jsonObject.getDouble("Distance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                totalPrice = jsonObject.getDouble("TotalFare");
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

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            return HttpUtil.post(url, accessToken, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @return assess_token 示例：
     */
    private static @Nullable String getAuth() {
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
        try {
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
             * 返回结果示例
             */
            JSONObject jsonObject = new JSONObject(result.toString());
            String res = jsonObject.getString("access_token");
            Log.e("token", res);
            return res;
        } catch (Exception e) {
            System.err.print("获取token失败！");
            e.printStackTrace();
        }
        return null;
    }
}
