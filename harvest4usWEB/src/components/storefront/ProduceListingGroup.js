import React from "react";
import ProduceListing from "./ProduceListing.js"
/*
const produceListed = [
    {
        key: "1111",
        produceCategory: "Apple",
        produceType: "Granny Smith",
        usdaGrade: "A",
        price: "5.49",
        unit: "lb",
        availableQuantity: "3",
        organic: true,
        dateEdited: "9/21/21",
        active: true,
        disableEdits: true
    },
    {
        key: "1112",
        produceCategory: "Apple",
        produceType: "Pink Lady",
        usdaGrade: "B",
        price: "2.49",
        unit: "lb",
        availableQuantity: "8",
        organic: false,
        dateEdited: "9/25/21",
        active: false,
        disableEdits: true
    },
    {
        key: "1113",
        produceCategory: "Apple",
        produceType: "Red Delicious",
        usdaGrade: "C",
        price: "1.23",
        unit: "lb",
        availableQuantity: "24",
        organic: false,
        dateEdited: "9/27/21",
        active: true,
        disableEdits: true
    }
];
*/
function ProduceListingGroup(props) {
    var listings = [];
    props.itemsArr.map((item) => {
        listings.push(<ProduceListing listing={item} clicked={props.handleEditButton} handleEdits={props.handleEdits} handleDelete={props.handleDelete}/>);
    });

        return(
            <div style={{display:"flex", flexDirection: "row", flexWrap: "wrap", justifyContent: "flex-start", listStyle: "none"}}>
                {listings}
            </div>
        );
}

export default ProduceListingGroup;