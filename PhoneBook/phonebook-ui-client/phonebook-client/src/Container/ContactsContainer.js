import { Grid } from "@material-ui/core";
import Card from "./Card";
import ContactReadingComp from "../ContactInfo/ContactReadingComp";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";

const ContactsContainer = () => {
  const phoneBookApiState = useSelector((state) => state.phoneBookApi);
  const [contactsComponent, setContactsComponent] = useState(null);
  const { sendRequestToGetMatchingContacts } = usePhoneBookApi();

  useEffect(() => {
    //compare arrays and detect if they have changed
    buildContactsComponent(contacts);
  }, []);

  useEffect(() => {
    //compare arrays and detect if they have changed
    buildContactsComponent(contacts);
  }, []);

  function refresh() {
    sendRequestToGetMatchingContacts("");
  }

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
};
