import { Formik } from 'formik';
import * as yup from 'yup';
import { Form, Row, Col, InputGroup, Button } from 'react-bootstrap';
import ProduceAddTypes from "./storefront/ProduceAddTypes.js"

const schema = yup.object().shape({
  usdaGrade: yup.string().matches(/[A-Z]/).required(),
  price: yup.string().matches(/^\d{0,2}\.\d{2}?$/).required(),
  unit: yup.string().matches(/[A-Za-z]/).required(),
  organic: yup.string().matches(/[A-Za-z]/).required(),
  quantity: yup.string().matches(/^\d{0,2}\.\d{2}?$/).required(),
  zip: yup.string().required(),
  terms: yup.bool().required().oneOf([true], 'Terms must be accepted'),
});


function FormTest() {
  return (
    <Formik
      validationSchema={schema}
      validateOnChange={false}
      initialValues={{
        usdaGrade: '',
        price: '',
        unit: '',
        availableQuantity: '',
        organic: '',
        dateEdited: '',
        active: false,
        category: '',
        type: ''
      }}
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
        <Form noValidate onSubmit={handleSubmit}>
          <Row className="mb-3">
          <Form.Group as={Col} md="4" controlId="validationFormik00">
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
        <Row>

            <Form.Group as={Col} md="4" controlId="validationFormik01">
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

        <Row>
            <Form.Group as={Col} md="4" controlId="validationFormik02">
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
                <option value="Pounds">per Pound</option>
                <option value="Kilograms">per Kilogram</option>
                  </Form.Select>
                    <Form.Control.Feedback type="invalid">
                        Please select a unit.
                    </Form.Control.Feedback>
                </InputGroup>
            </Form.Group>
            
          </Row>
          <Row className="mb-3">
            <Form.Group as={Col} md="4" controlId="validationFormik03">
              <InputGroup>
              <InputGroup.Text>Quantity</InputGroup.Text>
              <Form.Control
                type="text"
                name="quantity"
                value={values.quantity}
                onChange={handleChange}
                placeholder="0.00"
                isInvalid={!!errors.quantity}
              />
              <InputGroup.Text>{values.unit}</InputGroup.Text>
                <Form.Control.Feedback type="invalid">
                        Quantity must be formatted as 0.00.
                </Form.Control.Feedback>
                </InputGroup>
            </Form.Group>
            </Row>
            <Row>
            <Form.Check 
                type="switch"
                id="custom-switch"
                label="Active Listing"
            />
            
          <Button type="submit">Submit form</Button>
          </Row>
        </Form>
      )}
    </Formik>
  );
}

export default FormTest;
