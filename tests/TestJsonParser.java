package tests;

import java.util.List;

import jsonparser.JSONParser;
import scan.Test;

@Test
public class TestJsonParser extends TestABC {

    public TestJsonParser() {
        addTest(this::testParser);
    }

    public void testParser() {
        var jsonString = "{\"name\":\"John\", \"age\":30, \"skills\":[\"Java\", \"Python\"]}";
        var jsonData = JSONParser.parseJson(jsonString);
        assert jsonData.getData().get("name").equals("John") 
            : "Name is not John";
        assert jsonData.getData().get("age").equals(30) 
            : "Age is not 30";
        assert jsonData.getData().get("skills").equals(List.of("Java", "Python")) 
            : "Skills are not Java, Python";
    }

}
