import React from "react";
import nba from "nba";
import * as d3 from "d3";
import { hexbin } from "d3-hexbin";
import { court, shots } from "d3-shotchart";
import PropTypes from "prop-types";

window.d3_hexbin = { hexbin: hexbin }; // workaround library problem

export class ShotChart extends React.Component {
  //type check
  static propTypes = {
    playerId: PropTypes.number.isRequired,
    minCount: PropTypes.number.isRequired,
    chartType: PropTypes.string.isRequired,
    displayTooltip: PropTypes.bool.isRequired
  };

  //   componentDidUpdate() {
  componentDidUpdate() {
    const { playerId, minCount, chartType, displayTooltip } = this.props;
    nba.stats
      .shots({
        PlayerID: playerId
      })
      .then(response => {
        //create an object from response body
        const final_shots = response.shot_Chart_Detail.map(shot => ({
          x: (shot.locX + 250) / 10,
          y: (shot.locY + 50) / 10,
          action_type: shot.actionType,
          shot_distance: shot.shotDistance,
          shot_made_flag: shot.shotMadeFlag
        }));
        //check d3 shotChart
        const courtSelection = d3.select("#shot-chart");
        //every update, erase the old html
        courtSelection.html("");
        //get court image background
        const chart_court = court().width(500);
        const chart_shots = shots()
          .shotRenderThreshold(minCount)
          .displayToolTips(displayTooltip)
          .displayType(chartType);
        courtSelection.call(chart_court);
        courtSelection.datum(final_shots).call(chart_shots);
      });
  }
  render() {
    return <div id="shot-chart"></div>;
  }
}
