package edu.bupt.ticketextraction.tickets;

import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/08
 *     desc   : 票据抽象基类
 *     version: 0.0.1
 * </pre>
 */
public abstract class AbstractTicket {
    protected final String WALLET_NAME;
    protected final String SOURCE_NAME;


    /**
     * @param walletName 钱包名
     * @param sourceName 资源文件名
     */
    protected AbstractTicket(@NotNull String walletName, @NotNull String sourceName) {
        this.WALLET_NAME = walletName;
        // 资源名只取文件名，不要目录名了
        int lastSlash = sourceName.lastIndexOf('/');
        this.SOURCE_NAME = sourceName.substring(lastSlash);
    }

    @SuppressWarnings("unused")
    public abstract static class Builder<T extends Builder<T>> {
        abstract AbstractTicket create();
    }

    protected abstract void writeToData();
}
