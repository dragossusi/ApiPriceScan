import com.google.gson.Gson;

import spark.Spark;

public class Main {

    public static void main(String[] args) {
    	Gson gson = new Gson();
    	DBConnection connection = new DBConnection();
        Hello.setupListener(connection, gson);
    }
}
// ar trebui m-am mai conectat la baze de date din java dar erau mysql, dar nu cred ca e mare diferenta
//pot sa ma conectez la api
/*
inti il opresti altfel porneste ceva de doua ori si dupa nu mai merge
{
	"name" : "Zacusca de Raureni",
	"image_path" : "produs/zacusca.jpg",
	"results":
	[
		{
			"pret" : 3.29,
			"magazin" : "Profi Romania",
			"image_path" : "magazin/profi.jpg"
		},
		{
			"pret" : 3.19,
			"magazin" : "Kaufland Romania",
			"image_path" : "magazin/kaufland"
		},
		{
			"pret" : 3.49,
			"magazin" : "Alimentara nuj care",
			"image_path" : "magazin/alimentara"
		}
	]
}

 */