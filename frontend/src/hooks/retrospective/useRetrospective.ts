import { useContext } from 'react';
import RetrospectiveContext from './RetrospectiveContext';

export default function useRetrospective() {
  const context = useContext(RetrospectiveContext);
  if (!context) {
    throw new Error('useRetrospective must be used within a RetrospectiveProvider');
  }
  return context;
}