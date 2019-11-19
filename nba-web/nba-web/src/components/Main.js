import React, { Component } from "react";
import { Profile } from "./Profile";
import { DataViewContainer } from "./DataViewContainer";
import SearchBar from "./SearchBar";
import nba from "nba";
import { DEFAULT_PLAYER_INFO } from "../constant";

export class Main extends Component {
  state = {
    // playerId: nba.findPlayer("Stephen Curry").playerId,
    playerInfo: DEFAULT_PLAYER_INFO
  };

  componentDidMount() {
    this.loadPlayerInfo(this.state.playerInfo.fullName);
  }

  handleSelectPlayer = playerName => {
    this.loadPlayerInfo(playerName);
    console.log(playerName);
  };

  loadPlayerInfo = playerName => {
    nba.stats
      .playerInfo({ PlayerID: nba.findPlayer(playerName).playerId })
      .then(info => {
        const playerInfo = Object.assign(
          info.commonPlayerInfo[0],
          info.playerHeadlineStats[0]
        );
        this.setState({ playerInfo });
      });
  };
  // loadPlayerInfo = playerName => {
  //   nba.stats
  //     .playerInfo({ PlayerID: this.state.playerId })
  //     .then(info => {
  //       console.log(info);
  //       //combine player Info and stats
  //       const playerInfo = Object.assign(
  //         info.commonPlayerInfo[0],
  //         info.playerHeadlineStats[0]
  //       );
  //       console.log("playerInfo", playerInfo);
  //       this.setState({ playerInfo });
  //     })
  //     .catch(e => console.log(e));
  // };

  render() {
    const { playerInfo } = this.state;
    return (
      <div className="main">
        <SearchBar handleSelectPlayer={this.handleSelectPlayer} />
        <div className="player">
          <Profile playerInfo={playerInfo} />
          <DataViewContainer playerId={playerInfo.playerId} />
        </div>
      </div>
    );
  }
}
