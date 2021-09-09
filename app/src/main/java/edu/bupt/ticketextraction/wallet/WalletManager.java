package edu.bupt.ticketextraction.wallet;

import edu.bupt.ticketextraction.activity.MainActivity;
import edu.bupt.ticketextraction.file.FileManager;
import edu.bupt.ticketextraction.file.filefactory.FileFactory;
import edu.bupt.ticketextraction.fragment.WalletButtonFragment;
import edu.bupt.ticketextraction.fragment.WalletCheckBoxFragment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/03
 *     desc   : 钱包管理类
 *              练习一下单例模式
 *     version: 0.0.1
 * </pre>
 */
public enum WalletManager {
    INSTANCE;

    /**
     *  key: wallet name string
     *  value: wallet
     *  采用LinkedHashMap原因：在界面初始化时必须要根据原有输入顺序创建所有钱包fragment
     *  HashMap会打乱顺序，LinkedHashMap会保留原有输入顺序
     *  所以采用LinkedHashMap
     **/
    private final LinkedHashMap<String, Wallet> wallets = new LinkedHashMap<>();

    private final static String WALLETS_DATA_PATH = FileFactory.EXTERNAL_FILE_DIR + "/wallets/WalletsData.dat";

    private static MainActivity mainActivity;

    /**
     *  由于在创建wallet fragment时需要设置按钮点击监听器
     *  使得点击wallet时能够从main activity跳转到对应wallet activity
     *  于是需要一个main activity的实例来进行跳转
     *  在本类中只能选择用一个静态变量获取到main activity
     *  以便将其传给wallet fragment
     */
    public static void setMainActivity(MainActivity mainActivity) {
        WalletManager.mainActivity = mainActivity;
    }

    public static WalletManager getInstance() {
        return INSTANCE;
    }

    /**
     * CreateWalletActivity也会调用此方法
     * @param walletName 钱包名
     * @return 是否创建成功
     **/
    public boolean createWallet(String walletName) {
        if (walletName.isEmpty()) {
            return false;
        }
        if (walletName.equals("默认")) {
            return true;
        }
        // 新建一个wallet并添加到wallets中管理
        Wallet wallet = new Wallet(walletName);
        WalletManager.getInstance().addWallet(wallet);

        // 新建一个wallet button fragment并将其添加到对应的container中
        WalletButtonFragment fgBtnWallet = new WalletButtonFragment(walletName, mainActivity);
        // 将新建的wallet fragment添加到MainActivity中并在其中展示
        MainActivity.walletButtonFragments.put(fgBtnWallet, false);

        // 新建一个wallet button fragment并将其添加到对应的container中
        WalletCheckBoxFragment fgCBWallet = new WalletCheckBoxFragment(walletName, mainActivity);
        // 将新建的实例添加到HashMap中
        WalletCheckBoxFragment.checkBoxFragmentHashMap.put(walletName, fgCBWallet);
        // 将新建的wallet check box fragment添加到MainActivity中并在其中展示
        MainActivity.walletCheckBoxFragments.put(fgCBWallet, false);
        return true;
    }

    // 先把wallets写入，再写入每个wallet
    public void writeToData() {
        File file = new File(WALLETS_DATA_PATH);
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(WALLETS_DATA_PATH, false);
            for (Map.Entry<String, Wallet> entry: wallets.entrySet()) {
                // 加个\n，每个钱包名为一行
                byte[] bytes = (entry.getKey() + "\n").getBytes(StandardCharsets.UTF_8);
                outputStream.write(bytes);
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 先读入wallets，再读入每个wallet
    public void readFromData() {
        // 存所有wallet的name
        ArrayList<String> walletNames = new ArrayList<>();
        try {
            // 构造BufferReader来读取一行
            // 一个wallet的name为一行
            FileInputStream inputStream = new FileInputStream(WALLETS_DATA_PATH);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            // 局部变量line保存每行读取的钱包名
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                walletNames.add(line);
            }
            // 关闭所有输入流
            bufferedReader.close();
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 根据所有钱包的名字创建钱包
        if (!walletNames.isEmpty()) {
            for (String name: walletNames) {
                createWallet(name);
            }
        }
    }

    /**
     * @param wallet 钱包实例
     **/
    public void addWallet(Wallet wallet) {
        wallets.put(wallet.getWalletName(), wallet);
    }

    /**
     * @param wallet 钱包实例
     **/
    public void deleteWallet(Wallet wallet) {
        wallets.remove(wallet.getWalletName());
    }

    /**
     * @param walletName 钱包名字符串
     **/
    public void deleteWalletDirectory(String walletName) {
        String path = FileFactory.EXTERNAL_FILE_DIR + "/wallets/" + walletName + "/";
        FileManager.getInstance().deleteAllFiles(path);
    }

    /**
     * @param walletName 钱包名字符串
     * @return 钱包名对应的钱包实例
     **/
    public Wallet getWallet(String walletName) {
        return wallets.get(walletName);
    }
}
