# Build stage (Node 22)
FROM node:22.14.0-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
CMD ["npm", "run", "dev", "--", "--host"]
