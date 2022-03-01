export interface IUser {
  id?: number;
  login?: string;
  dni?: string;
  firstName?: string | null;
  lastName?: string | null;
  telefono?: number | null;
}

export class User implements IUser {
  constructor(
    public id: number,
    public login: string,
    public dni: string,
    public firstName: string | null,
    public lastName: string | null,
    public telefono: number | null
  ) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
