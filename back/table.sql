--database commune_db

CREATE SEQUENCE commune_seq;
CREATE TABLE Commune (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    nom VARCHAR(50)
);
CREATE SEQUENCE personne_seq;
CREATE TABLE Personne (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    idUnique VARCHAR(50) NOT NULL,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    dateNaissance DATE,
    lieuNaissance VARCHAR(50),
    heureNaissance TIME,
    pere VARCHAR(70),
    mere VARCHAR(70),
    idCommune VARCHAR(50),
    FOREIGN KEY(idCommune) REFERENCES Commune(id)
);
CREATE SEQUENCE droit_seq;
CREATE TABLE Droit (
    id VARCHAR(21) PRIMARY KEY NOT NULL,
    nom VARCHAR(50)
);
insert into droit values('DRT'||nextval('droit_seq'),'normal');
insert into droit values('DRT'||nextval('droit_seq'),'SU');
CREATE SEQUENCE usercommune_seq;
CREATE TABLE UserCommune (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    email VARCHAR(80),
    mdp VARCHAR(255),
    idCommune VARCHAR(50),
    idDroit VARCHAR(50),
    FOREIGN KEY(idCommune) REFERENCES Commune(id),
    FOREIGN KEY(idDroit) REFERENCES Droit(id)
);

CREATE SEQUENCE UserToken_seq;
CREATE TABLE UserToken (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    idUserCommune VARCHAR(50),
    creation TIMESTAMP,
    token VARCHAR(255),
    expiration TIMESTAMP,
    etat integer,-- 1: cree,11:annuler
    FOREIGN KEY(idUserCommune) REFERENCES UserCommune(id)
);
CREATE SEQUENCE demandecopie_seq;
CREATE TABLE DemandeCopie (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    idPersonne VARCHAR(50),
    dateDemande DATE,
    nbCopie INTEGER,
    etat INTEGER,   --1 valide, 10 vita sonia,20 annuler
    idCommune VARCHAR(50),
    urlDown VARCHAR(50),
    --karapanondronle olona ndemande
    FOREIGN KEY(idCommune) REFERENCES Commune(id),
    FOREIGN KEY(idPersonne) REFERENCES Personne(id)
);
CREATE SEQUENCE delivrancecopie_seq;
CREATE TABLE DelivranceCopie (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    idDemandeCopie VARCHAR(50),
    dateDelivrance TIMESTAMP,
    etat INTEGER,  --1 valide, 10 annuler
    FOREIGN KEY(idDemandeCopie) REFERENCES DemandeCopie(id)
);
create view ListeDemandeClient as
select DemandeCopie.id,
        Personne.idUnique,
        DemandeCopie.dateDemande,
        DemandeCopie.nbCopie,
        DemandeCopie.etat,
        Commune.nom nomCommune,
        DemandeCopie.urlDown
from DemandeCopie join Personne on DemandeCopie.idPersonne=Personne.id join Commune on Commune.id=DemandeCopie.idCommune;
