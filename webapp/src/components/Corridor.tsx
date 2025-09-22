import { useQuery } from "@tanstack/react-query";
import { API_URL, areas, type TimeseriesData } from "../lib/api";
import { OverviewChart } from "./OverviewChart";

const start = new Date(2025, 0, 1);
const end = new Date(2025, 11, 31);

export const Corridor = () => {
	const { data, isLoading, dataUpdatedAt } = useQuery({
		queryKey: [
			"timeseries",
			"start",
			start.toISOString(),
			"end",
			end.toISOString(),
		],
		queryFn: async () => {
			const data = await Promise.all(
				areas.map((area) =>
					fetch(
						`${API_URL}/timeseries?area=${area}&start=${start.toISOString()}&end=${end.toISOString()}`,
					),
				),
			);
			const res = await Promise.all(
				data.map((res) => res.json() as Promise<TimeseriesData>),
			);

			return res;
		},
		refetchInterval: 1000,
	});

	return (
		<div>
			<div>
				<h1>Corridor</h1>
				<p>Refreshed at {new Date(dataUpdatedAt).toLocaleString()}</p>
			</div>
			{isLoading ? (
				<p>Loading...</p>
			) : data ? (
				<OverviewChart data={data} />
			) : (
				<p>No data</p>
			)}
		</div>
	);
};
