package com.livebox.phonebookapi.service;

import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.model.CreateOrUpdateContactRequest;
import com.livebox.phonebookapi.repository.PhoneBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

@Service
@AllArgsConstructor
public class PhoneBookServiceImpl implements PhoneBookService{

    private final PhoneBookRepository phoneBookRepository;

    private final Executor taskExecutor;

    @Override
    @Transactional
    public List<Contact> searchContactsByCriteria(final List<String> parameters) {
        if(parameters.isEmpty()){
            return phoneBookRepository.findAll();
        }
            ContactLookupStrategy contactLookupStrategy;
            if(parameters.size() == 1){
                contactLookupStrategy = new SingleParamLookupStrategy(phoneBookRepository);
                return contactLookupStrategy.getSearchingParameters(parameters.get(0));
            }else{
                contactLookupStrategy = new MultipleParamsLookupStrategy(phoneBookRepository, taskExecutor);
                return contactLookupStrategy.getSearchingParameters(parameters);
            }

    }

    @Override
    @Transactional
    public void deleteContact(final UUID id) {
        try{
            phoneBookRepository.deleteContact(id);
        }catch(final Exception exception){
            throw new RuntimeException("Something went wrong while deleting contact");
        }
    }

    @Override
    @Transactional
    public Contact updateContact(final CreateOrUpdateContactRequest contactRequest, final UUID id) {

        final Contact contact = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .phoneNumber(contactRequest.getPhoneNumber())
                .build();
        try{
            return phoneBookRepository.updateContact(contact, id);
        }catch(final Exception exception){
            throw new RuntimeException("Something went wrong while updating contact");
        }
    }

    @Override
    @Transactional
    public Contact createContact(final CreateOrUpdateContactRequest contactRequest) {
        try {
            return phoneBookRepository.createContact(Contact.builder()
                    .firstName(contactRequest.getFirstName())
                    .lastName(contactRequest.getLastName())
                    .phoneNumber(contactRequest.getPhoneNumber())
                    .build());
        }catch (final Exception exception){
            throw new RuntimeException("Something went wrong while creating contact");
        }
    }
}
