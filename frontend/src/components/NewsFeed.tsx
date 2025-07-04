import{ useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { Link } from "react-router-dom";
import Container from "react-bootstrap/esm/Container";

import MessageCompose from "./MessageCompose";
import MessageItem from "./MessageItem";
import { getMessages } from "../feature/messageSlice";
import { useAppDispatch, useAppSelector } from '../app/hooks'

import styles from "./styles/NewsFeed.module.css";

interface Message {
    id: string;
    userEmail: string;
    content: string;
    comments: Comment[];
    likes: string[];
    createdAt: unknown;
    quote: string;
}

interface Comment {
    id: string;
    userEmail: string;
    content: string;
    createdAt: string;
}


function NewsFeedContent() {
    const dispatch = useAppDispatch();
    const storeMessages: Message[] = useAppSelector((state: any) => state.messageReducer.messages);

    useEffect(() => {
        dispatch(getMessages());
    }, [dispatch]);

    return (
        <Container fluid className={styles.container}>
        <div>
            <h1>NewsFeed</h1> 
            <Link to="/">Go to Homepage</Link>
            <MessageCompose />
            {storeMessages.length > 0 ? (
                storeMessages.map((message: Message) => {
                    return (
                        <MessageItem
                            key={message.id}
                            id={message.id}
                            userEmail={message.userEmail}
                            content={message.content}
                            likes={message.likes}
                            comments={message.comments}
                            createdAt={message.createdAt}
                            quote={message.quote}
                        />
                    );
                })
            ) : (
                <div>
                    <Spinner animation="border" variant="success" />
                </div>
            )}
            </div>
        </Container>
    );
}

export default NewsFeedContent;