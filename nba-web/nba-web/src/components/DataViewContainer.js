import React, { Component } from "react";
import { ShotChart } from "./ShotChart";
import { CountSlider } from "./CountSlider";
import { Radio, Row, Col } from "antd";
import { Switch, Icon } from "antd";
import _ from "lodash";

export class DataViewContainer extends Component {
  state = {
    minCount: 2,
    chartType: "hexbin",
    displayTooltip: true
  };
  //use debounce to stop too many request, after 500mm, then
  //do the update of minCount
  handleMinCountChange = _.debounce(minCount => {
    this.setState({ minCount });
  }, 500);

  handleChartTypeChange = e => {
    this.setState({ chartType: e.target.value });
  };
  handleDisplayTooltipChange = displayTooltip => {
    this.setState({ displayTooltip });
  };
  render() {
    const { playerId } = this.props;
    const { minCount, chartType, displayTooltip } = this.state;
    return (
      <div className="data-view">
        <ShotChart
          playerId={playerId}
          minCount={minCount}
          chartType={chartType}
          displayTooltip={displayTooltip}
        />
        {/* <ShotChart playerId={this.state.playerId} /> */}
        <div className="filters">
          {chartType === "hexbin" ? (
            <CountSlider
              handleMinCountChange={this.handleMinCountChange}
              defaultValue={2}
            />
          ) : null}
          <Row>
            <Col span={9}>
              <Radio.Group
                onChange={this.handleChartTypeChange}
                value={chartType}
              >
                <Radio value="hexbin">Hexbin</Radio>
                <Radio value="scatter">Scatter</Radio>
              </Radio.Group>
            </Col>
            <Col>
              <Switch
                checkedChildren="On"
                unCheckedChildren="Off"
                onChange={this.handleDisplayTooltipChange}
                defaultChecked
              />
            </Col>
          </Row>
        </div>
      </div>
    );
  }
}
