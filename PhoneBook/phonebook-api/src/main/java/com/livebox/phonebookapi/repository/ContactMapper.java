package com.livebox.phonebookapi.repository;


import com.livebox.phonebookapi.model.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ContactMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Contact.builder()
                .id((UUID) resultSet.getObject("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .phoneNumber(resultSet.getLong("phone_number"))
                .build();
    }
}
