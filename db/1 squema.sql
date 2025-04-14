
CREATE TABLE gender (
    id INTEGER NOT NULL
    , name VARCHAR(200) NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name)
);

CREATE TABLE marital_status (
    id INTEGER NOT NULL
    , name VARCHAR(200) NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name)
);

CREATE TABLE contract_type (
    id INTEGER NOT NULL
    , name VARCHAR(200) NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name)
);

CREATE TABLE country (
    id INTEGER NOT NULL
    , name VARCHAR(200) NOT NULL
	, nationality VARCHAR(200) NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name)
);
CREATE TABLE region (
    id INTEGER NOT NULL
    , country_id INTEGER NOT NULL
	, name VARCHAR(200) NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name)
	, FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE city (
    id INTEGER NOT NULL
    , region_id INTEGER NOT NULL
	, name VARCHAR(200) NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name)
    , FOREIGN KEY (region_id) REFERENCES region(id)
);

CREATE TABLE commune (
    id INTEGER NOT NULL
	, city_id INTEGER NOT NULL
    , name VARCHAR(200) NOT NULL
    , sii_code VARCHAR(10) NULL
    , tgr_code VARCHAR(10) NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(sii_code)
	, UNIQUE(tgr_code)
	, UNIQUE(name)
    , FOREIGN KEY (city_id) REFERENCES city(id)
);

CREATE TABLE company (
    id SERIAL NOT NULL
	, commune_id INTEGER NOT NULL

    , code VARCHAR(20) NOT NULL
    , name VARCHAR(200) NOT NULL
    , address VARCHAR(500) NOT NULL
	, phone VARCHAR(20) NULL
    , alias VARCHAR(200) NULL
    , giro VARCHAR(500) NULL
    , email VARCHAR(500) NULL

    , geolocation BOOLEAN NOT NULL
    , selfie BOOLEAN NOT NULL
    , lag BOOLEAN NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(code)
	, FOREIGN KEY (commune_id) REFERENCES commune(id)
);

CREATE TABLE branch (
    id SERIAL NOT NULL
	, company_id INTEGER NOT NULL

	, name VARCHAR(200) NOT NULL
    , address VARCHAR(500) NOT NULL
	, phone VARCHAR(20) NULL
    , alias VARCHAR(200) NULL
    , active BOOLEAN  NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(name, company_id)
	, FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE job_type (
	id SERIAL  NOT NULL

	, "name" varchar(100) NOT NULL
	, company_id INTEGER NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
 	, UNIQUE(name, company_id)
 	, FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE job_scheduler (
	id SERIAL NOT NULL
	, name VARCHAR(500) NOT NULL
	, monday BOOLEAN NOT NULL
	, monday_from time NULL
	, monday_to time NULL

	, tuesday  BOOLEAN NOT NULL
	, tuesday_from time NULL
	, tuesday_to time NULL

	, wednesday  BOOLEAN NOT NULL
	, wednesday_from time NULL
	, wednesday_to time NULL

	, thursday  BOOLEAN NOT NULL
	, thursday_from time NULL
	, thursday_to time NULL

	, friday  BOOLEAN NOT NULL
	, friday_from time NULL
	, friday_to time NULL

	, saturday  BOOLEAN NOT NULL
	, saturday_from time NULL
	, saturday_to time NULL

	, sunday  BOOLEAN NOT NULL
	, sunday_from time NULL
	, sunday_to time NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
 	, UNIQUE(name)
);

CREATE TABLE employee (
    id SERIAL NOT NULL
    , UUID VARCHAR(50) NOT NULL
    , branch_id INTEGER NOT NULL
	, job_scheduler_id INTEGER NOT NULL
    , commune_id INTEGER NOT NULL
    , nationality_id INTEGER NOT NULL
	, gender_id INTEGER NOT NULL
	, marital_status_id INTEGER NOT NULL
	, contract_type_id INTEGER NULL
    , job_type_id INTEGER NOT NULL
	, code VARCHAR(20) NOT NULL
    , name VARCHAR(500) NOT NULL
    , last_name VARCHAR(200) NOT NULL
    , mother_last_name VARCHAR(200) NULL

	, birth_date DATE NULL
	, phone VARCHAR(20) NULL
    , email VARCHAR(500) NULL
    , address VARCHAR(500) NOT NULL

	, contract_date DATE NULL
	, contract_end_date DATE NULL

	, active BOOLEAN NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
	, UNIQUE(code)
	, FOREIGN KEY (branch_id) REFERENCES branch(id)
	, FOREIGN KEY (job_scheduler_id) REFERENCES job_scheduler(id)
    , FOREIGN KEY (nationality_id) REFERENCES country(id)
	, FOREIGN KEY (commune_id) REFERENCES commune(id)
	, FOREIGN KEY (gender_id) REFERENCES gender(id)
	, FOREIGN KEY (marital_status_id) REFERENCES marital_status(id)
	, FOREIGN KEY (contract_type_id) REFERENCES contract_type(id)
	, FOREIGN KEY (job_type_id) REFERENCES job_type(id)
);

CREATE TABLE register (
	id SERIAL  NOT NULL
	, employee_id INTEGER NOT NULL
	,start_of_the_day time NOT null
	, entry time null
	, exit time null
	, break_time time  null
	, back_to_work time  null
	, day date NOT NULL

 	, PRIMARY KEY (id)
 	, UNIQUE(id)
 	, FOREIGN KEY (employee_id) REFERENCES employee(id)
);




