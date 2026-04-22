


## 🗄️ Database DDL

### 📋 Patient Table

```sql
CREATE TABLE IF NOT EXISTS master.patient (
    id BIGINT NOT NULL DEFAULT nextval('master.patient_id_seq'::regclass),
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    zip_code VARCHAR(20),
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT patient_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS master.patient
    OWNER TO postgres;
```


### 📋 Patient id Sequence
```sql
CREATE SEQUENCE IF NOT EXISTS master.patient_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE master.patient_id_seq
    OWNED BY master.patient.id;

ALTER SEQUENCE master.patient_id_seq
    OWNER TO postgres;
```
