import React from 'react';
import { ListGroup } from 'react-bootstrap';

function ProduceAddCategories(props) {

    const formattedCategories = [];
    (props.categories).map((item) => {
        formattedCategories.push(
            <ListGroup.Item action key={item.toString()} id={item.toString()} onClick={props.handleCategoryClick}> {item} </ListGroup.Item>
        );
    });

    return <ListGroup className="m-3" style={{height: "25vh", overflowY: "scroll"}}>{formattedCategories}</ListGroup>;
    
}

export default ProduceAddCategories;