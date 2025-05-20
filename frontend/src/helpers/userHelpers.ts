import { IUser } from "../types/user.types";

export const name = (user: IUser) => {
  return user.fullName ?? user.githubUsername ?? user.email;
};
