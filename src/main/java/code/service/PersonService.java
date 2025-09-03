package code.service;

import code.dao.PersonTransactionsDAO;
import code.models.Person;
import java.io.IOException;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class PersonService {
  @Autowired PersonService self; // bad idea
  @Autowired PersonTransactionsDAO personTransactionsDAO;

  public void createTwoPeopleWithManualTransaction() throws SQLException {
    personTransactionsDAO.saveWithManualTransaction(
        new Person(0, "First person", 100, "test1@test.com"),
        new Person(0, "Second person", 100, "test2@test.com"));
  }

  // rolls back RuntimeException or Error
  // will not be triggered by checked exceptions like SQLException (or IOException)!
  @Transactional(rollbackFor = IOException.class)
  public void createTwoPeopleWithAutoTransaction() throws SQLException {
    personTransactionsDAO.savePersonSuccessfully(
        new Person(0, "Success person", 100, "test3@test.com"));
    //    personTransactionsDAO.savePersonSuccessfully(
    //        new Person(0, "Success person-2", 100, "test4@test.com"));
    personTransactionsDAO.savePersonWithException(
        new Person(0, "Exception person", 100, "test4@test.com"));
  }

  @Transactional
  public void save(Person person) throws SQLException {
    System.out.println(
        ">> Transaction active? " + TransactionSynchronizationManager.isActualTransactionActive());
    personTransactionsDAO.savePersonSuccessfully(person);
  }

  public void internalCallSave(Person person) throws SQLException {
    // do something first - validation, business logic etc.
//    this.save(person);
    self.save(person);
  }
}
