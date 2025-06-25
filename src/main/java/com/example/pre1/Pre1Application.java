package com.example.pre1;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Pre1Application {

    public static void main(String[] args) {
        SpringApplication.run(Pre1Application.class, args);
    }

    @Bean
    public MinioClient minioClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        return MinioClient.builder()
                // --- 修改部分：使用最标准的本机回环地址 127.0.0.1 ---
                .endpoint("http://127.0.0.1:9000")
                .credentials("minioadmin", "minioadmin")
                .httpClient(httpClient)
                .build();
    }
}
