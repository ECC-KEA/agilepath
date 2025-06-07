import {useContext} from "react";
import AnalyticsContext from "./AnalyticsContext";

export default function useAnalytics() {
    const context = useContext(AnalyticsContext);
    if (!context) {
        throw new Error("useAnalytics must be used within AnalyticsProvider");
    }
    return context;
}