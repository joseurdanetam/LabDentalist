export interface IUser {
  dni?: string;
  id?: number;
  login?: string;
  firstName?: string | null;
  lastName?: string | null;
  telefono?: number | null;
  categoria?: string | null;
  email?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: string[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export class User implements IUser {
  constructor(
    public dni?: string,
    public id?: number,
    public login?: string,
    public firstName?: string | null,
    public lastName?: string | null,
    public telefono?: number | null,
    public categoria?: string | null,
    public email?: string,
    public activated?: boolean,
    public langKey?: string,
    public authorities?: string[],
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {}
}
