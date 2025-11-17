package com.example.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合工具类
 */
public class CollectionUtils {

    private CollectionUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return true 表示为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 集合
     * @return true 表示不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断 Map 是否为空
     *
     * @param map Map 对象
     * @return true 表示为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断 Map 是否不为空
     *
     * @param map Map 对象
     * @return true 表示不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 获取集合的第一个元素
     *
     * @param collection 集合
     * @param <T>        元素类型
     * @return 第一个元素，如果集合为空则返回 null
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }

    /**
     * 获取集合的最后一个元素
     *
     * @param list 列表
     * @param <T>  元素类型
     * @return 最后一个元素，如果列表为空则返回 null
     */
    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 转换集合元素类型
     *
     * @param collection 源集合
     * @param mapper     转换函数
     * @param <T>        源元素类型
     * @param <R>        目标元素类型
     * @return 转换后的列表
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 过滤集合
     *
     * @param collection 源集合
     * @param predicate  过滤条件
     * @param <T>        元素类型
     * @return 过滤后的列表
     */
    public static <T> List<T> filter(Collection<T> collection, java.util.function.Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 将集合转换为 Map
     *
     * @param collection  源集合
     * @param keyMapper   键映射函数
     * @param valueMapper 值映射函数
     * @param <T>         元素类型
     * @param <K>         键类型
     * @param <V>         值类型
     * @return Map 对象
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
                                             Function<T, K> keyMapper,
                                             Function<T, V> valueMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v2));
    }

    /**
     * 将集合按键分组
     *
     * @param collection 源集合
     * @param keyMapper  键映射函数
     * @param <T>        元素类型
     * @param <K>        键类型
     * @return 分组后的 Map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> keyMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.groupingBy(keyMapper));
    }

    /**
     * 去重
     *
     * @param collection 源集合
     * @param <T>        元素类型
     * @return 去重后的列表
     */
    public static <T> List<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(new LinkedHashSet<>(collection));
    }

    /**
     * 求交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         元素类型
     * @return 交集
     */
    public static <T> List<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1) || isEmpty(collection2)) {
            return new ArrayList<>();
        }
        return collection1.stream()
                .filter(collection2::contains)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 求并集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         元素类型
     * @return 并集
     */
    public static <T> List<T> union(Collection<T> collection1, Collection<T> collection2) {
        Set<T> set = new LinkedHashSet<>();
        if (isNotEmpty(collection1)) {
            set.addAll(collection1);
        }
        if (isNotEmpty(collection2)) {
            set.addAll(collection2);
        }
        return new ArrayList<>(set);
    }

    /**
     * 求差集（collection1 - collection2）
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         元素类型
     * @return 差集
     */
    public static <T> List<T> subtract(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1)) {
            return new ArrayList<>();
        }
        if (isEmpty(collection2)) {
            return new ArrayList<>(collection1);
        }
        return collection1.stream()
                .filter(item -> !collection2.contains(item))
                .collect(Collectors.toList());
    }

    /**
     * 分页
     *
     * @param list     列表
     * @param pageNum  页码（从1开始）
     * @param pageSize 每页大小
     * @param <T>      元素类型
     * @return 分页后的列表
     */
    public static <T> List<T> page(List<T> list, int pageNum, int pageSize) {
        if (isEmpty(list) || pageNum < 1 || pageSize < 1) {
            return new ArrayList<>();
        }

        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= list.size()) {
            return new ArrayList<>();
        }

        int toIndex = Math.min(fromIndex + pageSize, list.size());
        return new ArrayList<>(list.subList(fromIndex, toIndex));
    }

    /**
     * 分批处理
     *
     * @param list      列表
     * @param batchSize 批次大小
     * @param <T>       元素类型
     * @return 分批后的列表
     */
    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        if (isEmpty(list) || batchSize < 1) {
            return new ArrayList<>();
        }

        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            partitions.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return partitions;
    }
}
