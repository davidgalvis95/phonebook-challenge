import React from 'react';

import classes from './Card.module.css';

const Card = (props) => {

    const style = {
        backgroundColor: 'black',
    }

    return <div className={classes.card} style={props.style}>{props.children}</div>;
};

export default Card;