# banking
Training

Usecase

1) Customer Registration and account creation
2) Fund transfer
3) Statement (Month-yeas ex: Jan-2021)

git clone https://github.com/cktheni/banking.git

drop table customer_account_cnf_setup;
drop table customer;
drop table customer_account;
drop table transfers_transaction_details;


Auto Generate Customer Rel Number / Account Setup
===================================================
ID					- P
COUNTRY_CODE		 
RELATIONSHIP_NO		 
ACCOUNT_NO			 
ACCOUNT_CURRENCY	 


CREATE TABLE `banking`.`customer_account_cnf_setup` (
  `CUST_ID` int  NOT NULL AUTO_INCREMENT,
  `COUNTRY_CODE` VARCHAR(3) NOT NULL,
  `RELATIONSHIP_NO` VARCHAR(20) NOT NULL,
  `ACCOUNT_NO` VARCHAR(20) NOT NULL,
  `ACCOUNT_CURRENCY` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`CUST_ID`));
  
Customer Master
================
  
COUNTRY_CODE		- P
RELATIONSHIP_NO		- P
FIRST_NAME
LAST_NAME
MOBILE_NO
EMAIL_ID
DATE_OF_BIRTH
REGISTRATION_DATE
CUSTOMER_ADDRESS
CREATED_BY
CREATED_DATE
UPDATED_BY
UPDATED_DATE


  CREATE TABLE `banking`.`customer` (
  `COUNTRY_CODE` VARCHAR(3) NOT NULL,
  `RELATIONSHIP_NO` VARCHAR(20) NOT NULL,
  `FIRST_NAME` VARCHAR(50) NOT NULL,
  `LAST_NAME` VARCHAR(50) NULL,
  `MOBILE_NO` VARCHAR(20) NOT NULL,
  `EMAIL_ID` VARCHAR(20) NULL,
  `DATE_OF_BIRTH` DATETIME NOT NULL,
  `REGISTRATION_DATE` DATETIME NOT NULL,
  `CUSTOMER_ADDRESS` VARCHAR(200) NOT NULL,
  `CREATED_BY` VARCHAR(50) NOT NULL,
  `CREATED_DATE` DATETIME NOT NULL,
  `UPDATED_BY` VARCHAR(50) NULL,
  `UPDATED_DATE` DATETIME NULL,
  PRIMARY KEY (`COUNTRY_CODE`, `RELATIONSHIP_NO`));
  
 Customer_Account
 =================
 
COUNTRY_CODE			P
RELATIONSHIP_NO			P
ACCOUNT_NO				P
ACCOUNT_CURRENCY		P
BRANCH
AVAILABLE_BALANCE
CREATED_BY
CREATED_DATE
UPDATED_BY
UPDATED_DATE

 
  CREATE TABLE `banking`.`customer_account` (
  `COUNTRY_CODE` VARCHAR(3) NOT NULL,
  `RELATIONSHIP_NO` VARCHAR(20) NOT NULL,
  `MOBILE_NO` VARCHAR(20) NOT NULL,
  `ACCOUNT_NO` VARCHAR(20) NOT NULL,
  `ACCOUNT_CURRENCY` VARCHAR(20) NOT NULL,
  `BRANCH` VARCHAR(45) NOT NULL,
  `AVAILABLE_BALANCE` DOUBLE NULL,
  `CREATED_BY` VARCHAR(50) NOT NULL,
  `CREATED_DATE` DATETIME NOT NULL,
  `UPDATED_BY` VARCHAR(50) NULL,
  `UPDATED_DATE` DATETIME NULL,
  PRIMARY KEY (`COUNTRY_CODE`, `RELATIONSHIP_NO`, `ACCOUNT_NO`, `ACCOUNT_CURRENCY`));
  
  
TRANSFERS_TRANSACTION_DETAILS
==============================

COUNTRY_CODE					P
RELATIONSHIP_NO
TRANSACTION_ID					P
MOBILE_NO
FROM_ACCOUNT_NUMBER
FROM_ACCOUNT_CURRENCY
TO_ACCOUNT_NUMBER
TO_ACCOUNT_CURRENCY
TO_ACCOUNT_NAME
TRANSACTION_AMOUNT
TRANSACTION_STATUS
TRANSACTION_DATE
REFERENCE_NO
CREATED_BY
CREATED_DATE
UPDATED_BY
UPDATED_DATE


 CREATE TABLE `banking`.`transfers_transaction_details` (
  `COUNTRY_CODE` VARCHAR(3) NOT NULL,
  `RELATIONSHIP_NO` VARCHAR(20) NOT NULL,
  `TRANSACTION_ID` VARCHAR(300) NOT NULL,
  `MOBILE_NO` VARCHAR(15) NOT NULL,
  `FROM_ACCOUNT_NUMBER` VARCHAR(15) NOT NULL,
  `FROM_ACCOUNT_CURRENCY` VARCHAR(15) NOT NULL,
  `TO_ACCOUNT_NUMBER` VARCHAR(15) NOT NULL,
  `TO_ACCOUNT_CURRENCY` VARCHAR(15) NOT NULL,
  `TO_ACCOUNT_NAME` VARCHAR(100) NOT NULL,
  `TRANSACTION_AMOUNT` DOUBLE NOT NULL,
  `TRANSACTION_DATE` DATETIME NOT NULL,
  `REFERENCE_NO` VARCHAR(500) NOT NULL,
   `TRANSACTION_TYPE` VARCHAR(20) NOT NULL,
  `CREATED_BY` VARCHAR(50) NOT NULL,
  `CREATED_DATE` DATETIME NOT NULL,
  `UPDATED_BY` VARCHAR(50) NULL,
  `UPDATED_DATE` DATETIME NULL,
  PRIMARY KEY (`COUNTRY_CODE`, `TRANSACTION_ID`));
  
  
 Register
 =======
  
//1
{
  "countryCode": "IND",
  "firstName": "Chandru",
  "lastName": "Mahalingam",
  "mobileNumber": "9677152375",
  "emailId": "cktheni@gmail.com",
  "dateOfBirth": "1975-29-09",
  "registrationDate": "2021-01-17T12:22:12.864Z",
  "customerAddress": "Chennai, Urapakkam",
  "customerAccount": {
   "currencyCode": "RS",
    "branch": "Chennai",
    "availableBalance": 1000
  }
}

//2
{
  "countryCode": "IND",
  "firstName": "Siva",
  "lastName": "Sankaran",
  "mobileNumber": "9940372228",
  "emailId": "siva@gmail.com",
  "dateOfBirth": "1992-23-02",
  "registrationDate": "2021-01-17T12:22:12.864Z",
  "customerAddress": "Chennai, Urapakkam",
   
  "customerAccount": {
 	"currencyCode": "RS",
    "branch": "Chennai",
    "availableBalance": 1500
    }
}


TRANSFERS
=========
//1
{
  "countryCode": "IND",
  "relationshipNo": "10000",
  "mobileNumber": "9677152375",
  "fromAccountNumber": "100000",
  "fromAccountCurrency": "RS",
  "toAccountNumber": "100001",
  "toAccountCurrency": "RS",
  "toCustomerName": "Siva",
  "transactionAmount": 200,
  "transactionType": "D"
}

//2
  {
  "countryCode": "IND",
  "relationshipNo": "10001",
  "mobileNumber": "9940372228",
  "fromAccountNumber": "100001",
  "fromAccountCurrency": "RS",
  "toAccountNumber": "100000",
  "toAccountCurrency": "RS",
  "toCustomerName": "Chandru",
  "transactionAmount": 100,
  "transactionType": "D"
}
