--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2
-- Dumped by pg_dump version 13.2

-- Started on 2024-05-03 13:17:53

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3017 (class 1262 OID 17118)
-- Name: leap; Type: DATABASE; Schema: -; Owner: master
--

CREATE DATABASE leap WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Hindi_India.utf8';


ALTER DATABASE leap OWNER TO master;

\connect leap

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 17121)
-- Name: lp; Type: SCHEMA; Schema: -; Owner: master
--

CREATE SCHEMA lp;


ALTER SCHEMA lp OWNER TO master;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 17124)
-- Name: mst_co_setup; Type: TABLE; Schema: lp; Owner: master
--

CREATE TABLE lp.mst_co_setup (
    co_int_code integer NOT NULL,
    co_name character varying(30),
    co_address character varying(50),
    co_address_1 character varying(50),
    co_city character varying(50),
    co_state character varying(30),
    co_pin integer,
    co_logo_path character varying(100),
    co_bg_image_path character varying(100)
);


ALTER TABLE lp.mst_co_setup OWNER TO master;

--
-- TOC entry 202 (class 1259 OID 17122)
-- Name: co_setup_co_int_code_seq; Type: SEQUENCE; Schema: lp; Owner: master
--

CREATE SEQUENCE lp.co_setup_co_int_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lp.co_setup_co_int_code_seq OWNER TO master;

--
-- TOC entry 3018 (class 0 OID 0)
-- Dependencies: 202
-- Name: co_setup_co_int_code_seq; Type: SEQUENCE OWNED BY; Schema: lp; Owner: master
--

ALTER SEQUENCE lp.co_setup_co_int_code_seq OWNED BY lp.mst_co_setup.co_int_code;


--
-- TOC entry 205 (class 1259 OID 17164)
-- Name: employee_master; Type: TABLE; Schema: lp; Owner: master
--

CREATE TABLE lp.employee_master (
    emp_int_code integer NOT NULL,
    emp_code character varying(50),
    emp_name character varying(70),
    email_id character varying(50),
    phone_no character varying(11),
    birth_place character varying(40),
    address character varying(100),
    status character varying(10),
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    rpt_mgr character varying,
    v_pwd character varying(20) NOT NULL,
    crypt_pwd character varying(100) NOT NULL,
    user_type character varying
);


ALTER TABLE lp.employee_master OWNER TO master;

--
-- TOC entry 204 (class 1259 OID 17162)
-- Name: employee_master_emp_int_code_seq; Type: SEQUENCE; Schema: lp; Owner: master
--

CREATE SEQUENCE lp.employee_master_emp_int_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lp.employee_master_emp_int_code_seq OWNER TO master;

--
-- TOC entry 3019 (class 0 OID 0)
-- Dependencies: 204
-- Name: employee_master_emp_int_code_seq; Type: SEQUENCE OWNED BY; Schema: lp; Owner: master
--

ALTER SEQUENCE lp.employee_master_emp_int_code_seq OWNED BY lp.employee_master.emp_int_code;


--
-- TOC entry 207 (class 1259 OID 17178)
-- Name: menu_details; Type: TABLE; Schema: lp; Owner: master
--

CREATE TABLE lp.menu_details (
    menu_int_code integer NOT NULL,
    user_type character varying,
    menu_code integer,
    menu_display_name character varying,
    menu_desc character varying,
    menu_icon character varying,
    menu_index integer,
    menu_href character varying,
    menu_level integer,
    ref_menu_int_code integer
);


ALTER TABLE lp.menu_details OWNER TO master;

--
-- TOC entry 206 (class 1259 OID 17176)
-- Name: menu_details_meanu_int_code_seq; Type: SEQUENCE; Schema: lp; Owner: master
--

CREATE SEQUENCE lp.menu_details_meanu_int_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lp.menu_details_meanu_int_code_seq OWNER TO master;

--
-- TOC entry 3020 (class 0 OID 0)
-- Dependencies: 206
-- Name: menu_details_meanu_int_code_seq; Type: SEQUENCE OWNED BY; Schema: lp; Owner: master
--

ALTER SEQUENCE lp.menu_details_meanu_int_code_seq OWNED BY lp.menu_details.menu_int_code;


--
-- TOC entry 2867 (class 2604 OID 17167)
-- Name: employee_master emp_int_code; Type: DEFAULT; Schema: lp; Owner: master
--

ALTER TABLE ONLY lp.employee_master ALTER COLUMN emp_int_code SET DEFAULT nextval('lp.employee_master_emp_int_code_seq'::regclass);


--
-- TOC entry 2869 (class 2604 OID 17181)
-- Name: menu_details menu_int_code; Type: DEFAULT; Schema: lp; Owner: master
--

ALTER TABLE ONLY lp.menu_details ALTER COLUMN menu_int_code SET DEFAULT nextval('lp.menu_details_meanu_int_code_seq'::regclass);


--
-- TOC entry 2866 (class 2604 OID 17127)
-- Name: mst_co_setup co_int_code; Type: DEFAULT; Schema: lp; Owner: master
--

ALTER TABLE ONLY lp.mst_co_setup ALTER COLUMN co_int_code SET DEFAULT nextval('lp.co_setup_co_int_code_seq'::regclass);


--
-- TOC entry 3009 (class 0 OID 17164)
-- Dependencies: 205
-- Data for Name: employee_master; Type: TABLE DATA; Schema: lp; Owner: master
--

INSERT INTO lp.employee_master VALUES (1, 'abc', 'test', NULL, NULL, NULL, NULL, NULL, '2024-04-27 11:38:18.916384', NULL, 'abc', '$2a$04$qc7r40bzzqzmBNyItgfDNuJJrE6gBSOK1CJt3EHEkaFlfYMhwT83S', 'ADMIN');


--
-- TOC entry 3011 (class 0 OID 17178)
-- Dependencies: 207
-- Data for Name: menu_details; Type: TABLE DATA; Schema: lp; Owner: master
--

INSERT INTO lp.menu_details VALUES (1, 'ADMIN', 100, 'Masters', 'Masters', 'fa-icon', 1, '/masters', 0, 0);
INSERT INTO lp.menu_details VALUES (2, 'ADMIN', 101, 'Transaction', 'Transaction', 'fa-icon', 2, '/transactions', 0, 0);
INSERT INTO lp.menu_details VALUES (3, 'ADMIN', 102, 'Setup', 'Setup', 'fa-icon', 3, '/setup', 0, 0);
INSERT INTO lp.menu_details VALUES (5, 'ADMIN', 104, 'Masters', 'Setup Masters', 'fa-icon', 1, '/master_setup', 1, 3);
INSERT INTO lp.menu_details VALUES (6, 'ADMIN', 105, 'Company Setup', 'Setup Company Details', 'fa-icon', 1, '/co_setup', 1, 5);
INSERT INTO lp.menu_details VALUES (4, 'ADMIN', 103, 'User Master', 'Maintain user related details', 'fa-icon', 1, '/user_master', 1, 1);
INSERT INTO lp.menu_details VALUES (7, 'ADMIN', 106, 'Bill To Master', 'Billing To Master', 'fa-icon', 2, '/btl', 1, 1);


--
-- TOC entry 3007 (class 0 OID 17124)
-- Dependencies: 203
-- Data for Name: mst_co_setup; Type: TABLE DATA; Schema: lp; Owner: master
--

INSERT INTO lp.mst_co_setup VALUES (2, 'abc', NULL, NULL, NULL, NULL, NULL, 'https://weikfield.com/wp-content/uploads/2023/03/weikfield-1.png', 'https://pixabay.com/photos/sunset-tree-water-silhouette-1373171/');


--
-- TOC entry 3021 (class 0 OID 0)
-- Dependencies: 202
-- Name: co_setup_co_int_code_seq; Type: SEQUENCE SET; Schema: lp; Owner: master
--

SELECT pg_catalog.setval('lp.co_setup_co_int_code_seq', 2, true);


--
-- TOC entry 3022 (class 0 OID 0)
-- Dependencies: 204
-- Name: employee_master_emp_int_code_seq; Type: SEQUENCE SET; Schema: lp; Owner: master
--

SELECT pg_catalog.setval('lp.employee_master_emp_int_code_seq', 1, true);


--
-- TOC entry 3023 (class 0 OID 0)
-- Dependencies: 206
-- Name: menu_details_meanu_int_code_seq; Type: SEQUENCE SET; Schema: lp; Owner: master
--

SELECT pg_catalog.setval('lp.menu_details_meanu_int_code_seq', 7, true);


--
-- TOC entry 2871 (class 2606 OID 17129)
-- Name: mst_co_setup co_setup_pkey; Type: CONSTRAINT; Schema: lp; Owner: master
--

ALTER TABLE ONLY lp.mst_co_setup
    ADD CONSTRAINT co_setup_pkey PRIMARY KEY (co_int_code);


--
-- TOC entry 2875 (class 2606 OID 17173)
-- Name: employee_master employee_master_un; Type: CONSTRAINT; Schema: lp; Owner: master
--

ALTER TABLE ONLY lp.employee_master
    ADD CONSTRAINT employee_master_un UNIQUE (emp_code);


--
-- TOC entry 2872 (class 1259 OID 17174)
-- Name: employee_master_emp_code_idx; Type: INDEX; Schema: lp; Owner: master
--

CREATE INDEX employee_master_emp_code_idx ON lp.employee_master USING btree (emp_code);


--
-- TOC entry 2873 (class 1259 OID 17175)
-- Name: employee_master_status_idx; Type: INDEX; Schema: lp; Owner: master
--

CREATE INDEX employee_master_status_idx ON lp.employee_master USING btree (status);


-- Completed on 2024-05-03 13:17:53

--
-- PostgreSQL database dump complete
--

