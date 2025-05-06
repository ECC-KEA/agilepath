import {useContext} from 'react';
import SprintContext from './SprintContext';

export default function useSprint() {
  const context = useContext(SprintContext);
  if (!context) {
    throw new Error('useSprint must be used within a SprintProvider');
  }
  return context;
}