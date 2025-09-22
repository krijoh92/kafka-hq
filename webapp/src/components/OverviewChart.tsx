"use client";

import { format } from "date-fns";
import { useMemo } from "react";
import { CartesianGrid, Line, LineChart, XAxis, YAxis } from "recharts";
import {
	Card,
	CardContent,
	CardDescription, CardHeader,
	CardTitle
} from "@/components/ui/card";
import {
	type ChartConfig,
	ChartContainer,
	ChartTooltip,
	ChartTooltipContent,
} from "@/components/ui/chart";
import type { TimeseriesData } from "@/lib/api";

export const description = "A linear line chart";

const chartConfig = {
	NO1: {
		label: "NO1",
		color: "var(--chart-1)",
	},
	NO2: {
		label: "NO2",
		color: "var(--chart-2)",
	},
	NO3: {
		label: "NO3",
		color: "var(--chart-3)",
	},
	NO4: {
		label: "NO4",
		color: "var(--chart-4)",
	},
	NO5: {
		label: "NO5",
		color: "var(--chart-5)",
	},
} satisfies ChartConfig;

type ChartData = Array<{
	timestamp: string;
	displayTime: string;
	NO1: number;
	NO2: number;
	NO3: number;
	NO4: number;
	NO5: number;
}>;

interface OverviewChartProps {
	data: TimeseriesData[];
}

export function OverviewChart({ data }: OverviewChartProps) {
	const { chartData, timeRange } = useMemo(() => {
		// Collect all unique timestamps across all areas
		const allTimestamps = new Set<string>();

		for (const area of data) {
			for (const item of area.series) {
				allTimestamps.add(item.timestamp);
			}
		}

		// Sort timestamps to ensure proper chronological order
		const sortedTimestamps = Array.from(allTimestamps).sort();

		// Calculate time range from actual data
		const firstTimestamp = sortedTimestamps[0];
		const lastTimestamp = sortedTimestamps[sortedTimestamps.length - 1];

		// Initialize chart data with all timestamps
		const chartdata: ChartData = sortedTimestamps.map((timestamp) => {
			const date = new Date(timestamp);
			const displayTime = format(date, "HH:mm");

			return {
				timestamp,
				displayTime,
				NO1: 0,
				NO2: 0,
				NO3: 0,
				NO4: 0,
				NO5: 0,
			};
		});

		// Fill in the actual values for each area
		for (const area of data) {
			for (const item of area.series) {
				const timestampIndex = chartdata.findIndex(
					(chartItem) => chartItem.timestamp === item.timestamp,
				);

				if (timestampIndex !== -1) {
					chartdata[timestampIndex] = {
						...chartdata[timestampIndex],
						[area.area]: item.value,
					};
				}
			}
		}

		return {
			chartData: chartdata,
			timeRange: {
				start: firstTimestamp,
				end: lastTimestamp
			}
		};
	}, [data]);

	// console.log(chartData)

	return (
		<Card>
			<CardHeader>
				<CardTitle>ACE OL per bidding zone</CardTitle>
				<CardDescription>
					{timeRange.start && timeRange.end ? (
						`${format(new Date(timeRange.start), "yyyy-MM-dd HH:mm")} - ${format(new Date(timeRange.end), "yyyy-MM-dd HH:mm")}`
					) : (
						"No data available"
					)}
				</CardDescription>
			</CardHeader>
			<CardContent>
				<ChartContainer config={chartConfig}>
					<LineChart
						accessibilityLayer
						data={chartData}
						margin={{
							left: 12,
							right: 12,
						}}
					>
						<CartesianGrid vertical={false} />
						<XAxis
							dataKey="displayTime"
							tickLine={false}
							axisLine={false}
							tickMargin={8}
							tickFormatter={(value) => value}
						/>
						<ChartTooltip
							cursor={false}
							content={<ChartTooltipContent className="bg-white" hideLabel />}
						/>
						<Line
							dataKey="NO1"
							type="linear"
							stroke="var(--color-NO1)"
							strokeWidth={2}
							dot={false}
						/>
						<Line
							dataKey="NO2"
							type="linear"
							stroke="var(--color-NO2)"
							strokeWidth={2}
							dot={false}
						/>
						<Line
							dataKey="NO3"
							type="linear"
							stroke="var(--color-NO3)"
							strokeWidth={2}
							dot={false}
						/>
						<Line
							dataKey="NO4"
							type="linear"
							stroke="var(--color-NO4)"
							strokeWidth={2}
							dot={false}
						/>
						<Line
							dataKey="NO5"
							type="linear"
							stroke="var(--color-NO5)"
							strokeWidth={2}
							dot={false}
						/>
						<YAxis />
					</LineChart>
				</ChartContainer>
			</CardContent>
		</Card>
	);
}
