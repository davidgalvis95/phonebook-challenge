package com.livebox.phonebookapi.service;

import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.repository.PhoneBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
public class SingleParamLookupStrategy extends ContactLookupStrategy{

    private final PhoneBookRepository phoneBookRepository;

    public SingleParamLookupStrategy(final PhoneBookRepository phoneBookRepository){
        this.phoneBookRepository = phoneBookRepository;
    }

    @Override
    public <T> List<Contact> getSearchingParameters(T param) {

        final String stringParam = (String) param;

        try{
            int phoneNumber = Integer.parseInt(stringParam);
            return phoneBookRepository.filterContactsByMatchingNumber(phoneNumber);
        }catch(NumberFormatException numberFormatException){
            return phoneBookRepository.filterContactsByMatchingName(stringParam);
        }catch(RuntimeException exception){
            log.error("Something went wrong while processing the request to the DB");
            return Collections.emptyList();
        }
    }
}
