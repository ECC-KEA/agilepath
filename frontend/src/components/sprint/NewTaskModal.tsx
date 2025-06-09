import { useMemo, useState } from "react";
import useTask from "../../hooks/task/useTask";
import { IColumn } from "../../types/column.types";
import { ITaskRequest, TshirtEstimate, PointEstimate } from "../../types/story.types";
import Modal from "../generic/Modal";
import { notifyError, notifySuccess } from "../../helpers/notify";
import Input from "../generic/inputs/Input";
import CustomSelect from "../generic/select/CustomSelect";
import useStory from "../../hooks/story/useStory";
import TextArea from "../generic/inputs/CustomTextArea";
import Tooltip from "../generic/tooltips/Tooltip";
import ShowIf from "../generic/ShowIf";
import useCurrentProject from "../../hooks/projects/useCurrentProject";
import { EstimationMethod } from "../../types/project.types";
import { FaTshirt } from "react-icons/fa";

interface NewTaskModalProps {
  column: IColumn;
  show: boolean;
  onClose: () => void;
}
function NewTaskModal(props: Readonly<NewTaskModalProps>) {
  const { createTask } = useTask();
  const { stories } = useStory();
  const { project } = useCurrentProject();

  const [storyID, setStoryID] = useState<string>("");
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [tshirtEstimate, setTshirtEstimate] = useState<TshirtEstimate | undefined>(undefined);
  const [storyPointEstimate, setStoryPointEstimate] = useState<PointEstimate | undefined>(undefined);


  const storyOptions = useMemo(() => {
    return stories.map((s) => ({
      label: s.title,
      value: s.id
    }));
  }, [stories]);

  const disableAction = useMemo(() => {
    return storyID === "" || title === "";
  }, [storyID, title]);

  const handleCreateTask = () => {
    const tmp: ITaskRequest = {
      assigneeIds: [], // TODO: select m. members i projekt,
      sprintColumnId: props.column.id,
      storyId: storyID,
      title,
      description,
      estimateTshirt: tshirtEstimate,
      estimatePoints: storyPointEstimate
    };
    void createTask(tmp)
      .then(() => notifySuccess("Successfully created task"))
      .catch(() => notifyError("Failed to create task"));
    props.onClose();
  };

  const tshirtEstimateOptions = [
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />
          XS
        </span>
      ),
      value: TshirtEstimate.XSMALL
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />S
        </span>
      ),
      value: TshirtEstimate.SMALL
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />M
        </span>
      ),
      value: TshirtEstimate.MEDIUM
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />L
        </span>
      ),
      value: TshirtEstimate.LARGE
    },
    {
      label: (
        <span className="flex items-center gap-2">
          <FaTshirt className="text-ap-lavender-500" />
          XL
        </span>
      ),
      value: TshirtEstimate.XLARGE
    }
  ];

  const storyPointEstimateOptions = [
    { label: "1", value: PointEstimate.POINT_1 },
    { label: "2", value: PointEstimate.POINT_2 },
    { label: "3", value: PointEstimate.POINT_3 },
    { label: "5", value: PointEstimate.POINT_5 },
    { label: "8", value: PointEstimate.POINT_8 },
    { label: "13", value: PointEstimate.POINT_13 },
    { label: "21", value: PointEstimate.POINT_21 }
  ];

  return (
    <Modal
      title="New Task"
      show={props.show}
      onClose={props.onClose}
      actionText="Create"
      onAction={handleCreateTask}
      disableAction={disableAction}
    >
      <div className="flex flex-col gap-4 p-4">
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Story</div>
          <CustomSelect
            options={storyOptions}
            value={storyOptions.find((o) => o.value === storyID)}
            onChange={(o) => {
              if (o) {
                setStoryID(o.value);
              }
            }}
          />
        </div>
        <div className="flex flex-col">
          <div className="text-ap-onyx-200">Title</div>
          <Input
            type="text"
            placeholder="Task title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div>
          <div className="text-ap-onyx-400 flex justify-between">
            <div className="flex items-center gap-1">
              Description
              <Tooltip
                text="A clear description of the task helps team members<br /> understand the requirements and expectations."
                className="ml-1"
                id="taskDescriptionTooltip"
              />
            </div>
            <div className="italic">Optional</div>
          </div>
          <TextArea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Task description"
            className="w-full"
          />
        </div>
        <ShowIf if={project?.estimationMethod === EstimationMethod.TSHIRT_SIZES}>
          <label>
            <div className="text-ap-onyx-400 flex justify-between">
              <div className="flex items-center gap-1">
                <span className="text-xs">Estimate (Tshirt size)</span>
                <Tooltip
                  text="Use larger sizes for tasks that seem bigger or more complex.<br/>XS means small effort, while XL suggests much more work or uncertainty.<br />If you do not estimate tasks, burndown chart will not be accurate.<br />You can update estimation later. "
                  className="ml-1"
                  id="tshirtEstimateTooltip"
                />
              </div>
              <div className="italic">Optional</div>
            </div>
            <CustomSelect
              options={tshirtEstimateOptions}
              value={
                tshirtEstimateOptions.find((o) => o.value === tshirtEstimate)
              }
              onChange={(o) => {
                if (o) {
                  setTshirtEstimate(o.value as TshirtEstimate);
                }
              }}
              className="w-1/2 text-right"
            />
          </label>
        </ShowIf>
        <ShowIf if={project?.estimationMethod === EstimationMethod.STORY_POINTS}>
          <label>
            <div className="text-ap-onyx-400 flex justify-between">
              <div className="flex items-center gap-1">
                <span className="text-xs">Estimate (Story points)</span>
                <Tooltip
                  text="Story points reflect relative effort using the Fibonacci sequence. <br />Higher numbers mean more work, complexity, or uncertainty.<br />If you do not estimate tasks, burndown chart will not be accurate.<br />You can update estimation later. "
                  className="ml-1"
                  id="storyPointEstimateTooltip"
                />
              </div>
              <div className="italic">Optional</div>
            </div>
            <CustomSelect
              options={storyPointEstimateOptions}
              value={
                storyPointEstimateOptions.find((o) => o.value === storyPointEstimate)
              }
              onChange={(o) => {
                if (o) {
                  setStoryPointEstimate(o.value);
                }
              }}
              classNames={{
                menuList: (base) => `${base} text-right`
              }}
              className="w-1/2 text-right"
              isSearchable={false}
            />
          </label>
        </ShowIf>
      </div>
    </Modal>
  );
}
export default NewTaskModal;
