import { useState, useEffect, useCallback } from "react";
import { shallowEqual, useSelector } from "react-redux";
import Card from "./Card";
import ContactReadingComp from "../ContactInfo/ContactReadingComp";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";

const ContactsContainer = () => {
  const searchResponse = useSelector(
    (state) => state.phoneBookApi.data.getContactsData?.response, shallowEqual
  );

  const newContact = useSelector(
    (state) => state.phoneBookApi.data.createContactData?.response
  );

  const updatedContact = useSelector(
    (state) => state.phoneBookApi.data.updateContactData?.response
  );

  const deletedResponse = useSelector(
    (state) => state.phoneBookApi.data.deleteContactData?.message
  );
  const loading = useSelector((state) => state.phoneBookApi.loading);
  const [localContacts, setLocalContacts] = useState([]);
  const [contactsComponent, setContactsComponent] = useState(null);
  const { sendRequestToGetAllTheContacts } = usePhoneBookApi();

  useEffect(() => {
    additionHandler();
  }, [newContact]);

  useEffect(() => {
    processContacts();
  }, [JSON.stringify(searchResponse?.matchingContacts)]);

  useEffect(() => {
    contactRemovalHandler();
  }, [deletedResponse]);

  useEffect(() => {
    contactUpdateHandler();
  },[updatedContact])


  const processContacts = useCallback(() => {
    if (searchResponse?.matchingContacts != null) {
        setLocalContacts(searchResponse.matchingContacts);
        buildContactsComponent(searchResponse.matchingContacts);
    }
  },[]);

  const contactRemovalHandler = useCallback(() => {
    if (deletedResponse && !loading) {
      const contactsNew = [...localContacts];
      const contactsWithoutDeleted = contactsNew.filter(
        (contact) => contact.id !== deletedResponse.split("'")[1]
      );
      buildContactsComponent(contactsWithoutDeleted);
      setLocalContacts(contactsWithoutDeleted);
    }
  },[]);

  const contactUpdateHandler = useCallback(() => {
    if (!loading && updatedContact) {
    sendRequestToGetAllTheContacts();
    const contactsNew = [...localContacts];
    contactsNew.splice(
      contactsNew.findIndex((contact) => contact.id === updatedContact.id),
      1,
      updatedContact
    );
    setLocalContacts(contactsNew);
    buildContactsComponent(contactsNew);
    }
  },[]);

  const additionHandler = useCallback(() => {
    if (!loading && newContact) {
      const contactsNew = [...localContacts];
      contactsNew.push(newContact);
      setLocalContacts(contactsNew);
      buildContactsComponent(contactsNew);
    }
  },[]);

  function capitalize(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

  const buildContactsComponent = (contacts) => {
    let contactsUI = null;
    if (!loading && searchResponse) {
      contactsUI = contacts.map((contact) => {
        //TODO make implementation of email in api and UI
        return (
            <ContactReadingComp
              key={contact.id}
              firstName={capitalize(contact.firstName)}
              lastName={capitalize(contact.lastName)}
              phoneNumber={contact.phoneNumber}
              emailAddress={"example@example.com"}
              dataId={contact.id}
              handlePostUpdateFromContainer={contactUpdateHandler}
            />
        );
      });
      setContactsComponent(contactsUI);
    }
  };

  return (
    <Card>
      <div>{contactsComponent}</div>
    </Card>
  );
};

export default ContactsContainer;
