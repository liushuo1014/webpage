import logo from "../assets/images/nba-logoman-word-white.svg";
import "../styles/topNavBar.css";
// import "../styles/App.css";
import React from "react";

export function TopNavBar() {
  return (
    <header className="App-header">
      <img src={logo} className="App-logo" alt="logo" />
    </header>
  );
}
