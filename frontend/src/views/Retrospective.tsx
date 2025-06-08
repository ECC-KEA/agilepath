import useRetrospective from '../hooks/retrospective/useRetrospective';
import { useState, useEffect } from 'react';
import { PropsWithChildren } from "react";
import { useParams } from 'react-router';
import RetrospectiveProvider from '../hooks/retrospective/RetrospectiveProvider';
import { INewRetrospective, ITalkingPoint } from '../types/retrospective.types';
import Button from '../components/generic/buttons/Button'
import Input from '../components/generic/inputs/Input';
import CustomTextArea from '../components/generic/inputs/CustomTextArea';
import {FaPlus} from 'react-icons/fa';
import { FaMinus } from 'react-icons/fa6';
import ShowIf from '../components/generic/ShowIf';
import { notifyError } from "../helpers/notify";
import { PiOpenAiLogoDuotone } from 'react-icons/pi';
import Markdown from "react-markdown";
import AnalyticsProvider from '../hooks/analytics/AnalyticsProvider';
import useAnalytics from '../hooks/analytics/useAnalytics';
import { ISprintAnalysis } from '../types/analytics.types';
import AssistantProvider from '../hooks/assistant/AssistantProvider';
import useAssistant from '../hooks/assistant/useAssistant';
import useOpenAI from '../hooks/openai/useOpenAI';
import OpenAIProvider from '../hooks/openai/OpenAIProvider';
import Tooltip from '../components/generic/tooltips/Tooltip';


export function RetrospectiveWrapper({children}: Readonly<PropsWithChildren>) {
  const { sprintId } = useParams();

  if (!sprintId) {
    return null; // TODO return 404 page
  }

  return (
    <RetrospectiveProvider sprintId={sprintId}>
      <AssistantProvider>
        <OpenAIProvider>
          <AnalyticsProvider>
            {children}
          </AnalyticsProvider>
        </OpenAIProvider>
      </AssistantProvider>
    </RetrospectiveProvider>
  );
}


function Retrospective() {
  const { retrospective, createRetrospective } = useRetrospective();
  const { getSprintAnalysis } = useAnalytics();
  const { loadAssistant } = useAssistant();
  const { sendMessage } = useOpenAI();
  const [sprintAnalysis, setSprintAnalysis] = useState<ISprintAnalysis>();
  const { sprintId } = useParams();

  useEffect(() => {
    if (sprintId) {
      getSprintAnalysis(sprintId)
        .then(setSprintAnalysis)
        .catch((error) => {
          console.error('Failed to fetch sprint analysis:', error);
          notifyError('Failed to fetch sprint analysis.');
        });
    } 
  }, [sprintId]);
  
  const [teamMood, setTeamMood] = useState<string>('');
  const [talkingPoints, setTalkingPoints] = useState<ITalkingPoint[]>([{ prompt: '', response: '' }]);
  const [keepDoing, setKeepDoing] = useState<string[]>(['']);
  const [stopDoing, setStopDoing] = useState<string[]>(['']);
  const [startDoing, setStartDoing] = useState<string[]>(['']);
  const [openAIResponse, setOpenAIResponse] = useState<string | undefined>(undefined);

  useEffect(() => {
    if (retrospective) {
      setTeamMood(retrospective.teamMood || '');
      setTalkingPoints(retrospective.talkingPoints);
      setKeepDoing(retrospective.keepDoing);
      setStopDoing(retrospective.stopDoing);
      setStartDoing(retrospective.startDoing);
    }
  }, [retrospective]);

  const handleTalkingPointSuggestions = () => {
    setOpenAIResponse(undefined);
    loadAssistant("talking_point_helper")
      .then((assistant) => {
        console.log("Loaded AI assistant:", assistant);
        const systemMessage = {
          role: "system",
          content: assistant?.prompt ?? "You are a helpful assistant."
        };

        const userMessage = {
          role: "user",
          content: JSON.stringify({
            sprintAnalysis: sprintAnalysis
          })
        };

        const body = {
          model: assistant?.model ?? "gpt-4o-mini",
          messages: [systemMessage, userMessage],
          stream: true
        }

        const handleChunk = (chunk: string) => {
          setOpenAIResponse((prev) => (prev ? prev + chunk : chunk));
        };

        return sendMessage(body, handleChunk)
          .then((response) => {
            console.log("Final response from OpenAI:", response);
          });
      })
      .catch(() => {
      notifyError("Error loading AI assistant or sending message");
    });
  }

  const addTalkingPoint = () => setTalkingPoints([...talkingPoints, { prompt: '', response: '' }]);
  const removeTalkingPoint = (index: number) => setTalkingPoints(talkingPoints.filter((_, i) => i !== index));
  const updateTalkingPoint = (index: number, field: keyof ITalkingPoint, value: string) => {
    const updated = [...talkingPoints];
    updated[index] = { ...updated[index], [field]: value };
    setTalkingPoints(updated);
  };

  const addKeepDoing = () => setKeepDoing([...keepDoing, '']);
  const removeKeepDoing = (i: number) => setKeepDoing(keepDoing.filter((_, index) => index !== i));
  const updateKeepDoing = (i: number, value: string) => {
    const updated = [...keepDoing];
    updated[i] = value;
    setKeepDoing(updated);
  };

  const addStopDoing = () => setStopDoing([...stopDoing, '']);
  const removeStopDoing = (i: number) => setStopDoing(stopDoing.filter((_, index) => index !== i));
  const updateStopDoing = (i: number, value: string) => {
    const updated = [...stopDoing];
    updated[i] = value;
    setStopDoing(updated);
  };

  const addStartDoing = () => setStartDoing([...startDoing, '']);
  const removeStartDoing = (i: number) => setStartDoing(startDoing.filter((_, index) => index !== i));
  const updateStartDoing = (i: number, value: string) => {
    const updated = [...startDoing];
    updated[i] = value;
    setStartDoing(updated);
  };

  const validateInputs = () => {
    
    if (talkingPoints.some(point => point.prompt.trim() === '')) {
      notifyError('Talking points cannot be empty.');
      return false;
    }
    const uniqueTalkingPoints = new Set(talkingPoints.map(point => point.prompt.trim()));
    if (uniqueTalkingPoints.size !== talkingPoints.length) {
      notifyError('Talking points must be unique.');
      return false;
    }

    if (keepDoing.some(item => item.trim() === '')) {
      notifyError('Keep Doing items cannot be empty.');
      return false;
    }
    const uniqueKeepDoing = new Set(keepDoing.map(item => item.trim()));
    if (uniqueKeepDoing.size !== keepDoing.length) {
      notifyError('Keep Doing items must be unique.');
      return false;
    }

    if (stopDoing.some(item => item.trim() === '')) {
      notifyError('Stop Doing items cannot be empty.');
      return false;
    }
    const uniqueStopDoing = new Set(stopDoing.map(item => item.trim()));
    if (uniqueStopDoing.size !== stopDoing.length) {
      notifyError('Stop Doing items must be unique.');
      return false;
    }


    if (startDoing.some(item => item.trim() === '')) {
      notifyError('Start Doing items cannot be empty.');
      return false;
    }
    const uniqueStartDoing = new Set(startDoing.map(item => item.trim()));
    if (uniqueStartDoing.size !== startDoing.length) {
      notifyError('Start Doing items must be unique.');
      return false;
    }

    return true;
  }

  const handleSubmit = () => {
    if (!sprintId) {
      notifyError('Sprint ID is missing.');
      return false;
    }

    if (!validateInputs()) {
      return;
    }
    
    const retrospectiveData: INewRetrospective = {
      sprintId: sprintId, 
      teamMood: teamMood,
      talkingPoints,
      keepDoing,
      stopDoing,
      startDoing,
    };
    
    createRetrospective(retrospectiveData);
  };

  return (
    <div className="flex h-[calc(100vh-140px)] overflow-y-auto w-full relative">
      <div className="max-w-3xl p-6 space-y-8 bg-white w-1/2 ">
        <div className="text-3xl font-bold">
          Retrospective
        </div>
        <ShowIf if={!!retrospective}> 
          <div>
            Completed at: {retrospective?.completedAt ? new Date(retrospective.completedAt).toLocaleString() : ''}
          </div>
        </ShowIf>

        {/* Team Mood */}
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <div className="text-xl font-semibold">
              Team Mood
            </div>
            <ShowIf if={!retrospective}>
              <Tooltip
                id="teamMoodTooltip"
                text="How is the team feeling overall? Share your general mood or any emotional highlights from the sprint. You could use fist of five and write the average score here, or any other mood indicator."
              />
            </ShowIf>
          </div>
          
          <CustomTextArea
            id="teamMood"
            placeholder="(Optional)"
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none"
            value={teamMood}
            disabled={!!retrospective}
            onChange={(e) => setTeamMood(e.target.value)}
          />
        </div>

        {/* Talking Points */}
        <div className="space-y-4">
          <div className="flex items-center gap-2">
            <div className="text-xl font-semibold">
              Talking Points
            </div>
            <ShowIf if={!retrospective}>
              <Tooltip
                id="talkingPointsTooltip"
                text="What topics should we discuss together? This can include blockers, feedback, or anything worth addressing as a group."
              />
            </ShowIf>
          </div>
          {talkingPoints.map((point, index) => (
            <div key={index} className="flex flex-row gap-2 items-end">
              <div className="flex flex-col w-full gap-2">
                <Input
                  placeholder="Title"
                  className="border border-gray-300 rounded px-3 py-2 w-full"
                  value={point.prompt}
                  disabled={!!retrospective}
                  onChange={(e) => updateTalkingPoint(index, 'prompt', e.target.value)}
                />
                <CustomTextArea
                  rows={3}
                  placeholder="Notes (Optional)"
                  className="border border-gray-300 rounded px-3 py-2 w-full"
                  value={point.response ?? ''}
                  disabled={!!retrospective}
                  onChange={(e) => updateTalkingPoint(index, 'response', e.target.value)}
                />
              </div>
              <ShowIf if={!retrospective}>
                <Button
                  text={
                    <span className="flex items-center gap-2">
                      <FaMinus className="text-ap-lavender-800" />
                    </span>
                  }
                  className="bg-white px-5 border border-ap-onyx-50 w-fit"
                  onClick={() => removeTalkingPoint(index)}
                />
              </ShowIf>
            </div>
          ))}
          <ShowIf if={!retrospective}>
            <div className="flex items-center gap-4">
              <Button
                text={
                  <span className="flex items-center gap-2">
                    <FaPlus className="text-ap-lavender-800" />
                  </span>
                }
                className="bg-white px-5 border border-ap-onyx-50 w-fit"
                onClick={addTalkingPoint}
              />
              <Button
                text={
                  <span className="flex items-center gap-2">
                    <PiOpenAiLogoDuotone className="flex-shrink-0 text-xl" />
                    Help
                  </span>
                }
                className="bg-gradient-to-br to-ap-lavender-900 from-ap-cyan-900 text-white px-10"
                title="Click to get AI help for potential talking points"
                onClick={() => {handleTalkingPointSuggestions()} /* TODO: Implement AI help functionality */}
              />
            </div>
          </ShowIf>
        </div>

        {/* Keep Doing */}
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <div className="text-xl font-semibold">
              Keep Doing
            </div>
            <ShowIf if={!retrospective}>
              <Tooltip
                id="keepDoingTooltip"
                text="What went well that we should continue doing? Highlight habits, practices, or approaches that are working."
              />
            </ShowIf>
          </div>
          {keepDoing.map((item, index) => (
            <div key={index} className="flex flex-row gap-2 items-end">
              <CustomTextArea
                rows={2}
                key={index}
                disabled={!!retrospective}
                className="w-full border border-gray-300 rounded px-3 py-2"
                value={item}
                onChange={(e) => updateKeepDoing(index, e.target.value)}
              />
              <ShowIf if={!retrospective}>
                <Button
                  text={
                    <span className="flex items-center gap-2">
                      <FaMinus className="text-ap-lavender-800" />
                    </span>
                  }
                  className="bg-white px-5 border border-ap-onyx-50 w-fit"
                  onClick={() => removeKeepDoing(index)}
                />
              </ShowIf>
            </div>
          ))}
          <ShowIf if={!retrospective}>
            <Button
              text={
                <span className="flex items-center gap-2">
                  <FaPlus className="text-ap-lavender-800" />
                  
                </span>
              }
              className="bg-white px-5 border border-ap-onyx-50 w-fit"
              onClick={addKeepDoing}
            />
          </ShowIf>
        </div>

        {/* Start Doing */}
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <div className="text-xl font-semibold">
              Start Doing
            </div>
            <ShowIf if={!retrospective}>
              <Tooltip
                id="startDoingTooltip"
                text="What new things should we try? Suggest improvements, tools, or processes that could help the team."
              />
            </ShowIf>
          </div>
          {startDoing.map((item, index) => (
            <div key={index} className="flex flex-row gap-2 items-end">
              <CustomTextArea
                rows={2}
                key={index}
                disabled={!!retrospective}
                className="w-full border border-gray-300 rounded px-3 py-2"
                value={item}
                onChange={(e) => updateStartDoing(index, e.target.value)}
              />
              <ShowIf if={!retrospective}>
                <Button
                  text={
                    <span className="flex items-center gap-2">
                      <FaMinus className="text-ap-lavender-800" />
                    </span>
                  }
                  className="bg-white px-5 border border-ap-onyx-50 w-fit"
                  onClick={() => removeStartDoing(index)}
                />
              </ShowIf>
            </div>
          ))}
          <ShowIf if={!retrospective}>
            <Button
              text={
                <span className="flex items-center gap-2">
                  <FaPlus className="text-ap-lavender-800" />
                </span>
              }
              className="bg-white px-5 border border-ap-onyx-50 w-fit"
              onClick={addStartDoing}
            />
          </ShowIf>
        </div>

        {/* Stop Doing */}
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <div className="text-xl font-semibold">
              Stop Doing
            </div>
            <ShowIf if={!retrospective}>
              <Tooltip
                id="stopDoingTooltip"
                text="What is not working or is slowing us down? Call out behaviors or practices we should reconsider or drop."
              />
            </ShowIf>
          </div>
          {stopDoing.map((item, index) => (
            <div key={index} className="flex flex-row gap-2 items-end">
              <CustomTextArea
                rows={2}
                disabled={!!retrospective}
                key={index}
                className="w-full border border-gray-300 rounded px-3 py-2"
                value={item}
                onChange={(e) => updateStopDoing(index, e.target.value)}
              />
              <ShowIf if={!retrospective}>
                <Button
                  text={
                    <span className="flex items-center gap-2">
                      <FaMinus className="text-ap-lavender-800" />
                    </span>
                  }
                  className="bg-white px-5 border border-ap-onyx-50 w-fit"
                  onClick={() => removeStopDoing(index)}
                />
              </ShowIf>
            </div>
          ))}
          <ShowIf if={!retrospective}>
            <Button
              text={
                <span className="flex items-center gap-2">
                  <FaPlus className="text-ap-lavender-800" />
                </span>
              }
              className="bg-white px-5 border border-ap-onyx-50 w-fit"
              onClick={addStopDoing}
            />
          </ShowIf>
        </div>

        <ShowIf if={!retrospective}>
          {/* Submit */}
          <div className="pt-4 text-center">
            <Button
              text="Submit"
              onClick={handleSubmit}
              className="bg-ap-lavender-800 text-white px-3 w-full justify-center shadow-xs sm:ml-3 sm:w-auto"
            />
          </div>
        </ShowIf>
      </div>
      <div className="w-1/2 flex-shrink-0">
        <ShowIf if={!!openAIResponse}>
          <div className="flex flex-col">
            <div className="sticky top-0 flex gap-4 text-xl items-center bg-gradient-to-br p-4 to-ap-lavender-900 from-ap-cyan-900 text-white">
              <PiOpenAiLogoDuotone className="flex-shrink-0" />
              Talking point suggestions
            </div>
            <div className="p-2 text-sm">
              <Markdown>{openAIResponse}</Markdown>
            </div>
          </div>
        </ShowIf>
      </div>
    </div>
  );
}

export default Retrospective;