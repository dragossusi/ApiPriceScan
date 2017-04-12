import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.log.Log;

/**
 * Created by catal on 30-Mar-17.
 */
public class DBConnection {
    private Connection connect;
    private Statement stm;

    static private final String user = "sql11168438";
    static private final String pass = "g5HExUH8e7";
    static private final String host = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11168438";
    static private final String dbClass = "com.mysql.jdbc.Driver";

    private static final String sqlCommand= "select produs.nume,produs.image_path ,firma.nume ,firma.image_path,magazin.oras ,stocare.pret from firma"
            + " inner join magazin on firma.id_firma = magazin.id_firma "
            + "inner join stocare on magazin.ID_MAGAZIN = stocare.id_magazin "
            + "inner join produs on stocare.cod_produs = produs.cod_produs";

    private static final String sqlShops = "select magazin.oras,firma.nume,magazin.strada from firma inner join magazin on firma.id_firma = magazin.id_firma order by oras";
    private static final String sqlShopsInCity = "select firma.nume,magazin.strada from firma inner join magazin on firma.id_firma = magazin.id_firma where oras = '";
    private static final String sqlFirmNamesInCity = "select distinct firma.nume from firma inner join magazin on firma.id_firma = magazin.id_firma where magazin.oras = '";
    private static final String sqlCities = "select distinct oras from magazin";

    private static final String sqlStreets = "select distinct magazin.strada from magazin INNER JOIN  firma on magazin.id_firma = firma.id_firma"+
            " where firma.nume = '%s' and magazin.oras = '%s'";

    public DBConnection(){
        try {
            Class.forName(dbClass).newInstance();
            connect = DriverManager.getConnection(host, user, pass);
            stm = connect.createStatement();
            System.out.println("Conexiune deschisa");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAll(String cod) throws Exception{
        check();
        String finalCommand = sqlCommand+" where produs.cod_produs = '"+cod+"'" + " order by pret";
        return stm.executeQuery(finalCommand);
    }

    public boolean isCod(String cod) throws Exception{
        check();
        ResultSet rset = stm.executeQuery("select cod_produs from produs");
        while (rset.next()){
            if(rset.getString(1).trim().equals(cod.trim())) {
                return true;
            }
        }
        rset.close();
        return false;
    }

    public ArrayList<Shop> getShops() throws Exception{
        check();
        ResultSet resultSet = stm.executeQuery(sqlShops);
        ArrayList<Shop> result = new ArrayList<>();
        while (resultSet.next()) {
            int i=1;
            result.add(new Shop(resultSet.getString(i++), resultSet.getString(i++), resultSet.getString(i)));
        }
        resultSet.close();
        return result;
    }

    public ArrayList<Shop> getShops(String oras) throws Exception{
        check();
        ResultSet resultSet = stm.executeQuery(sqlShopsInCity+oras+"'");
        ArrayList<Shop> result = new ArrayList<>();
        while (resultSet.next()) {
            int i=1;
            result.add(new Shop(oras, resultSet.getString(i++), resultSet.getString(i)));
        }
        resultSet.close();
        return result;
    }

    public ArrayList<String> getFirme(String oras) throws Exception {
        check();
        ResultSet resultSet = stm.executeQuery(sqlFirmNamesInCity+oras+"'");
        ArrayList<String> result = new ArrayList<>();
        while(resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        resultSet.close();
        return result;
    }

    public ArrayList<String> getCities() throws Exception {
        check();
        ResultSet resultSet = stm.executeQuery(sqlCities);
        ArrayList<String> orase = new ArrayList<>();
        while(resultSet.next()) {
            orase.add(resultSet.getString(1));
        }
        resultSet.close();
        return orase;
    }

    public ArrayList<String> getStreets(String oras, String firma) throws Exception{
        check();
        System.out.println(String.format(sqlStreets,oras,firma));
        ResultSet resultSet = stm.executeQuery(String.format(sqlStreets,firma,oras));
        ArrayList<String> strazi = new ArrayList<>();
        while (resultSet.next()){
            strazi.add(resultSet.getString(1));
        }
        resultSet.close();
        return strazi;
    }

    private void check() throws Exception {
        if(connect.isClosed()) {
            connect = DriverManager.getConnection(host, user, pass);
            stm = connect.createStatement();
            System.out.println("Conexiune deschisa");
        }
    }
    public void closeConnection() throws Exception{
        connect.close();
        System.out.println("Conexiune inchisa\n");
    }
}