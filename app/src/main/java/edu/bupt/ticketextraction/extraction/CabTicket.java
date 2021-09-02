package edu.bupt.ticketextraction.extraction;

import edu.bupt.ticketextraction.file.FileManager;
import org.jetbrains.annotations.Contract;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/06
 *     desc   : 出租车车票类，包含各种信息
 *     version:
 * </pre>
 */
public class CabTicket {
    double unitPrice;
    double money;
    double distance;
    String date;
    CabTicket(){
        unitPrice = 0.0;
        money = 0.0;
        distance = 0.0;
        date = "\0";
    }
    CabTicket(double unitPrice, double money, double distance, String date){
        this.unitPrice = unitPrice;
        this.money = money;
        this.distance = distance;
        this.date = date;
    }
    @Contract(pure = true)
    CabTicket(CabTicket rhs){
        this.unitPrice = rhs.unitPrice;
        this.money = rhs.money;
        this.distance = rhs.distance;
        this.date = rhs.date;
    }
}
