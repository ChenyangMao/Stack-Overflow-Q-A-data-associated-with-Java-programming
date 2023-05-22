package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;
import org.json.JSONObject;

public class CollectQuestion {
    public static void main(String[] args) {
            try {
                for(int i=1;i<11;i++) {
                    URL url = new URL("https://api.stackexchange.com/2.3/questions?page="+
                            i
                            +"&pagesize=100&fromdate=1672531200&todate=1684454400&order=desc&sort=votes&tagged=java&" +
                            "site=stackoverflow&filter=!-l647(J2Okb-r23gPqEQLcawHQdIOeESGeGQXWEbjlbdV0jT.BE5Ci");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 设置压缩编码为gzip
                    connection.setRequestProperty("Accept-Encoding", "gzip");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader;
                        String encoding = connection.getContentEncoding();
                        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
                            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
                        } else {
                            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        }

                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        String jsonResponse = response.toString();

                        // 在这里处理JSON响应
                        JSONObject json = new JSONObject(jsonResponse);
//                System.out.println(json.length());
                        String filePath = "C:\\Users\\cheny\\IdeaProjects\\2023-Spring-Java2-Project-Demo-master\\src\\main\\java\\data\\questions\\" +
                                "question_"+i+".json";
                        saveJsonToFile(jsonResponse, filePath);
                        System.out.println("question_"+i+".json 保存成功！");
                    } else {
                        System.out.println("请求失败，响应代码: " + responseCode);
                    }

                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void saveJsonToFile (String json, String filePath) throws IOException {
            Path path = Path.of(filePath);
            BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
            writer.write(json);
            writer.close();
        }

}
