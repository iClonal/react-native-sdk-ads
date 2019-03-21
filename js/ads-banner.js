'use strict';

import React from 'react';
import {
    requireNativeComponent,
    UIManager,
    findNodeHandle,
} from 'react-native';

import {createErrorFromErrorData} from './utils';

export class AdsBanner extends React.PureComponent {
    constructor(props) {
        super(props);
        this.loadBanner = this._loadBanner.bind(this);
        this.onSizeChange = this._onSizeChange.bind(this);
        this.adFailedToLoad = this._adFailedToLoad.bind(this);
        this.state = {
            style: {},
        };
        this._bannerViewRef = null;
    }

    componentDidMount() {
        this._loadBanner();
    }

    render() {
        return (
            <RNSdkAdsBannerView
                {...this.props}
                style={[this.props.style, this.state.style]}
                onSizeChange={this.onSizeChange}
                onAdFailedToLoad={this.adFailedToLoad}
                ref={ref => this._bannerViewRef = ref}
            />
        );
    }

    _loadBanner() {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this._bannerViewRef),
            UIManager.RNSdkAdsBannerView.Commands.loadBanner,
            null,
        );
    }

    _onSizeChange(evt) {
        const {
            nativeEvent: {
                width,
                height,
            }
        } = evt;
        const {onSizeChange} = this.props;
        this.setState((prevState)=>{
            return { 
                style: {width, height} 
            };
        });
        !!onSizeChange && onSizeChange({width, height});
    }

    _adFailedToLoad(evt) {
        const {onAdFailedToLoad} = this.props;
        const {
            nativeEvent: {
                error,
            }
        } = evt;
        !!onAdFailedToLoad && onAdFailedToLoad(createErrorFromErrorData(error));
    }
};

const RNSdkAdsBannerView = requireNativeComponent("RNSdkAdsBannerView", AdsBanner);

export const AdsBannerSizeType = {
    BANNER: "banner",
    LARGE_BANNER: "largeBanner",
    MEDIUM_RECTANGLE: "mediumRectangle",
    FULL_BANNER: "fullBanner",
    LEADER_BOARD: "leaderBoard",
    SMART_BANNER: "smartBanner",
    SMART_PORTRAIT: "smartBannerPortrait",
    SMART_LANDSCAPE: "smartBannerLandscape",
};