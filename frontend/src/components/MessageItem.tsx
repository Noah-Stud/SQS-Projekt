import React, { useState } from "react";

import { Button, Form, Row } from "react-bootstrap";
import {
    RiHeartFill,
    RiHeartLine,
    RiMessage2Fill,
    RiSendPlane2Fill,
} from "react-icons/ri";

import { useDispatch } from "react-redux";
import {
    addComment,
    addLike
} from "../feature/messageSlice";
import styles from "./styles/MessageItem.module.css";


interface MessageItemProp {
    id: string;
    userEmail: string;
    content: string;
    comments: CommentItem[];
    likes: string[];
    createdAt: any;
    quote: string;
}

interface CommentItem {
    id: string;
    userEmail: string;
    content: string;
    createdAt: string;
}


const MessageItem: React.FC<MessageItemProp> = (props) => {
    const dispatch = useDispatch();

    const [likeStatus, setLikeStatus] = useState<boolean>(false);
    const [commentStatus, setCommentStatus] = useState<boolean>(false);
    const [commentContent, setCommentContent] = useState<string>("");
    const [sendButtonDisable, setSendButtonDisable] = useState<boolean>(true);
    const [currentUserEmail] = useState<string | null>(
        localStorage.getItem("psnUserEmail")
    );
    const [messageId] = useState<string>(props.id);

    function handleLikeClick() {
        if (currentUserEmail && !props.likes.includes(currentUserEmail)) {
            setLikeStatus(true);
            dispatch(addLike({ messageId: messageId, userEmail: currentUserEmail }));
        } else if (currentUserEmail && props.likes.includes(currentUserEmail)) {
            setLikeStatus(false);
            dispatch(addLike({ messageId: messageId, userEmail: currentUserEmail }));
        }
    }

    function handleCommentButtonClick() {
        setCommentStatus(!commentStatus);
    }

    function handleCommentContentChange(e: React.ChangeEvent<HTMLTextAreaElement>) {
        e.preventDefault();

        setCommentContent(e.target.value);

        if (currentUserEmail != null && commentContent.length - 1 > 0 && commentContent.length - 1 <= 100) {
            setSendButtonDisable(false);
        } else {
            setSendButtonDisable(true);
        }
    }

    function sendComment() {
        if (currentUserEmail) {
            dispatch(
                addComment({
                    messageId: messageId,
                    comment: commentContent,
                })
            );
            setCommentContent("");
        }
    }

    return (
        <div className={`${styles.message}`}>
            <Row>
                <div className={"d-flex align-items-center mb-3"}>
                    <div className={`${styles.email}`}>{props.userEmail}</div>
                </div>
                <div className={`${styles.content}`}>
                    <p>{props.content}</p>
                    <p className={`${styles.quote }`}>{props.quote}</p>
                </div>
                <div>
                    <p>{props.createdAt}</p>
                </div>

                {/* Sub-functions of a message */}
                <div className="d-flex justify-content-center align-items-center">
                    {/* Sub-function like button */}
                    <div className="mx-3">
                        <span
                            className={`${styles.likeButton} mx-1 fs-4`}
                            onClick={handleLikeClick}
                        >
                            {likeStatus ? (
                                <RiHeartFill className="text-danger" />
                            ) : (
                                <RiHeartLine className="text-danger" />
                            )}
                        </span>
                        <span>
                            {props.likes.length > 0 ? props.likes.length : null}
                        </span>
                    </div>

                    {/* Sub-function comment button */}
                    <div className="mx-3">
                        <span
                            className={`${styles.commentButton} mx-1 fs-4`}
                            onClick={handleCommentButtonClick}
                        >
                            <RiMessage2Fill className="text-primary" />
                        </span>
                        {/*<span>*/}
                        {/*    {props.comments.length > 0 ? props.comments.length : null}*/}
                        {/*</span>*/}
                    </div>
                </div>

                {/* List of comments and comment input box */}
                {commentStatus ? (
                    <div className="mt-3">
                        <div className="d-flex align-items-center">
                            <Form className="w-100 mx-1">
                                <Form.Group>
                                    <Form.Control
                                        type="text"
                                        placeholder="Write a comment..."
                                        value={commentContent}
                                        onChange={handleCommentContentChange}
                                    />
                                </Form.Group>
                            </Form>
                            <span className="mx-1">{commentContent.length}/100</span>
                            <div className="ms-auto">
                                <Button
                                    variant="success"
                                    className="p-1"
                                    disabled={sendButtonDisable}
                                    onClick={sendComment}
                                >
                                    <RiSendPlane2Fill className="fs-4" />
                                </Button>
                            </div>
                        </div>
                        {props.comments.map((commentItem: { id: string; userEmail: string, content: string; createdAt: string }) => (
                            <div className={`${styles.content}`}>
                                <div className={`${styles.email}`}>{props.userEmail}</div>
                                <div>{commentItem.content}</div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <span></span>
                )}
            </Row>
        </div>
    );
};

export default MessageItem;