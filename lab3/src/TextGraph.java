import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class TextGraph {
    private static Map<String, Map<String, Integer>> graph = new HashMap<>();
    private static Map<String, Set<String>> bridgeWords = new HashMap<>();
    private static boolean stopRandomWalk = false;

    public static void main(String[] args) {
        String fileName = "word.txt";
        String[] textwords = generateDirectedGraph(fileName);
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("请选择功能：");
                System.out.println("1. 展示有向图");
                System.out.println("2. 查询桥接词");
                System.out.println("3. 根据桥接词生成新文本");
                System.out.println("4. 计算两个单词之间的最短路径");
                System.out.println("5. 随机游走");
                System.out.println("6. 退出");

                int choice = 0;
                boolean validInput = false;

                while (!validInput) {
                    System.out.print("输入您的选择：");
                    String input = scanner.nextLine();
                    try {
                        choice = Integer.parseInt(input);
                        if (choice >= 1 && choice <= 6) {
                            validInput = true;
                        } else {
                            System.out.println("无效选择，请输入1到6之间的数字。");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("无效输入，请输入一个数字。");
                    }
                }
                switch (choice) {
                    case 1:
                        showDirectedGraph(graph);
                        Map<String, List<String>> adjacencyList = convertToAdjacencyList(graph);
                        drawDirectedGraph(adjacencyList, "directed_graph.svg", textwords);
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
                        System.out.println("请输入单词，以空格分隔：");
                        String[] shortestPathWords = scanner.nextLine().split(" ");
                        int numWords = shortestPathWords.length;
                        if (numWords == 0) {
                            System.out.println("请输入至少一个单词。");
                            break;
                        } else if (numWords > 2) {
                            System.out.println("请输入最多两个单词。");
                            break;
                        }
                        String shortestPath = calcShortestPath(shortestPathWords[0], numWords == 2 ? shortestPathWords[1] : null, textwords);
                        System.out.println(shortestPath);
                        break;
                    case 5:
                        stopRandomWalk = false;
                        String randomWalkResult = randomWalk();
                        System.out.println(randomWalkResult);
                        break;
                    case 6:
                        System.out.println("感谢使用，再见！");
                        return;
                    default:
                        System.out.println("无效选择，请重新选择。");
                        break;
                }
            }
        }
    }

    private static String[] generateDirectedGraph(String fileName) {
        String[] textwords = new String[50];;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(" ");
            }
            String text = sb.toString().toLowerCase().replaceAll("[^a-z\\s]", ""); // 将文本转换为小写并去除非字母字符

            String[] words = text.split("\\s+"); // 使用空格分割单词
//            System.out.println(text);
            for (int i = 0; i < words.length - 1; i++) {
//                System.out.println(words[i]);
                String word1 = words[i];
                String word2 = words[i + 1];
                graph.putIfAbsent(word1, new HashMap<>());
                graph.get(word1).put(word2, graph.get(word1).getOrDefault(word2, 0) + 1);
            }
            graph.putIfAbsent(words[words.length-1], new HashMap<>());  //加入最后一个单词
            textwords = words;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textwords;
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

    public static void drawDirectedGraph(Map<String, List<String>> adjacencyList, String filePath,String[] textwords) {
        StringBuilder svgContent = generateSVG(adjacencyList, textwords);
        writeSVGToFile(svgContent.toString(), filePath);
    }

    private static StringBuilder generateSVG(Map<String, List<String>> adjacencyList, String[] textwords) {
        // 调用方法计算节点位置
        Map<String, Point> nodePositions = new HashMap<>();

        int startY = 100; // 起始 Y 坐标
        int deltaX = 100; // 水平间距
        int avgX = 500; // 画布中央 X 坐标
        int offsetY = 50; // 垂直间距

        // 外层循环按照数组顺序逐个处理节点
        for (String word : textwords) {
            // 判断节点是否已经有位置
            Point nodePosition0 = nodePositions.get(word);
            if (nodePosition0 == null) {
                Queue<String> queue = new LinkedList<>();
                queue.offer(word);
                // 确定当前节点的位置
                int nodeX = avgX; // 节点的 X 坐标为画布中点的平均值
                int nodeY = startY; // 节点的 Y 坐标为 startY
                nodePositions.put(word, new Point(nodeX, nodeY));
                while (!queue.isEmpty()) {
                    List<String> neighbors = new ArrayList<>();
                    while(!queue.isEmpty()){
                        String node = queue.poll();
                        List<String> temp = adjacencyList.get(node);
                        if (temp != null) {
                            neighbors.addAll(temp); // 将当前节点的邻接点添加到列表
                        }
                    }
                    nodeY += offsetY; // 调整 Y 坐标，确保相邻节点的 Y 坐标相同
//                    System.out.println(neighbors);
                    if (neighbors != null) {
                        int i=1;
                        for (String neighbor : neighbors) {
                            if (!nodePositions.containsKey(neighbor)) {
                                queue.offer(neighbor);
                                nodeX = avgX - deltaX * (neighbors.size()/2 - i);
                                nodePositions.put(neighbor, new Point(nodeX, nodeY));
                            }
                            i++;
                        }
                    }
                }
            }
        }
        System.out.println(nodePositions);
        StringBuilder svgContent = new StringBuilder();
        svgContent.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10000\" height=\"10000\">\n");

        // Define the arrowhead
        svgContent.append("<defs>\n");
        svgContent.append("<marker id=\"arrow\" markerWidth=\"10\" markerHeight=\"10\" refX=\"5\" refY=\"2\" orient=\"auto\">\n");
        svgContent.append("<path d=\"M0,0 L0,4 L6,2 z\" fill=\"black\" />\n");
        svgContent.append("</marker>\n");
        svgContent.append("</defs>\n");

        for (String node : textwords) {
            Point nodePosition = nodePositions.get(node);
            if (nodePosition != null) {
                int nodeX = (int) nodePosition.getX();
                int nodeY = (int) nodePosition.getY();
                // Calculate the text length and set the ellipse size accordingly
                int textLength = node.length();
                int ellipseWidth = textLength * 7; // Approximate width per character
                int ellipseHeight = 20; // Fixed height for the ellipse

                // Draw the ellipse
                svgContent.append(String.format("<ellipse cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\" fill=\"none\" stroke=\"blue\"/>\n", nodeX, nodeY, ellipseWidth / 2, ellipseHeight / 2));

                // Draw the text inside the ellipse
                svgContent.append(String.format("<text x=\"%d\" y=\"%d\" fill=\"blue\" font-size=\"12\" text-anchor=\"middle\" alignment-baseline=\"middle\">%s</text>\n", nodeX, nodeY, node));
            }
        }

        for (String node : adjacencyList.keySet()) {
            Point nodePosition = nodePositions.get(node);
            if (nodePosition != null) {
                int nodeX = (int) nodePosition.getX();
                int nodeY = (int) nodePosition.getY();

                int textLength = node.length();
                int rx = textLength * 7 / 2; // Approximate width per character / 2
                int ry = 10; // Fixed height for the ellipse / 2

                List<String> neighbors = adjacencyList.get(node);
                for (String neighbor : neighbors) {
                    Point neighborPosition = nodePositions.get(neighbor);
                    if (neighborPosition != null) {
                        int neighborX = (int) neighborPosition.getX();
                        int neighborY = (int) neighborPosition.getY();

                        // Calculate angle between nodes
                        double angle = Math.atan2(neighborY - nodeY, neighborX - nodeX);

                        // Calculate start and end points on the ellipse boundary
                        int startX0 = (int) (nodeX + rx * Math.cos(angle));
                        int startY0 = (int) (nodeY + ry * Math.sin(angle));

                        int neighborTextLength = neighbor.length();
                        int neighborRx = neighborTextLength * 7 / 2;
                        int neighborRy = 10;

                        int endX = (int) (neighborX - neighborRx * Math.cos(angle));
                        int endY = (int) (neighborY - neighborRy * Math.sin(angle));

//                         Calculate control points for curved edges
                        int controlX1 = (int) (startX0 + 0.5 * (endX - startX0) - 0.1 * (endY - startY0));
                        int controlY1 = (int) (startY0 + 0.5 * (endY - startY0) + 0.1 * (endX - startX0));
                        int controlX2 = (int) (endX - 0.5 * (endX - startX0) + 0.1 * (endY - startY0));
                        int controlY2 = (int) (endY - 0.5 * (endY - startY0) - 0.1 * (endX - startX0));

                        // Draw curved edge with arrowhead
                        svgContent.append(String.format("<path d=\"M%d,%d C%d,%d %d,%d %d,%d\" fill=\"none\" stroke=\"black\" marker-end=\"url(#arrow)\"/>\n", startX0, startY0, controlX1, controlY1, controlX2, controlY2, endX, endY));
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
    private static String calcShortestPath(String word1, String word2, String[] textwords) {
        Map<String, List<String>> adjacencyList = convertToAdjacencyList(graph);
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.offer(word1);
        visited.add(word1);
        distance.put(word1, 0);

        // 随机选择一个节点作为word2
        Random random = new Random();
        List<String> nodes = new ArrayList<>(graph.keySet());
        String randomNode = nodes.get(random.nextInt(nodes.size()));
        if (word2 == null) {
            word2 = randomNode;
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
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

        // 构建最短路径
        List<String> shortestPath = new ArrayList<>();
        String node = word2;
        while (node != null) {
            shortestPath.add(0, node);
            node = parentMap.get(node);
        }
        String[] pathword = shortestPath.toArray(new String[0]);

        StringBuilder svgContent = ShortPathSVG(adjacencyList, textwords, pathword);
        writeSVGToFile(svgContent.toString(), "shortpath.svg");
        // 构造并返回结果字符串
        StringBuilder result = new StringBuilder();
        result.append("The shortest path from '").append(word1).append("' to '").append(word2).append("' is: ");
        for (int i = 0; i < shortestPath.size(); i++) {
            result.append(shortestPath.get(i));
            if (i < shortestPath.size() - 1) {
                result.append(" -> ");
            }
        }
        result.append(". Total distance: ").append(distance.get(word2));
        return result.toString();
    }

    private static StringBuilder ShortPathSVG(Map<String, List<String>> adjacencyList, String[] textwords, String[] shortpath) {
        // 调用方法计算节点位置
        Map<String, Point> nodePositions = new HashMap<>();

        int startY = 100; // 起始 Y 坐标
        int deltaX = 100; // 水平间距
        int avgX = 500; // 画布中央 X 坐标
        int offsetY = 50; // 垂直间距

        // 外层循环按照数组顺序逐个处理节点
        for (String word : textwords) {
            // 判断节点是否已经有位置
            Point nodePosition0 = nodePositions.get(word);
            if (nodePosition0 == null) {
                Queue<String> queue = new LinkedList<>();
                queue.offer(word);
                // 确定当前节点的位置
                int nodeX = avgX; // 节点的 X 坐标为画布中点的平均值
                int nodeY = startY; // 节点的 Y 坐标为 startY
                nodePositions.put(word, new Point(nodeX, nodeY));
                while (!queue.isEmpty()) {
                    List<String> neighbors = new ArrayList<>();
                    while(!queue.isEmpty()){
                        String node = queue.poll();
                        List<String> temp = adjacencyList.get(node);
                        if (temp != null) {
                            neighbors.addAll(temp); // 将当前节点的邻接点添加到列表
                        }
                    }
                    nodeY += offsetY; // 调整 Y 坐标，确保相邻节点的 Y 坐标相同
//                    System.out.println(neighbors);
                    if (neighbors != null) {
                        int i=1;
                        for (String neighbor : neighbors) {
                            if (!nodePositions.containsKey(neighbor)) {
                                queue.offer(neighbor);
                                nodeX = avgX - deltaX * (neighbors.size()/2 - i);
                                nodePositions.put(neighbor, new Point(nodeX, nodeY));
                            }
                            i++;
                        }
                    }
                }
            }
        }
//        System.out.println(nodePositions);
        StringBuilder svgContent = new StringBuilder();
        svgContent.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10000\" height=\"10000\">\n");

        // Define the arrowhead
        svgContent.append("<defs>\n");
        svgContent.append("<marker id=\"arrow\" markerWidth=\"10\" markerHeight=\"10\" refX=\"5\" refY=\"2\" orient=\"auto\">\n");
        svgContent.append("<path d=\"M0,0 L0,4 L6,2 z\" fill=\"black\" />\n");
        svgContent.append("</marker>\n");
        svgContent.append("</defs>\n");

        for (String node : textwords) {
            Point nodePosition = nodePositions.get(node);
            if (nodePosition != null) {
                int nodeX = (int) nodePosition.getX();
                int nodeY = (int) nodePosition.getY();
                // Calculate the text length and set the ellipse size accordingly
                int textLength = node.length();
                int ellipseWidth = textLength * 7; // Approximate width per character
                int ellipseHeight = 20; // Fixed height for the ellipse
                int flag = 0;
                for (String pathnode : shortpath) {
                    if (node.equals(pathnode)){
                        // Draw the ellipse
                        svgContent.append(String.format("<ellipse cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\" fill=\"none\" stroke=\"red\"/>\n", nodeX, nodeY, ellipseWidth / 2, ellipseHeight / 2));
                        // Draw the text inside the ellipse
                        svgContent.append(String.format("<text x=\"%d\" y=\"%d\" fill=\"red\" font-size=\"12\" text-anchor=\"middle\" alignment-baseline=\"middle\">%s</text>\n", nodeX, nodeY, node));
                        flag = 1;
                    }
                }
                if(flag == 1) {
                    continue;
                }
                // Draw the ellipse
                svgContent.append(String.format("<ellipse cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\" fill=\"none\" stroke=\"blue\"/>\n", nodeX, nodeY, ellipseWidth / 2, ellipseHeight / 2));
                // Draw the text inside the ellipse
                svgContent.append(String.format("<text x=\"%d\" y=\"%d\" fill=\"blue\" font-size=\"12\" text-anchor=\"middle\" alignment-baseline=\"middle\">%s</text>\n", nodeX, nodeY, node));
            }
        }

        for (String node : adjacencyList.keySet()) {
            Point nodePosition = nodePositions.get(node);
            if (nodePosition != null) {
                int nodeX = (int) nodePosition.getX();
                int nodeY = (int) nodePosition.getY();

                int textLength = node.length();
                int rx = textLength * 7 / 2; // Approximate width per character / 2
                int ry = 10; // Fixed height for the ellipse / 2

                List<String> neighbors = adjacencyList.get(node);
                for (String neighbor : neighbors) {
                    Point neighborPosition = nodePositions.get(neighbor);
                    if (neighborPosition != null) {
                        int neighborX = (int) neighborPosition.getX();
                        int neighborY = (int) neighborPosition.getY();

                        // Calculate angle between nodes
                        double angle = Math.atan2(neighborY - nodeY, neighborX - nodeX);

                        // Calculate start and end points on the ellipse boundary
                        int startX0 = (int) (nodeX + rx * Math.cos(angle));
                        int startY0 = (int) (nodeY + ry * Math.sin(angle));

                        int neighborTextLength = neighbor.length();
                        int neighborRx = neighborTextLength * 7 / 2;
                        int neighborRy = 10;

                        int endX = (int) (neighborX - neighborRx * Math.cos(angle));
                        int endY = (int) (neighborY - neighborRy * Math.sin(angle));

//                         Calculate control points for curved edges
                        int controlX1 = (int) (startX0 + 0.5 * (endX - startX0) - 0.1 * (endY - startY0));
                        int controlY1 = (int) (startY0 + 0.5 * (endY - startY0) + 0.1 * (endX - startX0));
                        int controlX2 = (int) (endX - 0.5 * (endX - startX0) + 0.1 * (endY - startY0));
                        int controlY2 = (int) (endY - 0.5 * (endY - startY0) - 0.1 * (endX - startX0));
                        int flag = 0;
                        for (String pathnode : shortpath) {
                            if (node.equals(pathnode)){
                                flag += 1;
                            }
                            if (neighbor.equals(pathnode)){
                                flag += 1;
                            }
                        }
                        if(flag == 2) {
                            svgContent.append(String.format("<path d=\"M%d,%d C%d,%d %d,%d %d,%d\" fill=\"none\" stroke=\"red\" marker-end=\"url(#arrow)\"/>\n", startX0, startY0, controlX1, controlY1, controlX2, controlY2, endX, endY));
                        }else {
                            svgContent.append(String.format("<path d=\"M%d,%d C%d,%d %d,%d %d,%d\" fill=\"none\" stroke=\"black\" marker-end=\"url(#arrow)\"/>\n", startX0, startY0, controlX1, controlY1, controlX2, controlY2, endX, endY));
                        }
                    }
                }
            }
        }
        svgContent.append("</svg>");
        return svgContent;
    }

    private static String randomWalk() {
        List<String> nodes = new ArrayList<>(graph.keySet());
        if (nodes.isEmpty()) {
            return "The graph is empty!";
        }
        String currentNode = nodes.get(new Random().nextInt(nodes.size()));
        System.out.println("currentNode = " + currentNode);
        Set<String> visitedEdges = new HashSet<>();
        StringBuilder walkResult = new StringBuilder();
        walkResult.append(currentNode);

        Scanner scanner = new Scanner(System.in);

        while (!stopRandomWalk) {
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

            // 添加延时
//            try {
//                Thread.sleep(10); //
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            // 检查用户输入是否要停止遍历
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("stop")) {
                    stopRandomWalk = true;
                }
            }
        }

        // 写入路径到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("random_walk.txt"))) {
            writer.write(walkResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Random walk result: " + walkResult.toString();
    }
}