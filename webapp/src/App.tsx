import "./App.css";
import { OverviewChart } from "./components/OverviewChart";

function App() {
	return (
		<div className="space-y-20">
			<h1>Kafka HQ Dashboard</h1>
      <OverviewChart />
		</div>
	);
}

export default App;
