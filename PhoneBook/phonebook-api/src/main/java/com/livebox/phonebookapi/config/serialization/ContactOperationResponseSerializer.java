package com.livebox.phonebookapi.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.model.ContactOperationResponse;
import com.livebox.phonebookapi.model.ContactSearchOutput;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ContactOperationResponseSerializer extends JsonSerializer<ContactOperationResponse> {

    public ContactOperationResponseSerializer() {
        super();
    }

    @Override
    public void serialize(ContactOperationResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value instanceof ContactSearchOutput){
            final ContactSearchOutput contactSearchOutput = (ContactSearchOutput) value;
            gen.writeStartObject();
            gen.writeFieldName("matchingContacts");
            gen.writeStartArray();
            for(Contact contact : contactSearchOutput.getMatchingContacts()){
                writeContactField(gen, contact);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }else{
            final Contact contact = (Contact) value;
            writeContactField(gen, contact);
        }
    }

    private void writeContactField(JsonGenerator gen, Contact contact) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id",contact.getId().toString());
        gen.writeStringField("firstName",contact.getFirstName());
        gen.writeStringField("lastName", contact.getLastName());
        gen.writeNumberField("phoneNumber",contact.getPhoneNumber());
        gen.writeEndObject();
    }
}