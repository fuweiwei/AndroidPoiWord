package com.test.poiword.utils;

import android.database.Cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 将Cursor转换成对象和对象集合
 * Created by ${dzm} on 2015/9/8 0008.
 */

public class CursorUtils {

    public static Object cursorToBean(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        Object obj;
        try {
            obj = setValuesToFields(c, clazz);
            return obj;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("ERROR @：cursor2Bean");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 通过Cursor转换成对应的Bean集合。注意：Cursor里的字段名（可用别名）必须要和Bean的属性名一致
     * @param c
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List cursorToBeanList(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        List list = new LinkedList();
        Object obj;
        try {
            while (c.moveToNext()) {
                obj = setValuesToFields(c, clazz);
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR @：cursorToBeanList");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 把值设置进类属性里
     * @param c
     * @param clazz
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static Object setValuesToFields(Cursor c, Class clazz) throws Exception {

        String[] columnNames = c.getColumnNames();// 字段数组
        // init a instance from the Bean`s class
        Object obj = clazz.newInstance();
        // return a field array from obj`s ALL(include private exclude
        // inherite(from father)) field
        Field[] fields = clazz.getDeclaredFields();

        for (Field _field : fields) {
            // field`s type
            Class<? extends Object> typeClass = _field.getType();// 属性类型
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                typeClass = getBasicClass(typeClass);
                // if typeclass is basic class ,package.if not,no change
                boolean isBasicType = isBasicType(typeClass);

                if (isBasicType) {
                    if (columnName.equalsIgnoreCase(_field.getName())) {// 是基本类型
                        String _str = c.getString(c.getColumnIndex(columnName));
                        if (_str == null) {
                            break;
                        }
                        _str = _str == null ? "" : _str;
                        // if value is null,make it to ""
                        // use the constructor to init a attribute instance by
                        // the value
                        Constructor<? extends Object> cons = typeClass.getConstructor(String.class);
                        Object attribute = cons.newInstance(_str);
                        _field.setAccessible(true);
                        // give the obj the attr
                        _field.set(obj, attribute);
                        break;
                    }
                } else {
                    Object obj2 = setValuesToFields(c, typeClass);// 递归
                    _field.set(obj, obj2);
                    break;
                }

            }
        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        if (typeClass.equals(Integer.class) || typeClass.equals(Long.class) || typeClass.equals(Float.class) || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class) || typeClass.equals(Byte.class) || typeClass.equals(Short.class) || typeClass.equals(String.class)) {

            return true;

        } else {
            return false;
        }
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<Class, Class>();
    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }

}