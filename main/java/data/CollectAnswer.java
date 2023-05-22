package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;

public class CollectAnswer {

    public static void main(String[] args) {
        try {
            URL url = new URL("https://api.stackexchange.com/2.3/questions/" +
                    "75038793;75036298;75023857;75022618;75020008;74994090;74988742;74985434;74974924;76236171;75967075;76213908;76187166;76183642;76157424;76148273;76134623;76119036;76113041;76105303;76102308;76094246;76070870;76070173;76069107;76067944;76063023;76054136;76052119;76036487;76023279;76022012;76001878;75990087;75988380;75985676;75977751;75954609;75948434;75944653;75942564;75928987;75928132;75897357;75893162;75886278;75880974;75866135;75866006;75851642;75838826;75805557;75798722;75763522;75751602;75748062;75710932;75682740;75681703;75672918;75651209;75650768;75648837;75647321;75647244;75644225;75642109;75641863;75626057;75604960;75596353;75589041;75583755;75539595;75500805;75494536;75489154;75486574;75477445;75467915;75467680;75465091;75463790;75463524;75437563;75426631;75416884;75414437;75413742;75409545;75393258;75392754;75378517;75360572;75358262;75348362;75325675;75311178;75307153;75304413"+
                    "/answers?pagesize=100&order=desc&sort=activity&site=stackoverflow&filter=!nOedRLgcx)");

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
                String filePath = "C:\\Users\\cheny\\IdeaProjects\\2023-Spring-Java2-Project-Demo-master\\src\\main\\java\\data\\answers\\" +
                        "answer_4.json";
                saveJsonToFile(jsonResponse, filePath);
                System.out.println("JSON保存成功！");
            } else {
                System.out.println("请求失败，响应代码: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void saveJsonToFile(String json, String filePath) throws IOException {
        Path path = Path.of(filePath);
        BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
        writer.write(json);
        writer.close();
    }

    private static String readJsonFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        reader.close();
        return content.toString();
    }

}
