export const API_URL = import.meta.env.VITE_API_BASE_URL;


export type TimeseriesData = {
    area: string;
    series: Array<{
        timestamp: string
        value: number
    }>
}

export const areas = ["NO1", "NO2", "NO3", "NO4", "NO5"]