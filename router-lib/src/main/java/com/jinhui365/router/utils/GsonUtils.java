package com.jinhui365.router.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Name:GsonUtils
 * Author:jmtian
 * Commemt:gson工具类
 * Date: 2017/8/23 14:31
 */


public class GsonUtils {
    private static Gson gson = null;

    private GsonUtils() {
    }

    static {
        if (null == gson) {
            gson = (new GsonBuilder())
                    .registerTypeHierarchyAdapter(Integer.class, new JsonIntAdapter())
                    .registerTypeHierarchyAdapter(Integer.TYPE, new GsonUtils.JsonIntAdapter())
                    .registerTypeHierarchyAdapter(Long.class, new GsonUtils.JsonLongAdapter())
                    .registerTypeHierarchyAdapter(Long.TYPE, new JsonLongAdapter())
                    .registerTypeHierarchyAdapter(Double.class, new JsonDoubleAdapter())
                    .registerTypeHierarchyAdapter(Double.TYPE, new JsonDoubleAdapter())
                    .registerTypeHierarchyAdapter(Float.class, new JsonFloatAdapter())
                    .registerTypeHierarchyAdapter(Float.TYPE, new JsonFloatAdapter())
                    .create();
        }

    }

    static class JsonFloatAdapter implements JsonDeserializer<Float>, JsonSerializer<Float> {
        JsonFloatAdapter() {
        }

        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Float.valueOf(json.getAsFloat());
            } catch (Exception var5) {
                return Float.valueOf(0.0F);
            }
        }

        public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(String.valueOf(src));
        }
    }

    static class JsonDoubleAdapter implements JsonDeserializer<Double>, JsonSerializer<Double> {
        JsonDoubleAdapter() {
        }

        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Double.valueOf(json.getAsDouble());
            } catch (Exception var5) {
                return Double.valueOf(0.0D);
            }
        }

        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(String.valueOf(src));
        }
    }

    static class JsonLongAdapter implements JsonDeserializer<Long>, JsonSerializer<Long> {
        JsonLongAdapter() {
        }

        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Long.valueOf(json.getAsLong());
            } catch (Exception var5) {
                return Long.valueOf(0L);
            }
        }

        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(String.valueOf(src));
        }
    }

    static class JsonIntAdapter implements JsonDeserializer<Integer>, JsonSerializer<Integer> {
        JsonIntAdapter() {
        }

        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Integer.valueOf(json.getAsInt());
            } catch (Exception var5) {
                return Integer.valueOf(0);
            }
        }

        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(String.valueOf(src));
        }
    }

    /**
     * 对象转化为json字符串
     *
     * @param ts
     * @return
     */
    public static String toJson(Object ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }

        return jsonStr;
    }

    /**
     * String解析成map<k,List<V>>
     *
     * @param reader
     * @param clz1
     * @param clz2
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, List<V>> jsonToMapList(String reader, Class<K> clz1, Class<V> clz2) {
        ParameterizedTypeImpl listType = new ParameterizedTypeImpl(List.class, new Class[]{clz2});
        ParameterizedTypeImpl mapType = new ParameterizedTypeImpl(Map.class, new Type[]{clz1, listType});
        try {
            return gson.fromJson(reader, mapType);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    /**
     * json解析成map
     *
     * @param reader
     * @param clz1
     * @param clz2
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> jsonToMap(String reader, Class<K> clz1, Class<V> clz2) {
        ParameterizedTypeImpl mapType = new ParameterizedTypeImpl(Map.class, new Class[]{clz1, clz2});

        try {
            return gson.fromJson(reader, mapType);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
