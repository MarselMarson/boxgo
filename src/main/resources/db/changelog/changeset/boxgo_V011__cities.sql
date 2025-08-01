CREATE TABLE country (
                         id INTEGER PRIMARY KEY,
                         urlFlag VARCHAR(50)
);

CREATE TABLE country_translation (
                                     countryId INTEGER REFERENCES country(id),
                                     languageCode VARCHAR(10),
                                     translation VARCHAR(100),
                                     PRIMARY KEY (countryId, languageCode)
);

CREATE TABLE city (
                      id INTEGER PRIMARY KEY,
                      timezone VARCHAR(50),
                      priority INTEGER,
                      latLng VARCHAR(50),
                      countryId INTEGER REFERENCES country(id)
);

CREATE TABLE city_translation (
                                  id INTEGER,
                                  languageCode VARCHAR(10),
                                  name VARCHAR(100),
                                  PRIMARY KEY (id, languageCode)
);