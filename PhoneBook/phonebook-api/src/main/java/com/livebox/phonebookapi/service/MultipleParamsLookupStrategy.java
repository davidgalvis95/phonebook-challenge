package com.livebox.phonebookapi.service;

import com.livebox.phonebookapi.exception.MoreThanOnePhoneNumberException;
import com.livebox.phonebookapi.model.Contact;
import com.livebox.phonebookapi.repository.PhoneBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MultipleParamsLookupStrategy extends ContactLookupStrategy {

    private final PhoneBookRepository phoneBookRepository;

    private final Executor taskExecutor;

    public MultipleParamsLookupStrategy(final PhoneBookRepository phoneBookRepository,
                                        final Executor taskExecutor) {
        this.phoneBookRepository = phoneBookRepository;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public <T> List<Contact> getSearchingParameters(List<T> params) {

        final List<Integer> phoneNumber = params.stream()
                .map(element -> {
                    try {
                        return Integer.valueOf((String) element);
                    } catch (final NumberFormatException numberFormatException) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        try {
            if (phoneNumber.isEmpty()) {

                return processSearch(params, null);

            } else if (phoneNumber.size() == 1) {
                final List<String> paramsWithoutNumber = params.stream()
                        .filter(element -> {
                            try {
                                Integer.valueOf((String) element);
                                return false;
                            } catch (final NumberFormatException numberFormatException) {
                                return true;
                            }
                        })
                        .map(element -> (String) element)
                        .collect(Collectors.toList());

                return processSearch(paramsWithoutNumber, phoneNumber.stream().findFirst().orElse(null));

            } else {
                throw new MoreThanOnePhoneNumberException("There are more phone numbers than the expected ones");
            }
        } catch (Exception exception) {
            log.error("Something went wrong while searching for results");
            return Collections.emptyList();
        }
    }

    private <T> List<Contact> processSearch(final List<T> parameters, final Integer phoneNumber) {
        final List<Contact> nonReversedResults = executeSearchesForPossibleCombinations(parameters, phoneNumber);

        Collections.reverse(parameters);

        return Stream.of(nonReversedResults, executeSearchesForPossibleCombinations(parameters, phoneNumber))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private <T> List<Contact> executeSearchesForPossibleCombinations(final List<T> parameters, final Integer phoneNumber) {
        final List<Pair<String, String>> combinationsForSearch = getAllThePossibleCombinationsOfFirstNameAndLastName((List<String>) parameters);

        final List<Contact> getResultsOfPossibleMatches = combinationsForSearch.stream()
                .map(combination -> findContactsByCriteria(phoneNumber, combination.getFirst(), combination.getSecond()))
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        return getResultsOfPossibleMatches;
    }

    private CompletableFuture<List<Contact>> findContactsByCriteria(final Integer phoneNumber, final String firstName, final String lastName) {
        return CompletableFuture.supplyAsync(() -> phoneBookRepository.filterContactByMatchingMultipleParameters(phoneNumber, firstName, lastName), taskExecutor);
    }

    private List<Pair<String, String>> getAllThePossibleCombinationsOfFirstNameAndLastName(final List<String> names) {
        List<String> firstNameWords = new ArrayList<>();
        List<Pair<String, String>> pairsOfNames = new ArrayList<>();
        for (int i = 0; i <= names.size(); i++) {
            final StringBuilder firstName = new StringBuilder();
            final StringBuilder lastName = new StringBuilder();
            firstNameWords.forEach(word -> {
                if (firstNameWords.indexOf(word) == 0) {
                    firstName.append(word);
                } else {
                    firstName.append(" ").append(word);
                }
            });
            final List<String> lastNameWords = names.stream()
                    .filter(name -> !firstNameWords.contains(name))
                    .collect(Collectors.toList());
            lastNameWords.forEach(word -> {
                if (lastNameWords.indexOf(word) == 0) {
                    lastName.append(word);
                } else {
                    lastName.append(" ").append(word);
                }
            });
            pairsOfNames.add(Pair.of(firstName.toString(), lastName.toString()));
            if (i < names.size()) firstNameWords.add(names.get(i));
        }
        return pairsOfNames;
    }
}
