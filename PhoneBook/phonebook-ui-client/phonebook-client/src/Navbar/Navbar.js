import React from "react";
import { NavLink } from "react-router-dom";
import classes from "./Navbar.module.css";
import { BsFillPersonPlusFill } from "react-icons/bs";
import Search from "../Search/Search";
import PhoneBookLogo from "../Images/phonebook.png";

const navbar = () => {
  return (
    <div className={classes.Navbar}>
      <NavLink to="/contacts">
        <img
          src={PhoneBookLogo}
          alt="Phonebook logo"
          className={classes.logo}
        />
      </NavLink>
      <div className={classes.Search}>
        <Search />
      </div>
      <div className={classes.NavBtn}>
        <NavLink to="/contact/creation">
          <BsFillPersonPlusFill className={classes.newContactLogo}/>
        </NavLink>
      </div>
    </div>
  );
};

export default navbar;
