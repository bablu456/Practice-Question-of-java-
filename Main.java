import com.mysql.cj.xdevapi.UpdateType;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/lenden";
    private static final String username = "root";
    private static final String password = "bablu@0703";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String debit_query = "UPDATE accounts SET balance = balance - ?WHERE account_number = ?";
            String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement debitPreparedstatement = connection.prepareStatement(debit_query);
            PreparedStatement creditPreparedstatement = connection.prepareStatement(credit_query);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the Account Number : ");
            int account_number = sc.nextInt();
            System.out.println("Enter the Balance : ");
            double amount = sc.nextDouble();

//            debitPreparedstatement.setDouble(1, amount);
            debitPreparedstatement.setInt(1, account_number);
            creditPreparedstatement.setDouble(1, amount);
//            creditPreparedstatement.setInt(2, 102);
            debitPreparedstatement.executeUpdate();
             creditPreparedstatement.executeUpdate();
            if(isSufficient(connection,account_number,amount)) {
                connection.commit();
                System.out.println("Transaction Successfully !! ");
//                int affectedRows1 = debitPreparedstatement.executeUpdate();
//                int affectedrows2 = creditPreparedstatement.executeUpdate();
            }else{
                connection.rollback();
                System.out.println("Transaction Failed !! ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    static boolean isSufficient(Connection connection, int account_number, double amount) {
        try {
            String query = "SELECT balance FROM account_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, account_number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double current_balance = resultSet.getDouble("balance");
                if (amount > current_balance) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
