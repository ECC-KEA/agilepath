import {useState} from "react";
import {FaChevronDown, FaChevronRight, FaLightbulb, FaTimes} from "react-icons/fa";
import ShowIf from "../ShowIf";
import Button from "../buttons/Button";
import {ceremoniesTips} from "./ceremonyTips";

interface AgileCeremoniesGuideProps {
    className?: string;
}

function AgileCeremoniesGuide({className = ""}: Readonly<AgileCeremoniesGuideProps>) {
    const [isVisible, setIsVisible] = useState(true);
    const [expandedCeremony, setExpandedCeremony] = useState<string | null>(null);
    const [currentTipIndex, setCurrentTipIndex] = useState(0);

    if (!isVisible) {
        return (
            <div className={`${className} fixed bottom-4 right-4 z-50`}>
                <Button
                    text={
                        <span className="flex items-center gap-2">
              <FaLightbulb className="text-ap-lavender-800"/>
              <span className="text-sm">Agile Tips</span>
            </span>
                    }
                    className="bg-ap-lavender-100 text-ap-lavender-800 border border-ap-lavender-300 px-4 py-2 shadow-lg"
                    onClick={() => setIsVisible(true)}
                />
            </div>
        );
    }

    const currentTip = ceremoniesTips[currentTipIndex];

    const nextTip = () => {
        setCurrentTipIndex((prev) => (prev + 1) % ceremoniesTips.length);
        setExpandedCeremony(null);
    };

    const previousTip = () => {
        setCurrentTipIndex((prev) => (prev - 1 + ceremoniesTips.length) % ceremoniesTips.length);
        setExpandedCeremony(null);
    };

    const toggleExpanded = () => {
        setExpandedCeremony(expandedCeremony === currentTip.id ? null : currentTip.id);
    };

    return (
        <div className={`${className} fixed bottom-4 right-4 z-50 max-w-md`}>
            <div className="bg-white rounded-lg shadow-xl border border-ap-onyx-100 overflow-hidden">
                {/* Header */}
                <div className="bg-ap-lavender-100 p-3 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                        <FaLightbulb className="text-ap-lavender-800 text-lg"/>
                        <span className="font-semibold text-ap-lavender-800 text-sm">Agile Ceremonies Guide</span>
                    </div>
                    <Button
                        text={<FaTimes className="text-ap-onyx-400"/>}
                        className="p-1 hover:bg-ap-onyx-100 rounded"
                        onClick={() => setIsVisible(false)}
                    />
                </div>

                {/* Content */}
                <div className="p-4">
                    <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-2">
                            <currentTip.icon className={`text-xl ${currentTip.iconColor}`}/>
                            <h3 className="font-semibold text-ap-onyx-800">{currentTip.title}</h3>
                        </div>
                        <div className="text-xs text-ap-onyx-400">
                            {currentTipIndex + 1} of {ceremoniesTips.length}
                        </div>
                    </div>

                    <p className="text-sm text-ap-onyx-600 mb-3 leading-relaxed">
                        {currentTip.description}
                    </p>

                    <div className="bg-white p-3 rounded shadow border border-ap-onyx-50 mb-3">
                        <div className="text-xs font-medium text-ap-onyx-800 mb-1">💡 Why it matters:</div>
                        <div className="text-xs text-ap-onyx-400 mt-1">{currentTip.importance}</div>
                    </div>

                    <div className="bg-white p-3 rounded shadow border border-ap-onyx-50 mb-3">
                        <div className="text-xs font-medium text-ap-onyx mb-1">⏰ When to use:</div>
                        <div className="text-xs text-ap-onyx-400 mt-1">{currentTip.whenToUse}</div>
                    </div>

                    {/* Expandable Best Practices */}
                    <Button
                        text={
                            <div className="flex items-center justify-between w-full">
                                <span className="text-xs font-medium text-ap-onyx-700">Best Practices</span>
                                {expandedCeremony === currentTip.id ? (
                                    <FaChevronDown className="text-ap-onyx-400 text-xs"/>
                                ) : (
                                    <FaChevronRight className="text-ap-onyx-400 text-xs"/>
                                )}
                            </div>
                        }
                        className="w-full bg-ap-onyx-50 p-2 rounded border border-ap-onyx-100 mb-3"
                        onClick={toggleExpanded}
                    />

                    <ShowIf if={expandedCeremony === currentTip.id}>
                        <div className="mb-3 bg-ap-lavender-50 p-3 rounded-md">
                            <ul className="space-y-1">
                                {currentTip.bestPractices.map((practice, index) => (
                                    <li key={index} className="text-xs text-ap-lavender-800 flex items-start gap-2">
                                        <span className="text-ap-lavender-600 mt-0.5">•</span>
                                        <span>{practice}</span>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </ShowIf>

                    {/* Navigation */}
                    <div className="flex items-center justify-between">
                        <Button
                            text="← Previous"
                            className="text-xs bg-ap-onyx-100 text-ap-onyx-700 px-3 py-1 border border-ap-onyx-200"
                            onClick={previousTip}
                        />
                        <div className="flex gap-1">
                            {ceremoniesTips.map((_, index) => (
                                <div
                                    key={index}
                                    className={`w-2 h-2 rounded-full ${
                                        index === currentTipIndex ? "bg-ap-lavender-600" : "bg-ap-onyx-200"
                                    }`}
                                />
                            ))}
                        </div>
                        <Button
                            text="Next →"
                            className="text-xs bg-ap-lavender-600 text-white px-3 py-1"
                            onClick={nextTip}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AgileCeremoniesGuide;