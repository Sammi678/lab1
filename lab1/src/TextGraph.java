import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class TextGraph {
    private static Map<String, Map<String, Integer>> graph = new HashMap<>();
    private static Map<String, Set<String>> bridgeWords = new HashMap<>();
    public static void main(String[] args) {
        String fileName = "word.txt";
//        String inputText = "Seek to explore new and exciting synergies";
//        if (args.length > 0) {
//            fileName = args[0];
//        }
        generateDirectedGraph(fileName);
        try {

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
                        Map<String, List<String>> adjacencyList = convertToAdjacencyList(graph);
                        drawDirectedGraph(adjacencyList, "directed_graph.svg");
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
//                        String shortestPath = calcShortestPath("to", "and");
//                        System.out.println(shortestPath);
                        System.out.println("请输入两个单词，以空格分隔：");
                        String[] shortestPathWords = scanner.nextLine().split(" ");
                        String shortestPath = calcShortestPath(shortestPathWords[0], shortestPathWords[1]);
                        System.out.println(shortestPath);
                        break;
                    case 5:
                        String randomWalkResult = randomWalk();
                        System.out.println(randomWalkResult);
                        break;
                    case 6:
                        System.out.println("感谢使用，再见！");
                        return;
                    default:
                        System.out.println("无效选择，请重新选择。");
                }
            }

        }finally {

        }
    }



    private static void generateDirectedGraph(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase().replaceAll("[^a-z ]", " ");
                String[] words = line.split("\\s+");
                System.out.println("Words array: " + Arrays.toString(words));
                for (int i = 0; i < words.length - 1; i++) {
                    String word1 = words[i];
                    String word2 = words[i + 1];
                    graph.putIfAbsent(word1, new HashMap<>());
                    graph.get(word1).put(word2, graph.get(word1).getOrDefault(word2, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showDirectedGraph(Map<String, Map<String, Integer>> graph) {
        for (String node : graph.keySet()) {
            System.out.print(node + " -> ");
            Map<String, Integer> edges = graph.get(node);
            for (String neighbor : edges.keySet()) {
                System.out.print(neighbor + "(" + edges.get(neighbor) + "), ");
            }
            System.out.println();
        }
    }

    private static String queryBridgeWords(String word1, String word2) {
        StringBuilder result = new StringBuilder("Bridge words between \"" + word1 + "\" and \"" + word2 + "\": ");
        if (!graph.containsKey(word1) && !graph.containsKey(word2)) {
            return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
        } else if (!graph.containsKey(word1)) {
            return "No \"" + word1 + "\" in the graph!";
        } else if (!graph.containsKey(word2)) {
            return "No \"" + word2 + "\" in the graph!";
        }

        Set<String> bridgeWords = new HashSet<>();
        for (String neighbor : graph.get(word1).keySet()) {
            if (graph.containsKey(neighbor) && graph.get(neighbor).containsKey(word2)) {
                bridgeWords.add(neighbor);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from \"" +  word1 + "\" to \"" + word2 + "\"!";
        }
        String[] wordsArray = bridgeWords.toArray(new String[0]);
        String bridgeword = wordsArray[0];
        result.append(bridgeword);
        return result.toString();
    }

    private static String generateNewText(String inputText) {
        StringBuilder newText = new StringBuilder();
        String[] words = inputText.toLowerCase().replaceAll("[^a-z ]", " ").split("\\s+");
        Random random = new Random();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            newText.append(word1).append(" ");
            if (graph.containsKey(word1) && graph.containsKey(word2)) {
                Set<String> bridgeWords = new HashSet<>();
                for (String neighbor : graph.get(word1).keySet()) {
                    if (graph.containsKey(neighbor) && graph.get(neighbor).containsKey(word2)) {
                        bridgeWords.add(neighbor);
                    }
                }
                if (!bridgeWords.isEmpty()) {
                    String[] wordsArray = bridgeWords.toArray(new String[0]);
                    String bridgeword = wordsArray[0];
                    newText.append(bridgeword).append(" ");
                }
            }
        }
        newText.append(words[words.length - 1]);
        return newText.toString();
    }

    private static Map<String, List<String>> convertToAdjacencyList(Map<String, Map<String, Integer>> graph) {
        Map<String, List<String>> adjacencyList = new HashMap<>();
        for (String node : graph.keySet()) {
            List<String> neighbors = new ArrayList<>();
            for (String neighbor : graph.get(node).keySet()) {
                neighbors.add(neighbor);
            }
            adjacencyList.put(node, neighbors);
        }
        System.out.println(adjacencyList);
        return adjacencyList;
    }

    public static void drawDirectedGraph(Map<String, List<String>> adjacencyList, String filePath) {
        StringBuilder svgContent = generateSVG(adjacencyList);
        writeSVGToFile(svgContent.toString(), filePath);
    }
    private static StringBuilder generateSVG(Map<String, List<String>> adjacencyList) {
        StringBuilder svgContent = new StringBuilder();
        svgContent.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"600\" height=\"400\">\n");

        // Define the arrowhead
        svgContent.append("<defs>\n");
        svgContent.append("<marker id=\"arrow\" markerWidth=\"10\" markerHeight=\"10\" refX=\"5\" refY=\"2\" orient=\"auto\">\n");
        svgContent.append("<path d=\"M0,0 L0,4 L6,2 z\" fill=\"black\" />\n");
        svgContent.append("</marker>\n");
        svgContent.append("</defs>\n");

        Map<String, Point> nodePositions = new HashMap<>();
        Random rand = new Random();
        for (String node : adjacencyList.keySet()) {
            Point position = nodePositions.get(node);
            if (position == null) { // Check if position is not already set
                int x = rand.nextInt(500) + 50; // Random x position within 50-550
                int y = rand.nextInt(300) + 50; // Random y position within 50-350
                Point newNodePosition = new Point(x, y);
                nodePositions.put(node, newNodePosition);
//                System.out.println("Node: " + node + ", Position: (" + x + ", " + y + ")");
                // Draw circle
                svgContent.append(String.format("<circle cx=\"%d\" cy=\"%d\" r=\"1\" fill=\"black\"/>\n", x, y));

                // Draw text label
                svgContent.append(String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-size=\"12\" text-anchor=\"middle\">%s</text>\n", x, y - 10, node));

            }
            List<String> neighbors = adjacencyList.get(node);
            for (String neighbor : neighbors) {
                Point neighborPosition = nodePositions.get(neighbor);
                if (neighborPosition == null) { // Check if neighbor position is not already set
                    int x = rand.nextInt(500) + 50; // Random x position within 50-550
                    int y = rand.nextInt(300) + 50; // Random y position within 50-350
                    Point newNeighborPosition = new Point(x, y);
                    nodePositions.put(neighbor, newNeighborPosition);
//                    System.out.println("Neighbor of " + node + ": " + neighbor + ", Position: (" + x + ", " + y + ")");
                    // Draw circle
                    svgContent.append(String.format("<circle cx=\"%d\" cy=\"%d\" r=\"1\" fill=\"black\"/>\n", x, y));

                    // Draw text label
                    svgContent.append(String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-size=\"12\" text-anchor=\"middle\">%s</text>\n", x, y - 10, neighbor));

                }
            }
        }

        // Draw nodes
        for (String node : adjacencyList.keySet()) {
            Point nodePosition = nodePositions.get(node);
            if (nodePosition != null) {
                int nodeX = (int) nodePosition.getX();
                int nodeY = (int) nodePosition.getY();

                // Print node name and position
//                System.out.println("Node: " + node + ", Position: (" + nodeX + ", " + nodeY + ")");

                // Draw circle
                svgContent.append(String.format("<circle cx=\"%d\" cy=\"%d\" r=\"1\" fill=\"black\"/>\n", nodeX, nodeY));

                // Draw text label
                svgContent.append(String.format("<text x=\"%d\" y=\"%d\" fill=\"black\" font-size=\"12\" text-anchor=\"middle\">%s</text>\n", nodeX, nodeY - 10, node));
            }
        }


        // Draw edges
        for (String node : adjacencyList.keySet()) {
            Point nodePosition = nodePositions.get(node);
            if (nodePosition != null) {
                int nodeX = (int) nodePosition.getX();
                int nodeY = (int) nodePosition.getY();

                List<String> neighbors = adjacencyList.get(node);
                for (String neighbor : neighbors) {
                    Point neighborPosition = nodePositions.get(neighbor);
                    if (neighborPosition != null) {
                        int neighborX = (int) neighborPosition.getX();
                        int neighborY = (int) neighborPosition.getY();

                        // Draw edge line with arrowhead
                        svgContent.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\" marker-end=\"url(#arrow)\"/>\n", nodeX, nodeY, neighborX, neighborY));
                    }
                }
            }
        }

        svgContent.append("</svg>");
        return svgContent;
    }

    private static void writeSVGToFile(String svgContent, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(svgContent);
            System.out.println("SVG file saved at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static String calcShortestPath(String word1, String word2) {
//        Map<String, List<String>> adjacencyList = convertToAdjacencyList(graph);
//        Queue<String> queue = new LinkedList<>();
//        Set<String> visited = new HashSet<>();
//        Map<String, String> parentMap = new HashMap<>();
//
//        queue.offer(word1);
//        visited.add(word1);
//
//        while (!queue.isEmpty()) {
//            String current = queue.poll();
//            if (current.equals(word2)) {
//                break;
//            }
//            List<String> neighbors = adjacencyList.get(current);
//            if (neighbors != null) {
//                for (String neighbor : neighbors) {
//                    if (!visited.contains(neighbor)) {
//                        visited.add(neighbor);
//                        parentMap.put(neighbor, current);
//                        queue.offer(neighbor);
//                    }
//                }
//            }
//        }
//
//        if (!parentMap.containsKey(word2)) {
//            return "The words '" + word1 + "' and '" + word2 + "' are not reachable in the graph!";
//        }
//        StringBuilder shortestPathStr = new StringBuilder();
//        String node = word2;
//        while (node != null) {
//            shortestPathStr.insert(0, node);
//            node = parentMap.get(node);
//            if (node != null) {
//                shortestPathStr.insert(0, "->");
//            }
//        }
//
//        return "The shortest path from '" + word1 + "' to '" + word2 + "' is: " + shortestPathStr.toString();
//    }


    private static String calcShortestPath(String word1, String word2) {
        Map<String, List<String>> adjacencyList = convertToAdjacencyList(graph);
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.offer(word1);
        visited.add(word1);
        distance.put(word1, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(word2)) {
                break;
            }
            List<String> neighbors = adjacencyList.get(current);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parentMap.put(neighbor, current);
                        queue.offer(neighbor);
                        distance.put(neighbor, distance.get(current) + graph.get(current).get(neighbor));
                    }
                }
            }
        }

        if (!parentMap.containsKey(word2)) {
            return "The words '" + word1 + "' and '" + word2 + "' are not reachable in the graph!";
        }

        StringBuilder shortestPathStr = new StringBuilder();
        String node = word2;
        int totalDistance = distance.get(word2);
        while (node != null) {
            shortestPathStr.insert(0, node);
            node = parentMap.get(node);
            if (node != null) {
                shortestPathStr.insert(0, "->");
            }
        }

        return "The shortest path from '" + word1 + "' to '" + word2 + "' is: " + shortestPathStr.toString() + ". Total distance: " + totalDistance;
    }

    private static String randomWalk() {
        List<String> nodes = new ArrayList<>(graph.keySet());
        if (nodes.isEmpty()) {
            return "The graph is empty!";
        }
        String currentNode = nodes.get(new Random().nextInt(nodes.size()));
//        String currentNode = "to";
        System.out.println("currentNode = " + currentNode);
        Set<String> visitedEdges = new HashSet<>();
        StringBuilder walkResult = new StringBuilder();
        walkResult.append(currentNode);

        while (true) {
            Map<String, Integer> neighbors = graph.get(currentNode);
            if (neighbors == null || neighbors.isEmpty()) {
                break;
            }

            List<String> neighborList = new ArrayList<>(neighbors.keySet());
            String nextNode = neighborList.get(new Random().nextInt(neighborList.size()));
            String edge = currentNode + "->" + nextNode;

            if (visitedEdges.contains(edge)) {
                break;
            }

            visitedEdges.add(edge);
            walkResult.append(" ").append(nextNode);
            currentNode = nextNode;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("random_walk.txt"))) {
            writer.write(walkResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Random walk result: " + walkResult.toString();
    }
}