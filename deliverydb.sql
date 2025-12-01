--
-- PostgreSQL database dump
--

-- Dumped from database version 10.10
-- Dumped by pg_dump version 16.2

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
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--



ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

--
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    cat_id integer NOT NULL,
    cat_nome character varying(20)
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- Name: categoria_cat_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categoria_cat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categoria_cat_id_seq OWNER TO postgres;

--
-- Name: categoria_cat_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categoria_cat_id_seq OWNED BY public.categoria.cat_id;


--
-- Name: item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item (
    pro_id integer NOT NULL,
    ped_id integer NOT NULL,
    it_quant integer,
    it_valor numeric(10,2)
);


ALTER TABLE public.item OWNER TO postgres;

--
-- Name: marca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.marca (
    mar_id integer NOT NULL,
    mar_nome character varying(20)
);


ALTER TABLE public.marca OWNER TO postgres;

--
-- Name: marca_mar_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.marca_mar_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.marca_mar_id_seq OWNER TO postgres;

--
-- Name: marca_mar_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.marca_mar_id_seq OWNED BY public.marca.mar_id;


--
-- Name: pedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedido (
    ped_id integer NOT NULL,
    ped_data date,
    ped_clinome character varying(50),
    ped_clifone character varying(15),
    ped_total numeric(10,2),
    ped_entregue character(1),
    tpg_id integer,
    ped_local text,
    ped_numero_res character varying(10)
);


ALTER TABLE public.pedido OWNER TO postgres;

--
-- Name: pedido_ped_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pedido_ped_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pedido_ped_id_seq OWNER TO postgres;

--
-- Name: pedido_ped_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pedido_ped_id_seq OWNED BY public.pedido.ped_id;


--
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produto (
    pro_id integer NOT NULL,
    pro_nome character varying(30),
    pro_preco numeric(10,2),
    cat_id integer,
    pro_volume integer,
    mar_id integer
);


ALTER TABLE public.produto OWNER TO postgres;

--
-- Name: produto_pro_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.produto_pro_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.produto_pro_id_seq OWNER TO postgres;

--
-- Name: produto_pro_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.produto_pro_id_seq OWNED BY public.produto.pro_id;


--
-- Name: tipo_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_pagamento (
    tpg_id integer NOT NULL,
    tpg_nome character varying(20)
);


ALTER TABLE public.tipo_pagamento OWNER TO postgres;

--
-- Name: tipo_pagamento_tpg_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tipo_pagamento_tpg_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tipo_pagamento_tpg_id_seq OWNER TO postgres;

--
-- Name: tipo_pagamento_tpg_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tipo_pagamento_tpg_id_seq OWNED BY public.tipo_pagamento.tpg_id;


--
-- Name: categoria cat_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria ALTER COLUMN cat_id SET DEFAULT nextval('public.categoria_cat_id_seq'::regclass);


--
-- Name: marca mar_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marca ALTER COLUMN mar_id SET DEFAULT nextval('public.marca_mar_id_seq'::regclass);


--
-- Name: pedido ped_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido ALTER COLUMN ped_id SET DEFAULT nextval('public.pedido_ped_id_seq'::regclass);


--
-- Name: produto pro_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto ALTER COLUMN pro_id SET DEFAULT nextval('public.produto_pro_id_seq'::regclass);


--
-- Name: tipo_pagamento tpg_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_pagamento ALTER COLUMN tpg_id SET DEFAULT nextval('public.tipo_pagamento_tpg_id_seq'::regclass);


--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.categoria VALUES (1, 'refrigerante');
INSERT INTO public.categoria VALUES (2, 'cerveja');
INSERT INTO public.categoria VALUES (3, 'destilado');


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.item VALUES (4, 1, 1, 15.00);
INSERT INTO public.item VALUES (5, 1, 2, 10.00);


--
-- Data for Name: marca; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.marca VALUES (1, 'antarctica');
INSERT INTO public.marca VALUES (2, 'Heineken');
INSERT INTO public.marca VALUES (3, 'Askov');
INSERT INTO public.marca VALUES (4, 'Coca Cola');


--
-- Data for Name: pedido; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.pedido VALUES (1, '2025-10-15', 'Clayde Navarro', '18 99995-9688', 20.00, 'S', 1, 'Av Brasil, Vila Industrial, Presidente Prudente, SP', '2000');


--
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.produto VALUES (1, 'Vodka', 25.00, 3, 1000, 3);
INSERT INTO public.produto VALUES (2, 'Cerveja long neck', 8.50, 2, 350, 2);
INSERT INTO public.produto VALUES (3, 'Cerveja garrafa', 11.50, 2, 600, 1);
INSERT INTO public.produto VALUES (4, 'Cerveja garrafa', 15.00, 2, 600, 2);
INSERT INTO public.produto VALUES (5, 'Coca Lata lata', 5.00, 1, 350, 4);
INSERT INTO public.produto VALUES (6, 'Cerveja Lata', 8.00, 2, 350, 2);
INSERT INTO public.produto VALUES (7, 'Guaraná lata', 3.50, 1, 350, 1);


--
-- Data for Name: tipo_pagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_pagamento VALUES (1, 'PIX');
INSERT INTO public.tipo_pagamento VALUES (2, 'Dinheiro');
INSERT INTO public.tipo_pagamento VALUES (3, 'Cartão Débito');
INSERT INTO public.tipo_pagamento VALUES (4, 'Cartão Crédito');


--
-- Name: categoria_cat_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoria_cat_id_seq', 4, true);


--
-- Name: marca_mar_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.marca_mar_id_seq', 4, true);


--
-- Name: pedido_ped_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pedido_ped_id_seq', 1, true);


--
-- Name: produto_pro_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.produto_pro_id_seq', 7, true);


--
-- Name: tipo_pagamento_tpg_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tipo_pagamento_tpg_id_seq', 4, true);


--
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (cat_id);


--
-- Name: item item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_pkey PRIMARY KEY (pro_id, ped_id);


--
-- Name: marca marca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marca
    ADD CONSTRAINT marca_pkey PRIMARY KEY (mar_id);


--
-- Name: pedido pedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido
    ADD CONSTRAINT pedido_pkey PRIMARY KEY (ped_id);


--
-- Name: produto produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (pro_id);


--
-- Name: tipo_pagamento tipo_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_pagamento
    ADD CONSTRAINT tipo_pagamento_pkey PRIMARY KEY (tpg_id);


--
-- Name: item item_ped_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_ped_id_fkey FOREIGN KEY (ped_id) REFERENCES public.pedido(ped_id) NOT VALID;


--
-- Name: item item_pro_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_pro_id_fkey FOREIGN KEY (pro_id) REFERENCES public.produto(pro_id);


--
-- Name: pedido pedido_tpg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido
    ADD CONSTRAINT pedido_tpg_id_fkey FOREIGN KEY (tpg_id) REFERENCES public.tipo_pagamento(tpg_id) NOT VALID;


--
-- Name: produto produto_cat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_cat_id_fkey FOREIGN KEY (cat_id) REFERENCES public.categoria(cat_id);


--
-- Name: produto produto_mar_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_mar_id_fkey FOREIGN KEY (mar_id) REFERENCES public.marca(mar_id) NOT VALID;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

