import React from "react";
import { Icon, Input, AutoComplete } from "antd";
import { PROFILE_PIC_URL_PREFIX } from "../constant";
import nba from "nba";

class SearchBar extends React.Component {
  state = {
    dataSource: []
  };
  handleSearch = value => {
    // console.log(value);

    this.setState({
      dataSource: !value
        ? []
        : nba.searchPlayers(value).map(player => ({
            fullName: player.fullName,
            playerId: player.playerId
          }))
    });
  };

  onSelect = playerName => {
    this.props.handleSelectPlayer(playerName);
  };
  render() {
    const { dataSource } = this.state;
    const { Option } = AutoComplete;
    const options = dataSource.map(player => (
      <Option
        key={player.fullName}
        value={player.fullName}
        className="player-option"
      >
        <img
          className="player-option-image"
          src={`${PROFILE_PIC_URL_PREFIX}/${player.playerId}.png`}
        />
        <span className="player-option-label">{player.fullName}</span>
      </Option>
    ));

    return (
      <AutoComplete
        className="search-bar"
        size="large"
        placeholder="Search NBA Player"
        onSearch={this.handleSearch}
        onSelect={this.onSelect}
        optionLabelProp="value"
        //dataSource is an attribute of AutoComplete. not this.state.dataSource
        dataSource={options}
      >
        <Input
          suffix={
            // <Button
            //   className="search-btn"
            //   style={{ marginRight: -12 }}
            //   size="large"
            //   type="default"
            // >
            <Icon type="search" className="certain-category-icon" />
            // </Button>
          }
        />
      </AutoComplete>
    );
  }
}
export default SearchBar;
