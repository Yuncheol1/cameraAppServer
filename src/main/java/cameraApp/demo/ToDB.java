/*
package cameraApp.demo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@Component
public class ToDB
{
    static Connection connection;
    static Statement stmt;
    static String endl = "\n";
    static String tap = "\t";

    public ToDB() throws SQLException, IOException
    {
        DBInit();
        int id = GetUser("tester","1234");
    }

    @PreDestroy
    public void closeConnection() {
        try {
            if (stmt != null)
            {
                stmt.executeUpdate("delete from landmark_user;");                       // DB 초기화 문이니까 필요하면 주석 처리
                stmt.executeUpdate("ALTER TABLE landmark_user AUTO_INCREMENT = 1;");
                stmt.close();
            }
            if (connection != null) connection.close();
            System.out.println("DB closed");
        } catch (SQLException e) {}
    }


    static <T> void print(T value) {
        System.out.print(value);
    }

    static void DBInit() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "1234");
        stmt = connection.createStatement();
        stmt.executeUpdate("use db;");
        print("Load DB Successfully");
    }

    static int GetUser(String id, String pw) throws SQLException
    {
        String cnt = String.format("select user_id from user where id = \"%s\" and pw = \"%s\"",id,pw);
        ResultSet result = stmt.executeQuery(cnt);
        if (result.next()) {
            int userId = result.getInt("user_id");
            return userId;
        } else {
            return -1;
        }
    }

    //ALTER TABLE landmark_user AUTO_INCREMENT = 1; <- Auto_Increment 제거
    static void InsertValues_Landmark(int id, String payload) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        LandmarkWrapper data = mapper.readValue(payload, LandmarkWrapper.class);
        data.landmarks = data.landmarks.stream()
                .filter(land1 -> land1.id == 0 || (land1.id >= 11 && land1.id <= 32))
                .toList();
        // Make JSON
        Map<String, Object> jsonMap = mapper.convertValue(data, Map.class);
        //System.out.print("landmark 수: " + data.landmarks.size());
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("D:/Unity/Capstone_MR/Assets/StreamingAssets/test_user.json"), jsonMap);
        //print(", Make JSon Successfully");
        // To DB
        String cnt;
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO landmark_user(pose_id, landmark_id, position, pressure, user_id) VALUES (?, ?, ?, ?, ?)");
        for(Landmark land : data.landmarks)
        {
            cnt = String.format("{\"x\":%f,\"y\":%f,\"z\":%f}", land.x, land.y, land.z);
            pstmt.setInt(1, 1);             // Pose_Id
            pstmt.setInt(2, land.id);          // landmark_Id
            pstmt.setString(3, cnt);           // Position
            pstmt.setFloat(4, 0);           // pressure
            pstmt.setInt(5, 1);             // id
            pstmt.executeUpdate();
        }
        //print("Insert Landmark Successfully\n");
    }

    public static class LandmarkWrapper
    {
        public List<Landmark> landmarks;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Landmark
    {
        public int id;
        public float x;
        public float y;
        public float z;
    }
}


*/
