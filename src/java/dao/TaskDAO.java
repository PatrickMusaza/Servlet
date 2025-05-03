package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Task;

public class TaskDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private String jdbcUsername = "Administrator";
    private String jdbcPassword = "g@Secur!";

    private static final String INSERT_TASKS_SQL = "INSERT INTO tasks (title, description, status, assigned_to, created_by) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_TASK_BY_ID = "SELECT id, title, description, status, assigned_to, created_by, created_at, updated_at FROM tasks WHERE id =?";
    private static final String SELECT_ALL_TASKS = "SELECT * FROM tasks";
    private static final String SELECT_USER_TASKS = "SELECT * FROM tasks WHERE assigned_to = ?";
    private static final String DELETE_TASKS_SQL = "DELETE FROM tasks WHERE id = ?";
    private static final String UPDATE_TASKS_SQL = "UPDATE tasks SET title = ?, description= ?, status =?, assigned_to =?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertTask(Task task) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASKS_SQL)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus());
            preparedStatement.setInt(4, task.getAssignedTo());
            preparedStatement.setInt(5, task.getCreatedBy());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Task selectTask(int id) {
        Task task = null;
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TASK_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String status = rs.getString("status");
                int assignedTo = rs.getInt("assigned_to");
                int createdBy = rs.getInt("created_by");
                task = new Task(title, description, status, assignedTo, createdBy);
                task.setId(id);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return task;
    }

    public List<Task> selectAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String status = rs.getString("status");
                int assignedTo = rs.getInt("assigned_to");
                int createdBy = rs.getInt("created_by");
                tasks.add(new Task(id, title, description, status, assignedTo, createdBy));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return tasks;
    }

    public List<Task> selectUserTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_TASKS)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String status = rs.getString("status");
                int assignedTo = rs.getInt("assigned_to");
                int createdBy = rs.getInt("created_by");
                tasks.add(new Task(id, title, description, status, assignedTo, createdBy));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return tasks;
    }

    public boolean deleteTask(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_TASKS_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateTask(Task task) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_TASKS_SQL)) {
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getStatus());
            statement.setInt(4, task.getAssignedTo());
            statement.setInt(5, task.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public int selectTaskCompletedCount() {
        int count = 0;
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT COUNT(*) FROM tasks WHERE status = 'completed'")) {
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return count;
    }

    public int selectActiveCount() {
        int count = 0;
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT COUNT(*) FROM tasks WHERE not status = 'completed'")) {
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return count;
    }
}