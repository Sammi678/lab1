//package test.;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import java.lang.reflect.Method;


/** 
* TextGraph Tester. 
* 
* @author <Authors name> 
* @since <pre>6月 12, 2024</pre> 
* @version 1.0 
*/ 
public class TextGraphTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: main(String[] args) 
* 
*/ 
@Test
public void testMain() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: drawDirectedGraph(Map<String, List<String>> adjacencyList, String filePath, String[] textwords) 
* 
*/ 
@Test
public void testDrawDirectedGraph() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: generateDirectedGraph(String fileName) 
* 
*/ 
@Test
public void testGenerateDirectedGraph() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("generateDirectedGraph", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: showDirectedGraph(Map<String, Map<String, Integer>> graph) 
* 
*/ 
@Test
public void testShowDirectedGraph() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("showDirectedGraph", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: queryBridgeWords(String word1, String word2) 
* 
*/ 
@Test
public void testQueryBridgeWords1() throws Exception {
//TODO: Test goes here...
    try {
        TextGraph textGraph = new TextGraph();
        Method method2 = TextGraph.class.getDeclaredMethod("generateDirectedGraph", String.class);
        method2.setAccessible(true);
        String[] text = (String[]) method2.invoke(textGraph, "word.txt");

        Method method = TextGraph.class.getDeclaredMethod("queryBridgeWords", String.class, String.class);
        method.setAccessible(true);  // 授权访问私有方法
        String result = (String) method.invoke(textGraph, "xxx", "yyy");  // 调用私有方法
        Assert.assertEquals(result,"No \"xxx\" and \"yyy\" in the graph!");
    } catch (Exception e) {
        e.printStackTrace();
    }


/*
try {
   Method method = TextGraph.getClass().getMethod("queryBridgeWords", String.class, String.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
}

@Test
public void testQueryBridgeWords2() throws Exception {
//TODO: Test goes here...
        try {
            TextGraph textGraph = new TextGraph();
            Method method2 = TextGraph.class.getDeclaredMethod("generateDirectedGraph", String.class);
            method2.setAccessible(true);
            String[] text = (String[]) method2.invoke(textGraph, "word.txt");

            Method method = TextGraph.class.getDeclaredMethod("queryBridgeWords", String.class, String.class);
            method.setAccessible(true);  // 授权访问私有方法
            String result = (String) method.invoke(textGraph, "xxx", "of");  // 调用私有方法
            Assert.assertEquals(result,"No \"xxx\" in the graph!");
        } catch (Exception e) {
            e.printStackTrace();
        }
}

@Test
public void testQueryBridgeWords3() throws Exception {
//TODO: Test goes here...
    try {
        TextGraph textGraph = new TextGraph();
        Method method2 = TextGraph.class.getDeclaredMethod("generateDirectedGraph", String.class);
        method2.setAccessible(true);
        String[] text = (String[]) method2.invoke(textGraph, "word.txt");

        Method method = TextGraph.class.getDeclaredMethod("queryBridgeWords", String.class, String.class);
        method.setAccessible(true);  // 授权访问私有方法
        String result = (String) method.invoke(textGraph, "cross", "yyy");  // 调用私有方法
        Assert.assertEquals(result,"No \"yyy\" in the graph!");
    } catch (Exception e) {
        e.printStackTrace();
    }

}


@Test
public void testQueryBridgeWords4() throws Exception {
//TODO: Test goes here...
    try {
        TextGraph textGraph = new TextGraph();
        Method method2 = TextGraph.class.getDeclaredMethod("generateDirectedGraph", String.class);
        method2.setAccessible(true);
        String[] text = (String[]) method2.invoke(textGraph, "word.txt");

        Method method = TextGraph.class.getDeclaredMethod("queryBridgeWords", String.class, String.class);
        method.setAccessible(true);  // 授权访问私有方法
        String result = (String) method.invoke(textGraph, "peers", "of");  // 调用私有方法
        Assert.assertEquals(result,"No bridge words from \"peers\" to \"of\"!");
    } catch (Exception e) {
        e.printStackTrace();
    }

}


@Test
public void testQueryBridgeWords5() throws Exception {
//TODO: Test goes here...
    try {
        TextGraph textGraph = new TextGraph();
        Method method2 = TextGraph.class.getDeclaredMethod("generateDirectedGraph", String.class);
        method2.setAccessible(true);
        String[] text = (String[]) method2.invoke(textGraph, "word.txt");

        Method method = TextGraph.class.getDeclaredMethod("queryBridgeWords", String.class, String.class);
        method.setAccessible(true);  // 授权访问私有方法
        String result = (String) method.invoke(textGraph, "all", "of");  // 调用私有方法
        Assert.assertEquals(result,"No bridge words from \"all\" to \"of\"!");
    } catch (Exception e) {
        e.printStackTrace();
    }

}

@Test
public void testQueryBridgeWords6() throws Exception {
//TODO: Test goes here...
    try {
        TextGraph textGraph = new TextGraph();
        Method method2 = TextGraph.class.getDeclaredMethod("generateDirectedGraph", String.class);
        method2.setAccessible(true);
        String[] text = (String[]) method2.invoke(textGraph, "word.txt");

        Method method = TextGraph.class.getDeclaredMethod("queryBridgeWords", String.class, String.class);
        method.setAccessible(true);  // 授权访问私有方法
        String result = (String) method.invoke(textGraph, "cross", "of");  // 调用私有方法
        Assert.assertEquals(result,"Bridge words between \"cross\" and \"of\": section");
    } catch (Exception e) {
        e.printStackTrace();
    }

}




    /**
* 
* Method: generateNewText(String inputText) 
* 
*/ 
@Test
public void testGenerateNewText() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("generateNewText", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: convertToAdjacencyList(Map<String, Map<String, Integer>> graph) 
* 
*/ 
@Test
public void testConvertToAdjacencyList() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("convertToAdjacencyList", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: generateSVG(Map<String, List<String>> adjacencyList, String[] textwords) 
* 
*/ 
@Test
public void testGenerateSVG() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("generateSVG", Map<String,.class, String[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: writeSVGToFile(String svgContent, String filePath) 
* 
*/ 
@Test
public void testWriteSVGToFile() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("writeSVGToFile", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: calcShortestPath(String word1, String word2, String[] textwords) 
* 
*/ 
@Test
public void testCalcShortestPath() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("calcShortestPath", String.class, String.class, String[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: ShortPathSVG(Map<String, List<String>> adjacencyList, String[] textwords, String[] shortpath) 
* 
*/ 
@Test
public void testShortPathSVG() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("ShortPathSVG", Map<String,.class, String[].class, String[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: randomWalk() 
* 
*/ 
@Test
public void testRandomWalk() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = TextGraph.getClass().getMethod("randomWalk"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
