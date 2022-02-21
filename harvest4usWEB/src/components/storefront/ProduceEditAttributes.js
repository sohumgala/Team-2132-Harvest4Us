import { Formik } from 'formik';
import * as yup from 'yup';
import { Form, Row, Col, InputGroup, Button } from 'react-bootstrap';
import React from 'react';

const schema = yup.object().shape({
  price: yup.string().matches(/^\d{0,2}\.\d{2}?$/).required(),
  unit: yup.string().matches(/[A-Za-z]/).required(),
  availableQuantity: yup.string().matches(/^\d{0,2}\.\d{2}?$/).required(),
  active: yup.bool()
});

//A form for creating and editing produce attributes
function ProduceEditAttributes(props) {
  return (
    <Formik
      validationSchema={schema}
      validateOnChange={false}
      initialValues={{
        key: props.listing.product_id,
        usdaGrade: props.listing.usdaGrade,
        price: props.listing.price,
        unit: props.listing.unit,
        availableQuantity: props.listing.availableQuantity,
        organic: props.listing.organic,
        dateEdited: '',
        active: props.listing.active,
      }}
      onSubmit={(values) => {
          //var string = JSON.stringify(values, null, 2);
          console.log(values);
          props.submitHandle(values);
      }}
    >
      {({
        handleSubmit,
        handleChange,
        values,
        errors,
      }) => (
        <Form noValidate onSubmit={handleSubmit} className="m-3">
        <Row className="my-3">
            <Col>
                <div><b>USDA Grade: </b></div>
            </Col>
            <Col>
                {props.listing.usdaGrade}
            </Col>
        </Row>

        <Row className="my-3">
            <Form.Group controlId="validationFormik02">
            <InputGroup>
              <InputGroup.Text>Price</InputGroup.Text>
              <InputGroup.Text>$</InputGroup.Text>
              <Form.Control
                type="text"
                name="price"
                value={values.price}
                onChange={handleChange}
                placeholder="0.00"
                isInvalid={!!errors.price}
              />
                <Form.Control.Feedback type="invalid">
                        Price must be formatted as 0.00
                </Form.Control.Feedback>

                <Form.Select
                type="text"
                name="unit"
                value={values.unit}
                onChange={handleChange}
                isInvalid={!!errors.unit}
              >
                <option value="0">Select Unit</option>
                <option value="lbs">per Pound</option>
                <option value="kg">per Kilogram</option>
                  </Form.Select>
                    <Form.Control.Feedback type="invalid">
                        Please select a unit.
                    </Form.Control.Feedback>
                </InputGroup>
            </Form.Group>
          </Row>

          <Row >
            <Form.Group controlId="validationFormik03">
              <InputGroup>
              <InputGroup.Text>Quantity Remaining</InputGroup.Text>
              <Form.Control
                type="text"
                name="availableQuantity"
                value={values.availableQuantity}
                onChange={handleChange}
                placeholder="0.00"
                isInvalid={!!errors.availableQuantity}
              />
              <InputGroup.Text>{values.unit}</InputGroup.Text>
                <Form.Control.Feedback type="invalid">
                        Quantity must be formatted as 0.00.
                </Form.Control.Feedback>
                </InputGroup>
            </Form.Group>
            </Row>

            <Row className="my-3">
            <Col>
                <div><b>Organic: </b></div>
            </Col>
            <Col>
                {props.listing.organic ? "Yes" : "No"}
            </Col>
            </Row>

            <Row className="my-3">
              <Form.Group controlId="validationFormik04">
                <InputGroup>
                <Col>
                <InputGroup.Text>Active: </InputGroup.Text>
                </Col>
                <Col style={{marginLeft: "10px"}}>
                <Form.Check 
                  type="checkbox"
                  name="active"
                  value={values.active}
                  defaultChecked = {values.active}
                  onChange={handleChange}
                  label={values.active ? "Currently Active" : "Currently Inactive"}
                />
                </Col>
                </InputGroup>
              </Form.Group>
            </Row>

            <Row>
                <Button type="submit">Submit</Button>
                <Button variant="outline-danger" id={props.listing.product_id} onClick={props.handleDelete}>Delete</Button>
            </Row>
        </Form>
      )}
    </Formik>
  );
}

export default ProduceEditAttributes;

/*
<Row className="my-3">
            <Col>
                <div><b>Active: </b></div>
            </Col>
            <Col>
                {props.listing.active.toString()}
            </Col>
            </Row>
            */