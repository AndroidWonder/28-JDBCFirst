package jdbcfirst.example.course.com.a28_jdbcfirst;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private Thread t = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start thread
        t = new Thread(background);
        t.start();
    }

    private Runnable background = new Runnable() {
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/test";
            String username = "harry";
            String password = "harry";
            

            Statement stmt = null;
            Connection con = null;
            try { //create connection and statement objects
                con = DriverManager.getConnection(
                        URL,
                        username,
                        password);
                stmt = con.createStatement();
            } catch (SQLException e) {
                Log.e("JDBC", "problem connecting");
            }

            try {
                // execute SQL commands to create table, insert data, select contents
                stmt.executeUpdate("drop table if exists first;");
                stmt.executeUpdate("create table first(id integer primary key, city varchar(25));");
                stmt.executeUpdate("insert into first values(1, 'Waltham');");
                stmt.executeUpdate("insert into first values(2, 'Cambridge');");
                stmt.executeUpdate("insert into first values(3, 'Somerville');");
                stmt.executeUpdate("insert into first values(4, 'Boston');");

                ResultSet result = stmt.executeQuery("select * from first;");

                //read result set, write data to Log
                while (result.next()) {
                    String city = result.getString("city");
                    String str = String.format("%d    %s", result.getInt("id"), result.getString("city"));
                    Log.e("JDBC", str);
                }

                //clean up
                t = null;

            } catch (SQLException e) {
                Log.e("JDBC", "problems with SQL sent to " + URL +
                        ": " + e.getMessage());
            } finally {
                try { //close connection, may throw checked exception
                    if (con != null)
                        con.close();
                } catch (SQLException e) {
                    Log.e("JDBC", "close connection failed");
                }
            }

        }
    };
}

