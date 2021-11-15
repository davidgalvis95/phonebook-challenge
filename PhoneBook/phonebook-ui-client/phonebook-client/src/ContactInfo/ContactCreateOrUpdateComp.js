import { React, useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";
import { useNavigate } from 'react-router-dom';
import {BsCheckCircleFill} from "react-icons/bs"



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
      navigate('/contacts');
      
    }
  };

  const mapContactReqProperties = (obj) => {
    obj['firstName'] = fName;
    obj['lastName'] = lName;
    obj['phoneNumber'] = phone;
    return obj;
  };

  const handleFirstNameChange = (event) => setFName(event.target.value);
  const handleLastNameChange = (event) => setLName(event.target.value);
  const handlePhoneNameChange = (event) => setPhone(event.target.value);

  const updatingComponent = (
    <div>
      <Grid container>
        <Grid item xs={4}>
          <div>First name</div>
          <input type="text" value={fName} onChange={handleFirstNameChange} />
        </Grid>
        <Grid item xs={4}>
          <div>Last name</div>
          <input type="text" value={lName} onChange={handleLastNameChange} />
        </Grid>
        <Grid item xs={3}>
          <div>Phone</div>
          <input type="text" value={phone} onChange={handlePhoneNameChange} />
        </Grid>
        <Grid item xs={2}>
          <div onClick={() => createOrUpdateContact()}><BsCheckCircleFill/></div>
        </Grid>
      </Grid>
    </div>
  );

  return (
    <React.Fragment>
      <Grid item xs={12}>
        {updatingComponent}
      </Grid>
    </React.Fragment>
  );
};

export default ContactReadingComp;
