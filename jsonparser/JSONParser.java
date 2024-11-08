package jsonparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {

    public static JSONData parseJson(String jsonString) {
        Map<Object, Object> data = new HashMap<>();

        var isChar = false;
        var isNum = false;
        var isList = false;
        var isKey = false;

        List<Object> listItems = new ArrayList<>();
        var key = "";
        var actualString = "";

        Character[] charslist = { '"', ']', '}', ' ', ',' };
        List<Character> delimString = List.of(charslist);

        for (int index = 0; index < jsonString.length(); index++) {

            var cur = jsonString.charAt(index);

            if (isChar) {
                if (delimString.contains(cur) || cur == 0) {
                    isChar = false;
                    if (isList) {
                        if (actualString.replace(".", "").replace("-", "").chars().allMatch(Character::isDigit)) {
                            isNum = false;
                            if (actualString.contains(".")) {
                                listItems.add(Double.parseDouble(actualString));
                            } else {
                                listItems.add(Integer.parseInt(actualString));
                            }
                            ;
                        } else {
                            listItems.add(actualString);
                        }
                        actualString = "";
                    } else if (isKey) {
                        if (actualString.replace(".", "").replace("-", "").chars().allMatch(Character::isDigit)) {
                            isNum = false;
                            if (actualString.contains(".")) {
                                data.put(key, Double.parseDouble(actualString));
                            } else {
                                data.put(key, Integer.parseInt(actualString));
                            }
                        } else {
                            data.put(key, actualString);
                        }
                        key = "";
                        isKey = false;
                        actualString = "";
                    }
                    if (!key.isEmpty()) {
                        actualString = "";
                    }
                    if (cur != ']') {
                        continue;
                    }
                }
                if (!isNum) {
                    actualString += cur;
                }
            }

            if (cur == '{' && isKey) {
                var jsonData = parseJson(jsonString.substring(index + 1));
                index += jsonData.getIndex() + 1;
                if (isList) {
                    listItems.add(jsonData.getData());
                } else if (isKey) {
                    data.put(key, jsonData.getData());
                    key = "";
                    isKey = false;
                }
            } else if (cur == '"') {
                isChar = true;
            } else if (cur == ':') {
                if (!isKey) {
                    key = actualString;
                    isKey = true;
                    actualString = "";
                }
            } else if ((Character.isDigit(cur)) || (isNum && cur == '.')) {
                isChar = true;
                isNum = true;
                actualString += cur;
            } else if (cur == '[') {
                isList = true;
            } else if (cur == ']' && isList) {
                isList = false;
                data.put(key, listItems);
                key = "";
                isKey = false;
                listItems = new ArrayList<>();
                actualString = "";
            } else if (cur == '}') {
                return new JSONData(data, index);
            }
            cur = 0;
        }

        return new JSONData(data, 0);
    }

    public static void main(String[] args) {
        var content = "{\"name\": \"Hello\"}";
        content = "{\"name\": \"kamuri\", \"personal_info\": {\"age\": 50, \"skills\": [\"python\", \"java\"], \"salary\": 1.5}}";
        var result = parseJson(content).getData();
        System.out.println(result);
    }

}
