import { PropsWithChildren, useCallback, useEffect, useMemo, useState } from "react";
import { IRetrospective, INewRetrospective } from "../../types/retrospective.types";
import { useApi } from "../utils/useApi";
import RetrospectiveContext from "./RetrospectiveContext";
import { useLoading } from "../utils/loading/useLoading";

interface RetrospectiveProviderProps extends PropsWithChildren {
  sprintId: string;
}

function RetrospectiveProvider({ children, sprintId }: Readonly<RetrospectiveProviderProps>) {
  const loader = useLoading();
  const { get, post } = useApi();
  const [retrospective, setRetrospective] = useState<IRetrospective | undefined>();

  const loadRetrospective = useCallback(async () => {
    loader.add();
    return get(`/sprints/${sprintId}/retrospective`)
      .then((res => {
        if(res.status === 404) {
          setRetrospective(undefined);
        } else {
          setRetrospective(res);
        }
      }))
      .finally(loader.done);
  }, [get, sprintId]);
  
  const createRetrospective = useCallback(
    (retrospective: INewRetrospective) => {
      return post(`/retrospectives`, retrospective).then((res) => {setRetrospective(res)});
    },
    [post, sprintId, loader]
  );


  useEffect(() => {
    loadRetrospective();
  }, [loadRetrospective]);

  const contextValue = useMemo(() => ({
    retrospective,
    sprintId,
    createRetrospective
  }), [retrospective, sprintId, createRetrospective]);

  return (
    <RetrospectiveContext.Provider value={contextValue}>
      {children}
    </RetrospectiveContext.Provider>
  );
}

export default RetrospectiveProvider;