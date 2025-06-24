package com.example.pre1;

import io.minio.MinioClient;
import okhttp3.OkHttpClient; // 1. 导入 OkHttpClient
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit; // 2. 导入 TimeUnit

@SpringBootApplication
public class Pre1Application {

    public static void main(String[] args) {
        SpringApplication.run(Pre1Application.class, args);
    }

    @Bean
    public MinioClient minioClient() {
        // 创建一个自定义的 OkHttpClient，并设置10秒的连接、写入和读取超时
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        // 在构建 MinioClient 时，使用我们自定义的 httpClient
        return MinioClient.builder()
                .endpoint("http://192.168.0.102:9000") // 继续使用我们之前确认的局域网IP
                .credentials("minioadmin", "minioadmin")
                .httpClient(httpClient) // <-- 应用这个带有超时设置的客户端
                .build();
    }
}