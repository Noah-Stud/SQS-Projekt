import React from "react";
import { HashRouter, Routes, Route } from "react-router-dom";

import HomePage from "./HomePage";
import LogInPage from "./LogIn";
import RegisterPage from "./Register";
import NewsFeedPage from "./NewsFeed";

const AppContainer: React.FC = () => {
  return (
    <HashRouter>
      <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LogInPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/newsfeed" element={<NewsFeedPage />} />
      </Routes>
    </HashRouter>
  );
}

export default AppContainer;