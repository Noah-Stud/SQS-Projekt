import { configureStore } from "@reduxjs/toolkit";
import messageReducer from "../feature/messageSlice";

export const store = configureStore({
    reducer: {
        messageReducer: messageReducer,
    },
});

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
export type AppStore = typeof store