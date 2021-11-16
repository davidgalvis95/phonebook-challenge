import { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import ContactCreateOrUpdateComp from "./ContactCreateOrUpdateComp";
import { RiDeleteBin5Fill } from "react-icons/ri";
import { GrUpdate } from "react-icons/gr";
import { BsPersonCircle } from "react-icons/bs";
import Card from "../Container/Card";
import classes from "./ContactReadingComponent.module.css";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";
import { useNavigate } from "react-router-dom";

const ContactReadingComp = ({
  firstName,
  lastName,
  phoneNumber,
  emailAddress,
  contactRemovalHandler,
  dataId
}) => {
  const [fName, setFName] = useState("");
  const [lName, setLName] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [id, setId] = useState("");
  const [updating, setUpdating] = useState(false);
  const navigate = useNavigate();
  const { sendRequestToDeleteAContact } =
  usePhoneBookApi();

  useEffect(() => {
    setFName(firstName);
    setLName(lastName);
    setPhone(phoneNumber);
    setEmail(emailAddress);
  }, []);

  const handlePreUpdate = () => {
    setUpdating(true);
    setId(dataId);
  };

  const handlePostUpdate = () => {
    setUpdating(false);
    navigate("/");
  };

  const handleDelete = () => {
    sendRequestToDeleteAContact(dataId);
    navigate("/");
  };

  const readingComponent = (
    <Card
      style={{
        backgroundColor: "#ffffff",
        width: "90%",
      }}
    >
      <Grid container>
        <Grid item xs={1}>
          <div>
            <BsPersonCircle className={classes.icon}/>
          </div>
        </Grid>
        <Grid item xs={3}>
          <div className={classes.description}>Full Name</div>
          <div className={classes.values}>{`${fName} ${lName}`}</div>
        </Grid>
        <Grid item xs={3}>
          <div className={classes.description}>Phone</div>
          <div className={classes.values}>{phone}</div>
        </Grid>
        <Grid item xs={3}>
          <div className={classes.description}>Email Address</div>
          <div className={classes.values}>{email}</div>
        </Grid>
        <Grid item xs={1}>
          <div onClick={handlePreUpdate}>
            <GrUpdate className={classes.icon}/>
          </div>
        </Grid>
        <Grid item xs={1}>
          <div onClick={handleDelete}>
            <RiDeleteBin5Fill className={classes.icon}/>
          </div>
        </Grid>
      </Grid>
    </Card>
  );

  return (
    <div>
      {updating ? (
        <ContactCreateOrUpdateComp
          name1={fName}
          name2={lName}
          id={id}
          phoneX={phone}
          operation={"UPDATE"}
          handlePostCreateOrUpdate={handlePostUpdate}
        />
      ) : (
        <Grid item xs={12}>
          {readingComponent}
        </Grid>
      )}
    </div>
  );
};

export default ContactReadingComp;
