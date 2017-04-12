import com.google.gson.Gson;
import spark.Spark;

public class Main {

    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Gson gson = new Gson();
    	DBConnection connection = new DBConnection();
        Hello.setupListener(connection, gson);
    }
}
