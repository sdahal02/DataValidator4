import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class DataValidation {


    public static void main(String[] args) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(args[0]);
            HttpResponse response = null;

            response = client.execute(request);


            HttpEntity entity = response.getEntity();

            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(json);
            System.out.println(args[1]);
            SchemaValidator schemaValidator = new SchemaValidator(args[1]);
            //SchemaValidator schemaValidator = new SchemaValidator("json-schema.json");

            List<Map<String, Integer>> validationErros = schemaValidator.validate(jsonArray);
            System.out.println("Total Objects:");
            System.out.println(jsonArray.length());
            System.out.println("Errors:");
            System.out.println(validationErros.get(0));
            System.out.println("Null Counts:");
            System.out.println(validationErros.get(1));

        } catch (Exception e) {
            e.getMessage();
        }

    }
}
