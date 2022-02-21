import { Form, Row, InputGroup } from 'react-bootstrap';
import React from 'react';

function ProduceAddTypes(props) {
  const formattedTypes = [];
  (props.types).map((item) => {
    formattedTypes.push(
        <option value={item.toString()} key={"11" + item.toString()}>{item}</option>
    );
  });


  return (
    <Row className="m-3">
    <InputGroup className="my-2">
      <InputGroup.Text>Category</InputGroup.Text> 
      <Form.Select
        defaultValue={"test"}
        onChange={props.handleTypeClick}
      > 
        {formattedTypes} 
      </Form.Select>
    </InputGroup>
    </Row>
  );
}

export default ProduceAddTypes

