import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "./App.css";
import { Corridor } from "./components/Corridor";

const queryClient = new QueryClient();

function App() {
	return (
		<QueryClientProvider client={queryClient}>
			<div className="space-y-20">
				<h1>Kafka HQ Dashboard</h1>
				<Corridor />
			</div>
		</QueryClientProvider>
	);
}

export default App;
