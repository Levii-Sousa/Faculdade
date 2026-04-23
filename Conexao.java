package javasavir; // Pacote do projeto

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao
{
    private static final String URL = "jdbc:mysql://localhost:3306/seu_banco";
    private static final String USER = "root";
    private static final String PASS = "";

    // Método para obter conexão
    public static Connection getConnection() throws Exception
    {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Método para testar a conexão
    public static boolean testarConexao()
    {
        try
        {
            Connection conn = getConnection();

            if (conn != null)
            {
                conn.close();
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}