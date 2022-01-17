import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import MainWindow from './components/MainWindow/MainWindow';
import PostClient from './components/utils/PostController.js';

ReactDOM.render(
  <React.StrictMode>
    <MainWindow postClient={new PostClient(process.env.REACT_APP_SERVER_HOST ?? 'localhost', 8080)}/>
  </React.StrictMode>,
  document.getElementById('root'),
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
