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


export function RetrospectiveWrapper({children}: Readonly<PropsWithChildren>) {
  const { sprintId } = useParams();

  if (!sprintId) {
    return null; // TODO return 404 page
  }

  return (
    <RetrospectiveProvider sprintId={sprintId}>
      {children}
    </RetrospectiveProvider>
  );
}


function Retrospective() {
  const { retrospective, createRetrospective } = useRetrospective();
  const { sprintId } = useParams();
  
  const [teamMood, setTeamMood] = useState<string>('');
  const [talkingPoints, setTalkingPoints] = useState<ITalkingPoint[]>([{ prompt: '', response: '' }]);
  const [keepDoing, setKeepDoing] = useState<string[]>(['']);
  const [stopDoing, setStopDoing] = useState<string[]>(['']);
  const [startDoing, setStartDoing] = useState<string[]>(['']);

  useEffect(() => {
    if (retrospective) {
      setTeamMood(retrospective.teamMood || '');
      setTalkingPoints(retrospective.talkingPoints);
      setKeepDoing(retrospective.keepDoing);
      setStopDoing(retrospective.stopDoing);
      setStartDoing(retrospective.startDoing);
    }
  }, [retrospective]);

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
    <div className="max-w-3xl p-6 space-y-8 bg-white rounded-lg shadow w-1/2">
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
        <div className="block font-medium">
          Team Mood
        </div>
        <CustomTextArea
          id="teamMood"
          className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none"
          value={teamMood}
          disabled={!!retrospective}
          onChange={(e) => setTeamMood(e.target.value)}
        />
      </div>

      {/* Talking Points */}
      <div className="space-y-4">
        <div className="text-xl font-semibold">Talking Points</div>
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
          <Button
            text={
              <span className="flex items-center gap-2">
                <FaPlus className="text-ap-lavender-800" />
              </span>
            }
            className="bg-white px-5 border border-ap-onyx-50 w-fit"
            onClick={addTalkingPoint}
          />
        </ShowIf>
      </div>

      {/* Keep Doing */}
      <div className="space-y-2">
        <div className="text-xl font-semibold">Keep Doing</div>
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

      {/* Stop Doing */}
      <div className="space-y-2">
        <div className="text-xl font-semibold">Stop Doing</div>
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

      {/* Start Doing */}
      <div className="space-y-2">
        <div className="text-xl font-semibold">Start Doing</div>
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
  );
}

export default Retrospective;