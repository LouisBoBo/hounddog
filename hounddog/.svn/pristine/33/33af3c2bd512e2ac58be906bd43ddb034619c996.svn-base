package com.slxk.hounddog.mvp.utils;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class JsonUtils {

    public static RequestBody param2RequestBody(Map<String, String> params) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(params);
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
    }

    public static <P> RequestBody param2RequestBody(P params) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(params);
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
    }

    /**
     * 创建文件请求体
     *
     * @param files 文件路径
     * @return
     */
    public static List<MultipartBody.Part> createMultipartBody(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String path : files) {
            if (path == null) {
                continue;
            }
            File file = new File(path);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(body);
        }
        return parts;
    }

    /**
     * 创建文件请求体
     *
     * @param files 文件路径
     * @return
     */
    public static List<MultipartBody.Part> createMultipartBody_2(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String path : files) {
            if (path == null) {
                continue;
            }
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(body);
        }
        return parts;
    }

    /**
     * 创建文件请求体
     *
     * @param files 文件路径
     * @return
     */
    public static List<MultipartBody.Part> createMultipartBody_3(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String path : files) {
            if (path == null) {
                continue;
            }
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("videoFile", file.getName(), requestFile);
            parts.add(body);
        }
        return parts;
    }

    /**
     * 创建文件请求体
     *
     * @param files 文件路径
     * @return
     */
    public static List<MultipartBody.Part> createMultipartBody_4(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String path : files) {
            if (path == null) {
                continue;
            }
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("coverFile", file.getName(), requestFile);
            parts.add(body);
        }
        return parts;
    }

    /**
     * 创建文件请求体
     *
     * @param files 文件路径
     * @return
     */
    public static MultipartBody.Part createMultipartBody_5(List<String> files) {
        MultipartBody.Part parts = null;
        for (String path : files) {
            if (path == null) {
                continue;
            }
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody.create(MediaType.parse("image/png"), file);
            parts = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }
        return parts;
    }

    /**
     * 创建文件请求体
     *
     * @param path 文件路径
     * @return
     */
    public static MultipartBody.Part createMultipartBody_6(String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    /**
     * 创建文本请求体
     *
     * @param param
     * @return
     */
    public static RequestBody createRequestBody(String param) {
        return RequestBody.create(
                MediaType.parse("text/plain"), param);
    }
}
