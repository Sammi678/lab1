import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class TextToDirectedGraph {

    public static void main(String[] args) {
        String filePath = "word.txt"; // 文件路径
        if (args.length > 0) {
            filePath = args[0]; // 如果启动程序时提供了文件路径参数，则使用参数中的路径
        } else {
            // 从用户输入中获取文件路径和文件名
            // 这里需要使用键盘输入的方式获取文件路径和文件名，例如使用Scanner类
        }

        Map<String, Map<String, Integer>> directedGraph = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }
            String text = sb.toString().toLowerCase().replaceAll("[^a-z\\s]", ""); // 将文本转换为小写并去除非字母字符

            String[] words = text.split("\\s+"); // 使用空格分割单词
            for (int i = 0; i < words.length - 1; i++) {
                String word1 = words[i];
                String word2 = words[i + 1];
                if (!directedGraph.containsKey(word1)) {
                    directedGraph.put(word1, new HashMap<>());
                }
                Map<String, Integer> neighbors = directedGraph.get(word1);
                neighbors.put(word2, neighbors.getOrDefault(word2, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 输出有向图
        for (String node : directedGraph.keySet()) {
            Map<String, Integer> neighbors = directedGraph.get(node);
            for (String neighbor : neighbors.keySet()) {
                int weight = neighbors.get(neighbor);
                System.out.println(node + " -> " + neighbor + ", weight: " + weight);
            }
        }

        //输出到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("directedGraph.txt"))) {
            for (String node : directedGraph.keySet()) {
                Map<String, Integer> neighbors = directedGraph.get(node);
                for (String neighbor : neighbors.keySet()) {
                    int weight = neighbors.get(neighbor);
                    writer.write(node + " -> " + neighbor + ", weight: " + weight);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}