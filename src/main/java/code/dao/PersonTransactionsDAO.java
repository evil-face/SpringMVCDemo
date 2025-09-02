package code.dao;

import code.models.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PersonTransactionsDAO {
  @Autowired private DataSource dataSource;
  @Autowired private JdbcTemplate jdbcTemplate;

  // auto-commit enabled by default in Connection!
  // if we want control, we can work directly with Connection object:
  public void saveWithManualTransaction(Person person1, Person person2) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      try (PreparedStatement ps1 =
              connection.prepareStatement("INSERT INTO person(name, age, email) VALUES (?, ?, ?)");
          PreparedStatement ps2 =
              connection.prepareStatement(
                  "INSERT INTO person(name, age, email) VALUES (?, ?, ?)")) {

        ps1.setString(1, person1.getName());
        ps1.setInt(2, person1.getAge());
        ps1.setString(3, person1.getEmail());
        ps1.executeUpdate();

        ps2.setString(1, person2.getName());
        ps2.setInt(2, person2.getAge());
        ps2.setString(3, person2.getEmail());
        ps2.executeUpdate();

//        if (true) {
//          throw new SQLException("Oh no, something was very wrong with DB!");
//        }

        connection.commit();
        System.out.println("Transaction committed with JdbcTemplate!");
      } catch (SQLException e) {
        connection.rollback();
        System.out.println("Transaction rolled back due to: " + e.getMessage());
        throw e;
      }
    }
  }

  public void savePersonSuccessfully(Person person) {
    jdbcTemplate.update(
        "INSERT INTO person(name, age, email) VALUES (?, ?, ?)",
        person.getName(),
        person.getAge(),
        person.getEmail());
    // also calls something
  }

  public void savePersonWithException(Person person) {
    jdbcTemplate.update(
        "INSERT INTO person(name, age, email) VALUES (?, ?, ?)",
        person.getName(),
        person.getAge(),
        person.getEmail());
    throw new RuntimeException("Oh no, something was very wrong with DB!");
  }
}
