import {ColorKey, getColorClasses} from "../../helpers/analyticsHelpers.ts";

interface ComplexityBarProps {
    label: string;
    count: number;
    total: number;
    color: ColorKey;
}

function ComplexityBar({label, count, total, color}: Readonly<ComplexityBarProps>) {
    const percentage = total > 0 ? (count / total) * 100 : 0;
    const colorClasses = getColorClasses(color);

    return (
        <div>
            <div className="flex justify-between text-sm mb-1">
                <span className="text-ap-onyx-600">{label}</span>
                <span className="text-ap-onyx-400">{count} tasks</span>
            </div>
            <div className="w-full bg-ap-onyx-100 rounded-full h-2">
                <div
                    className={`${colorClasses.bgDark} h-2 rounded-full transition-all duration-300`}
                    style={{width: `${percentage}%`}}
                />
            </div>
        </div>
    );
}

export default ComplexityBar;