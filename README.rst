HrStatus: Change de Horário de Verão?!?!
#########################################
---
author: spolti
badges:
  build:
    alt: build status
    title: check build status
    img: https://travis-ci.org/hrstatus/hrstatus.svg?branch=master
    url: https://travis-ci.org/hrstatus/hrstatus
github: hrstatus/hrstatus
ghribbon: black-upright
date: 2012-08-26T15:08:27+02:00
description: Seu braço direito durante as viradas do horário de verão
draft: false
keywords:
- hrstatus
- static
- site
tags:
- go
- generator
- site
title: HrStatus
topics:
- project
---

HrStatus foi desenvolvido inicialmente para prover suporte confiável e em grande
escala durante as atualizações de data/hora do horário de verão. Com ele é possível realizar a verificação de data e hora em todos os
servidores cadastrados em seu banco de dados independentemente do sistema operacional, e
também, a partir da versão 3.0 obter o timestamp dos banco de dados Oracle, PostgreSQL,
Mysql e DB2 de seu ambiente, contando também com a funcionalidade de atualizar a hora
automaticamente (função disponível somente para servidores Linux), tornando a migração rápida e
confiável. 

.. class:: no-web

    .. image:: https://github.com/hrstatus/hrstatus/blob/master/hr.png
        :alt: HTTPie compared to cURL
        :width: 50%
        :align: center

Requisitos:
###########

- Java 8+
- Maven 3+
- WildFly 10+
- Banco de Dados
