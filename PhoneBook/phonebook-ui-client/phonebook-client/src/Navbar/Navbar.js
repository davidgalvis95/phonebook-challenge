import React from 'react';
import {NavLink} from "react-router-dom";
import classes from './Navbar.module.css';
import WeatherLogo from '../Icons/weather/WeatherLogo';

const navbar = () => {
    return (
            <div className={classes.Navbar}>
                <NavLink to="/home">
                    <WeatherLogo className={classes.WeatherLogo}/>
                </NavLink>
                <div className={classes.Bars}/>
                <div className={classes.NavMenu}>
                    <NavLink className={classes.NavLink} to="/about" activeStyle="">
                        About
                    </NavLink>
                    <NavLink className={classes.NavLink} to="/services" activeStyle="">
                        Current Weather
                    </NavLink>
                    <NavLink className={classes.NavLink} to="/contact-us" activeStyle="">
                        Contact Us
                    </NavLink>
                </div>
                <div className={classes.NavBtn}>
                    <NavLink className={classes.NavBtnLink} to="/sign-in">Sign In</NavLink>
                    <NavLink className={classes.NavBtnLink} to="/sign-up">Sign Up</NavLink>
                </div>
            </div>
    )
}

export default navbar;