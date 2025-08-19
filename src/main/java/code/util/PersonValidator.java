package code.util;

import code.dao.PersonDAO;
import code.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private PersonDAO personDAO;

    @Autowired
    PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> existingPerson = personDAO.show(person.getEmail());
        if (existingPerson.isPresent() && existingPerson.get().getId() != person.getId()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}
