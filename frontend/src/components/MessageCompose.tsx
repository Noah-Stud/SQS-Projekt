import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";

import "react-toastify/dist/ReactToastify.css";
import { useAppDispatch } from '../app/hooks'
import { getMessages } from "../feature/messageSlice";

function MessageCompose() {
    const dispatch = useAppDispatch();

    const [userEmail] = useState<string>(localStorage.getItem("psnUserEmail") ?? "You are not logged in" );
    const [messageContent, setMessageContent] = useState<string>("");
    const [messageContentCount, setMessageContentCount] = useState<number>(0);
    const [disableMessageButton, setDisableMessageButton] = useState<boolean>(true);

    function showSuccessMessage(inputMessage: string) {
        toast.success(inputMessage, {
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

    function showFailMessage(inputMessage: string) {
        toast.error(inputMessage, {
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

    function handleContentChange(e: React.ChangeEvent<HTMLTextAreaElement>) {
        setMessageContent(e.target.value);
        setMessageContentCount(e.target.value.length);
        if (localStorage.getItem("psnUserEmail") == null || messageContentCount === 0 || messageContentCount > 200) {
            setDisableMessageButton(true);
        } else {
            setDisableMessageButton(false);
        }
    }

    async function createMessage(inputContent: string) {
        try {
            const response = await axios({
                method: "post",
                url: "/api/message/v1/insert",
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("psnToken"),
                },
                data: inputContent,
            });

            if (response.data !== null && response.data.status === "success") {
                showSuccessMessage("Message created successfully!");
                setMessageContent("");
                setMessageContentCount(0);
                setDisableMessageButton(true);
                window.location.reload();
            }

            if (response.data !== null && response.data.status === "fail") {
                showFailMessage("Post failed. Please try again later!");
            }
        } catch (error) {
            showFailMessage("Post failed. Please try again later!");
        }
    }

    async function handleCreateMessage(e: React.FormEvent<HTMLButtonElement>) {
        e.preventDefault();
        void createMessage(messageContent);
        dispatch(getMessages());
    }

    return (
        <div>
            <div>
                <ToastContainer />
                <Form>
                    <Form.Group>
                        <Form.Label>
                            <div>
                                <div className="fs-4 fw-bold">{userEmail}</div>
                            </div>
                        </Form.Label>
                        <Form.Control
                            id="inputMessage"
                            as="textarea"
                            rows={4}
                            placeholder="..."
                            value={messageContent}
                            onChange={handleContentChange}
                            style={{ resize: "none", height: "7rem" }}
                        />
                    </Form.Group>
                    <div>
                        <span>Characters: {messageContentCount}/200</span>
                        <Button
                            id="buttonPost"
                            onClick={handleCreateMessage}
                            variant="success"
                            disabled={disableMessageButton}
                            className="col-2 mx-3"
                        >
                            Post
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
    );
}

export default MessageCompose;