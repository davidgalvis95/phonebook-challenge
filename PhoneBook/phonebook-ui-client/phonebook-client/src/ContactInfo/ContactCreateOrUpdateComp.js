import { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";
import { useNavigate } from "react-router-dom";
import { IoMdCheckmarkCircleOutline } from "react-icons//io";
import Card from "../Container/Card";
import classes from "./ContactCreateOrUpdateComp.module.css";

const ContactReadingComp = ({
  name1,
  name2,
  phoneX,
  id,
  operation,
  handlePostCreateOrUpdate,
}) => {
  const navigate = useNavigate();
  const [fName, setFName] = useState("");
  const [lName, setLName] = useState("");
  const [phone, setPhone] = useState("");
  const { sendRequestToUpdateAContact, sendRequestToCreateAContact } =
    usePhoneBookApi();

  useEffect(() => {
    setFName(name1);
    setLName(name2);
    setPhone(phoneX);
  }, []);

  const contactRequest = {
    firstName: "",
    lastName: "",
    phoneNumber: "",
  };

  const createOrUpdateContact = () => {
    const contact = mapContactReqProperties(contactRequest);
    if (operation === "UPDATE") {
      sendRequestToUpdateAContact(contact, id);
      handlePostCreateOrUpdate();
    } else if (operation === "CREATE") {
      sendRequestToCreateAContact(contact);
      navigate("/contacts");
    }
  };

  const mapContactReqProperties = (obj) => {
    obj["firstName"] = fName;
    obj["lastName"] = lName;
    obj["phoneNumber"] = phone;
    return obj;
  };

  const handleFirstNameChange = (event) => setFName(event.target.value);
  const handleLastNameChange = (event) => setLName(event.target.value);
  const handlePhoneNameChange = (event) => setPhone(event.target.value);

  const updatingComponent = (
    <Card
      style={
        operation === "UPDATE"
          ? {
              backgroundColor: "#30336b",
              width: "90%",
            }
          : {}
      }
    >
      <Grid container>
        <Grid item xs={4}>
          <div
            className={`${
              operation === "UPDATE"
                ? classes.textBoxUpdate
                : classes.textBoxCreate
            }`}
          >
            <div className={classes.description}>First Name</div>
            <input type="text" value={fName} onChange={handleFirstNameChange} />
          </div>
        </Grid>
        <Grid item xs={4}>
          <div
            className={`${
              operation === "UPDATE"
                ? classes.textBoxUpdate
                : classes.textBoxCreate
            }`}
          >
            <div className={classes.description}>Last Name</div>
            <input type="text" value={lName} onChange={handleLastNameChange} />
          </div>
        </Grid>
        <Grid item xs={3}>
          <div
            className={`${
              operation === "UPDATE"
                ? classes.textBoxUpdate
                : classes.textBoxCreate
            }`}
          >
            <div className={classes.description}>Phone</div>
            <input type="text" value={phone} onChange={handlePhoneNameChange} />
          </div>
        </Grid>
        <Grid item xs={1}>
          <div onClick={() => createOrUpdateContact()}>
            <IoMdCheckmarkCircleOutline className={`${
              operation === "UPDATE"
                ? classes.checkLogoUpdate
                : classes.checkLogoCreate
            }`} />
          </div>
        </Grid>
      </Grid>
    </Card>
  );

  return (
    <Grid item xs={12}>
      {updatingComponent}
    </Grid>
  );
};

export default ContactReadingComp;
