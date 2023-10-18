FROM vault:1.9.1
COPY ./config.hcl /vault/config.hcl
ENTRYPOINT ["vault", "server", "-config=/vault/config.hcl"]
