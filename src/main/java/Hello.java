import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Service.StaticFiles;
import spark.Spark;
import spark.staticfiles.StaticFilesConfiguration;

import java.security.Provider.Service;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.get;



class Hello {

	static void setupListener(DBConnection connection, Gson gson) {
		try {
			get("/", (Request req, Response res) -> {
				log("A intrat pe /");
				res.redirect("asd.html");
				return null;
			});
			/// cautare
			get("/search/:cod", (Request req, Response res) -> {
				// TODO baga in hashmap rezultat(asta in thread cred, ca sa nu
				// blocheze pentru alte cautari in acelasi timp
				log("Cauta "+req.params("cod"));
				if (connection.isCod(req.params("cod"))) {
					HashMap<String, Object> finalResult = doMap(connection.getAll(req.params("cod")));

					log("Exista");
					connection.closeConnection();

					res.type("application/json");
					return gson.toJson(finalResult);
				}

				log("Nu exista");
				connection.closeConnection();

				return "Produsul cu codul " + req.params("cod") + " nu se afla in baza de date";
			});

			/// lista magazine
			get("/magazine", (Request req, Response res) -> {
				// TODO
				ArrayList<Shop> magazine = connection.getShops();

				log("Lista magazine");
				connection.closeConnection();

				res.type("application/json");
				return gson.toJson(magazine);
			});
			///lista magazine din oras
			get("/magazine/:oras",(Request req, Response res) -> {
				ArrayList<Shop> magazine = connection.getShops(req.params("oras"));

				log("Lista magazine pentru orasul "+req.params("oras"));
				connection.closeConnection();

				res.type("application/json");
				return gson.toJson(magazine);
			});
			///lista nume firme din oras
			get("/firme/:oras", (Request req, Response res) -> {
				ArrayList<String> firme = connection.getFirme(req.params("oras"));

				log("Lista firme din roasul "+req.params("oras"));
				connection.closeConnection();

				res.type("application/json");
				return gson.toJson(firme);
			});
			///lsita orase
			get("/orase", (Request req, Response res) -> {
				ArrayList<String> orase = connection.getCities();

				log("lista orase");
				connection.closeConnection();

				res.type("application/json");
				return gson.toJson(orase);
			});
			///lista strazi
			get("/strazi/:oras/:firma",(Request req, Response res) -> {
				ArrayList<String> strazi = connection.getStreets(req.params("oras"),req.params("firma"));

				log("lista strazi "+req.params("oras")+" "+ req.params("firma"));
				connection.closeConnection();

				res.type("application/json");
				return gson.toJson(strazi);
			});
		} catch (Exception e) {
			log(e);
			e.printStackTrace();
		}
	}

	private static HashMap<String, Object> doMap(ResultSet resultSet) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		resultSet.next();
		result.put("image_path", resultSet.getString(2));
		result.put("name", resultSet.getString(1));
		ArrayList<Object> results = new ArrayList<>();
		HashMap<String, Object> magazin = new HashMap<>();
		magazin.put("magazin", resultSet.getString(3));
		magazin.put("image_path", resultSet.getString(4));
		magazin.put("oras", resultSet.getString(5));
		magazin.put("pret", resultSet.getDouble(6));
		results.add(magazin);
		while (resultSet.next()) {
			magazin = new HashMap<>();
			magazin.put("magazin", resultSet.getString(3));
			magazin.put("image_path", resultSet.getString(4));
			magazin.put("oras", resultSet.getString(5));
			magazin.put("pret", resultSet.getDouble(6));
			results.add(magazin);
		}
		result.put("results", results);
		resultSet.close();
		return result;
	}
	private static void log(Object o) {
		System.out.println(o);
	}
}