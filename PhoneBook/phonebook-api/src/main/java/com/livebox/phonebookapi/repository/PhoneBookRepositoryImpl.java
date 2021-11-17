package com.livebox.phonebookapi.repository;

import com.livebox.phonebookapi.model.Contact;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
public class PhoneBookRepositoryImpl implements PhoneBookRepository{

    private static final String INSERT_NEW_CONTACT = "INSERT INTO contacts(first_name, last_name, phone_number)" + 
            "VALUES (:firstName,:lastName, :phoneNumber)";

    private static final String UPDATE_CONTACT = "UPDATE contacts SET first_name=:firstName, last_name=:lastName," + 
            " phone_number=:phoneNumber WHERE id=:id";

    private static final String FIND_CONTACT_BY_FIRST_NAME = "SELECT id, first_name, last_name, phone_number FROM contacts " +
            "WHERE first_name LIKE :firstName";

    private static final String FIND_CONTACT_BY_LAST_NAME = "SELECT id, first_name, last_name, phone_number FROM contacts " +
            "WHERE last_name LIKE :lastName";

    private static final String FIND_CONTACT_BY_FIRST_NAME_AND_NUMBER = "SELECT id, first_name, last_name, phone_number FROM contacts " +
            "WHERE first_name LIKE :firstName AND phone_number LIKE :phoneNumber";

    private static final String FIND_CONTACT_BY_LAST_NAME_AND_NUMBER = "SELECT id, first_name, last_name, phone_number FROM contacts " +
            "WHERE last_name LIKE :lastName AND phone_number=:phoneNumber";

    private static final String DELETE_CONTACT = "DELETE FROM contacts WHERE id=:id";

    private static final String FIND_CONTACTS = "SELECT * FROM contacts";

    private static final String FIND_CONTACT_BY_PHONE_NUMBER = "SELECT id, first_name, last_name, phone_number " +
            "FROM contacts WHERE phone_number=:phoneNumber";;

    private static final String FIND_CONTACT_BY_ARGUMENTS = "SELECT id, first_name, last_name, phone_number FROM contacts" +
            " WHERE first_name LIKE :firstName AND last_name LIKE :lastName";

    private static final String FIND_CONTACT_BY_ARGUMENTS_WITH_NUMBER = "SELECT id, first_name, last_name, phone_number " +
            "FROM contacts WHERE first_name LIKE :firstName AND last_name LIKE :lastName AND phone_number=:phoneNumber";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Contact createContact(final Contact contact) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("firstName", contact.getFirstName())
                .addValue("lastName", contact.getLastName())
                .addValue("phoneNumber", contact.getPhoneNumber());
        namedParameterJdbcTemplate.update(INSERT_NEW_CONTACT, parameters, keyHolder, new String[]{"id"});
        contact.setId((UUID) keyHolder.getKeyList().get(0).get("id"));
        return contact;
    }

    @Override
    public Contact updateContact(final Contact contact, final UUID id) {
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("firstName", contact.getFirstName())
                .addValue("lastName", contact.getLastName())
                .addValue("phoneNumber", contact.getPhoneNumber());
        namedParameterJdbcTemplate.update(UPDATE_CONTACT, parameters);
        contact.setId(id);
        return contact;
    }

    @Override
    public void deleteContact(UUID id) {
        final Map<String, UUID> parameters = new HashMap<>();
        parameters.put("id", id);
        namedParameterJdbcTemplate.update(DELETE_CONTACT, parameters);
    }

    @Override
    public List<Contact> findAll() {
        return namedParameterJdbcTemplate.query(FIND_CONTACTS, new ContactMapper());
    }

    @Override
    public List<Contact> filterContactsByMatchingName(final String name) {

        return Stream.of(namedParameterJdbcTemplate.query(FIND_CONTACT_BY_FIRST_NAME, Map.of("firstName", name + "%"), new ContactMapper()),
                namedParameterJdbcTemplate.query(FIND_CONTACT_BY_LAST_NAME, Map.of("lastName", name + "%"), new ContactMapper()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> filterContactsByMatchingNumber(final Long number) {
        final Map<String, Long> parameters = new HashMap<>();
        parameters.put("phoneNumber", number);
        return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_PHONE_NUMBER, parameters, new ContactMapper());
    }

    @Override
    public List<Contact> filterContactByMatchingMultipleParameters(final Long number, final String firstName, final String lastName) {

        if(number == null){
            if(firstName.isEmpty()){
                final SqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("lastName", lastName + "%");
                return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_LAST_NAME, parameters, new ContactMapper());
            }
            else if(lastName.isEmpty()){
                final SqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("firstName", firstName + "%");
                return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_FIRST_NAME, parameters, new ContactMapper());
            }else {
                final SqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("firstName", firstName + "%")
                        .addValue("lastName", lastName + "%");
                return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_ARGUMENTS, parameters, new ContactMapper());
            }
        }else{
            if(firstName.isEmpty()){
                final SqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("lastName", lastName + "%")
                        .addValue("phoneNumber", number + "%");
                return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_LAST_NAME_AND_NUMBER, parameters, new ContactMapper());
            }
            else if(lastName.isEmpty()){
                final SqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("firstName", firstName + "%")
                        .addValue("phoneNumber", number + "%");
                return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_FIRST_NAME_AND_NUMBER, parameters, new ContactMapper());
            }else {
                final SqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("firstName", firstName + "%")
                        .addValue("lastName", lastName + "%")
                        .addValue("phoneNumber", number + "%");
                return namedParameterJdbcTemplate.query(FIND_CONTACT_BY_ARGUMENTS_WITH_NUMBER, parameters, new ContactMapper());
            }
        }
    }
}
