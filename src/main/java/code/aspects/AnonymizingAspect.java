package code.aspects;

import code.models.Person;
import java.util.Set;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnonymizingAspect {

  private static final Set<String> SECRET_LIST = Set.of("Laura Hall", "Oliver King");

  @AfterReturning(
      pointcut = "execution(code.models.Person code.dao.PersonDAO.*(..))",
      returning = "person")
  public void anonymizePerson(Person person) {
    if (person != null && SECRET_LIST.contains(person.getName())) {
      person.setEmail("*****@*****.com");
    }
  }
}
