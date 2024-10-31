package ru.safiullina;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper(); // Создаем маппер
    public static <Stream> void main(String[] args) throws IOException {

        // Указываем конфигурацию
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                .build();

        // Конфигурируем клиента (создаем с помощью билдера)
        try(CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build()) {

            // Создаем запрос
            HttpGet httpGet = new HttpGet(URL);

            // Пытаемся выполнить запрос и получить ответ
            try(CloseableHttpResponse response = httpClient.execute(httpGet)) {

                // Сохраним поток ответа в массив байт
                byte[] responseByte = response.getEntity().getContent().readAllBytes();

                // Для распарсивания используем метод маппера readValue()
                List<CatsFacts> catsFactsList = mapper.readValue(
                        responseByte, new TypeReference<>(){});

                // Получаем поток из списка и выводим на экран
                catsFactsList.stream().
                        filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0).
                        forEach(System.out::println);

            }

        }

    }
}