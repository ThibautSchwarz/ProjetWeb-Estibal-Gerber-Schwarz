package servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author tschwartz
 */
public class DAO {

    protected final DataSource myDataSource;

    /**
     *
     * @param dataSource la source de données à utiliser
     */
    public DAO(DataSource dataSource) {
        this.myDataSource = dataSource;
    }

    /**
     *
     * @return le nombre d'enregistrements dans la table CUSTOMER
     * @throws DAOException
     */
    public int numberOfCustomers() throws DAOException {
        int result = 0;
        
        String sql = "SELECT COUNT(*) AS NUMBER FROM CUSTOMER";
        // Syntaxe "try with resources" 
        // cf. https://stackoverflow.com/questions/22671697/try-try-with-resources-and-connection-statement-and-resultset-closing
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement(); // On crée un statement pour exécuter une requête
                ResultSet rs = stmt.executeQuery(sql) // Un ResultSet pour parcourir les enregistrements du résultat
                ) {
            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                // On récupère le champ NUMBER de l'enregistrement courant
                result = rs.getInt("NUMBER");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

        return result;
    }

    /**
     * Detruire un enregistrement dans la table CUSTOMER
     *
     * @param customerId la clé du client à détruire
     * @return le nombre d'enregistrements détruits (1 ou 0 si pas trouvé)
     * @throws DAOException
     */
    public int deleteCustomer(int customerId) throws DAOException {

        // Une requête SQL paramétrée
        String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Définir la valeur du paramètre
            stmt.setInt(1, customerId);

            return stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }

    /**
     *
     * @param customerId la clé du client à recherche
     * @return le nombre de bons de commande pour ce client (table
     * PURCHASE_ORDER)
     * @throws DAOException
     */
    public int numberOfOrdersForCustomer(int customerId) throws DAOException {
        int result = 0;

        // Une requête SQL paramétrée
        String sql = "SELECT COUNT(*) AS NUMBER FROM PURCHASE_ORDER WHERE CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Définir la valeur du paramètre
            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next(); // On a toujours exactement 1 enregistrement dans le résultat
                result = rs.getInt("NUMBER");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * Trouver un Customer à partir de sa clé
     *
     * @param customerID la clé du CUSTOMER à rechercher
     * @return l'enregistrement correspondant dans la table CUSTOMER, ou null si
     * pas trouvé
     * @throws DAOException
     */
    public CustomerEntity findCustomer(int customerID) throws DAOException {
        CustomerEntity result = null;

        String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection(); // On crée un statement pour exécuter une requête
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // On a trouvé
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESSLINE1");
                    // On crée l'objet "entity"
                    result = new CustomerEntity(customerID, name, address);
                } // else on n'a pas trouvé, on renverra null
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

        return result;
    }

    /**
     * Liste des clients localisés dans un état des USA
     *
     * @param state l'état à rechercher (2 caractères)
     * @return la liste des clients habitant dans cet état
     * @throws DAOException
     */
    public List<CustomerEntity> customersInState(String state) throws DAOException {
        List<CustomerEntity> result = new LinkedList<>(); // Liste vIde

        String sql = "SELECT * FROM CUSTOMER WHERE STATE = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, state);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) { // Tant qu'il y a des enregistrements
                    // On récupère les champs nécessaires de l'enregistrement courant
                    int id = rs.getInt("CUSTOMER_ID");
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESSLINE1");
                    // On crée l'objet entité
                    CustomerEntity c = new CustomerEntity(id, name, address);
                    // On l'ajoute à la liste des résultats
                    result.add(c);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

        return result;

    }

    //liste des différents états
    public List<String> existingStates() throws DAOException {
        List<String> result = new LinkedList<>();
        String sql = "SELECT DISTINCT STATE FROM CUSTOMER";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String state = rs.getString("STATE");
                result.add(state);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return result;
    }

    // cherche le client en fct de son e mail et identifiant
    public CustomerEntity findUser(String email, int customerID) throws DAOException {
        CustomerEntity result = null;
        String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ? AND EMAIL = ?";
        try (Connection connection = myDataSource.getConnection(); // On crée un statement pour exécuter une requête
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(2, email);
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // On a trouvé
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESSLINE1");
                    // On crée l'objet "entity"
                    result = new CustomerEntity(customerID, name, address);
                } // else on n'a pas trouvé, on renverra null
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

        return result;
    }

    // récupère toutes les commandes du client dont le nom est passé en paramètre
    public List<PurchaseOrder> allOrdersCustomer(String Name) throws SQLException {
        List<PurchaseOrder> result = new LinkedList<>();
        String sql = "SELECT * FROM PURCHASE_ORDER INNER JOIN CUSTOMER USING (CUSTOMER_ID)  WHERE NAME = ?  ORDER BY ORDER_NUM ";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, Name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int num = rs.getInt("order_num");
                int qt = rs.getInt("quantity");
                float cost = rs.getFloat("shipping_cost");
                Date salesDate = rs.getDate("sales_date");
                Date shipDate = rs.getDate("shipping_date");
                String comp = rs.getString("freight_company");
                int cus = rs.getInt("customer_id");
                int product = rs.getInt("product_id");
                PurchaseOrder p = new PurchaseOrder(num, cus, product, qt, cost, salesDate, shipDate, comp);
                result.add(p);
            }
        }
        return result;
    }

    // ajoute une commande 
    public int addPurchaseOrder(int num, int qt, float cost, String Name, int product, Date sales, Date ship, String comp) throws SQLException {
        int result = 0;
        String sql = "INSERT INTO PURCHASE_ORDER VALUES (?,(select customer_id from customer where name = ?),?,?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, num);
            stmt.setString(2, Name);
            stmt.setInt(3, product);
            stmt.setInt(4, qt);
            stmt.setFloat(5, cost);
            stmt.setDate(6, sales);
            stmt.setDate(7, ship);
            stmt.setString(8, comp);
            result = stmt.executeUpdate();
        }
        return result;
    }
    
    // supprime une commande
    public int deletePurchaseOrder(int num) throws SQLException {
        int result = 0;
        String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, num);
            result = stmt.executeUpdate();
        }
        return result;
    }

    // modifie une commande pour un numéro de commande passé en paramètre
    public int updatePurchaseOrder(int num, int product, int qt, float cost, String comp) throws SQLException {
        int result = 0;
        String sql = "update purchase_order set product_id=?,quantity=?,shipping_cost=?,freight_company=? where order_num = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, product);
            stmt.setInt(2, qt);
            stmt.setFloat(3, cost);
            stmt.setString(4, comp);
            stmt.setInt(5, num);
            result = stmt.executeUpdate();
        }
        return result;
    }

   // méthode facultative pour une évolution possible du site
    public float cost(int qt, int prodid) throws SQLException {
        float cost = 0;
        String sql = "select purchase_cost from product where product_id=?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, prodid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // On a trouvé
                    cost = rs.getFloat("purchase_cost") * qt;
                } // else on n'a pas trouvé, on renverra 0
            }
        }
        return cost;
    }

    //Chiffres d'affaire par client, renvoie une hashmap contenant les ventes et les noms des clients
    public Map<String, Double> salesByCustomer(Date debut, Date fin) throws DAOException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT NAME, SUM(PURCHASE_COST * QUANTITY) AS SALES FROM CUSTOMER c INNER JOIN PURCHASE_ORDER o ON (c.CUSTOMER_ID = o.CUSTOMER_ID) INNER JOIN PRODUCT p ON (o.PRODUCT_ID = p.PRODUCT_ID) WHERE o.SALES_DATE > ? AND o.SALES_DATE < ? GROUP BY NAME";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, debut);
            stmt.setDate(2, fin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // On récupère les champs nécessaires de l'enregistrement courant
                String name = rs.getString("NAME");
                double sales = rs.getDouble("SALES");
                // On l'ajoute à la liste des résultats
                result.put(name, sales);
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

     //Chiffres d'affaire par type de produit, renvoie une hashmap contenant les ventes et les types de produits 
    public Map<String, Double> salesByTypes(Date debut, Date fin) throws DAOException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT PRODUCT_CODE, SUM(PURCHASE_COST * QUANTITY) AS SALES FROM PURCHASE_ORDER o INNER JOIN PRODUCT p ON (o.PRODUCT_ID = p.PRODUCT_ID) WHERE o.SALES_DATE > ? AND o.SALES_DATE < ? GROUP BY PRODUCT_CODE";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, debut);
            stmt.setDate(2, fin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // On récupère les champs nécessaires de l'enregistrement courant
                String product_code = rs.getString("PRODUCT_CODE");
                double sales = rs.getDouble("SALES");
                // On l'ajoute à la liste des résultats
                result.put(product_code, sales);
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }
    
     //Chiffres d'affaire par état, renvoie une hashmap contenant les ventes et les états
    public Map<String, Double> salesByStates(Date debut, Date fin) throws DAOException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT STATE, SUM(PURCHASE_COST * QUANTITY) AS SALES FROM CUSTOMER c INNER JOIN PURCHASE_ORDER o ON (c.CUSTOMER_ID = o.CUSTOMER_ID) INNER JOIN PRODUCT p ON (o.PRODUCT_ID = p.PRODUCT_ID) WHERE o.SALES_DATE > ? AND o.SALES_DATE < ? GROUP BY STATE";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, debut);
            stmt.setDate(2, fin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // On récupère les champs nécessaires de l'enregistrement courant
                String product_code = rs.getString("STATE");
                double sales = rs.getDouble("SALES");
                // On l'ajoute à la liste des résultats
                result.put(product_code, sales);
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }
}
