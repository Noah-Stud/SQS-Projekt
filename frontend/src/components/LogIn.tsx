import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Formik} from 'formik';
import * as yup from 'yup';
import axios from 'axios';
import { Container, Row, Col, Button, Form } from 'react-bootstrap';
import { ToastContainer, toast } from 'react-toastify';
import styles from "./styles/LogIn.module.css";


interface InputData {
    email: string;
    password: string;
}

const LogIn: React.FC = () => {
    const [, setResData] = useState<unknown>(null);
    const navigate = useNavigate();

    const schema = yup.object().shape({
        email: yup.string().email().required(),
        password: yup.string().required(),
    });

    const postSignInInfo = async (inputData: InputData) => {
        try {
            const response = await axios({
                method: "post",
                url: "/api/auth/v1/login",
                data: {
                    email: inputData.email,
                    password: inputData.password,
                },
            });

            if (response.data !== null && response.data.status === "fail") {
                showWarningToast("Invalid email or password");
                console.log("unseccesfull login");
            }

            if (response.data !== null && response.data.status === "success") {
                setResData(response.data);
                localStorage.setItem("psnToken", response.data.payload);
                localStorage.setItem("psnUserEmail", inputData.email);
                console.log("succesfull login");
                navigate("/newsfeed");
            }
        } catch (err) {
            var error = err as Error
            console.log(error);
            showWarningToast(error.message);
        }
    };

    const showWarningToast = (inputMessage: string) => {
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
        console.log("toast");
    };

    return (
        <Container fluid className={styles.container}>
            <ToastContainer />
            <Formik
                validationSchema={schema}
                initialValues={{
                    email: "",
                    password: "",
                }}
                onSubmit={(values, { setSubmitting }) => {
                    void postSignInInfo(values);
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
                        <Row>
                            <h1 id="inputLogin" className="text-success">Log In</h1>
                        </Row>
                        <Row className="mb-3">
                            <Form.Group as={Col} md="12">
                                <Form.Label>Email </Form.Label>
                                <Form.Control
                                    id="logInEmail"
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
                            <Form.Group as={Col} md="12">
                                <Form.Label>Password </Form.Label>
                                <Form.Control
                                    id="logInPassword"
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
                            Log In
                        </Button>
                        <Link to="/">Go to HomePage</Link>
                    </Form>
                )}
            </Formik>
        </Container>
    );
};

export default LogIn;