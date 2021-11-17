package com.livebox.phonebookapi.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.model.ContactOperationResponse;
import com.livebox.phonebookapi.model.ContactSearchOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class ContactOperationResponseDeserializer extends JsonDeserializer<ContactOperationResponse> {

    private ObjectMapper objectMapper;

    @Autowired
    public ContactOperationResponseDeserializer(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ContactOperationResponseDeserializer() {
        super();
    }

    @Override
    public ContactOperationResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final JsonNode node = parser.readValueAsTree();
        final JsonNode matchingContactsNode = node.get("matchingContacts");
        if (matchingContactsNode != null) {
            final List<Contact> matchingContacts = objectMapper.readerFor(new TypeReference<List<Contact>>() {
            }).readValue(matchingContactsNode);
            return ContactSearchOutput.builder().matchingContacts(matchingContacts).build();
        } else {
            return Contact.builder()
                    .id(UUID.fromString(node.get("id").textValue()))
                    .firstName(node.get("firstName").asText())
                    .lastName(node.get("lastName").asText())
                    .phoneNumber(node.get("phoneNumber").asLong())
                    .build();
        }
    }
}