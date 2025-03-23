package com.heima.account.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.modules.po.VehiclePO;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class AiHelper {
    @Value("${baidu.apikey}")
    private String API_KEY;
    @Value("${baidu.secretkey}")
    private String SECRET_KEY;

    private final static Logger logger = LoggerFactory.getLogger(AiHelper.class);

    final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();


    public static void main(String []args) throws IOException {
        String code = new AiHelper().getLicense(null);
        System.out.println(code);
    }

    /*

    图像识别，获取车牌信息
    文档（行驶证识别）：https://cloud.baidu.com/doc/OCR/s/yk3h7y3ks
    文档（车牌识别）：https://cloud.baidu.com/doc/OCR/s/ck3h7y191
    获取车辆照片url
    将url下载到某个临时文件夹
    将文件编码为base64
    调百度AI接口，返回对应信息
    对比：行驶证车牌 和 车辆车牌是否一致
    如果一致，设置车牌信息，认证通过，身份变更为车主

    简化版业务流程（至少完成）：识别车辆车牌号即可

    * */
    public String getLicense(VehiclePO vehiclePO) throws IOException {
        if (vehiclePO == null || vehiclePO.getCarFrontPhoto() == null || vehiclePO.getCarFrontPhoto() == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "Vehicle photo or license photo is missing.");
        }

        String vehicleImageUrl = vehiclePO.getCarFrontPhoto();   // URL of vehicle plate photo
        String licenseImageUrl = vehiclePO.getCarBackPhoto();   // URL of license card photo

        logger.info("Start recognizing vehicle info. Vehicle image: {}, License image: {}", vehicleImageUrl, licenseImageUrl);

        String accessToken = getAccessToken();

        String plateFromPhoto = recognizeLicensePlate(vehicleImageUrl, accessToken);
        String plateFromLicense = recognizeVehicleLicense(licenseImageUrl, accessToken);

        logger.info("License plate recognition result: photo={}, license={}", plateFromPhoto, plateFromLicense);
        System.out.println(">>> DEBUG: plateFromPhoto=" + plateFromPhoto + ", plateFromLicense=" + plateFromLicense);

        if (plateFromPhoto != null && plateFromPhoto.equalsIgnoreCase(plateFromLicense)) {
            logger.info("License plate matched. Authentication passed: {}", plateFromPhoto);
            return plateFromPhoto;
        } else {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR, "License plate mismatch. Authentication failed.");
        }
    }

    private String getAccessToken() throws IOException {
        String url = "https://aip.baidubce.com/oauth/2.0/token" +
                "?grant_type=client_credentials" +
                "&client_id=" + API_KEY +
                "&client_secret=" + SECRET_KEY;

        Request request = new Request.Builder().url(url).get().build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to obtain access token from Baidu OCR.");
            }
            String result = response.body().string();
            JSONObject json = new JSONObject(result);
            return json.getString("access_token");
        }
    }

    private String recognizeLicensePlate(String imageUrl, String accessToken) throws IOException {
        File imageFile = downloadImage(imageUrl);
        String base64 = encodeToBase64(imageFile);
        imageFile.delete();

        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate?access_token=" + accessToken;

        RequestBody body = new FormBody.Builder().add("image", base64).build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            String result = response.body().string();
            JsonNode json = new ObjectMapper().readTree(result);
            return json.path("words_result").path("number").asText(null);
        }
    }

    private String recognizeVehicleLicense(String imageUrl, String accessToken) throws IOException {
        File imageFile = downloadImage(imageUrl);
        String base64 = encodeToBase64(imageFile);
        imageFile.delete();

        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license?access_token=" + accessToken;

        RequestBody body = new FormBody.Builder().add("image", base64).build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            String result = response.body().string();
            JsonNode json = new ObjectMapper().readTree(result);
            return json.path("words_result").path("号牌号码").path("words").asText(null);
        }
    }

    private File downloadImage(String url) throws IOException {
        File file = File.createTempFile("img_", ".jpg");
        FileUtils.copyURLToFile(new URL(url), file);
        return file;
    }

    private String encodeToBase64(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(bytes);
    }

}
