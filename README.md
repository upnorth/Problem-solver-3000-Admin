# Problem-solver-3000
Student project for support-management in object oriented Java with JFrame GUI and Derby DB

Script for Derby DB setup (project is programmed for: database name=ps3, user=ps3, password=ps3):

CREATE TABLE "PROBLEM"
(    
   "ID" INT not null primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),   
   "CLIENT" INTEGER,
   "MANAGER" INTEGER NOT NULL,     
   "REGISTERED" TIMESTAMP NOT NULL,
   "CATEGORY" VARCHAR(20),
   "PRIORITY" INTEGER,  
   "COMMENTS" VARCHAR(500),
   "STATUS" BOOLEAN      
)

CREATE TABLE "TASK"
(    
   "ID" INT not null primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),   
   "PROBLEM_ID" INTEGER,
   "CATEGORY" VARCHAR(20),    
   "TECH_ID" INTEGER,
   "ESTTIME" INTEGER,
   "USEDTIME" INTEGER,
   "PRIORITY" INTEGER,  
   "COMMENTS" VARCHAR(500),
   "STATUS" BOOLEAN      
);
