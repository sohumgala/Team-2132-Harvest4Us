
import React from 'react';

function FieldEditOn(props) {
    var returnField;
    if (props.textArea) {
        returnField = <textarea style={{width: "40vw"}} name={props.field} value={props.fieldValue} onChange={props.handleInputChange} />
    } else {
        returnField = <input style={{minWidth: "30%"}} name={props.field} value={props.fieldValue} id={props.fieldID} pattern={props.pattern} onChange={props.handleInputChange} />
    }
    return (
        returnField
    )
}

export default FieldEditOn;