import React from 'react';
import {NavLink} from "react-router-dom";
import classes from './Navbar.module.css';
import {BsFillPersonPlusFill} from "react-icons/bs"
import Search from "../Search/Search"


const navbar = () => {
    return (
            <div className={classes.Navbar}>
                <NavLink to="/contacts">
                    <img src="../Images/phonebook.png" alt="Phonebook logo"/>
                </NavLink>
                <div><Search/></div>
                <div className={classes.NavBtn}>
                    <NavLink className={classes.NavBtnLink} to="/contact/creation"><BsFillPersonPlusFill/></NavLink>
                </div>
            </div>
    )
}

export default navbar;