import React from 'react';
import './styles/HomePage.css';

import { Link } from "react-router-dom";


const App: React.FC = () => {
    return (
        <div className="App">
            <header className="App-header">
                <Link to="/login">Go to LogInPage</Link>
                <Link to="/register">Go to RegisterPage</Link>
                <Link to="/newsfeed">Go to NewsFeedPage</Link>
            </header>
            <div className="Disclaimer">
                Inspirational quotes provided by <a href="https://zenquotes.io/" target="_blank">ZenQuotes API</a>
            </div>
        </div>
    );
}

export default App;