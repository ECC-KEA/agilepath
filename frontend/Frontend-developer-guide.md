# AgilePath – Frontend Developer Guide

Dette dokument forklarer projektets frontend-arkitektur, konventioner og hvordan man arbejder med React, hooks, context og API-integration.

---

## 🧱 Arkitekturprincipper

Frontend-projektet bygger på principper om komponentbaseret udvikling og separation of concerns. Vi bruger React med TypeScript for at sikre type-sikkerhed og genanvendelighed.

### Komponentstruktur

- **Generiske komponenter:** Komponenter, der kan bruges på tværs af projektet, placeres i `components/generic/`.
- **Feature-specifikke komponenter:** Komponenter, der er knyttet til en bestemt feature, placeres i `components/<feature>/`.
- **Views:** Hver side eller route har sin egen mappe i `views/`.

### State Management

Vi bruger React Context til at dele state på tværs af komponenter, kombineret med hooks for at holde koden ren og genanvendelig.

---

## 📦 Projektstruktur (overview)

```markdown
src/
├── components/
│ ├── generic/ - Genanvendelige komponenter
│ └── <feature>/ - Feature-specifikke komponenter
├── hooks/ - Custom hooks m. context/state-management
│ └── <feature>/ - Feature-specifikke hooks
│ └── utils/ - Utility hooks
├── GlobalContextProviderWrapper.tsx - Wrapper til global context provider
├── helpers/ - diverse hjælpefunktioner
├── types/ - TypeScript-typer og interfaces
├── views/ - Sider og routes
├── App.tsx - Hovedapplikationen
└── main.tsx - Entry point
```

---

## 🧩 Hooks og Context

### Oprettelse af Context

Context bruges til at dele state og funktionalitet på tværs af komponenter. Eksempel:

```tsx
// filepath: src/hooks/example/ExampleContext.tsx
import React, { createContext, useContext, useState, PropsWithChildren } from "react";

interface IExampleContext {
  example: string | null;
  setToken: (example: string | null) => void;
}

const ExampleContext = createContext<IExampleContext | undefined>(undefined);

// filepath: src/hooks/example/ExampleProvider.tsx
export function ExampleProvider({ children }: Readonly<PropsWithChildren>) {
  const [example, setExample] = useState<string | null>(null);

  return (
    <ExampleContext.Provider value={{ example, setExample }}>{children}</ExampleContext.Provider>
  );
}

// filepath: src/hooks/example/useExample.tsx
export const useExample = (): IExampleContext => {
  const context = useContext(ExampleContext);
  if (!context) {
    throw new Error("useExample must be used within an ExampleProvider");
  }
  return context;
};
```

### Brug af Context i komponenter

Hvis contexten skal bruges i hele appen kan du tilføje den til GlobalContextProviderWrapper i `src/hooks/GlobalContextProviderWrapper.tsx`.

Hent context i en komponent med `useAuth`:

```tsx
// filepath: src/components/example/ExampleComponent.tsx
import React from "react";
import { useExample } from "../hooks/example/useExample";

function ExampleComponent() {
  const { example, setExample } = useExample();

  return (
    <div>
      <p>Current Example: {example}</p>
      <button onClick={() => setExample("new-example")}>Set Example</button>
    </div>
  );
}

export default ExampleComponent;
```

---

## 🔑 Hent JWT til API-kald

For at hente en JWT til brug i API-kald, bruger vi Clerk's `useAuth` hook.

### Eksempel på at hente en JWT

```tsx
// filepath: src/hooks/utils/useApi.ts
import { useAuth } from "@clerk/clerk-react";

export const useApi = () => {
  const { getToken } = useAuth();

  const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
    const token = await getToken();
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`
    };

    return fetch(url, { ...options, headers });
  };

  return { fetchWithAuth };
};
```

### Brug af `useApi` i en context provider

Vi vil helst undgå at fetche fra en komponent, så det er bedst at fetche i et custom hook og så bruge det i komponenten.

Eksempel på at bruge `fetchWithAuth` i et custom hook:

```tsx
// filepath: src/hooks/useFetchExample.ts
import { useEffect, useState, PropsWithChildren } from "react";
import { useApi } from "./utils/useApi";

export function ExampleProvider({ children }: Readonly<PropsWithChildren>) {
  const [example, setExample] = useState<string | null>(null);

  const { fetchWithAuth } = useApi();

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetchWithAuth("/api/example");
      const result = await response.json();
      setExample(result.example);
    };

    fetchData();
  }, [fetchWithAuth]);

  return (
    <ExampleContext.Provider value={{ example, setExample }}>{children}</ExampleContext.Provider>
  );
}
```

### Brug af `useMe` til at hente brugeroplysninger

For at hente brugeroplysninger på den bruger der er logget ind, kan du bruge `useMe`.

```tsx
const { me } = useMe();

// me er en IUser
export interface IUser {
  id: string;
  email: string;
  fullName?: string;
  avatarUrl?: string;
  githubUsername?: string;
  githubProfileUrl?: string;
  createdAt: string;
}
```

---

## ✅ Konventioner

- **Hooks:** Navngiv custom hooks med `use`-prefix, fx `useApi`, `useAuth`.
- **Context:** Placer context i `hooks/<domæne-navn>/` og eksporter både provider og hook.
- **API-kald:** Brug `fetchWithAuth` fra `useApi` for at sikre korrekt JWT-håndtering.
- **TypeScript:** Brug interfaces til props og context-typer. Navngiv dem med `I`-prefix, fx `IExampleContext`, hvis det er props navngiv dem med `Props`-suffix, fx `ExampleComponentProps`.
- **Styling:** Brug tailwindcss til styling. Brug vores custom tailwind farver defineret i index.css.
- **Komponenter:** Hold komponenter små og fokuserede. En komponent bør kun have én ansvarlighed. Hvis en komponent bliver for stor, overvej at bryde den op i mindre komponenter.
- **Props:** Brug `function` til at definere komponenter. Definér props som interfaces og brug dem i komponenten.

---

## 🛠 TODO / Tips

- Hold hooks fokuserede og genanvendelige.
- Brug `useMemo` til at optimere performance for tunge komponenter.
