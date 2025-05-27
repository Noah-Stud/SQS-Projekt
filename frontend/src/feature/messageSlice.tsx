import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

interface InitialState {
    messages: Message[];
}

interface Message {
    id: string;
    likes: string[];
    comments: string[];
}

interface AddLikePayload {
    messageId: string;
    userEmail: string;
}

interface AddCommentPayload {
    messageId: string;
    comment: string;
}

const initialState: InitialState = {
    messages: [],
};

export const getMessages = createAsyncThunk(
    "Messages/getAllMessages",
    async (thunkAPI) => {
        const response = await axios({
            method: "get",
            url: "/api/message/v1/getAll",
            headers: {
                Authorization: "Bearer " + localStorage.getItem("psnToken"),
            }
        });
        return response.data.payload;
    }
);

async function insertComment(messageId: string, commentContent: string) {
    await axios({
        method: "post",
        url: "/api/comment/v1/create",
        headers: {
            Authorization: "Bearer " + localStorage.getItem("psnToken"),
        },
        data: {
            messageId: messageId,
            commentContent: commentContent
        },
    });
}

async function updateLike(messageId: string) {
    const response = await axios({
        method: "post",
        url: "/api/message/v1/like",
        headers: {
            Authorization: "Bearer " + localStorage.getItem("psnToken"),
        },
        data: messageId,

    });

    return response.data;
}

export const messageSlice = createSlice({
    name: "messagesSlice",
    initialState,
    reducers: {
        addLike: (state, action: { payload: AddLikePayload }) => {
            if (state.messages !== null) {
                for (let i = 0; i < state.messages.length; i++) {
                    if (state.messages[i].id === action.payload.messageId) {
                        if (!state.messages[i].likes.includes(action.payload.userEmail)) {
                            state.messages[i].likes.push(action.payload.userEmail);
                            updateLike(action.payload.messageId);
                        } else {
                            state.messages[i].likes = state.messages[i].likes.filter(item => item !== action.payload.userEmail);
                            updateLike(action.payload.messageId);
                        }
                    }
                }
            }
        },

        addComment: (state, action: { payload: AddCommentPayload }) => {
            if (state.messages !== null) {
                for (let i = 0; i < state.messages.length; i++) {
                    if (state.messages[i].id === action.payload.messageId) {
                        state.messages[i].comments.push(action.payload.comment);
                        insertComment(action.payload.messageId, action.payload.comment);
                    }
                }
            }
        }
    },
    extraReducers: (builder) => {
        builder.addCase(getMessages.fulfilled, (state, action) => {
            state.messages = action.payload;
        });
    },
});

export const { addLike, addComment } = messageSlice.actions;
export default messageSlice.reducer;