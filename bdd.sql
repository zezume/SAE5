DROP TABLE IF EXISTS
    Utilisateur,
    Question,
    Questionnaire,
    Reponse,
    Appareil,
    ActiviteNumerique,
    Conseil,
    AppareilUtilisateur,
    ActiviteNumeriqueUtilisateur,
    ConseilAppareil,
    ConseilActiviteNumerique,
    QuestionnaireQuestion,
    Utilisation

    CASCADE;

-- =======================================
-- Suppression des types ENUM
-- =======================================
DROP TYPE IF EXISTS type_question CASCADE;
DROP TYPE IF EXISTS type_conseil CASCADE;


-- =======================================
-- Création des types ENUM
-- =======================================
CREATE TYPE type_question AS ENUM ('checkbox', 'radio', 'text', 'number');
CREATE TYPE type_conseil AS ENUM ('energie', 'co2', 'bonnes_pratiques', 'autre');

-- =======================================
-- Table : Utilisateur
-- =======================================
CREATE TABLE Utilisateur (
                             id_utilisateur SERIAL PRIMARY KEY,
                             nom VARCHAR(100) NOT NULL,
                             email VARCHAR(150) UNIQUE NOT NULL
);

-- =======================================
-- Table : Question
-- =======================================
CREATE TABLE Question (
                          id_question SERIAL PRIMARY KEY,
                          intitule VARCHAR(255) NOT NULL,
                          description TEXT,
                          type type_question NOT NULL
);

-- =======================================
-- Table : Questionnaire
-- =======================================
CREATE TABLE Questionnaire (
                               id_questionnaire SERIAL PRIMARY KEY,
                               titre VARCHAR(255) NOT NULL,
                               description TEXT
);


-- =======================================
-- Table : Réponse
-- =======================================
CREATE TABLE Reponse (
                         id_reponse INT NOT NULL ,
                         id_question INT NOT NULL REFERENCES Question(id_question) ON DELETE CASCADE,

                         reponse TEXT,
                         bonne bool default false,
                        PRIMARY KEY (id_reponse,id_question)
);

-- =======================================
-- Table : Appareil
-- =======================================
CREATE TABLE Appareil (
                          id_appareil SERIAL PRIMARY KEY,
                          type VARCHAR(100) NOT NULL,
                          consommation_watts NUMERIC(10,2) CHECK (consommation_watts >= 0),
                          emission_moyenne_co2 NUMERIC(10,2) CHECK (emission_moyenne_co2 >= 0)
);

-- =======================================
-- Table : ActivitéNumérique
-- =======================================
CREATE TABLE ActiviteNumerique (
                                   id_activite SERIAL PRIMARY KEY,
                                   nom VARCHAR(150) NOT NULL,
                                   description TEXT,
                                   consommation_moyenne NUMERIC(10,2) CHECK (consommation_moyenne >= 0),
                                   emission_moyenne_co2 NUMERIC(10,2) CHECK (emission_moyenne_co2 >= 0)
);

-- =======================================
-- Table : Conseil
-- =======================================
CREATE TABLE Conseil (
                         id_conseil SERIAL PRIMARY KEY,
                         titre VARCHAR(255) NOT NULL,
                         description TEXT,
                         type type_conseil DEFAULT 'autre'
);

-- =======================================
-- Tables d’association N–N
-- =======================================

-- Appareils d’un utilisateur
CREATE TABLE AppareilUtilisateur (
                                     id_utilisateur INT NOT NULL REFERENCES Utilisateur(id_utilisateur) ON DELETE CASCADE,
                                     id_appareil INT NOT NULL REFERENCES Appareil(id_appareil) ON DELETE CASCADE,
                                     PRIMARY KEY (id_utilisateur, id_appareil)
);

-- Activités d’un utilisateur
CREATE TABLE ActiviteNumeriqueUtilisateur (
                                              id_utilisateur INT NOT NULL REFERENCES Utilisateur(id_utilisateur) ON DELETE CASCADE,
                                              id_activite INT NOT NULL REFERENCES ActiviteNumerique(id_activite) ON DELETE CASCADE,
                                              PRIMARY KEY (id_utilisateur, id_activite)
);

-- Conseils liés à un appareil
CREATE TABLE ConseilAppareil (
                                 id_appareil INT NOT NULL REFERENCES Appareil(id_appareil) ON DELETE CASCADE,
                                 id_conseil INT NOT NULL REFERENCES Conseil(id_conseil) ON DELETE CASCADE,
                                 PRIMARY KEY (id_appareil, id_conseil)
);

-- Conseils liés à une activité numérique
CREATE TABLE ConseilActiviteNumerique (
                                          id_activite INT NOT NULL REFERENCES ActiviteNumerique(id_activite) ON DELETE CASCADE,
                                          id_conseil INT NOT NULL REFERENCES Conseil(id_conseil) ON DELETE CASCADE,
                                          PRIMARY KEY (id_activite, id_conseil)
);

-- Table : QuestionnaireQuestion
CREATE TABLE QuestionnaireQuestion (
                                       id_questionnaire INT NOT NULL REFERENCES Questionnaire(id_questionnaire) ON DELETE CASCADE,
                                       id_question INT NOT NULL REFERENCES Question(id_question) ON DELETE CASCADE,
                                       ordre INT NOT NULL,
                                       PRIMARY KEY (id_questionnaire, id_question)
);

-- =======================================
-- Table : Utilisation
-- =======================================
CREATE TABLE Utilisation (
                             id_utilisation SERIAL PRIMARY KEY,
                             id_utilisateur INT NOT NULL REFERENCES Utilisateur(id_utilisateur) ON DELETE CASCADE,
                             id_activite INT REFERENCES ActiviteNumerique(id_activite) ON DELETE SET NULL,
                             id_appareil INT REFERENCES Appareil(id_appareil) ON DELETE SET NULL,
                             duree_minutes INT CHECK (duree_minutes >= 0),
                             date_utilisation DATE DEFAULT CURRENT_DATE
);