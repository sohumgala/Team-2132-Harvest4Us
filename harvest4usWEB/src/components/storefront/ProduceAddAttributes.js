import { Formik } from 'formik';
import * as yup from 'yup';
import { Form, Row, InputGroup, Button } from 'react-bootstrap';
import React from 'react';

const schema = yup.object().shape({
  usdaGrade: yup.string().matches(/[A-Z]/).required(),
  price: yup.string().matches(/^\d{0,2}\.\d{2}?$/).required(),
  unit: yup.string().matches(/[A-Za-z]/).required(),
  organic: yup.string().matches(/[A-Za-z]/).required(),
  availableQuantity: yup.string().matches(/^\d{0,2}\.\d{2}?$/).required()
});

//A form for adding produce attributes
function ProduceAddAttributes(props) {
  return (
    <Formik
      validationSchema={schema}
      validateOnChange={false}
      initialValues={{
        key: '11115',
        usdaGrade: '',
        price: '',
        unit: '',
        availableQuantity: '',
        organic: '',
        dateEdited: '',
        active: false,
        disableEdits: true
      }}
      onSubmit={(values) => {props.handleMySubmit(values)}}
    >
      {({
        handleSubmit,
        handleChange,
        handleBlur,
        values,
        touched,
        isValid,
        errors,
      }) => (
        <Form noValidate onSubmit={handleSubmit} className="m-3">
          <Row className="my-3">
          <Form.Group controlId="validationFormik00">
            <InputGroup>    
              <InputGroup.Text>USDA Organic:</InputGroup.Text>
              <Form.Select
                type="text"
                name="organic"
                value={values.organic}
                onChange={handleChange}
                isInvalid={!!errors.organic}
              >
                  <option value="0">Select Organic</option>
                <option value="true">Yes</option>
                <option value="false">No</option>
                  </Form.Select>
                    <Form.Control.Feedback type="invalid">
                        Please indicate if produce is organic.
                    </Form.Control.Feedback>
              </InputGroup>
            </Form.Group>
        </Row>
        <Row className="my-3">
            <Form.Group controlId="validationFormik01">
            <InputGroup>    
              <InputGroup.Text>USDA Grade:</InputGroup.Text>
              <Form.Select
                type="text"
                name="usdaGrade"
                value={values.usdaGrade}
                onChange={handleChange}
                isInvalid={!!errors.usdaGrade}
              >
                  <option value="0">Select Grade</option>
                <option value="A">A</option>
                <option value="B">B</option>
                <option value="C">C</option>
                  </Form.Select>
                    <Form.Control.Feedback type="invalid">
                        Please Select a USDA Grade.
                    </Form.Control.Feedback>
              </InputGroup>
            </Form.Group>
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
              <InputGroup.Text>Quantity</InputGroup.Text>
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

            <Row>
                <Button className="m-4" style={{width: "93%"}} type="submit">Submit</Button>
            </Row>
        </Form>
      )}
    </Formik>
  );
}

export default ProduceAddAttributes;
