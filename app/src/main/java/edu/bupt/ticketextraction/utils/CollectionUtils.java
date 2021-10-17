package edu.bupt.ticketextraction.utils;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/17
 *     desc   : 集合工具类，提供一些集合的操作
 *     version: 0.0.1
 * </pre>
 */
public class CollectionUtils {
    /**
     * 替换LinkedHashMap的Key值，同时保持LinkedHashMap原有的顺序
     *
     * @param map    待替换的LinkedHashMap
     * @param oldKey 待替换的Key值
     * @param newKey 新的Key值
     * @param <K>    Key的类型
     * @param <V>    Value的类型
     * @return 替换后的LinkedHashMap
     */
    public static <K, V> @NotNull LinkedHashMap<K, V> replaceLinkedHashMapKey(@NotNull LinkedHashMap<K, V> map, K oldKey, K newKey) {
        LinkedHashMap<K, V> temp = new LinkedHashMap<>();
        V value = map.get(oldKey);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            // 找到目标Entry则插入新名称
            if (entry.getKey().equals(oldKey)) {
                temp.put(newKey, value);
                continue;
            }
            temp.put(entry.getKey(), entry.getValue());
        }
        return temp;
    }

    /**
     * @throws InstantiationException 实例化异常，该类不可实例化
     */
    private CollectionUtils() throws InstantiationException {
        throw new InstantiationException();
    }
}
