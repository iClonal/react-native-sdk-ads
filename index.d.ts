import React from 'react';
import {
    ViewProps,
    EventSubscription,
} from 'react-native';

export enum AdsBannerSizeType {
    BANNER          = "banner",
    LARGE_BANNER    = "largeBanner",
    MEDIUM_RECTANGLE= "mediumRectangle",
    FULL_BANNER     = "fullBanner",
    LEADER_BOARD    = "leaderBoard",
    SMART_BANNER    = "smartBanner",
    SMART_PORTRAIT  = "smartBannerPortrait",
    SMART_LANDSCAPE = "smartBannerLandscape"
}

export interface AdsBannerProps extends ViewProps {
    adSize?: AdsBannerSizeType;
    testDevices?: string[];
    onSizeChange?: (layout: {width: number, height: number}) => void;
    onAdLoaded?: () => void;
    onAdFailedToLoad?: () => void;
    onAdOpened?: () => void;
    onAdClosed?: () => void;
    onAdLeftApplication?: () => void;
}

export class AdsBanner extends React.Component<AdsBannerProps> {

}

type AdsInterstitialEvent = "adLoaded" | "adFailedToLoad" | "adOpened" | "adClosed" | "adLeftApplication"
type AdsInterstitialEventHandler = () => void

declare module AdsInterstitial {
    //export function setAdUnitID(id: string) : void;
    export function setTestDevices(list: string[]): void;
    export function requestAd(): void;
    export function showAd(): Promise<void>;
    export function isReady(): Promise<boolean>;
    export function addEventListener(evt: AdsInterstitialEvent, handler: AdsInterstitialEventHandler): EventSubscription;
    export function removeEventListener(type: any, handler: AdsInterstitialEventHandler): void;
    export function removeAllListeners(): void;
}


type AdsRewardedEvent = "adLoaded" | "adFailedToLoad" | "adOpened" | "adClosed" | "adLeftApplication" | "rewarded" | "videoStarted"
type AdsRewardedEventHandler = () => void

declare module AdsRewarded {
    //export function setAdUnitID(id: string): void;
    export function setTestDevices(list: string[]): void;
    export function requestAd(): void;
    export function showAd(): Promise<void>;
    export function isReady(): Promise<boolean>;
    export function addEventListener(evt: AdsRewardedEvent, handler: AdsRewardedEventHandler): EventSubscription;
    export function removeEventListener(type: any, handler: AdsRewardedEventHandler): void;
    export function removeAllListeners(): void;
}