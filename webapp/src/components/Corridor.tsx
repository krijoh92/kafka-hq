import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { API_URL } from "../lib/api";
import { OverviewChart } from "./OverviewChart";

const start = new Date(2025, 0, 1);
const end = new Date(2025, 11, 31);

export const Corridor = () => {
	const [area, setArea] = useState("NO1");

	const { data, isLoading } = useQuery({
		queryKey: ["corridor"],
		queryFn: async () => {
			const res = await fetch(`${API_URL}/timeseries?area=${area}&start=${start.toISOString()}&end=${end.toISOString()}`);

			return res.json();
		},
	});

    console.log(data, isLoading)

	return (
		<div>
			<h1>Corridor</h1>
			<OverviewChart />
		</div>
	);
};
