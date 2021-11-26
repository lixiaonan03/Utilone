package com.lxn.utilone.util.toolutil;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaonan
 * 功能描述: 集合工具类的
 * 时 间： 2019-11-08 15:15
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断是否为空
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.size() == 0;
    }

    /**
     * 判断是否非空
     *
     * @param coll
     * @return
     */
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    /**
     * 获取列表中的某一个位置的元素 异常返回null
     */
    public static <T> T objectAtIndex(List<T> list, int index) {
        try {
            if (isNotEmpty(list)) {
                if (index >= 0 && index < list.size()) {
                    return list.get(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断map为null
     *
     * @param map
     * @return
     */
    public static boolean isEmptyMap(Map map) {
        return map == null || map.size() <= 0;
    }

    /**
     * 判断map不为 null
     *
     * @param map
     * @return
     */
    public static boolean isNotEmptyMap(Map map) {
        return map != null && map.size() > 0;
    }

    /**
     * 获取map大小
     * @param map
     * @return
     */
    public static int mapSize(Map map) {
        if (map == null) {
            return 0;
        }
        return map.size();
    }

    /**
     * 根据同类型的数据 生成数组
     * @param array
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> T[] newArray(T... array) {
        return array;
    }

    /**
     * 判断数组是否为空
     * @param array The array.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmptyForArray(Object array) {
        return getLength(array) == 0;
    }

    /**
     * 获取数组长度
     * @param array The array.
     * @return the size of array
     */
    public static int getLength(Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
}
