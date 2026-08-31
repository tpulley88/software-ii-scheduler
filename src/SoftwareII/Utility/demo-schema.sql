CREATE TABLE countries (
  Country_ID INT PRIMARY KEY,
  Country VARCHAR(50) NOT NULL,
  Create_Date TIMESTAMP,
  Created_By VARCHAR(50),
  Last_Update TIMESTAMP,
  Last_Updated_By VARCHAR(50)
);

CREATE TABLE first_level_divisions (
  Division_ID INT PRIMARY KEY,
  Division VARCHAR(100) NOT NULL,
  Create_Date TIMESTAMP,
  Created_By VARCHAR(50),
  Last_Update TIMESTAMP,
  Last_Updated_By VARCHAR(50),
  Country_ID INT NOT NULL
);

CREATE TABLE customers (
  Customer_ID INT AUTO_INCREMENT PRIMARY KEY,
  Customer_Name VARCHAR(100) NOT NULL,
  Address VARCHAR(150),
  Postal_Code VARCHAR(20),
  Phone VARCHAR(30),
  Create_Date TIMESTAMP,
  Created_By VARCHAR(50),
  Last_Update TIMESTAMP,
  Last_Updated_By VARCHAR(50),
  Division_ID INT NOT NULL
);

CREATE TABLE users (
  User_ID INT PRIMARY KEY,
  User_Name VARCHAR(50) NOT NULL,
  Password VARCHAR(50) NOT NULL
);

CREATE TABLE contacts (
  Contact_ID INT PRIMARY KEY,
  Contact_Name VARCHAR(100) NOT NULL,
  Email VARCHAR(100) NOT NULL
);

CREATE TABLE appointments (
  Appointment_ID INT AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(100),
  Description VARCHAR(255),
  Location VARCHAR(100),
  Type VARCHAR(50),
  Start TIMESTAMP,
  End TIMESTAMP,
  Create_Date TIMESTAMP,
  Created_By VARCHAR(50),
  Last_Update TIMESTAMP,
  Last_Updated_By VARCHAR(50),
  Customer_ID INT,
  User_ID INT,
  Contact_ID INT
);

INSERT INTO countries VALUES (1, 'United States', CURRENT_TIMESTAMP, 'demo', CURRENT_TIMESTAMP, 'demo');
INSERT INTO first_level_divisions VALUES (1, 'Virginia', CURRENT_TIMESTAMP, 'demo', CURRENT_TIMESTAMP, 'demo', 1);
INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)
VALUES ('Sample Client', '100 Demo Avenue', '00000', '555-0100', CURRENT_TIMESTAMP, 'demo', CURRENT_TIMESTAMP, 'demo', 1);
INSERT INTO users VALUES (1, 'demo', 'demo');
INSERT INTO contacts VALUES (1, 'Jordan Lee', 'jordan@example.test');
INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)
VALUES ('Portfolio Walkthrough', 'Fictional demonstration appointment', 'Remote', 'Demo', CURRENT_TIMESTAMP, DATEADD('HOUR', 1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 'demo', CURRENT_TIMESTAMP, 'demo', 1, 1, 1);
