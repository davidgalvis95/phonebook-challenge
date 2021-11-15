import { Grid } from "@material-ui/core";
import Card from "./Card";
import ContactReadingComp from "../ContactInfo/ContactReadingComp";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";

const ContactsContainer = () => {
  const contacts = useSelector(
    (state) =>
      state.phoneBookApi.data.getContactsData?.response?.matchingContacts ?? ""
  );
  const [localContacts, setLocalContacts] = useState([]);
  const [contactsComponent, setContactsComponent] = useState(null);
  const { sendRequestToGetMatchingContacts } = usePhoneBookApi();

  useEffect(() => {
    refresh();
    processContacts();
  }, []);

  useEffect(() => {
    processContacts();
  }, [contacts]);

  function refresh() {
    sendRequestToGetMatchingContacts("");
  }

  const processContacts = () => {
    if (contacts !== "") {
      if (JSON.stringify(localContacts) != JSON.stringify(contacts)) {
        setLocalContacts(contacts);
        buildContactsComponent(contacts);
      }
    }
  };

  const buildContactsComponent = (contacts) => {
    const contactsUI = contacts.map((contact) => {
      //TODO make implementation of email in api and UI
      return (
        <Grid container>
          <ContactReadingComp
            key={contact.id}
            firstName={contact.firstName}
            lastName={contact.lastName}
            phoneNumber={contact.phoneNumber}
            email={"example@example.com"}
            refresh={refresh}
          />
        </Grid>
      );
    });

    setContactsComponent(contactsUI);
  };

  render(
    <Card>
      <div>{contactsComponent}</div>
    </Card>
  );
}


export default ContactsContainer;