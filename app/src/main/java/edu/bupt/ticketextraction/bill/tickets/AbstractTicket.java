package edu.bupt.ticketextraction.bill.tickets;

import edu.bupt.ticketextraction.utils.file.Writable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/08
 *     desc   : 票据抽象基类
 *              实现Serializable接口可在Activity间传递
 *     version: 0.0.1
 * </pre>
 */
@SuppressWarnings("unused")
public abstract class AbstractTicket implements Serializable, Writable {
    protected final String WALLET_NAME;
    protected final String SOURCE_NAME;
    protected final String TICKET_TYPE;
    protected final String TICKET_NUMBER;
    protected final String TICKET_CODE;

    /**
     * @param walletName   钱包名
     * @param sourceName   资源文件名
     * @param ticketType   发票类型
     * @param ticketNumber 发票号码
     * @param ticketCode   发票代码
     */
    protected AbstractTicket(@NotNull String walletName,
                             @NotNull String sourceName,
                             @NotNull String ticketType,
                             @NotNull String ticketNumber,
                             @NotNull String ticketCode) {
        this.WALLET_NAME = walletName;
        this.TICKET_TYPE = ticketType;
        // 资源名只取文件名，不要目录名了
        int lastSlash = sourceName.lastIndexOf('/');
        this.SOURCE_NAME = sourceName.substring(lastSlash);
        this.TICKET_NUMBER = ticketNumber;
        this.TICKET_CODE = ticketCode;
    }

    public String getSourceName() {
        return SOURCE_NAME;
    }

    public String getWalletName() {
        return WALLET_NAME;
    }

    public String getTicketNumber() {
        return TICKET_NUMBER;
    }

    public String getTicketCode() {
        return TICKET_CODE;
    }

    public String getTicketType() {
        return TICKET_TYPE;
    }

    /**
     * 发票构造器抽象基类
     */
    @SuppressWarnings("unused")
    public abstract static class Builder<T extends Builder<T>> {
        abstract AbstractTicket create();
    }
}
