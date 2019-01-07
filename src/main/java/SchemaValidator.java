import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

class SchemaValidator {

    private JSONObject schemaJSON;

    SchemaValidator(String schemaLocation) throws Exception {

        if (schemaLocation == null) {
            throw new Exception("Error in schema file location");
        }

        InputStream is = new FileInputStream(schemaLocation);
        String jsonTxt = org.apache.commons.io.IOUtils.toString(is, "UTF-8");
        this.schemaJSON = new JSONObject(jsonTxt);

    }

    List<Map<String, Integer>> validate(JSONArray jsonArray) {
        Map<String, Integer> errorMap = new HashMap<>();
        Map<String, Integer> nullCountMap = new HashMap<>();
        this.schemaJSON.get("properties");
        Iterator<String> keys = this.schemaJSON.getJSONObject("properties").keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Integer errorCount = 0, nullCount = 0;

            for (Object item : jsonArray) {
                if (item instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) item;
                    if (this.schemaJSON.getJSONObject("properties").getJSONObject(key).get("allowNull").equals(true) && jsonObj.isNull(key)) {
                        nullCount++;
                    } else if (this.schemaJSON.getJSONObject("properties").getJSONObject(key).get("type").equals("string")) {
                        if ((jsonObj.getString(key) == null)) {
                            errorCount++;
                        }
                    } else if (this.schemaJSON.getJSONObject("properties").getJSONObject(key).get("type").equals("array")) {
                        if (!(jsonObj.get(key) instanceof JSONArray)) {
                            errorCount++;
                        }
                    } else if (this.schemaJSON.getJSONObject("properties").getJSONObject(key).get("type").equals("alpha_two_code")) {
                        if (!(jsonObj.get(key) instanceof String)) {
                            errorCount++;
                        } else {
                            if (((String) jsonObj.get(key)).length() != 2) {
                                errorCount++;
                            }
                        }
                    }

                }
            }

            if (errorCount > 0) {
                errorMap.put(key, errorCount);
            }
            if (nullCount > 0) {
                nullCountMap.put(key, nullCount);
            }

        }

        List<Map<String, Integer>> errorsList = new ArrayList<>();
        errorsList.add(errorMap);
        errorsList.add(nullCountMap);

        return errorsList;
    }


}
