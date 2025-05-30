-- liquibase formatted sql

-- changeSet kostusonline:0c62e892-6f60-5a52-b3f6-2c25ed116452 runOnChange:true
DELETE FROM "account";

-- changeSet kostusonline:6234f4cb-110c-5aa4-b4ed-20178ab4f3a9 runOnChange:true
ALTER TABLE "account"
ADD COLUMN "balance_autolimit" DECIMAL NOT NULL DEFAULT 0 CHECK ("balance_autolimit" >= 0);

-- changeSet kostusonline:c541ebb5-7fa0-56d1-bdfc-baaaf3cf3ba7 runOnChange:true
INSERT INTO
    "account" ("user_id", "balance", "balance_autolimit")
SELECT u.id, 10.0, 20.7 -- 207%
FROM "user" u;
