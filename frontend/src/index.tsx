import React from 'react';
import ReactDOM from 'react-dom/client';
import axios from 'axios';
import { Provider } from "react-redux";

import App from './App';
import { store } from "./app/store";

axios.defaults.baseURL = 'http://localhost:8080';

const root = ReactDOM.createRoot(
  document.getElementById('root')!
);

root.render(
    <Provider store={store}>
        <React.StrictMode>
            <App />
        </React.StrictMode>
    </Provider>
);