import React, { useState } from "react";
import { Formik } from "formik";
import * as yup from "yup";
import axios from "axios";

import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";

import styles from "./styles/Register.module.css";
import Container from "react-bootstrap/esm/Container";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";

interface SignUpData {
    email: string;
    password: string;
}

function SignUp() {
    const [, setResData] = useState<unknown>(null);

    let navigate = useNavigate();

    const schema = yup.object().shape({
        email: yup.string().email().required(),
        password: yup.string().required(),
    });

    async function postSignUpInfo(inputData: SignUpData) {
        const response = await axios({
            method: "post",
            url: "/api/auth/v1/register",
            data: {
                email: inputData.email,
                password: inputData.password,
            },
        });

        if (response.data !== null) {
            setResData(response.data);
        }

        if (response.data !== null && response.data.status === "fail") {
            showWarningToast(response.data.message);
        }

        if (response.data !== null && response.data.status === "success") {
            navigate("/login");
        }
    }

    function showWarningToast(inputMessage: string) {
        toast.warn(inputMessage, {
            position: "bottom-center",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "colored",
        });
    }


    return (
        <Container fluid className={styles.container}>
            <ToastContainer />
            <Formik
                validationSchema={schema}
                initialValues={{
                    email: "",
                    password: ""
                }}
                onSubmit={(values, { setSubmitting }) => {
                    void postSignUpInfo(values);
                    setSubmitting(false);
                }}
            >
                {({
                    handleSubmit,
                    handleChange,
                    values,
                    touched,
                    errors,
                }) => (
                    <Form
                        noValidate
                        onSubmit={handleSubmit}
                        className={styles.formContainer}
                    >
                        <Row className="mb-5 text-center">
                            <h1 className="text-success">Sign Up</h1>
                        </Row>
                        <Row className="mb-3">
                            <Form.Group as={Col} md="12" controlId="signInEmail">
                                <Form.Label>Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    name="email"
                                    value={values.email}
                                    onChange={handleChange}
                                    isInvalid={touched.email && !!errors.email}
                                />
                                <Form.Control.Feedback type="invalid">
                                    Please enter a valid email
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Row>
                        <Row className="mb-3">
                            <Form.Group as={Col} md="12" controlId="signInPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    name="password"
                                    value={values.password}
                                    onChange={handleChange}
                                    isInvalid={touched.password && !!errors.password}
                                />

                                <Form.Control.Feedback type="invalid">
                                    Please enter your password
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Row>
                        <Button type="submit" variant="success">
                            Sign Up
                        </Button>
                    </Form>
                )}
            </Formik>
        </Container>
    );
}

export default SignUp;