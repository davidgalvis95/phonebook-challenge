package com.livebox.phonebookapi.service;

import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.model.CreateOrUpdateContactRequest;
import com.livebox.phonebookapi.repository.PhoneBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneBookServiceTest {

    @Mock
    private PhoneBookRepository phoneBookRepository;

    private PhoneBookService phoneBookService;

    private List<Contact> contacts;

    @BeforeEach
    void setUp(){

        contacts = buildContactList();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize( 5 );
        executor.setMaxPoolSize( 10 );
        executor.setQueueCapacity( 10 );
        executor.initialize();
        phoneBookService = new PhoneBookServiceImpl(phoneBookRepository, executor);
    }

    @Test
    void searchMultipleParamsReturnResult(){

        when(phoneBookRepository.findAll()).thenReturn(contacts);
        when(phoneBookRepository.filterContactByMatchingMultipleParameters(12345L, "fi", "la")).thenReturn(contacts.subList(1,2));
        when(phoneBookRepository.filterContactByMatchingMultipleParameters(12345L, "fi la", "")).thenReturn(contacts.subList(1,2));
        when(phoneBookRepository.filterContactByMatchingMultipleParameters(12345L, "", "fi la")).thenReturn(contacts.subList(1,2));
        when(phoneBookRepository.filterContactsByMatchingName("first3")).thenReturn(Collections.singletonList(contacts.get(2)));
        when(phoneBookRepository.filterContactsByMatchingNumber(12345L)).thenReturn(Collections.singletonList(contacts.get(0)));

        var response1 = phoneBookService.searchContactsByCriteria(Arrays.asList("12345", "fi", "la"));
        var response2 = phoneBookService.searchContactsByCriteria(Collections.emptyList());
        var response3 = phoneBookService.searchContactsByCriteria(Collections.singletonList("12345"));
        var response4 = phoneBookService.searchContactsByCriteria(Collections.singletonList("first3"));

        verify(phoneBookRepository).findAll();
        verify(phoneBookRepository).filterContactByMatchingMultipleParameters(12345L, "fi", "la");
        verify(phoneBookRepository).filterContactByMatchingMultipleParameters(12345L, "fi la", "");
        verify(phoneBookRepository).filterContactByMatchingMultipleParameters(12345L, "", "fi la");
        verify(phoneBookRepository).filterContactsByMatchingName("first3");
        verify(phoneBookRepository).filterContactsByMatchingNumber(12345L);

        assertEquals(contacts.subList(1,2), response1);
        assertEquals(contacts, response2);
        assertEquals(Collections.singletonList(contacts.get(0)), response3);
        assertEquals(Collections.singletonList(contacts.get(2)), response4);
    }

    @Test
    void createNewContact(){
        when(phoneBookRepository.createContact(any(Contact.class))).thenReturn(contacts.get(0));
        var response = phoneBookService.createContact(buildReqFromContact(contacts.get(0)));
        verify(phoneBookRepository).createContact(any(Contact.class));
        assertEquals(contacts.get(0), response);
    }

    @Test
    void deleteContact(){
        phoneBookService.deleteContact(contacts.get(0).getId());
        verify(phoneBookRepository).deleteContact(eq(contacts.get(0).getId()));
    }

    @Test
    void updateContact(){
        when(phoneBookRepository.updateContact(any(Contact.class), eq(contacts.get(0).getId()))).thenReturn(contacts.get(1));
        var response = phoneBookService.updateContact(buildReqFromContact(contacts.get(0)), contacts.get(0).getId());
        verify(phoneBookRepository).updateContact(any(Contact.class),eq(contacts.get(0).getId()));
        assertEquals(contacts.get(1), response);
    }

    private static List<Contact> buildContactList(){
        return Arrays.asList(Contact.builder()
                        .id(UUID.randomUUID())
                        .firstName("first1")
                        .lastName("last1")
                        .phoneNumber(12345L)
                        .build(),
                Contact.builder()
                        .id(UUID.randomUUID())
                        .firstName("first2")
                        .lastName("last2")
                        .phoneNumber(67890L)
                        .build(),
                Contact.builder()
                        .id(UUID.randomUUID())
                        .firstName("first3")
                        .lastName("last3")
                        .phoneNumber(98765L)
                        .build());
    }

    private static CreateOrUpdateContactRequest buildReqFromContact(final Contact contact){
        return CreateOrUpdateContactRequest.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phoneNumber(contact.getPhoneNumber())
                .build();
    }
}
