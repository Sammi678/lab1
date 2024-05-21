import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class TextToGraph {

    private static Map<String, Map<String, Integer>> graph;

    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java TextToGraph <filename>");
//            return;
//        }

        String filename = "word.txt";
        try {
            graph = buildGraph(filename);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请选择功能：");
                System.out.println("1. 展示有向图");
                System.out.println("2. 查询桥接词");
                System.out.println("3. 根据桥接词生成新文本");
                System.out.println("4. 计算两个单词之间的最短路径");
                System.out.println("5. 随机游走");
                System.out.println("6. 退出");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        showDirectedGraph(graph);
                        break;
                    case 2:
                        System.out.println("请输入两个单词，以空格分隔：");
                        String[] words = scanner.nextLine().split(" ");
                        String bridgeWords = queryBridgeWords(words[0], words[1]);
                        System.out.println(bridgeWords);
                        break;
                    case 3:
                        System.out.println("请输入新文本：");
                        String inputText = scanner.nextLine();
                        String newText = generateNewText(inputText);
                        System.out.println("生成的新文本是： " + newText);
                        break;
                    case 4:
                        System.out.println("请输入两个单词，以空格分隔：");
                        String[] shortestPathWords = scanner.nextLine().split(" ");
                        String shortestPath = calcShortestPath(shortestPathWords[0], shortestPathWords[1]);
                        System.out.println(shortestPath);
                        break;
                    case 5:
                        randomWalk();
                        break;
                    case 6:
                        System.out.println("感谢使用，再见！");
                        return;
                    default:
                        System.out.println("无效选择，请重新选择。");
                }
            }
        } catch (IOException e) {
            System.err.println("文件读取错误：" + e.getMessage());
        }
    }

    private static Map<String, Map<String, Integer>> buildGraph(String filename) throws IOException {
        Map<String, Map<String, Integer>> directedGraph = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
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
        return graph;
    }

    private static void showDirectedGraph(Map<String, Map<String, Integer>> graph) {
        System.out.println("展示有向图：");
        for (String node : graph.keySet()) {
            System.out.println(node + " -> " + graph.get(node));
        }
    }

    private static String queryBridgeWords(String word1, String word2) {
        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            return "No " + (graph.containsKey(word1) ? "word2" : "word1") + " in the graph!";
        }

        Set<String> bridgeWords = new HashSet<>();
        for (String bridge : graph.getOrDefault(word1, Collections.emptyMap()).keySet()) {
            if (graph.getOrDefault(bridge, Collections.emptyMap()).containsKey(word2)) {
                bridgeWords.add(bridge);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
        }
    }

    private static String generateNewText(String inputText) {
        StringBuilder newText = new StringBuilder();
        String[] words = inputText.split("[\\s\\p{Punct}]+");
        for (int i = 0; i < words.length - 1; i++) {
            newText.append(words[i]).append(" ");
            if (graph.containsKey(words[i]) && graph.containsKey(words[i + 1])) {
                Set<String> bridgeWords = new HashSet<>(graph.get(words[i]).keySet());
                bridgeWords.retainAll(graph.get(words[i + 1]).keySet());
                if (!bridgeWords.isEmpty()) {
                    List<String> bridgeList = new ArrayList<>(bridgeWords);
                    Collections.shuffle(bridgeList);
                    newText.append(bridgeList.get(0)).append(" ");
                }
            }
        }
        newText.append(words[words.length - 1]);
        return newText.toString();
    }

    private static String calcShortestPath(String word1, String word2) {
        // Implement shortest path calculation here
        return "Shortest path from " + word1 + " to " + word2 + " is: " + word1 + " -> ... -> " + word2;
    }

    private static void randomWalk() {
        // Implement random walk functionality here
    }
}