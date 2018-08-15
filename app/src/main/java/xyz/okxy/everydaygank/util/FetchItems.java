package xyz.okxy.everydaygank.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.okxy.everydaygank.bean.Item;

public class FetchItems {
    /**
     * 解析JSON并返回数据列表
     * @return List<Item>
     */
    public List<Item> Fetch(String categoryUrl, int page) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(categoryUrl + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JsonObject root = new JsonParser().parse(jsonString).getAsJsonObject();
            JsonArray resultsArray = root.getAsJsonArray("results");

            Gson gson = new Gson();
            List<Item> items = new ArrayList<>();
            for (JsonElement element:resultsArray) {
                Item item = gson.fromJson(element, Item.class);
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
