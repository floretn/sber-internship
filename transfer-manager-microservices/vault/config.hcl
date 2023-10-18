backend "file" {
path = "./vault/vault-conf/data"
}

listener "tcp" {
address = "vault:8200"
tls_disable = 1
scheme="http"
}

disable_mlock = true

api_addr = "http://vault:8200"
cluster_addr = "https://vault:8201"
