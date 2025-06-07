interface MetricCardProps {
    title: string;
    value: string;
    subtitle: string;
    color: string;
    tooltip?: string;
}

function MetricCard({title, value, subtitle, color, tooltip}: Readonly<MetricCardProps>) {
    return (
        <div
            className="bg-white p-4 rounded shadow border border-ap-onyx-50 hover:shadow-md transition-shadow"
            data-tooltip-id="metric-tooltip"
            data-tooltip-content={tooltip}
        >
            <div className="flex items-center justify-between">
                <div>
                    <h3 className="text-sm font-medium text-ap-onyx-600">{title}</h3>
                    <div className={`text-2xl font-bold text-${color}-800 mt-1`}>{value}</div>
                    <p className="text-xs text-ap-onyx-400 mt-1">{subtitle}</p>
                </div>
            </div>
        </div>
    );
}

export default MetricCard;