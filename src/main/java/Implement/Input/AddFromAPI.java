package Implement.Input;

import Implement.Input.SingleWord.DictionaryEntry;
import com.google.gson.Gson;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AddFromAPI {
  public interface Callback {
    void onSuccess(DictionaryEntry entry);
  }

  public void getWord(String str, Callback callback) {
    Thread thread = new Thread(() -> {
      HttpClient httpClient = HttpClients.createDefault();
      try {
        String encodedStr = URLEncoder.encode(str, StandardCharsets.UTF_8);
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + encodedStr;
        HttpGet httpGet = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(httpGet);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Gson gson = new Gson();
        if (jsonResponse.startsWith("[")) {
          DictionaryEntry[] entries = gson.fromJson(jsonResponse, DictionaryEntry[].class);
          Platform.runLater(() -> callback.onSuccess(entries[0]));
        } else {
          Platform.runLater(() -> callback.onSuccess(null));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    thread.start();
  }
}
