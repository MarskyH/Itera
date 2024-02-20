--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2024-02-17 09:14:57

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
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 4850 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16485)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 16495)
-- Name: foods; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.foods (
    id integer NOT NULL,
    title text NOT NULL,
    price integer NOT NULL,
    image text
);


--
-- TOC entry 216 (class 1259 OID 16494)
-- Name: foods_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.foods_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4851 (class 0 OID 0)
-- Dependencies: 216
-- Name: foods_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.foods_id_seq OWNED BY public.foods.id;


--
-- TOC entry 4693 (class 2604 OID 16498)
-- Name: foods id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.foods ALTER COLUMN id SET DEFAULT nextval('public.foods_id_seq'::regclass);


--
-- TOC entry 4842 (class 0 OID 16485)
-- Dependencies: 215
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.flyway_schema_history VALUES (1, '1', 'create-table-foods', 'SQL', 'V1__create-table-foods.sql', 1458108831, 'mars', '2024-02-13 21:52:03.26002', 33, true);


--
-- TOC entry 4844 (class 0 OID 16495)
-- Dependencies: 217
-- Data for Name: foods; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.foods VALUES (1, 'Pizza', 25, 'https://blog.milium.com.br/wp-content/uploads/2023/07/Pizza-conteudo-940x420-2.jpg');
INSERT INTO public.foods VALUES (2, 'Pizza', 25, 'https://blog.milium.com.br/wp-content/uploads/2023/07/Pizza-conteudo-940x420-2.jpg');
INSERT INTO public.foods VALUES (3, 'Pizza', 25, 'https://blog.milium.com.br/wp-content/uploads/2023/07/Pizza-conteudo-940x420-2.jpg');


--
-- TOC entry 4852 (class 0 OID 0)
-- Dependencies: 216
-- Name: foods_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.foods_id_seq', 3, true);


--
-- TOC entry 4695 (class 2606 OID 16492)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 4698 (class 2606 OID 16502)
-- Name: foods foods_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.foods
    ADD CONSTRAINT foods_pkey PRIMARY KEY (id);


--
-- TOC entry 4696 (class 1259 OID 16493)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


-- Completed on 2024-02-17 09:14:57

--
-- PostgreSQL database dump complete
--

