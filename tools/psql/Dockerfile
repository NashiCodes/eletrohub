FROM postgres:14.8

# Instalar dependências para compilar a extensão vector
RUN apt-get update && apt-get install -y \
    git \
    build-essential \
    postgresql-server-dev-14 \
    locales

RUN rm -rf /usr/local/src/pgvector

RUN sed -i -e 's/# en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen && \
    locale-gen

# Clonar e compilar a extensão vector
RUN git clone https://github.com/pgvector/pgvector.git /usr/local/src/pgvector \
    && cd /usr/local/src/pgvector \
    && make && make install

# Limpar pacotes desnecessários após a instalação
RUN apt-get remove -y git build-essential \
    && apt-get autoremove -y \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
