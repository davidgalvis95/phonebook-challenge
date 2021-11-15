import { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import ContactCreateOrUpdateComp from "./ContactCreateOrUpdateComp";
import { RiDeleteBin5Fill } from "react-icons/ri";
import { GrUpdate } from "react-icons/gr";

const ContactReadingComp = ({
  firstName,
  lastName,
  phoneNumber,
  emailAddress,
  refresh,
}) => {
  const [fName, setFName] = useState("");
  const [lName, setLName] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [id, setId] = useState("");
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    setFName(firstName);
    setLName(lastName);
    setPhone(phoneNumber);
    setEmail(emailAddress);
  }, []);

  const handlePreUpdate = (event) => {
    setUpdating(true);
    console.log(event.target.getAttribute("key"));
    setId(event.target.getAttribute("key"));
  };

  const handlePostUpdate = () => {
    setUpdating(false);
    refresh();
  };

  const handleDelete = (event) => {
    console.log(event.target.getAttribute("key"));
    refresh();
  };

  const readingComponent = (
    <div>
      <Grid container>
        <Grid item xs={1}>
          <div></div>
        </Grid>
        <Grid item xs={4}>
          <div>Name</div>
          <div>{`${fName} ${lName}`}</div>
        </Grid>
        <Grid item xs={2}>
          <div>Phone</div>
          <div>{phone}</div>
        </Grid>
        <Grid item xs={3}>
          <div>Email Address</div>
          <div>{email}</div>
        </Grid>
        <Grid item xs={1}>
          <div onClick={handlePreUpdate}>
            <GrUpdate />
          </div>
        </Grid>
        <Grid item xs={1}>
          <div onClick={handleDelete}>
            <RiDeleteBin5Fill />
          </div>
        </Grid>
      </Grid>
    </div>
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
