import React from "react";
import "../styles/App.css";
import { TopNavBar } from "./TopNavBar";
// import { ShotChart } from "./ShotChart";
import { Main } from "./Main";
function App() {
  return (
    <div className="App">
      <TopNavBar />
      <Main />
      {/* <ShotChart playerId={201939} /> */}
    </div>
  );
}

export default App;
