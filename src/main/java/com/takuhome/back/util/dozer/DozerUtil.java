package com.takuhome.back.util.dozer;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 将一个对象的属性复制到另一个对象或者list中
 * @Title:DozerUtil
 * @Author:NekoTaku
 * @Date:2022/05/23 10:25
 * @Version:1.0
 */


public class DozerUtil {

    private static Mapper mapper = new DozerBeanMapper();

    public static <T> List<T> transforList(List<?> sources, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (sources == null) {
            return list;
        }
        for (Object o : sources) {
            T t = transfor(o, clazz);
            list.add(t);
        }
        return list;
    }

    public static void transfor(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        mapper.map(source, target);
    }

    public static <T> T transfor(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        return mapper.map(source, target);
    }
}
