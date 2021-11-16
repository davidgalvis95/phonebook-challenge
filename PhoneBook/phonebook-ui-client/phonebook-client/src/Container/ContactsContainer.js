import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import Card from "./Card";
import ContactReadingComp from "../ContactInfo/ContactReadingComp";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";


//TODO improve glitches when loading the updates and creations of contacts into the search
const ContactsContainer = () => {
  const searchResponse = useSelector(
    (state) =>
      state.phoneBookApi.data.getContactsData?.response
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
  const { sendRequestToGetMatchingContacts } = usePhoneBookApi();

  useEffect(() => {
    processContacts();
    additionHandler();
  }, [searchResponse?.matchingContacts, loading, newContact]);

  useEffect(() => {
    processContacts();
    contactUpdateHandler();
  }, [searchResponse?.matchingContacts, loading, updatedContact]);

  useEffect(() => {
    processContacts();
    contactRemovalHandler();
  }, [searchResponse?.matchingContacts, loading, deletedResponse]);

  function refresh() {
    sendRequestToGetMatchingContacts("");
  }

  const processContacts = () => {
    if (searchResponse?.matchingContacts != null) {
      if (JSON.stringify(localContacts) !== JSON.stringify(searchResponse?.matchingContacts)) {
        setLocalContacts(searchResponse.matchingContacts);
        buildContactsComponent(searchResponse.matchingContacts);
      }
    }
  };

  const contactRemovalHandler = () => {
    if (deletedResponse && !loading) {
      const contactsNew = [...localContacts];
      const contactsWithoutDeleted = contactsNew.filter(
        (contact) => contact.id !== deletedResponse.split("'")[1]
      );
      setLocalContacts(contactsWithoutDeleted);
      buildContactsComponent(contactsWithoutDeleted);
    }
  };

  const contactUpdateHandler = () => {
    if (!loading && updatedContact) {
      const contactsNew = [...localContacts];
      const contactToUpdate = contactsNew.filter(
        (contact) => contact.id === updatedContact.id
      );
      contactsNew.splice(
        contactsNew.findIndex((contact) => contact.id === updatedContact.id),
        1,
        updatedContact
      );
      setLocalContacts(contactsNew);
      buildContactsComponent(contactsNew);
    }
  };

  const additionHandler = () => {
    if (!loading && newContact) {
      const contactsNew = [...localContacts];
      contactsNew.push(newContact);
      setLocalContacts(contactsNew);
      buildContactsComponent(contactsNew);
    }
  };

  function capitalize(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

  const buildContactsComponent = (contacts) => {
    const contactsUI = contacts.map((contact) => {
      //TODO make implementation of email in api and UI
      return (
        <div>
          <ContactReadingComp
            key={contact.id}
            firstName={capitalize(contact.firstName)}
            lastName={capitalize(contact.lastName)}
            phoneNumber={contact.phoneNumber}
            emailAddress={"example@example.com"}
            refresh={refresh}
            dataId={contact.id}
          />
        </div>
      );
    });

    setContactsComponent(contactsUI);
  };

  return (
    <Card>
      <div>{loading || !searchResponse ? null : contactsComponent}</div>
    </Card>
  );
};

export default ContactsContainer;
