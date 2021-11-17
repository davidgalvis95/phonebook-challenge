package com.livebox.phonebookapi.apitests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livebox.phonebookapi.config.PhonebookTestDBConfig;
import com.livebox.phonebookapi.model.*;
import com.livebox.phonebookapi.repository.PhoneBookRepository;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Testcontainers
@TestPropertySource(locations = {"classpath:application-test.yml"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PhonebookApiIntegrationTest {

    public static final String SEARCHING_PARAMS = "searchingParams";

    public static final String ID_PARAMS = "id";

    public static final String CREATION_PAYLOAD = "{\n\"firstName\":\"David\",\n\"lastName\":\"Galvis\",\n\"phoneNumber\":11111111111\n}";

    @ClassRule
    public static PostgreSQLContainer<PhonebookTestDBConfig> container = PhonebookTestDBConfig.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PhoneBookRepository phoneBookRepository;

    @BeforeAll
    static void setUp() {
        container.start();
    }


    @Test
    void getAllTheContacts() throws Exception {

        final String response = mockMvc.perform(
                get(PhoneBookApiPaths.PHONEBOOK_API_READER_FULL_PATH)
                        .header(ACCEPT, APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final PhoneBookApiResponse phoneBookApiResponse = parseResponse(response);

        assertNotNull(phoneBookApiResponse);
        final List<Contact> contacts = ((ContactSearchOutput) phoneBookApiResponse.getResponse()).getMatchingContacts();

        assertEquals(1, contacts.stream()
                .filter(contact -> contact.getFirstName().equals("arun")
                        && contact.getLastName().equals("kart")
                        && contact.getPhoneNumber() == 4158679089L).count());

        assertEquals(1, contacts.stream()
                .filter(contact -> contact.getFirstName().equals("nolux")
                        && contact.getLastName().equals("fernandez")
                        && contact.getPhoneNumber() == 3102930291L).count());

        assertEquals(1, contacts.stream()
                .filter(contact -> contact.getFirstName().equals("juan")
                        && contact.getLastName().equals("torus")
                        && contact.getPhoneNumber() == 3012390930L).count());
    }

    @Test
    void createSearchUpdateAndDeleteContact() throws Exception {

        var x = objectMapper.readValue(CREATION_PAYLOAD, CreateOrUpdateContactRequest.class);
        final MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

        //Create contact
        final String creationResponse = mockMvc.perform(
                post(PhoneBookApiPaths.PHONEBOOK_API_UPDATE_FULL_PATH)
                        .contentType(MEDIA_TYPE_JSON_UTF8)
                        .accept(MEDIA_TYPE_JSON_UTF8)
                        .content(CREATION_PAYLOAD))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final PhoneBookApiResponse creationResponseParsed = parseResponse(creationResponse);

        assertEquals(1, phoneBookRepository.filterContactsByMatchingName("david").size());

        assertNotNull(creationResponseParsed.getResponse());
        final Contact contact = ((Contact) creationResponseParsed.getResponse());

        assertEquals("david", contact.getFirstName());
        assertEquals("galvis", contact.getLastName());
        assertEquals(11111111111L, contact.getPhoneNumber());

        //Search contacts passing new name as parameters
        final String searchingResponse = mockMvc.perform(
                get(PhoneBookApiPaths.PHONEBOOK_API_READER_FULL_PATH)
                        .param(SEARCHING_PARAMS, "David,Ga")
                        .contentType(MEDIA_TYPE_JSON_UTF8)
                        .accept(MEDIA_TYPE_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final PhoneBookApiResponse searchingResponseParsed = parseResponse(searchingResponse);

        assertNotNull(searchingResponseParsed);
        final List<Contact> matches = ((ContactSearchOutput) searchingResponseParsed.getResponse()).getMatchingContacts();

        assertEquals(1, matches.stream()
                .filter(match -> match.getFirstName().equals("david")
                        && match.getLastName().equals("galvis")
                        && match.getPhoneNumber() == 11111111111L).count());

        //Update contact
        final CreateOrUpdateContactRequest updatePayload = CreateOrUpdateContactRequest.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phoneNumber(22222222222L)
                .build();

        final String updateResponse = mockMvc.perform(
                put(PhoneBookApiPaths.PHONEBOOK_API_UPDATE_FULL_PATH)
                        .contentType(MEDIA_TYPE_JSON_UTF8)
                        .accept(MEDIA_TYPE_JSON_UTF8)
                        .param(ID_PARAMS, contact.getId().toString())
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final PhoneBookApiResponse updateResponseParsed = parseResponse(updateResponse);

        assertEquals(1, phoneBookRepository.filterContactsByMatchingNumber(22222222222L).size());

        assertNotNull(updateResponseParsed);
        final Contact contactUpdated = ((Contact) updateResponseParsed.getResponse());

        assertEquals("david", contactUpdated.getFirstName());
        assertEquals("galvis", contactUpdated.getLastName());
        assertEquals(22222222222L, contactUpdated.getPhoneNumber());

        //Delete contact
        final String deleteResponse = mockMvc.perform(
                delete(PhoneBookApiPaths.PHONEBOOK_API_UPDATE_FULL_PATH)
                        .contentType(MEDIA_TYPE_JSON_UTF8)
                        .accept(MEDIA_TYPE_JSON_UTF8)
                        .param(ID_PARAMS, contact.getId().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final PhoneBookApiResponse deleteResponseParsed = parseResponse(deleteResponse);

        assertTrue(phoneBookRepository.filterContactsByMatchingName("david").isEmpty());

        assertNotNull(deleteResponseParsed);
        assertNull(deleteResponseParsed.getResponse());
        assertTrue(deleteResponseParsed.getMessage().contains(contact.getId().toString()));
    }

    private PhoneBookApiResponse parseResponse(final String response) throws JsonProcessingException {
        return objectMapper.readValue(response, PhoneBookApiResponse.class);
    }
}
